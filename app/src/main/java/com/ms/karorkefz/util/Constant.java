package com.ms.karorkefz.util;

import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    public static final boolean author = true;
    public static final String url = "http://www.moshou.tech/karorkefz/";
    public static final String ip = "http://www.moshou.tech/";
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/moshou";
    public static final List Setting_list = Setting_ArrayList();
    public static boolean open = false;
    public static int code = 0;
    public static String msg = "暂未通过网络校验，请点击更新状态信息。";
    public static  Adapter adapter = null;
    public static int uid = 0;

    static ArrayList Setting_ArrayList() {
        ArrayList<Object> list = new ArrayList<>();
        list.add( new Setting( "启动页广告关闭", "启动页广告关闭", "enableStart", "Introduction" ) );

        list.add( new Setting( "歌房功能", "歌房总功能开关", "enableKTV", "Introduction/ktv.html" ) );
        list.add( new Setting( "歌房语音席点击触发", "歌房语音席点击触发", "enableKTVY", "Introduction/ktv.html" ) );
        list.add( new Setting( "歌房密码自动输入", "歌房密码自动输入", "enableKTVIN", "Introduction/ktv.html" ) );
        list.add( new Setting( "歌房滑动禁用", "歌房滑动禁用", "enableKTV_cS", "Introduction/ktv.html" ) );
        list.add( new Setting( "歌房机器人", "歌房机器人", "enableKTV_Robot", "Introduction/ktv.html" ) );
        list.add( new Setting( "歌房自动点歌", "歌房自动点歌总功能开关", "enableKTV_Auto_Song", "Introduction/ktv.html" ) );
        list.add( new Setting( "自动音频上麦", "自动点击音频上麦按钮", "enableKTV_Auto_wheat", "Introduction/ktv.html" ) );
        list.add( new Setting( "自动点歌", "自动点歌", "enableKTV_Auto_Song_inquiry", "Introduction/ktv.html" ) );
        list.add( new Setting( "自动下麦", "他人点歌，机器人自动下麦", "enableKTV_Auto_Listener_Song_List", "Introduction/ktv.html" ) );

        list.add( new Setting( "直播间功能", "直播间总功能开关", "enableLIVE", "Introduction/live.html" ) );
        list.add( new Setting( "直播间滑动禁用", "直播间滑动禁用", "enableLIVE_cS", "Introduction/live.html" ) );
        list.add( new Setting( "直播间机器人", "直播间机器人", "enableLIVE_Robot", "Introduction/live.html" ) );

        list.add( new Setting( "其他功能", "其他总功能开关", "enableOther", "Introduction/other.html" ) );
        list.add( new Setting( "通知栏样式更改", "歌曲播放通知栏样式更改", "enableOther_Notification", "Introduction/other.html" ) );
        list.add( new Setting( "礼物录屏", "礼物录屏", "enableOther_Gift_Recorder", "Introduction/other.html" ) );
        list.add( new Setting( "礼物列表更新", "礼物列表更新", "enableOther_Gift_Update", "Introduction/other.html" ) );
        list.add( new Setting( "移除粉丝", "移除粉丝", "enableFansDelete", "Introduction/other.html" ) );
        list.add( new Setting( "歌曲评分", "歌曲评分", "enableRecording", "Introduction/other.html" ) );
        return list;
    }

    public static class Setting {
        public String title;
        public String subTitle;
        public String key;
        public String url;

        Setting(String title, String subTitle, String key, String url) {
            this.title = title;
            this.subTitle = subTitle;
            this.key = key;
            this.url = url;
        }
    }
}
