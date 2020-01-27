package com.ms.karorkefz.xposed;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

import com.ms.karorkefz.util.Config;
import com.ms.karorkefz.util.Log.LogUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class KaraokeHook {
    static Context context;

    public static void Load() {
        if (context == null) return;
        LogUtil.v( "karorkefz", "进入Karorke,加载Load." );
        Config config = new Config( context );
        if (config.isOn( "enableStart" )) {
            new Karaoke_MainTab_Hook( context ).init();
        }
        if (config.isOn( "enableKTV" )) {
            new Karaoke_Ktv_Hook( context ).init();
        }
        if (config.isOn( "enableLIVE" )) {
            new Karaoke_Live_Hook( context ).init();
        }
        if (config.isOn( "enableRecording" )) {
            new Karaoke_RecordingActivity_Hook( context ).init();
        }
        if (config.isOn( "enableOther" )) {
            new Karaoke_Other_Hook( context ).init();//其他功能
        }
    }

    public void init() {
        LogUtil.v( "karorkefz", "进入Karorke" );
        XposedHelpers.findAndHookMethod( Instrumentation.class, "callApplicationOnCreate", Application.class, new XC_MethodHook() {
            private boolean mCalled = false;
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                context = (Context) param.args[0];
                if (mCalled == false) {
                    mCalled = true;
                    context = (Context) param.args[0];
                    new Karaoke_Setting_Hook( context.getClassLoader() ).init();
                    new Karaoke_Load_Hook( context.getClassLoader() ).init();
                }
//                if (!open) {
//                    Context finalContext = context;
//                    new Thread() {
//                        public void run() {
//                            int i = 0;
//                            while (true) {
//                                try {
//                                    if (open) {
//                                        Config config = new Config( finalContext );
//                                        if (config.isOn( "enableStart" )) {
//                                            new Karaoke_MainTab_Hook( finalContext ).init();
//                                        }
//                                        if (config.isOn( "enableKTV" )) {
//                                            new Karaoke_Ktv_Hook( finalContext ).init();
//                                        }
//                                        if (config.isOn( "enableLIVE" )) {
//                                            new Karaoke_Live_Hook( finalContext ).init();
//                                        }
//                                        if (config.isOn( "enableRecording" )) {
//                                            new Karaoke_RecordingActivity_Hook( finalContext ).init();
//                                        }
//                                        if (config.isOn( "enableOther" )) {
//                                            new Karaoke_Other_Hook( finalContext ).init();//其他功能
//                                        }
//                                        break;
//                                    }
//                                    sleep( 1000 );
//                                } catch (InterruptedException e) {
//                                    LogUtil.e( "karorkefz", "异常:" + e.getMessage() );
//                                }
//                                if (i == 10) break;
//                                i++;
//                            }
//                        }
//                    }.start();
//                }
            }
        } );
//        XposedBridge.hookAllMethods( ClassLoader.class, "loadClass", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                if (param.hasThrowable()) return;
//                if (param.args.length != 1) return;
//                Class<?> cls = (Class<?>) param.getResult();
//                String name = cls.getName();
////                LogUtil.e( "karorkefz", "class:"+name );
//                if ( name.equals( "com.tencent.karaoke.module.splash.ui.SplashBaseActivity")) {
//                        LogUtil.v( "karorkefz", "Karorke-准备加载用户权限" );
//                        new Karaoke_Load_Hook( cls.getClassLoader() ).init();
//                    }
//                if (name.equals( View_Class )) {
//                    LogUtil.v( "karorkefz", "Karorke-准备加载 设置" );
//                    new Karaoke_Setting_Hook( cls.getClassLoader() ).init();
//                }
//                if(!open)return;
//                if (enableStart && name.equals( "com.tencent.karaoke.module.splash.ui.SplashBaseActivity" )) {
//                    enableStart = false;
//                    LogUtil.v( "karorkefz", "Karorke-准备加载maintab" );
//                    new Karaoke_MainTab_Hook( cls.getClassLoader() ).init();
//                }
//                if (enableKTV && name.equals( "com.tencent.karaoke.module.ktv.ui.KtvRoomActivity" )) {
//                    enableKTV = false;
//                    LogUtil.v( "karorkefz", "Karorke-准备加载单麦Ktv" );
//                    new Karaoke_Ktv_Hook( cls.getClassLoader(), config ).init();
//                }
//                if (enableLIVE && name.equals( "com.tencent.karaoke.module.live.ui.LiveActivity" )) {
//                    enableLIVE = false;
//                    LogUtil.v( "karorkefz", "Karorke-准备加载Live" );
//                    new Karaoke_Live_Hook( cls.getClassLoader(), config ).init();
//                }
//                if (enableRecording && name.equals( "com.tencent.karaoke.module.recording.ui.main.RecordingActivity" )) {
//                    enableRecording = false;
//                    LogUtil.v( "karorkefz", "Karorke-准备加载 歌曲录制" );
//                    new Karaoke_RecordingActivity_Hook( cls.getClassLoader() ).init();
//                }
//                if (enableOther) {
//                    enableOther = false;
//                    new Karaoke_Other_Hook( cls.getClassLoader(), config ).init();//其他功能
//                }
//            }
//        } );
    }
}
