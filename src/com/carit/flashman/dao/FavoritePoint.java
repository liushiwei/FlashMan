package com.carit.flashman.dao;

public class FavoritePoint {
    
    public static final int AMAP = 0;
    
    public static final int GOOGLE = 1;
    
    public static final int BAIDU = 2;
    
    public static final int NETWORK = 3;
    
    public static final int GPS = 4;
    
    public static final int SMS = 5;
    
    public static final int LONGPRESS = 6;
    
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescribe() {
        return Describe;
    }

    public void setDescribe(String describe) {
        Describe = describe;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

    public int getSource() {
        return Source;
    }

    public void setSource(int source) {
        Source = source;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    private String Title;
    
    private String Describe;
    
    private String Lat;
    
    private String Lng;
            
    private int Source;
    
    private long Time;

}
