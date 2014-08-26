package com.leigo.android.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.leigo.android.mimi.R;

/**
 * Created by Administrator on 2014/8/26.
 */
public class ProgressingDialog extends Dialog {

    private int progressingTextRes;

    public ProgressingDialog(Context context) {
        super(context, R.style.ProgressingDialog);
    }

    public ProgressingDialog(Context context, int theme) {
        super(context, theme);
        this.progressingTextRes = theme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressing_dialog);
        if (progressingTextRes != 0) {
            TextView textView = (TextView) findViewById(R.id.progressing_text);
            textView.setVisibility(View.VISIBLE);
            textView.setText(progressingTextRes);
        }
    }
}
