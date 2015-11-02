package com.imark.nghia.idsdemo.ui.assign;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.windyroad.nghia.common.models.ActionResult;
import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.AssignData;
import com.imark.nghia.idsdemo.helper.AppAlertDialog;
import com.imark.nghia.idscore.helper.UserPref;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idsdemo.services.UploadDoingAssignsService;
import com.imark.nghia.idscore.tasks.GetNewAssignsWSTask;
import com.imark.nghia.idscore.tasks.ITaskResultListener;
import com.imark.nghia.idsdemo.adapters.AssignPagerAdapter;
import com.windyroad.nghia.common.models.FragmentPager;

import java.util.ArrayList;

public class ListAssignActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    public TabLayout mTabLayout;  // header
    public ViewPager mViewPager;
    public AssignPagerAdapter mAssignPagerAdapter;

    private ListAssignFragment mFragmentNew;
    private ListAssignFragment mFragmentDoing;
    private ListAssignFragment mFragmentFinish;
    private ProgressDialog mDialogDownload;
    private ProgressDialog mDialogUpload;
    private GetNewAssignsWSTask mTaskGetNewAssigns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_assign);

        initToolbar();
        initTab();
        findViews();
        initVars();
        setEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mReceiverUploading, new IntentFilter(UploadDoingAssignsService.ACTION_UPLOADING));
        registerReceiver(mReceiverUploadFinish, new IntentFilter(UploadDoingAssignsService.ACTION_UPLOAD_FINISH));
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mReceiverUploading);
        unregisterReceiver(mReceiverUploadFinish);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_assign, menu);

        initSearchMenuItem(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //startSearchAssignActivity();
                return true;
            case R.id.action_add:
                startAddAssignActivity();
                return true;
            case R.id.action_download:
                downloadAssignHandle();
                return true;
            case R.id.action_upload_doing_assign:
                uploadDoingAssignHandle();
                return true;
            case R.id.action_clear_new_assign:
                clearNewAssignHandle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //region Khởi tạo
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_Main);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);  // hiển thị Home/ Up
        }
    }

    private void initTab() {
        // Khởi tạo các List Fragment Pager
        mFragmentNew = ListAssignFragment.newInstance(Assign.WorkStatus.NEW);
        mFragmentDoing = ListAssignFragment.newInstance(Assign.WorkStatus.DOING);
        mFragmentFinish = ListAssignFragment.newInstance(Assign.WorkStatus.FINISHED);

        ArrayList<FragmentPager> listFragmentPager = new ArrayList<FragmentPager>() {{
            add(new FragmentPager(getString(R.string.title_tab_new_assign), 0, mFragmentNew));
            add(new FragmentPager(getString(R.string.title_tab_doing_assign), 0, mFragmentDoing));
            add(new FragmentPager(getString(R.string.title_tab_finish_assign), 0, mFragmentFinish));
        }};

        //Inflate View, find Views
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout_Main);  // header
        mViewPager = (ViewPager) findViewById(R.id.viewPager_Main);

        // Đặt Adapter cho View Pager
        mAssignPagerAdapter = new AssignPagerAdapter(getSupportFragmentManager(), this, listFragmentPager);
        mViewPager.setAdapter(mAssignPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);  // Đồng bộ TabLayout và ViewPager

        // Set Custom Header All Tabs
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            tab.setCustomView(mAssignPagerAdapter.getTabView(i));
        }

        // Set selection
        TabLayout.Tab tab1 = mTabLayout.getTabAt(1);
        tab1.select();
    }

    private void findViews() {

    }

    private void initVars() {
        mDialogDownload = new ProgressDialog(this);
        mDialogUpload = new ProgressDialog(this);


        // Lấy sự kiện từ Fragment về
        ListAssignFragment.OnItemClickListener fragListener = new ListAssignFragment.OnItemClickListener() {
            @Override
            public void onItemClick(Assign assign) {
                startViewAssignActivity(assign);
            }
        };
        mFragmentNew.setOnItemClickListener(fragListener);
        mFragmentDoing.setOnItemClickListener(fragListener);
        mFragmentFinish.setOnItemClickListener(fragListener);
    }

    private void setEvents() {
    }

    /** Khởi tạo Search Item */
    private void initSearchMenuItem(Menu menu){
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    // không có search
                    mFragmentNew.beginSearch(newText);

                } else {
                    // có search

                    mFragmentNew.cancelSearch();
                }
                return false;
            }
        });

        /* Auto complete Search Item
        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)
                searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        ArrayList<String> testList = new ArrayList<String>(){{
            add("dfa");
            add("dsfa");
            add("ẻwe");
            add("jhg");
            add("534fg");
        }};
        ArrayAdapter<String> testAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testList);
        searchAutoComplete.setAdapter(testAdapter);*/
    }
    //endregion


    //region Sự kiện
    private void downloadAssignHandle() {
        AppAlertDialog.showGetDataDialog(this, mDialogDownload);

        mTaskGetNewAssigns = new GetNewAssignsWSTask(
                this,
                UserPref.getUserPrefId(this),
                new ITaskResultListener() {
                    @Override
                    public void onPostExecuteResult(ActionResult result) {
                        mTaskGetNewAssigns = null;
                        mDialogDownload.dismiss();

                        if (result.getResult() == ActionResult.ResultStatus.SUCCESS){
                            // Thành công load lại List

                            Toast.makeText(getBaseContext(), getString(R.string.message_success), Toast.LENGTH_LONG).show();
                            initTab();

                        } else {
                            String errMsg = getString(R.string.message_error_server_plus) + result.getMessage();
                            Toast.makeText(getBaseContext(), errMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        mTaskGetNewAssigns.execute();
    }


    private void uploadDoingAssignHandle() {
        startService(new Intent(this, UploadDoingAssignsService.class));
    }


    /**
     * Xóa phân công mới
     */
    private void clearNewAssignHandle() {
        long rowEffect = AssignData.deleteBy_WorkStatus(this, Assign.WorkStatus.NEW);
        String msg = getString(R.string.message_delete_success, rowEffect);

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

        initTab();
    }


    private void startSearchAssignActivity() {
        startActivity(new Intent(this, SearchAssignActivity.class));
    }


    /** Nhận receiver đang Upload */
    private BroadcastReceiver mReceiverUploading = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            AppAlertDialog.showPostDataDialog(getBaseContext(), mDialogUpload);
        }
    };

    /** Nhận receiver Upload Finish */
    private BroadcastReceiver mReceiverUploadFinish = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mDialogUpload.dismiss();
            initVars();  // khởi tạo lại
        }
    };
    //endregion


    //region Hỗ trợ
    /** start Activity: Add Assign */
    private void startAddAssignActivity() {
        startActivity(new Intent(this, AddAssignActivity.class));
    }

    private void startViewAssignActivity(Assign assign) {
        Intent intent = new Intent(this, ViewAssignActivity.class);
        intent.putExtra(ViewAssignActivity.EXTRA_ASSIGN_ID, assign.get_id());
        startActivity(intent);
    }
    //endregion
}
