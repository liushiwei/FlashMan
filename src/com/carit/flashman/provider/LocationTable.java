package com.carit.flashman.provider;

import java.util.HashMap;

import android.net.Uri;
import android.provider.BaseColumns;

public class LocationTable implements BaseColumns{
    public static final String TABLE_NAME =      "Location";
    public static final int TABLE_NO =      FlashManProvider.TABLE_NO+1;
    public static final Uri CONTENT_URI = Uri.parse("content://" + FlashManProvider.AUTHORITY + "/location_table");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.carit.location_table";
    public static final String DEFAULT_SORT_ORDER = "time ASC";
    public static final String TIME =      "time";      
    public static final String PROVIDER =   "provider";    
    public static final String LAT =       "lat";
    public static final String LNG =       "lng";       
    public static final String ALTITUDE =  "altitude";  
    public static final String SPEED = "speed"; 
    public static final String BEARING =  "bearing";      
    public static final String ACCURACY  =  "accuracy";  
    
    public static final String CREATE_SQL = "CREATE TABLE location_table ("
            +_ID+" INTEGER primary key,"
            +"time INTEGER,"
            +"provider TEXT,"
            +"lat TEXT,"
            +"lng TEXT," 
            +"altitude TEXT," 
            +"speed TEXT," 
            +"bearing TEXT," 
            +"accuracy TEXT" 
            +");";
    
    public static HashMap<String, String> tableProjectionMap;
    
    static{
        tableProjectionMap = new HashMap<String, String>();
        tableProjectionMap.put(_ID,_ID);
        tableProjectionMap.put(LocationTable.ACCURACY, LocationTable.ACCURACY);
        tableProjectionMap.put(LocationTable.TIME, LocationTable.TIME);
        tableProjectionMap.put(LocationTable.ALTITUDE, LocationTable.ALTITUDE);
        tableProjectionMap.put(LocationTable.BEARING, LocationTable.BEARING);
        tableProjectionMap.put(LocationTable.LAT, LocationTable.LAT);
        tableProjectionMap.put(LocationTable.LNG, LocationTable.LNG);
        tableProjectionMap.put(LocationTable.PROVIDER, LocationTable.PROVIDER);
        tableProjectionMap.put(LocationTable.SPEED, LocationTable.SPEED);
    }

}
