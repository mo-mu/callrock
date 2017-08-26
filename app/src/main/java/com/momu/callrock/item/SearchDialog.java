package com.momu.callrock.item;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.FrameLayout;

import com.momu.callrock.R;
import com.momu.callrock.activity.MainActivity;
import com.momu.callrock.activity.SearchActivity;
import com.momu.callrock.config.CConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by songmho on 2017. 8. 26..
 */

public class SearchDialog extends Dialog {

    @BindView(R.id.btn_search) FrameLayout btnSearch;
    Context context;
    public SearchDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_search);
        ButterKnife.bind(this);



    }

    @OnClick(R.id.btn_search)
    void onSearchClick(){
        ((MainActivity)context).sendSearch();
        dismiss();
    }

    @OnClick(R.id.btn_current)
    void onCurrentClick(){
        ((MainActivity)context).getLocationData();
        dismiss();
    }
}
