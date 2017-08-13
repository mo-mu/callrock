package com.momu.callrock.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

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

    @BindView(R.id.autoTextView) AutoCompleteTextView autoTextView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManager;
    ArrayList<SearchItem> items = new ArrayList<>();
    DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        context = this;


        getInitAddrDB();

        init();

        autoTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    btnSearchClick();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 페이지 초기화
     *
     */
    private void init() {

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        items.add(new SearchItem(0,"adsf",0,0));
    }

    /**
     * DB에서  주소 불러오는 코드
     */
    private void getInitAddrDB() {
        try {
            databaseAccess = DatabaseAccess.getInstance(context);
            databaseAccess.open();
            addrList = databaseAccess.getWholeAddress();

            LogHelper.e("count", "" + addrList.size());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, addrList);

            autoTextView.setAdapter(adapter);
            databaseAccess.close();

        }catch (Exception e){
            Log.e(e.getMessage()," : "+e.getLocalizedMessage());
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
    @OnClick(R.id.btn_search)
    void btnSearchClick(){
         try {

            databaseAccess = DatabaseAccess.getInstance(context);
            databaseAccess.open();

            items = databaseAccess.getSearchAddress(autoTextView.getText().toString());

            SearchAdapter searchAdapter = new SearchAdapter(getApplicationContext(),items);
            recyclerView.setAdapter(searchAdapter);

             databaseAccess.close();
        }catch (Exception e){
            Log.e(e.getMessage()," : "+e.getLocalizedMessage());
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
