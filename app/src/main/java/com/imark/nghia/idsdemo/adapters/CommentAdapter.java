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
import com.imark.nghia.idscore.data.models.Comment;

import java.util.List;

/**
 * Created by Nghia-PC on 9/16/2015.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

    public static final int ACTION_ID_ADD = -1;  // kiểm tra chọn Thêm mới, -1 vì không có id này
    private final Context mContext;
    private final int mLayoutResourceId;
    private final List<Comment> mListComment;

    public CommentAdapter(Context context, int layoutResourceId, List<Comment> listOutlet) {
        super(context, layoutResourceId, listOutlet);

        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
        this.mListComment = listOutlet;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Comment comment = mListComment.get(position);

        // ----- Init View -----
        CommentViewHolder viewHolder;
        if (convertView == null) {
            //R.layout.list_item_outlet
            convertView = ((Activity) mContext)
                    .getLayoutInflater().inflate(this.mLayoutResourceId, parent, false);

            // find views
            viewHolder = new CommentViewHolder();
            viewHolder.textView_Advantage =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_Advantage);
            viewHolder.textView_Disadvantage =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_Disadvantage);
            viewHolder.textView_Suggestion =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_Suggestion);
            viewHolder.imageView_UploadStatus =
                    (ImageView) convertView.findViewById(R.id.imageView_RowItem_UploadStatus);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommentViewHolder) convertView.getTag();
        }

        // ----- Set value - Display Giao diện ------
        viewHolder.textView_Advantage.setText(comment.getAdvantage());
        viewHolder.textView_Disadvantage.setText(comment.getDisadvantage());
        viewHolder.textView_Suggestion.setText(comment.getSuggestion() + "");

        // workStatus image
        int statusResourceId = -1;
        switch (comment.getUploadStatus())
        {
            case WAITING_UPLOAD:
                statusResourceId = R.drawable.ic_new_black_24dp;
                break;
            case UPLOADING:
                statusResourceId = R.drawable.ic_doing_24dp;
                break;
            case UPLOADED:
                statusResourceId = R.drawable.ic_finish_24dp;
                break;
        }
        viewHolder.imageView_UploadStatus
                .setImageDrawable(ContextCompat.getDrawable(mContext, statusResourceId));

        // set Events

        return convertView;
    }

    /**
     * TÁc dụng: Hiệu suất
     */
    private class CommentViewHolder {

        TextView textView_Advantage;
        TextView textView_Disadvantage;
        TextView textView_Suggestion;
        ImageView imageView_UploadStatus;
    }
}
