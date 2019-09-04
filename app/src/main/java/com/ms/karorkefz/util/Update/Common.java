package com.ms.karorkefz.util.Update;

import android.app.AlertDialog;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.ms.karorkefz.util.Shot.getActivity;

public class Common {
    public static byte[] convertIsToByteArray(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            while ((length = inputStream.read( buffer )) != -1) {
                baos.write( buffer, 0, length );
            }
            inputStream.close();
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
    public static void fail(){
        getActivity().runOnUiThread( () -> {
            new AlertDialog.Builder( getActivity() )
                    .setTitle( "服务器连接失败，可能服务器已到期！" )
                    .setMessage( "作者已取消此模块的后续适配，如想使用，请自行适配。项目文件已同步到github" )
                    .setNegativeButton( "确定", null )
                    .create()
                    .show();
        } );
    }
}
