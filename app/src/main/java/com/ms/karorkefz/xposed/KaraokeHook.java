package com.ms.karorkefz.xposed;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.Config;
import com.ms.karorkefz.util.Log.LogUtil;

import org.json.JSONException;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import static com.ms.karorkefz.util.Constant.open;

public class KaraokeHook {
    boolean enableStart, enableFansDelete, enableKTV, enableLIVE, enableRecording, enableOther;
    Config config;
    Adapter adapter;
    String View_Class;

    public KaraokeHook(){
        try {
            adapter = new Adapter( "Setting" );
            View_Class = adapter.getString( "View_Class" );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        LogUtil.v( "karorkefz", "进入Karorke" );
        XposedHelpers.findAndHookMethod( Instrumentation.class, "callApplicationOnCreate", Application.class, new XC_MethodHook() {
            private boolean mCalled = false;

            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (mCalled == false) {
                    mCalled = true;
                    Context context = (Context) param.args[0];
                    config = new Config( context );
                    enableStart = config.isOn( "enableStart" );
                    enableFansDelete = config.isOn( "enableFansDelete" );
                    enableKTV = config.isOn( "enableKTV" );
                    enableLIVE = config.isOn( "enableLIVE" );
                    enableRecording = config.isOn( "enableRecording" );
                    enableOther = config.isOn( "enableOther" );
                }
            }
        } );
        XposedBridge.hookAllMethods( ClassLoader.class, "loadClass", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.hasThrowable()) return;
                if (param.args.length != 1) return;
                Class<?> cls = (Class<?>) param.getResult();
                String name = cls.getName();
                if (name.equals( View_Class )) {
                    LogUtil.v( "karorkefz", "Karorke-准备加载 设置" );
                    new Karaoke_Setting_Hook( cls.getClassLoader() ).init();
                }
                if (enableStart && name.equals( "com.tencent.karaoke.module.splash.ui.SplashBaseActivity" )) {
                    enableStart = false;
                    LogUtil.v( "karorkefz", "Karorke-准备加载maintab" );
                    new Karaoke_MainTab_Hook( cls.getClassLoader() ).init();
                }
                if (enableFansDelete && name.equals( "com.tencent.karaoke.module.user.ui.FollowFansActivity" )) {
                    enableFansDelete = false;
                    LogUtil.v( "karorkefz", "Karorke-准备加载 user" );
                    new Karaoke_User_Hook( cls.getClassLoader() ).init();
                }
                if (enableKTV && name.equals( "com.tencent.karaoke.module.ktv.ui.KtvRoomActivity" )) {
                    enableKTV = false;
                    LogUtil.v( "karorkefz", "Karorke-准备加载单麦Ktv" );
                    new Karaoke_Ktv_Hook( cls.getClassLoader(), config ).init();
                }
                if (enableLIVE && name.equals( "com.tencent.karaoke.module.live.ui.LiveActivity" )) {
                    enableLIVE = false;
                    LogUtil.v( "karorkefz", "Karorke-准备加载Live" );
                    new Karaoke_Live_Hook( cls.getClassLoader(), config ).init();
                }
                if (enableRecording && name.equals( "com.tencent.karaoke.module.recording.ui.main.RecordingActivity" )) {
                    enableRecording = false;
                    LogUtil.v( "karorkefz", "Karorke-准备加载 歌曲录制" );
                    new Karaoke_RecordingActivity_Hook( cls.getClassLoader() ).init();
                }
                if (enableOther && name.equals( "com.tencent.karaoke.module.splash.ui.SplashBaseActivity" )) {
                    enableOther = false;
                    new Karaoke_Other_Hook( cls.getClassLoader(), config ).init();//其他功能
                    new Karaoke_ceshi_Hook( cls.getClassLoader(), config ).init();//其他功能
                }
            }
        } );
    }
}
