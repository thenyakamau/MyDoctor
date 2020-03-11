package com.example.mydoctor.ui.dashboardactivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mydoctor.R;
import com.example.mydoctor.baseviews.BaseActivity;
import com.example.mydoctor.ui.fragments.homefragment.HomeFragment;
import com.example.mydoctor.ui.mapactivity.MapActivity;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id._toolbar_dashboard)
    Toolbar _toolbar_dashboard;
    @BindView(R.id._nav_viewer_dashboard)
    NavigationView _nav_viewer_dashboard;
    @BindView(R.id._drawer_dashboard)
    DrawerLayout _draw_layout_dashboard;

    private  HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        ButterKnife.bind(this);

        initializeToolBar();

        homeFragment = new HomeFragment();

        _nav_viewer_dashboard.setNavigationItemSelectedListener(this);
        _nav_viewer_dashboard.setCheckedItem(R.id._dash_board_home);

    }

    private void initializeToolBar() {

        setSupportActionBar(_toolbar_dashboard);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, _draw_layout_dashboard, _toolbar_dashboard, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        _draw_layout_dashboard.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {

        if (_draw_layout_dashboard.isDrawerOpen(GravityCompat.START)) {

            _draw_layout_dashboard.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id._dash_board_home:
                AddFragment(homeFragment);
                return true;

            case R.id._dash_board_map:
                startActivity(new Intent(DashBoardActivity.this, MapActivity.class));
                return true;

        }

        _draw_layout_dashboard.closeDrawer(GravityCompat.START);
        return true;
    }

    public void AddFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id._dash_board_frame_layout, fragment);
        fragmentTransaction.commit();

    }


}
