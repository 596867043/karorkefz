package com.ms.karorkefz.xposed;

import com.google.gson.Gson;
import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.Log.LogUtil;

import org.json.JSONException;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class Karaoke_KaraScore_Hook {
    int i;
    private ClassLoader classLoader;
    Adapter adapter;

    Karaoke_KaraScore_Hook(ClassLoader mclassLoader) throws JSONException {
        classLoader = mclassLoader;
        this.adapter = new Adapter( "KaraScore" );
    }

    public void init() {
        LogUtil.d( "karorkefz", "KaraScore" );
        String KaraScore_Class_String ="com.tencent.karaoke.audiobasesdk.KaraScore";
        String getAllScore_Method_String = "getAllScore";
        String getLastScore_Method_String = "getLastScore";
        String getTotalScore_Method_String = "getTotalScore";
        try {
             KaraScore_Class_String = adapter.getString( "KaraScore_Class" );
             getAllScore_Method_String = adapter.getString( "getAllScore_Method" );
             getLastScore_Method_String = adapter.getString( "getLastScore_Method" );
             getTotalScore_Method_String = adapter.getString( "getTotalScore_Method" );
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "sss评分修改出错:" + e.getMessage() );
        }
        try {
            XposedHelpers.findAndHookMethod( KaraScore_Class_String,
                    classLoader,
                    getAllScore_Method_String,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            LogUtil.d( "karorkefz", "getAllScore: " + String.valueOf( param.getResult() ) );
                            Gson c3Gson = new Gson();
                            String c3_jsonString = c3Gson.toJson( param.getResult() );
                            LogUtil.d( "karorkefz", "getAllScore:原：  " + c3_jsonString );
                            int[] a = (int[]) param.getResult();
                            i = a.length;
                            for (int j = 0; j < i; j++) {
                                a[j] = 99;
                            }
                            i++;
                            Gson aGson = new Gson();
                            String a_jsonString = aGson.toJson( a );
                            LogUtil.d( "karorkefz", "getAllScore: 修： " + a_jsonString );
                            param.setResult( a );
                        }
                    } );
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "sss评分修改出错:" + e.getMessage() );
        }
        try {
            XposedHelpers.findAndHookMethod( KaraScore_Class_String,
                    classLoader,
                    getLastScore_Method_String,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            if (!String.valueOf( param.getResult() ).equals( "-1" )) {
                                LogUtil.d( "karorkefz", "getLastScore:" + String.valueOf( param.getResult() ) );
                                Gson c3Gson = new Gson();
                                String c3_jsonString = c3Gson.toJson( param.getResult() );
                                LogUtil.d( "karorkefz", "getLastScore: " + c3_jsonString );
                                param.setResult( 99 );
                            }
                        }
                    } );
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "sss评分修改出错:" + e.getMessage() );
        }
        try {
            XposedHelpers.findAndHookMethod( KaraScore_Class_String,
                    classLoader,
                    getTotalScore_Method_String,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            LogUtil.d( "karorkefz", "getTotalScore: " + String.valueOf( param.getResult() ) );
                            if (i != 0) {
                                int j = 99 * i;
                                param.setResult( j );
                            }
                        }
                    } );
        } catch (Exception e) {
            LogUtil.w( "karorkefz", "sss评分修改出错:" + e.getMessage() );
        }
    }
}
