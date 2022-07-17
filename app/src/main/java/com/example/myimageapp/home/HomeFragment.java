package com.example.myimageapp.home;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myimageapp.service.DownImageService;
import com.example.myimageapp.service.DownloadService;
import com.example.myimageapp.LoadDataListener;
import com.example.myimageapp.LoadingDataAsynctask;
import com.example.myimageapp.R;
import com.example.myimageapp.DownResultReceiver;
import com.example.myimageapp.detail.DetailActivity;
import com.example.myimageapp.model.ImageModel;
import com.example.myimageapp.utils.DebugHelper;
import com.example.myimageapp.utils.SQLiteHistoryHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements HomeAdapter.OnItemClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private RecyclerView rcvCatogory;
    private HomeAdapter mHomeAdapter;
    private boolean isLoadingData;
    private Parcelable savedRecycleLayoutState;
    private ImageView imgMultiDown;
    private boolean isDownBoxSelected;
    private TextView tvMultiDown;
    private HashMap<Integer, ImageModel> map;
    final String historyTable = "History";
    SQLiteHistoryHelper sqLiteHistoryHelper;
    ArrayList<ImageModel> historyModels;
    private final int RQ_WRITE_PERMISSION = 2810;
    ArrayList<String> urls;
    private ProgressDialog mProgressDialog;
    private static final String DOWNLOADNG_ACTION= "com.yenvth.ACTION";
    IntentFilter intentFilter;
    private DownloadService downloadService;
    Intent i;
    int currentPage = 0;
    String[] arrDataName = {
            "data1.json",
            "data2.json",
            "data3.json",
    };
    String[] arr = new String[arrDataName.length];
//    public HomeFragment() {
//        // Required empty public constructor
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        map = new HashMap<>();
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        sqLiteHistoryHelper = new SQLiteHistoryHelper(getContext());
        urls = new ArrayList<>();
        historyModels = new ArrayList<>();
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("A message");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);


        //TODO: intent to filter for file downloaded intent
        intentFilter = new IntentFilter();
        intentFilter.addAction("FILE_DOWNLOADED_ACTION");
        
        initView(view);
        initAction();

        if(savedInstanceState != null){
            savedRecycleLayoutState = savedInstanceState.getParcelable("positionScroll");
            currentPage = savedInstanceState.getInt("currentPage");
            DebugHelper.logDebug("onCreate.currentPageSaved ", currentPage + " ")  ;
        }
        if(currentPage>= arr.length)
            currentPage = arr.length - 1;
        if(currentPage<0)
            currentPage = 0;
        for(int i=0; i<= currentPage; i++){
            arr[i] = arrDataName[i];
            DebugHelper.logDebug("onCreate.arrTemp", arr[i]+"");
        }
        new LoadingDataAsynctask(getActivity(), new LoadDataListener() {
            @Override
            public void onLoadDataFinished(ArrayList<ImageModel> arrayList) {
                initAdapter(arrayList);
                if(savedRecycleLayoutState!=null){
                    rcvCatogory.getLayoutManager().onRestoreInstanceState(savedRecycleLayoutState);
                }
                getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }
        }).execute(arr);
//        downloadService = new DownloadService(getActivity());

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        savedRecycleLayoutState = rcvCatogory.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("positionScroll", savedRecycleLayoutState);
        outState.putInt("currentPage", currentPage);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            int pos = data.getIntExtra("current_position", 0);
            ArrayList<ImageModel> newList = (ArrayList<ImageModel>) data.getSerializableExtra("newList");
            initAdapter(newList);
            rcvCatogory.smoothScrollToPosition(pos);
