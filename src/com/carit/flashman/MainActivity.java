package com.carit.flashman;

import java.lang.reflect.Method;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.PoiItem;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.MyLocationOverlay;
import com.carit.flashman.amap.BusLineSearch;
import com.carit.flashman.util.Constants;
import com.carit.flashman.util.MyLocationManager;
import com.carit.flashman.util.MyLocationManager.LocationCallBack;

public class MainActivity extends MapActivity implements OnClickListener,LocationCallBack{

    private MapView mMapView;
    private MapController mMapController;
    private GeoPoint point;
    private MyLocationOverlay mLocationOverlay;
    //private LocationManagerProxy locationManager = null;
    private MyLocationManager mMyLocationManager;
    private static final String TAG = "MainActivity"; 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mMapView = (MapView) findViewById(R.id.vmapView);
        mMapView.setVectorMap(true);
        mMapView.setBuiltInZoomControls(true);  
        mMapController = mMapView.getController();  
        point = new GeoPoint((int) (22.541949 * 1E6),
                (int) (113.989629 * 1E6));  //用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        mMapController.setCenter(point);  //设置地图中心点
        mMapController.setZoom(12);   
        mLocationOverlay = new MyLocationOverlay(this, mMapView);
        mMapView.getOverlays().add(mLocationOverlay);
        //实现初次定位使定位结果居中显示
        mLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                handler.sendMessage(Message.obtain(handler, Constants.FIRST_LOCATION));
            }
        });
        
        
        findViewById(R.id.ImageButton_RouteAlert).setOnClickListener(this);
        //locationManager = LocationManagerProxy.getInstance(this);
        MyLocationManager.init(getApplicationContext(), this);
        mMyLocationManager = MyLocationManager.getInstance();
        Method[] methods =PoiItem.class.getMethods();
        for(Method method:methods){
            Log.e(TAG, method.getName()+"\t\t return:"+method.getReturnType().getName());
        }
    }
    
    @Override
    protected void onPause() {
        //disableMyLocation();
        this.mLocationOverlay.disableMyLocation();
        super.onPause();
    }

    @Override
    protected void onResume() {
        this.mLocationOverlay.enableMyLocation();
        super.onResume();
        //mMyLocationManager.enableMyLocation();
    }

    @Override
    protected void onDestroy() {
    	mMyLocationManager.destoryLocationManager();
        super.onDestroy();
    }
    
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == Constants.FIRST_LOCATION) {
                mMapController.animateTo(mLocationOverlay.getMyLocation());
            }else{
                Toast.makeText(getBaseContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
                mMapController.animateTo(new GeoPoint(msg.arg1,msg.arg2));
                Log.e(TAG, msg.obj.toString());
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ImageButton_RouteAlert:
                Intent intent = new Intent(this,BusLineSearch.class);
                startActivity(intent);
                break;
        }
        
    }

   
    @Override
    public void onCurrentLocation(Location location) {
        if (location != null) {
            Double geoLat = location.getLatitude();
            Double geoLng = location.getLongitude();
            String str = ("定位成功:(" + geoLng + "," + geoLat + ") provider ="+location.getProvider());
            Message msg = new Message();
            msg.obj = str;
            msg.arg1 = (int)(location.getLatitude()*1E6);
            msg.arg2 = (int)(location.getLongitude()*1E6);
            if (handler != null) {
                handler.sendMessage(msg);
            }
        }
        
    }
}
