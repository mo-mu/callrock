package com.momu.callrock.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.momu.callrock.adapter.SearchArrayAdapter;
import com.momu.callrock.item.SearchDropdownItem;
import com.momu.callrock.R;
import com.momu.callrock.sql.SQLiteHelper;
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
    ArrayAdapter adapter;

    @BindView(R.id.autoTextView) AutoCompleteTextView autoTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        context = this;


        SQLiteHelper sqLiteHelper = new SQLiteHelper(getApplicationContext(),"whole_city.db",null,1);

        for(int i = 0; i<sqLiteHelper.getWholeAddress().size();i++)
            addrList.add(new SearchDropdownItem(sqLiteHelper.getWholeAddress().get(i),i));

        LogHelper.e("count", "" + addrList.size());
        adapter = new SearchArrayAdapter<SearchDropdownItem>(context, R.layout.item_dropdown, addrList);
        autoTextView.setAdapter(adapter);
        autoTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, ""+((TextView)view).getText().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 백버튼 클릭 이벤트
     */
    @OnClick(R.id.btn_back)
    void btnBackClick() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
