package com.medicalstorefinder.mychemists;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class IncomingSms extends BroadcastReceiver {

    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNumber = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody().replaceAll("[\\D]", "");
                    message = message.substring(0, message.length());
                    Log.i("SmsReceiver", "SenderNum: " + senderNumber + "; message :" + message);
                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message", message);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver: " + e);
        }

    }
}
