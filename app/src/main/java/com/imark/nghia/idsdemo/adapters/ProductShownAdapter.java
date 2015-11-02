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
import com.imark.nghia.idscore.data.models.ProductShown;

import java.util.List;

/**
 * Created by Nghia-PC on 9/16/2015.
 */
public class ProductShownAdapter extends ArrayAdapter<ProductShown> {

    public static final int ACTION_ID_ADD = -1;  // kiểm tra chọn Thêm mới, -1 vì không có id này
    private final Context mContext;
    private final int mLayoutResourceId;
    private final List<ProductShown> mListProductShown;

    public ProductShownAdapter(Context context, int layoutResourceId, List<ProductShown> listProductShown) {
        super(context, layoutResourceId, listProductShown);

        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
        this.mListProductShown = listProductShown;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ProductShown productShown = mListProductShown.get(position);

        // ----- Init View -----
        ProductShownViewHolder viewHolder;
        if (convertView == null) {
            //R.layout.list_item_outlet
            convertView = ((Activity) mContext)
                    .getLayoutInflater().inflate(this.mLayoutResourceId, parent, false);

            // find views
            viewHolder = new ProductShownViewHolder();
            viewHolder.textView_Product =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_Product);
            viewHolder.textView_Number =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_Number);
            viewHolder.textView_RetailPrice =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_RetailPrice);
            viewHolder.imageView_UploadStatus =
                    (ImageView) convertView.findViewById(R.id.imageView_RowItem_UploadStatus);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductShownViewHolder) convertView.getTag();
        }

        // ----- Set value - Display Giao diện ------
        viewHolder.textView_Product.setText(productShown.getProduct(mContext).getName());
        viewHolder.textView_Number.setText(productShown.getNumber()+"");
        viewHolder.textView_RetailPrice.setText(productShown.getRetailPrice()+"");

        // workStatus image
        int statusResourceId = -1;
        switch (productShown.getUploadStatus())
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
    private class ProductShownViewHolder {

        TextView textView_Product;
        TextView textView_Number;
        TextView textView_RetailPrice;
        ImageView imageView_UploadStatus;
    }
}
