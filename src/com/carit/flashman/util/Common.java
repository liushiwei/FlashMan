package com.carit.flashman.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.util.Log;

import com.carit.flashman.provider.CityTable;

public class Common {
    
    private static final String TAG = "Common";
    
    public static void init(Context context,String city) {
        String cityCode=null;
        Cursor result = context.getContentResolver().query(CityTable.CONTENT_URI, new String[]{"code"}, "city like \"%"+city+"%\"", null, null);
        if(result.moveToFirst()){
            cityCode =  result.getString(result.getColumnIndex("code"));
        }
        SharedPreferences info = context.getSharedPreferences("Info", 0);
        Editor editor = info.edit();
        editor.putBoolean("Init", true);
        editor.putString("CityName", city);
        editor.putString("CityCode", cityCode);
        editor.commit();
        Log.e(TAG, "city:"+city +",cityCode:" +cityCode);
    }


}
