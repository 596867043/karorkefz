package com.ms.karorkefz.xposed;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.Keep;

import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.Base.BasicViewNotification;
import com.ms.karorkefz.util.Log.LogUtil;

import org.json.JSONException;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

@Keep
public class Karaoke_Notification_Hook extends BasicViewNotification {

    private static MediaSession.Token mTOKEN;
    Adapter adapter;

    Karaoke_Notification_Hook(ClassLoader mclassLoader) throws JSONException {
        classLoader = mclassLoader;
        this.adapter = new Adapter( "Notification" );
    }

    @Override
    public void init() {
        try {
            LogUtil.d( "karorkefz", "进入Karaoke_Notification_Hook" );
            String Notification__Class = adapter.getString( "Notification__Class" );
            String Notification_Method = adapter.getString( "Notification_Method" );
            titleID = Integer.parseInt( adapter.getString( "titleID" ), 16 );
            textID = Integer.parseInt( adapter.getString( "textID" ), 16 );
            bitmapID = Integer.parseInt( adapter.getString( "bitmapID" ), 16 );
            iconID = Integer.parseInt( adapter.getString( "iconID" ), 16 );
            String PlaySongInfo__Class = adapter.getString( "PlaySongInfo__Class" );
            Class playInfoClazz = XposedHelpers.findClass( PlaySongInfo__Class, classLoader );
            String Intent_Class = adapter.getString( "Intent_Class" );
            String preSongIntent_Field = adapter.getString( "preSongIntent_Field" );
            String playIntent_Field = adapter.getString( "playIntent_Field" );
            String nextSongIntent_Field = adapter.getString( "nextSongIntent_Field" );

            XposedHelpers.findAndHookMethod( Notification__Class, classLoader, Notification_Method, Context.class, playInfoClazz, int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod( param );
                    context = (Context) param.args[0];
                    Parcelable playSongInfo = (Parcelable) param.args[1];
                    oldNotification = (Notification) param.getResult();
                    statue = ((int) param.args[2]) == 8;
                    if (mTOKEN == null)
                        mTOKEN = new MediaSession( context, "Karaoke media button" ).getSessionToken();
                    token = mTOKEN;
                    preSongIntent = new Intent( (String) XposedHelpers.getStaticObjectField( XposedHelpers.findClass( Intent_Class, classLoader ), preSongIntent_Field ) ).putExtra( "play_current_song", playSongInfo );
                    playIntent = new Intent( (String) XposedHelpers.getStaticObjectField( XposedHelpers.findClass( Intent_Class, classLoader ), playIntent_Field ) ).putExtra( "play_current_song", playSongInfo );
                    nextSongIntent = new Intent( (String) XposedHelpers.getStaticObjectField( XposedHelpers.findClass( Intent_Class, classLoader ), nextSongIntent_Field ) ).putExtra( "play_current_song", playSongInfo );
                    contentIntent = new Intent( "com.tencent.karaoke.action.PLAYER" );
                    contentIntent.setData( Uri.parse( "qmkege://" ) )
                            .putExtra( "action", "notification_player" )
                            .putExtra( "from", "from_notification" )
                            .setClassName( context, XposedHelpers.findClass( "com.tencent.karaoke.widget.intent.IntentHandleActivity", classLoader ).getCanonicalName() )
                            .addCategory( "android.intent.category.DEFAULT" );
                    XposedBridge.log( "加载方法完毕" );
                    param.setResult( viewBuild() );

                }
            } );
        } catch (Exception e) {
            LogUtil.e( "karorkefz", "通知栏处理出错:" + e.getMessage() );
        }
    }
}
