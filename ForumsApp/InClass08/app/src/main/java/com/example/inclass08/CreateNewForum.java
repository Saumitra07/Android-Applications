package com.example.inclass08;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateNewForum#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNewForum extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore db;
    FirebaseAuth mauth;
    User us;
    public CreateNewForum() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment CreateNewForum.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNewForum newInstance(User us) {
        CreateNewForum fragment = new CreateNewForum();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, us);
       // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            us = (User) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof newForumListener)
        {
            mListener = (newForumListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + "Must impl listner");
        }
    }
    EditText forumtitle;
    EditText forumdescription;
    String name;

    public boolean Validation(String forum,String title)
    {
        if(forum.length()==0)
        {
            Toast.makeText(getActivity(),"Enter Description", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(title.length()==0)
        {
            Toast.makeText(getActivity(),"Enter Title", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("New Forum");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_new_forum, container, false);
//        Log.d("demo","inside createnewforum"+us.getEmail());
        forumtitle=view.findViewById(R.id.enter_forumtitle);
        forumdescription=view.findViewById(R.id.enter_forumtext);

        view.findViewById(R.id.button_cancelNewForum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToForums();

            }
        });

        view.findViewById(R.id.button_submit_newforum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db=FirebaseFirestore.getInstance();
                String title=forumtitle.getText().toString();
                String desciption=forumdescription.getText().toString();

                if(Validation(desciption,title))
                {

                    mauth=FirebaseAuth.getInstance();
                    db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(QueryDocumentSnapshot doc:task.getResult())
                            {
                                String email=doc.getData().get("email").toString();
                                name=doc.getData().get("fullname").toString();
                                if(email.equals(mauth.getCurrentUser().getEmail().toString())){

                                    Map<String, Object> newForum = new HashMap<>();
                                    newForum.put("creator_email", email);
                                    newForum.put("creator", name);
                                    newForum.put("created_date",Utils.HelperFunctions.getDate());
                                    newForum.put("description",desciption);
                                    newForum.put("title",title);
                                    newForum.put("likes",new ArrayList<>());
                                    newForum.put("comments",new ArrayList<>());
                                    db.collection("forums").add(newForum).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            Log.d("demo","new forum created");
                                            mListener.newForumAdded();



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("demo","new forum failed");

                                        }
                                    });

                                }

                            }
                        }
                    });

                }
                else
                {

                }





            }
        });


        return  view;
    }
    newForumListener mListener;
    interface newForumListener{

        void newForumAdded();

        void goToForums();

    }
}