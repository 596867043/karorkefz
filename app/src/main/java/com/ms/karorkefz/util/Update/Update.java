package com.ms.karorkefz.util.Update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.ms.karorkefz.BuildConfig;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.ms.karorkefz.util.Constant.url;

public class Update {
    public static void getUpdate(Activity activity) {
        new Thread( () -> {
            try {
                activity.runOnUiThread( () -> Toast.makeText( activity, "正在联网检查最新版本", Toast.LENGTH_SHORT ).show() );
                HttpURLConnection conn = (HttpURLConnection) new URL( url + "app/check/version.json" ).openConnection();
                conn.setConnectTimeout( 3000 );
                conn.setRequestMethod( "GET" );
                if (conn.getResponseCode() == 200) {
                    InputStream inputStream = conn.getInputStream();
                    byte[] jsonBytes = (byte[]) Common.convertIsToByteArray( inputStream );
                    String json = new String( jsonBytes );
                    if (json.length() > 0) {
                        activity.runOnUiThread( () -> {
                            try {
                                JSONObject jsonObject = new JSONObject( json );
                                int versionCode = jsonObject.optInt( "code" );
                                int nowCode = BuildConfig.VERSION_CODE;
                                if (versionCode > nowCode) {
                                    new AlertDialog.Builder( activity )
                                            .setTitle( "发现新版本" )
                                            .setMessage( jsonObject.optString( "name" ) )
                                            .setNegativeButton( "取消", null )
                                            .setPositiveButton( "下载", (dialogInterface, i) -> {
                                                Intent localIntent = new Intent( "android.intent.action.VIEW" );
                                                localIntent.setData( Uri.parse( url ) );
                                                activity.startActivity( localIntent );
                                            } )
                                            .create()
                                            .show();
                                } else
                                    activity.runOnUiThread( () -> Toast.makeText( activity, "检查更新成功，当前已是最新版本", Toast.LENGTH_SHORT ).show() );
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } );
                        return;
                    }
                }
                activity.runOnUiThread( () -> Toast.makeText( activity, "检查更新时出错", Toast.LENGTH_SHORT ).show() );
                Common.fail();
            } catch (Exception e) {
                e.printStackTrace();
                activity.runOnUiThread( () -> Toast.makeText( activity, "检查更新时出错：" + e.getMessage(), Toast.LENGTH_LONG ).show() );
                Common.fail();
            }
        } ).start();

    }
}
