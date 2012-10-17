package com.carit.flashman.amap;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.carit.flashman.R;
import com.carit.flashman.provider.FavoritePointTable;

public class FavoritePointsActivity extends Activity implements OnItemClickListener {
    
    private ListView mPoints;
    
    private FavoritePointsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fav_points);
        mPoints = (ListView) findViewById(R.id.fav_point_listview);
        Cursor cursor = managedQuery(FavoritePointTable.CONTENT_URI, null, null, null, FavoritePointTable.DEFAULT_SORT_ORDER);
        mAdapter = new FavoritePointsAdapter(getBaseContext(), cursor, R.layout.fav_points_item);
        mPoints.setAdapter(mAdapter);
        mPoints.setOnItemClickListener(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        
    }
    
    
    

}
