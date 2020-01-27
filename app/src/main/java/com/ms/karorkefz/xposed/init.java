package com.ms.karorkefz.xposed;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class init implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam)  {

        Log.v( "karorkefz", "进入init" );
        if (lpparam.packageName.equals( "com.ms.karorkefz" )) {
            findAndHookMethod( "com.ms.karorkefz.xposed.HookStatue", lpparam.classLoader, "isEnabled", XC_MethodReplacement.returnConstant( true ) );
            return;
        }
        if (lpparam.packageName.equals( "com.tencent.karaoke" )) {
            Log.v( "karorkefz", "init-if包" );
            new KaraokeHook().init();
        }
    }
}
