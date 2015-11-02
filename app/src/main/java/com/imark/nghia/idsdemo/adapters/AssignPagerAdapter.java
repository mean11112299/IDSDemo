package com.imark.nghia.idsdemo.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.imark.nghia.idsdemo.R;
import com.windyroad.nghia.common.models.FragmentPager;

import java.util.ArrayList;

/**
 * Created by Nghia-PC on 8/26/2015.
 * Adapter cho ViewPager Assign (hiển thị theo Page)
 */
public class AssignPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private ArrayList<FragmentPager> mListAssignTab;

    public AssignPagerAdapter(FragmentManager fm, Context context, ArrayList<FragmentPager> listFragmentPager) {
        super(fm);
        this.mContext = context;

        // Mảng Tiêu đề, và Fragment Assign
        mListAssignTab = listFragmentPager;
    }

    @Override
    public int getCount() {
        return mListAssignTab.size();
    }

    /**
     * Trả về tiêu đề Liên quan Vị trí
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mListAssignTab.get(position).getTitle();
    }

    /**
     * Trả về Fragment có Liên quan Vị trí
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        Fragment resultFrag = mListAssignTab.get(position).getFragment();
        return resultFrag;
    }


    /**
     * Tạo custom View Header trả về, hiển thị
     * @param position
     * @return
     */
    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.tab_header_assign, null);

        TextView tv = (TextView) itemView.findViewById(R.id.textView);
        tv.setText(mListAssignTab.get(position).getTitle());

        return itemView;
    }
}
