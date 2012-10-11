
package com.carit.flashman;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class FlashManApplication extends Application {

    public static int lastLat = 0;

    public static int lastLng = 0;

    public static int lastLatOffset = 0;

    public static int lastLngOffset = 0;
    
    private static Context mContext; 

    @Override
    public void onCreate() {
        SharedPreferences info = getSharedPreferences("Info", 0);
        lastLat = info.getInt("lastLat", 0);
        lastLng = info.getInt("lastLng", 0);
        lastLatOffset = info.getInt("lastLatOffset", 0);
        lastLngOffset = info.getInt("lastLngOffset", 0);
        mContext = getApplicationContext();  
        super.onCreate();
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        FlashManApplication.mContext = mContext;
    }

    

}
