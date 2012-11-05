
package com.carit.flashman.amap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.carit.flashman.R;
import com.carit.flashman.provider.BusLineRelevanceTable;
import com.carit.flashman.provider.BusLineTable;

public class BusLineDetailActivity extends Activity implements OnClickListener {

    private ProgressBar mProgDialog;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    Cursor busline = (Cursor) msg.obj;
                    if (busline.moveToFirst()) {
                        ((TextView) findViewById(R.id.bus_start_time)).setText(busline
                                .getString(busline.getColumnIndex(BusLineTable.STARTTIME)));
                        ((TextView) findViewById(R.id.bus_end_time)).setText(busline
                                .getString(busline.getColumnIndex(BusLineTable.ENDTIME)));
                        ((TextView) findViewById(R.id.bus_front_name)).setText(busline
                                .getString(busline.getColumnIndex(BusLineTable.FRONTNAME)));
                        ((TextView) findViewById(R.id.bus_terminal_name)).setText(busline
                                .getString(busline.getColumnIndex(BusLineTable.TERMINALNAME)));
                        ((TextView) findViewById(R.id.bus_basic_price))
                                .setText(String.format(getString(R.string.bus_basic_price), busline
                                        .getDouble(busline.getColumnIndex(BusLineTable.BASICPRICE))));
                        ((TextView) findViewById(R.id.bus_total_price))
                                .setText(String.format(getString(R.string.bus_total_price), busline
                                        .getDouble(busline.getColumnIndex(BusLineTable.TOTALPRICE))));
                        ((TextView) findViewById(R.id.bus_length)).setText(String.format(
                                getString(R.string.bus_length),
                                busline.getDouble(busline.getColumnIndex(BusLineTable.LENGTH))));
                    }
                    busline.close();
                    break;
                case 1:
                    Cursor busStations = (Cursor) msg.obj;
                    ListView listView = (ListView) findViewById(R.id.bus_stations);
                    listView.setAdapter(new SimpleCursorAdapter(BusLineDetailActivity.this,
                            R.layout.busstation_list_item, busStations, new String[] {
                                BusLineRelevanceTable.BUSSTATIONNAME
                            }, new int[] {
                                android.R.id.text1
                            }){

                                @Override
                                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                                    View view = super.newView(context, cursor, parent);
                                    view.findViewById(R.id.view_busline).setOnClickListener(BusLineDetailActivity.this);
                                    view.findViewById(R.id.map_view_busline).setOnClickListener(BusLineDetailActivity.this);
                                    return view;
                                }

                                @Override
                                public void bindView(View view, Context context, Cursor cursor) {
                                    
                                    super.bindView(view, context, cursor);
                                }
                                
                                
                        
                    });
                    // busStations.close();
                    mProgDialog.setVisibility(View.INVISIBLE);
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.busline_detail);
        ((TextView) findViewById(R.id.bus_name)).setText(getIntent().getStringExtra("bus_name"));
        findViewById(R.id.btn_back).setOnClickListener(this);
        new GetDetailThread().start();
        mProgDialog = (ProgressBar) findViewById(R.id.loading_data);
        super.onCreate(savedInstanceState);
    }

    class GetDetailThread extends Thread {

        @Override
        public void run() {
            String bus_line_id = getIntent().getStringExtra("bus_line_id");
            Cursor busline = getContentResolver().query(
                    BusLineTable.CONTENT_URI,
                    new String[] {
                            BusLineTable._ID, BusLineTable.STARTTIME, BusLineTable.ENDTIME,
                            BusLineTable.LENGTH, BusLineTable.FRONTNAME, BusLineTable.TERMINALNAME,
                            BusLineTable.BASICPRICE, BusLineTable.TOTALPRICE
                    }, BusLineTable.LINEID + " = " + bus_line_id, null, null);
            mHandler.obtainMessage(0, busline).sendToTarget();
            Cursor busStation = getContentResolver().query(
                    BusLineRelevanceTable.CONTENT_URI,
                    new String[] {
                            BusLineRelevanceTable._ID, BusLineRelevanceTable.BUSSTATIONID,
                            BusLineRelevanceTable.BUSSTATIONNAME,
                    }, BusLineRelevanceTable.BUSLINEID + "=" + bus_line_id, null, null);
            mHandler.obtainMessage(1, busStation).sendToTarget();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }

    }

    // double roundTwoDecimals(double d)
    // {
    // DecimalFormat twoDForm = new DecimalFormat("#.####");
    // return Double.valueOf(twoDForm.format(d));
    // }
    //

}
