package edu.uncc.midtermapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.postViewHolder> {
    IuserListner mListener;
    ArrayList<Posts.Post> ps;
    UserToken userToken;
    public PostRecyclerAdapter(ArrayList<Posts.Post> data,UserToken userToken, IuserListner mListener)
    {
        this.mListener=mListener;
        this.ps=data;
        this.userToken=userToken;
        //this.us=data;

    }


    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row_layout,parent,false);

        postViewHolder postview=new postViewHolder(view,userToken,mListener);

        return postview;
    }



    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position) {
        holder.usr=ps.get(position);
        holder.post.setText(ps.get(position).getPost_text());
        holder.creator.setText(ps.get(position).getCreated_by_name());
        holder.date.setText(ps.get(position).getCreated_at());
        holder.position=position;
       // holder.postobj=ps.get(position);
        Posts.Post p=ps.get(position);
        if(p.getCreated_by_uid().equals(userToken.getUserId()))

        {

            holder.trashicon.setImageResource(R.drawable.ic_trash);
        }

    }


    @Override
    public int getItemCount() {
        return ps.size();
    }

    public static class postViewHolder extends  RecyclerView.ViewHolder{

        TextView post;
        TextView creator;
        TextView date;
        View rootview;
        //Posts.Post postobj;
        String post1;
        String creator1;
        String date1;
        UserToken userToken;

        ImageView trashicon;
        int position;
        Posts.Post usr;
        IuserListner mListener;
        public postViewHolder(@NonNull View itemView,UserToken userToken,IuserListner mListener) {
            super(itemView);
            rootview=itemView;
            this.mListener=mListener;
            this.userToken=userToken;
            trashicon=itemView.findViewById(R.id.button_delete_post);
            post=itemView.findViewById(R.id.post_text);
            creator=itemView.findViewById(R.id.post_creator);
            date=itemView.findViewById(R.id.post_date);
            itemView.findViewById(R.id.button_delete_post).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.deletePost(usr,userToken);
                    Log.d("demo","delete post"+usr.getPost_id());


                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

//


        }
    }





    interface IuserListner
    {
        // void goToDetails(User user);

        void deletePost(Posts.Post post,UserToken userToken);
    }


}

