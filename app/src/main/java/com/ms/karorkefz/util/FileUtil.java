package com.ms.karorkefz.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.ms.karorkefz.util.Constant.FILE_PATH;

public class FileUtil {
    //    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/moshou";

    //adapter
    // write SDCard
    public static void writeFileSdcardFile(String fileName, String writeStr) throws IOException {
        DeleteFolder();
        try {
            Log.i( "karorkefz", "调用写文件" );
            File file = new File( FILE_PATH, fileName );
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
            FileOutputStream fout = new FileOutputStream( file, true );
            byte[] bytes = writeStr.getBytes();
            fout.write( bytes );
            fout.write( "\r\n".getBytes() );// 写入一个换行
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void writeFile(String fileName, String writeStr) throws IOException {
        DeleteFolder();
        try {
            Log.i( "karorkefz", "调用写文件" );
            File file = new File( FILE_PATH, fileName );
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
            FileOutputStream fout = new FileOutputStream( file );
            byte[] bytes = writeStr.getBytes();
            fout.write( bytes );
            fout.write( "\r\n".getBytes() );// 写入一个换行
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    read SDCard
    public static String readFileSdcardFile(String fileName) throws IOException {
        DeleteFolder();
        String res = "";
        try {
            Log.i( "karorkefz", "调用读文件" );
            Log.i( "karorkefz", FILE_PATH );
            FileInputStream fin = new FileInputStream( FILE_PATH + fileName );
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read( buffer );
            res = new String( buffer, "UTF-8" );
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    public static Boolean DeleteFile(String fileName) {
        File file = new File( FILE_PATH, fileName );
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e( "karorkefz", "删除文件:" + fileName + "成功！" );
                return true;
            } else {
                Log.e( "karorkefz", "删除文件:" + fileName + "失败！" );
                return false;
            }
        } else {
            Log.e( "karorkefz", "删除文件:" + fileName + "不存在！" );
            return false;
        }
    }

    //集体删除原来旧文件夹
    public static Boolean deletefile(String fileName) {
        File file = new File(  fileName );
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Log.e( "karorkefz", "删除文件:" + fileName + "成功！" );
                return true;
            } else {
                Log.e( "karorkefz", "删除文件:" + fileName + "失败！" );
                return false;
            }
        } else {
            Log.e( "karorkefz", "删除文件:" + fileName + "不存在！" );
            return false;
        }
    }

    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith( File.separator )) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File( filePath );
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deletefile( files[i].getAbsolutePath() );
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory( files[i].getAbsolutePath() );
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    public static boolean DeleteFolder() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/moshou";
        File file = new File( filePath );
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deletefile( filePath );
            } else {
                return deleteDirectory( filePath );
            }
        }
    }
}

