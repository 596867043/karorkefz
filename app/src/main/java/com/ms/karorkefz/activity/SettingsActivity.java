package com.ms.karorkefz.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.Toast;

import com.ms.karorkefz.R;
import com.ms.karorkefz.util.FileUtil;
import com.ms.karorkefz.util.NetWork.Adapter;
import com.ms.karorkefz.util.NetWork.Update;

import org.json.JSONException;

import static com.ms.karorkefz.util.Constant.author;
import static com.ms.karorkefz.util.NetWork.Gift.Gift_List;

/**
 * Created by Jason on 2017/11/24.
 */

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceChangeListener {


    private ListPreference preference;

    public static void open(Context context) {
        try {
            Intent intent = new Intent( context, SettingsActivity.class );
            context.startActivity( intent );
        } catch (Exception e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        getPreferenceManager().setSharedPreferencesMode( MODE_WORLD_READABLE );
        addPreferencesFromResource( R.xml.settings );
        PreferenceManager.getDefaultSharedPreferences( this ).registerOnSharedPreferenceChangeListener( this );
        preference = (ListPreference) findPreference( "Log_Level" );
        preference.setOnPreferenceChangeListener( this );
        if (preference.getEntry() != null) {
            preference.setSummary( preference.getEntry() );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceManager().setSharedPreferencesMode( MODE_WORLD_READABLE );
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences( this ).unregisterOnSharedPreferenceChangeListener( this );
        super.onDestroy();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            CharSequence[] entries = listPreference.getEntries();
            int index = listPreference.findIndexOfValue( (String) newValue );
            listPreference.setSummary( entries[index] );
            Log.d( "karorkefz", "onPreferenceChange run: s" + newValue );
            Toast.makeText( this, entries[index].toString(), Toast.LENGTH_LONG ).show();
        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "show_icon":
                boolean showIcon = sharedPreferences.getBoolean( "show_icon", true );
                Log.e( "karorkefz", String.valueOf( showIcon ) );
                onShowIconChange( showIcon );
                break;
            case "Log":
                boolean log = sharedPreferences.getBoolean( "Log", true );
                Log.e( "karorkefz", "Log文件输出：" + String.valueOf( log ) );
                if (!log) {
                    DeleteLog();
                }
                break;
        }
    }

    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        switch (preference.getKey()) {
            case "gift_update":
                if (author) {
                    Toast.makeText( this, "礼物更新中...", Toast.LENGTH_SHORT ).show();
                    Gift_List();
                    return true;
                } else {
                    Toast.makeText( this, "你估计在逗我....", Toast.LENGTH_SHORT ).show();
                    return true;
                }
            case "DeleteLog":
                DeleteLog();
                return true;
            case "DeleteApdate":
                Adapter.DeleteApdate( this );
                return true;
            case "adapter":
                try {
                    Adapter.getAdapter( this );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            case "update":
                Update.getUpdate( this );
                return true;
        }
        return false;
    }

    private void DeleteLog() {
        if (FileUtil.DeleteFile( "/Log/Hook.txt" )) {
            Toast.makeText( this, "日志文件已删除", Toast.LENGTH_SHORT ).show();
        } else {
            Toast.makeText( this, "删除日志文件:失败！", Toast.LENGTH_SHORT ).show();
        }
    }

    private void onShowIconChange(boolean showIcon) {
        int state = showIcon ? PackageManager.COMPONENT_ENABLED_STATE_DEFAULT : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        Log.e( "karorkefz", String.valueOf( state ) );
        ComponentName aliasName = new ComponentName( this, "com.ms.karorkefz.Main" );
        getPackageManager().setComponentEnabledSetting( aliasName, state, PackageManager.DONT_KILL_APP );
    }
}
