
package com.carit.flashman.amap;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.mapapi.busline.BusLineItem;
import com.amap.mapapi.busline.BusPagedResult;
import com.amap.mapapi.busline.BusQuery;
import com.amap.mapapi.busline.BusSearch;
import com.amap.mapapi.busline.BusStationItem;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView;
import com.carit.flashman.R;
import com.carit.flashman.amap.BusLineListAdapter.BusLineData;
import com.carit.flashman.provider.BusLineRelevanceTable;
import com.carit.flashman.provider.BusLineTable;
import com.carit.flashman.provider.BusStationTable;
import com.google.gson.Gson;

public class BusLineSearch extends Activity implements OnItemSelectedListener, OnClickListener,
        BusLineOverlay.BusLineMsgHandler, OnCheckedChangeListener {
    
    public static final String TAG = "BusLineSearch";
    
    private EditText searchName;

    private String[] itemCitys = {
            "北京-010", "上海-021", "西安-029", "深圳-0755"
    };

    private String cityCode;

    private EditText pageSizeText;

    private ProgressDialog progDialog = null;

    private BusPagedResult result = null;

    private int curPage = 1;

    private boolean mIsSearchBusLine = true;

    public static final int BUSLINE_RESULT = 6000;

    public static final int BUSLINE_DETAIL_RESULT = 6001;

    public static final int BUSLINE_ERROR_RESULT = 6002;

    public static final int KEY_NULL = 6003;

    public static final int SEARCHING = 6004;
    
    public static final int UPDATE_ADAPTER = 6005;
    
    private ExpandableListView mBusLineList;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case KEY_NULL:
                    Toast.makeText(getBaseContext(), "输入不能为空", Toast.LENGTH_LONG).show();
                    break;
                case UPDATE_ADAPTER:
                    if(mIsSearchBusLine){
                    Cursor cursor = getContentResolver().query(BusLineTable.CONTENT_URI, new String[] {
                            BusLineTable._ID,
                            BusLineTable.LINEID,
                            BusLineTable.NAME
                    }, BusLineTable.NAME+" Like "+"'"+msg.obj+"%'", null, null);
                    mBusLineList.setAdapter(new BusLineListAdapter(cursor,BusLineSearch.this,mIsSearchBusLine));
                    }else{
                        Cursor cursor = getContentResolver().query(BusStationTable.CONTENT_URI, new String[] {
                                BusStationTable._ID,
                                BusStationTable.LAT,
                                BusStationTable.LNG,
                                BusStationTable.NAME,
                        }, BusStationTable.NAME+" Like "+"'"+msg.obj+"%' )GROUP BY ("+BusStationTable.NAME, null, null);
                        mBusLineList.setAdapter(new BusLineListAdapter(cursor,BusLineSearch.this,mIsSearchBusLine));
                    }
                    mBusLineList.invalidate();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bussearch);
        searchName = (EditText) findViewById(R.id.busName);
        findViewById(R.id.search).setOnClickListener(this);
        ((RadioButton) findViewById(R.id.search_busline)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.search_busstation)).setOnCheckedChangeListener(this);
        SharedPreferences info = getSharedPreferences("Info", 0);
        cityCode = info.getString("CityCode", "0755");
        mBusLineList = (ExpandableListView)findViewById(R.id.busLineList);
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void drawBusLine(BusLineItem busLine) {
        ArrayList<GeoPoint> pts = new ArrayList<GeoPoint>();
        pts.add(busLine.getLowerLeftPoint());
        pts.add(busLine.getUpperRightPoint());
        for(BusStationItem item:busLine.getmStations()){
            Log.e(TAG, "item:"+item.getmName()+"-"+item.getmCoord().getLatitudeE6()+","+item.getmCoord().getLongitudeE6());
        }
    }

    private void showResultList(List<BusLineItem> list) {
        BusSearchDialog dialog = new BusSearchDialog(BusLineSearch.this, list);

        dialog.setTitle("搜索结果:");
        dialog.setOnListClickListener(new OnListItemClick() {
            @Override
            public void onListItemClick(BusSearchDialog dialog, final BusLineItem busLineItem) {
                progDialog = ProgressDialog.show(BusLineSearch.this, null, "正在搜索...", true, false);
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        String lineId = busLineItem.getmLineId();
                        BusSearch busSearch = new BusSearch(BusLineSearch.this, new BusQuery(
                                lineId, BusQuery.SearchType.BY_ID, cityCode)); // 设置搜索字符串
                        try {
                            result = busSearch.searchBusLine();
                            buslineHandler.sendEmptyMessage(BUSLINE_DETAIL_RESULT);
                        } catch (AMapException e) {
                            Message msg = new Message();
                            msg.what = BUSLINE_ERROR_RESULT;
                            msg.obj = e.getErrorMessage();
                            buslineHandler.sendMessage(msg);
                        }
                    }

                });
                t.start();
            }

        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.search){
            String search = searchName.getText().toString().trim();
            if ("".equals(search)) {
                mHandler.sendEmptyMessage(KEY_NULL);
                return;
            }
           mHandler.obtainMessage(UPDATE_ADAPTER, search).sendToTarget();
            
        }
        if(v.getId()==R.id.map_view_busline){
            
        }
        if(v.getId()==R.id.map_view_busstation){
            if(mIsSearchBusLine){
                
            }else{
                Intent intent = new Intent();
                intent.putExtra("lat", ((BusLineData)v.getTag()).getLat());
                intent.putExtra("lng", ((BusLineData)v.getTag()).getLng());
                intent.putExtra("name", ((BusLineData)v.getTag()).getName());
                setResult(1, intent);
                finish();
            }
        }
        if(v.getId()==R.id.view_busline){
            
        }
        
        /*Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                String search = searchName.getText().toString().trim();
                BusQuery.SearchType type = BusQuery.SearchType.BY_LINE_NAME;
                if ("".equals(search)) {
                    mHandler.sendEmptyMessage(KEY_NULL);
                    return;
                }
                buslineHandler.sendEmptyMessage(SEARCHING);
                if (!mIsSearchBusLine) {

                    type = BusQuery.SearchType.BY_STATION_NAME;
                }
                try {
                    curPage = 1;
                    BusSearch busSearch = new BusSearch(BusLineSearch.this, new BusQuery(search,
                            type, cityCode)); // 设置搜索字符串
                    busSearch.setPageSize(4);
                    result = busSearch.searchBusLine();
                    Log.d("AMAP POI search", "poi search page count = " + result.getPageCount());
                    buslineHandler.sendEmptyMessage(BUSLINE_RESULT);
                } catch (AMapException e) {
                    Message msg = new Message();
                    msg.what = BUSLINE_ERROR_RESULT;
                    msg.obj = e.getErrorMessage();
                    buslineHandler.sendMessage(msg);
                }
            }

        });
        t.start();*/
    }

    private Handler buslineHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == BUSLINE_RESULT) {
                progDialog.dismiss();
                List<BusLineItem> items;
                try {
                    if (result == null || (items = result.getPage(curPage)) == null
                            || items.size() == 0) {
                        Toast.makeText(getApplicationContext(), "没有找到！", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("AMAP busline search", "item number of 1st page = " + items.size());
                        Log.d("AMAP busline search", items.toString());
                        Gson gson = new Gson();
                        Log.d("AMAP busline search", gson.toJson(items));
                        showResultList(items);
                    }
                } catch (AMapException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == BUSLINE_DETAIL_RESULT) {
                progDialog.dismiss();
                List<BusLineItem> list;
                try {
                    if (result != null) {
                        list = result.getPage(1);
                        if (list != null && list.size() > 0) {
                            drawBusLine(list.get(0));
                        }
                    }
                } catch (AMapException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == BUSLINE_ERROR_RESULT) {
                progDialog.dismiss();
                Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT)
                        .show();
            } else if (msg.what == KEY_NULL) {
                Toast.makeText(getBaseContext(), "输入不能为空", Toast.LENGTH_LONG).show();
            } else if(msg.what == SEARCHING){
                progDialog = ProgressDialog.show(BusLineSearch.this, null, "正在搜索...", true, false);
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String cityString = itemCitys[position];
        cityCode = cityString.substring(cityString.indexOf("-") + 1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    interface OnListItemClick {
        /**
         * This method will be invoked when the dialog is canceled.
         * 
         * @param dialog The dialog that was canceled will be passed into the
         *            method.
         */
        public void onListItemClick(BusSearchDialog dialog, BusLineItem item);
    }

    public class BusSearchDialog extends Dialog implements OnItemClickListener,
            OnItemSelectedListener {
        private List<BusLineItem> busLineItems;

        private Context context;

        private BusSearchAdapter adapter;

        protected OnListItemClick mOnClickListener;

        private Button preButton, nextButton;

        public BusSearchDialog(Context context) {
            this(context, android.R.style.Theme_Dialog);
            this.context = context;
        }

        public BusSearchDialog(Context context, int theme) {
            super(context, theme);
            this.context = context;
        }

        public BusSearchDialog(Context context, List<BusLineItem> busLineItems) {
            this(context, android.R.style.Theme_Dialog);
            this.busLineItems = busLineItems;
            this.context = context;
            adapter = new BusSearchAdapter(context, busLineItems);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            setContentView(R.layout.navsearch_list_busline);
            ListView listView = (ListView) findViewById(R.id.ListView_busline);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dismiss();
                    mOnClickListener.onListItemClick(BusSearchDialog.this,
                            busLineItems.get(position));
                }
            });

            onButtonClick listener = new onButtonClick();
            preButton = (Button) findViewById(R.id.preButton);
            if (curPage <= 1) {
                preButton.setEnabled(false);
            }
            preButton.setOnClickListener(listener);
            nextButton = (Button) findViewById(R.id.nextButton);
            if (curPage >= result.getPageCount()) {
                nextButton.setEnabled(false);
            }
            nextButton.setOnClickListener(listener);
        }

        class onButtonClick implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                BusSearchDialog.this.dismiss();
                if (v.equals(preButton)) {
                    curPage--;
                } else if (v.equals(nextButton)) {
                    curPage++;
                }

                progDialog = ProgressDialog.show(BusLineSearch.this, null, "正在搜索...", true, false);
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            result.getPage(curPage);
                            buslineHandler.sendEmptyMessage(BUSLINE_RESULT);
                        } catch (AMapException e) {
                            Message msg = new Message();
                            msg.what = BUSLINE_ERROR_RESULT;
                            msg.obj = e.getErrorMessage();
                            buslineHandler.sendMessage(msg);
                        }
                    }

                });
                t.start();
            }
        }

        @Override
        public void onItemClick(AdapterView<?> view, View view1, int arg2, long arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

        public void setOnListClickListener(OnListItemClick l) {
            mOnClickListener = l;
        }
    }

    // Dialog list view adapter
    public class BusSearchAdapter extends BaseAdapter {
        private Context context;

        private List<BusLineItem> busLineItems = null;

        private LayoutInflater mInflater;

        public BusSearchAdapter(Context context, List<BusLineItem> busLineItems) {
            this.context = context;
            this.busLineItems = busLineItems;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return busLineItems.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.bus_result_list, null);
            }

            TextView PoiName = ((TextView) convertView.findViewById(R.id.buslineName));
            TextView poiAddress = (TextView) convertView.findViewById(R.id.buslineLength);
            PoiName.setText(busLineItems.get(position).getmName()+":"+busLineItems.get(position).getmLineId());
            float length = busLineItems.get(position).getmLength();
            poiAddress.setText("全长:" + busLineItems.get(position).getmLength() + "公里");
            ;
            return convertView;
        }
    }

    @Override
    public boolean onStationClickEvent(MapView mapView, BusLineOverlay overlay, int index) {
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.search_busline&&isChecked) {
            mIsSearchBusLine = true;
        } else if(buttonView.getId() == R.id.search_busstation&&isChecked){
            mIsSearchBusLine = false;
        }

    }
}
