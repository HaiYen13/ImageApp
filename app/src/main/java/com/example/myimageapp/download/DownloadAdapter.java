package com.example.myimageapp.download;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myimageapp.R;
import com.example.myimageapp.model.ImageModel;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder>{
    private ArrayList<ImageModel> downList;
    private Context context;
    private DownloadAdapter.OnItemClickListener listener;
    private int height;

    public DownloadAdapter(ArrayList<ImageModel> downList, Context context, DownloadAdapter.OnItemClickListener listener) {
        this.downList = downList;
        this.context = context;
        this.listener = listener;
        height = context.getResources().getDisplayMetrics().heightPixels/3;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ImageModel model = downList.get(position);
        Picasso.get()
                .load(model.getUrl())
                .into(holder.img);
        holder.tvName.setText(model.getName()+"\n");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(model, position);
            }
        });
        holder.imgFar.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return downList!=null? downList.size() : 0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tvName;
        private ImageView imgFar;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_item);
            tvName = itemView.findViewById(R.id.tv_name);
            imgFar = itemView.findViewById(R.id.img_btn_favorite);
            img.getLayoutParams().height = height;
        }
    }
    public interface OnItemClickListener {
        void onClick(ImageModel model, int position);
    }

}
