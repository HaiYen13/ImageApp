package com.example.myimageapp.model;

import android.content.Intent;

import java.io.Serializable;

public class ImageModel implements Serializable {
    private int id;
    private String name;
    private String url;
    private Integer isFavorited;
    private Integer isSelected;

    public ImageModel(int id, String name, String url, Integer isFavorited, Integer isSelected) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.isFavorited = isFavorited;
        this.isSelected = isSelected;
    }

    public ImageModel(String name, String img, Integer isDownLoaded) {
        this.name = name;
        this.url = img;
        this.isSelected = isDownLoaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(int isFavorited) {
        this.isFavorited = isFavorited;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }
}
