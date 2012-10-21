package com.carit.flashman.provider;

import java.util.HashMap;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritePointTable implements BaseColumns {
    public static final String TABLE_NAME = "FavoritePoint";

    public static final int TABLE_NO = FlashManProvider.TABLE_NO + 7;

    public static final Uri CONTENT_URI = Uri.parse("content://" + FlashManProvider.AUTHORITY + "/"+TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.carit.favorite_point_table";
    
    public static final String DEFAULT_SORT_ORDER = "Time ASC";

    public static final String TITLE = "Title";
    
    public static final String DESCRIBE = "Describe";
    
    public static final String LAT = "Lat";
    
    public static final String LNG = "Lng";
            
    public static final String SOURCE = "Source";
    
    public static final String TIME = "Time";
    
    public static final String CREATE_SQL = "CREATE TABLE [FavoritePoint] ( "
                                +_ID+" INTEGER primary key AUTOINCREMENT,"
                                +"[Title] TEXT," 
                                +"[Describe] TEXT, "
                                +"[Lat] TEXT, "
                                +"[Lng] TEXT, "
                                +"[Source] INT, "
                                +"[Type] INT, "
                                +"[Time] INTEGER);";
                                        
    public static HashMap<String, String> tableProjectionMap;
    
    static{
        tableProjectionMap = new HashMap<String, String>();
        tableProjectionMap.put(_ID,_ID);
        tableProjectionMap.put(TITLE,TITLE);
        tableProjectionMap.put(DESCRIBE,DESCRIBE);
        tableProjectionMap.put(LAT,LAT);
        tableProjectionMap.put(LNG,LNG);
        tableProjectionMap.put(SOURCE,SOURCE);
        tableProjectionMap.put(TIME,TIME);
    }
}
