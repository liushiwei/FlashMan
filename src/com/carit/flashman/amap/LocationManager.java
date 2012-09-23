package com.carit.flashman.amap;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.amap.mapapi.location.LocationManagerProxy;
import com.carit.flashman.R;
/**
 * 获取定位信息。
 */
public class LocationManager extends Activity implements LocationListener {

    private LocationManagerProxy locationManager = null;
    private TextView myLocation;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            myLocation.setText((String) msg.obj);
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        myLocation = (TextView) findViewById(R.id.myLocation);
        locationManager = LocationManagerProxy.getInstance(this);
    }

    public boolean enableMyLocation() {
        boolean result = true;
        Criteria cri = new Criteria();
        cri.setAccuracy(Criteria.ACCURACY_COARSE);
        cri.setAltitudeRequired(false);
        cri.setBearingRequired(false);
        cri.setCostAllowed(false);
        String bestProvider = locationManager.getBestProvider(cri, true);
        locationManager.requestLocationUpdates(bestProvider, 2000, 10, this);
        return result;
    }

    public void disableMyLocation() {
        locationManager.removeUpdates(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        enableMyLocation();
    }

    @Override
    protected void onPause() {
        disableMyLocation();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager.destory();
        }
        locationManager = null;
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        if (location != null) {
            Double geoLat = location.getLatitude();
            Double geoLng = location.getLongitude();
            String str = ("定位成功:(" + geoLng + "," + geoLat + ")");
            Message msg = new Message();
            msg.obj = str;
            if (handler != null) {
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}
