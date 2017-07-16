package com.momu.callrock.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.momu.callrock.item.SearchDropdownItem;
import com.momu.callrock.R;
import com.momu.callrock.utility.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songm on 2017-07-15.
 */

public class SearchArrayAdapter<S> extends ArrayAdapter<SearchDropdownItem> {
    private Context context;
    private List<SearchDropdownItem> addrList;
    private ArrayList<SearchDropdownItem> itemsAll;
    private ArrayList<SearchDropdownItem> suggestions;
    private int itemDropdown;

    public SearchArrayAdapter(Context context, int itemDropdown, ArrayList<SearchDropdownItem> addrList) {
        super(context, itemDropdown, addrList);
        this.addrList = addrList;
        this.itemsAll = (ArrayList<SearchDropdownItem>) addrList.clone();
        this.suggestions = new ArrayList<>();
        this.context = context;
        this.itemDropdown = itemDropdown;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(itemDropdown, null);
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
            if (constraint != null) {
                suggestions.clear();
                for (SearchDropdownItem i : itemsAll) {
                    if (i.getAddr().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(i);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                LogHelper.e("adsf", "" + filterResults.count);
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.count > 0) {
                ArrayList<SearchDropdownItem> filteredList = (ArrayList<SearchDropdownItem>) results.values;
                clear();
                for (SearchDropdownItem c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
