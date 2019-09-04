package com.ms.karorkefz.xposed;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Parcel;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.Config;
import com.ms.karorkefz.util.FileUtil;
import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.util.ScreenUntil;
import com.ms.karorkefz.view.RoomPasswordDialogViewAdd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MEDIA_PROJECTION_SERVICE;
import static com.ms.karorkefz.util.Shot.getActivity;

class Karaoke_Other_Hook extends RoomPasswordDialogViewAdd {
    Config config;
    Adapter adapter;
    boolean enableOther_Notification, enableOther_Gift_Recorder, enableOther_Gift_Update;
    private ClassLoader classLoader;
    private static int RECORD_REQUEST_CODE = 5;
    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private ScreenUntil recordService = new ScreenUntil();
    Activity activity;
    private boolean gift_list = true;

    Karaoke_Other_Hook(ClassLoader mclassLoader, Config config) throws JSONException {

        classLoader = mclassLoader;
        this.config = config;
        this.adapter = new Adapter( "Other" );
        enableOther_Notification = config.isOn( "enableOther_Notification" );
        enableOther_Gift_Recorder = config.isOn( "enableOther_Gift_Recorder" );
        enableOther_Gift_Update = config.isOn( "enableOther_Gift_Update" );

    }

    public void init() {
        LogUtil.d( "karorkefz", "进入Other" );
        if (enableOther_Notification) {
            try {
                new Karaoke_Notification_Hook( classLoader ).init();//通知栏
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (enableOther_Gift_Recorder) {
            //礼物录屏
            try {
                LogUtil.i( "karorkefz", "礼物录屏" );
                String Gift_Class = adapter.getString( "Gift_Class" );
                String Gift_Method = adapter.getString( "Gift_Method" );
                String GiftInfo_Class = adapter.getString( "GiftInfo_Class" );
                Class GiftInfo = XposedHelpers.findClass( GiftInfo_Class, classLoader );
                String two_Class_String = adapter.getString( "two_Class" );
                Class two_Class = XposedHelpers.findClass( two_Class_String, classLoader );
                XposedHelpers.findAndHookMethod( Gift_Class,
                        classLoader,
                        Gift_Method,// 被Hook的函数
                        GiftInfo,
                        two_Class,
                        two_Class,
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "第1个： " + param.args[0] );
                                LogUtil.d( "karorkefz", "第2个： " + param.args[1] );
                                LogUtil.d( "karorkefz", "第3个： " + param.args[2] );
                                Object one = param.args[0];
                                Field GiftNameField = XposedHelpers.findField( one.getClass(), "GiftName" );
                                String GiftName = (String) GiftNameField.get( one );
                                Field GiftNumField = XposedHelpers.findField( one.getClass(), "GiftNum" );
                                int GiftNum = (int) GiftNumField.get( one );
                                Field GiftPriceField = XposedHelpers.findField( one.getClass(), "GiftPrice" );
                                int GiftPrice = (int) GiftPriceField.get( one );
                                LogUtil.d( "karorkefz", "礼物： " + GiftName + "   数量： " + GiftNum + "   价格：" + GiftPrice );

                                if (GiftName.equals( "鲜花" )) return;
                                if ((GiftNum * GiftPrice) >= 1000) {
                                    LogUtil.d( "karorkefz", "录屏:" );
                                    if (recordService.isRunning()) return;
                                    activity = getActivity();
                                    projectionManager = (MediaProjectionManager) activity.getSystemService( MEDIA_PROJECTION_SERVICE );
                                    Intent captureIntent = projectionManager
                                            .createScreenCaptureIntent();
                                    DisplayMetrics metrics = new DisplayMetrics();
                                    activity.getWindowManager().getDefaultDisplay().getMetrics( metrics );
                                    recordService.setConfig( metrics.widthPixels, metrics.heightPixels, metrics.densityDpi, 10000, "recording" );
                                    activity.startActivityForResult( captureIntent, RECORD_REQUEST_CODE );
                                } else if ((GiftNum * GiftPrice) >= 300) {
                                    LogUtil.d( "karorkefz", "录屏:" );
                                    if (recordService.isRunning()) return;
                                    activity = getActivity();
                                    projectionManager = (MediaProjectionManager) activity.getSystemService( MEDIA_PROJECTION_SERVICE );
                                    Intent captureIntent = projectionManager
                                            .createScreenCaptureIntent();
                                    DisplayMetrics metrics = new DisplayMetrics();
                                    activity.getWindowManager().getDefaultDisplay().getMetrics( metrics );
                                    recordService.setConfig( metrics.widthPixels, metrics.heightPixels, metrics.densityDpi, 3000, "recording" );
                                    activity.startActivityForResult( captureIntent, RECORD_REQUEST_CODE );
                                }
                            }
                        } );
            } catch (Exception e) {
                LogUtil.w( "karorkefz", "录屏礼物监听数据出错:" + e.getMessage() );
            }
            try {
                String LIVE_Gift_onActivityResult_Class = adapter.getString( "Gift_onActivityResult_Class" );
                String LIVE_Gift_onActivityResult_Method = adapter.getString( "Gift_onActivityResult_Method" );
                XposedHelpers.findAndHookMethod( LIVE_Gift_onActivityResult_Class,
                        classLoader,
                        LIVE_Gift_onActivityResult_Method,// 被Hook的函数
                        int.class,
                        int.class,
                        Intent.class,
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                int requestCode = (int) param.args[0];
                                int resultCode = (int) param.args[1];
                                Intent data = (Intent) param.args[2];
                                LogUtil.d( "karorkefz", "requestCode:" + requestCode );
                                LogUtil.d( "karorkefz", "resultCode:" + resultCode );
                                if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK) {
                                    //######## 录屏逻辑 ########
                                    try {
                                        mediaProjection = projectionManager
                                                .getMediaProjection( resultCode, data );
                                        recordService.setMediaProjection( mediaProjection );
                                        recordService.start();
                                    } catch (Exception e) {
                                        LogUtil.d( "karorkefz", "Hook函数：录屏已创建" );
                                        return;
                                    }
                                }
                            }
                        } );
            } catch (Exception e) {
                LogUtil.w( "karorkefz", "录屏礼物监听数据出错:" + e.getMessage() );
            }
        }
        if (enableOther_Gift_Update) {
            if (true) {
                try {
                    String Other_Gift_Live_Close_Class = adapter.getString( "Other_Gift_Live_Close_Class" );
                    String Other_Gift_Live_Close_Method = adapter.getString( "Other_Gift_Live_Close_Method" );
                    XposedHelpers.findAndHookMethod( Other_Gift_Live_Close_Class,
                            classLoader,
                            Other_Gift_Live_Close_Method,// 被Hook的函数
                            Activity.class,
                            new XC_MethodHook() {
                                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                                    LogUtil.e( "karorkefz", "直播间抽奖监听q.e，返回:" + param.args[0].getClass().getName() );
                                    if (gift_list) {
                                        String Other_Gift_Live_Close_String = adapter.getString( "Other_Gift_Live_Close_String" );
                                        if (param.args[0].getClass().getName().equals( Other_Gift_Live_Close_String )) {
                                            gift_list = false;
                                            new AlertDialog.Builder( (Context) param.args[0] )
                                                    .setTitle( "危险警告" )
                                                    .setMessage( "抱歉，使用全民k歌辅助模块的同时,开启抽奖会使礼物列表更新失效" )
                                                    .setPositiveButton( "确定", (dialogInterface, i) -> {
                                                    } )
                                                    .create()
                                                    .show();
                                        }
                                    }
                                }
                            } );
                } catch (Exception e) {
                    gift_list = false;
                    LogUtil.w( "karorkefz", "礼物列表修改，直播间监听出错:" + e.getMessage() );
                }
                try {
                    String Other_Gift_Ktv_Close_Class = adapter.getString( "Other_Gift_Ktv_Close_Class" );
                    String Other_Gift_Ktv_Close_Method = adapter.getString( "Other_Gift_Ktv_Close_Method" );
                    XposedHelpers.findAndHookMethod( Other_Gift_Ktv_Close_Class,
                            classLoader,
                            Other_Gift_Ktv_Close_Method,// 被Hook的函数
                            String.class,
                            new XC_MethodHook() {
                                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                                    String result = String.valueOf( param.args[0] );
                                    LogUtil.v( "karorkefz", "HippyArray:" + "pushString：" + result );
                                    if (result.indexOf( "onLoad" ) != -1 && gift_list) {
                                        LogUtil.e( "karorkefz", "存在礼物敏感字，返回" );
                                        gift_list = false;
                                        new AlertDialog.Builder( getActivity() )
                                                .setTitle( "危险警告" )
                                                .setMessage( "抱歉，使用全民k歌辅助模块的同时,开启抽奖会使礼物列表更新失效" )
                                                .setPositiveButton( "确定", (dialogInterface, i) -> {
                                                } )
                                                .create()
                                                .show();
                                    }
                                }
                            } );
                } catch (Exception e) {
                    gift_list = false;
                    LogUtil.w( "karorkefz", "礼物列表修改，歌房监听出错:" + e.getMessage() );
                }
            }
            //礼物列表
            if (gift_list) {
                String Gift_List_json = null;
                String Gift_List = adapter.getString( "Gift_List" );
                try {
                    Gift_List_json = FileUtil.readFileSdcardFile( "/list.json" );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    String Gift_List_Class_String = adapter.getString( "Gift_List_Class" );
                    String Gift_List_Method_String = adapter.getString( "Gift_List_Method" );
                    String Gift_Data_Class_String = adapter.getString( "Gift_Data_Class" );
                    Class<?> GiftData = XposedHelpers.findClass( Gift_Data_Class_String, classLoader );
                    if (Gift_List_json != null) {
                        Gift_List = Gift_List_json;
                    }
                    String finalGift_List = Gift_List;
                    XposedHelpers.findAndHookMethod( Gift_List_Class_String,
                            classLoader,
                            Gift_List_Method_String,// 被Hook的函数
                            List.class,
                            new XC_MethodHook() {
                                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                                    Gson kGson = new Gson();
                                    String kVar2_jsonString = kGson.toJson( param.args[0] );
                                    LogUtil.i( "karorkefz", "原礼物列表:" + kVar2_jsonString );

                                    List list = (List) param.args[0];
                                    if (gift_list) {
                                        JSONArray p_jsonJSONArray = new JSONArray( finalGift_List );
                                        int len = p_jsonJSONArray.length();
                                        for (int z = 0; z < len; z++) {
                                            JSONObject p_jsonJSONObject = p_jsonJSONArray.getJSONObject( z );
                                            long a = Long.parseLong( p_jsonJSONObject.getString( "GiftId" ) );
                                            long b = Long.parseLong( p_jsonJSONObject.getString( "GiftPrice" ) );
                                            String c = p_jsonJSONObject.getString( "GiftLogo_str" );
                                            String d = p_jsonJSONObject.getString( "GiftLogo" );
                                            String e = p_jsonJSONObject.getString( "GiftName" );
                                            int f = Integer.parseInt( p_jsonJSONObject.getString( "f" ) );
                                            long g = Long.parseLong( p_jsonJSONObject.getString( "g" ) );
                                            int h = Integer.parseInt( p_jsonJSONObject.getString( "h" ) );
                                            Parcel parcle = Parcel.obtain();
                                            parcle.writeLong( a );
                                            parcle.writeLong( b );
                                            parcle.writeString( c );
                                            parcle.writeString( d );
                                            parcle.writeString( e );
                                            parcle.writeInt( f );
                                            parcle.writeLong( g );
                                            parcle.writeInt( h );
                                            parcle.marshall();
                                            parcle.setDataPosition( 0 );
                                            Object GiftDataObject = XposedHelpers.newInstance( GiftData, parcle );
                                            list.add( GiftDataObject );
                                        }
                                    }
                                }
                            } );
                } catch (Exception e) {
                    LogUtil.w( "karorkefz", "礼物列表修改出错:" + e.getMessage() );
                }
            }
        }
    }
}