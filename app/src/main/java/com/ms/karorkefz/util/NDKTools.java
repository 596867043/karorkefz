package com.ms.karorkefz.util;

public class NDKTools {
    static String Dir;
//    static {
////        System.loadLibrary("native-lib");
//        System.load("/data/data/com.ms.karorkefz/lib/libcJSON.so");
//        System.load("/data/data/com.ms.karorkefz/lib/libNetWork.so");
//        System.load("/data/data/com.ms.karorkefz/lib/libnative-lib.so");
//    }
    public static void NDKTools_LibraryDir(String nativeLibraryDir) {
        Dir = nativeLibraryDir;
//        System.loadLibrary("native-lib");
        System.load( Dir + "/libcJSON.so" );
        System.load( Dir + "/libNetWork.so" );
        System.load( Dir + "/libnative-lib.so" );
    }

    public static native String NetWorkFromNDK(String send_data_path, String data);

    public static native String getStringFromNDK();

    public static native String setStringFromNDK(String string);

    public static native int sumFromNDK(int a, int b);
}
