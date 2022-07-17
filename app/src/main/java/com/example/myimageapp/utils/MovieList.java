package com.example.myimageapp.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class MovieList{
    public static String loadJsonFromAssets(Context context, String fileName){
        String reader = null;
        try{
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            reader = new String(buffer, "UTF-8");

        }catch (IOException exception){
            exception.printStackTrace();
            return null;
        }
        return reader;

    }
}
