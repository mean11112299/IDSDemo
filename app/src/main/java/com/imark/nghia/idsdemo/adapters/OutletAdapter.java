package com.imark.nghia.idsdemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.models.Outlet;
import com.imark.nghia.idscore.data.OutletData;

import java.util.ArrayList;
import java.util.List;

/**
 * Dừng lại đang lỗi Adapter
 * Created by Nghia-PC on 8/19/2015.
 * Outlet Adapter cho Spinner
 */
public class OutletAdapter extends ArrayAdapter<Outlet> {
    public static final long ACTION_ID_ADD = -1;

    private final Context mContext;
    private final int mLayoutResourceId;
    private List<Outlet> mListOutlet;
    private OnItemSelectedListener mListenerItemSelected;

    public OutletAdapter(Context context, int layoutResourceId, int textViewResourceId,  List<Outlet> listOutlet) {
        super(context, layoutResourceId, textViewResourceId, listOutlet);

        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
        this.mListOutlet = listOutlet;
    }

    /** lấy sự kiện trả về
     * @param listener
     */
    public void setItemSelectedListener(OnItemSelectedListener listener){
        mListenerItemSelected = listener;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        /** Sử dụng Cho SPINNER */
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // ----- Init View -----
        OutletViewHolder viewHolder;
        if (convertView == null){
            //R.layout.list_item_outlet
            convertView = ((Activity)mContext)
                    .getLayoutInflater().inflate(this.mLayoutResourceId, parent, false);

            viewHolder = new OutletViewHolder();
            viewHolder.textView_OutletName =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_OutletName);
            viewHolder.textView_OutletArea =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_OutletArea);
            viewHolder.textView_OutletAddress =
                    (TextView) convertView.findViewById(R.id.textView_RowItem_OutletAddress);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OutletViewHolder) convertView.getTag();
        }

        // ----- Set value - Display Giao diện ------
        Outlet outlet = mListOutlet.get(position);

        // Không có giá trị thì ẩn View
        String area = outlet.getDistrict() + " / " + outlet.getCity();

        viewHolder.textView_OutletName.setText(outlet.getName());
        viewHolder.textView_OutletArea.setText(area);
        viewHolder.textView_OutletAddress.setText(outlet.getAddress());

        // giao diện add new
        if (outlet.get_id() == ACTION_ID_ADD){
            viewHolder.textView_OutletArea.setVisibility(View.GONE);
            viewHolder.textView_OutletAddress.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        // tránh bị lỗi Out of Size
        return mListOutlet.size();
    }



    @Override
    public Filter getFilter() {
        /** Tạo Filter lọc kết quả trả về, dùng cho AUTOCOMPLETE TEXT VIEW*/
        return mNameFilter;
    }

    Filter mNameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {

            Outlet selectItem = (Outlet)resultValue;

            // Lấy kí tự chuỗi trả về
            String str = selectItem.getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {

                // Tạo list từ Kí tự
                // Trả về result từ kết quả
                mListOutlet = OutletData.search5byName(mContext, constraint.toString());

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListOutlet;
                filterResults.count = mListOutlet.size();

                return filterResults;

            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Outlet> filteredList = (ArrayList<Outlet>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Outlet c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };


    /**TÁc dụng: Hiệu suất */
    private class OutletViewHolder {
        TextView textView_OutletName;
        TextView textView_OutletArea;
        TextView textView_OutletAddress;
    }

    /**
     * Khi chọn Item nhận sự kiện
     */
    public interface OnItemSelectedListener {
        public void onItemSelected(Outlet outlet);
    }
}
