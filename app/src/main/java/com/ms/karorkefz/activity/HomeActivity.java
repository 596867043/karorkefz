package com.ms.karorkefz.activity;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ms.karorkefz.BuildConfig;
import com.ms.karorkefz.R;
import com.ms.karorkefz.adapter.PreferenceAdapter;
import com.ms.karorkefz.util.Constant;
import com.ms.karorkefz.util.Update.Adapter;
import com.ms.karorkefz.xposed.HookStatue;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.ms.karorkefz.util.Constant.Setting_list;
import static com.ms.karorkefz.util.Constant.url;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private PreferenceAdapter mListAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.home );
        ListView listView = (ListView) findViewById( R.id.list );
        List<PreferenceAdapter.Data> list = new ArrayList<>();
        for (int i = 0; i < Setting_list.size(); i++) {
            Constant.Setting setting = (Constant.Setting) Setting_list.get( i );
            list.add( new PreferenceAdapter.Data( setting.title, "点击查看教程", setting.key, setting.url ) );
        }
        String statue;
        if (HookStatue.isEnabled()) statue = "模块已激活";
        else if (HookStatue.isExpModuleActive( this )) statue = "太极已激活";
        else {
            statue = "模块未激活";
        }
        list.add( new PreferenceAdapter.Data( "状态", statue, "statue" ) );
        list.add( new PreferenceAdapter.Data( "项目地址", "https://github.com/jiumoshou/karorkefz", "github" ) );
//        list.add( new PreferenceAdapter.Data( "联系Q群", "1004952748", "Contact" ) );
        list.add( new PreferenceAdapter.Data( "版本", BuildConfig.VERSION_NAME, "version" ) );
        mListAdapter = new PreferenceAdapter( list );
        listView.setAdapter( mListAdapter );
        listView.setOnItemClickListener( this );
//        CheckPoster();
        try {
            Adapter.getAdapter( this );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void CheckPoster() {
        int Build_Code = BuildConfig.VERSION_CODE;
        SharedPreferences sharedPreferences = getSharedPreferences( "settings", Context.MODE_PRIVATE );
        int sp_Code = sharedPreferences.getInt( "code", 0 );
        if (Build_Code != sp_Code) {
            this.runOnUiThread( () -> {
                new AlertDialog.Builder( this )
                        .setTitle( "请求赞助" )
                        .setMessage( "本人还未工作，无法继续支撑服务器费用，请各位大佬加群赞助一下每个月10块钱的费用，谢谢。" )
                        .setNegativeButton( "取消", null )
                        .setPositiveButton( "赞助", (dialogInterface, i) -> {
                            joinQQGroup();
                        } )
                        .create()
                        .show();
                sharedPreferences.edit().putInt( "code", Build_Code ).apply();
            } );
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        PreferenceAdapter.Data data = mListAdapter.getItem( i );
        if (data == null || TextUtils.isEmpty( data.title ) ) {
            return;
        } else if ("状态".equals( data.title )) {
            if ("模块未激活".equals( data.subTitle )) {
                statue();
            }
        } else if ("项目地址".equals( data.title )) {
            WebActivity.openUrl( this, "https://github.com/jiumoshou/karorkefz" );
        } else if ("联系Q群".equals( data.title )) {
            joinQQGroup();
        } else if ("版本".equals( data.title )) {
            Intent intent = new Intent();
            intent.setAction( "android.intent.action.VIEW" );
            intent.setData( Uri.parse( url ) );
            HomeActivity.this.startActivity( intent );
        } else if(! TextUtils.isEmpty( data.url )){
            WebActivity.openUrl( this, url + data.url );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_settings:
                SettingsActivity.open( this );
                return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private boolean statue() {
        Intent t = new Intent( "me.weishu.exp.ACTION_MODULE_MANAGE" );
        t.setData( Uri.parse( "package:" + "com.ms.karorkefz" ) );
        t.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
        try {
            HomeActivity.this.startActivity( t );
        } catch (ActivityNotFoundException e) {
            return false;
        }
        return true;
    }

    boolean joinQQGroup() {
        String key = "AF4gXBHuhdkO2CsIrTFJ-rHYhL5MDtQp";
        Intent intent = new Intent();
        intent.setData( Uri.parse( "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key ) );
        try {
            startActivity( intent );
            return true;
        } catch (Exception e) {
            Toast.makeText( HomeActivity.this, "未安装QQ或者QQ版本不支持", Toast.LENGTH_SHORT ).show();
            return false;
        }
    }
}

