package com.leigo.android.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.leigo.android.mimi.R;

/**
 * Created by Administrator on 2014/10/13.
 */
public class FeedbackActivity extends Activity {

    public static void startFrom(Activity activity) {
        Intent intent = new Intent(activity, FeedbackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivityForResult(intent, 11);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }
}
