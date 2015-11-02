package com.windyroad.nghia.common.activity;

import android.content.Intent;

/**
 * todo mới
 * Created by Imark-N on 11/2/2015.
 * Nghe sự kiện trả về cho Presenter
 */
public interface IActivityResultListener {
    void onResult(int resultCode, Intent data);
}
