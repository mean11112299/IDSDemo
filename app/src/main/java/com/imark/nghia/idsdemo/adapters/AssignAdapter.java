package com.imark.nghia.idsdemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.helper.AppConfig;
import com.windyroad.nghia.common.ConvertUtil;

import java.util.List;

/**
 * Created by Nghia-PC on 8/24/2015.
 * Kết nối Assign và giao diện
 */
public class AssignAdapter extends ArrayAdapter<Assign> {
    public static final long ACTION_ID_ADD = -1;

    private final Context mContext;
    private final int mLayoutResourceId;
    private final List<Assign> mListAssign;

    public AssignAdapter(Context context, int layoutResourceId, List<Assign> listAssign) {
        super(context, layoutResourceId, listAssign);

        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
        this.mListAssign = listAssign;
    }

   /* @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // ----- Init View -----
        AssignViewHolder viewHolder;
        if (convertView == null){
            //R.layout.list_item_outlet
            convertView = ((Activity)mContext)
                    .getLayoutInflater().inflate(this.mLayoutResourceId, parent, false);
            viewHolder = new AssignViewHolder();
            viewHolder.textView_OutletName =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_OutletName);
            viewHolder.textView_OutletArea =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_OutletArea);
            viewHolder.textView_OutletAddress =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_OutletAddress);
            viewHolder.textView_ExpiredDate =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_ExpiredDate);
            viewHolder.imageView_WorkStatus =
                    (ImageView) convertView.findViewById(R.id.imageView_RowItem_WorkStatus);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AssignViewHolder) convertView.getTag();
        }

        // ----- Set value - Display Giao diện ------
        Assign assign = mListAssign.get(position);

        String expiredAt = ConvertUtil.Calendar2String(assign.getExpiredAt(), AppConfig.APP_DATE_FORMAT, null);

        viewHolder.textView_OutletName.setText(assign.getOutletName());
        viewHolder.textView_OutletArea.setText(assign.getOutletCity());
        viewHolder.textView_OutletAddress.setText(assign.getOutletAddress());
        viewHolder.textView_ExpiredDate.setText(expiredAt);
        // workStatus image
        int statusResourceId = -1;
        switch (assign.getWorkStatus()){
            case NEW:
                statusResourceId = R.drawable.ic_new_black_24dp;
                break;
            case DOING:
                statusResourceId = R.drawable.ic_doing_24dp;
                break;
            case FINISHED:
                statusResourceId = R.drawable.ic_finish_24dp;
                break;
        }
        viewHolder.imageView_WorkStatus
                .setImageDrawable(ContextCompat.getDrawable(mContext, statusResourceId));

        return convertView;
    }

    /**TÁc dụng: Hiệu suất */
    private class AssignViewHolder {

        TextView textView_OutletName;
        TextView textView_OutletArea;
        TextView textView_OutletAddress;
        TextView textView_ExpiredDate;
        ImageView imageView_WorkStatus;

    }
}
