package com.ms.karorkefz.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

public class UrlUtil {

    public final static String TAG = UrlUtil.class.getName();

    private static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo( uri, PackageManager.GET_ACTIVITIES );
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public static void openUrl(Context context, String url) {
        String[] browser = {"com.tencent.mtt", "com.UCMobile", "com.uc.browser", "com.oupeng.browser", "com.oupeng.mini.android", "com.android.browser"};

        Intent intent = null;
        for (String br : browser) {
            if (isAppInstalled( context, br )) {
                String clsName = null;
                try {
                    PackageManager pm = context.getPackageManager();
                    Intent intent1 = pm.getLaunchIntentForPackage( br );
                    ComponentName act = intent1.resolveActivity( pm );
                    clsName = act.getClassName();
                } catch (Exception e) {
                }
                if (clsName == null) {
                    break;
                }
                intent = new Intent();
                intent.setAction( "android.intent.action.VIEW" );
                Uri content_url = Uri.parse( url );
                intent.setData( content_url );
                intent.setClassName( br, clsName );
                break;
            }
        }
        if (intent == null) {
            intent = new Intent();
            intent.setAction( "android.intent.action.VIEW" );
            Uri content_url = Uri.parse( url );
            intent.setData( content_url );
        }
        try {
            context.startActivity( intent );
        } catch (Exception e) {

        }
    }

}