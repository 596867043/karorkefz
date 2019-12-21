package com.ms.karorkefz.xposed;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AndroidAppHelper;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import com.google.gson.Gson;
import com.ms.karorkefz.BuildConfig;
import com.ms.karorkefz.thread.XSingleThreadPool;
import com.ms.karorkefz.util.Adapter;
import com.ms.karorkefz.util.Constant;
import com.ms.karorkefz.util.DpUtil;
import com.ms.karorkefz.util.Log.LogUtil;
import com.ms.karorkefz.util.TimeHook;
import com.ms.karorkefz.util.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.ms.karorkefz.util.Shot.getActivity;

public class Karaoke_User_Hook {
    Adapter adapter;
    boolean kViewhook = false;
    ArrayList<String> list = new ArrayList();
    XSingleThreadPool xSingleThreadPool = new XSingleThreadPool();
    private ClassLoader classLoader;

    Karaoke_User_Hook(ClassLoader mclassLoader) throws JSONException {

        classLoader = mclassLoader;
        this.adapter = new Adapter( "User" );
    }

    public void init() {
        try {
            LogUtil.d( "karorkefz", "user" );
            String CheckBox_View_Class_String = adapter.getString( "CheckBox_View_Class" );
            String CheckBox_View_Method_String = adapter.getString( "CheckBox_View_Method" );
            String CheckBox_View_oneClass_String = adapter.getString( "CheckBox_View_oneClass" );
            String CheckBox_View_twoClass_String = adapter.getString( "CheckBox_View_twoClass" );
            String CheckBox_View_fansUid_String = adapter.getString( "CheckBox_View_fansUid" );
            String CheckBox_View_Field_String = adapter.getString( "CheckBox_View_Field" );

            Class<?> CheckBox_View_oneClass = XposedHelpers.findClass( CheckBox_View_oneClass_String, classLoader );
            Class<?> CheckBox_View_twoClass = XposedHelpers.findClass( CheckBox_View_twoClass_String, classLoader );
            XposedHelpers.findAndHookMethod( CheckBox_View_Class_String, classLoader,
                    CheckBox_View_Method_String,
                    CheckBox_View_oneClass,
                    CheckBox_View_twoClass,
                    int.class,
                    new XC_MethodHook() {
                        @SuppressLint("ResourceType")
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            Gson kGson = new Gson();
                            String kVar2_jsonString = kGson.toJson( param.args[0] );
                            LogUtil.i( "karorkefz", "Live_list:" + kVar2_jsonString );
                            // 新建JSONObject
                            JSONObject kVar2_JSONObject = new JSONObject( kVar2_jsonString );

                            // 直接可得数据
                            String fansUid = kVar2_JSONObject.getString( CheckBox_View_fansUid_String );//fansUid

                            Field kField = XposedHelpers.findField( param.args[1].getClass(), CheckBox_View_Field_String );
                            View kView = (View) kField.get( param.args[1] );

                            TableRow fViewParent = (TableRow) kView.getParent();

                            Context context = AndroidAppHelper.currentApplication();

                            CheckBox selectBox;
                            selectBox = new CheckBox( context );
                            selectBox.setId( 0x962464 );
                            selectBox.setContentDescription( fansUid );
                            selectBox.setClickable( true );
                            selectBox.setFocusable( true );
                            selectBox.setFocusableInTouchMode( true );
                            selectBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    // TODO Auto-generated method stub
                                    LogUtil.e( "karorkefz", "user添加复选框状态：" + isChecked );
                                    if (isChecked) {
                                        list.add( buttonView.getContentDescription().toString() );
                                        LogUtil.e( "karorkefz", "user列表：" + list );
                                    } else {
                                        list.remove( buttonView.getContentDescription() );
                                        LogUtil.e( "karorkefz", "user列表：" + list );
                                    }
                                }
                            } );
                            ViewUtil.getAllChildren( fViewParent );
                            fViewParent.addView( selectBox );
                        }
                    }
            );
        } catch (Exception e) {
            LogUtil.e( "karorkefz", "添加复选框出错:" + e.getMessage() );
        }
        try {
            String Delete_View_Class_String = adapter.getString( "Delete_View_Class" );
            String Delete_View_Method_String = adapter.getString( "Delete_View_Method" );
            String Delete_View_Field_String = adapter.getString( "Delete_View_Field" );

            XposedHelpers.findAndHookMethod( Delete_View_Class_String,
                    classLoader,
                    Delete_View_Method_String,
                    LayoutInflater.class,
                    ViewGroup.class,
                    Bundle.class,
                    new XC_MethodHook() {
                        protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                            Field kField = XposedHelpers.findField( param.thisObject.getClass(), Delete_View_Field_String );
                            View kView = (View) kField.get( param.thisObject );
                            kViewhook = true;
                            RelativeLayout kViewParent = (RelativeLayout) kView.getParent();
                            LinearLayout Parent = (LinearLayout) kViewParent.getParent();
                            Context context = AndroidAppHelper.currentApplication();
                            FansView( Parent, context );
                        }
                    }
            );
        } catch (Exception e) {
            LogUtil.e( "karorkefz", "添加删除按钮出错:" + e.getMessage() );
        }
    }

    private void FansView(LinearLayout Parent, Context context) {
        LinearLayout settingsItemRootLLayout = new LinearLayout( context );
        settingsItemRootLLayout.setOrientation( LinearLayout.VERTICAL );
        settingsItemRootLLayout.setLayoutParams( new AbsListView.LayoutParams( MATCH_PARENT, WRAP_CONTENT ) );


        LinearLayout settingsItemLinearLayout = new LinearLayout( context );
        settingsItemLinearLayout.setOrientation( LinearLayout.VERTICAL );
        settingsItemLinearLayout.setLayoutParams( new ViewGroup.LayoutParams( MATCH_PARENT, WRAP_CONTENT ) );


        LinearLayout itemHlinearLayout = new LinearLayout( context );
        itemHlinearLayout.setOrientation( LinearLayout.HORIZONTAL );
        itemHlinearLayout.setWeightSum( 1 );

        itemHlinearLayout.setGravity( Gravity.CENTER_VERTICAL );

        Button delete = new Button( context );
        delete.setText( "移除选中粉丝" );
        delete.setTextColor( Color.rgb( 128, 128, 128 ) );
        delete.setBackgroundColor( Color.WHITE );
        delete.setGravity( Gravity.CENTER );
        delete.setPadding( 0, 0, 0, 0 );
        delete.setTextSize( 14 );
        delete.setOnClickListener( view -> {
            new AlertDialog.Builder( getActivity() )
                    .setTitle( "提示" )
                    .setMessage( "确定移除选中粉丝？\n确定移除，请刷新粉丝列表，数据可能会有延迟。" )
                    .setNegativeButton( "取消", null )
                    .setPositiveButton( "确定", (dialogInterface, i) -> {
                        try {
                            deleteOnClick();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } )
                    .create()
                    .show();

        } );


        itemHlinearLayout.addView( delete, new LinearLayout.LayoutParams( MATCH_PARENT, MATCH_PARENT, 1 ) );


        View lineView = new View( context );
        lineView.setBackgroundColor( 0xFFD5D5D5 );
        settingsItemLinearLayout.addView( itemHlinearLayout, new LinearLayout.LayoutParams( MATCH_PARENT, DpUtil.dip2px( context, 50 ) ) );
        settingsItemLinearLayout.addView( lineView, new LinearLayout.LayoutParams( MATCH_PARENT, DpUtil.dip2px( context, 6 ) ) );

        settingsItemRootLLayout.addView( settingsItemLinearLayout );
        settingsItemRootLLayout.setTag( BuildConfig.APPLICATION_ID );

        Parent.addView( settingsItemRootLLayout, 1 );
    }

    private void deleteOnClick() throws IllegalAccessException, InstantiationException, JSONException {
        for (int i = 0; i < list.size(); i++) {
            long lUid = Long.parseLong( list.get( i ) );
            send( lUid );
            LogUtil.d( "karorkefz", "列表it: lUid: " + lUid );
        }
        list.clear();
    }

    private <WebappRmFanReq> void send(long lUid) throws InstantiationException, IllegalAccessException {
        try {
            long myUid = Constant.uid;
            String send_Class_String = adapter.getString( "send_Class" );
            Class send_Class = XposedHelpers.findClass( send_Class_String, classLoader );

            String one = "relation.rmfan";
            String two = String.valueOf( myUid );

            String send_oneClass_String = adapter.getString( "send_oneClass" );
            Class send_oneClass = XposedHelpers.findClass( send_oneClass_String, classLoader );
            WebappRmFanReq webapprmfanreq = (WebappRmFanReq) send_oneClass.newInstance();
            XposedHelpers.setLongField( webapprmfanreq, "lUid", myUid );
            XposedHelpers.setLongField( webapprmfanreq, "lFanUid", lUid );
            WebappRmFanReq three = webapprmfanreq;

            WeakReference four = null;
            Object[] five = new Object[0];

            Object a = XposedHelpers.newInstance( send_Class, one, two, three, four, five );

            xSingleThreadPool.add( new Runnable() {
                public void run() {
                    LogUtil.d( "karorkefz", "线程:" + TimeHook.SimpleDateFormat_Time() );
                    XposedHelpers.callMethod( a, "j" );
                }
            }, 1000 );
        } catch (Exception e) {
            LogUtil.e( "karorkefz", "移除粉丝发送数据出错:" + e.getMessage() );
        }
    }
}