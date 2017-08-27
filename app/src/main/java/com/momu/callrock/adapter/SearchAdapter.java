package com.momu.callrock.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.momu.callrock.R;
import com.momu.callrock.activity.SearchActivity;
import com.momu.callrock.config.CConfig;
import com.momu.callrock.constant.CConstants;
import com.momu.callrock.item.SearchItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by songm on 2017-08-13.
 */

public class SearchAdapter extends RecyclerView.Adapter {
    Activity context;
    ArrayList<SearchItem> items;

    public SearchAdapter(Activity context, ArrayList<SearchItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search,viewGroup,false);
        return new ViewHoler(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final SearchItem curItem = items.get(i);

        ((ViewHoler)viewHolder).txtAddress.setText(curItem.getAddress());

        ((ViewHoler)viewHolder).view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMain = new Intent();
                gotoMain.putExtra("address",curItem.getAddress());
                gotoMain.putExtra("x",curItem.getLongitude());
                gotoMain.putExtra("y",curItem.getLatitude());

                context.setResult(CConstants.SELECT_ITEM,gotoMain);
                context.finish();
            }
        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_address) TextView txtAddress;
        @BindView(R.id.view) FrameLayout view;
        public ViewHoler(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }
}
