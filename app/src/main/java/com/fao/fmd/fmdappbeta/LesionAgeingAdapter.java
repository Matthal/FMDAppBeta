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

public class LesionAgeingAdapter extends RecyclerView.Adapter<LesionAgeingAdapter.SimpleViewHolder> {
    private static final int COUNT = 67;

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

    public LesionAgeingAdapter(Context context) {
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
    public void onBindViewHolder(final SimpleViewHolder holder, int position) {
        final Integer[] IMAGES = {R.drawable.onetwo_photo_1,R.drawable.onetwo_photo_2,R.drawable.onetwo_photo_3,R.drawable.onetwo_photo_4,R.drawable.onetwo_photo_5,R.drawable.onetwo_photo_6,
                R.drawable.twothree_photo_1,R.drawable.twothree_photo_2,R.drawable.twothree_photo_3,R.drawable.twothree_photo_4,R.drawable.twothree_photo_5,R.drawable.twothree_photo_6,R.drawable.twothree_photo_7,R.drawable.twothree_photo_8,R.drawable.twothree_photo_9,R.drawable.twothree_photo_10,R.drawable.twothree_photo_11,
                R.drawable.threefour_photo_1,R.drawable.threefour_photo_2,R.drawable.threefour_photo_3,R.drawable.threefour_photo_4,R.drawable.threefour_photo_5,R.drawable.threefour_photo_6,R.drawable.threefour_photo_7,R.drawable.threefour_photo_8,
                R.drawable.fourfive_photo_1,R.drawable.fourfive_photo_2,R.drawable.fourfive_photo_3,R.drawable.fourfive_photo_4,R.drawable.fourfive_photo_5,R.drawable.fourfive_photo_6,R.drawable.fourfive_photo_7,R.drawable.fourfive_photo_8,R.drawable.fourfive_photo_9,R.drawable.fourfive_photo_10,R.drawable.fourfive_photo_11,R.drawable.fourfive_photo_12,R.drawable.fourfive_photo_13,R.drawable.fourfive_photo_14,R.drawable.fourfive_photo_15,R.drawable.fourfive_photo_16,
                R.drawable.fiveseven_photo_1,R.drawable.fiveseven_photo_2,R.drawable.fiveseven_photo_3,R.drawable.fiveseven_photo_4,R.drawable.fiveseven_photo_5,R.drawable.fiveseven_photo_6,R.drawable.fiveseven_photo_7,R.drawable.fiveseven_photo_8,R.drawable.fiveseven_photo_9,R.drawable.fiveseven_photo_10,R.drawable.fiveseven_photo_11,R.drawable.fiveseven_photo_12,R.drawable.fiveseven_photo_13,R.drawable.fiveseven_photo_14,R.drawable.fiveseven_photo_15,R.drawable.fiveseven_photo_16,R.drawable.fiveseven_photo_17,
                R.drawable.seventen_photo_1,
                R.drawable.tenfourteen_photo_1,R.drawable.tenfourteen_photo_2,R.drawable.tenfourteen_photo_3,R.drawable.tenfourteen_photo_4,R.drawable.tenfourteen_photo_5,
                R.drawable.fourteenplus_photo_1,R.drawable.fourteenplus_photo_2,R.drawable.fourteenplus_photo_3};
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
