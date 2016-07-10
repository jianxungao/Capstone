package com.biz.timux.capstone.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.biz.timux.capstone.R;
import com.biz.timux.capstone.utils.CustomFilter;

import java.util.ArrayList;

/**
 * Created by gaojianxun on 16/6/30.
 */
public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder>
        implements Filterable {

    private static final String TAG = CountryAdapter.class.getSimpleName();
    private Context mContext;
    private final LayoutInflater mInflater;
    public ArrayList<CountryModel> mModels;
    private OnItemClickListener mListener;
    private CustomFilter mFilter;


    /**
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     * <p/>
     * <p>This method is usually implemented by {@link Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {
        if (mFilter == null){
            mFilter = new CustomFilter(mModels, this);
        }
        return mFilter;
    }

    /**
     * Interface for receiving click events from cells.
     */
    public interface OnItemClickListener {
        public void onClick(View view, String name);
    }


    public CountryAdapter(Context context,
                          ArrayList<CountryModel> models,
                          OnItemClickListener listener ){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>(models);
        mListener = listener;
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = mInflater.inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CountryViewHolder holder, int position) {
        //int defaultImage = R.drawable.ad;
        final CountryModel model = mModels.get(position);
        if (null != holder.mTextViewCountryName) {
            holder.mTextViewCountryName.setText(model.getCountryName());
        } else {
            Log.d(TAG, "inflating text view got problem");
        }
        //holder.countryFlagIcon.setImageResource(R.drawable.afghanistan);
//        Glide.with(mContext)
//                .load("http://www.geognos.com/api/en/countries/flag/HM.png")
//                .error(defaultImage)
//                .crossFade()
//                .into(holder.countryFlagIcon);

    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

//    public void animateTo(List<CountryModel> models) {
//        applyAndAnimateRemovals(models);
//        applyAndAnimateAdditions(models);
//        applyAndAnimateMovedItems(models);
//    }
//
//    private void applyAndAnimateRemovals(List<CountryModel> newModels) {
//        for (int i = mModels.size() - 1; i >= 0; i--) {
//            final CountryModel model = mModels.get(i);
//            if (!newModels.contains(model)) {
//                removeItem(i);
//            }
//        }
//    }
//
//    private void applyAndAnimateAdditions(List<CountryModel> newModels) {
//        for (int i = 0, count = newModels.size(); i < count; i++) {
//            final CountryModel model = newModels.get(i);
//            if (!mModels.contains(model)) {
//                addItem(i, model);
//            }
//        }
//    }
//
//    private void applyAndAnimateMovedItems(List<CountryModel> newModels) {
//        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
//            final CountryModel model = newModels.get(toPosition);
//            final int fromPosition = mModels.indexOf(model);
//            if (fromPosition >= 0 && fromPosition != toPosition) {
//                moveItem(fromPosition, toPosition);
//            }
//        }
//    }
//
//    public CountryModel removeItem(int position) {
//        final CountryModel model = mModels.remove(position);
//        notifyItemRemoved(position);
//        return model;
//    }
//
//    public void addItem(int position, CountryModel model) {
//        mModels.add(position, model);
//        notifyItemInserted(position);
//    }
//
//    public void moveItem(int fromPosition, int toPosition) {
//        final CountryModel model = mModels.remove(fromPosition);
//        mModels.add(toPosition, model);
//        notifyItemMoved(fromPosition, toPosition);
//    }

    public class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTextViewCountryName;
        private ImageView countryFlagIcon;

        public CountryViewHolder(View itemView) {
            super(itemView);
            mTextViewCountryName = (TextView) itemView.findViewById(R.id.txtName);
            countryFlagIcon = (ImageView) itemView.findViewById(R.id.countryFlagIcon);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            mListener.onClick(v, mTextViewCountryName.getText().toString());
        }

//        public void bind(final CountryModel model) {
//            tvText.setText(model.getCountryName());
//            //countryFlagIcon.setImageURI(ConfigUrl.setFlagIconUrl(model.getCountryCode()));
//            tvText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //listener.onClick()
//                    //mClickListener.onClick(v, model.getCountryName());
//                }
//            });
//        }


    }
}
