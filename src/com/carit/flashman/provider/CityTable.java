package com.carit.flashman.provider;

import java.util.HashMap;

import android.net.Uri;
import android.provider.BaseColumns;

public class CityTable implements BaseColumns{
    public static final String TABLE_NAME =      "CityTable";
    public static final int TABLE_NO =      FlashManProvider.TABLE_NO+6;
    public static final Uri CONTENT_URI = Uri.parse("content://" + FlashManProvider.AUTHORITY + "/"+TABLE_NAME);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.carit.city_table";
    //public static final String DEFAULT_SORT_ORDER = "time ASC";
    public static final String CITY =      "city";      
    public static final String PROVINCE =   "province";    
    public static final String LAT =       "lat";
    public static final String LNG =       "lng";       
    public static final String PINYIN =  "pinyin";  
    public static final String CODE = "code"; 
    
    public static final String CREATE_SQL = "CREATE TABLE CityTable ("
            +_ID+" INTEGER primary key,"
            +"city TEXT,"
            +"province TEXT,"
            +"lat TEXT,"
            +"lng TEXT," 
            +"pinyin TEXT," 
            +"code TEXT" 
            +");";
    
    public static HashMap<String, String> tableProjectionMap;
    
    static{
        tableProjectionMap = new HashMap<String, String>();
        tableProjectionMap.put(_ID,_ID);
        tableProjectionMap.put(CityTable.CITY, CityTable.CITY);
        tableProjectionMap.put(CityTable.PROVINCE, CityTable.PROVINCE);
        tableProjectionMap.put(CityTable.PINYIN, CityTable.PINYIN);
        tableProjectionMap.put(CityTable.LAT, CityTable.LAT);
        tableProjectionMap.put(CityTable.LNG, CityTable.LNG);
        tableProjectionMap.put(CityTable.CODE, CityTable.CODE);
    }
}