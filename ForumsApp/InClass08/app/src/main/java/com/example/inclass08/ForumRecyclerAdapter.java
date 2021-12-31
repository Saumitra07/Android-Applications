package com.example.inclass08;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ForumRecyclerAdapter extends RecyclerView.Adapter<ForumRecyclerAdapter.ForumViewHolder> {

    ArrayList<Forums> fs;

    FirebaseAuth mAuth;

    ForumDeleteListener mListener;

    public ForumRecyclerAdapter(ArrayList<Forums> fs,ForumDeleteListener mListener)
    {
        this.fs=fs;
        this.mListener=mListener;

    }



    @NonNull
    @Override
    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_row_layout,parent,false);

        ForumViewHolder forumViewHolder=new ForumViewHolder(view,mListener);

        return forumViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {

        holder.title.setText(fs.get(position).getTitle());
        holder.creator.setText(fs.get(position).getCreator());
        holder.post.setText(fs.get(position).getDesciption());
        holder.post_date.setText(fs.get(position).getCreated_date());
        holder.f=fs.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.goToForumComments(holder.f);

            }
        });

        holder.likes.setText(String.valueOf(holder.f.likes.size())+" Likes | ");


        mAuth=FirebaseAuth.getInstance();

        Log.d("TEST","current user: "+mAuth.getCurrentUser().getEmail().toString()+" forum :"+fs.get(position).getCreator_email());


        if(fs.get(position).isOwner)
        {
            holder.delete_forum.setImageResource(R.drawable.rubbish_bin);

        }
        if(holder.f.likes.size()>0){
            for(int i=0;i<holder.f.likes.size();i++)
            {

                if(holder.f.likes.contains(mAuth.getCurrentUser().getEmail().toString()))
                {
                    holder.like_forum.setImageResource(R.drawable.like_favorite);
                    break;
                }
            }
        }
        else
        {
            holder.like_forum.setImageResource(R.drawable.like_not_favorite);
        }


    }

    @Override
    public int getItemCount() {
        return fs.size();
    }

    public static class ForumViewHolder extends  RecyclerView.ViewHolder{
        TextView title;
        TextView creator;
        TextView post;
        TextView post_date;
        TextView likes;
        ImageView delete_forum;
        ImageView like_forum;
        Forums f;
        int pos;
        ForumDeleteListener mListener;

        public ForumViewHolder(@NonNull View itemView,ForumDeleteListener mListener) {
            super(itemView);
            this.mListener=mListener;
            title=itemView.findViewById(R.id.forum_title);
            creator=itemView.findViewById(R.id.forum_creator);
            post=itemView.findViewById(R.id.forum_text);
            post_date=itemView.findViewById(R.id.forum_date);
            delete_forum=itemView.findViewById(R.id.delete_forum);
            likes = itemView.findViewById(R.id.forum_likes);
            like_forum = itemView.findViewById(R.id.button_like);
            itemView.findViewById(R.id.delete_forum).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)

                {
                    mListener.deleteForum(f);

                }
            });

            itemView.findViewById(R.id.button_like).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)

                {



                    mListener.likeForum(f);


                }
            });

        }
    }



    interface ForumDeleteListener
    {
        // void goToDetails(User user);

        void deleteForum(Forums fr);
        void likeForum(Forums fr);
        void goToForumComments(Forums fr);
    }


}
