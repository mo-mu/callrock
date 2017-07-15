package com.momu.callrock.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.momu.callrock.Adapter.SearchArrayAdapter;
import com.momu.callrock.Item.SearchDropdownItem;
import com.momu.callrock.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by songm on 2017-07-09.
 */

public class SearchActivity extends AppCompatActivity {

    Context context;
    ArrayList<SearchDropdownItem> addrList;
    Button btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        context = this;
        final AppCompatAutoCompleteTextView autoTextView = (AppCompatAutoCompleteTextView)findViewById(R.id.autoTextView);

        SharedPreferences prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        String text = prefs.getString("Observe", "");

        try {
            addrList = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(text);
            for(int i=0; i<jsonArray.length();i++){
                Log.e(""+i+" : ",jsonArray.getJSONObject(i).getString("addr"));
                addrList.add(new SearchDropdownItem(jsonArray.getJSONObject(i).getString("addr"), jsonArray.getJSONObject(i).getString("stationName")));
                Log.e(addrList.get(i).getAddr(),addrList.get(i).getStationName());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("count", ""+addrList.size());
        ArrayAdapter adapter = new SearchArrayAdapter<SearchDropdownItem>(context,R.layout.item_dropdown,addrList);
        autoTextView.setAdapter(adapter);


        btnBack = (Button)findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
