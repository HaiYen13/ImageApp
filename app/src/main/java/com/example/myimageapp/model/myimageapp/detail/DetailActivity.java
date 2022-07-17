package com.example.myimageapp.model.myimageapp.detail;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myimageapp.R;
import com.example.myimageapp.model.myimageapp.model.ImageModel;
import com.example.myimageapp.model.myimageapp.service.DownLoadingImageAsynctask;
import com.example.myimageapp.model.myimageapp.utils.DebugHelper;
import com.example.myimageapp.model.myimageapp.utils.SQLiteHelper;
import com.example.myimageapp.model.myimageapp.utils.SQLiteHistoryHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private ImageView imgDownload;
    private ImageView imgFavorite;
    private ProgressDialog pDialog;
    private SQLiteHistoryHelper historyHelper;
    public static final int process_bar_type = 0;
//    private BottomNavigationView bottomNavigationView;
    private ArrayList<ImageModel> list = null;
    int pos = 0;
    final String favoriteTableName = "Favorite";
    final String historyTableName = "History";
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    SQLiteHelper sqLiteHelper;
    private final int RQ_WRITE_PERMISSION = 2810;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqLiteHelper = new SQLiteHelper(DetailActivity.this);
        setContentView(R.layout.activity_detail);
        list = (ArrayList<ImageModel>) getIntent().getSerializableExtra("myList");
        pos = getIntent().getIntExtra("position",0);
        historyHelper = new SQLiteHistoryHelper(DetailActivity.this);
        initView();
//        setResult();
//        finish();
        initAction();

    }
    private void initView(){
        imgFavorite =findViewById(R.id.img_favorite);
        imgDownload = findViewById(R.id.imgDownload);
        mViewPager = findViewById(R.id.vp_view_pager);
    }

    private void initAction() {
        ImageModel model = list.get(pos);
        imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()) {
                    ArrayList<String> urls = new ArrayList<>();
                    urls.add(model.getUrl());
                    new DownLoadingImageAsynctask(DetailActivity.this).execute(urls);
                    historyHelper.insertHistory(model, historyTableName);
                }
            }
        });
        mPagerAdapter = new ViewPagerAdapter(DetailActivity.this, list);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(pos);
        onPageChange(pos);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                onPageChange(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageModel model = list.get(mViewPager.getCurrentItem());
                if(model.getIsFavorited()==0){
                    boolean isSuccess = sqLiteHelper.insertFavorite(model, favoriteTableName);
                    if(isSuccess){
                        imgFavorite.setImageResource(R.drawable.ic_favorite_selected);
                        Toast.makeText(DetailActivity.this, "Add image into favorite storage" , Toast.LENGTH_SHORT).show();
                        model.setIsFavorited(1);
                    }else{
                        Toast.makeText(DetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    imgFavorite.setImageResource(R.drawable.ic_favorite);
                    sqLiteHelper.delete(favoriteTableName,model.getId());
                    Toast.makeText(DetailActivity.this, "Delete image from favorite storage" , Toast.LENGTH_SHORT).show();
                    model.setIsFavorited(0);
                }
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RQ_WRITE_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    new DownLoadingImageAsynctask(DetailActivity.this).equals(list.get(pos).getUrl());
                } else {
                    Toast.makeText(DetailActivity.this, "Allow permission for storage access", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
//TODO: TRong TH người dùng click vào never ask again thì sẽ sử dụng hàm này
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RQ_WRITE_PERMISSION){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.v("DetaiActivity.PerRes", "Permission: "+permissions[0]+ "was " + grantResults[0]);
                    new DownLoadingImageAsynctask(DetailActivity.this).equals(list.get(pos).getUrl());
                }else {
                        new AlertDialog.Builder(DetailActivity.this).setMessage("You need to enable permision to use this feature").setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();

                                Uri uri = Uri.fromParts("package", DetailActivity.this.getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS), RQ_WRITE_PERMISSION);
                            }
                        }).setNegativeButton("Go back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DetailActivity.this.initView();
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
            if(DetailActivity.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                DebugHelper.logDebug("Permison is granted","");
                return true;
            }else{
                ActivityCompat.requestPermissions(DetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RQ_WRITE_PERMISSION);
                return false;
            }
        }else
            return true;// android < 6 khong can request permistion
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("current_position", mViewPager.getCurrentItem());
        intent.putExtra("newList", list);
        setResult(123, intent);
        super.onBackPressed();
    }
    private void onPageChange(int position){
        ImageModel model = list.get(position);
        if(model.getIsFavorited() == 1){
            imgFavorite.setImageResource(R.drawable.ic_favorite_selected);
        }else
            imgFavorite.setImageResource(R.drawable.ic_favorite);
    }

}
