package com.momu.callrock.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.momu.callrock.R;
import com.momu.callrock.adapter.SearchAdapter;
import com.momu.callrock.item.SearchItem;
import com.momu.callrock.sql.DatabaseAccess;
import com.momu.callrock.utility.LogHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 검색 페이지
 * Created by songm on 2017-07-09.
 */
public class SearchActivity extends AppCompatActivity {
    Context context;
    List<String> addrList;
    List<String> currentList = null;

    @BindView(R.id.editText) EditText editText;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.view_nothing) FrameLayout nothingView;

    RecyclerView.LayoutManager layoutManager;
    ArrayList<SearchItem> items = new ArrayList<>();
    DatabaseAccess databaseAccess;
    ArrayAdapter<String> adapter;

    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        context = this;

        getInitAddrDB();
        init();

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    btnSearchClick();
                }
                return false;
            }
        });
    }

    /**
     * 페이지 초기화
     */
    private void init() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * DB에서  주소 불러오는 코드
     */
    private void getInitAddrDB() {
        try {
            databaseAccess = DatabaseAccess.getInstance(context);
            databaseAccess.open();
            addrList = databaseAccess.getWholeAddress();
            databaseAccess.close();

            LogHelper.e("count", "" + addrList.size());
            currentList = addrList;
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, currentList);

        } catch (Exception e) {
            LogHelper.errorStackTrace(e);
        }
    }

    /**
     * 백버튼 클릭 이벤트
     */
    @OnClick(R.id.btn_back)
    void btnBackClick() {
        finish();
    }

    /**
     * 검색 버튼 클릭 이벤트
     */
    @OnClick(R.id.btn_refresh)
    void btnSearchClick() {
        if(!editText.getText().toString().equals("")) { //editText에 1글자라도 있을 때
            try {
                databaseAccess = DatabaseAccess.getInstance(context);
                databaseAccess.open();

                items = databaseAccess.getSearchAddress(editText.getText().toString());

                if (items.size() > 0) {
                    SearchAdapter searchAdapter = new SearchAdapter(SearchActivity.this, items);
                    recyclerView.setAdapter(searchAdapter);
                    nothingView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    nothingView.setVisibility(View.VISIBLE);
                }
                databaseAccess.close();

                editText.clearFocus();
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            } catch (Exception e) {
                LogHelper.errorStackTrace(e);
            }
        }else{  //editText에 글자가 없을 때
            recyclerView.setVisibility(View.GONE);
            nothingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
