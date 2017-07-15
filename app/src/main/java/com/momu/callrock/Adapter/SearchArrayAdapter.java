package com.momu.callrock.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.momu.callrock.Item.SearchDropdownItem;
import com.momu.callrock.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songm on 2017-07-15.
 */

public class SearchArrayAdapter<S> extends ArrayAdapter<SearchDropdownItem> {
    Context context;
    List<SearchDropdownItem> addrList;
    private ArrayList<SearchDropdownItem> itemsAll;
    private ArrayList<SearchDropdownItem> suggestions;
    private int item_dropdown;

    public SearchArrayAdapter(Context context, int item_dropdown, ArrayList<SearchDropdownItem> addrList) {
        super(context,item_dropdown,addrList);
        this.addrList = addrList;
        this.itemsAll = (ArrayList<SearchDropdownItem>) addrList.clone();
        this.suggestions = new ArrayList<>();
        this.context = context;
        this.item_dropdown = item_dropdown;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(item_dropdown, null);
        }
        SearchDropdownItem item = addrList.get(position);
        if (item != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.text1);
            if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
                customerNameLabel.setText(item.getAddr());
            }
        }
        return v;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return newFilter;
    }

    Filter newFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (SearchDropdownItem i : itemsAll) {
                    if(i.getAddr().toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(i);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                Log.e("adsf",""+filterResults.count);
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<SearchDropdownItem> filteredList = (ArrayList<SearchDropdownItem>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (SearchDropdownItem c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
