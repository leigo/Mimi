package com.leigo.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leigo.android.mimi.R;
import com.leigo.android.util.Utils;

/**
 * Created by Administrator on 2014/10/27.
 */
public class ChatInfoItemTextView extends LinearLayout {

    private static final int SUMMARY_POSITION_CENTER = 0;
    private static final int SUMMARY_POSITION_RIGHT = 1;
    private RelativeLayout contentLayout;
    private TextView summaryView;
    private TextView titleView;

    public ChatInfoItemTextView(Context context) {
        this(context, null);
    }

    public ChatInfoItemTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatInfoItemTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChatInfoItemTextView);
        CharSequence text = a.getText(R.styleable.ChatInfoItemTextView_title);
        int titleTextColor = a.getColor(R.styleable.ChatInfoItemTextView_titleTextColor, getResources().getColor(R.color.group_chat_info_item_editable));
        int summaryTextColor = a.getColor(R.styleable.ChatInfoItemTextView_summaryTextColor, getResources().getColor(R.color.group_chat_info_item_uneditable));
        Drawable background = a.getDrawable(R.styleable.ChatInfoItemTextView_background);
        int summaryPosition = a.getInt(R.styleable.ChatInfoItemTextView_summaryPosition, 0);
        a.recycle();

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        setOrientation(VERTICAL);
        setMinimumHeight(Utils.dipToPixels(displayMetrics, 48));
        int padding = Utils.dipToPixels(displayMetrics, 14);
        setPadding(padding, 0, 0, 0);

        contentLayout = new RelativeLayout(context);
        contentLayout.setPadding(0, padding, padding, padding);

        LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight = 1;
        addView(contentLayout, layoutParams);

        if (background != null) {
            setContentBackground(background);
        }

        titleView = new TextView(context);
        titleView.setMinimumWidth(Utils.dipToPixels(displayMetrics, 88));
        titleView.setTextColor(getResources().getColor(R.color.group_chat_info_item_title));
        titleView.setTextSize(16);
        titleView.setTextColor(titleTextColor);
        titleView.setText(text);
        titleView.setSingleLine();
        titleView.setId(1);
        contentLayout.addView(titleView);

        summaryView = new TextView(getContext());
        summaryView.setTextSize(16);
        summaryView.setTextColor(summaryTextColor);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(0, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (summaryPosition == 0) {
            lp.addRule(RelativeLayout.RIGHT_OF, titleView.getId());
            lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        } else if (summaryPosition == 1) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        }
        contentLayout.addView(summaryView, lp);
        View view = new View(context);
        view.setBackgroundColor(getResources().getColor(R.color.divider));
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, 1));

    }

    public String getSummaryText() {
        return summaryView.getText().toString();
    }

    public void setContentBackground(int resId) {
        setContentBackground(getResources().getDrawable(resId));
    }

    public void setContentBackground(Drawable paramDrawable) {
        int left = contentLayout.getPaddingLeft();
        int top = contentLayout.getPaddingTop();
        int right = contentLayout.getPaddingRight();
        int bottom = contentLayout.getPaddingBottom();
        contentLayout.setBackgroundDrawable(paramDrawable);
        contentLayout.setPadding(left, top, right, bottom);
    }

    public RelativeLayout getContentLayout() {
        return contentLayout;
    }

    public void setSummaryText(String text) {
        summaryView.setText(text);
    }

    public void setSummaryTextColor(int color) {
        summaryView.setTextColor(color);
    }

    public void setTitleTextColor(int color) {
        titleView.setTextColor(color);
    }
}
