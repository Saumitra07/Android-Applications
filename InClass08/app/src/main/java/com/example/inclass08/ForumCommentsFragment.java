package com.example.inclass08;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumCommentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumCommentsFragment extends Fragment implements  CommentAdapter.CommentDeleteListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Forums forum;
    FirebaseAuth mAuth;
    FirebaseFirestore fb;
    public User us;

    TextView f_title;
    TextView f_creater;
    TextView f_desc;
    TextView f_comments_count;
    EditText f_comment;

    ArrayList<Comment> comments;

    LinearLayoutManager lm;
    RecyclerView rv;
    CommentAdapter ap;


    public ForumCommentsFragment() {
        // Required empty public constructor
    }
    public ForumCommentsFragment(Forums fr,User us) {
        if(fr!= null )
        {
            this.forum = fr;
            this.comments = fr.comments;
        }
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForumCommentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForumCommentsFragment newInstance(String param1, String param2) {
        ForumCommentsFragment fragment = new ForumCommentsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_forum_comments, container, false);
        getActivity().setTitle("Forum");
        rv=view.findViewById(R.id.commentsview);
        rv.setHasFixedSize(true);
        lm=new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);



        ap=new CommentAdapter(comments,this,forum);
        rv.setAdapter(ap);


        f_title = view.findViewById(R.id.forum_detail_title);
        f_creater = view.findViewById(R.id.forum_detail_creator);
        f_desc = view.findViewById(R.id.forum_detail_desciption);
        f_comments_count = view.findViewById(R.id.forum_detail_no_comments);
        f_comment = view.findViewById(R.id.enter_comment);

        f_title.setText(forum.getTitle());
        f_creater.setText(forum.getCreator());
        f_desc.setText(forum.getDesciption());
        f_comments_count.setText(forum.comments.size()+" comments");

        view.findViewById(R.id.submit_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("demo", "onClick Comment: ");
                if(f_comment.getText().toString().length()==0)
                {
                    Toast.makeText(getActivity(),"Please enter comment",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    commentData(forum,f_comment.getText().toString());

                }
            }
        });





        return view;
    }


    void commentData(Forums fr,String comment)
    {

        mAuth = FirebaseAuth.getInstance();
        fb=FirebaseFirestore.getInstance();

        HashMap<String, Object> newDoc = new HashMap<>();

        fb.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for(QueryDocumentSnapshot val:task.getResult())
                {

                    if(mAuth.getCurrentUser().getEmail().toString().equals(val.getData().get("email").toString()))
                    {
                        us=new User(val.getData().get("email").toString(),val.getData().get("fullname").toString());

                        Comment commentObj =  new Comment(us.getFullname(),mAuth.getCurrentUser().getEmail(),comment);
                        fr.comments.add(commentObj);

                        fb= FirebaseFirestore.getInstance();
                        newDoc.put("comments",fr.comments);

                        fb.collection("forums")
                                .document(fr.getId())
                                .update(newDoc)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            // holder.imageView2.setImageResource(R.drawable.like_favorite);
                                            Log.d("demo", "Comment posted: "+comment);
                                            f_comments_count.setText(forum.comments.size()+" comments");
                                            f_comment.setText("");
                                        }
                                        else
                                        {
                                            Log.d("demo", "Exception: "+task.getException().toString());
                                        }
                                    }
                                });

                        //getFragmentManager().beginTransaction().replace(R.id.container1,ForumsFragment.newInstance(us)).commit();

                    }
                }
            }
        });




    }



    @Override
    public void deleteComment(Forums fr,Comment commentObj) {

        HashMap<String, Object> newDoc = new HashMap<>();
        forum.comments.remove(commentObj);
        fb=FirebaseFirestore.getInstance();

        newDoc.put("comments",forum.comments);
        fb.collection("forums")
                .document(forum.getId())
                .update(newDoc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ap=new CommentAdapter(comments, ap.IcommentDeleteListener, forum);
                            rv.setAdapter(ap);
                            ap.notifyDataSetChanged();
                            f_comments_count.setText(forum.comments.size()+" comments");
                            // holder.imageView2.setImageResource(R.drawable.like_favorite);
                        }
                        else
                        {
                            Log.d("demo", "Exception: "+task.getException().toString());
                        }
                    }
                });




    }

    @Override
    public void likeForum(Forums fr) {

    }

    @Override
    public void goToForumComments(Forums fr) {

    }
}