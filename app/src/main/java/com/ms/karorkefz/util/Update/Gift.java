package com.ms.karorkefz.util.Update;

import android.util.Log;

import com.ms.karorkefz.util.FileUtil;
import com.ms.karorkefz.util.Log.LogUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.ms.karorkefz.util.Constant.ip;
import static com.ms.karorkefz.util.Update.Common.convertIsToByteArray;

public class Gift {
    public static void Gift_send_Chatist(String data) {
        LogUtil.e( "karorkefz", "聊天监听准备发送礼物列表" );
        String url = "tp5.1/public/karorkefz/Gift/Gift_send_GiftPrice";
        send( data, url );
    }

    public static void Gift_send(String data) {
        LogUtil.e( "karorkefz", "准备发送礼物列表" );
        String url = "tp5.1/public/karorkefz/Gift/Gift_send";
        send( data, url );
    }

    public static void Gift_update(String data) {
        LogUtil.e( "karorkefz", "准备发送原礼物列表" );
        String url = "tp5.1/public/karorkefz/Gift/Gift_update";
        send( data, url );
    }

    public static void Gift_List() {
        Log.e( "karorkefz", "获取礼物列表" );
        String url = "tp5.1/public/karorkefz/gift/get_Gift_main";
        send( url );
    }

    private static void send(String surl) {
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
                if (httpURLConnection.getResponseCode() == 200) {
                    InputStream adapterInputStream = httpURLConnection.getInputStream();
                    byte[] adapterJsonBytes = convertIsToByteArray( adapterInputStream );
                    String adaperJson = new String( adapterJsonBytes );
                    Log.w( "karorkefz", "返回:" + adaperJson );
                    FileUtil.writeFile( "/list.json", adaperJson );
                } else {
                    Log.w( "karorkefz", "服务器连接失败:" + httpURLConnection.getResponseCode() );
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.w( "karorkefz", "错误:" + e.toString() );
            }
        } ).start();
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
                } else {
                    LogUtil.w( "karorkefz", "服务器连接失败:" + httpURLConnection.getResponseCode() );
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.w( "karorkefz", "错误:" + e.toString() );
            }
        } ).start();
    }
}
