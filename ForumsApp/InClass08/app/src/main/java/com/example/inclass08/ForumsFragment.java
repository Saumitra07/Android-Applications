package com.example.inclass08;

import android.content.Context;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumsFragment extends Fragment implements ForumRecyclerAdapter.ForumDeleteListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    User us;
    FirebaseAuth mAuth;
    FirebaseFirestore fb;
    ArrayList<Forums> forums=new ArrayList<>();

    LinearLayoutManager lm;
    RecyclerView rv;
    ForumRecyclerAdapter ap;



    public ForumsFragment() {

        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ForumsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForumsFragment newInstance(User us) {
        ForumsFragment fragment = new ForumsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, us);
       // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof forumFragmentListener)
        {
            flistener = (forumFragmentListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + "Must impl listner");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            us = (User) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Forums");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_forums, container, false);
        FirebaseAuth mauth;
        mauth= FirebaseAuth.getInstance();
        Log.d("demo",mauth.getCurrentUser().getEmail());





        rv=view.findViewById(R.id.forumview);
        rv.setHasFixedSize(true);
        lm=new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);



        ap=new ForumRecyclerAdapter(forums,this);
        rv.setAdapter(ap);




        initData();

//        getData();



        view.findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth=FirebaseAuth.getInstance();
                mAuth.signOut();

               flistener.goToLogin();

            }
        });

        view.findViewById(R.id.button_newForum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  getFragmentManager().beginTransaction().replace(R.id.container1,CreateNewForum.newInstance(us)).commit();
                flistener.goToCreateNewForum(us);
            }
        });

        return view;
    }

//
//    public void getData(){
//        fb=FirebaseFirestore.getInstance();
//        fb.collection("forums").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                for(QueryDocumentSnapshot val:task.getResult())
//                {
//
//                    Forums f=new Forums("",val.getData().get("creator").toString(),val.getData().get("creator_email").toString(),val.getData().get("description").toString(),val.getData().get("title").toString(),val.getId());
//                    forums.add(f);
//
//
//                }
//                ap.notifyDataSetChanged();
//            }
//        });
//    }
public  void initData()
{
    fb=FirebaseFirestore.getInstance();
    fb.collection("forums").addSnapshotListener(new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            forums.clear();
            for(QueryDocumentSnapshot val:value)
            {
                boolean isOwner =false;
                mAuth = FirebaseAuth.getInstance();

                if(mAuth.getCurrentUser().getEmail().toString().equals(val.getData().get("creator_email").toString()))
                {
                    isOwner=true;
                }

                Forums f=new Forums(val.getData().get("created_date").toString(),val.getData().get("creator").toString(),val.getData().get("creator_email").toString(),val.getData().get("description").toString(),val.getData().get("title").toString(),val.getId(),isOwner, (ArrayList<String>) val.getData().get("likes"),getComments((ArrayList<HashMap<String, String>>) val.getData().get("comments")) );
                forums.add(f);

            }

            ap.notifyDataSetChanged();





        }
    });
}
    public void getDataSnapshot()
    {
       // forums = new ArrayList<Forums>();

        fb=FirebaseFirestore.getInstance();
        fb.collection("forums").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                forums.clear();
                for(QueryDocumentSnapshot val:value)
                {
                    boolean isOwner =false;
                    mAuth = FirebaseAuth.getInstance();

                    if(mAuth.getCurrentUser().getEmail().toString().equals(val.getData().get("creator_email").toString()))
                    {
                        isOwner=true;
                    }

                    Forums f=new Forums(val.getData().get("created_date").toString(),val.getData().get("creator").toString(),val.getData().get("creator_email").toString(),val.getData().get("description").toString(),val.getData().get("title").toString(),val.getId(),isOwner, (ArrayList<String>) val.getData().get("likes"),getComments((ArrayList<HashMap<String, String>>) val.getData().get("comments")));
                    forums.add(f);

                }





            }
        });

        ap=new ForumRecyclerAdapter(forums,this);
        rv.setAdapter(ap);
        ap.notifyDataSetChanged();


    }

    ArrayList<Comment> getComments(ArrayList<HashMap<String,String>> data)
    {
        ArrayList<Comment> comments = new ArrayList<>();

        for(HashMap mp:data)
        {
            comments.add(new Comment(mp.get("name").toString(),mp.get("email").toString(),mp.get("desc").toString()));
        }
        return comments;


    }



    forumFragmentListener flistener;

    @Override
    public void deleteForum(Forums fr) {

        fb=FirebaseFirestore.getInstance();

        fb.collection("forums").document(fr.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {

                    Log.d("demo","forum delete success");
                    getDataSnapshot();
                    ap.notifyDataSetChanged();

                }

                else
                {
                    Log.d("demo","forum delete failed");

                }

            }
        });







    }

    @Override
    public void likeForum(Forums fr) {

        HashMap<String, Object> newDoc = new HashMap<>();

        if(fr.likes.contains(mAuth.getCurrentUser().getEmail()))
        {
            fr.likes.remove(mAuth.getCurrentUser().getEmail());
            }
        else
        {
            fr.likes.add(mAuth.getCurrentUser().getEmail());
        }

        fb=FirebaseFirestore.getInstance();
        newDoc.put("likes",fr.likes);
        fb.collection("forums")
                .document(fr.getId())
                .update(newDoc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                           // holder.imageView2.setImageResource(R.drawable.like_favorite);
                        }
                        else
                        {
                            Log.d("demo", "Exception: "+task.getException().toString());
                        }
                    }
                });

        getDataSnapshot();



    }

    @Override
    public void goToForumComments(Forums fr) {

        flistener.goToMainToForumComments(fr,us);

    }

    interface forumFragmentListener{

        void goToCreateNewForum(User us);
        void goToLogin();
        void goToMainToForumComments(Forums fr,User us);


    }


}