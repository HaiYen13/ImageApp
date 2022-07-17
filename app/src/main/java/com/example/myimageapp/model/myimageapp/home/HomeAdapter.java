package com.example.myimageapp.model.myimageapp.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myimageapp.R;
import com.example.myimageapp.model.myimageapp.model.ImageModel;
import com.example.myimageapp.model.myimageapp.utils.SQLiteHelper;
import com.example.myimageapp.model.myimageapp.utils.SQLiteHistoryHelper;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private ArrayList<ImageModel> dataList;
    private Context context ;
    private Fragment homeFragment;
    private HomeAdapter.OnItemClickListener listener;
    SQLiteHelper sqLiteHelper;
    SQLiteHistoryHelper sqLiteHistoryHelper;
    private int width;
    private int height;
    final String favoriteTableName = "favorite";
    private boolean isDownBoxSelected;
//    SharedPreferences  sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

    public ArrayList<ImageModel> getDataList() {
        return dataList;
    }

    public interface OnItemClickListener {
        void onClick(ImageModel model, int pos);
        void onItemChecked(ImageModel model, int pos, boolean isChecked);
    }

    public HomeAdapter(ArrayList<ImageModel> dataList, Context context, HomeAdapter.OnItemClickListener listener) {
        this.dataList = dataList;
        this.context = context;
        this.listener = listener;
        sqLiteHelper = new SQLiteHelper(context);
        sqLiteHistoryHelper = new SQLiteHistoryHelper(context);
        height = context.getResources().getDisplayMetrics().heightPixels/3;
    }
    public void onUpdateData(ArrayList<ImageModel> newList){
        if(newList!=null&&!newList.isEmpty()){
            if(dataList == null){
                dataList = newList;
                notifyDataSetChanged();
            }else {
                int startPos = dataList.size();
                dataList.addAll(newList);
                notifyItemRangeInserted(startPos, newList.size());
            }
        }
    }
    public void onSelectChange(boolean isChecked){
        isDownBoxSelected = isChecked;
        notifyDataSetChanged();
    }
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ImageModel model = dataList.get(position);
        Picasso.get()
                .load(model.getUrl()+"")
                .into(holder.imgItem);
        holder.tvName.setText(model.getName()+"\n");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(model, position);
            }
        });
        if(model.getIsFavorited()==1){
            holder.imgFav.setImageResource(R.drawable.ic_favorite_selected);
        }else{
            holder.imgFav.setImageResource(R.drawable.ic_favorite);
        }
        holder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(model.getIsFavorited()==0){
                    boolean isSuccess = sqLiteHelper.insertFavorite(model, favoriteTableName);
                    if(isSuccess){
                        holder.imgFav.setImageResource(R.drawable.ic_favorite_selected);
                        Toast.makeText(context, "Add image into favorite storage" , Toast.LENGTH_SHORT).show();
                        model.setIsFavorited(1);
                    }else{
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    sqLiteHelper.delete(favoriteTableName, model.getId());
                    holder.imgFav.setImageResource(R.drawable.ic_favorite);
                    Toast.makeText(context, "Delete image from favorite storage" , Toast.LENGTH_SHORT).show();
                    model.setIsFavorited(0);
                }
            }
        });
        model.setIsSelected(0);
        holder.checkBox.setChecked(false);
        if(isDownBoxSelected){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else{
            holder.checkBox.setVisibility(View.GONE);
        }
       holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                model.setIsSelected(1);
            }else{
                model.setIsSelected(0);
            }
            listener.onItemChecked(model, position, isChecked);
           }
       });
    }


    @Override
    public int getItemCount() {
        return dataList!=null? dataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgItem;
        private TextView tvName;
        private ImageButton imgFav;
        private CheckBox checkBox;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_item);
            tvName = itemView.findViewById(R.id.tv_name);
            imgFav = itemView.findViewById(R.id.img_btn_favorite);
            checkBox = itemView.findViewById(R.id.cb_down_checkbox);
            imgItem.getLayoutParams().height = height;
        }
    }


}
