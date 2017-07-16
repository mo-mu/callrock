package com.momu.callrock.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.momu.callrock.adapter.SearchArrayAdapter;
import com.momu.callrock.item.SearchDropdownItem;
import com.momu.callrock.R;
import com.momu.callrock.utility.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 검색 페이지
 * Created by songm on 2017-07-09.
 */
public class SearchActivity extends AppCompatActivity {
    Context context;
    ArrayList<SearchDropdownItem> addrList = new ArrayList<>();
    ;
    ArrayAdapter adapter;

    @BindView(R.id.autoTextView) AutoCompleteTextView autoTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        context = this;

        SharedPreferences prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        String text = prefs.getString("Observe", "");

        try {
            JSONArray jsonArray = new JSONArray(text);
            for (int i = 0; i < jsonArray.length(); i++) {
                LogHelper.e("" + i + " : ", jsonArray.getJSONObject(i).getString("addr"));
                addrList.add(new SearchDropdownItem(jsonArray.getJSONObject(i).getString("addr"), jsonArray.getJSONObject(i).getString("stationName")));
                LogHelper.e(addrList.get(i).getAddr(), addrList.get(i).getStationName());
            }
        } catch (JSONException e) {
            LogHelper.errorStackTrace(e);
        }

        LogHelper.e("count", "" + addrList.size());
        adapter = new SearchArrayAdapter<SearchDropdownItem>(context, R.layout.item_dropdown, addrList);
        autoTextView.setAdapter(adapter);
    }

    /**
     * 백버튼 클릭 이벤트
     */
    @OnClick(R.id.btn_back)
    void btnBackClick() {
        finish();
    }
}
