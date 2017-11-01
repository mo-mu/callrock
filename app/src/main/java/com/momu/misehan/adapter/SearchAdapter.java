package com.momu.misehan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.momu.misehan.R;
import com.momu.misehan.constant.CConstants;
import com.momu.misehan.item.SearchItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 검색 페이지 검색 결과 리스트 Adapter
 * Created by songm on 2017-08-13.
 */
public class SearchAdapter extends RecyclerView.Adapter {
    private Activity context;
    private ArrayList<SearchItem> items;

    public SearchAdapter(Activity context, ArrayList<SearchItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final SearchItem curItem = items.get(i);

        ((ViewHolder) viewHolder).txtAddress.setText(curItem.getAddress());

        ((ViewHolder) viewHolder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMain = new Intent();
                gotoMain.putExtra("address", curItem.getAddress());
                gotoMain.putExtra("x", curItem.getLongitude());
                gotoMain.putExtra("y", curItem.getLatitude());

                context.setResult(CConstants.RESULT_SELECT_ITEM, gotoMain);
                context.finish();
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_address) TextView txtAddress;
        @BindView(R.id.view) FrameLayout view;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
