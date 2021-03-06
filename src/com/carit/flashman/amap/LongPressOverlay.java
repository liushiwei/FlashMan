package com.carit.flashman.amap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.ItemizedOverlay.OnFocusChangeListener;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.carit.flashman.MainActivity;
import com.carit.flashman.R;
import com.carit.flashman.dao.FavoritePoint;
import com.carit.flashman.util.Common;

@SuppressWarnings("rawtypes")
public class LongPressOverlay extends ItemizedOverlay implements OnDoubleTapListener,
OnGestureListener, OnClickListener, OnFocusChangeListener{

    private Context mContext;

    private MapView mMapView;

    private Handler mHandler;

    private MapController mMapCtrl;

    private GestureDetector gestureScanner = new GestureDetector(this);

    private int level = 0;

    private View mPopView;

    private Drawable mDrawable;

    private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

    private int mCurrentIndex;

    private EditText mFav_title;

    private EditText mFav_describe;

    public LongPressOverlay(Context context, MapView mapView, MapController mapCtrl,
            Drawable drawable) {
        super(boundCenterBottom(drawable));
        mContext = context;
        mMapView = mapView;
        mMapCtrl = mapCtrl;
        setOnFocusChangeListener(this);
        // 初始化气泡,并设置为不可见
        mPopView = View.inflate(mContext, R.layout.long_press_popup, null);
        mMapView.addView(mPopView, new MapView.LayoutParams(MapView.LayoutParams.WRAP_CONTENT,
                MapView.LayoutParams.WRAP_CONTENT, null, MapView.LayoutParams.BOTTOM_CENTER));
        mPopView.setVisibility(View.GONE);
        mDrawable = drawable;
        ImageButton end = (ImageButton) mPopView.findViewById(R.id.btn_end);
        end.setOnClickListener(this);
        end = (ImageButton) mPopView.findViewById(R.id.btn_pass);
        end.setOnClickListener(this);
        end = (ImageButton) mPopView.findViewById(R.id.btn_delete);
        end.setOnClickListener(this);
        end = (ImageButton) mPopView.findViewById(R.id.btn_fav);
        end.setOnClickListener(this);
        populate(); // Add this
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        return gestureScanner.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        if (++level % 3 == 0) {
            mMapCtrl.zoomIn();
            level = 0;
        }
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        MapView.LayoutParams params = (MapView.LayoutParams) mPopView.getLayoutParams();
        params.x = mDrawable.getBounds().centerX();// Y轴偏移
        params.y = -mDrawable.getBounds().height();// Y轴偏移
        GeoPoint point = mMapView.getProjection().fromPixels((int) e.getX(), (int) e.getY());
        params.point = point;
        mMapCtrl.animateTo(point);
        mOverlays.add(new OverlayItem(point, "", ""));
        mCurrentIndex = mOverlays.size() - 1;
        populate();
        mMapView.updateViewLayout(mPopView, params);
        mPopView.setVisibility(View.VISIBLE);
        Log.e("LongPressOverlay", "point = " + point.getLatitudeE6() + "," + point.getLongitudeE6());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
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
    
    public void addOverlayItem(OverlayItem item){
        MapView.LayoutParams params = (MapView.LayoutParams) mPopView.getLayoutParams();
        params.x = mDrawable.getBounds().centerX();// Y轴偏移
        params.y = -mDrawable.getBounds().height();// Y轴偏移
        GeoPoint point = item.getPoint();
        params.point = point;
        mMapCtrl.animateTo(point);
        mOverlays.add(item);
        mCurrentIndex = mOverlays.size() - 1;
        populate();
        mMapView.updateViewLayout(mPopView, params);
        mPopView.setVisibility(View.VISIBLE);
        Log.e("LongPressOverlay", "point = " + point.getLatitudeE6() + "," + point.getLongitudeE6());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_end:
                if (mOverlays.size() > 1) {
                    GeoPoint[] passPoint = new GeoPoint[mOverlays.size() - 1];

                    for (int i = 0, j = 0; i < mOverlays.size(); i++) {
                        if (i != mCurrentIndex) {
                            passPoint[j] = mOverlays.get(i).getPoint();
                            j++;
                        }
                    }
                    //mContext.getPath(mOverlays.get(mOverlays.size() - 1).getPoint(), passPoint);
                } else
                    //mContext.getPath(mOverlays.get(mOverlays.size() - 1).getPoint(), null);

                break;
            case R.id.btn_delete:
                mOverlays.remove(mCurrentIndex);
                populate();
                setLastFocusedIndex(-1);
                mMapView.invalidate();
                mPopView.setVisibility(View.GONE);
                break;
            case R.id.btn_pass:
                
                ((MainActivity)mContext).setSMSPoint(mOverlays.get(mCurrentIndex).getPoint());
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("vnd.android.cursor.dir/phone");
                ((MainActivity) mContext).startActivityForResult(i, 0);
                break;
            case R.id.btn_fav:
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
                                point.setLat(mOverlays.get(mCurrentIndex).getPoint().getLatitudeE6()/1E6+"");
                                point.setLng(mOverlays.get(mCurrentIndex).getPoint().getLongitudeE6()/1E6+"");
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
              break;
            
        }

    }

    @Override
    public void onFocusChanged(ItemizedOverlay overlay, OverlayItem newFocus) {
        if (newFocus != null) {
            mPopView.setVisibility(View.GONE);
            MapView.LayoutParams params = (MapView.LayoutParams) mPopView.getLayoutParams();
            params.x = mDrawable.getBounds().centerX();// Y轴偏移
            params.y = -mDrawable.getBounds().height();// Y轴偏移
            GeoPoint point = newFocus.getPoint();
            params.point = point;
            mMapCtrl.animateTo(point);
            mCurrentIndex = mOverlays.indexOf(newFocus);
            populate();
            mMapView.updateViewLayout(mPopView, params);
            mPopView.setVisibility(View.VISIBLE);
        }
    }


}
