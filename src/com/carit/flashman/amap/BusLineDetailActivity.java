package com.carit.flashman.amap;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.carit.flashman.R;
import com.carit.flashman.provider.BusLineTable;

public class BusLineDetailActivity extends Activity {
    
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            
            switch(msg.what){
                case 0:
                    Cursor busline = (Cursor) msg.obj;
                    if(busline.moveToFirst()){
                        ((TextView)findViewById(R.id.bus_start_time)).setText(busline.getString(busline.getColumnIndex(BusLineTable.STARTTIME)));
                        ((TextView)findViewById(R.id.bus_end_time)).setText(busline.getString(busline.getColumnIndex(BusLineTable.ENDTIME)));
                        ((TextView)findViewById(R.id.bus_front_name)).setText(busline.getString(busline.getColumnIndex(BusLineTable.FRONTNAME)));
                        ((TextView)findViewById(R.id.bus_terminal_name)).setText(busline.getString(busline.getColumnIndex(BusLineTable.TERMINALNAME)));
                        ((TextView)findViewById(R.id.bus_basic_price)).setText(String.format(getString(R.string.bus_basic_price), busline.getDouble(busline.getColumnIndex(BusLineTable.BASICPRICE))));
                        ((TextView)findViewById(R.id.bus_total_price)).setText(String.format(getString(R.string.bus_total_price), busline.getDouble(busline.getColumnIndex(BusLineTable.TOTALPRICE))));
                        }
                    break;
            }
        }
        
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.busline_detail);
        ((TextView)findViewById(R.id.bus_name)).setText(getIntent().getStringExtra("bus_name"));
        new GetDetailThread().start();
        super.onCreate(savedInstanceState);
    }
    
    class GetDetailThread extends Thread{

        @Override
        public void run() {
            String bus_line_id = getIntent().getStringExtra("bus_line_id");
            Cursor  busline= getContentResolver().query(BusLineTable.CONTENT_URI, new String[]{BusLineTable._ID,BusLineTable.STARTTIME,BusLineTable.ENDTIME,BusLineTable.LENGTH,BusLineTable.FRONTNAME,BusLineTable.TERMINALNAME,BusLineTable.BASICPRICE,BusLineTable.TOTALPRICE}, BusLineTable.LINEID+" = "+bus_line_id, null, null);
            mHandler.obtainMessage(0, busline).sendToTarget();
        }
        
    }
    
    

}
