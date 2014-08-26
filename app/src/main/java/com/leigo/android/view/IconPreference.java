package com.leigo.android.view;

import android.content.Context;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.leigo.android.mimi.R;
import com.leigo.android.util.Utils;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2014/8/26.
 */
public class IconPreference extends Preference {
    private TextView iconView;
    private String mIconText;

    public IconPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.icon_preference_holo);
    }

    public IconPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.icon_preference_holo);
    }

    public IconPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.icon_preference_holo);
    }

    public void clearNew() {
        setIconText(null);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        iconView = (TextView) view.findViewById(R.id.icon);
        if (iconView != null) {
            if (!TextUtils.isEmpty(mIconText)) {
                Utils.setVisibility(iconView, View.VISIBLE);
                iconView.setText(mIconText);
            }
        } else {
            Utils.setVisibility(iconView, View.GONE);
        }
    }

    public void setIconText(String iconText) {
        if (!TextUtils.equals(mIconText, iconText)) {
            mIconText = iconText;
            notifyChanged();
        }
    }

    public void setNew() {
        setIconText("New");
    }
}
