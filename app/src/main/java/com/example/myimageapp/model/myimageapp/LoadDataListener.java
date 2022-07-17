package com.example.myimageapp.model.myimageapp;

import com.example.myimageapp.model.myimageapp.model.ImageModel;

import java.util.ArrayList;

public interface LoadDataListener {
    void onLoadDataFinished(ArrayList<ImageModel> arrayList);
}
