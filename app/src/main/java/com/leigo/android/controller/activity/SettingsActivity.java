package com.leigo.android.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.leigo.android.mimi.R;
import com.leigo.android.model.helper.HttpHelper;
import com.leigo.android.view.IconPreference;

/**
 * Created by Administrator on 2014/8/26.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    private static final String KEY_CHANGE_PASSWORD_SETTINGS = "change_password_settings";
    private static final String KEY_CHAT_NOTIFICATION_SETTINGS = "chat_notification_settings";
    private static final String KEY_LOGOUT_SETTINGS = "logout_settings";
    private static final String KEY_NEARBY_SECRET_SETTINGS = "nearby_secret_settings";
    private static final String KEY_PRIVACY_SETTINGS = "privacy_settings";
    private static final String KEY_SYS_BLOCKED_USER_SETTINGS = "blocked_user_settings";
    private static final String KEY_SYS_NOTIFICATION_SETTINGS = "sys_notification_settings";
    private static final String KEY_UNLINK_SETTINGS = "unlink_settings";
    private static final String KEY_VERSION_SETTINGS = "version_settings";

    private DisplayMetrics displayMetrics;

    private IconPreference versionPreference;

    public static void startFrom(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivityForResult(intent, 11);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.preferences);
        findPreference(KEY_CHAT_NOTIFICATION_SETTINGS).setOnPreferenceClickListener(this);
        findPreference(KEY_SYS_BLOCKED_USER_SETTINGS).setOnPreferenceClickListener(this);
        findPreference(KEY_PRIVACY_SETTINGS).setOnPreferenceClickListener(this);
        findPreference(KEY_CHANGE_PASSWORD_SETTINGS).setOnPreferenceClickListener(this);
        versionPreference = (IconPreference) findPreference(KEY_VERSION_SETTINGS);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionPreference.setSummary(getString(R.string.settings_version_current, packageInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String str = preference.getKey();
        if (KEY_CHAT_NOTIFICATION_SETTINGS.equals(str)) {
            SysNotificationActivity.startFrom(this);
        } else if (KEY_SYS_BLOCKED_USER_SETTINGS.equals(str)) {
            BlockedUserActivity.startFrom(this);
        } else if (KEY_PRIVACY_SETTINGS.equals(str)) {
            WebViewActivity.startFrom(this, HttpHelper.createUrl("privacy"));
        } else if (KEY_CHANGE_PASSWORD_SETTINGS.equals(str)) {
            ChangePasswordActivity.startFrom(this);
        }
        return true;
    }

}
