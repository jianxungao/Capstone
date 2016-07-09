package com.biz.timux.capstone.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.EditText;

import com.biz.timux.capstone.R;
import com.biz.timux.capstone.data.Countries;

/**
 * Created by gaojianxun on 16/7/8.
 */
public class CountryEditTextPreference extends EditTextPreference{

    static final private int DEFAULT_MINIMUM_LOCATION_LENGTH = 2;
    private int mMinLength;
    //private static AutoCompleteTextView mEditText ;
    private ArrayAdapter<String> adapter;

    public CountryEditTextPreference(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);

//        mEditText = new AutoCompleteTextView(context, attributeSet);
//        mEditText.setThreshold(0);
//
//        adapter = new ArrayAdapter<String>(context,
//                android.R.layout.simple_dropdown_item_1line,
//                Countries.COUNTRIES);
//        mEditText.setAdapter(adapter);

//        TypedArray array = context.getTheme().obtainStyledAttributes(
//                attributeSet,
//                R.styleable.CountryEditTextPreference,
//                0, 0
//        );
//
//        try {
//            mMinLength = array.getInteger(R.styleable.CountryEditTextPreference_minLength, DEFAULT_MINIMUM_LOCATION_LENGTH);
//        } finally {
//            array.recycle();
//        }

    }


    protected void showDialog(Bundle state) {
        super.showDialog(state);

        //AutoCompleteTextView et = mEditText;
        EditText et = getEditText();
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //CountryEditTextPreference.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Dialog d = getDialog();
                if (d instanceof AlertDialog) {
                    AlertDialog dialog = (AlertDialog) d;
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    final String countryName = s.toString();
                    if (!isCountryValid(countryName)) {
                        // Disable OK button
                        positiveButton.setEnabled(false);
                    } else {
                        // Re-enable the button.
                        positiveButton.setEnabled(true);
                    }
                }

            }
        });
    }

    private boolean isCountryValid(String countryName) {

        boolean found = false;
        for (String country : Countries.COUNTRIES){
            if (country.equals(countryName)){
                found = true;
            }
        }
        return found;
    }

}
