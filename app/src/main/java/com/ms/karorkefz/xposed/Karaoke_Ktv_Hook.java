package com.ms.karorkefz.xposed;

import android.annotation.SuppressLint;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import com.ms.karorkefz.util.Constant;
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
    boolean enableKTV_cS, enableKTVY, enableKTVIN, enableKTV_Robot, enableRecording, Auto_Song;
    boolean start = false;
    boolean b = false;
    Object hObject, h$26Object, WeObject;
    WeakReference one;
    String strRoomId, strShowId;
    com.ms.karorkefz.util.ColationList ColationList;
    private ClassLoader classLoader;
    private EditText inputEditText;
    private int i = 0000;
    static Object Song_inquiry, KtvDownloadObbDialog, l;
    static int list = 0, update = 0;
    static ArrayList SongList;

    Karaoke_Ktv_Hook(ClassLoader mclassLoader, Config config) throws JSONException {

        classLoader = mclassLoader;
        this.config = config;
        this.adapter = new Adapter( "Ktv" );
        enableKTV_cS = config.isOn( "enableKTV_cS" );
        enableKTVY = config.isOn( "enableKTVY" );
        enableKTVIN = config.isOn( "enableKTVIN" );
        enableKTV_Robot = config.isOn( "enableKTV_Robot" );
        enableRecording = config.isOn( "enableRecording" );
        Auto_Song = config.isOn( "enableKTV_Auto_Song" );


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
                                strRoomId = String.valueOf( param.args[1] );
                                strShowId = String.valueOf( param.args[2] );
                            }
                        } );
                String Robot_Listener_my_Class = adapter.getString( "Robot_Listener_my_Class" );
                String Robot_Listener_my_Class_one = adapter.getString( "Robot_Listener_my_Class_one" );
                XposedHelpers.findAndHookConstructor( Robot_Listener_my_Class, classLoader,
                        XposedHelpers.findClass( Robot_Listener_my_Class_one, classLoader ),
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
                String InformGetMicDialog_Class_String = adapter.getString( "InformGetMicDialog_Class" );
                String InformGetMicDialog_Class_one_String = adapter.getString( "InformGetMicDialog_one_Class" );
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
                                            String Mic_Button_String = adapter.getString( "Mic_Button" );
                                            String Mic_onClick_Method_String = adapter.getString( "Mic_onClick_Method" );
                                            sleep( 5000 );
                                            View view = new View( AndroidAppHelper.currentApplication() );
                                            view.setId( Integer.parseInt( Mic_Button_String, 16 ) );
                                            XposedHelpers.callMethod( param.thisObject, Mic_onClick_Method_String, view );
                                            LogUtil.d( "karorkefz", "InformGetMicDialog点击" );
                                            sleep( 5000 );

                                            String KaraokeContext_String = adapter.getString( "KaraokeContext" );
                                            Class KaraokeContext = XposedHelpers.findClass( KaraokeContext_String, classLoader );
                                            Object KaraokeContextObject = KaraokeContext.newInstance();

                                            String getKtvController_String = adapter.getString( "getKtvController" );
                                            Object KtvController = XposedHelpers.callMethod( KaraokeContextObject, getKtvController_String );

                                            String Original_String = adapter.getString( "Original" );
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
                String Listener_Music_List_Class_String = adapter.getString( "Listener_Music_List_Class" );
                String Listener_Music_List_Method_String = adapter.getString( "Listener_Music_List_Method" );
                XposedHelpers.findAndHookMethod( Listener_Music_List_Class_String,
                        classLoader,
                        Listener_Music_List_Method_String,
                        List.class,
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                LogUtil.i( "karorkefz", "Music_List:update:" + param.args[0] );
                                SongList = (ArrayList) param.args[0];
                            }
                        } );
                //歌曲信息发送
                String Music_Send_Class_String = adapter.getString( "Music_Send_Class" );
                String Music_Send_Class_one = adapter.getString( "Music_Send_Class_one" );
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
                String Start_Send_Class_String = adapter.getString( "Start_Send_Class" );
                String Start_Send_Class_one = adapter.getString( "Start_Send_Class_one" );
                String Start_Send_Class_two = adapter.getString( "Start_Send_Class_two" );
                XposedHelpers.findAndHookConstructor( Start_Send_Class_String,
                        classLoader,
                        XposedHelpers.findClass( Start_Send_Class_one, classLoader ),
                        XposedHelpers.findClass( Start_Send_Class_two, classLoader ),
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                LogUtil.i( "karorkefz", "zg:" + param.args[0] );
                                LogUtil.i( "karorkefz", "zg:" + param.args[1] );
                                Song_inquiry = param.thisObject;
                            }
                        } );


                //下麦监听
                String Listener_Mic_down_Class_String = adapter.getString( "Listener_Mic_down_Class" );
                String Listener_Mic_down_Method_String = adapter.getString( "Listener_Mic_down_Method" );
                String Listener_Mic_down_oneClass_String = adapter.getString( "Listener_Mic_down_oneClass" );
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
                                    String Listener_Mic_down_mapExt = adapter.getString( "Listener_Mic_down_mapExt" );
                                    String mapExt_jsonString = kVar2_JSONObject.getString( Listener_Mic_down_mapExt );
                                    JSONObject mapExt_JSONObject = new JSONObject( mapExt_jsonString );
                                    // 直接可得数据
                                    String Listener_Mic_down_reason = adapter.getString( "Listener_Mic_down_reason" );
                                    String reason = mapExt_JSONObject.getString( Listener_Mic_down_reason );

                                    // 直接可得数据
                                    String Listener_Mic_down_stActUser = adapter.getString( "Listener_Mic_down_stActUser" );
                                    String stActUser_jsonString = kVar2_JSONObject.getString( Listener_Mic_down_stActUser );
                                    JSONObject stActUser_JSONObject = new JSONObject( stActUser_jsonString );
                                    // 直接可得数据
                                    String Listener_Mic_down_uid = adapter.getString( "Listener_Mic_down_uid" );
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
                String Listener_Song_List_Class_String = adapter.getString( "Listener_Song_List_Class" );
                String Listener_Song_List_Method_String = adapter.getString( "Listener_Song_List_Method" );
                XposedHelpers.findAndHookMethod( Listener_Song_List_Class_String,
                        classLoader,
                        Listener_Song_List_Method_String,
                        ArrayList.class,
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                ArrayList list = (ArrayList) param.args[0];
                                if (list.size() > 1) {
                                    String KaraokeContext_String = adapter.getString( "KaraokeContext" );
                                    Class KaraokeContext = XposedHelpers.findClass( KaraokeContext_String, classLoader );
                                    Object KaraokeContextObject = KaraokeContext.newInstance();

                                    String getKtvController_String = adapter.getString( "getKtvController" );
                                    Object KtvController = XposedHelpers.callMethod( KaraokeContextObject, getKtvController_String );

                                    String Auto_xiamai_Method_String = adapter.getString( "Auto_xiamai" );
                                    XposedHelpers.callMethod( KtvController, Auto_xiamai_Method_String, true, false, true, true );
                                    LogUtil.d( "karorkefz", KtvController.getClass().getName() );
                                    LogUtil.d( "karorkefz", "点击下麦" );
                                }
                            }
                        } );
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