//            new MyAsyntask(getActivity(), new LoadDataListener() {
//                @Override
//                public void onLoadDataFinished(ArrayList<ImageModel> arrayList) {
//                    initAdapter(arrayList);
//                    if(savedRecycleLayoutState!=null){
//                        rcvCatogory.getLayoutManager().onRestoreInstanceState(savedRecycleLayoutState);
//                    }
//                    getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
//                }
//            }).execute(arr);
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
    private void initAdapter(ArrayList<ImageModel> arrayList) {
        mHomeAdapter = new HomeAdapter(arrayList, getActivity(), this);
        rcvCatogory.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcvCatogory.setAdapter(mHomeAdapter);
    }
    private void initView(View view){
        rcvCatogory = view.findViewById(R.id.rcvHome);
        imgMultiDown = view.findViewById(R.id.img_down_box);
        tvMultiDown = view.findViewById(R.id.tvDown);
    }
    private void loadMore(){
        currentPage++;
        new LoadingDataAsynctask(getActivity(), new LoadDataListener() {
            @Override
            public void onLoadDataFinished(ArrayList<ImageModel> arrayList) {
                mHomeAdapter.onUpdateData(arrayList);
                isLoadingData = false;
            }
        }).execute(arrDataName[currentPage]);
        DebugHelper.logDebug("currentPage", "currentPage" + currentPage );

    }
    private void initAction() {
        rcvCatogory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                Log.e("rcvCatogory.onScrollStateChanged", "newState:"+newState);
                if(!isLoadingData && newState == RecyclerView.SCROLL_STATE_IDLE
                        && currentPage < arrDataName.length - 1){
                    int lastPos = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if(lastPos == mHomeAdapter.getItemCount() - 1){
                        isLoadingData = true;
                        loadMore();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                DebugHelper.logDebug("rcvCatogory.onScrolled", "dx:"+dx +" - dy:"+dy);
            }
        });
        imgMultiDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDownBoxSelected){
                    imgMultiDown.setImageResource(R.drawable.ic_box);
                    tvMultiDown.setVisibility(View.GONE);
                    isDownBoxSelected = false;
                    map.clear();
                }else{
                    imgMultiDown.setImageResource(R.drawable.ic_box_selected);
                    tvMultiDown.setVisibility(View.VISIBLE);
                    isDownBoxSelected = true;
                }
                if(mHomeAdapter!=null)
                    mHomeAdapter.onSelectChange(isDownBoxSelected);
            }
        });
        //Todo: download mang url
        tvMultiDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()) {
                    for (Map.Entry<Integer, ImageModel> entry : map.entrySet()) {
                        ImageModel value = entry.getValue();
                        urls.add(value.getUrl());
                        historyModels.add(value);
                    }
                    mProgressDialog.show();
//                    new DownLoadingImageAsynctask(getActivity()).execute(urls);
                    startDownService(urls);
                    sqLiteHistoryHelper.insertListHistory(historyModels, historyTable);
                }
            }

        });
    }

    private void startDownService(ArrayList<String> urls) {
        Intent intent = new Intent(getContext(), DownImageService.class);
        intent.putExtra("key_down_intent", urls);
        intent.putExtra("receiver", new DownResultReceiver(new Handler(), mProgressDialog));
        getActivity().startService(intent);
    }

    @Override
    public void onClick(ImageModel model, int pos) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("myList", mHomeAdapter.getDataList());
        intent.putExtra("position", pos);
        startActivityForResult(intent, 123);

    }

    @Override
    public void onItemChecked(ImageModel model, int pos, boolean isChecked) {
        if(isChecked){
            map.put(model.getId(), model);
        }else{
            if(map.get(model.getId()) != null){
                map.remove(model.getId());
            }
        }

    }

    public boolean isDownBoxSelected() {
        return isDownBoxSelected;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RQ_WRITE_PERMISSION){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.v("HomeFragment.PerRes", "Permission: "+permissions[0]+ "was " + grantResults[0]);
//                    new DownLoadingImageAsynctask(getActivity()).execute(urls);
                    startDownService(urls);
                }else {
                    new AlertDialog.Builder(getActivity()).setMessage("You need to enable permision to use this feature").setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS), RQ_WRITE_PERMISSION);
                        }
                    }).setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(getView() != null){
                                initView(getView());
                            }
                        }
                    }).show();
                }
            }
        }
    }

    public boolean isStoragePermissionGranted(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            return true;
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                DebugHelper.logDebug("Permison is granted","");
                return true;
            }else{
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RQ_WRITE_PERMISSION);
                return false;
            }
        }else
            return true;// android < 6 khong can request permistion
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(DOWNLOADNG_ACTION.equals(intent.getAction())){
                Toast.makeText(getContext(), "onReceive", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(DOWNLOADNG_ACTION);
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        DebugHelper.logDebug("onStop", "mBroadcastReceiver");
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }
}
