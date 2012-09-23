package com.carit.flashman.provider;

import java.util.HashMap;

import android.net.Uri;
import android.provider.BaseColumns;

public class BusLineTable implements BaseColumns {
    public static final String TABLE_NAME = "BusLine";

    public static final int TABLE_NO = FlashManProvider.TABLE_NO + 2;

    public static final Uri CONTENT_URI = Uri.parse("content://" + FlashManProvider.AUTHORITY + "/busline_table");

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.carit.busline_table";

    public static final String DEFAULT_SORT_ORDER = _ID+" DESC";

    public static final String AIR = "Air";

    public static final String AUTO = "Auto";

    public static final String BASICPRICE = "BasicPrice";

    public static final String COMMUNICATIONTICKET = "CommunicationTicket";

    public static final String COMPANY = "Company";

    public static final String DATASOURCE = "DataSource";

    public static final String DESCRIPTION = "Description";

    public static final String ENDTIME = "EndTime";

    public static final String FRONTNAME = "FrontName";

    public static final String FRONTSPELL = "FrontSpell";

    public static final String KEYNAME = "KeyName";

    public static final String LENGTH = "Length";

    public static final String LINEID = "LineId";

    public static final String NAME = "Name";

    public static final String SPEED = "Speed";

    public static final String STARTTIME = "StartTime";

    public static final String STATIONS = "Stations";

    public static final String STATUS = "Status";

    public static final String TERMINALNAME = "TerminalName";

    public static final String TERMINALSPELL = "TerminalSpell";

    public static final String TOTALPRICE = "TotalPrice";

    public static final String TYPE = "Type";

    public static final String ISDOUBLEDECK = "IsDoubleDeck";

    public static final String ISEXPRESSWAY = "IsExpressWay";

    public static final String ISICCARD = "IsIcCard";

    public static final String ISLOOP = "IsLoop";
    
    public static final String CREATE_SQL= "CREATE TABLE [BusLine] ("
                                              +_ID+" INTEGER primary key,"
                                              +"[Air] BOOLEAN," 
                                              +"[Auto] BOOLEAN, "
                                              +"[BasicPrice] FLOAT," 
                                              +"[CommunicationTicket] BOOLEAN," 
                                              +"[Company] TEXT, "
                                              +"[DataSource] INT, "
                                              +"[Description] TEXT, "
                                              +"[EndTime] TEXT, "
                                              +"[FrontName] TEXT, "
                                              +"[FrontSpell] TEXT, "
                                              +"[KeyName] TEXT, "
                                              +"[Length] FLOAT, "
                                              +"[LineId] TEXT, "
                                              +"[Name] TEXT, "
                                              +"[Speed] FLOAT, "
                                              +"[StartTime] TEXT," 
                                              +"[Status] INT, "
                                              +"[TerminalName] TEXT, "
                                              +"[TerminalSpell] TEXT, "
                                              +"[TotalPrice] FLOAT, "
                                              +"[Type] INT, "
                                              +"[IsDoubleDeck] BOOLEAN, "
                                              +"[IsExpressWay] BOOLEAN, "
                                              +"[IsIcCard] BOOLEAN, "
                                              +"[IsLoop] BOOLEAN);";
    
    public static HashMap<String, String> tableProjectionMap;
    
    static{
        tableProjectionMap = new HashMap<String, String>();
        tableProjectionMap.put(_ID,_ID);
        tableProjectionMap.put(AIR ,AIR);                                    
        tableProjectionMap.put(AUTO ,AUTO);                              
        tableProjectionMap.put(BASICPRICE ,BASICPRICE);                  
        tableProjectionMap.put(COMMUNICATIONTICKET ,COMMUNICATIONTICKET);
        tableProjectionMap.put(COMPANY ,COMPANY);                        
        tableProjectionMap.put(DATASOURCE ,DATASOURCE);                  
        tableProjectionMap.put(DESCRIPTION ,DESCRIPTION);                
        tableProjectionMap.put(ENDTIME ,ENDTIME);                        
        tableProjectionMap.put(FRONTNAME ,FRONTNAME);                    
        tableProjectionMap.put(FRONTSPELL ,FRONTSPELL);                  
        tableProjectionMap.put(KEYNAME ,KEYNAME);                        
        tableProjectionMap.put(LENGTH ,LENGTH);                          
        tableProjectionMap.put(LINEID ,LINEID);                          
        tableProjectionMap.put(NAME ,NAME);                              
        tableProjectionMap.put(SPEED ,SPEED);                            
        tableProjectionMap.put(STARTTIME ,STARTTIME);                    
        tableProjectionMap.put(STATIONS ,STATIONS);                      
        tableProjectionMap.put(STATUS ,STATUS);                          
        tableProjectionMap.put(TERMINALNAME ,TERMINALNAME);              
        tableProjectionMap.put(TERMINALSPELL ,TERMINALSPELL);            
        tableProjectionMap.put(TOTALPRICE ,TOTALPRICE);                  
        tableProjectionMap.put(TYPE ,TYPE);                              
        tableProjectionMap.put(ISDOUBLEDECK ,ISDOUBLEDECK);              
        tableProjectionMap.put(ISEXPRESSWAY ,ISEXPRESSWAY);              
        tableProjectionMap.put(ISICCARD ,ISICCARD);                     
        tableProjectionMap.put(ISLOOP ,ISLOOP);  
    }

}
