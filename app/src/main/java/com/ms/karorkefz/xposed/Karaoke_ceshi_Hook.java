package com.ms.karorkefz.xposed;

import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.Config;
import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.view.RoomPasswordDialogViewAdd;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class Karaoke_ceshi_Hook extends RoomPasswordDialogViewAdd {
    Config config;
    Adapter adapter;
    private static ClassLoader classLoader;
    static Object Song_inquiry, KtvDownloadObbDialog;
    static int list = 0, update = 0;
    static ArrayList SongList;
    Object l = null;

    Karaoke_ceshi_Hook(ClassLoader mclassLoader, Config config) throws JSONException {

        classLoader = mclassLoader;
        this.config = config;
        this.adapter = new Adapter( "Other" );

    }

    public void init() {
//        XposedHelpers.findAndHookMethod( "com.tencent.karaoke.i.x.a.y",
//                classLoader,
//                "a",
//                WeakReference.class,
//                int.class,
//                long.class,
//                List.class,
//                XposedHelpers.findClass( "com.tencent.karaoke.module.giftpanel.ui.sb",classLoader ),
//                long.class,
//                int.class,
//                String.class,
//                new XC_MethodHook() {
//                    protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
//                        LogUtil.w( "karorkefz", "y:" + String.valueOf( param.args[3] ) );
//                        Gson cGson = new Gson();
//                        String cVar2_jsonString = cGson.toJson(  param.args[3] );
//                        LogUtil.i( "karorkefz", "y:" + cVar2_jsonString );
//                        int i=1/0;
//
//                    }
//                } );
//        XposedHelpers.findAndHookMethod( "com.tencent.karaoke.module.giftpanel.ui.P",
//                classLoader,
//                "getView",
//                int.class,
//                View.class,
//                ViewGroup.class,
//                new XC_MethodHook() {
//                    protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
//                        LogUtil.w( "karorkefz", "P:" + String.valueOf( param.args[0] ) );
//                        Field GiftDataListField = XposedHelpers.findField( param.thisObject.getClass(), "c" );
//                        Object GiftDataList =  GiftDataListField.get( param.thisObject );
//                        ArrayList list = (ArrayList) GiftDataList;
//                        LogUtil.w( "karorkefz", "P:" + String.valueOf(GiftDataList) );
//                        LogUtil.w( "karorkefz", "P:" + String.valueOf(list.size()) );
//                        int i=1/0;
//
//                    }
//                } );
    }
}