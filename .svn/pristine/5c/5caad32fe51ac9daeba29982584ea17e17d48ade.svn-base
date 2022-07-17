package com.example.myimageapp.download;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myimageapp.R;
import com.example.myimageapp.model.ImageModel;
import com.example.myimageapp.utils.DebugHelper;
import com.example.myimageapp.utils.SQLiteHelper;
import com.example.myimageapp.utils.SQLiteHistoryHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DownloadFragment extends Fragment implements DownloadAdapter.OnItemClickListener{
    RecyclerView rcvDown;
    private DownloadAdapter mDownloadAdapter;
    private ArrayList<ImageModel> downList = new ArrayList<>();
    private SQLiteHistoryHelper sqLiteHistoryHelper;
    private String downTable = "History";
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        initView(view);
        initAction();
        return view;
    }

    private void getData() {
        sqLiteHistoryHelper = new SQLiteHistoryHelper(getActivity());
        downList = sqLiteHistoryHelper.getDownloadList(downTable);
        DebugHelper.logDebug("History.GetData", downList+"");
        mDownloadAdapter = new DownloadAdapter(downList, getContext(),this);
        rcvDown.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcvDown.setAdapter(mDownloadAdapter);
        mDownloadAdapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        rcvDown = view.findViewById(R.id.rcvDownload);
    }

    private void initAction() {
        getData();
    }

    @Override
    public void onClick(ImageModel model, int position) {

    }

}
