package com.biz.timux.capstone.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.biz.timux.capstone.R;

/**
 * Created by gaojianxun on 16/7/3.
 */
public class CountryDetailAdapter extends RecyclerView.Adapter<CountryDetailAdapter.CountryDetailAdapterViewHolder>{

    private static final String TAG = CountryDetailAdapter.class.getSimpleName();
    private final Context mContext;
    private Cursor mCursor;

    public CountryDetailAdapter (Context context){
        this.mContext = context;
    }


    public class CountryDetailAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView mTextView;

        public CountryDetailAdapterViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.textView);


        }


        @Override
        public void onClick(View v) {

        }
    }


    @Override
    public CountryDetailAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_country, parent, false);
        view.setFocusable(true);
        return new CountryDetailAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CountryDetailAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String countryName = mCursor.getString(CountryFragment.COL_COUNTRY_NAME);
        //int
        Log.d(TAG, "get value from Cursor - " + countryName );
        holder.mTextView.setText(countryName);

    }



    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();

    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();

    }

    public Cursor getCursor() {
        return mCursor;
    }
}
