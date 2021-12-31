package edu.uncc.midtermapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatePostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatePostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    OkHttpClient client=new OkHttpClient();
    public CreatePostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatePostFragment.
     */

    UserToken userToken;

    public  CreatePostFragment(UserToken userToken)
    {
        this.userToken=userToken;
    }
    // TODO: Rename and change types and number of parameters
    public static CreatePostFragment newInstance(String param1, String param2) {
        CreatePostFragment fragment = new CreatePostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public void addnewPost(String post, UserToken userToken){

        try
        {
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("https")
                    .host("www.theappsdr.com")
                    .addPathSegment("posts")
                    .addPathSegment("create")
                    .build();
            //System.out.println(url);
            RequestBody formBody = new FormBody.Builder()
                    .add("post_text",post)
                    .build();

            Request request = new Request.Builder()
                    .header("Authorization", "BEARER "+userToken.getToken())
                    .url(url)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful())
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListener.goBackToPosts(userToken);

                            }
                        });

                    }

                }
            });
        }
        catch (Exception e)
        {

        }


    }
    EditText createnewPost;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Create Post");
        View view= inflater.inflate(R.layout.fragment_create_post, container, false);
        createnewPost=view.findViewById(R.id.newpost_text);

        view.findViewById(R.id.newPost_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mListener.createnewpost(createnewPost.getText().toString(),userToken);
                if(createnewPost.getText().toString()==null || createnewPost.getText().toString().length()==0)
                {
                    Toast.makeText(getContext(),"please enter a post", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addnewPost(createnewPost.getText().toString(),userToken);
                }


            }
        });

        view.findViewById(R.id.newPost_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelNewPost();
            }
        });
        return view;
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof newPostListener)
        {
            mListener = (newPostListener)context;
        }
        else
        {
            throw new RuntimeException(context.toString() + "Must impl listner");
        }
    }

    newPostListener mListener;

    interface newPostListener{

       // void createnewpost(String post, UserToken userToken);
        void goBackToPosts(UserToken userToken);
        void cancelNewPost();
    }
}