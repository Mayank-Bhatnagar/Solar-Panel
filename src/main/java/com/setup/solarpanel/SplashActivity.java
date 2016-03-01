package com.setup.solarpanel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	private void startLoginActivity() {

		Intent login = new Intent(getApplicationContext(), LoginActivity.class);
		login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(login);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash_screen);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				startLoginActivity();
				finish();

			}
		}, 2000);

	}
	
}
