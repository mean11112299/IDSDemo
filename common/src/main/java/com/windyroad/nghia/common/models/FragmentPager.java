package com.windyroad.nghia.common.models;

import android.support.v4.app.Fragment;

/**
 * Created by Nghia-PC on 8/28/2015.
 * Item dùng cho [Fragment Pager Adapter]
 * Gồm có Fragment + Title + ImageResourceId
 */
public class FragmentPager {
    private String title;
    private int imageId;
    private Fragment fragment;

    public FragmentPager() {
    }

    public FragmentPager(String title, int imageId, Fragment fragment) {
        this.title = title;
        this.imageId = imageId;
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
