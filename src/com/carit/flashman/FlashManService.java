
package com.carit.flashman;


import com.carit.flashman.provider.LocationTable;
import com.carit.flashman.util.MockProvider;
import com.carit.flashman.util.MyLocationManager;
import com.carit.flashman.util.MyLocationManager.LocationCallBack;
import com.map.projection.Projection;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class FlashManService extends Service implements LocationCallBack {

    private static final String TAG = "NaviAideService";

    private MyBinder mBinder = new MyBinder();

    private ServiceCallBack mCallBack;

    private MyLocationManager mMyLocationManager;

    @Override
    public void onStart(Intent intent, int startId) {
        MyLocationManager.init(FlashManService.this.getApplicationContext(), FlashManService.this);
        mMyLocationManager = MyLocationManager.getInstance();
        new Thread() {

            @Override
            public void run() {
                Projection.init();
                Log.e(TAG, "Projection.init Over");
                //super.run();
            }

        }.start();
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mMyLocationManager.destoryLocationManager();
        super.onDestroy();
    }

    public ServiceCallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(ServiceCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public class MyBinder extends Binder {
        FlashManService getService() {
            return FlashManService.this;
        }
    }

    public interface ServiceCallBack {
        /**
         * 当前位置
         * 
         * @param location
         */
        void onLocationChange(Location location);
    }

    @Override
    public void onCurrentLocation(Location location) {
        if (Projection.isInit()) {
            location.setProvider(LocationManager.NETWORK_PROVIDER);
//            double [] latlng =Projection.adjustLatLng(location.getLatitude(), location.getLongitude());
//            location.setLatitude(latlng[0]);
//            location.setLongitude(latlng[1]);
            if (mCallBack != null)
                mCallBack.onLocationChange(location);
            
            //MockProvider.getInstance().setLocation(location);
            //saveLocation(location);
        }

    }

    private void saveLocation(Location location) {
        ContentValues values = new ContentValues();
        values.put(LocationTable.TIME, location.getTime());
        values.put(LocationTable.LAT, location.getLatitude());
        values.put(LocationTable.LNG, location.getLongitude());
        Uri uri = this.getApplicationContext().getContentResolver()
                .insert(LocationTable.CONTENT_URI, values);
    }
    
    public void requestLocationUpdates(LocationListener locationListener){
    	mMyLocationManager.requestLocationUpdates(locationListener);
//    	Location location = new Location(MockProvider.MODK_PROVIDER);
//        location.setLatitude(22.538928);
//        location.setLongitude(113.994162);
//        location.setTime(System.currentTimeMillis());
//        location.setAltitude(100);
//        MockProvider.getInstance().setLocation(location);
    }
    
    public void destoryLocationManager(LocationListener locationListener) {
    	mMyLocationManager.destoryLocationManager(locationListener);
    }

}
