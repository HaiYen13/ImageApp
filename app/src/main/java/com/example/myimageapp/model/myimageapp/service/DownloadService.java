package com.example.myimageapp.model.myimageapp.service;

import static com.example.myimageapp.home.MyApplication.CHANEL_ID;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.myimageapp.R;
import com.example.myimageapp.model.Broadcast;
import com.example.myimageapp.model.myimageapp.home.HomeFragment;
import com.example.myimageapp.model.myimageapp.utils.DebugHelper;

import java.util.ArrayList;

public class DownloadService extends Service {

    private static final String TAG = "Download";
    private Activity contextParent;
    private boolean isRunning = false;
    private Broadcast downBroadcast;
    private final IBinder binder = new MyBinder();
    private static final String DOWNLOADNG_ACTION= "com.yenvth.ACTION";
    private static final String DOWNLOADNG_TEXT= "com.yenvth.Texy";

    public class MyBinder extends Binder{
        DownloadService getService(){
            return DownloadService.this;
        }
    }

//    public DownloadService(Activity contextParent) {
//        this.contextParent = contextParent;
//        progressDialog = new ProgressDialog(contextParent);
//        progressDialog.setIndeterminate(false);
//        progressDialog.setMax(100);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        DebugHelper.logDebug(TAG, "Service onBind");
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        DebugHelper.logDebug(TAG, "Service onCreate");
        isRunning = true;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ArrayList<String> strKeyIntent = intent.getStringArrayListExtra("key_down_intent");
        DebugHelper.logDebug(TAG, "Service onStartCommand");
//    //TODO: Creating new thread for my service
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        try {
            startDownloadFile(strKeyIntent);
//            sendNotification(strKeyIntent);
            downSendBroadcast();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isRunning) {
            DebugHelper.logDebug(TAG, "Service running");
        }
        //TODO: Stop service once it finishes its task
        stopSelf();
        return START_NOT_STICKY;
            }

    private void startDownloadFile(ArrayList<String> urls) {
        for(String url : urls){
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setTitle("Download");
            request.setDescription("Download image");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()+ ".jpg"));
            DownloadManager downloadManager = (DownloadManager) getSystemService(contextParent.DOWNLOAD_SERVICE);
            if(downloadManager != null){
                downloadManager.enqueue(request);
/*                progressDialog.show();*/
            }
        }
    }

    private void sendNotification(ArrayList<String> urls) {
        Intent intent = new Intent(this, HomeFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        for(String url : urls){
            Notification notification = new NotificationCompat.Builder(this, CHANEL_ID)
                    .setContentTitle("Đang tải hình ảnh xuống")
                    .setContentText(url)
                    .setSmallIcon(R.drawable.ic_download)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
        }
    }

    private void downSendBroadcast() {
        Intent intent = new Intent(DOWNLOADNG_ACTION);
        intent.putExtra(DOWNLOADNG_TEXT, "Image is loading");
        sendBroadcast(intent);
    }



    @Override
    public boolean onUnbind(Intent intent) {
        DebugHelper.logDebug(TAG, "Service onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DebugHelper.logDebug(TAG, "Service onDestroy");
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();

    }
}
