package com.carit.flashman;

import com.amap.mapapi.core.OverlayItem;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.gesture.GesturePoint;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

public class Boot_Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent it = new Intent(Intent.ACTION_RUN);
            it.setClass(context, FlashManService.class);
            it.putExtra("from", FlashManService.BOOT_COMPLETED);
            context.startService(it);
        }
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            // 不知道为什么明明只有一条消息，传过来的却是数组，也许是为了处理同时同分同秒同毫秒收到多条短信
            // 但这个概率有点小
            SmsMessage[] message = new SmsMessage[pdus.length];
            StringBuilder sb = new StringBuilder();
            Log.e("SMS_Receiver", "pdus长度" + pdus.length);
            for (int i = 0; i < pdus.length; i++) {
                // 虽然是循环，其实pdus长度一般都是1
                message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                sb.append("接收到短信来自:\n");
                sb.append(message[i].getDisplayOriginatingAddress() + "\n");
                sb.append("内容:" + message[i].getDisplayMessageBody());
                String body = message[i].getDisplayMessageBody();
                if (body.contains("#navi#")) {
                    String point = body.substring(body.indexOf("|") + 1,
                            body.indexOf("|", body.indexOf("|") + 1));
                    String[] tmp = point.split(",");
                    
                    abortBroadcast();
                    Intent it = new Intent();
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    it.setClass(context, GetSMSActivity.class);
                    it.putExtra("lat", tmp[0]);
                    it.putExtra("lng", tmp[1]);
                    it.putExtra("number", message[i].getOriginatingAddress());
                    it.putExtra("from", FlashManService.BOOT_COMPLETED);
                    context.startActivity(it);
                }
            }
            Log.e("SMS_Receiver", sb.toString());
        }
    }
    
      
    

}