package com.momu.callrock.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
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
    List<String> currentList = null;

    @BindView(R.id.autoTextView) AutoCompleteTextView autoTextView;
//    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    //    RecyclerView.LayoutManager layoutManager;
//    ArrayList<SearchItem> items = new ArrayList<>();
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
//        init();

        autoTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    btnSearchClick();
                    return true;
                }
                return false;
            }
        });

        autoTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                LogHelper.e(TAG, arg0.getItemAtPosition(position).toString());
                Toast.makeText(SearchActivity.this, "선택한 주소는 " + arg0.getItemAtPosition(position).toString() + " 입니다. 이 주소로 좌표를 검색한 다음 해당 좌표로 서버에 검색을 때린 다음 SearchActivity를 닫는 것이 좋을 것 같네요. ", Toast.LENGTH_SHORT).show();
            }

        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            autoTextView.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if(currentList != null) {
                        currentList.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    /**
     * 페이지 초기화
     */
    private void init() {
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(context);
//        recyclerView.setLayoutManager(layoutManager);
//        items.add(new SearchItem(0, "adsf", 0, 0));
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
    @OnClick(R.id.btn_search)
    void btnSearchClick() {
        if (adapter != null) {
            currentList = addrList;
            autoTextView.setAdapter(adapter);
        }
        if (autoTextView.isPopupShowing()) {
            LogHelper.e(TAG, "popup  showing");
        } else {
            LogHelper.e(TAG, "popup not showing");
            autoTextView.showDropDown();
        }

        try {
//            databaseAccess = DatabaseAccess.getInstance(context);
//            databaseAccess.open();
//
//            items = databaseAccess.getSearchAddress(autoTextView.getText().toString());
//
//            SearchAdapter searchAdapter = new SearchAdapter(getApplicationContext(),items);
//            recyclerView.setAdapter(searchAdapter);
//
//             databaseAccess.close();
        } catch (Exception e) {
            LogHelper.errorStackTrace(e);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
