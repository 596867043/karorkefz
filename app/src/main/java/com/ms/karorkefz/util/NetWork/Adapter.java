package com.ms.karorkefz.util.NetWork;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.ms.karorkefz.util.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.ms.karorkefz.util.Constant.url;
import static com.ms.karorkefz.util.NetWork.Common.convertIsToByteArray;

public class Adapter {
    public static void getAdapter(Activity activity) throws JSONException {
        PackageManager pm = activity.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo( "com.tencent.karaoke", 0 );
            String KgVersionName = packageInfo.versionName;
            Log.v( "karorkefz", "全民K歌版本号：" + KgVersionName );
            if (KgVersionName.equals( "" )) {
                return;
            }
            String adapter = null;
            try {
                adapter = FileUtil.readFileSdcardFile( "/adapter.json" );
            } catch (Exception e) {
                Log.v( "karorkefz", "适配文件：不存在！" );
            }
            if (adapter != null) {
                JSONObject jsonObject = new JSONObject( adapter );
                String adapterVersionName = jsonObject.optString( "version" );
                if (KgVersionName.equals( adapterVersionName )) {
                    Log.v( "karorkefz", "全民K歌版本号与配置版本号一致" );
                    activity.runOnUiThread( () -> Toast.makeText( activity, "此版本已适配", Toast.LENGTH_SHORT ).show() );
                    return;
                }
            }
            new Thread( () -> {
                try {
                    activity.runOnUiThread( () -> Toast.makeText( activity, "正在联网检查最新适配", Toast.LENGTH_SHORT ).show() );
                    HttpURLConnection adapterConn = (HttpURLConnection) new URL( url + "app/check/adapter.json" ).openConnection();
                    adapterConn.setConnectTimeout( 10000 );
                    adapterConn.setRequestMethod( "GET" );
                    if (adapterConn.getResponseCode() == 200) {
                        InputStream adapterInputStream = adapterConn.getInputStream();
                        byte[] adapterJsonBytes = convertIsToByteArray( adapterInputStream );
                        String adaperJson = new String( adapterJsonBytes );
                        if (adaperJson.length() > 0) {
                            activity.runOnUiThread( () -> {
                                try {
                                    JSONArray adapterJsonArray = new JSONArray( adaperJson );
                                    for (int j = 0; j < adapterJsonArray.length(); j++) {
                                        JSONObject adapterJsonObject = adapterJsonArray.getJSONObject( j );
                                        String versionJsonString = adapterJsonObject.getString( "version" );
                                        if (versionJsonString.equals( KgVersionName )) {
                                            new AlertDialog.Builder( activity )
                                                    .setTitle( "发现配置文件" )
                                                    .setMessage( adapterJsonObject.optString( "version" ) )
                                                    .setNegativeButton( "取消", null )
                                                    .setPositiveButton( "适配", (dialogInterface, i) -> {
                                                        new Thread( () -> {
                                                            try {
                                                                activity.runOnUiThread( () -> Toast.makeText( activity, "正在联网下载适配", Toast.LENGTH_SHORT ).show() );
                                                                HttpURLConnection adapterDownConn = (HttpURLConnection) new URL( url + "app/check/adapter/" + adapterJsonObject.optString( "name" ) + ".json" ).openConnection();
                                                                adapterDownConn.setConnectTimeout( 10000 );
                                                                adapterDownConn.setRequestMethod( "GET" );
                                                                if (adapterDownConn.getResponseCode() == 200) {
                                                                    InputStream inputStream = adapterDownConn.getInputStream();
                                                                    byte[] jsonBytes = convertIsToByteArray( inputStream );
                                                                    String json = new String( jsonBytes );
                                                                    if (json.length() > 0) {
                                                                        try {
                                                                            FileUtil.writeFile( "/adapter.json", json );
                                                                            activity.runOnUiThread( () -> Toast.makeText( activity, "适配文件已保存", Toast.LENGTH_SHORT ).show() );
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        return;
                                                                    }
                                                                }
                                                                activity.runOnUiThread( () -> Toast.makeText( activity, "下载适配文件出错", Toast.LENGTH_SHORT ).show() );
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                activity.runOnUiThread( () -> Toast.makeText( activity, "下载适配文件出错：" + e.getMessage(), Toast.LENGTH_LONG ).show() );
                                                            }
                                                        } ).start();
                                                        ;
                                                    } )
                                                    .create()
                                                    .show();

                                            return;
                                        }
                                    }
                                    activity.runOnUiThread( () -> Toast.makeText( activity, "检查适配成功，版本：" + KgVersionName + "  暂未适配。", Toast.LENGTH_SHORT ).show() );
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } );
                            return;
                        }
                    }
                    activity.runOnUiThread( () -> Toast.makeText( activity, "检查适配时出错。", Toast.LENGTH_SHORT ).show() );
                    Common.fail();
                } catch (Exception e) {
                    e.printStackTrace();
                    activity.runOnUiThread( () -> Toast.makeText( activity, "检查适配时出错：" + e.getMessage(), Toast.LENGTH_LONG ).show() );
                    Common.fail();
                }
            } ).start();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteApdate(Activity activity) {
        if (FileUtil.DeleteFile( "/adapter.json" )) {
            Log.e( "karorkefz", "删除适配文件:成功！" );
            Toast.makeText( activity, "适配文件已删除，请重新获取", Toast.LENGTH_SHORT ).show();
        } else {
            Toast.makeText( activity, "删除适配文件:失败！", Toast.LENGTH_SHORT ).show();
        }
    }
}
