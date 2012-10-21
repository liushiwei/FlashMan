package com.carit.flashman.amap;

import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView;
import com.carit.flashman.MainActivity;
import com.carit.flashman.R;
import com.carit.flashman.dao.FavoritePoint;
import com.carit.flashman.util.Common;

/**
 * 
 *自定义MyLocationOverlay。更换MyLocation图标。
 */
public class MyLocationOverlayProxy extends com.amap.mapapi.map.MyLocationOverlay implements OnClickListener{

	 private Context mContext;
	 private MapView mMapView;
    private View mPopView;
    private EditText mFav_title;
    private EditText mFav_describe;
	 public MyLocationOverlayProxy(Context context, MapView mapView) {
		super(context, mapView);
		mMapView = mapView;
		mContext = context;
		
		mPopView = View.inflate(mContext, R.layout.my_location_popup, null);
        mMapView.addView(mPopView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
                MapView.LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.BOTTOM_CENTER));
        mPopView.setVisibility(View.GONE);
        ImageButton end = (ImageButton) mPopView.findViewById(R.id.btn_share);
        end.setOnClickListener(this);
        end = (ImageButton) mPopView.findViewById(R.id.btn_fav);
        end.setOnClickListener(this);
        end = (ImageButton) mPopView.findViewById(R.id.btn_poi);
        end.setOnClickListener(this);
	 }
	
	 
	 
	

	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub	
	    if(location!=null){
	    Toast.makeText(mContext,"Location provider = "+location.getProvider(), Toast.LENGTH_SHORT).show();
        if (LocationManager.GPS_PROVIDER.equals(location.getProvider())) {
            double [] latlng =Common.adjustLatLng(location.getLatitude(), location.getLongitude());
            location.setLatitude(latlng[0]);
            location.setLongitude(latlng[1]);
            Common.saveLocation(location);
            super.onLocationChanged(location);
        }else if("lbs".equals(location.getProvider())){
            Common.saveLocation(location);
            super.onLocationChanged(location);
        }
	    }
	}




    @Override
    public boolean onTap(GeoPoint arg0, MapView arg1) {
        Log.e("MyLocation", "onTap x="+arg0.getLatitudeE6()+ " y = "+arg0.getLongitudeE6());
        if(getMyLocation()!=null){
        int size = 30;
        Point out=new Point();
        mMapView.getProjection().toPixels(getMyLocation(), out);    
        Rect rect = new Rect(out.x-size,out.y-size,out.x+size,out.y+size);
        mMapView.getProjection().toPixels(arg0, out);
        if(rect.contains(out.x, out.y)){
            Log.e("MyLocation", "click my location ");
            mPopView.setVisibility(View.GONE);
            MapView.LayoutParams params = (MapView.LayoutParams) mPopView.getLayoutParams();
            params.x = 0;// Y轴偏移
            params.y = -5;// Y轴偏移
            GeoPoint point = getMyLocation().e();
            params.point = point;
            //mMapCtrl.animateTo(point);
            mMapView.updateViewLayout(mPopView, params);
            mPopView.setVisibility(View.VISIBLE);
        }else{
            mPopView.setVisibility(View.GONE) ;  
        }
        }
        return super.onTap(arg0, arg1);
    }




    @Override
    protected void drawCompass(Canvas arg0, float arg1) {
        //Log.e("MyLocationOverlayProxy", "arg1 = " +arg1);
        super.drawCompass(arg0, arg1);
    }






    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_share){
            
        }else if(v.getId()==R.id.btn_fav){
            View view = View.inflate(mContext, R.layout.fav_dialog, null);
            mFav_title = (EditText) view.findViewById(R.id.fav_title);
            mFav_title.setText("my Location");
            
            mFav_title.setSelectAllOnFocus(true);
            mFav_title.requestFocus();
            mFav_describe = (EditText) view.findViewById(R.id.fav_describe);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
            .setTitle(R.string.fav_point).setView(view)
            .setIcon(R.drawable.fav)
            .setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //sendSms(mToNumber,"#navi#|"+mSMSPoint.getLatitudeE6()/1E6+","+mSMSPoint.getLongitudeE6()/1E6+"|");
                            FavoritePoint point = new FavoritePoint();
                            point.setTitle(mFav_title.getText().toString());
                            point.setDescribe(mFav_describe.getText().toString());
                            point.setLat(getMyLocation().getLatitudeE6()/1E6+"");
                            point.setLng(getMyLocation().getLongitudeE6()/1E6+"");
                            point.setTime(new Date().getTime());
                            point.setSource(FavoritePoint.LONGPRESS);
                            Common.saveFavoritePoint(point);
                            Toast.makeText(mContext,"Location Saved", Toast.LENGTH_SHORT).show();          
                        }
                    })
            .setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });
          builder.show();
        }else{
            
        }
        
    }
    
    
	
	
	

	
	

}
