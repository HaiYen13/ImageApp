package com.example.myimageapp.model.myimageapp.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myimageapp.R;
import com.example.myimageapp.model.myimageapp.download.DownloadFragment;
import com.example.myimageapp.model.myimageapp.favorite.FavoriteFragment;
import com.example.myimageapp.model.myimageapp.home.HomeAdapter;
import com.example.myimageapp.model.myimageapp.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcvCatogory;
    private HomeAdapter mHomeAdapter;
    private BottomNavigationView navigationBarView;
    int pos =0;
    private Fragment homeFragment = new HomeFragment();
    private Fragment favoriteFragment = new FavoriteFragment();
    private Fragment downloadFragment = new DownloadFragment();
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initView();
        initAction();
        mHomeAdapter = new HomeAdapter(null, this, null);
    }
    private void initView() {
        navigationBarView = (BottomNavigationView) findViewById(R.id.bottom_navi);
//
////        attaching bottom sheet behaviour - hide / show on scroll
//        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigationBarView.getLayoutParams();
//        layoutParams.setBehavior(new BottomNavigationBehavior());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentManager, homeFragment).commit();
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            pos = data.getIntExtra("current_position", 0);
//            rcvCatogory.smoothScrollToPosition(pos);
//        }catch (Throwable e){
//            e.printStackTrace();
//        }
//    }
    private void initAction() {
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment selectedFragment =null;
                switch (item.getItemId()){
                    case R.id.action_home:
                        selectedFragment = homeFragment;
                        break;
                    case R.id.action_favorite:
                        selectedFragment = favoriteFragment;
                        break;
                    case R.id.action_download:
                        selectedFragment = downloadFragment;
                        break;
                    default:
                        selectedFragment = homeFragment;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentManager,selectedFragment).commit();
                return true;
            }
        });


    }

//    @Override
//    public void onClick(MovieImageModel model, int pos) {
//        Intent intent = new Intent(getContext(), DetailActivity.class);
//        intent.putExtra("myList", mMovieAdapter.getDataList());
//        intent.putExtra("position", pos);
//        startActivityForResult(intent, 123);
//
//    }
}
