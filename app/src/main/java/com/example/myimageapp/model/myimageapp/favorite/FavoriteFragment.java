package com.example.myimageapp.model.myimageapp.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myimageapp.R;
import com.example.myimageapp.model.myimageapp.detail_favorite.DetailImage;
import com.example.myimageapp.model.myimageapp.model.ImageModel;
import com.example.myimageapp.model.myimageapp.utils.DebugHelper;
import com.example.myimageapp.model.myimageapp.utils.SQLiteHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements FavoriteAdapter.OnItemClickListener{
    private RecyclerView rcvFavorite;
    private FavoriteAdapter mFavoriteAdapter;
    private ArrayList<ImageModel> mImageModel = new ArrayList<>();
    private SQLiteHelper sqLiteHelper;
    private boolean isLoadingData;
    private Parcelable savedRecycleLayoutState;
    public String tableName = "Favorite";
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         View view = inflater.inflate(R.layout.fragment_favorite, container, false);
         initView(view);
         initAction();
         return view;
    }

    private void initView(View view) {
        rcvFavorite =view.findViewById(R.id.rcvFavorite);
    }

    private void initAction() {
        getData();

    }

    public void getData(){
        sqLiteHelper = new SQLiteHelper(getActivity());
        mImageModel =sqLiteHelper.getArrayListData(tableName);
        DebugHelper.logDebug("Favorite.GetData", mImageModel+"");
        mFavoriteAdapter = new FavoriteAdapter(mImageModel, getContext(), this);
        rcvFavorite.setLayoutManager(new GridLayoutManager(getContext(),2));
        rcvFavorite.setAdapter(mFavoriteAdapter);
        mFavoriteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(ImageModel model, int position) {
        Intent intent = new Intent(getContext(), DetailImage.class);
        startActivity(intent);
        DetailImage detailImage = new DetailImage();
        detailImage.setIntent(intent);
    }
//    public void initAdapter()

}
