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

import java.nio.ByteBuffer;

/**
 * 项目名称：
 * 类描述： 录屏服务类
 * 创建人：JinWei
 * 创建时间：2017/6/7
 * 修改人：
 * 修改时间：2017/6/7
 * 修改备注：
 */

public class ShotUntil {
    private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;
    private boolean running;
    private int width = 720;
    private int height = 1080;
    private int dpi;
    private String type;
    private ImageReader mImageReader;
    private MediaProjection mediaProjection;

    public ShotUntil() {
        running = false;
        mediaRecorder = new MediaRecorder();
    }
    public void setMediaProjection(MediaProjection mediaProjection) {
        this.mediaProjection = mediaProjection;
    }
    public void setConfig(int width, int height, int dpi) {
        this.width = width;
        this.height = height;
        this.dpi = dpi;
    }
    /**
     * 初始化ImageRead参数
     */
    public void initImageReader() {
        if (mImageReader == null) {
            int maxImages = 2;
            mImageReader = ImageReader.newInstance( width, height, PixelFormat.RGBA_8888, maxImages );
            createImageVirtualDisplay();
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
     * 请求完权限后马上获取有可能为null，可以通过判断is null来重复获取。
     */
    public Bitmap getBitmap() {
        initImageReader();
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
