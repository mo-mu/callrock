package com.momu.callrock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.momu.callrock.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navView) NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initDrawer();
    }

    /**
     * Navigation drawer 초기화
     */
    void initDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /**
     * 검색 버튼 클릭 이벤트
     */
    @OnClick(R.id.btn_search)
    void btnSearchClick() {
        startActivity(new Intent(MainActivity.this, SearchActivity.class));
    }

    /**
     * 설정 버튼 클릭 이벤트
     */
    @OnClick(R.id.btn_setting)
    void btnSettingClick() {
        drawerLayout.openDrawer(navigationView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
