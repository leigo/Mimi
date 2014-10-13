package com.leigo.android.view;

import android.content.Context;
import android.util.AttributeSet;

import com.leigo.android.mimi.R;

/**
 * Created by Administrator on 2014/10/13.
 */
public class CheckBoxPreference extends android.preference.CheckBoxPreference {

    public CheckBoxPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.icon_preference_holo);
    }


    public CheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.icon_preference_holo);
    }

    public CheckBoxPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.icon_preference_holo);
    }


}
