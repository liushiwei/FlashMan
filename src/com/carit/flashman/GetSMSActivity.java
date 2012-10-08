package com.carit.flashman;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

public class GetSMSActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.get_sms);
        getPeople(getIntent().getStringExtra("number"));
        super.onCreate(savedInstanceState);
    }
    public void getPeople(String number) {   
        String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,   
                                ContactsContract.CommonDataKinds.Phone.NUMBER};   
   
           
        // 将自己添加到 msPeers 中   
        Cursor cursor = this.getContentResolver().query(   
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,   
                projection,    // Which columns to return.   
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + number + "'", // WHERE clause.   
                null,          // WHERE clause value substitution   
                null);   // Sort order.   
   
        if( cursor == null ) {   
            Log.d("GetSMSActivity", "getPeople null");   
            return;   
        }   
        Log.d("GetSMSActivity", "getPeople cursor.getCount() = " + cursor.getCount());   
        for( int i = 0; i < cursor.getCount(); i++ )   
        {   
            cursor.moveToPosition(i);   
               
            // 取得联系人名字   
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);      
            String name = cursor.getString(nameFieldColumnIndex);   
            Log.i("Contacts", "" + name + " .... " + nameFieldColumnIndex); // 这里提示 force close   
            ((TextView)findViewById(R.id.sms_label)).setText(name);   
        }   
    } 
    

}
