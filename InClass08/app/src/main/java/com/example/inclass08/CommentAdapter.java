package com.example.inclass08;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder>{

    ArrayList<Comment> comments;

    FirebaseAuth mAuth;

    CommentDeleteListener IcommentDeleteListener;

    Forums f;



    public CommentAdapter(ArrayList<Comment> comments, CommentAdapter.CommentDeleteListener IcommentDeleteListener,Forums f)
    {
        this.comments=comments;
        this.IcommentDeleteListener=  IcommentDeleteListener;
        this.mAuth=FirebaseAuth.getInstance();
        this.f = f;
       // this.mListener=mListener;

    }


    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row_layout,parent,false);

        CommentHolder commentHolder=new CommentHolder(view,IcommentDeleteListener);

        return commentHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentHolder holder, int position) {

        holder.comment.setText(comments.get(position).getDesc());
        holder.date.setText(comments.get(position).getDate());
        holder.creator.setText(comments.get(position).getName());
        holder.commentObj = comments.get(position);
        if(comments.get(position).getEmail().equals(mAuth.getCurrentUser().getEmail()))
        {
            holder.delete.setImageResource(R.drawable.rubbish_bin);
        }


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentHolder extends  RecyclerView.ViewHolder{
        TextView comment;
        TextView date;
        TextView creator;
        ImageView delete;
        Comment commentObj;
        Forums f;
        int pos;
        CommentAdapter.CommentDeleteListener mListener;

        public CommentHolder(@NonNull View itemView,CommentAdapter.CommentDeleteListener IcommentDeleteListener) {
            super(itemView);
            comment=itemView.findViewById(R.id.comment_description);
            creator=itemView.findViewById(R.id.comment_creator);
            date=itemView.findViewById(R.id.comment_date);
            delete = itemView.findViewById(R.id.button_comment_delete);



            itemView.findViewById(R.id.button_comment_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                   // mListener.deleteForum(f);
                    IcommentDeleteListener.deleteComment(f,commentObj);

                }
            });



        }


    }



    interface CommentDeleteListener
    {
        // void goToDetails(User user);

        void deleteComment(Forums fr,Comment commentObj);
        void likeForum(Forums fr);
        void goToForumComments(Forums fr);
    }






}
