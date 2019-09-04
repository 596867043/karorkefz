package com.ms.karorkefz.xposed;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.ChatList;
import com.ms.karorkefz.util.ColationList;
import com.ms.karorkefz.util.Config;
import com.ms.karorkefz.util.FileUtil;
import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.util.TimeHook;
import com.ms.karorkefz.view.RoomPasswordDialogViewAdd;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import static com.ms.karorkefz.util.ColationList.colationKtvList;

class Karaoke_Ktv_Hook extends RoomPasswordDialogViewAdd {
    Config config;
    Adapter adapter;
    boolean enableKTV_cS, enableKTVY, enableKTVIN, enableKTV_Robot, enableRecording;
    boolean start = false;
    boolean b = false;
    Object hObject, h$26Object, WeObject;
    WeakReference one;
    com.ms.karorkefz.util.ColationList ColationList;
    private ClassLoader classLoader;
    private EditText inputEditText;
    private int i = 0000;

    Karaoke_Ktv_Hook(ClassLoader mclassLoader, Config config) throws JSONException {

        classLoader = mclassLoader;
        this.config = config;
        this.adapter = new Adapter( "Ktv" );
        enableKTV_cS = config.isOn( "enableKTV_cS" );
        enableKTVY = config.isOn( "enableKTVY" );
        enableKTVIN = config.isOn( "enableKTVIN" );
        enableKTV_Robot = config.isOn( "enableKTV_Robot" );
        enableRecording = config.isOn( "enableRecording" );

        ColationList = new ColationList();
    }

