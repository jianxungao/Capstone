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

        public final TextView mTextViewCountryFullName;
        public final TextView mTextViewISO2;
        public final TextView mTextViewContinent;
        public final TextView mTextViewTimezone;
        public final TextView mTextViewLanguage;

        public final TextView mJan;
        public final TextView mFeb;
        public final TextView mMar;
        public final TextView mApr;
        public final TextView mMay;
        public final TextView mJun;
        public final TextView mJul;
        public final TextView mAug;
        public final TextView mSep;
        public final TextView mOct;
        public final TextView mNov;
        public final TextView mDec;

        public final TextView mAdvise;
        public final TextView mUrl;
        public final TextView mVoltage;
        public final TextView mFrequency;

        public final TextView mAU;
        public final TextView mCA;
        public final TextView mEU;
        public final TextView mHK;
        public final TextView mMX;
        public final TextView mNZ;
        public final TextView mUS;



        public CountryDetailAdapterViewHolder(View view){
            super(view);
            mTextViewCountryFullName = (TextView) view.findViewById(R.id.tv_full_name);
            mTextViewISO2 = (TextView) view.findViewById(R.id.tv_iso2);
            mTextViewContinent = (TextView) view.findViewById(R.id.tv_continent);
            mTextViewTimezone = (TextView) view.findViewById(R.id.tv_timezone);
            mTextViewLanguage = (TextView) view.findViewById(R.id.tv_language);

            mJan = (TextView) view.findViewById(R.id.tv_january);
            mFeb = (TextView) view.findViewById(R.id.tv_february);
            mMar = (TextView) view.findViewById(R.id.tv_march);
            mApr = (TextView) view.findViewById(R.id.tv_april);
            mMay = (TextView) view.findViewById(R.id.tv_may);
            mJun = (TextView) view.findViewById(R.id.tv_june);
            mJul = (TextView) view.findViewById(R.id.tv_july);
            mAug = (TextView) view.findViewById(R.id.tv_august);
            mSep = (TextView) view.findViewById(R.id.tv_september);
            mOct = (TextView) view.findViewById(R.id.tv_october);
            mNov = (TextView) view.findViewById(R.id.tv_november);
            mDec = (TextView) view.findViewById(R.id.tv_december);

            mAdvise = (TextView) view.findViewById(R.id.tv_advise);
            mUrl = (TextView) view.findViewById(R.id.tv_url);
            mVoltage = (TextView) view.findViewById(R.id.tv_voltage);
            mFrequency = (TextView) view.findViewById(R.id.tv_frequency);

            mAU = (TextView) view.findViewById(R.id.tv_australian);
            mCA = (TextView) view.findViewById(R.id.tv_canadian);
            mEU = (TextView) view.findViewById(R.id.tv_euro);
            mHK = (TextView) view.findViewById(R.id.tv_hk);
            mMX = (TextView) view.findViewById(R.id.tv_mexian);
            mNZ = (TextView) view.findViewById(R.id.tv_nz);
            mUS = (TextView) view.findViewById(R.id.tv_us);

        }


        @Override
        public void onClick(View v) {

        }
    }


    @Override
    public CountryDetailAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (parent instanceof RecyclerView) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_country, parent, false);
            view.setFocusable(true);
            return new CountryDetailAdapterViewHolder(view);
        } else {
            throw new RuntimeException( " Not bound to RecyclerView ");
        }
    }


    @Override
    public void onBindViewHolder(CountryDetailAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String countryName = mCursor.getString(CountryFragment.COL_COUNTRY_FNAME);
        String iso2 = mCursor.getString(CountryFragment.COL_COUNTRY_ISO2);
        String continent = mCursor.getString(CountryFragment.COL_COUNTRY_CONTINENT);
        String timezone = mCursor.getString(CountryFragment.COL_COUNTRY_TIMEZONE);
        String lanugage = mCursor.getString(CountryFragment.COL_COUNTRY_LANGUAGE);

        String jan = mCursor.getString(CountryFragment.COL_COUNTRY_JAN);
        String feb = mCursor.getString(CountryFragment.COL_COUNTRY_FEB);
        String mar = mCursor.getString(CountryFragment.COL_COUNTRY_MAR);
        String apr = mCursor.getString(CountryFragment.COL_COUNTRY_APR);
        String may = mCursor.getString(CountryFragment.COL_COUNTRY_MAY);
        String jun = mCursor.getString(CountryFragment.COL_COUNTRY_JUN);
        String jul = mCursor.getString(CountryFragment.COL_COUNTRY_JUL);
        String aug = mCursor.getString(CountryFragment.COL_COUNTRY_AUG);
        String sep = mCursor.getString(CountryFragment.COL_COUNTRY_SEP);
        String oct = mCursor.getString(CountryFragment.COL_COUNTRY_OCT);
        String nov = mCursor.getString(CountryFragment.COL_COUNTRY_NOV);
        String dec = mCursor.getString(CountryFragment.COL_COUNTRY_DEC);

        String voltage = mCursor.getString(CountryFragment.COL_COUNTRY_VOLTAGE);
        String url = mCursor.getString(CountryFragment.COL_COUNTRY_URL);
        String advise = mCursor.getString(CountryFragment.COL_COUNTRY_ADVISE);
        String frequency = mCursor.getString(CountryFragment.COL_COUNTRY_FREQUENCY);

        String au = mCursor.getString(CountryFragment.COL_COUNTRY_AU_RATE);
        String ca = mCursor.getString(CountryFragment.COL_COUNTRY_CA_RATE);
        String eu = mCursor.getString(CountryFragment.COL_COUNTRY_EU_RATE);
        String hk = mCursor.getString(CountryFragment.COL_COUNTRY_HK_RATE);
        String mx = mCursor.getString(CountryFragment.COL_COUNTRY_MX_RATE);
        String nz = mCursor.getString(CountryFragment.COL_COUNTRY_NZ_RATE);
        String us = mCursor.getString(CountryFragment.COL_COUNTRY_US_RATE);


        //int
        Log.d(TAG, "get value from Cursor - " + countryName.toString() );
        holder.mTextViewCountryFullName.setText(countryName.toString());
        if (iso2.equals("null")){
            holder.mTextViewISO2.setText("-");
            Log.d(TAG, " iso2 is null");
        } else {
            holder.mTextViewISO2.setText(iso2);
        }

        holder.mTextViewContinent.setText(continent);
        holder.mTextViewTimezone.setText(timezone);
        holder.mTextViewLanguage.setText(lanugage);

        holder.mJan.setText(jan);
        holder.mFeb.setText(feb);
        holder.mMar.setText(mar);
        holder.mApr.setText(apr);
        holder.mMay.setText(may);
        holder.mJun.setText(jun);
        holder.mJul.setText(jul);
        holder.mAug.setText(aug);
        holder.mSep.setText(sep);
        holder.mOct.setText(oct);
        holder.mNov.setText(nov);
        holder.mDec.setText(dec);

        holder.mFrequency.setText(frequency);
        holder.mVoltage.setText(voltage);
        holder.mAdvise.setText(advise);
        holder.mUrl.setText(url);

        holder.mAU.setText(au);
        holder.mCA.setText(ca);
        holder.mEU.setText(eu);
        holder.mHK.setText(hk);
        holder.mMX.setText(mx);
        holder.mNZ.setText(nz);
        holder.mUS.setText(us);
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
