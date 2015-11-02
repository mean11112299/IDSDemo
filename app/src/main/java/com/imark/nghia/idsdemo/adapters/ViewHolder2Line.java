package com.imark.nghia.idsdemo.adapters;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Nghia-PC on 8/27/2015.
 */
public class ViewHolder2Line {
    TextView textView_1;
    TextView textView_2;

    /**
     * Khởi tạo, tìm View sẵn
     */
    public ViewHolder2Line(View rootView){
        textView_1 = (TextView) rootView.findViewById(android.R.id.text1);
        textView_2 = (TextView) rootView.findViewById(android.R.id.text2);
    }
}
