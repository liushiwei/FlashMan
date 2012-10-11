package com.carit.flashman.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.carit.flashman.FlashManApplication;
import com.carit.flashman.provider.CityTable;
import com.carit.flashman.provider.LocationTable;
import com.map.projection.Projection;

public final class Common {
    
    private Common(){};
    
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
    
    public static void saveLocation(Location location) {
        ContentValues values = new ContentValues();
        values.put(LocationTable.TIME, location.getTime());
        values.put(LocationTable.LAT, location.getLatitude());
        values.put(LocationTable.LNG, location.getLongitude());
        Uri uri = FlashManApplication.getContext().getContentResolver()
                .insert(LocationTable.CONTENT_URI, values);
    }
    
    
    public static double[] adjustLatLng(double lat, double lng) {
        double[] adjustedLatLng = new double[2];
        
        if(FlashManApplication.lastLat==0){
            if(Projection.isInit()){
               double offset[] = Projection.getOffset(lng,lat);
               covert(adjustedLatLng,offset[0],offset[1],lat,lng);
               FlashManApplication.lastLat = (int)(lat*10);
               FlashManApplication.lastLng = (int)(lng*10);
               FlashManApplication.lastLatOffset = (int)offset[0];
               FlashManApplication.lastLngOffset = (int)offset[1];
               SharedPreferences info = FlashManApplication.getContext().getSharedPreferences("Info", 0);
               Editor editor = info.edit();
               editor.putInt("lastLatOffset", FlashManApplication.lastLatOffset);
               editor.putInt("lastLngOffset", FlashManApplication.lastLngOffset);
               editor.putInt("lastLat", FlashManApplication.lastLat);
               editor.putInt("lastLng", FlashManApplication.lastLng);
               
               editor.commit();
               
            }else{
                adjustedLatLng[0] = lat;
                adjustedLatLng[1] = lng;
            }
            
        }else{
            
            if((int)(lat*10)==FlashManApplication.lastLat&&(int)(lng*10)==FlashManApplication.lastLng)
            covert(adjustedLatLng,FlashManApplication.lastLatOffset,FlashManApplication.lastLngOffset,lat,lng);
            else{
                if(Projection.isInit()){
                    double offset[] = Projection.getOffset(lat, lng);
                    covert(adjustedLatLng,offset[0],offset[1],lat,lng);
                    FlashManApplication.lastLat = (int)(lat*10);
                    FlashManApplication.lastLng = (int)(lng*10);
                    FlashManApplication.lastLatOffset = (int)offset[0];
                    FlashManApplication.lastLngOffset = (int)offset[1];
                    SharedPreferences info = FlashManApplication.getContext().getSharedPreferences("Info", 0);
                    Editor editor = info.edit();
                    editor.putInt("lastLatOffset", FlashManApplication.lastLatOffset);
                    editor.putInt("lastLngOffset", FlashManApplication.lastLngOffset);
                    editor.putInt("lastLat", FlashManApplication.lastLat);
                    editor.putInt("lastLng", FlashManApplication.lastLng);
                    
                    editor.commit();
                 }else{
                     adjustedLatLng[0] = lat;
                     adjustedLatLng[1] = lng;
                 }
            }
        }
        return adjustedLatLng;
    }
    
    
    private static void covert(double []out,double offsetLat,double offsetLng,double lat,double lng){
        double latPixel = Math.round(Projection.latToPixel(lat, 18));
        double lngPixel = Math.round(Projection.lngToPixel(lng, 18));
        latPixel += offsetLng;
        lngPixel += offsetLat;
        out[0] = Projection.pixelToLat(latPixel, 18);
        out[1] = Projection.pixelToLng(lngPixel, 18);
    }


}
