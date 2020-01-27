package com.ms.karorkefz.xposed;

import android.annotation.SuppressLint;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.ms.karorkefz.thread.MyThread;
import com.ms.karorkefz.util.ChatList;
import com.ms.karorkefz.util.ColationList;
import com.ms.karorkefz.util.Config;
import com.ms.karorkefz.util.Constant;
import com.ms.karorkefz.util.FileUtil;
import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.util.TimeHook;
import com.ms.karorkefz.view.RoomPasswordDialogViewAdd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import static com.ms.karorkefz.util.ColationList.colationKtvList;

class Karaoke_Ktv_Hook extends RoomPasswordDialogViewAdd {
    com.ms.karorkefz.thread.MyThread MyThread = new MyThread();
    Config config;
    boolean enableKTV_cS, enableKTVY, enableKTVIN, enableKTV_Robot, enableRecording, Auto_Song;
    boolean start = false;
    boolean b = false;
    Object hObject, h$26Object, WeObject;
    WeakReference one;
    String strRoomId, strShowId;
    com.ms.karorkefz.util.ColationList ColationList;
    private ClassLoader classLoader;
    private Handler handler;
    private int i = 0000;
    static Object Song_inquiry, Song_inquiry_locality, KtvDownloadObbDialog, l;
    static int list = 0, update = 0;
    static ArrayList SongList;

    Karaoke_Ktv_Hook(Context context) {
        classLoader = context.getClassLoader();
        this.config = new Config( context );
        enableKTV_cS = config.isOn( "enableKTV_cS" );
        enableKTVY = config.isOn( "enableKTVY" );
        enableKTVIN = config.isOn( "enableKTVIN" );
        enableKTV_Robot = config.isOn( "enableKTV_Robot" );
        enableRecording = config.isOn( "enableRecording" );
        Auto_Song = config.isOn( "enableKTV_Auto_Song" );
        ColationList = new ColationList();
    }

