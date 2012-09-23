package com.carit.flashman.provider;

import java.util.HashMap;

import android.net.Uri;
import android.provider.BaseColumns;

public class PoiTable implements BaseColumns {
    public static final String TABLE_NAME = "Poi";

    public static final int TABLE_NO = FlashManProvider.TABLE_NO + 5;

    public static final Uri CONTENT_URI = Uri.parse("content://" + FlashManProvider.AUTHORITY + "/poi_table");

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.carit.poi_table";

    public static final String ADCODE = "AdCode";
    
    public static final String POIID = "PoiId";
    
    public static final String LAT = "Lat";
    
    public static final String LNG = "Lng";
            
    public static final String SNIPPET = "Snippet";
    
    public static final String TYPECODE = "TypeCode";
    
    public static final String TYPEDES = "TypeDes";
    
    public static final String XMLNODE = "XmlNode";
    
    public static final String CREATE_SQL = "CREATE TABLE [Poi] ( "
                                +_ID+" INTEGER primary key,"
                                +"[AdCode] TEXT," 
                                +"[PoiId] TEXT, "
                                +"[Lat] INT, "
                                +"[Lng] INT, "
                                +"[Snippet] TEXT, "
                                +"[Tel] TEXT, "
                                +"[Title] TEXT," 
                                +"[TypeCode] TEXT, "
                                +"[TypeDes] TEXT, "
                                +"[XmlNode] TEXT);";
                                        
    public static HashMap<String, String> tableProjectionMap;
    
    static{
        tableProjectionMap = new HashMap<String, String>();
        tableProjectionMap.put(_ID,_ID);
        tableProjectionMap.put(ADCODE,ADCODE);
        tableProjectionMap.put(POIID,POIID);
        tableProjectionMap.put(LAT,LAT);
        tableProjectionMap.put(LNG,LNG);
        tableProjectionMap.put(SNIPPET,SNIPPET);
        tableProjectionMap.put(TYPECODE,TYPECODE);
        tableProjectionMap.put(TYPEDES,TYPEDES);
        tableProjectionMap.put(XMLNODE,XMLNODE);
    }
}
