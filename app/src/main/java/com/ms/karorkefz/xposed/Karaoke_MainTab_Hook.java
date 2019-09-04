package com.ms.karorkefz.xposed;

import com.ms.karorkefz.util.ColationList;
import com.ms.karorkefz.util.Constant;
import com.ms.karorkefz.util.Log.LogUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class Karaoke_MainTab_Hook {
    private ClassLoader classLoader;
    private boolean b = true;

    Karaoke_MainTab_Hook(ClassLoader mclassLoader) {

        classLoader = mclassLoader;
    }

    public void init() throws InstantiationException, IllegalAccessException {
        LogUtil.d( "karorkefz", "进入maintab" );
        try {
            Class KaraokeContext = XposedHelpers.findClass( "com.tencent.karaoke.common.KaraokeContext", classLoader );
            Object KaraokeContextObject = KaraokeContext.newInstance();

            Object getLoginManager = XposedHelpers.callMethod( KaraokeContextObject, "getLoginManager" );
            long CurrentUserInfo = (long) XposedHelpers.callMethod( getLoginManager, "c" );
            Constant.uid = Integer.parseInt( String.valueOf( CurrentUserInfo ) );
            LogUtil.e( "karorkefz", "过滤自己uid:" + CurrentUserInfo );
            ColationList.zong_add( Integer.parseInt( String.valueOf( CurrentUserInfo ) ) );
        } catch (Exception e) {
            LogUtil.e( "karorkefz", "处理自己uid出错:" + e.getMessage() );
        }
        try {
            //停止开始页面
            Class NewSplashAdView = XposedHelpers.findClass( "com.tencent.karaoke.module.splash.ui.NewSplashAdView", classLoader );
            XposedBridge.hookAllMethods( NewSplashAdView,
                    "a",
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
