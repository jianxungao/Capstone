package com.biz.timux.capstone.utils;

import android.widget.Filter;

import com.biz.timux.capstone.data.CountryAdapter;
import com.biz.timux.capstone.data.CountryModel;

import java.util.ArrayList;

/**
 * Created by gaojianxun on 16/6/30.
 */
public class CustomFilter extends Filter {

    private CountryAdapter adapter;
    private ArrayList<CountryModel> list;


    public CustomFilter(ArrayList<CountryModel> filterList, CountryAdapter adapter)
    {
        this.adapter = adapter;
        this.list = filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0) {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<CountryModel> filteredList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                //CHECK
                if(list.get(i).getCountryName().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredList.add(list.get(i));
                }
            }

            results.count = filteredList.size();
            results.values = filteredList;

        } else {
            results.count = list.size();
            results.values = list;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.mModels = (ArrayList<CountryModel>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}