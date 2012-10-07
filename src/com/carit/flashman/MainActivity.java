
package com.carit.flashman;

import java.lang.reflect.Method;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.PoiItem;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.MyLocationOverlay;
import com.carit.flashman.FlashManService.ServiceCallBack;
import com.carit.flashman.amap.BusLineSearch;
import com.carit.flashman.amap.MyLocationOverlayProxy;
import com.carit.flashman.util.Constants;
import com.carit.flashman.util.MyLocationManager;
import com.carit.flashman.util.MyLocationManager.LocationCallBack;

public class MainActivity extends MapActivity implements OnClickListener,
        ServiceCallBack {

    private MapView mMapView;
    private MapController mMapController;
    private GeoPoint point;
    private MyLocationOverlayProxy mLocationOverlay;
    // private LocationManagerProxy locationManager = null;
    // private MyLocationManager mMyLocationManager;
    private static final String TAG = "MainActivity";
    private ProgressDialog mGetting_location_dialog;
    private FlashManService mMyService;
    private static final int LOCATION_CHANGE = 0x01;
    private static final int FIRST_LOCATION = 0x02;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mMapView = (MapView) findViewById(R.id.vmapView);
        mMapView.setVectorMap(true);
        mMapView.setBuiltInZoomControls(true);
        mMapController = mMapView.getController();
        // point = new GeoPoint((int) (22.541949 * 1E6),
        // (int) (113.989629 * 1E6)); //用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        // mMapController.setCenter(point); //设置地图中心点
        mMapController.setZoom(12);
        mLocationOverlay = new MyLocationOverlayProxy(this, mMapView);
        mMapView.getOverlays().add(mLocationOverlay);
        // 实现初次定位使定位结果居中显示
        mLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mGetting_location_dialog.dismiss();
                handler.sendMessage(Message.obtain(handler,
                        FIRST_LOCATION));
                mMapController.animateTo(mLocationOverlay.getMyLocation());
            }
        });

        findViewById(R.id.ImageButton_RouteAlert).setOnClickListener(this);
        findViewById(R.id.ImageButtonMyloc).setOnClickListener(this);
        // locationManager = LocationManagerProxy.getInstance(this);
        // MyLocationManager.init(getApplicationContext(), this);
        // mMyLocationManager = MyLocationManager.getInstance();
        // Method[] methods =PoiItem.class.getMethods();
        // for(Method method:methods){
        // Log.e(TAG,
        // method.getName()+"\t\t return:"+method.getReturnType().getName());
        // }
    }

    @Override
    protected void onPause() {
        // disableMyLocation();
        this.mLocationOverlay.disableMyLocation();
        super.onPause();
    }

    @Override
    protected void onResume() {
        this.mLocationOverlay.enableMyLocation();
        mGetting_location_dialog = ProgressDialog.show(this, "",
                getString(R.string.getting_location), true);
        mGetting_location_dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                    KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    MainActivity.this.finish();
                }
                return false;
            }
        });
        Intent i = new Intent();
        i.setClass(this, FlashManService.class);
        startService(i);
        bindService(i, mServiceConnection, BIND_AUTO_CREATE);
        super.onResume();
        // mMyLocationManager.enableMyLocation();
    }

    @Override
    protected void onDestroy() {
        // mMyLocationManager.destoryLocationManager();
        mMyService.destoryLocationManager(mLocationOverlay);
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.what){
                case FIRST_LOCATION:
                    mMapController.animateTo(mLocationOverlay.getMyLocation());
                    break;
                case LOCATION_CHANGE:
                    if(mGetting_location_dialog.isShowing())
                        mGetting_location_dialog.dismiss();
//                    Toast.makeText(getBaseContext(), msg.obj.toString(),
//                            Toast.LENGTH_SHORT).show();
                    //mMapController.animateTo(new GeoPoint(msg.arg1, msg.arg2));
                    //Log.e(TAG, msg.obj.toString());
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        
        return true;
    }
    
    

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        // TODO Auto-generated method stub
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId())//得到被点击的item的itemId
        {
        case R.id.menu_settings://这里的Id就是布局文件中定义的Id，在用R.id.XXX的方法获取出来
            break;
        case R.id.menu_offline_map:
            break;
        }
        return true;
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        // TODO Auto-generated method stub
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ImageButton_RouteAlert:
                Intent intent = new Intent(this, BusLineSearch.class);
                startActivity(intent);
                break;
            case R.id.ImageButtonMyloc:
                mMapController.animateTo(mLocationOverlay.getMyLocation());
                break;
        }

    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        // 当我bindService时，让TextView显示MyService里getSystemTime()方法的返回值
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            mMyService = ((FlashManService.MyBinder) service).getService();
            mMyService.setCallBack(MainActivity.this);
            mMyService.requestLocationUpdates(mLocationOverlay);
        }

        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onLocationChange(Location location) {
        if (location != null) {
            Double geoLat = location.getLatitude();
            Double geoLng = location.getLongitude();
            String str = ("定位成功:(" + geoLng + "," + geoLat + ") provider =" + location
                    .getProvider());
            Message msg = new Message();
            msg.obj = str;
            msg.arg1 = (int) (location.getLatitude() * 1E6);
            msg.arg2 = (int) (location.getLongitude() * 1E6);
            if (handler != null) {
                handler.sendMessage(msg);
            }
        }

    }

}
