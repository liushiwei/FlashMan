package com.carit.flashman.amap;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.carit.flashman.R;
import com.carit.flashman.dao.FavoritePoint;
import com.carit.flashman.provider.FavoritePointTable;
import com.carit.flashman.util.Common;

public class FavoritePointsActivity extends Activity implements OnItemClickListener,OnClickListener {
    
    private ListView mPoints;
    
    private FavoritePointsAdapter mAdapter;
    
    private static final int UPDATE_LISTVIEW = 0;
    
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case UPDATE_LISTVIEW:
                    Log.e("Fav", "update listview");
                    mAdapter.notifyDataSetChanged();
                    mPoints.invalidate();
                    break;
            }
        }
        
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fav_points);
        mPoints = (ListView) findViewById(R.id.fav_point_listview);
        Cursor cursor = managedQuery(FavoritePointTable.CONTENT_URI, null, null, null, FavoritePointTable.DEFAULT_SORT_ORDER);
        mAdapter = new FavoritePointsAdapter(this, cursor, R.layout.fav_points_item);
        mPoints.setAdapter(mAdapter);
        mPoints.setOnItemClickListener(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onClick(View v) {
        Log.e("Fav", "Onclick");
        FavoritePoint point =  (FavoritePoint) ((LinearLayout)v.getParent()).getTag();
        if(point!=null){
            Log.e("Fav", "point.getId() = "+point.getId());
            Common.deleteFavoritePoint(point.getId());
            mHandler.sendEmptyMessage(UPDATE_LISTVIEW);
        }
        
    }
    
    
    

}
