package com.ms.karorkefz.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.ms.karorkefz.util.Constant.FILE_PATH;

/**
 * 项目名称：
 * 类描述： 录屏服务类
 * 创建人：JinWei
 * 创建时间：2017/6/7
 * 修改人：
 * 修改时间：2017/6/7
 * 修改备注：
 */

public class ScreenUntil {
    private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;
    private boolean running;
    private int width = 720;
    private int height = 1080;
    private int dpi,sleep;
    private String type;
    private ImageReader mImageReader;
    private MediaProjection mediaProjection;

    public ScreenUntil() {
        running = false;
        mediaRecorder = new MediaRecorder();
    }

    public void setMediaProjection(MediaProjection mediaProjection) {
        this.mediaProjection = mediaProjection;
    }

    public boolean isRunning() {
        return running;
    }

    public void setConfig(int width, int height, int dpi,int sleep, String type) {
        this.width = width;
        this.height = height;
        this.dpi = dpi;
        this.sleep=sleep;
        this.type = type;

    }

    public void start() {
        if (type==null) {
            return;
        } else if (type.equals( "recording" )) {
            type = null;
            new Thread() {
                public void run() {
                    startRecord();
                }
            }.start();
        }
    }

    /**
     * 开始录屏
     *
     * @return true
     */
    private boolean startRecord() {
        if (mediaProjection == null || running) {
            return false;
        }
        try {
            initRecorder();
            createVirtualDisplay();
            mediaRecorder.start();
        } catch (Exception e) {
            return false;
        }
        running = true;
        new Thread() {
            public void run() {
                try {
                    sleep( sleep );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    stopRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return true;
    }


    /**
     * 结束录屏
     *
     * @return true
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean stopRecord() {
        if (!running) {
            return false;
        }
        mediaRecorder.stop();
        mediaRecorder.reset();
        virtualDisplay.release();
        mediaProjection.stop();
        running = false;
        return true;
    }


    /**
     * 初始化ImageRead参数
     */
    private void initImageReader() {
        if (mImageReader == null) {
            int maxImages = 2;
            mImageReader = ImageReader.newInstance( width, height, PixelFormat.RGBA_8888, maxImages );
            createImageVirtualDisplay();
        }
    }


    /**
     * 创建一个录屏 Virtual
     */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createVirtualDisplay() {
        try {

            virtualDisplay = mediaProjection
                    .createVirtualDisplay( "mediaprojection", width, height, dpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder
                            .getSurface(), null, null );
        } catch (Exception e) {
            Log.d( "karorkefz", "录屏函数：录屏已创建" );
            return;
        }
    }


    /**
     * 创建一个ImageReader Virtual
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createImageVirtualDisplay() {
        virtualDisplay = mediaProjection
                .createVirtualDisplay( "mediaprojection", width, height, dpi,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader
                                .getSurface(), null, null );
    }


    /**
     * 初始化保存屏幕录像的参数
     */
    private void initRecorder() {
//        mediaRecorder.setAudioSource( MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource( MediaRecorder.VideoSource.SURFACE );
        mediaRecorder.setOutputFormat( MediaRecorder.OutputFormat.THREE_GPP );
        mediaRecorder.setOutputFile(
                getSavePath() + TimeHook.SimpleDateFormat_Time() + ".mp4" );
        mediaRecorder.setVideoSize( width, height );
        mediaRecorder.setVideoEncoder( MediaRecorder.VideoEncoder.H264 );
//        mediaRecorder.setAudioEncoder( MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoEncodingBitRate( 5 * 1024 * 1024 );
        mediaRecorder.setVideoFrameRate( 30 );
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取一个保存屏幕录像的路径
     *
     * @return path
     */
    public String getSavePath() {
        if (Environment.getExternalStorageState()
                .equals( Environment.MEDIA_MOUNTED )) {
            String rootDir = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + FILE_PATH+"/ScreenRecord/";
            Log.e( "karorkefz", "rootDir:" + rootDir );
            File file = new File( rootDir );
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }
            return rootDir;
        } else {
            return null;
        }
    }


    /**
     * 请求完权限后马上获取有可能为null，可以通过判断is null来重复获取。
     */
    public Bitmap getBitmap() {
        Bitmap bitmap = cutoutFrame();
        if (bitmap == null) {
            getBitmap();
        }
        return bitmap;
    }


    /**
     * 通过底层来获取下一帧的图像
     *
     * @return bitmap
     */
    public Bitmap cutoutFrame() {
        Image image = mImageReader.acquireLatestImage();
        if (image == null) {
            return null;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap( width +
                rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888 );
        bitmap.copyPixelsFromBuffer( buffer );
        image.close();
        return Bitmap.createBitmap( bitmap, 0, 0, width, height );
    }
}
