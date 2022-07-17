package com.example.myimageapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.myimageapp.model.ImageModel;

import java.util.ArrayList;
import java.util.Stack;

public class SQLiteHelper extends SQLiteOpenHelper {
    static final String DB_FAVORITE_TABLE = "Favorite";
    SQLiteDatabase sqLiteDatabase;
    static final String DB_NAME = "Image.db";     //Tên database
    static final int DB_VERSION = 1;
    ContentValues contentValues;
    Cursor cursor;
    public SQLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryFavCreateTable = "CREATE TABLE favorite( id INTEGER NOT NULL PRIMARY KEY, "+
                " name VARCHAR(200) NOT NULL , "+
                " img VARCHAR(200) NOT NULL, "+
                " isFavorited INTERGER NOT NULL, "+
                " isDownLoaded INTERGER NOT NULL)";
        db.execSQL(queryFavCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion == newVersion){
            db.execSQL(" drop table if exists " + DB_FAVORITE_TABLE);
            onCreate(db);
        }
    }
    public boolean insertFavorite(ImageModel model, String nameTable){
        try {
            sqLiteDatabase =getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT id FROM Favorite where id = ?", new String[]{model.getId() + ""});
            if(cursor.moveToNext()){
                return false;
            }else{
                contentValues = new ContentValues();
                contentValues.put("id", model.getId());
                contentValues.put("name", model.getName());
                contentValues.put("img", model.getUrl());
                contentValues.put("isFavorited", model.getIsFavorited());
                contentValues.put("isDownLoaded", model.getIsSelected());
                return sqLiteDatabase.insert(nameTable,null, contentValues) != -1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        }
        return false;
    }
    public void delete(String tableName, int id){
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(tableName, "id"+"="+id, null);
    }
    public boolean deleteAll(String tableName){
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(tableName, null, null);
        closeDB();
        return true;
    }

    private void closeDB() {
        if(sqLiteDatabase != null) sqLiteDatabase.close();
        if(contentValues != null) contentValues.clear();
        if(cursor!= null) cursor.close();
    }
    public ArrayList<ImageModel> getArrayListData(String tableName){
        ArrayList<ImageModel> imageModels = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        cursor =sqLiteDatabase.query(true,tableName, null , null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Integer id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String img = cursor.getString(cursor.getColumnIndex("img"));
            Integer isFavorited = cursor.getInt(cursor.getColumnIndex("isFavorited"));
            Integer isDownLoaded = cursor.getInt(cursor.getColumnIndex("isDownLoaded"));
            ImageModel imageModel =new ImageModel(id, name, img, isFavorited, isDownLoaded);
            imageModels.add(imageModel);
        }
        return imageModels;
    }
    public ArrayList<Integer> getArrayIdFav(String tableName){
        ArrayList<Integer> listIdFavs = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        cursor = sqLiteDatabase.query(true, tableName,null, null, null,null, null, null, null);
        while (cursor.moveToNext()){
            Integer id = cursor.getInt(cursor.getColumnIndex("id"));
            listIdFavs.add(id);
        }
        return  listIdFavs;
    }
}
