package com.fao.fmd.fmdappbeta;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {
    private static final int COUNT = 64;

    private final Context mContext;
    private final List<Integer> mItems;
    private int mCurrentItemId = 0;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final ImageView image;

        public SimpleViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
        }

    }

    public SimpleAdapter(Context context) {
        mContext = context;
        mItems = new ArrayList<>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            addItem(i);
        }
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder,  int position) {
        final Integer[] IMAGES = {R.drawable.ves_photo_1,R.drawable.ves_photo_2,R.drawable.ves_photo_3,R.drawable.ves_photo_4,R.drawable.ves_photo_5,R.drawable.ves_photo_6,R.drawable.ves_photo_7,
                R.drawable.fib_photo_1,R.drawable.fib_photo_2,R.drawable.fib_photo_3,R.drawable.fib_photo_4,R.drawable.fib_photo_5,R.drawable.fib_photo_6,R.drawable.fib_photo_7,
                R.drawable.redyes_photo_1,R.drawable.redyes_photo_2,R.drawable.redyes_photo_3,R.drawable.redyes_photo_4,R.drawable.redyes_photo_5,R.drawable.redyes_photo_6,R.drawable.redyes_photo_7,
                R.drawable.pinkyes_photo_1,R.drawable.pinkyes_photo_2,R.drawable.pinkyes_photo_3,R.drawable.pinkyes_photo_4,R.drawable.pinkyes_photo_5,R.drawable.pinkyes_photo_6,R.drawable.pinkyes_photo_7,
                R.drawable.redno_photo_1,R.drawable.redno_photo_2,R.drawable.redno_photo_3,R.drawable.redno_photo_4,R.drawable.redno_photo_5,R.drawable.redno_photo_6,
                R.drawable.pinkno_photo_1,R.drawable.pinkno_photo_2,R.drawable.pinkno_photo_3,R.drawable.pinkno_photo_4,R.drawable.pinkno_photo_5,
                R.drawable.white_photo_1,R.drawable.white_photo_2,R.drawable.white_photo_3,R.drawable.white_photo_4,R.drawable.white_photo_5,R.drawable.white_photo_6,
                R.drawable.redyesround_photo_1,R.drawable.redyesround_photo_2,R.drawable.redyesround_photo_3,R.drawable.redyesround_photo_4,R.drawable.redyesround_photo_5,
                R.drawable.redyessharp_photo_1,R.drawable.redyessharp_photo_2,
                R.drawable.pinkyesround_photo_1,R.drawable.pinkyesround_photo_2,R.drawable.pinkyesround_photo_3,R.drawable.pinkyesround_photo_4,R.drawable.pinkyesround_photo_5,
                R.drawable.rednoround_photo_1,R.drawable.rednoround_photo_2,
                R.drawable.rednosharp_photo_1,R.drawable.rednosharp_photo_2,R.drawable.rednosharp_photo_3,R.drawable.rednosharp_photo_4,R.drawable.rednosharp_photo_5};
        Picasso.get().load(IMAGES[position]).resize(1280,960).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("img",IMAGES[position]);
                Intent intent = new Intent(mContext, PhotoViewer.class);
                intent.putExtras(b);
                mContext.startActivity(intent);
            }
        });
    }

    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
