package com.ms.karorkefz.util;

import com.ms.karorkefz.util.Log.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Adapter {
    JSONObject adapter;

    public Adapter(JSONObject adapter) {
        this.adapter = adapter;
    }

    public Adapter(String name) {
        try {
            String adapterString = FileUtil.readFileSdcardFile( "/adapter.json" );
            this.adapter =new JSONObject( adapterString ).getJSONObject( "data" ).getJSONObject( name );;
        } catch (Exception e) {
            LogUtil.i( "karorkefz", "Load适配出错:" + e.getMessage() );
        }
    }
    public String getString(String name) {
        try {
            String JsonString = this.adapter.getString( name );
            return JsonString;
        } catch (Exception e) {
            return null;
        }
    }
}
