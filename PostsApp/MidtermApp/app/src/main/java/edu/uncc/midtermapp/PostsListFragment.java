package edu.uncc.midtermapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostsListFragment extends Fragment  implements PostRecyclerAdapter.IuserListner,PagesRecyclerAdapter.PageClickHandler{
    private static final String ARG_USER_TOKEN = "ARG_USER_TOKEN";
    UserToken mUserToken;

    LinearLayoutManager lm;
    RecyclerView rv;
    PostRecyclerAdapter ap;

    String pageNo="1";


    LinearLayoutManager horizonalManager;
    RecyclerView pagerv;
    PagesRecyclerAdapter pageap;

    OkHttpClient client=new OkHttpClient();
    ArrayList<Posts.Post> posts=new ArrayList<>();
    ArrayList<Integer> pages=new ArrayList<>();

    public PostsListFragment(UserToken userToken) {
        // Required empty public constructor
        this.mUserToken=userToken;
        getPosts(mUserToken,pageNo);
    }

    public void getPosts(UserToken userToken,String pageNo) {

        Log.d("demo","get posts from page"+ pageNo);



        HttpUrl.Builder builder = new HttpUrl.Builder();
        HttpUrl url = builder.scheme("https")
                .host("www.theappsdr.com")
                .addPathSegment("posts")
                .addQueryParameter("page", pageNo)
                .build();


        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "BEARER " + userToken.token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful())
                {
                    try {
                        posts.clear();
                        pages.clear();
                        JSONObject json=new JSONObject(response.body().string());
                        ArrayList<Posts.Post> p=new ArrayList<>();
                        int number_of_pages=Integer.parseInt(json.getString("totalCount"))/10 +1;
                        for(int k=0;k<number_of_pages;k++)
                        {
                            pages.add(k+1);
                        }

                        JSONArray postArray=json.getJSONArray("posts");
                        for(int i=0;i<postArray.length();i++)
                        {
                            JSONObject jsonobj=postArray.getJSONObject(i);
                            Posts.Post post=new Posts.Post();
                            post.setCreated_by_name(jsonobj.getString("created_by_name"));
                            post.setPost_text(jsonobj.getString("post_text"));
                            post.setCreated_at(jsonobj.getString("created_at"));
                            post.setPost_id(jsonobj.getString("post_id"));
                            post.setCreated_by_uid(jsonobj.getString("created_by_uid"));
                            p.add(post);
                            posts.add(post);

                        }
                        Log.d("demo","hello"+p);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                show_pageno.setText(String.valueOf(pageNo)+"out of "+number_of_pages);

                                ap.notifyDataSetChanged();
                                pageap.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else
                {

                }
            }
        });
    }

    void addNewPost(String post,UserToken user)
    {

        Log.d("demo","inside postLists addnewpost api call");

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
                .header("Authorization", "BEARER "+user.getToken())
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
                            Log.d("demo","pst created");
                            ap.notifyDataSetChanged();
                            pageap.notifyDataSetChanged();
                            //getPosts(user,pageNo);
                        }
                    });
                }

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        getPosts(mUserToken,pageNo);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof PostListListener)
        {
            postListListener = (PostListListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + "Must impl listner");
        }
    }

    public static PostsListFragment newInstance(UserToken userToken) {
        PostsListFragment fragment = new PostsListFragment(userToken);
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER_TOKEN, userToken);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserToken = (UserToken)getArguments().getSerializable(ARG_USER_TOKEN);
        }
    }
    TextView welcome_txt;
    TextView show_pageno;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Posts");
       View view=inflater.inflate(R.layout.fragment_posts_list, container, false);

        welcome_txt=view.findViewById(R.id.welcome_text);
        welcome_txt.setText("Welcome "+mUserToken.fullname);
        show_pageno=view.findViewById(R.id.show_pageno);

        rv=view.findViewById(R.id.post_recyclerView);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(rv.getContext(),DividerItemDecoration.VERTICAL));
        lm=new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
        ap=new PostRecyclerAdapter(posts,mUserToken,this);

        try
        {
            pagerv=view.findViewById(R.id.page_recyclerview);
            pagerv.setHasFixedSize(true);
            horizonalManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
            pagerv.setLayoutManager(horizonalManager);
            pageap=new PagesRecyclerAdapter(pages,mUserToken,this);
        }
        catch (Exception e)
        {
            Log.d("demo","Exception occured in page Rv");
        }



        view.findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 postListListener.logOut();

            }
        });

        view.findViewById(R.id.button_createPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               postListListener.createNewPost(mUserToken);
            }
        });

        return view;



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Posts");
        rv.setAdapter(ap);

        ap.notifyDataSetChanged();

        pagerv.setAdapter(pageap);
        pageap.notifyDataSetChanged();
    }

    @Override
    public void deletePost(Posts.Post post,UserToken mUserToken) {
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Delete").setMessage("Are you sure you want to delete?").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HttpUrl url = new HttpUrl.Builder()
                            .scheme("https")
                            .host("www.theappsdr.com")
                            .addPathSegment("posts")
                            .addPathSegment("delete")
                            .build();
                    System.out.println(url);
                    RequestBody formBody = new FormBody.Builder()
                            .add("post_id", String.valueOf(post.getPost_id()))
                            .build();

                    Request request = new Request.Builder()
                            .header("Authorization", "BEARER "+mUserToken.getToken())
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
                                try {
                                    Log.d("demo","inside delete");

                                    for(int i=0;i<posts.size();i++)
                                    {
                                        if(post.getPost_id().equals(posts.get(i).getPost_id()))
                                        {
                                            posts.remove(i);
                                            break;
                                        }
                                    }




                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {


                                            ap.notifyDataSetChanged();
                                            pageap.notifyDataSetChanged();
                                            getPosts(mUserToken,pageNo);


                                        }
                                    });


                                }
                                catch (Exception ex)
                                {

                                    Log.d("demo", "API Failed: ");


                                }
                            }
                            else
                            {

                                Log.d("demo", "API Failed: ");
                            }
                        }
                    });
                }

            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Unable to delete post, please try again", Toast.LENGTH_SHORT).show();
                                    getPosts(mUserToken,pageNo);
                                }
                            });
                        }
                    });
            builder.create().show();


        }
        catch (Exception e)
        {

        }




    }

    PostListListener postListListener;

    @Override
    public void goToPagePost(int pageNo) {
        getPosts(mUserToken,String.valueOf(pageNo));
    }

    interface PostListListener{

        void logOut();
        void createNewPost(UserToken userToken);
    }
}