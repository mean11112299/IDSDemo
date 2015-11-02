package com.imark.nghia.idsdemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.models.Product;

import java.util.List;

/**
 * Created by Nghia-PC on 8/31/2015.
 * Hiển thị giao diện Product
 */
public class ProductAdapter extends ArrayAdapter<Product> {

    public static final int ACTION_ID_ADD = -1;  // kiểm tra chọn Thêm mới, -1 vì không có id này
    private final Context mContext;
    private final int mLayoutResourceId;
    private final List<Product> mListProduct;

    public ProductAdapter(Context context, int layoutResourceId, List<Product> listOutlet) {
        super(context, layoutResourceId, listOutlet);

        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
        this.mListProduct = listOutlet;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Product product = mListProduct.get(position);

        // ----- Init View -----
        ProductViewHolder viewHolder;
        if (convertView == null){
            //R.layout.list_item_outlet
            convertView = ((Activity)mContext)
                    .getLayoutInflater().inflate(this.mLayoutResourceId, parent, false);

            // find views
            viewHolder = new ProductViewHolder();
            viewHolder.textView_ProductName =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_ProductName);
            viewHolder.textView_Color =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_Color);
            viewHolder.textView_Price =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_Price);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductViewHolder) convertView.getTag();
        }

        // ----- Set value - Display Giao diện ------
        viewHolder.textView_ProductName.setText(product.getName());
        viewHolder.textView_Color.setText(product.getColor());
        viewHolder.textView_Price.setText(product.getPrice() + "");

        // init vars
        if (product.get_id() == ACTION_ID_ADD){
            // Cập nhật giao diện Add Item
            updateUIAddItem(viewHolder, true);
        } else {
            updateUIAddItem(viewHolder, false);
        }

        // set Events

        return convertView;
    }

    /** Cập nhật giao diện nút thêm mới
     * @param viewHolder
     * @param isAddItem
     */
    private void updateUIAddItem(ProductViewHolder viewHolder, boolean isAddItem) {
        if (isAddItem){
            viewHolder.textView_Color.setVisibility(View.GONE);
            viewHolder.textView_Price.setVisibility(View.GONE);
        } else {
            viewHolder.textView_Color.setVisibility(View.VISIBLE);
            viewHolder.textView_Price.setVisibility(View.VISIBLE);
        }
    }

    /**TÁc dụng: Hiệu suất */
    private class ProductViewHolder {

        TextView textView_ProductName;
        TextView textView_Color;
        TextView textView_Price;

    }
}
