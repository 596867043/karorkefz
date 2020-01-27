package com.ms.karorkefz.xposed;

import android.content.Context;

import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.Constant;
import com.ms.karorkefz.util.Log.LogUtil;

import org.json.JSONException;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class Karaoke_MainTab_Hook {
    private ClassLoader classLoader;
    private boolean b = true;
    Adapter adapter;
    Karaoke_MainTab_Hook(Context context) {
        classLoader = context.getClassLoader();
        this.adapter = new Adapter( "MainTab" );
    }

    public void init() {
        LogUtil.d( "karorkefz", "进入maintab" );
        try {
            //停止开始页面
            String Close_Class_String = adapter.getString( "Close_Class" );
            String Close_Method_String = adapter.getString( "Close_Method" );
            Class NewSplashAdView = XposedHelpers.findClass( Close_Class_String, classLoader );
            XposedBridge.hookAllMethods( NewSplashAdView,
                    Close_Method_String,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            LogUtil.d( "karorkefz", "maintab-find" );
                            try {
                                if (b) {
                                    b = false;
                                    XposedHelpers.callMethod( param.thisObject, "a", false );
                                }
                            } catch (Exception e) {
                            }
                        }
                    } );
        } catch (Exception e) {
            LogUtil.e( "karorkefz", "停止开始界面出错:" + e.getMessage() );
        }
    }
}
