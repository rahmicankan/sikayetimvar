package com.example.sikayetimvar.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sikayetimvar.databinding.RecyclerRowBinding;
import com.example.sikayetimvar.model.Post;
import com.squareup.picasso.Picasso;

import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private ArrayList<Post> postArrayList;
    private ObjectInputStream.GetField Picasso;

    public PostAdapter(ArrayList<Post> postArrayList){
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
       return new PostHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
    holder.recyclerRowBinding.recyclerViewUserEmailText.setText(postArrayList.get(position).email);
        holder.recyclerRowBinding.recyclerViewsikayetText.setText(postArrayList.get(position).sikayet);
        com.squareup.picasso.Picasso.get().load(postArrayList.get(position).downloadUrl).into(holder.recyclerRowBinding.recyclerViewImage);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{

        RecyclerRowBinding recyclerRowBinding;

        public PostHolder (RecyclerRowBinding recyclerRowBinding){
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;
        }

    }
}



