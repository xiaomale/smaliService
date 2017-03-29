package com.example.smaliservice;

import android.app.Application;
import android.content.Intent;

public class App extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Intent intents = new Intent(getApplicationContext(),
				injectService.class);
		startService(intents);
	}

}
