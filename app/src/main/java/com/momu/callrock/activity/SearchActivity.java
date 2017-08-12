package com.momu.callrock.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.momu.callrock.R;
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
    List<String> addrList = new ArrayList<>();

    @BindView(R.id.autoTextView) AutoCompleteTextView autoTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        context = this;


        try {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
