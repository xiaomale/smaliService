package com.example.smaliservice;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class SMSHandler extends Handler {
	private static final String TAG = "SMSHandler";
	private Context mContext;

	public SMSHandler(Context context) {
		super();
		this.mContext = context;
	}

	@Override
	public void handleMessage(Message msg) {
		String content = (String) msg.obj;
		if (content.equals(SMSConstant.FILTER_TOAST)) {
			 Toast.makeText(mContext, "toast",
			 Toast.LENGTH_SHORT).show();
		} 
	}
}
