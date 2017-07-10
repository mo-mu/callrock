package com.momu.callrock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;

/**
 * Created by songm on 2017-07-09.
 */

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        final AppCompatAutoCompleteTextView autoTextView = (AppCompatAutoCompleteTextView)findViewById(R.id.autoTextView);


    }
}
