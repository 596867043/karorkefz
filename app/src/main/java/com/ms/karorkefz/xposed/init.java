package com.ms.karorkefz.xposed;

import android.util.Log;

import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.xposed.mysqlclient.mysqlclient;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class init implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        Log.v( "karorkefz", "进入init" );
        if (lpparam.packageName.equals( "com.ms.karorkefz" )) {
            findAndHookMethod( "com.ms.karorkefz.xposed.HookStatue", lpparam.classLoader, "isEnabled", XC_MethodReplacement.returnConstant( true ) );
            return;
        }
        if (lpparam.packageName.equals( "com.tencent.karaoke" )) {
            Log.v( "karorkefz", "init-if包" );
            Adapter adapter = new Adapter( "adapter" );
            String adapter_version = adapter.getString( "version" );

            if (adapter_version.equals( "false" )) {
            Log.v( "karorkefz", "适配文件有误" );
                return;
            } else {
                Log.v( "karorkefz", "适配通过" );
                new KaraokeHook().init();
            }
        }
        if (false&&lpparam.packageName.equals( "com.vcrox.mysqlclient" )) {
            Log.v( "karorkefz", "找到mysqlclient" );
            new mysqlclient(lpparam.classLoader).init();
        }
    }
}
