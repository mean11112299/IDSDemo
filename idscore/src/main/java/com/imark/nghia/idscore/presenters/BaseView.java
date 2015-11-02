package com.imark.nghia.idscore.presenters;

import android.content.Context;

/**
 * Created by Imark-N on 10/26/2015.
 * View chứa giá trị chung
 */
public interface BaseView {
    Context getContext();

    /**
     * Hiện chờ đợi
     */
    void showWaiting();

    /**
     * Dừng chờ đợi
     */
    void hideWaiting();
}
