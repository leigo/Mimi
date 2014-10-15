package com.leigo.android.controller.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.leigo.android.mimi.R;

/**
 * Created by Administrator on 2014/10/13.
 */
public class BlockedUserActivity extends Activity {

    public static void startFrom(Context context) {
        context.startActivity(new Intent(context, BlockedUserActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blocked_user_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_info:
                about();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void about() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.blocked_user_info_dialog_message);
        builder.setNegativeButton(R.string.blocked_user_info_dialog_button, null);
        builder.show();
    }
}
