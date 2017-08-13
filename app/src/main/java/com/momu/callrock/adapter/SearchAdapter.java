package com.momu.callrock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.momu.callrock.R;
import com.momu.callrock.item.SearchItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by songm on 2017-08-13.
 */

public class SearchAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<SearchItem> items;

    public SearchAdapter(Context context, ArrayList<SearchItem> items) {
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
        SearchItem curItem = items.get(i);

        ((ViewHoler)viewHolder).txtAddress.setText(curItem.getAddress());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_address) TextView txtAddress;
        public ViewHoler(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }
}
