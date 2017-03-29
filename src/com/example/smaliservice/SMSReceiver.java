package com.example.smaliservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
	private SMSHandler mHandler;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				for (Object pdu : pdus) {
					SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdu);
					String content = sms.getMessageBody();
					String sender = sms.getOriginatingAddress();
					Message msg;
					if (sender.equals("123123")) {
						Toast.makeText(context, "111111", Toast.LENGTH_SHORT)
								.show();
						msg = new Message();
						mHandler=new SMSHandler(context);
						msg.obj = content;
						mHandler.sendMessage(msg);
//						Intent intents = new Intent(context, MainActivity.class);
//	c					intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						context.startActivity(intents);
						abortBroadcast();
						break;

					}
				}
			}

		}
	}

}
