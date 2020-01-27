package com.ms.karorkefz.xposed;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.ms.karorkefz.thread.MyThread;
import com.ms.karorkefz.util.ChatList;
import com.ms.karorkefz.util.ColationList;
import com.ms.karorkefz.util.Config;
import com.ms.karorkefz.util.Constant;
import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.util.TimeHook;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import static com.ms.karorkefz.util.ColationList.colationLiveList;


class Karaoke_Live_Hook {
    boolean enableLIVE_cS, enableLIVE_Robot, enableLIVE_Gift;
    String two, three;
    com.ms.karorkefz.thread.MyThread MyThread = new MyThread();
    com.ms.karorkefz.util.ColationList ColationList;
    private ClassLoader classLoader;
    Object LiveFragmentObject;

    Karaoke_Live_Hook(Context context) {
        classLoader = context.getClassLoader();
        Config config = new Config( context );
        enableLIVE_cS = config.isOn( "enableLIVE_cS" );
        enableLIVE_Robot = config.isOn( "enableLIVE_Robot" );
        enableLIVE_Gift = config.isOn( "enableLIVE_Gift" );
        ColationList = new ColationList();
    }

    public void init() {
        LogUtil.d( "karorkefz", "进入Live" );
        //禁止滑动
        if (enableLIVE_cS) {
            try {
                String LIVE_cS_Class = Constant.adapter.getString( "LIVE_cS_Class" );
                String LIVE_cS_Method = Constant.adapter.getString( "LIVE_cS_Method" );
                XposedHelpers.findAndHookMethod( LIVE_cS_Class,
                        classLoader,
                        LIVE_cS_Method,// 被Hook的函数
                        View.class,
                        boolean.class,
                        int.class,
                        int.class,
                        int.class,
                        new XC_MethodHook() {
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "直播间滑动：" + param.getResult() );
                                param.setResult( null );
                            }
                        } );
            } catch (Exception e) {
                LogUtil.w( "karorkefz", "直播间禁止滑动出错:" + e.getMessage() );
            }
        }
        if (enableLIVE_Robot) {
            try {
                String Robot_Function_Class = Constant.adapter.getString( "LIVE_Robot_Function_Class" );
                String Robot_Function_Method = Constant.adapter.getString( "LIVE_Robot_Function_Method" );
                String UserInfoCacheData_Class = Constant.adapter.getString( "LIVE_UserInfoCacheData_Class" );
                Class UserInfoCacheData = XposedHelpers.findClass( UserInfoCacheData_Class, classLoader );
                XposedHelpers.findAndHookMethod( Robot_Function_Class,
                        classLoader,
                        Robot_Function_Method,
                        String.class,
                        UserInfoCacheData,
                        String.class,
                        String.class,
                        String.class,
                        String.class,
                        new XC_MethodHook() {
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                two = String.valueOf( param.args[2] );
                                three = String.valueOf( param.args[5] );
                                LogUtil.d( "karorkefz", "直播间机器人LIVE_Robot_Function_Class：" + param.thisObject.getClass().toString() );
                            }
                        } );
                //监听自己发送
                String Robot_Listener_my_Class = Constant.adapter.getString( "LIVE_Robot_Listener_my_Class" );
                String Robot_Listener_my_Method = Constant.adapter.getString( "LIVE_Robot_Listener_my_Method" );
                String Robot_Listener_my_Class_one = Constant.adapter.getString( "LIVE_Robot_Listener_my_Class_one" );
                Class<?> LiveFragment = XposedHelpers.findClass( Robot_Listener_my_Class_one, classLoader );
                XposedHelpers.findAndHookMethod( Robot_Listener_my_Class,
                        classLoader,
                        Robot_Listener_my_Method,// 被Hook的函数
                        LiveFragment,
                        String.class,
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                LogUtil.d( "karorkefz", "直播间机器人LIVE_Robot_Listener_my_Class" );
                                LiveFragmentObject = param.args[0];
                            }
                        } );
                String Robot_Listener_Class = Constant.adapter.getString( "LIVE_Robot_Listener_Class" );
                String Robot_Listener_Method = Constant.adapter.getString( "LIVE_Robot_Listener_Method" );
                XposedHelpers.findAndHookMethod( Robot_Listener_Class,
                        classLoader,
                        Robot_Listener_Method,
                        List.class,
                        new XC_MethodHook() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                                List list = (List) param.args[0];
                                LogUtil.d( "karorkefz", "Live_list监听" );
                                WeakReference senObject = ChatList.Live_lists( list );
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
                LogUtil.w( "karorkefz", "直播间机器人出错:" + e.getMessage() );
            }
        }


    }

    private <LiveFragment> void send(int type, int uid, String text) {
        LogUtil.d( "karorkefz", "Live_send" );
        if (type == 2 && colationLiveList.contains( uid )) {
            LogUtil.d( "karorkefz", "过滤名单，存在:" + uid + "  " + text );
            return;
        }
        try {
            String Robot_send_Class_String = Constant.adapter.getString( "LIVE_Robot_send_Class" );
            String Robot_send_Method_String = Constant.adapter.getString( "LIVE_Robot_send_Method" );
            Class Robot_send_Class = XposedHelpers.findClass( Robot_send_Class_String, classLoader );
            Object Robot_send_Object = Robot_send_Class.newInstance();
            String LiveFragment_Class = Constant.adapter.getString( "LIVE_LiveFragment_Class" );
            Class LiveFragment = XposedHelpers.findClass( LiveFragment_Class, classLoader );
            LiveFragment LiveFragmentObeject1 = (LiveFragment) LiveFragment.newInstance();
            Object LiveFragmentObeject = LiveFragment.newInstance();
            WeakReference<Object> one = new WeakReference<>( XposedHelpers.callMethod( LiveFragmentObeject, "q", LiveFragmentObeject1 ) );
            final ArrayList five = new ArrayList();
            five.add( Long.valueOf( uid ) );
            String six = text;
            LogUtil.d( "karorkefz", "send:" + text );
            int i = 0;
            if (text.indexOf( "欢迎" ) != -1) {
                i = 1;
            }
            MyThread.Live_init( new Runnable() {
                public void run() {
                    LogUtil.e( "karorkefz", "核心线程:" + six + "  " + TimeHook.SimpleDateFormat_Time() );
                    try {
                        XposedHelpers.callMethod( Robot_send_Object, Robot_send_Method_String, one, two, three, 1, five, six );
                        send( six );
                    } catch (Exception e) {
                        LogUtil.w( "karorkefz", "直播间机器人发送信息出错:" + e.getMessage() );
                    }
                }
            }, i );
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "直播间机器人发送信息出错:" + e.getMessage() );
        }
    }

    private void send(String text) {
        try {
            if (LiveFragmentObject == null) return;
            LogUtil.d( "karorkefz", "直播间机器人自我发送信息:" + text );
            Object roomUserInfo = XposedHelpers.newInstance( XposedHelpers.findClass( Constant.adapter.getString( "Live_Robot_send_myself_roomUserInfo_Class" ), classLoader ) );
            XposedHelpers.setLongField( roomUserInfo, "uid", 1000000 );
            XposedHelpers.setObjectField( roomUserInfo, "nick", "机器人消息" );
            XposedHelpers.setLongField( roomUserInfo, "lRight", 256 );
            List arrayList = new ArrayList();

            Object dVar = XposedHelpers.newInstance( XposedHelpers.findClass( Constant.adapter.getString( "Live_Robot_send_myself_one_Class" ), classLoader ) );
            XposedHelpers.setObjectField( dVar, Constant.adapter.getString( "Live_Robot_send_myself_OneField" ), roomUserInfo );
            XposedHelpers.setIntField( dVar, Constant.adapter.getString( "Live_Robot_send_myself_TwoField" ), 7 );
            XposedHelpers.setObjectField( dVar, Constant.adapter.getString( "Live_Robot_send_myself_ThreeField" ), text );
            arrayList.add( dVar );
            XposedHelpers.callMethod( LiveFragmentObject, Constant.adapter.getString( "Live_Robot_send_myself_Method" ), arrayList );

        } catch (Exception e) {
            LogUtil.w( "karorkefz", "直播间机器人自我发送信息出错:" + e.getMessage() );
        }
    }
}