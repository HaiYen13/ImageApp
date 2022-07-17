package com.example.myimageapp;

import com.example.myimageapp.model.ImageModel;

import java.util.ArrayList;

public interface LoadDataListener {
    void onLoadDataFinished(ArrayList<ImageModel> arrayList);
}
