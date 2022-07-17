package com.example.myimageapp.favorite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myimageapp.R;
import com.example.myimageapp.model.ImageModel;
import com.example.myimageapp.utils.SQLiteHelper;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private ArrayList<ImageModel> dataList;
    private Context context;
    private FavoriteAdapter.OnItemClickListener listener;
    private SQLiteHelper sqLiteHelper;
    public int height;
    final String favoriteTableName = "favorite";
    public void setHeight(int height){
        this.height = height;
    }
    public FavoriteAdapter(ArrayList<ImageModel> dataList, Context context, FavoriteAdapter.OnItemClickListener listener) {
        this.dataList = dataList;
        this.context = context;
        this.listener = listener;
        sqLiteHelper = new SQLiteHelper(context);

        height = context.getResources().getDisplayMetrics().heightPixels/3;
    }
    public void onUpdateData(ArrayList<ImageModel> newList){
        if(newList!=null&&newList.isEmpty()){
            if(dataList==null){
                dataList = newList;
                notifyDataSetChanged();
            }else {
                int startPos = dataList.size();
                dataList.addAll(newList);
                notifyItemRangeChanged(startPos, newList.size());
            }
        }

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
        ImageModel model = dataList.get(position);
        Picasso.get()
                .load(model.getUrl() + "")
                .into(holder.imgItem);
        holder.tvNameItem.setText(model.getName()+"\n");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(model, position);
            }
        });
        holder.imgFav.setImageResource(R.drawable.ic_favorite_selected);
        holder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteHelper.delete(favoriteTableName, model.getId());
                holder.imgFav.setImageResource(R.drawable.ic_favorite);
                Toast.makeText(context, "Delete image from favorite storage", Toast.LENGTH_SHORT).show();
                model.setIsFavorited(0);
                removeAt(position);
            }
        });

    }
    public void removeAt(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataList.size());
    }

    @Override
    public int getItemCount() {
        return dataList!=null? dataList.size() : 0;
    }

    public interface OnItemClickListener {
        void onClick(ImageModel model, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgItem;
        private TextView tvNameItem;
        private ImageButton imgFav;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_item);
            tvNameItem = itemView.findViewById(R.id.tv_name);
            imgFav = itemView.findViewById(R.id.img_btn_favorite);
            imgItem.getLayoutParams().height = height;
        }
    }
}