//            String KaraokeContext_Class_String = adapter.getString( "KaraokeContext_Class" );
//            String getRoomController_Method_String = adapter.getString( "getRoomController_Method" );
//            String getRoomController_data_Method_String = adapter.getString( "getRoomController_data_Method" );
//            Class KC = XposedHelpers.findClass( KaraokeContext_Class_String, classLoader );
//            Object KCObject = KC.newInstance();
//            Object getRoomController = XposedHelpers.callMethod( KCObject, getRoomController_Method_String );
//            Object b = XposedHelpers.callMethod( getRoomController, getRoomController_data_Method_String );
//            Gson bGson = new Gson();
//            String b_jsonString = bGson.toJson( b );
//            JSONObject b_JSONObject = new JSONObject( b_jsonString );
//            String strRoomId = b_JSONObject.getString( "strRoomId" );
//            String strShowId = b_JSONObject.getString( "strShowId" );
            final ArrayList five = new ArrayList();
            five.add( Long.valueOf( uid ) );
            String six = text;
            LogUtil.d( "karorkefz", "send:" + one );
            if (one != null) {
                XposedHelpers.callMethod( Robot_send_Object, Robot_send_Method_String, one, strRoomId, strShowId, 2, five, six );
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

    public void Song_inquiry() {
        new Thread() {
            @SuppressLint("ResourceType")
            public void run() {
                try {
                    //构建歌曲对象
                    Object two = XposedHelpers.callStaticMethod( XposedHelpers.findClass( "com.tencent.karaoke.module.vod.ui.ha", classLoader ), "a", SongList.get( list++ ) );
                    if (list == SongList.size()) list = 0;
                    LogUtil.d( "karorkefz", "list" + list );
                    //向服务器发送歌曲对象
                    XposedHelpers.callMethod( KtvDownloadObbDialog, "a", two, 2 );

                    sleep( 1000 );

                    XposedHelpers.callMethod( KtvDownloadObbDialog, "d" );
                    XposedHelpers.callMethod( KtvDownloadObbDialog, "g" );
                    //更改本地歌曲对象，并发起点歌
                    XposedHelpers.setObjectField( Song_inquiry, "a", two );
                    XposedHelpers.callMethod( Song_inquiry, "b" );
                    LogUtil.d( "karorkefz", "调用自动点歌" );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}