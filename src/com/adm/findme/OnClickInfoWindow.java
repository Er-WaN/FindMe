package com.adm.findme;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class OnClickInfoWindow extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_on_click_info_window);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_on_click_info_window, menu);
		return true;
	}

}
