package edu.uncc.midtermapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PagesRecyclerAdapter extends RecyclerView.Adapter<PagesRecyclerAdapter.PageViewHolder> {

    ArrayList<Integer> pages;
    UserToken userToken;
    public PagesRecyclerAdapter(ArrayList<Integer> pages,UserToken userToken, PageClickHandler pagelistener)
    {
       // this.mListener=mListener;
        this.pages=pages;
        this.userToken=userToken;
        this.pagelistener=pagelistener;
        //this.us=data;

    }


    @NonNull
    @Override
    public PagesRecyclerAdapter.PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.page_row_layout,parent,false);

        PageViewHolder postview=new PageViewHolder(view,userToken,pagelistener);

        return postview;
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        holder.pos=pages.get(position);
        holder.show_pageno.setText(pages.get(position).toString());
        Log.d("demo","inside PageAdapter");


    }




    @Override
    public int getItemCount() {
        return this.pages.size();
    }

    public static class PageViewHolder extends  RecyclerView.ViewHolder{


        View rootview;
        TextView show_pageno;

        int pos;
        public PageViewHolder(@NonNull View itemView, UserToken userToken,PageClickHandler pageClickHandler) {
            super(itemView);
            rootview=itemView;

            show_pageno=itemView.findViewById(R.id.text_showPage);
            itemView.findViewById(R.id.text_showPage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pageClickHandler.goToPagePost(pos);
                    Log.d("demo","page No clicked"+pos);


                }
            });
//


        }
    }
    PageClickHandler pagelistener;

    interface PageClickHandler{

        void goToPagePost(int pageNo);

    }




}
