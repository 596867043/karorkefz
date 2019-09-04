package com.ms.karorkefz.util;

import com.google.gson.Gson;
import com.ms.karorkefz.util.Log.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import static com.ms.karorkefz.util.ColationList.managerKtvList;
import static com.ms.karorkefz.util.ColationList.managerLiveList;

public class ChatList {
    private static WeakReference ChatList;
    private static String enableLIVE_W_N = "欢迎", enableKTV_W_N = "欢迎";
    static ObjectCache ObjectCache = new ObjectCache();
    static boolean Live_send, Ktv_send;

    public static WeakReference Live_lists(List list) throws JSONException, IllegalAccessException, InstantiationException {
        List list2 = list;
        if (list != null) {
            if (!list.isEmpty()) {
                int size = list.size() - 1;
                for (; size >= 0; size--) {
                    Object kVar2 = list2.get( size );
                    Gson kGson = new Gson();
                    String kVar2_jsonString = kGson.toJson( kVar2 );
                    LogUtil.i( "karorkefz", "Live_list:" + kVar2_jsonString );
                    // 新建JSONObject
                    JSONObject kVar2_JSONObject = new JSONObject( kVar2_jsonString );

                    // 直接可得数据
                    String e_jsonString = kVar2_JSONObject.getString( "e" );
                    if (e_jsonString != null) {
                        // 新建JSONObject
                        JSONObject e_jsonObject = new JSONObject( e_jsonString );
                        // 直接可得数据
                        int uid = Integer.parseInt( e_jsonObject.getString( "uid" ) );
                        if (uid != 0) {
                            ObjectCache.setUid( uid );
                            ObjectCache.setType( 2 );
                            String nick = e_jsonObject.getString( "nick" );

                            int i2 = kVar2_JSONObject.getInt( "a" );
                            if (i2 == 1) {
                                String h = kVar2_JSONObject.getString( "h" );//消息内容
                                // 直接可得数据
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + nick + "  发送消息：" + h );
                                if (h.indexOf( "#" ) != -1) {
//                                    String myName = ColationList.myself.getName();
                                    int myUid = Constant.uid;
                                    if (uid == myUid || managerLiveList.contains( uid )) {
                                        if (uid == myUid) {
                                            ObjectCache.setType( 0 );
                                        } else {
                                            ObjectCache.setType( 1 );
                                        }
                                        String text = robot( h, 1 );
                                        if (text == null) {
                                            return null;
                                        }
                                        if (uid == myUid) {
                                            ObjectCache.setText( "机器人消息：" + " " + text );
                                        } else {
                                            ObjectCache.setText( "@" + nick + " " + text );
                                        }
                                        ChatList = new WeakReference( ObjectCache );
                                        return ChatList;
                                    } else {
                                        LogUtil.d( "karorkefz", "机器人权限不符合" );
                                    }
                                }
                            } else if (!Live_send) {
                                LogUtil.d( "karorkefz", "机器人已关闭" );
                                return null;
                            } else if (i2 == 2) {
                                String i_jsonString = kVar2_JSONObject.getString( "i" );
                                //新建JSONObject
                                JSONObject i_jsonObject = new JSONObject( i_jsonString );
                                //直接可得数据
                                String GiftName = i_jsonObject.getString( "GiftName" );
                                int GiftNum = i_jsonObject.getInt( "GiftNum" );
                                int GiftPrice = i_jsonObject.getInt( "GiftPrice" );
                                int RealUid = i_jsonObject.getInt( "RealUid" );
                                if (GiftPrice * GiftNum < 300) {
                                    return null;
                                }
                                if (RealUid == 0) {
                                    RealUid = uid;
                                }
                                String text = "@" + nick + " " + "谢谢送来的 “" + GiftName + "” *  " + GiftNum;
                                LogUtil.d( "karorkefz", "RealUid:" + RealUid + "  送出" + GiftName + " * " + GiftNum );
                                ObjectCache.setUid( RealUid );
                                ObjectCache.setText( text );
                                ChatList = new WeakReference( ObjectCache );
                                return ChatList;
                            } else if (i2 == 3) {
                                String h = kVar2_JSONObject.getString( "h" );
                                // 直接可得数据
                                String text = "@" + nick + " " + enableLIVE_W_N;
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + text );
                                ObjectCache.setText( text );
                                ChatList = new WeakReference( ObjectCache );
                                return ChatList;
                            } else if (i2 == 37) {
                                String w_jsonString = kVar2_JSONObject.getString( "w" );
//                                JSONArray p_jsonJSONArray = new JSONArray( p_jsonString );
//                                JSONObject p_jsonJSONObject=p_jsonJSONArray.getJSONObject( 1 );
                                JSONObject w_jsonJSONObject = new JSONObject( w_jsonString );
                                int b = w_jsonJSONObject.getInt( "b" );
                                if (b == 1) {
                                    String text = "@" + nick + " " + "感谢关注";
                                    ObjectCache.setText( text );
                                    ChatList = new WeakReference( ObjectCache );
                                    return ChatList;
                                } else if (b == 2) {
                                    String text = "@" + nick + " " + "感谢分享";
                                    ObjectCache.setText( text );
                                    ChatList = new WeakReference( ObjectCache );
                                    return ChatList;
                                } else if (b == 3) {
                                    String text = "@" + nick + " " + "感谢转发";
                                    ObjectCache.setText( text );
                                    ChatList = new WeakReference( ObjectCache );
                                    return ChatList;
                                }
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + nick );
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    public static WeakReference Ktv_lists(List list) throws JSONException {
        List list2 = list;
        if (list != null) {
            if (!list.isEmpty()) {
                int size = list.size() - 1;
                for (; size >= 0; size--) {
                    Object cVar2 = list2.get( size );
                    Gson cGson = new Gson();
                    String cVar2_jsonString = cGson.toJson( cVar2 );
                    LogUtil.i( "karorkefz", "Ktv_list:" + cVar2_jsonString );
                    // 新建JSONObject
                    JSONObject cVar2_JSONObject = new JSONObject( cVar2_jsonString );

                    // 直接可得数据
                    String e_jsonString = cVar2_JSONObject.getString( "e" );
                    if (e_jsonString != null) {
                        // 新建JSONObject
                        JSONObject e_jsonObject = new JSONObject( e_jsonString );
                        // 直接可得数据
                        int uid = Integer.parseInt( e_jsonObject.getString( "uid" ) );
                        if (uid != 0) {
                            ObjectCache.setUid( uid );
                            ObjectCache.setType( 2 );
                            String nick = e_jsonObject.getString( "nick" );

                            int i2 = cVar2_JSONObject.getInt( "a" );


                            if (i2 == 1) {
                                String h = cVar2_JSONObject.getString( "h" );
                                // 直接可得数据
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + nick + "  发送消息：" + h );
                                if (h.indexOf( "#" ) != -1) {
                                    int myUid = Constant.uid;
                                    if (uid == myUid || managerKtvList.contains( uid )) {
                                        if (uid == myUid) {
                                            ObjectCache.setType( 0 );
                                        } else {
                                            ObjectCache.setType( 1 );
                                        }
                                        String text = robot( h, 2 );
                                        if (text == null) {
                                            return null;
                                        }
                                        if (uid == myUid) {
                                            ObjectCache.setText( "机器人消息：" + " " + text );
                                        } else {
                                            ObjectCache.setText( "@" + nick + " " + text );
                                        }
                                        ChatList = new WeakReference( ObjectCache );
                                        return ChatList;
                                    } else {
                                        LogUtil.d( "karorkefz", "机器人权限不符合" );
                                    }
                                }
                            } else if (!Ktv_send) {
                                LogUtil.d( "karorkefz", "机器人已关闭" );
                                return null;
                            } else if (i2 == 3) {
                                String h = cVar2_JSONObject.getString( "h" );
                                // 直接可得数据
                                String text = "@" + nick + "  " + enableKTV_W_N;
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + nick + "  发送消息：" + h );
                                ObjectCache.setText( text );
                                ChatList = new WeakReference( ObjectCache );
                                return ChatList;
                            } else if (i2 == 29) {
                                String i_jsonString = cVar2_JSONObject.getString( "i" );
                                String g_jsonString = cVar2_JSONObject.getString( "g" );
                                //新建JSONObject
                                JSONObject i_jsonObject = new JSONObject( i_jsonString );
                                JSONObject g_jsonObject = new JSONObject( g_jsonString );
                                //直接可得数据
                                String g_nick = g_jsonObject.getString( "nick" );
                                String GiftName = i_jsonObject.getString( "GiftName" );
                                int GiftNum = i_jsonObject.getInt( "GiftNum" );
                                int GiftPrice = i_jsonObject.getInt( "GiftPrice" );
                                int RealUid = i_jsonObject.getInt( "RealUid" );
                                if (GiftPrice * GiftNum < 300) {
                                    return null;
                                }
                                if (RealUid == 0) {
                                    RealUid = uid;
                                }
                                String text = "@" + nick + "   " + "谢谢 " + nick + " 送给" + g_nick + "的 “" + GiftName + "” *  " + GiftNum;
                                LogUtil.d( "karorkefz", "RealUid:" + RealUid + "  送出" + GiftName + " * " + GiftNum );
                                ObjectCache.setText( text );
                                ChatList = new WeakReference( ObjectCache );
                                return ChatList;
                            } else if (i2 == 31) {
                                String h = cVar2_JSONObject.getString( "h" );

                                if (h.indexOf( "删除" ) != -1) {
                                    System.out.println( "存在包含关系，因为返回的值不等于-1" );
                                    return null;
                                }
                                if (h.indexOf( "置顶" ) != -1) {
                                    System.out.println( "存在包含关系，因为返回的值不等于-1" );
                                    return null;
                                }
                                // 直接可得数据
                                String text = "@" + nick + "  " + "感谢" + h;
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + nick + "  发送消息：" + h );
                                ObjectCache.setText( text );
                                ChatList = new WeakReference( ObjectCache );
                                return ChatList;
                            } else if (i2 == 37) {
                                String w_jsonString = cVar2_JSONObject.getString( "w" );
//                                JSONArray p_jsonJSONArray = new JSONArray( p_jsonString );
//                                JSONObject p_jsonJSONObject=p_jsonJSONArray.getJSONObject( 1 );
                                JSONObject w_jsonJSONObject = new JSONObject( w_jsonString );
                                int b = w_jsonJSONObject.getInt( "b" );
                                if (b == 1) {
                                    String text = "@" + nick + " " + "感谢关注";
                                    ObjectCache.setText( text );
                                    ChatList = new WeakReference( ObjectCache );
                                    return ChatList;
                                } else if (b == 2) {
                                    String text = "@" + nick + " " + "感谢分享";
                                    ObjectCache.setText( text );
                                    ChatList = new WeakReference( ObjectCache );
                                    return ChatList;
                                } else if (b == 3) {
                                    String text = "@" + nick + " " + "感谢转发";
                                    ObjectCache.setText( text );
                                    ChatList = new WeakReference( ObjectCache );
                                    return ChatList;
                                }
                                LogUtil.d( "karorkefz", "uid:" + uid + "  nick:" + nick );
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String robot(String text, int type) {
        if (type == 1) {
            if (text.indexOf( "#开启" ) != -1) {
                LogUtil.d( "karorkefz", "直播间机器人开启成功" );
                Live_send = true;
                return "直播间开启成功";
            } else if (text.indexOf( "#关闭" ) != -1) {
                LogUtil.d( "karorkefz", "直播间机器人关闭成功" );
                Live_send = false;
                return "直播间关闭成功";
            } else if (text.indexOf( "#添加过滤名单" ) != -1) {
                String a[] = text.split( ":" );
                int adduid = Integer.parseInt( a[1] );
                ColationList.colationLiveList_add( adduid );
                return "直播间过滤名单添加成功";
            } else if (text.indexOf( "#欢迎语" ) != -1) {
                String a[] = text.split( ":" );
                try {
                    enableLIVE_W_N = a[1];
                } catch (Exception e) {
                    LogUtil.d( "karorkefz", "设置欢迎语失败：格式错误" );
                    return "设置欢迎语失败：格式错误";
                }
                LogUtil.d( "karorkefz", "设置欢迎语成功：" + enableLIVE_W_N );
                return "直播间设置欢迎语成功：" + enableLIVE_W_N;
            }
        } else if (type == 2) {
            if (text.indexOf( "#开启" ) != -1) {
                LogUtil.d( "karorkefz", "歌房机器人开启成功" );
                Ktv_send = true;
                return "歌房开启成功";
            } else if (text.indexOf( "#关闭" ) != -1) {
                LogUtil.d( "karorkefz", "歌房机器人关闭成功" );
                Ktv_send = false;
                return "歌房关闭成功";
            } else if (text.indexOf( "#添加过滤名单" ) != -1) {
                String a[] = text.split( ":" );
                int adduid = Integer.parseInt( a[1] );
                ColationList.colationKtvList_add( adduid );
                return "歌房过滤名单添加成功";
            } else if (text.indexOf( "#欢迎语" ) != -1) {
                String a[] = text.split( ":" );
                try {
                    enableKTV_W_N = a[1];
                } catch (Exception e) {
                    LogUtil.d( "karorkefz", "设置欢迎语失败：格式错误" );
                    return "设置欢迎语失败：格式错误";
                }
                LogUtil.d( "karorkefz", "设置欢迎语成功：" + enableKTV_W_N );
                return "歌房设置欢迎语成功：" + enableKTV_W_N;
            }
        }
        return null;
    }

    public static class ObjectCache {
        private int type;
        private int uid;
        private String text;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUid() {
            return uid;
        }

        public String getText() {
            return text;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public void setText(String text) {
            this.text = text;
        }


    }
}

