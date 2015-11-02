package com.imark.nghia.idsdemo.ui.assign;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idsdemo.adapters.AssignAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnItemClickListener} interface
 * to handle interaction events.
 * Use the {@link ListAssignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListAssignFragment extends Fragment {
    private static final String KEY_WORK_STATUS = "work_status";
    private Assign.WorkStatus mWorkStatus;
    private OnItemClickListener mListener;

    private TextView mTextView_NoData;
    private ListView mListView_Assign;
    private ArrayList<Assign> mListAssign;
    private AssignAdapter mAdapterAssign;

    /**
     * Khởi tạo
     */
    public static ListAssignFragment newInstance(Assign.WorkStatus workStatus) {
        ListAssignFragment fragment = new ListAssignFragment();

        Bundle args = new Bundle();
        args.putString(KEY_WORK_STATUS, workStatus.name());

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Khởi tạo Sự kiện
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lấy giá trị
        if (getArguments() != null) {
            String strWorkStatus = getArguments().getString(KEY_WORK_STATUS);
            mWorkStatus = Assign.WorkStatus.valueOf(strWorkStatus);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_assign, container, false);

        findViews(rootView);
        // initVars();
        setEvents();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Khởi tạo lại khi trở lại
        initVars();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    //region Khởi tạo
    private void findViews(View rootView) {
        mTextView_NoData = (TextView) rootView.findViewById(R.id.textView_NoData);
        mListView_Assign = (ListView) rootView.findViewById(R.id.listView_Fragment_Assign);
    }

    private void initVars() {
        initListViewAssign();

        // Không có dữ liệu => hiện no data
        if (mListAssign.size() > 0)
            mTextView_NoData.setVisibility(View.GONE);
        else
            mTextView_NoData.setVisibility(View.VISIBLE);
    }

    private void setEvents() {
        mListView_Assign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemPressed(position);
            }
        });
    }
    //endregion


    //region Sự kiện
    public void onItemPressed(int position) {
        if (mListener != null) {
            // Đẩy sự kiện về Container View
            Assign selectAssign = mAdapterAssign.getItem(position);
            mListener.onItemClick(selectAssign);
        }
    }

    /**
     * Bắt đầu tìm kiếm
     * @param strKey từ khóa
     */
    public void beginSearch(String strKey) {
        mListAssign = AssignData
                .getSearchByWorkStatus(getActivity(), UserPref.getUserPrefId(getActivity()), mWorkStatus, strKey);
        mAdapterAssign = new AssignAdapter(getActivity(), R.layout.list_item_assign, mListAssign);
        mListView_Assign.setAdapter(mAdapterAssign);
    }

    /**
     * Hủy tìm kiếm
     */
    public void cancelSearch() {
        initListViewAssign();
    }
    //endregion


    //region Hỗ trợ
    void initListViewAssign(){
        mListAssign = AssignData
                .getByWorkStatus(getActivity(), UserPref.getUserPrefId(getActivity()), mWorkStatus);
        mAdapterAssign = new AssignAdapter(getActivity(), R.layout.list_item_assign, mListAssign);
        mListView_Assign.setAdapter(mAdapterAssign);
    }

    //endregion


    /**
     * Interface Tương tác
     */
    public interface OnItemClickListener {
        public void onItemClick(Assign assign);
    }

}
