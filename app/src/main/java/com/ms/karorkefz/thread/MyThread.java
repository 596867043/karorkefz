package com.ms.karorkefz.thread;

import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.util.TimeHook;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import de.robv.android.xposed.XposedHelpers;

public class MyThread {
    XSingleThreadPool Live_send_XSingleThreadPool = new XSingleThreadPool();
    XSingleThreadPool Ktv_send_XSingleThreadPool = new XSingleThreadPool();

    public void Live_init(Runnable run, int type) {
        LogUtil.w( "karorkefz", "MyThread线程:" + TimeHook.SimpleDateFormat_Time() );
        new Thread() {
            public void run() {
                try {
                    if (type == 1) {
                        LogUtil.w( "karorkefz", "MyThread线程:欢迎延迟 " + TimeHook.SimpleDateFormat_Time() );
                        sleep( 2000 );
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Live_send_XSingleThreadPool.add( run, 1000 );
            }
        }.start();
    }

    public void Ktv_init(Runnable run) {
        LogUtil.w( "karorkefz", "MyThread线程:" + TimeHook.SimpleDateFormat_Time() );
        new Thread() {
            public void run() {
                try {
                    sleep( 2000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Ktv_send_XSingleThreadPool.add( run, 1000 );
            }
        }.start();
    }
//    public void Live_init(final Object uObject, final WeakReference one, final String two, final String three, final int four, final ArrayList five, final String six) {
//
//        new Thread() {
//            public void run() {
//                try {
//                    sleep( 2000 );
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                Live_send( uObject, one, two, three, four, five, six );
//            }
//        }.start();
//    }
//
//    private void Live_send(final Object uObject, final WeakReference one, final String two, final String three, final int four, final ArrayList five, final String six) {
//
//        Live_send_XSingleThreadPool.add( new Runnable() {
//            public void run() {
//                LogUtil.w( "karorkefz", "Live_send线程:" + six );
//                XposedHelpers.callMethod( uObject, "a", one, two, three, four, five, six );
//            }
//        }, 13000 );
//    }

    public void Ktv_init(final Object uObject, final WeakReference one, final String two, final String three, final int four, final ArrayList five, final String six) {

        new Thread() {
            public void run() {
                try {
                    sleep( 2000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Ktv_send( uObject, one, two, three, four, five, six );
            }
        }.start();
    }

    private void Ktv_send(final Object uObject, final WeakReference one, final String two, final String three, final int four, final ArrayList five, final String six) {

        Ktv_send_XSingleThreadPool.add( new Runnable() {
            public void run() {
                LogUtil.w( "karorkefz", "Ktv_send线程:" + six );
                XposedHelpers.callMethod( uObject, "a", one, two, three, four, five, six );
            }
        }, 13000 );
    }
}
