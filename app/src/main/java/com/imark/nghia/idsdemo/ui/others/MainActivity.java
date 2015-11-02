package com.imark.nghia.idsdemo.ui.others;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.imark.nghia.idsdemo.R;
import com.imark.nghia.idsdemo.ui.assign.ListAssignFragment;

public class MainActivity extends AppCompatActivity {

    private View mLayout_Content;  // chứa SnackBar (Giống Toast), Toolbar
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFab();
        initToolbar();
        setupDrawerLayout();

        findViews();
        initVars();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Khởi tạo Floating Action Button */
    private void initFab() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mLayout_Content, "FAB Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /** Khởi tạo Toolbar */
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_Main);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);  // hiện icon back home
        }
    }

    /** Cài đặt Drawer Menu */
    private void setupDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    /** Drawer Select Item */
    private void selectDrawerItem(MenuItem menuItem) {

        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()){
            case R.id.drawer_assignment:
                fragmentClass = ListAssignFragment.class;
                break;
            default:
                fragmentClass = ListAssignFragment.class;
                break;
        }
        try{
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        replaceFragment(fragment);

        Snackbar.make(mLayout_Content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
        setTitle(menuItem.getTitle());  // đổi Title
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
    }

    /** Đổi Fragment Contain Nội dung */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction fragTran = fragManager.beginTransaction();
        fragTran.replace(R.id.layout_MainContainer, fragment);
        fragTran.commit();
    }


    private void findViews() {
        mLayout_Content = findViewById(R.id.layout_Content);
    }

    private void initVars() {
        //Chọn Home mặc định
        //replaceFragment(new AssignmentFragment());
    }
}
