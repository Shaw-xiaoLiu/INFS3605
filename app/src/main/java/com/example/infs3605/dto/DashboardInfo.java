package com.example.infs3605.dto;

import androidx.annotation.DrawableRes;

public class DashboardInfo {
    private final String title;
    @DrawableRes
    private final int image;

    public DashboardInfo(String title, @DrawableRes int image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public int getImage() {
        return image;
    }
}
