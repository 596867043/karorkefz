package com.ms.karorkefz.util;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;

import com.ms.karorkefz.util.Log.LogUtil;

import java.util.ArrayList;

public class ViewUtil {
    public static Drawable genBackgroundDefaultDrawable() {
        return genBackgroundDefaultDrawable( Color.TRANSPARENT );
    }

    public static Drawable genBackgroundDefaultDrawable(int defaultColor) {
        StateListDrawable statesDrawable = new StateListDrawable();
        statesDrawable.addState( new int[]{android.R.attr.state_pressed}, new ColorDrawable( 0xFFE5E5E5 ) );
        statesDrawable.addState( new int[]{}, new ColorDrawable( defaultColor ) );
        return statesDrawable;
    }
    @SuppressLint("ResourceType")
    public static ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));
            if (child.getId()==0x962464){
                LogUtil.i( "karorkefz", "移除子视图:" +child.getContentDescription());
                ((ViewGroup) v).removeView( child );
                return null;
            }
//            LogUtil.i( "karorkefz", "view:" + child.getContentDescription() );
            result.addAll(viewArrayList);
        }
        return result;
    }
}
