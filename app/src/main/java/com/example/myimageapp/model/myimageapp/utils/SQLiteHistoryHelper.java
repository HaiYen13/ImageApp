package com.example.myimageapp.model.myimageapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myimageapp.model.myimageapp.model.ImageModel;

import java.util.ArrayList;

public class SQLiteHistoryHelper extends SQLiteOpenHelper {

    static final String DB_HISTORY_TABLE = "History";
    SQLiteDatabase sqLiteDatabase;
    static final String DB_NAME = "History.db";     //Tên database
    static final int DB_VERSION = 1;
    ContentValues contentValues;
    Cursor cursor;
    public SQLiteHistoryHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryDownCreateTable = "CREATE TABLE History( id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "+
                " name VARCHAR(200) NOT NULL , "+
                " img VARCHAR(200) NOT NULL, "+
                " isDownLoaded INTERGER NOT NULL)";
        db.execSQL(queryDownCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == newVersion) {
            db.execSQL("drop table if exists " + DB_HISTORY_TABLE);
            onCreate(db);
        }
    }
    //TODO: them  bang ghi vao history
    public void insertListHistory(ArrayList<ImageModel> arrayList, String tableName) {
        try {
            sqLiteDatabase = getWritableDatabase();
            //TODO: Tao bien noi dung can them
            for (ImageModel model : arrayList) {
                contentValues = new ContentValues();
                contentValues.put("name", model.getName());
                contentValues.put("img", model.getUrl());
                contentValues.put("isDownLoaded", model.getIsSelected());
                sqLiteDatabase.insert(tableName, null, contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
    public void insertHistory(ImageModel model, String tableName) {
        try {
            sqLiteDatabase = getWritableDatabase();
                contentValues = new ContentValues();
                contentValues.put("name", model.getName());
                contentValues.put("img", model.getUrl());
                contentValues.put("isDownLoaded", model.getIsSelected());
                sqLiteDatabase.insert(tableName, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
    public boolean deleteAll(String tableName){
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(tableName, null, null);
        closeDB();
        return true;
    }

    public void closeDB() {
        if(sqLiteDatabase != null) sqLiteDatabase.close();
        if(contentValues != null) contentValues.clear();
        if(cursor!= null) cursor.close();
    }
    public ArrayList<ImageModel> getDownloadList(String tableName){
        ArrayList<ImageModel> models = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        cursor =sqLiteDatabase.query(true, tableName, null , null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String img = cursor.getString(cursor.getColumnIndex("img"));
            Integer isDownLoaded = cursor.getInt(cursor.getColumnIndex("isDownLoaded"));
            ImageModel imageModel =new ImageModel(name, img, isDownLoaded);
            models.add(imageModel);
        }
        return models;
    }
}