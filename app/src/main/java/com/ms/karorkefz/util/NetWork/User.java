package com.ms.karorkefz.util.NetWork;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.Constant;
import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.util.NDKTools;
import com.ms.karorkefz.xposed.KaraokeHook;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.ms.karorkefz.util.Constant.ip;
import static com.ms.karorkefz.util.Constant.uid;
import static com.ms.karorkefz.util.NetWork.Common.convertIsToByteArray;
import static com.ms.karorkefz.util.Shot.getActivity;

public class User {
    public static void UserMessage_send() {
        try {
            if (!(Constant.code == 0)) return;
            LogUtil.e( "karorkefz", "发送用户信息" );
            String url = "tp5.1/public/karorkefz/api/getAccess";
            JSONObject UserMessage = new JSONObject();
            UserMessage.put( "uid", uid );

            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo( "com.ms.karorkefz", 0 );
            JSONObject fzPackageInfo = new JSONObject();
            fzPackageInfo.put( "versionName", packageInfo.versionName );
            fzPackageInfo.put( "versionCode", packageInfo.versionCode );

            PackageInfo kgpackageInfo = getActivity().getPackageManager().getPackageInfo( "com.tencent.karaoke", 0 );
            JSONObject kgPackageInfo = new JSONObject();
            kgPackageInfo.put( "versionName", kgpackageInfo.versionName );
            kgPackageInfo.put( "versionCode", kgpackageInfo.versionCode );

            JSONObject message = new JSONObject();
            message.put( "UserMessage", UserMessage );
            message.put( "kgPackageInfo", kgPackageInfo );
            message.put( "fzPackageInfo", fzPackageInfo );
            send( message.toString(), url );
        } catch (JSONException | PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void UserMessage_send(String data) {
        LogUtil.e( "karorkefz", "发送用户信息" );
        String url = "tp5.1/public/karorkefz/api/UserMessage";
//        try {
//            String dir = getActivity().getPackageManager().getApplicationInfo( "com.ms.karorkefz", 0 ).nativeLibraryDir;
//            LogUtil.e( "karorkefz", "地址:" + dir );
//            NDKTools.NDKTools_LibraryDir( dir );
//            String string = NDKTools.NetWorkFromNDK( "/" + url, data );
//            LogUtil.e( "karorkefz", "网络返回值:" + string );
//            power( string );
//        } catch (PackageManager.NameNotFoundException e) {
//            LogUtil.e( "karorkefz", "获取地址失败。" );
//            e.printStackTrace();
//        }
        send( data, url );
    }

    private static void send(String data, String surl) {

        new Thread( () -> {
            try {
                URL url = new URL( ip + surl );
                HttpURLConnection httpURLConnection = (HttpURLConnection) url
                        .openConnection();
                // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
                // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
                httpURLConnection.setChunkedStreamingMode( 1024 * 1024 );// 128K
                // 允许输入输出流
                httpURLConnection.setDoInput( true );
                httpURLConnection.setDoOutput( true );
                httpURLConnection.setUseCaches( false );
                // 使用POST方法
                httpURLConnection.setRequestMethod( "POST" );
                httpURLConnection.setRequestProperty( "Connection", "Keep-Alive" );
                httpURLConnection.setRequestProperty( "Charset", "UTF-8" );
                httpURLConnection.setRequestProperty( "data", data );
                if (httpURLConnection.getResponseCode() == 200) {
                    InputStream adapterInputStream = httpURLConnection.getInputStream();
                    byte[] adapterJsonBytes = convertIsToByteArray( adapterInputStream );
                    String adaperJson = new String( adapterJsonBytes );
                    LogUtil.w( "karorkefz", "返回:" + adaperJson );
                    power( adaperJson );
                } else {
                    LogUtil.w( "karorkefz", "服务器连接失败:" + httpURLConnection.getResponseCode() );
                    Constant.msg = "服务器连 接失败，请检查网络重新进入全民K歌。";
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.w( "karorkefz", "错误:" + e.toString() );
                Constant.msg = "网络链接失败，请检查网络重新进入全民K歌。";
            }
        } ).start();
    }

    private static void power(String data) {
        try {
            JSONObject jsonObject = new JSONObject( data );
            if (jsonObject.getInt( "code" ) == 1) {
                String adapter = jsonObject.getString( "adapter" );
                Constant.adapter = new Adapter( new JSONObject( adapter ) );
                Constant.msg = jsonObject.getString( "msg" );
                Constant.code = 1;
                Constant.open = true;
                LogUtil.w( "karorkefz", "网络确认通过，开启。" );
                KaraokeHook.Load();
            } else if (jsonObject.getInt( "code" ) == 2) {
                Constant.msg = jsonObject.getString( "msg" );
                Constant.open = false;
                Constant.adapter = null;
                Constant.code = 2;
            } else if (jsonObject.getInt( "code" ) == 2) {
                Constant.msg = jsonObject.getString( "msg" );
                Constant.open = false;
                Constant.adapter = null;
                Constant.code = 3;
            }
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "网络返回值解析失败：" + e.getMessage() );
        }
    }
}
