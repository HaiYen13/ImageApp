package com.example.myimageapp.detail;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.myimageapp.R;
import com.example.myimageapp.model.ImageModel;
import com.example.myimageapp.utils.SQLiteHelper;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter{
    private Context mContext;
    private PhotoView imageView;
//    private ImageView imgFavorite;
    SQLiteHelper sqLiteHelper;
    final String favoriteTableName = "favorite";
    ArrayList<ImageModel> list;

    public ViewPagerAdapter(Context mContext, ArrayList<ImageModel> list) {
        this.mContext = mContext;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list!=null? list.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        ImageModel model = list.get(position);
//        RelativeLayout vParent = new RelativeLayout(mContext);
        imageView = new PhotoView(mContext);
//        imgFavorite = new ImageView(mContext);
//        vParent.addView(imageView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        vParent.addView(imgFavorite);
        sqLiteHelper = new SQLiteHelper(mContext);
        Picasso.get()
                .load(list.get(position).getUrl())
                .into(imageView);

//        if(model.getIsFavorited() == 1){
//            imgFavorite.setImageResource(R.drawable.ic_favorite_selected);
//        }else
//            imgFavorite.setImageResource(R.drawable.ic_favorite);
//
//        imgFavorite.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if(model.getIsFavorited()==0){
//                    boolean isSuccess = sqLiteHelper.insertFavorite(model, favoriteTableName);
//                    if(isSuccess){
//                        imgFavorite.setImageResource(R.drawable.ic_favorite_selected);
//                        Toast.makeText(mContext, "Add image into favorite storage" , Toast.LENGTH_SHORT).show();
//                        model.setIsFavorited(1);
//                    }else{
//                        Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    imgFavorite.setImageResource(R.drawable.ic_favorite);
//                    sqLiteHelper.delete(favoriteTableName,model.getId());
//                    Toast.makeText(mContext, "Delete image from favorite storage" , Toast.LENGTH_SHORT).show();
//                    model.setIsFavorited(0);
//                }
//            }
//        });

        container.addView(imageView);

        return imageView;
    }
}