    public void init() {
        LogUtil.d( "karorkefz", "进入ktv" );
        if (enableRecording) {
            new Karaoke_KaraScore_Hook( classLoader ).init();
        }
        if (enableKTVY) {
            try {
                String KTVY_Function_Class = Constant.adapter.getString( "KTV_Y_Function_Class" );
                String KTVY_Function_Method = Constant.adapter.getString( "KTV_Y_Function_Method" );
                String KTVY_Function_one_Class = Constant.adapter.getString( "KTV_Y_Function_one_Class" );
                XposedHelpers.findAndHookMethod( KTVY_Function_Class,
                        classLoader,
                        KTVY_Function_Method,
                        XposedHelpers.findClass( KTVY_Function_one_Class, classLoader ),
                        new XC_MethodHook() {
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "KtvY-Function" );
                                l = param.args[0];
                            }
                        } );
                String KTVY_Class = Constant.adapter.getString( "KTV_Y_Class" );
                String KTVY_Method = Constant.adapter.getString( "KTV_Y_Method" );
                String KTVY_one_Class = Constant.adapter.getString( "KTV_Y_one_Class" );
                String KTVY_call_Method_String = Constant.adapter.getString( "KTV_Y_call_Method" );
                XposedHelpers.findAndHookMethod( KTVY_Class,
                        classLoader,
                        KTVY_Method,
                        int.class,
                        MotionEvent.class,
                        XposedHelpers.findClass( KTVY_one_Class, classLoader ),
                        new XC_MethodHook() {
                            boolean i = true;

                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "KtvY-find" );
                                int j = (int) param.args[0];
                                MotionEvent k = (MotionEvent) param.args[1];
                                if (j == -2 && k.getAction() == 1) {
                                    LogUtil.d( "karorkefz", "Ktv-onclick" );
                                    if (i) {
                                        LogUtil.d( "karorkefz", "Ktv-onclick-if" );
                                        Object bottomv = param.thisObject;
                                        if (l != null) {
                                            XposedHelpers.callMethod( bottomv, KTVY_call_Method_String, j, l );
                                        }
                                        i = !i;
                                    } else {
                                        LogUtil.d( "karorkefz", "Ktv-onclick-if：else" );
                                        i = !i;
                                    }
                                }
                            }
                        } );
            } catch (Exception e) {
                LogUtil.w( "karorkefz", "歌房语音席出错:" + e.getMessage() );
            }
        }
        if (enableKTV_cS) {
            try {
                String KTV_cS_Class_String = Constant.adapter.getString( "KTV_cS_Class" );
                String KTV_cS_Method_String = Constant.adapter.getString( "KTV_cS_Method" );
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
                String KTVIN_Function_Class_String = Constant.adapter.getString( "KTV_IN_Function_Class" );
                XposedHelpers.findAndHookConstructor( KTVIN_Function_Class_String, classLoader,
                        new XC_MethodHook() {
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "调用函数准备" );
                                hObject = param.thisObject;
                            }
                        } );
                String KTVIN_InputEditText_Class_String = Constant.adapter.getString( "KTV_IN_InputEditText_Class" );
                String KTVIN_InputEditText_Method_String = Constant.adapter.getString( "KTV_IN_InputEditText_Method" );
                String KTVIN_InputEditText_Field_String = Constant.adapter.getString( "KTV_IN_InputEditText_Field" );
                XposedHelpers.findAndHookMethod( KTVIN_InputEditText_Class_String,
                        classLoader,
                        KTVIN_InputEditText_Method_String,
                        new XC_MethodHook() {
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "输入框准备" );
                                Field inputField = XposedHelpers.findField( param.thisObject.getClass(), KTVIN_InputEditText_Field_String );
                                EditText inputEditText = (EditText) inputField.get( param.thisObject );
                                LinearLayout inputEditTextParent = (LinearLayout) inputEditText.getParent();
                                LinearLayout Parent = (LinearLayout) inputEditTextParent.getParent();
                                Context context = AndroidAppHelper.currentApplication();
                                password( Parent, context );
                                handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage( msg );
                                        switch (msg.what) {
                                            case 0:
                                                inputEditText.setText( msg.obj.toString() );
                                                break;
                                        }
                                    }
                                };
                            }
                        } );
                String KTVIN_onClick_Class_String = Constant.adapter.getString( "KTV_IN_onClick_Class" );
                String KTVIN_onClick_Method_String = Constant.adapter.getString( "KTV_IN_onClick_Method" );
                XposedHelpers.findAndHookMethod( KTVIN_onClick_Class_String,
                        classLoader,
                        KTVIN_onClick_Method_String,
                        View.class,
                        new XC_MethodHook() {
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                start = false;
                            }
                        } );
                String KTVIN_failure_Class_String = Constant.adapter.getString( "KTV_IN_failure_Class" );
                String KTVIN_failure_Method_String = Constant.adapter.getString( "KTV_IN_failure_Method" );
                String KTVIN_failure_oneClass_String = Constant.adapter.getString( "KTV_IN_failure_oneClass" );
                String KTVIN_send_Method_String = Constant.adapter.getString( "KTV_IN_send_Method" );
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
                                                Message msg = new Message();
                                                msg.what = 0;
                                                msg.obj = text;
                                                handler.sendMessage( msg );
                                            } catch (Exception e) {
                                                LogUtil.d( "karorkefz", e.toString() );
                                            }

                                            XposedHelpers.callMethod( h$26Object, KTVIN_send_Method_String, text );
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
                                                Message msg = new Message();
                                                msg.what = 0;
                                                msg.obj = text;
                                                handler.sendMessage( msg );
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
                String Robot_Function_Class_String = Constant.adapter.getString( "KTV_Robot_Function_Class" );
                String Robot_Function_Method_String = Constant.adapter.getString( "KTV_Robot_Function_Method" );
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
                                strRoomId = String.valueOf( param.args[1] );
                                strShowId = String.valueOf( param.args[2] );
                            }
                        } );
                String Robot_Listener_my_Class = Constant.adapter.getString( "KTV_Robot_Listener_my_Class" );
                String Robot_Listener_my_Class_one = Constant.adapter.getString( "KTV_Robot_Listener_my_Class_one" );
                XposedHelpers.findAndHookConstructor( Robot_Listener_my_Class, classLoader,
                        XposedHelpers.findClass( Robot_Listener_my_Class_one, classLoader ),
                        new XC_MethodHook() {
                            protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "Ktv-监听自己发送" );
                                WeObject = param.args[0];
                                LogUtil.d( "karorkefz", String.valueOf( param.args[0] ) );
                            }
                        } );
                String Robot_Listener_Class_String = Constant.adapter.getString( "KTV_Robot_Listener_Class" );
                String Robot_Listener_Method_String = Constant.adapter.getString( "KTV_Robot_Listener_Method" );
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
                                    if (sendq.getText().indexOf( "开启自动点歌" ) != -1) Song_inquiry();
                                }
                            }
                        } );
            } catch (Exception e) {
                LogUtil.w( "karorkefz", "歌房机器人出错:" + e.getMessage() );
            }
        }
        //自动点歌
        if (Auto_Song) {
            boolean Auto_wheat = config.isOn( "enableKTV_Auto_wheat" );
            boolean Auto_Song_inquiry = config.isOn( "enableKTV_Auto_Song_inquiry" );
            boolean Auto_Listener_Song_List = config.isOn( "enableKTV_Auto_Listener_Song_List" );
            if (Auto_wheat) {
                //音频上麦按钮点击
                String InformGetMicDialog_Class_String = Constant.adapter.getString( "KTV_Auto_Song_InformGetMicDialog_Class" );
                String InformGetMicDialog_Class_one_String = Constant.adapter.getString( "KTV_Auto_Song_InformGetMicDialog_one_Class" );
                XposedHelpers.findAndHookConstructor( InformGetMicDialog_Class_String,
                        classLoader,
                        XposedHelpers.findClass( InformGetMicDialog_Class_one_String, classLoader ),
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "InformGetMicDialog:" + param.args[0] );
                                new Thread() {
                                    @SuppressLint("ResourceType")
                                    public void run() {
                                        try {
                                            String Mic_Button_String = Constant.adapter.getString( "KTV_Auto_Song_Mic_Button" );
                                            String Mic_onClick_Method_String = Constant.adapter.getString( "KTV_Auto_Song_Mic_onClick_Method" );
                                            sleep( 5000 );
                                            View view = new View( AndroidAppHelper.currentApplication() );
                                            view.setId( Integer.parseInt( Mic_Button_String, 16 ) );
                                            XposedHelpers.callMethod( param.thisObject, Mic_onClick_Method_String, view );
                                            LogUtil.d( "karorkefz", "InformGetMicDialog点击" );
                                            sleep( 5000 );

                                            String KaraokeContext_String = Constant.adapter.getString( "KTV_Auto_Song_KaraokeContext" );
                                            Class KaraokeContext = XposedHelpers.findClass( KaraokeContext_String, classLoader );
                                            Object KaraokeContextObject = KaraokeContext.newInstance();

                                            String getKtvController_String = Constant.adapter.getString( "KTV_Auto_Song_getKtvController" );
                                            Object KtvController = XposedHelpers.callMethod( KaraokeContextObject, getKtvController_String );

                                            String Original_String = Constant.adapter.getString( "KTV_Auto_Song_Original" );
                                            XposedHelpers.callMethod( KtvController, Original_String, false );
                                            LogUtil.d( "karorkefz", "打开原唱" );
                                        } catch (InterruptedException | IllegalAccessException | InstantiationException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            }
                        } );
            }
            if (Auto_Song_inquiry) {
                //自动点歌
                //歌曲列表监听
                String Listener_Music_List_Class_String = Constant.adapter.getString( "KTV_Auto_Song_Listener_Music_List_Class" );
                String Listener_Music_List_Method_String = Constant.adapter.getString( "KTV_Auto_Song_Listener_Music_List_Method" );
                //歌曲列表监听
                XposedHelpers.findAndHookMethod( Listener_Music_List_Class_String,
                        classLoader,
                        Listener_Music_List_Method_String,
                        String.class,
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                String paramString = String.valueOf( param.args[0] );

                                String string = paramString.substring( paramString.indexOf( "{" ) );
                                LogUtil.d( "karorkefz", "a$a_String:" + string );

                                if (string.indexOf( "vctHitedSongInfo" ) != -1) {
                                    JSONObject a$a_JSONObject = new JSONObject( string );

                                    JSONArray vctSongInfo_JSONArray = a$a_JSONObject.getJSONObject( "data" ).getJSONObject( "diange.get_hited_song" ).getJSONArray( "vctHitedSongInfo" );
                                    LogUtil.d( "karorkefz", "Song_JSONArray:" + vctSongInfo_JSONArray );
                                    LogUtil.d( "karorkefz", "Song_JSONArray:" + vctSongInfo_JSONArray.length() );
                                    SongInfo( vctSongInfo_JSONArray, classLoader );
                                }
                            }

                            private void SongInfo(JSONArray jsonArray, ClassLoader classLoader) throws InstantiationException, IllegalAccessException, JSONException {
                                ArrayList List = new ArrayList();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject( i );
                                    JSONObject song_jsonObject = jsonObject.getJSONObject( "stSongInfo" );
                                    Object SongInfo = XposedHelpers.findClass( "proto_ktvdata.SongInfo", classLoader ).newInstance();
                                    Iterator<String> keys = song_jsonObject.keys();
                                    String key;
                                    for (int j = 0; j < song_jsonObject.length(); j++) {
                                        key = keys.next();
                                        if (key.equals( "stRecItem" )) continue;
                                        if (key.equals( "mapContent" )) continue;
                                        XposedHelpers.setObjectField( SongInfo, key, song_jsonObject.get( key ) );
                                    }
                                    List.add( SongInfo );
                                }
                                if (SongList == null) {
                                    SongList = List;
                                } else {
                                    SongList.addAll( List );
                                }
                                LogUtil.d( "karorkefz", "SongList:" + SongList.size() );
                            }
                        } );
                //歌曲信息发送
                String Music_Send_Class_String = Constant.adapter.getString( "KTV_Auto_Song_Music_Send_Class" );
                String Music_Send_Class_one = Constant.adapter.getString( "KTV_Auto_Song_Music_Send_Class_one" );
                XposedHelpers.findAndHookConstructor( Music_Send_Class_String,
                        classLoader,
                        XposedHelpers.findClass( Music_Send_Class_one, classLoader ),
                        int.class,
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                LogUtil.i( "karorkefz", "KtvDownloadObbDialog:" + param.args[0] );
                                LogUtil.i( "karorkefz", "KtvDownloadObbDialog:" + param.args[1] );
                                KtvDownloadObbDialog = param.thisObject;
                            }
                        } );
                //启动点歌
                String Start_Send_locality_Class_String = Constant.adapter.getString( "KTV_Auto_Song_Start_Send_locality_Class" );
                String Start_Send_locality_Class_one = Constant.adapter.getString( "KTV_Auto_Song_Start_Send_locality_Class_one" );
                String Start_Send_locality_Class_two = Constant.adapter.getString( "KTV_Auto_Song_Start_Send_locality_Class_two" );
                XposedHelpers.findAndHookConstructor( Start_Send_locality_Class_String,
                        classLoader,
                        XposedHelpers.findClass( Start_Send_locality_Class_one, classLoader ),
                        XposedHelpers.findClass( Start_Send_locality_Class_two, classLoader ),
                        int.class,
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                Gson gson = new Gson();
                                String kVar2_jsonString = gson.toJson( param.args[1] );
                                LogUtil.i( "karorkefz", "ca:" + param.args[0] );
                                LogUtil.i( "karorkefz", "ca:" + kVar2_jsonString );
                                Song_inquiry_locality = param.thisObject;
                            }
                        } );
                String Start_Send_Class_String = Constant.adapter.getString( "KTV_Auto_Song_Start_Send_Class" );
                String Start_Send_Class_one = Constant.adapter.getString( "KTV_Auto_Song_Start_Send_Class_one" );
                String Start_Send_Class_two = Constant.adapter.getString( "KTV_Auto_Song_Start_Send_Class_two" );
                XposedHelpers.findAndHookConstructor( Start_Send_Class_String,
                        classLoader,
                        XposedHelpers.findClass( Start_Send_Class_one, classLoader ),
                        XposedHelpers.findClass( Start_Send_Class_two, classLoader ),
                        int.class,
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                Song_inquiry = param.thisObject;
                            }
                        } );
                //下麦监听
                String Listener_Mic_down_Class_String = Constant.adapter.getString( "KTV_Auto_Song_Listener_Mic_down_Class" );
                String Listener_Mic_down_Method_String = Constant.adapter.getString( "KTV_Auto_Song_Listener_Mic_down_Method" );
                String Listener_Mic_down_oneClass_String = Constant.adapter.getString( "KTV_Auto_Song_Listener_Mic_down_oneClass" );
                XposedHelpers.findAndHookMethod( Listener_Mic_down_Class_String,
                        classLoader,
                        Listener_Mic_down_Method_String,
                        XposedHelpers.findClass( Listener_Mic_down_oneClass_String, classLoader ),
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                Gson kGson = new Gson();
                                String kVar2_jsonString = kGson.toJson( param.args[0] );
                                LogUtil.e( "karorkefz", kVar2_jsonString );
                                JSONObject kVar2_JSONObject = new JSONObject( kVar2_jsonString );
                                try {
                                    // 直接可得数据
                                    String Listener_Mic_down_mapExt = Constant.adapter.getString( "KTV_Auto_Song_Listener_Mic_down_mapExt" );
                                    String mapExt_jsonString = kVar2_JSONObject.getString( Listener_Mic_down_mapExt );
                                    JSONObject mapExt_JSONObject = new JSONObject( mapExt_jsonString );
                                    // 直接可得数据
                                    String Listener_Mic_down_reason = Constant.adapter.getString( "KTV_Auto_Song_Listener_Mic_down_reason" );
                                    String reason = mapExt_JSONObject.getString( Listener_Mic_down_reason );

                                    // 直接可得数据
                                    String Listener_Mic_down_stActUser = Constant.adapter.getString( "KTV_Auto_Song_Listener_Mic_down_stActUser" );
                                    String stActUser_jsonString = kVar2_JSONObject.getString( Listener_Mic_down_stActUser );
                                    JSONObject stActUser_JSONObject = new JSONObject( stActUser_jsonString );
                                    // 直接可得数据
                                    String Listener_Mic_down_uid = Constant.adapter.getString( "KTV_Auto_Song_Listener_Mic_down_uid" );
                                    int uid = stActUser_JSONObject.getInt( Listener_Mic_down_uid );
                                    LogUtil.e( "karorkefz", "reason:" + reason + "uid:" + uid );
                                    if (reason.equals( "领唱下麦了" )) {
                                        update = 0;
                                        if (uid == Constant.uid) {
                                            LogUtil.e( "karorkefz", "监听再次调用" );
                                            Song_inquiry();
                                        }
                                    }
                                    if (reason.equals( "歌曲播放结束，系统自动切麦了~" )) {
                                        if (Song_inquiry == null) return;
                                        if (++update == 5) {
                                            Song_inquiry();
                                            update = 0;
                                        }
                                    }
                                } catch (Exception e) {
                                    LogUtil.e( "karorkefz", "监听下麦错误：" + e.getMessage() );
                                }
                            }
                        } );
            }
            if (Auto_Listener_Song_List) {
                //抓取麦序数据
                String Listener_Song_List_Class_String = Constant.adapter.getString( "KTV_Auto_Song_Listener_Song_List_Class" );
                String Listener_Song_List_Method_String = Constant.adapter.getString( "KTV_Auto_Song_Listener_Song_List_Method" );
                XposedHelpers.findAndHookMethod( Listener_Song_List_Class_String,
                        classLoader,
                        Listener_Song_List_Method_String,
                        ArrayList.class,
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                ArrayList list = (ArrayList) param.args[0];
                                if (list.size() > 1) {
                                    String KaraokeContext_String = Constant.adapter.getString( "KTV_Auto_Song_KaraokeContext" );
                                    Class KaraokeContext = XposedHelpers.findClass( KaraokeContext_String, classLoader );
                                    Object KaraokeContextObject = KaraokeContext.newInstance();

                                    String getKtvController_String = Constant.adapter.getString( "KTV_Auto_Song_getKtvController" );
                                    Object KtvController = XposedHelpers.callMethod( KaraokeContextObject, getKtvController_String );

                                    String Auto_xiamai_Method_String = Constant.adapter.getString( "KTV_Auto_Song_Auto_xiamai_Method" );
                                    XposedHelpers.callMethod( KtvController, Auto_xiamai_Method_String, true, false, true, true );
                                    LogUtil.d( "karorkefz", KtvController.getClass().getName() );
                                    LogUtil.d( "karorkefz", "点击下麦" );
                                }
                            }
                        } );
            }
        }
    }


    public void startOnClick() {
        try {
            start = true;
            b = true;
            i = 0000;
            String text = String.format( "%04d", i );
            String KTVIN_send_Class_String = Constant.adapter.getString( "KTV_IN_send_Class" );
            String KTVIN_send_Method_String = Constant.adapter.getString( "KTV_IN_send_Method" );
            Class<?> h$26 = XposedHelpers.findClass( KTVIN_send_Class_String, classLoader );
            h$26Object = XposedHelpers.newInstance( h$26, hObject );
            XposedHelpers.callMethod( h$26Object, KTVIN_send_Method_String, text );
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "歌房密码破解出错:" + e.getMessage() );
        }
    }

    public void proceedOnClick() {
        try {
            start = true;
            b = true;
            i = config.getPassword( "password" );
            String text = String.format( "%04d", i );
            String KTVIN_send_Class_String = Constant.adapter.getString( "KTV_IN_send_Class" );
            String KTVIN_send_Method_String = Constant.adapter.getString( "KTV_IN_send_Method" );
            Class<?> h$26 = XposedHelpers.findClass( KTVIN_send_Class_String, classLoader );
            h$26Object = XposedHelpers.newInstance( h$26, hObject );
            XposedHelpers.callMethod( h$26Object, KTVIN_send_Method_String, text );
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "歌房密码破解出错:" + e.getMessage() );
        }
    }

    private void send(int type, int uid, String text) {
        LogUtil.d( "karorkefz", "Ktv_send" );
        if (type == 2 && colationKtvList.contains( uid )) {
            LogUtil.d( "karorkefz", "过滤名单，存在:" + uid + "  " + text );
            return;
        }
        try {
            String Robot_send_Class_String = Constant.adapter.getString( "KTV_Robot_send_Class" );
            String Robot_send_Method_String = Constant.adapter.getString( "KTV_Robot_send_Method" );
            Class Robot_send_Class = XposedHelpers.findClass( Robot_send_Class_String, classLoader );
            Object Robot_send_Object = XposedHelpers.newInstance( Robot_send_Class );
            final ArrayList five = new ArrayList();
            five.add( Long.valueOf( uid ) );
            String six = text;
            LogUtil.d( "karorkefz", "send:" + one );
            if (one != null) {
                MyThread.Ktv_init( new Runnable() {
                    public void run() {
                        LogUtil.e( "karorkefz", "核心线程:" + six + "  " + TimeHook.SimpleDateFormat_Time() );
                        try {
                            XposedHelpers.callMethod( Robot_send_Object, Robot_send_Method_String, one, strRoomId, strShowId, 2, five, six );
                            send( six );
                        } catch (Exception e) {
                            LogUtil.w( "karorkefz", "直播间机器人发送信息出错:" + e.getMessage() );
                        }
                    }
                } );
            }
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "歌房机器人发送信息出错:" + e.getMessage() );
        }
    }

    private void send(String text) {
        Object roomUserInfo = XposedHelpers.newInstance( XposedHelpers.findClass( Constant.adapter.getString( "KTV_Robot_send_myself_roomUserInfo_Class" ), classLoader ) );
        XposedHelpers.setLongField( roomUserInfo, "uid", 1000000 );
        XposedHelpers.setObjectField( roomUserInfo, "nick", "机器人消息" );
        XposedHelpers.setLongField( roomUserInfo, "lRight", 256 );
        List arrayList = new ArrayList();
        Object dVar = XposedHelpers.newInstance( XposedHelpers.findClass( Constant.adapter.getString( "KTV_Robot_send_myself_one_Class" ), classLoader ) );
        XposedHelpers.setObjectField( dVar, Constant.adapter.getString( "KTV_Robot_send_myself_OneField" ), roomUserInfo );
        XposedHelpers.setIntField( dVar, Constant.adapter.getString( "KTV_Robot_send_myself_TwoField" ), 7 );
        XposedHelpers.setObjectField( dVar, Constant.adapter.getString( "KTV_Robot_send_myself_ThreeField" ), text );
        arrayList.add( dVar );
        XposedHelpers.callMethod( WeObject, Constant.adapter.getString( "KTV_Robot_send_myself_Method" ), arrayList );
    }

    public void Song_inquiry() {
        new Thread() {
            @SuppressLint("ResourceType")
            public void run() {
                try {
                    if (++list == SongList.size()) {
                        list = 0;
                    }
                    LogUtil.d( "karorkefz", "list" + list );
                    //向服务器发送歌曲对象
                    XposedHelpers.callMethod( KtvDownloadObbDialog, Constant.adapter.getString( "KTV_Auto_Song_send_music_oneMethod" ), SongList.get( list ), 2 );
                    sleep( 1000 );
                    XposedHelpers.callMethod( KtvDownloadObbDialog, Constant.adapter.getString( "KTV_Auto_Song_send_music_twoMethod" ) );
                    XposedHelpers.callMethod( KtvDownloadObbDialog, Constant.adapter.getString( "KTV_Auto_Song_send_music_threeMethod" ) );
                    //更改本地歌曲对象，并发起点歌
                    XposedHelpers.setObjectField( Song_inquiry_locality, Constant.adapter.getString( "KTV_Auto_Song_locality_music_oneMethod" ), SongList.get( list ) );
                    XposedHelpers.callMethod( Song_inquiry, Constant.adapter.getString( "KTV_Auto_Song_locality_music_twoMethod" ) );
                    LogUtil.d( "karorkefz", "调用自动点歌" );
                } catch (Exception e) {
                    LogUtil.d( "karorkefz", "调用自动点歌出错：" + e.getMessage() );
                }
            }
        }.start();
    }
}