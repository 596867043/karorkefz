package com.ms.karorkefz.util;

import com.ms.karorkefz.util.Log.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class ColationList {
    public static List<Integer> colationLiveList = new ArrayList<Integer>();
    public static List<Integer> colationKtvList = new ArrayList<Integer>();
    static List<Integer> managerLiveList = new ArrayList<Integer>();
    static List<Integer> managerKtvList = new ArrayList<Integer>();

    public ColationList( ) {
        colationLiveList_add( 447952980 );//凯
        colationLiveList_add( 333428993 );//拉斯维加斯
        colationLiveList_add( 776514009 );//七夜
        zong_add( 65788827 );//1640135964
        zong_add( 71190071 );//943970306
        zong_add( 330193977 );//沐雪
    }

    public static void zong_add(int uid) {
        colationLiveList_add( uid );
        colationKtvList_add( uid );
        managerLiveList_add( uid );
        managerKtvList_add( uid );
    }

    public static void colationLiveList_add(int uid) {
        if (colationLiveList.contains( uid )) {
            LogUtil.i( "karorkefz", "添加直播间过滤名单,已存在:" + uid );
            return;
        }
        colationLiveList.add( uid );
        LogUtil.i( "karorkefz", "添加直播间过滤名单成功:" + uid );
    } public static void colationKtvList_add(int uid) {
        if (colationKtvList.contains( uid )) {
            LogUtil.i( "karorkefz", "添加歌房过滤名单,已存在:" + uid );
            return;
        }
        colationKtvList.add( uid );
        LogUtil.i( "karorkefz", "添加歌房过滤名单成功:" + uid );
    }
    public static void managerLiveList_add(int uid) {
        if (managerLiveList.contains( uid )) {
            LogUtil.i( "karorkefz", "直播间管理员名单,已存在:" + uid );
            return;
        }
        managerLiveList.add( uid );
        LogUtil.i( "karorkefz", "添加直播间管理员成功:" + uid );
    }

    public static void managerKtvList_add(int uid) {
        if (managerKtvList.contains( uid )) {
            LogUtil.i( "karorkefz", "歌房管理员名单,已存在:" + uid );
            return;
        }
        managerKtvList.add( uid );
        LogUtil.i( "karorkefz", "添加歌房管理员成功:" + uid );
    }
}
