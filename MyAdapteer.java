package com.example.lucky.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapteer extends RecyclerView.Adapter<MyAdapteer.ViewHolder> {

    List<ListItem> listItems;
    private Context context;
    private OnItemClickListener mlistener;


    public MyAdapteer(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ListItem listItem = listItems.get(position);
        holder.t_head.setText(listItem.getTitle());
        holder.t_release.setText(listItem.getrelase_date());
        Picasso.with(context).load(listItem.getImage())
                .into(holder.t_image);


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public interface OnItemClickListener {

        void onItemClick(int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView t_head;
        public TextView t_release;
        public ImageView t_image;


        public ViewHolder(View itemView) {

            super(itemView);
            t_head = (TextView) itemView.findViewById(R.id.title);
            t_release = (TextView) itemView.findViewById(R.id.t_releasedate);
            t_image = (ImageView) itemView.findViewById(R.id.images);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mlistener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mlistener.onItemClick(position);
                        }
                    }


                }
            });


        }
    }
}
