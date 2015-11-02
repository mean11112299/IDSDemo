package com.imark.nghia.idsdemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.AssignImageData;
import com.imark.nghia.idscore.data.models.CodeDetail;

import java.util.ArrayList;

/**
 * Created by Nghia-PC on 8/27/2015.
 * Dùng cho List 2 dòng
 */
public class AssignImageTypeAdapter extends BaseAdapter {

    private final int mLayoutId;
    private final long mAssignId;
    private Context mContext;
    private ArrayList<CodeDetail> mListCodeDetail;

    public AssignImageTypeAdapter(Context context, int layoutId, ArrayList<CodeDetail> listCodeDetail, long assignId) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mListCodeDetail = listCodeDetail;
        this.mAssignId = assignId;
    }

    @Override
    public int getCount() {
        return mListCodeDetail.size();
    }

    @Override
    public Object getItem(int position) {
        return mListCodeDetail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // ----- Init View -----
        ViewHolder viewHolder2Line;
        if (convertView == null){
            convertView = ((Activity) mContext).getLayoutInflater().inflate(mLayoutId, parent, false);

            viewHolder2Line = new ViewHolder();
            viewHolder2Line.textView_ImageType = (TextView) convertView
                    .findViewById(R.id.textView_RowItem_ImageType);
            viewHolder2Line.textView_CountImage = (TextView) convertView
                    .findViewById(R.id.textView_RowItem_CountImage);

            convertView.setTag(viewHolder2Line);
        } else {
            viewHolder2Line = (ViewHolder) convertView.getTag();
        }

        // ----- Display lên giao diện -------
        CodeDetail codeDetail = mListCodeDetail.get(position);

        int totalImage = AssignImageData
                .countBy_AssignId_ImageType(mContext, mAssignId, codeDetail.getValue());

        viewHolder2Line.textView_ImageType.setText(codeDetail.getName());
        viewHolder2Line.textView_CountImage.setText(totalImage+" ");

        return convertView;
    }

    private class ViewHolder {
        TextView textView_ImageType;
        TextView textView_CountImage;
    }
}
