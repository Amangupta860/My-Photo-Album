package com.photoalbum.myphotoalbum;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.icu.text.Transliterator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyImageAdapter extends  RecyclerView.Adapter<MyImageAdapter.MyImageHolder>{
    List<MyImages> imagesList= new ArrayList<>();
    private OnImageClickListener listener;

    public void setListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    public void setImagesList(List<MyImages> imagesList) {
        this.imagesList = imagesList;
        notifyDataSetChanged();
    }
    public  interface OnImageClickListener{
        void onImageClick(MyImages myImages);
    }

    @NonNull
    @Override
    public MyImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card,parent,false);

        return new MyImageHolder(view);
    }
    public  MyImages getPosition(int position){
        return imagesList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull MyImageHolder holder, int position) {
        MyImages myImages = imagesList.get(position);
        holder.textViewTitle.setText(myImages.getImage_title());
        holder.textViewDescription.setText(myImages.getImage_description());
        holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(myImages.getImage(),0,myImages.getImage().length));

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public  class MyImageHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewTitle , textViewDescription;

        public MyImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle= itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    int position = getAdapterPosition();
                    if (listener !=null && position != RecyclerView.NO_POSITION ){
                        listener.onImageClick(imagesList.get(position));

                    }

                }
            });
        }
    }
}
