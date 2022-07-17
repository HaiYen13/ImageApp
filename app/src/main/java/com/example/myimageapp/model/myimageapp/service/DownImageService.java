package com.example.myimageapp.model.myimageapp.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myimageapp.model.myimageapp.utils.DebugHelper;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DownImageService extends IntentService {
    private Activity contextParent;
    public static final int UPDATE_PROCESS = 2810;

    public DownImageService(String name, Activity contextParent) {
        super(name);
        this.contextParent = contextParent;

    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<String> urls = intent.getStringArrayListExtra("urls");
        ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("receiver");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            saveImageToScopedStorage(urls,resultReceiver);
        }else{
            saveImageToExternalStorage(urls, resultReceiver);
        }
    }
    private void saveImageToExternalStorage(ArrayList<String> urls, ResultReceiver resultReceiver){
        int count;
        try {
            String root = Environment.getExternalStorageDirectory().getAbsolutePath();
            int lengthOfFile = 0;
            //Todo: Tinh tong khoi luong file
            for (String s : urls) {
                URL url = new URL(s);
                URLConnection connection = url.openConnection();
                connection.connect();
                lengthOfFile += connection.getContentLength();
            }
            //Todo: Download mang url
            long total = 0;
            for (String urlStr : urls) {
                URL url = new URL(urlStr);
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                OutputStream outputStream = new FileOutputStream(root + "/" + System.currentTimeMillis() + ".jpg");
                byte data[] = new byte[1024];
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    //TODO: publishing the progress....
                    Bundle resultData = new Bundle();
                    resultData.putInt("process", (int) ((total * 100) / lengthOfFile));
                    resultReceiver.send(UPDATE_PROCESS, resultData);
                    Thread.sleep(50);
                    DebugHelper.logDebug("% of dialog down", "" + total * 100 / lengthOfFile);
                    // writing data to file
                    outputStream.write(data, 0, count);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        Bundle resultData = new Bundle();
        resultData.putInt("process", 100);
        resultReceiver.send(UPDATE_PROCESS, resultData);
    }
    private void saveImageToScopedStorage(ArrayList<String> urls, ResultReceiver resultReceiver){

        try {
            //Todo: Tinh tong khoi luong file
            int lengthOfBitmap = 0;
            for (String s : urls) {
                URL url = new URL(s);
                URLConnection conection = url.openConnection();
                conection.connect();
                lengthOfBitmap += conection.getContentLength();
            }
            int count;
            long total = 0;
            for (String ulrStr : urls){
                Bitmap bitmap = getBitmapFromURL(ulrStr);
                Uri collection =
                        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);

                long date = System.currentTimeMillis();
                String extension = "jpg";
                //3
                ContentValues newImage = new ContentValues();
                newImage.put(MediaStore.Images.Media.DISPLAY_NAME, date+"."+extension);
                newImage.put(MediaStore.MediaColumns.MIME_TYPE, "image/"+extension);
                newImage.put(MediaStore.MediaColumns.DATE_ADDED, date);
                newImage.put(MediaStore.MediaColumns.DATE_MODIFIED, date);
                newImage.put(MediaStore.MediaColumns.SIZE, bitmap.getByteCount());
                newImage.put(MediaStore.MediaColumns.WIDTH, bitmap.getWidth());
                newImage.put(MediaStore.MediaColumns.HEIGHT, bitmap.getHeight());
                //4
                newImage.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+"/MyApp/");
                URL url = new URL(ulrStr);
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                byte data[] = new byte[1024];
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    Bundle resultData = new Bundle();
                    resultData.putInt("process", (int) ((total * 100) / lengthOfBitmap));
                    resultReceiver.send(UPDATE_PROCESS, resultData);
                    Thread.sleep(50);
                    DebugHelper.logDebug("progressDialog", total*100/lengthOfBitmap+"");
                }
                //5
                newImage.put(MediaStore.Images.Media.IS_PENDING, 1);
                Uri newImageUri = contextParent.getContentResolver().insert(collection, newImage);
                //6
                OutputStream outputStream = contextParent.getContentResolver().openOutputStream(newImageUri, "w");

                bitmap.compress(Bitmap.CompressFormat.JPEG,100, outputStream);
                newImage.clear();
                //7
                newImage.put(MediaStore.Images.Media.IS_PENDING, 0);
                //8
                contextParent.getContentResolver().update(newImageUri, newImage, null, null);
            }

        }catch (Throwable e){
            e.printStackTrace();
        }
        Bundle resultData = new Bundle();
        resultData.putInt("process", 100);
        resultReceiver.send(UPDATE_PROCESS, resultData);
    }
    private Bitmap getBitmapFromURL(String file_url){
        try {
            URL url = new URL(file_url);
            URLConnection connection = url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmap;
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}