package com.imark.nghia.idsdemo.ui.assign;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idscore.data.models.Assign;
import com.imark.nghia.idsdemo.adapters.AssignAdapter;

import java.util.ArrayList;

/**
 * Chưa hoàn thành
 */
public class SearchAssignActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ListView mListView_Assign;
    private ArrayList<Assign> mListAssign;
    private AssignAdapter mAdapterAssign;

    private TextView mTextView_Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_assign);

        initToolbar();
        findViews();
        initVars();
        setEvents();
    }

    //region Khởi tạo
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_Main);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);  // hiển thị Home/ Up

            // thêm search text View
            actionBar.setCustomView(mTextView_Search);
        }
    }

    private void findViews() {
        mListView_Assign = (ListView) findViewById(R.id.listView_Assign);
    }

    private void initVars() {
        mListAssign = new ArrayList<>();
        mAdapterAssign = new AssignAdapter(this, R.layout.list_item_assign, mListAssign);
        mListView_Assign.setAdapter(mAdapterAssign);

        mTextView_Search = new TextView(this);
    }

    private void setEvents() {
        mListView_Assign.setOnItemSelectedListener(mListView_Assign_itemSelected);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_assign, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemSelectedListener mListView_Assign_itemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Assign assign = mListAssign.get(i);
            startViewAssignActivity(assign);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    /**
     * Đến View Assign Activity
     * @param assign
     */
    private void startViewAssignActivity(Assign assign) {
        Intent intent = new Intent(this, ViewAssignActivity.class);
        intent.putExtra(ViewAssignActivity.EXTRA_ASSIGN_ID, assign.get_id());
        startActivity(intent);
    }
}
