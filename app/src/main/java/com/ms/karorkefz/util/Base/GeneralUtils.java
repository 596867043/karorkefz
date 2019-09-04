package com.ms.karorkefz.util.Base;

import android.app.AndroidAppHelper;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.ms.karorkefz.BuildConfig;

import de.robv.android.xposed.XposedBridge;

final public class GeneralUtils {

    public static Context getContext() {
        return AndroidAppHelper.currentApplication().getApplicationContext();
    }

    public static Context getMoudleContext(Context context) {
        Context moudleContext = null;
        try {
            moudleContext = context.createPackageContext( BuildConfig.APPLICATION_ID, Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY );
        } catch (PackageManager.NameNotFoundException e) {
            XposedBridge.log( e );
        }
        return moudleContext;
    }

    public static Context getMoudleContext() {
        return getMoudleContext( getContext() );
    }

    public static Notification buildMusicNotificationWithoutAction(Context context, int iconID, CharSequence titleString, CharSequence textString, boolean statue, RemoteViews remoteViews, PendingIntent contentIntent, String channelID, PendingIntent deleteIntent) {
        if (Build.VERSION.SDK_INT >= 26 && channelID != null) {
            Notification.Builder builder = new Notification.Builder( context, channelID )
                    .setSmallIcon( iconID )
                    .setContentTitle( titleString )
                    .setContentText( textString )
                    .setCategory( NotificationCompat.CATEGORY_STATUS )
                    .setVisibility( Notification.VISIBILITY_PUBLIC )
                    .setOngoing( statue )
                    .setCustomContentView( remoteViews )
                    .setCustomBigContentView( remoteViews )
                    .setContentIntent( contentIntent )
                    .setDeleteIntent( deleteIntent );
            return builder.build();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder( context )
                .setSmallIcon( iconID )
                .setContentTitle( titleString )
                .setContentText( textString )
                .setCategory( NotificationCompat.CATEGORY_STATUS )
                .setVisibility( NotificationCompat.VISIBILITY_PUBLIC )
                .setOngoing( statue )
                .setPriority( Notification.PRIORITY_MAX )
                .setContent( remoteViews )
                .setCustomBigContentView( remoteViews )
                .setCustomContentView( remoteViews )
                .setContentIntent( contentIntent )
                .setDeleteIntent( deleteIntent );
        return builder.build();
    }
}
