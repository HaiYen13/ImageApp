package com.example.myimageapp.model.myimageapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.myimageapp.model.myimageapp.model.ImageModel;
import com.example.myimageapp.model.myimageapp.utils.DebugHelper;
import com.example.myimageapp.model.myimageapp.utils.MovieList;
import com.example.myimageapp.model.myimageapp.utils.SQLiteHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class LoadingDataAsynctask extends AsyncTask<String, Void, ArrayList<ImageModel>> {

    Context contextParent;
    LoadDataListener listener;
    ArrayList<ImageModel> movieList;
    SQLiteHelper sqLiteHelper;
    String tableName = "Favorite";

    public LoadingDataAsynctask(Context contextParent, LoadDataListener listener) {
        this.contextParent = contextParent;
        this.listener = listener;
        sqLiteHelper = new SQLiteHelper(contextParent);
    }

    public ArrayList<ImageModel> getMovieList() {
        return movieList;
    }

    public void setMovieList(ArrayList<ImageModel> movieList) {
        this.movieList = movieList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(contextParent, "Start" , Toast.LENGTH_SHORT).show();
    }

    @Override
    protected ArrayList<ImageModel> doInBackground(String... filesName) {
        ArrayList<Integer> listIdFav = sqLiteHelper.getArrayIdFav(tableName);
        movieList = new ArrayList<>();
        Gson gson = new Gson();
        for(int i = 0; i< filesName.length;i++) {
            String fileName = filesName[i];
            if(fileName == null)
                continue;
            DebugHelper.logDebug("MyAsyntask", "doInBackground:" + fileName);
            String imageList = MovieList.loadJsonFromAssets(contextParent, fileName);
            ArrayList<ImageModel> list = gson.fromJson(imageList, new TypeToken<List<ImageModel>>() {
            }
                    .getType());
            if(list!=null&&!list.isEmpty()){
                for(int j =0; j< list.size(); j++){
                    for(int k =0; k < listIdFav.size(); k++){
                        if(list.get(j).getId()==listIdFav.get(k)){
                            list.get(j).setIsFavorited(1);
                        }
                    }

                }
                movieList.addAll(list);
            }
        }
        return movieList;
    }

    @Override
    protected void onPostExecute(ArrayList<ImageModel> imageModels) {
        listener.onLoadDataFinished(imageModels);

    }

}