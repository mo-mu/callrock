package com.momu.misehan.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.momu.misehan.R;
import com.momu.misehan.activity.MainActivity;
import com.momu.misehan.preference.AppPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 현재 위치 / 검색해서 찾기 여부 설정 팝업
 * Created by songmho on 2017. 8. 26..
 */
public class SearchDialog extends Dialog {
    @BindView(R.id.img_check_current) ImageView imgCheckCurrent;
    @BindView(R.id.img_check_search) ImageView imgCheckSearched;
    private Context context;

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

        if (AppPreference.loadIsUseSearchedStation(context)) {
            imgCheckCurrent.setVisibility(View.INVISIBLE);
            imgCheckSearched.setVisibility(View.VISIBLE);
        } else {
            imgCheckCurrent.setVisibility(View.VISIBLE);
            imgCheckSearched.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 검색해서 찾기 버튼 클릭 이벤트
     */
    @OnClick(R.id.btn_search)
    void onSearchClick() {
        imgCheckCurrent.setVisibility(View.INVISIBLE);
        imgCheckSearched.setVisibility(View.VISIBLE);
        AppPreference.saveIsUseSearchedStation(context, true);

        ((MainActivity) context).openSearchPage();
        dismiss();
    }

    /**
     * 현재 위치로 찾기 버튼 클릭 이벤트
     */
    @OnClick(R.id.btn_current)
    void onCurrentClick() {
        imgCheckCurrent.setVisibility(View.VISIBLE);
        imgCheckSearched.setVisibility(View.INVISIBLE);
        AppPreference.saveIsUseSearchedStation(context, false);

        ((MainActivity) context).btnSearchClick();
//        ((MainActivity) context).isSearch = false;
        dismiss();
    }
}
