package com.carit.flashman.amap;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.carit.flashman.FlashManApplication;
import com.carit.flashman.R;
import com.amap.mapapi.map.ItemizedOverlay.OnFocusChangeListener;


@SuppressWarnings("rawtypes")
public class SMSLocationOverlay extends ItemizedOverlay implements OnFocusChangeListener{

    private MapView mMapView;
    private MapController mMapCtrl;
    private View mPopView;
    private Drawable mDrawable;
    private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
   
    public SMSLocationOverlay( MapView mapView, MapController mapCtrl,
            Drawable drawable) {
        super(boundCenterBottom(drawable));
        mMapView = mapView;
        mMapCtrl = mapCtrl;
        setOnFocusChangeListener(this);
        // 初始化气泡,并设置为不可见
        mPopView = View.inflate(FlashManApplication.getContext(), R.layout.popup_nobtn, null);
        mMapView.addView(mPopView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
                MapView.LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.BOTTOM_CENTER));
        mPopView.setVisibility(View.GONE);
        mDrawable = drawable;
//        ImageButton end = (ImageButton) mPopView.findViewById(R.id.btn_end);
//        end.setOnClickListener(this);
//        end = (ImageButton) mPopView.findViewById(R.id.btn_pass);
//        end.setOnClickListener(this);
//        end = (ImageButton) mPopView.findViewById(R.id.btn_delete);
//        end.setOnClickListener(this);
        populate(); // Add this
    }
    
    @Override
    public void onFocusChanged(ItemizedOverlay<?> arg0, OverlayItem newFocus) {
        if (newFocus != null) {
            mPopView.setVisibility(View.GONE);
            MapView.LayoutParams params = (MapView.LayoutParams) mPopView.getLayoutParams();
            params.x = mDrawable.getBounds().centerX();// Y轴偏移
            params.y = -mDrawable.getBounds().height();// Y轴偏移
            TextView title_TextView = (TextView) mPopView.findViewById(R.id.ImageButton01);
            title_TextView.setText(newFocus.getTitle());
            TextView desc_TextView = (TextView) mPopView.findViewById(R.id.TextView02);
            desc_TextView.setText(R.string.im_here);
            GeoPoint point = newFocus.getPoint();
            params.point = point;
            mMapCtrl.animateTo(point);
            populate();
            mMapView.updateViewLayout(mPopView, params);
            mPopView.setVisibility(View.VISIBLE);
        }
        
    }

    @Override
    protected OverlayItem createItem(int i) {
        // TODO Auto-generated method stub
        return mOverlays.get(i);
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return mOverlays.size();
    }
    
    public void addSMSItem(GeoPoint point , String title){
        MapView.LayoutParams params = (MapView.LayoutParams) mPopView.getLayoutParams();
        params.x = mDrawable.getBounds().centerX();// Y轴偏移
        params.y = -mDrawable.getBounds().height();// Y轴偏移
        TextView title_TextView = (TextView) mPopView.findViewById(R.id.ImageButton01);
        title_TextView.setText(title);
        TextView desc_TextView = (TextView) mPopView.findViewById(R.id.TextView02);
        desc_TextView.setText(R.string.im_here);
        params.point = point;
        mMapCtrl.animateTo(point);
        mOverlays.add(new OverlayItem(point, title, ""));
        populate();
        mMapView.updateViewLayout(mPopView, params);
        mPopView.setVisibility(View.VISIBLE);
        Log.e("LongPressOverlay", "point = " + point.getLatitudeE6() + "," + point.getLongitudeE6());
        
    }


}
