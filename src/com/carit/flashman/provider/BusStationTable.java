package com.carit.flashman.provider;

import java.util.HashMap;

import android.net.Uri;
import android.provider.BaseColumns;

public class BusStationTable implements BaseColumns {
    
    public static final String TABLE_NAME = "BusStation";

    public static final int TABLE_NO = FlashManProvider.TABLE_NO + 3;


    public static final Uri CONTENT_URI = Uri.parse("content://" + FlashManProvider.AUTHORITY + "/busstation_table");

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.carit.busstation_table";

    public static final String CODE = "Code";

    public static final String LAT = "Lat";

    public static final String LNG = "Lng";

    public static final String NAME = "Name";

    public static final String SPELL = "Spell";

    public static final String STATIONNUM = "StationNum";
    
    public static final String CREATE_SQL =  "CREATE TABLE [BusStation] ("
                                              +_ID+" INTEGER primary key,"
                                              +"[Code] TEXT," 
                                              +"[Lat] INT, "
                                              +"[Lng] INT, "
                                              +"[Name] TEXT," 
                                              +"[Spell] TEXT," 
                                              +"[StationNum] INT);";
    
 public static HashMap<String, String> tableProjectionMap;
    
    static{
        tableProjectionMap = new HashMap<String, String>();
        tableProjectionMap.put(_ID,_ID);
        tableProjectionMap.put(CODE,CODE);
        tableProjectionMap.put(LAT,LAT);
        tableProjectionMap.put(LNG,LNG);
        tableProjectionMap.put(NAME,NAME);
        tableProjectionMap.put(SPELL,SPELL);
        tableProjectionMap.put(STATIONNUM,STATIONNUM);
    }
}
