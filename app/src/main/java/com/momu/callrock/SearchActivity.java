package com.momu.callrock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songm on 2017-07-09.
 */

public class SearchActivity extends AppCompatActivity {

    Context context;
    final List<String> addrList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        context = this;
        final AppCompatAutoCompleteTextView autoTextView = (AppCompatAutoCompleteTextView)findViewById(R.id.autoTextView);

        SharedPreferences prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        String text = prefs.getString("Observe", "");

        try {
            JSONArray jsonArray = new JSONArray(text);
            for(int i=0; i<jsonArray.length();i++){
                Log.e(""+i+" : ",jsonArray.getJSONObject(i).getString("addr"));
                addrList.add(jsonArray.getJSONObject(i).getString("addr"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,android.R.layout.simple_dropdown_item_1line,addrList);
        autoTextView.setAdapter(adapter);
    }
}
