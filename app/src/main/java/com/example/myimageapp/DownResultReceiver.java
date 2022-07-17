package com.example.myimageapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import com.example.myimageapp.service.DownImageService;

public class DownResultReceiver extends android.os.ResultReceiver {
    private ProgressDialog mProgressDialog;
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public DownResultReceiver(Handler handler, ProgressDialog progressDialog) {
        super(handler);
        mProgressDialog = progressDialog;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if(resultCode == DownImageService.UPDATE_PROCESS){
            int progress = resultData.getInt("process");
            mProgressDialog.setProgress(progress);

            if (progress == 100) {
                mProgressDialog.dismiss();
            }
        }
    }
}
