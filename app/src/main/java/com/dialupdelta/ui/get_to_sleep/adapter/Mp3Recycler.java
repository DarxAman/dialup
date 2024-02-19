package com.dialupdelta.ui.get_to_sleep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dialupdelta.R;
import com.dialupdelta.data.network.response.get_to_sleep_response.GetToSleepList;
import com.dialupdelta.ui.sleep_enhancer.adapter.DudSong;

import java.util.List;

public class Mp3Recycler extends RecyclerView.Adapter<Mp3Recycler.ViewHolder> {

    Context context;
    List<DudSong> list;
    GetClickedMp3 getClickedMp3;
    String url;

    public Mp3Recycler(Context context, List<DudSong> list, GetClickedMp3 getClickedMp3, String url){
        this.context = context;
        this.list = list;
        this.getClickedMp3 = getClickedMp3;
        this.url = url;
    }

    @NonNull
    @Override
    public Mp3Recycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_get_to_sleep_program_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Mp3Recycler.ViewHolder holder, int position) {
        holder.parentLayout.setOnClickListener(v->{
            getClickedMp3.getClickedNumber(position, url + list.get(position).getFilename());
        });
//        if(list.get(position) == false) {
            Glide.with(context).load(R.drawable.mp3logo).placeholder(R.drawable.mp3logo).error(R.drawable.mp3logo)
                    .into(holder.getToSleep_image);
//        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView programItemText;
        ConstraintLayout parentLayout;
        ImageView getToSleep_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            programItemText = itemView.findViewById(R.id.programItemText);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            getToSleep_image = itemView.findViewById(R.id.getToSleep_image);
        }
    }
}
