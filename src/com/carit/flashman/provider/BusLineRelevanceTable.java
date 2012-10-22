package com.carit.flashman.provider;

import java.util.HashMap;

import android.net.Uri;
import android.provider.BaseColumns;

public class BusLineRelevanceTable implements BaseColumns{
    public static final String TABLE_NAME =      "BusLineRelevance";
    public static final int TABLE_NO =      FlashManProvider.TABLE_NO+4;
    public static final Uri CONTENT_URI = Uri.parse("content://" + FlashManProvider.AUTHORITY + "/"+TABLE_NAME);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.carit.buslinerelevance_table";
    public static final String BUSLINEID =      "BusLineId";      
    public static final String BUSSTATIONID =   "BusStationId";
    public static final String BUSSTATIONORDER = "BusStationOrder";
    public static final String CREATE_SQL = "CREATE TABLE BusLineRelevance ("
            +_ID+" INTEGER primary key,"
            +"[BusLineId] TEXT NOT NULL, "
            +"[BusStationId] INT NOT NULL," +
            "[BusStationOrder] INT NOT NULL);";
    /*CREATE TABLE [BusLineRelevance] (
    [_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    
    [BusLineId] TEXT NOT NULL,
   
    [BusStationId] TEXT NOT NULL,
   
    [BusStationOrder] INT NOT NULL)
    */
    
public static HashMap<String, String> tableProjectionMap;
    
    static{
        tableProjectionMap = new HashMap<String, String>();
        tableProjectionMap.put(_ID,_ID);
        tableProjectionMap.put(BUSLINEID,BUSLINEID);
        tableProjectionMap.put(BUSSTATIONID,BUSSTATIONID);
        tableProjectionMap.put(BUSSTATIONORDER,BUSSTATIONORDER);
    }
}
