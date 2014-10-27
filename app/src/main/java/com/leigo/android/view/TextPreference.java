package com.leigo.android.view;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

import com.leigo.android.mimi.R;

/**
 * Created by Administrator on 2014/10/27.
 */
public class TextPreference extends Preference {

    public TextPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.text_preference_holo);
    }

    public TextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.text_preference_holo);
    }


    public TextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.text_preference_holo);
    }


}
