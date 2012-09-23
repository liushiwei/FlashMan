
package com.carit.flashman.util;

/**
 * @author rongfzh
 * @version 1.0.0  
 */
import com.amap.mapapi.location.LocationManagerProxy;

import android.content.ContentValues;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * @author why
 */
public class MyLocationManager {
    private final String TAG = "MyLocationManager";

    private static Context mContext;

    private static final int MINTIME = 5000;

    private static final int MININSTANCE = 0;

    private static MyLocationManager instance;

    private Location lastLocation = null;

    private static LocationCallBack mCallback;

    private static LocationManagerProxy locationManager = null;

    public static void init(Context c, LocationCallBack callback) {
        mContext = c;
        mCallback = callback;
        locationManager = LocationManagerProxy.getInstance(mContext);
    }

    private MyLocationManager() {
        // Gps 定位

        Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINTIME, MININSTANCE,
                locationListener);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINTIME,
                MININSTANCE, locationListener);

//        String mocLocationProvider = LocationManager.NETWORK_PROVIDER;
//        mLocationManager.addTestProvider(mocLocationProvider, false, false, false, false, true,
//                true, true, 0, 5);
//        mLocationManager.setTestProviderEnabled(mocLocationProvider, true);
//        mLocationManager.requestLocationUpdates(mocLocationProvider, 0, 0, locationListener);
//        Log.e(TAG, "network: " + networkLocation);
        if (gpsLocation != null)
            lastLocation = gpsLocation;
        else
            lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    	
        for (String provider : locationManager.getAllProviders()) {
            Log.e(TAG, "provider :" + provider);
        }
        
        for (String provider : ((LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE)).getAllProviders()) {
            Log.e(TAG, "provider :" + provider);
        }
    }

    public static MyLocationManager getInstance() {
        if (null == instance) {
            instance = new MyLocationManager();
        }
        return instance;
    }

    private void updateLocation(Location location) {
        lastLocation = location;
        mCallback.onCurrentLocation(location);
    }

    public boolean enableMyLocation() {
        boolean result = true;
        // Criteria cri = new Criteria();
        // cri.setAccuracy(Criteria.ACCURACY_COARSE);
        // cri.setAltitudeRequired(false);
        // cri.setBearingRequired(false);
        // cri.setCostAllowed(false);
        
        // String bestProvider = locationManager.getBestProvider(cri, true);
        // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
        // 2000, 10, locationListener);
        // if(locationManager .isProviderEnabled(LocationManager.GPS_PROVIDER)){
        // Log.e(TAG, "gps provider enable");
        // }
        // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
        // 2000, 10, locationListener);
        //locationManager.requestLocationUpdates("lbs", 2000, 10, locationListener);
        return result;
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged");
            updateLocation(location);
            // saveLocation(location);
        }
    };

    public Location getMyLocation() {
        return lastLocation;
    }

    private static int ENOUGH_LONG = 1000 * 60;

    public interface LocationCallBack {
        /**
         * 当前位置
         * 
         * @param location
         */
        void onCurrentLocation(Location location);
    }

    public void destoryLocationManager() {
        Log.d(TAG, "destoryLocationManager");
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
            locationManager.destory();
        }
        locationManager = null;
    }

    // private void saveLocation(Location location){
    // ContentValues values = new ContentValues();
    // values.put(LocationTable.TIME, location.getTime());
    // values.put(LocationTable.LAT, location.getLatitude());
    // values.put(LocationTable.LNG, location.getLongitude());
    // Uri uri = mContext.getContentResolver().insert(LocationTable.CONTENT_URI,
    // values);
    // }
}
