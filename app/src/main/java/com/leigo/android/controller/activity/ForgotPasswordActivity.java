package com.leigo.android.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;

import com.leigo.android.mimi.R;

/**
 * Created by Administrator on 2014/10/13.
 */
public class ForgotPasswordActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static void startFrom(Activity activity, String phoneNumber) {
        Intent intent = new Intent(activity, ForgotPasswordActivity.class);
        if (Patterns.PHONE.matcher(phoneNumber).matches()) {
            intent.putExtra("phoneNumber", phoneNumber);
        }
        activity.startActivityForResult(intent, 5);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
