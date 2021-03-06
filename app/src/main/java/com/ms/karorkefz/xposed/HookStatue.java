package com.ms.karorkefz.xposed;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class HookStatue {
    public static Boolean isEnabled() {

        Log.d( "karorkefz", "模块xp未激活" );
        return false;
    }

    public static boolean isExpModuleActive(Context context) {

        boolean isExp = false;
        if (context == null) {
            throw new IllegalArgumentException( "context must not be null!!" );
        }

        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = Uri.parse( "content://me.weishu.exposed.CP/" );
            Bundle result = null;
            try {
                result = contentResolver.call( uri, "active", null, null );
            } catch (RuntimeException e) {
                try {
                    Intent intent = new Intent( "me.weishu.exp.ACTION_ACTIVE" );
                    intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                    context.startActivity( intent );
                } catch (Throwable e1) {
                    return false;
                }
            }
            if (result == null) {
                result = contentResolver.call( uri, "active", null, null );
            }

            if (result == null) {
                return false;
            }
            isExp = result.getBoolean( "active", false );
        } catch (Throwable ignored) {
        }
        return isExp;
    }
}
