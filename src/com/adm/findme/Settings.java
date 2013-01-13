package com.adm.findme;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

public class Settings extends Activity {

	
	
public static final String PREFS_NAME = "MyPrefsFile";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		try {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
	    if (settings != null)
	    {
		String km = settings.getString("km", "");
	    EditText editKM = (EditText) findViewById(R.id.editTextSettingsKm);
	    editKM.setText(km);
	    }else
	    {
	    	
	    }
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void onStop ()
	{
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      EditText editKM = (EditText) findViewById(R.id.editTextSettingsKm);
	      editor.putString("km", editKM.getText().toString());

	      // Commit the edits!
	      editor.commit();
		super.onStop();
	}
	
	

	
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_settings);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_settings, menu);
//		return true;
//	}

}