    public void init() throws JSONException {
        LogUtil.d( "karorkefz", "进入ktv" );
        if (enableRecording) {
            new Karaoke_KaraScore_Hook( classLoader ).init();
        }
        if (enableKTVY) {
            try {
                String KTVY_Class_String = adapter.getString( "KTVY_Class" );
                String KTVY_Method_String = adapter.getString( "KTVY_Method" );
                String KTVY_Field_String = adapter.getString( "KTVY_Field" );
                String KTVY_Field_call_String = adapter.getString( "KTVY_Field_call" );
                XposedHelpers.findAndHookMethod( KTVY_Class_String,
                        classLoader,
                        KTVY_Method_String,
                        new XC_MethodHook() {
                            boolean i = true;

                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "Ktv-find" );
                                Field quRenField = XposedHelpers.findField( param.thisObject.getClass(), KTVY_Field_String );
                                final ImageView quRenButton = (ImageView) quRenField.get( param.thisObject );
                                quRenButton.setOnClickListener( new Button.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        LogUtil.d( "karorkefz", "Ktv-onclick" );
                                        if (i) {
                                            LogUtil.d( "karorkefz", "Ktv-onclick-if" );
                                            XposedHelpers.callMethod( param.thisObject, KTVY_Field_call_String );
                                            i = !i;
                                        } else {
                                            LogUtil.d( "karorkefz", "Ktv-onclick-if：else" );
                                            i = !i;
                                        }
                                    }
                                } );
                            }
                        } );
            } catch (Exception e) {
                LogUtil.w( "karorkefz", "歌房语音席出错:" + e.getMessage() );
            }
        }
        if (enableKTV_cS) {
            try {
                String KTV_cS_Class_String = adapter.getString( "KTV_cS_Class" );
                String KTV_cS_Method_String = adapter.getString( "KTV_cS_Method" );
                XposedHelpers.findAndHookMethod( KTV_cS_Class_String,
                        classLoader,
                        KTV_cS_Method_String,
                        new XC_MethodHook() {
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                param.setResult( false );
                            }
                        } );
            } catch (Exception e) {
                LogUtil.w( "karorkefz", "歌房滑动出错:" + e.getMessage() );
            }
        }
        if (enableKTVIN) {
            try {
                String KTVIN_Function_Class_String = adapter.getString( "KTVIN_Function_Class" );
                XposedHelpers.findAndHookConstructor( KTVIN_Function_Class_String, classLoader,
                        new XC_MethodHook() {
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "调用函数准备" );
                                hObject = param.thisObject;
                            }
                        } );
                String KTVIN_InputEditText_Class_String = adapter.getString( "KTVIN_InputEditText_Class" );
                String KTVIN_InputEditText_Method_String = adapter.getString( "KTVIN_InputEditText_Method" );
                String KTVIN_InputEditText_Field_String = adapter.getString( "KTVIN_InputEditText_Field" );
                XposedHelpers.findAndHookMethod( KTVIN_InputEditText_Class_String,
                        classLoader,
                        KTVIN_InputEditText_Method_String,
                        new XC_MethodHook() {
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "输入框准备" );
                                Field inputField = XposedHelpers.findField( param.thisObject.getClass(), KTVIN_InputEditText_Field_String );
                                inputEditText = (EditText) inputField.get( param.thisObject );
                                LinearLayout inputEditTextParent = (LinearLayout) inputEditText.getParent();
                                LinearLayout Parent = (LinearLayout) inputEditTextParent.getParent();
                                Context context = AndroidAppHelper.currentApplication();
                                password( Parent, context );
                            }
                        } );
                String KTVIN_onClick_Class_String = adapter.getString( "KTVIN_onClick_Class" );
                String KTVIN_onClick_Method_String = adapter.getString( "KTVIN_onClick_Method" );
                XposedHelpers.findAndHookMethod( KTVIN_onClick_Class_String,
                        classLoader,
                        KTVIN_onClick_Method_String,
                        View.class,
                        new XC_MethodHook() {
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                start = false;
                            }
                        } );
                String KTVIN_failure_Class_String = adapter.getString( "KTVIN_failure_Class" );
                String KTVIN_failure_Method_String = adapter.getString( "KTVIN_failure_Method" );
                String KTVIN_failure_oneClass_String = adapter.getString( "KTVIN_failure_oneClass" );
                Class<?> failure_oneClass = XposedHelpers.findClass( KTVIN_failure_oneClass_String, classLoader );
                XposedHelpers.findAndHookMethod( KTVIN_failure_Class_String,
                        classLoader,
                        KTVIN_failure_Method_String,
                        failure_oneClass,
                        int.class,
                        String.class,
                        int.class,
                        new XC_MethodHook() {
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                String resultMsg = String.valueOf( param.args[2] );
                                if (enableKTVIN && start) {
                                    try {
                                        String text = String.format( "%04d", i );
                                        LogUtil.d( "karorkefz", "密码:" + text );
                                        if (resultMsg.equals( "密码不正确" )) {
                                            LogUtil.d( "karorkefz", "密码不正确" );
                                            config.setPassword( "password", i );
                                            try {
                                                inputEditText.setText( text );
                                            } catch (Exception e) {
                                                LogUtil.d( "karorkefz", e.toString() );
                                            }

                                            XposedHelpers.callMethod( h$26Object, "a", text );
                                            i++;
                                        }
                                        if (resultMsg.equals( "null" ) && b) {
                                            b = false;
                                            i--;
                                            if (i == -001) return;
                                            text = String.format( "%04d", i );
                                            config.setPassword( "password", i );
                                            String filetext = TimeHook.SimpleDateFormat_Time() + " : " + text;
                                            FileUtil.writeFileSdcardFile( "/txt/password.txt", filetext );
                                            try {
                                                inputEditText.setText( text );
                                            } catch (Exception e) {
                                                LogUtil.d( "karorkefz", e.toString() );
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }
                        } );
            } catch (Exception e) {
                LogUtil.w( "karorkefz", "歌房密码破解出错:" + e.getMessage() );
            }
        }
        if (enableKTV_Robot) {
            try {
                String Robot_Function_Class_String = adapter.getString( "Robot_Function_Class" );
                String Robot_Function_Method_String = adapter.getString( "Robot_Function_Method" );
                XposedHelpers.findAndHookMethod( Robot_Function_Class_String,
                        classLoader,
                        Robot_Function_Method_String,
                        WeakReference.class,
                        String.class,
                        String.class,
                        int.class,
                        ArrayList.class,
                        String.class,
                        new XC_MethodHook() {
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", String.valueOf( param.args[0] ) );
                                LogUtil.d( "karorkefz", String.valueOf( param.args[1] ) );
                                LogUtil.d( "karorkefz", String.valueOf( param.args[2] ) );
                                LogUtil.d( "karorkefz", String.valueOf( param.args[3] ) );
                                LogUtil.d( "karorkefz", String.valueOf( param.args[4] ) );
                                LogUtil.d( "karorkefz", String.valueOf( param.args[5] ) );
                                one = (WeakReference) param.args[0];
                            }
                        } );
                Class<?> We = XposedHelpers.findClass( "com.tencent.karaoke.module.ktv.ui.We", classLoader );

                XposedHelpers.findAndHookConstructor( "com.tencent.karaoke.module.ktv.ui.Ie", classLoader,
                        We,
                        new XC_MethodHook() {
                            protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "Ktv-监听自己发送" );
                                WeObject = param.args[0];
                                LogUtil.d( "karorkefz", String.valueOf( param.args[0] ) );
                            }
                        } );
                String Robot_Listener_Class_String = adapter.getString( "Robot_Listener_Class" );
                String Robot_Listener_Method_String = adapter.getString( "Robot_Listener_Method" );
                XposedHelpers.findAndHookMethod( Robot_Listener_Class_String,
                        classLoader,
                        Robot_Listener_Method_String,
                        List.class,
                        new XC_MethodHook() {
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                List list = (List) param.args[0];
                                LogUtil.d( "karorkefz", "Ktv_list监听" );
                                WeakReference senObject = ChatList.Ktv_lists( list );
                                if (senObject != null) {
                                    ChatList.ObjectCache sendq = (ChatList.ObjectCache) senObject.get();
                                    if (sendq.getType() == 0) {
                                        send( sendq.getText() );
                                    } else {
                                        send( sendq.getType(), sendq.getUid(), sendq.getText() );
                                    }
                                }
                            }
                        } );
            } catch (Exception e) {
                LogUtil.w( "karorkefz", "歌房机器人出错:" + e.getMessage() );
            }
        }
    }


    public void startOnClick() throws Exception {
        try {
            start = true;
            b = true;
            i = 0000;
            String text = String.format( "%04d", i );
            String KTVIN_send_Class_String = adapter.getString( "KTVIN_send_Class" );
            String KTVIN_send_Method_String = adapter.getString( "KTVIN_send_Method" );
            Class<?> h$26 = XposedHelpers.findClass( KTVIN_send_Class_String, classLoader );
            h$26Object = XposedHelpers.newInstance( h$26, hObject );
            XposedHelpers.callMethod( h$26Object, KTVIN_send_Method_String, text );
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "歌房密码破解出错:" + e.getMessage() );
        }
    }

    public void proceedOnClick() throws Exception {
        try {
            start = true;
            b = true;
            i = config.getPassword( "password" );
            String text = String.format( "%04d", i );
            String KTVIN_send_Class_String = adapter.getString( "KTVIN_send_Class" );
            String KTVIN_send_Method_String = adapter.getString( "KTVIN_send_Method" );
            Class<?> h$26 = XposedHelpers.findClass( KTVIN_send_Class_String, classLoader );
            h$26Object = XposedHelpers.newInstance( h$26, hObject );
            XposedHelpers.callMethod( h$26Object, KTVIN_send_Method_String, text );
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "歌房密码破解出错:" + e.getMessage() );
        }
    }

    private void send(int type, int uid, String text) throws InstantiationException, IllegalAccessException, JSONException {
        LogUtil.d( "karorkefz", "Ktv_send" );
        if (type == 2 && colationKtvList.contains( uid )) {
            LogUtil.d( "karorkefz", "过滤名单，存在:" + uid + "  " + text );
            return;
        }
        try {
            String Robot_send_Class_String = adapter.getString( "Robot_send_Class" );
            String Robot_send_Method_String = adapter.getString( "Robot_send_Method" );
            Class Robot_send_Class = XposedHelpers.findClass( Robot_send_Class_String, classLoader );
            Object Robot_send_Object = XposedHelpers.newInstance( Robot_send_Class );
            String KaraokeContext_Class_String = adapter.getString( "KaraokeContext_Class" );
            String getRoomController_Method_String = adapter.getString( "getRoomController_Method" );
            String getRoomController_data_Method_String = adapter.getString( "getRoomController_data_Method" );
            Class KC = XposedHelpers.findClass( KaraokeContext_Class_String, classLoader );
            Object KCObject = KC.newInstance();
            Object getRoomController = XposedHelpers.callMethod( KCObject, getRoomController_Method_String );
            Object b = XposedHelpers.callMethod( getRoomController, getRoomController_data_Method_String );
            Gson bGson = new Gson();
            String b_jsonString = bGson.toJson( b );
            JSONObject b_JSONObject = new JSONObject( b_jsonString );
            String strRoomId = b_JSONObject.getString( "strRoomId" );
            String strShowId = b_JSONObject.getString( "strShowId" );
            final ArrayList five = new ArrayList();
            five.add( Long.valueOf( uid ) );
            String six = text;
            LogUtil.d( "karorkefz", "send:" + one );
            if (one != null) {
                XposedHelpers.callMethod( Robot_send_Object, Robot_send_Method_String, one, strRoomId, strShowId, 1, five, six );
                send( six );
            }
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "歌房机器人发送信息出错:" + e.getMessage() );
        }
    }

    private void send(String text) {
        try {
            String Robot_sendself_Function_Method_String = adapter.getString( "Robot_sendself_Function_Method" );
            String Robot_sendself_Method_String = adapter.getString( "Robot_sendself_Method" );
            Object UserInfoCacheDataObject = XposedHelpers.callMethod( WeObject, Robot_sendself_Function_Method_String, WeObject );
            XposedHelpers.callMethod( WeObject, Robot_sendself_Method_String, UserInfoCacheDataObject, text );
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "歌房机器人自我发送信息出错:" + e.getMessage() );
        }
    }
}