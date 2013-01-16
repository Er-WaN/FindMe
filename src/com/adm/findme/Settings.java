package com.adm.findme;

import adm_mysql_2.bd_access;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends Activity {

	int phoneNumber;
	String KM;
	public static final String PREFS_NAME = "MyPrefsFile";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		try {
			
			KM = getKMPref();
			EditText editKM = (EditText) findViewById(R.id.editTextSettingsKm);
	    	editKM.setText(KM);
		/*SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
	    if (settings != null)
	    {
		String km = settings.getString("km", "");
	    EditText editKM = (EditText) findViewById(R.id.editTextSettingsKm);
	    editKM.setText(km);
	    }else
	    {
	    	
	    }*/
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	

	/*
	 * Implementaci�n del listener para el bot�n "Delete Historic" que elimina
	 * el hist�rico de ubicaciones compartidas por el usuario	 
	 */
	
	   Button buttonSettingsDelete = (Button) findViewById(R.id.buttonSettingsDelete);
       
       buttonSettingsDelete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				phoneNumber = getPhoneNumber();
				
				
				if (phoneNumber == 0)
				{
					
					Toast.makeText(getApplicationContext(), "Phone: Null or empty value", Toast.LENGTH_LONG).show();				
				}
				else
				{
					bd_access bd = new bd_access("http://www.carlosexposito.es/");
					
					try
					{
					 int response = bd.deletePlacesByTelef(phoneNumber);	
					
					 switch (response)
						{
						case -1:
							Toast.makeText(getApplicationContext(), "User does not exist in database", Toast.LENGTH_LONG).show();
						break;
						
						case -2:
							Toast.makeText(getApplicationContext(), "Server conexion error", Toast.LENGTH_LONG).show();
						break;
						
						case 0:
							Toast.makeText(getApplicationContext(), "History of " + phoneNumber + " deleted", Toast.LENGTH_LONG).show();
						break;
						}
					
					}catch (Exception e)
					{
						e.printStackTrace();
					}
					
					
				}
			}});
		
		
       /*
        * Implementaci�n del listener para el bot�n "Delete Last Shared Location"
        * que borra la �ltima ubicaci�n compartido por el usuario        
        * 
        */
            Button buttonDeleteLastPlace = (Button) findViewById(R.id.buttonSettingsDeleteLastShare);
            
            buttonDeleteLastPlace.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					phoneNumber = getPhoneNumber();
					
					if (phoneNumber == 0)
					{
						Toast.makeText(getApplicationContext(), "Phone: Null or empty value", Toast.LENGTH_LONG).show();				
					}
					else
					{
						
						bd_access bd = new bd_access("http://www.carlosexposito.es/");
						try
						{
							int response = bd.deleteLastPlaceByTelef(phoneNumber);
							switch (response)
							{
							case -1:
								Toast.makeText(getApplicationContext(), "User does not exist in database", Toast.LENGTH_LONG).show();
							break;
							
							case -2:
								Toast.makeText(getApplicationContext(), "Server conexion error", Toast.LENGTH_LONG).show();
							break;
							
							case 0:
								Toast.makeText(getApplicationContext(), "Last place of " + phoneNumber + " deleted", Toast.LENGTH_LONG).show();
							break;
							}
										
							
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}});	
					
				
            
            
	}

	public void onResume ()
	{
		/*SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
	    if (settings != null)
	    {
		String km = settings.getString("km", "");
	    EditText editKM = (EditText) findViewById(R.id.editTextSettingsKm);
	    editKM.setText(km);
	    }*/
		
		KM = getKMPref();
		EditText editKM = (EditText) findViewById(R.id.editTextSettingsKm);
    	editKM.setText(KM);
	    
	    super.onResume();
	}
	
	public void onPause()
	{
		guardar();
		super.onPause();
	}
	
	public void onStop ()
	{
		
		guardar();
		super.onStop();
	}
	
	
	/*
	 * Con esta funci�n guardamos en la clave km del fichero de preferencias el valor del
	 * EditText. 
	 * 
	 */
	
	public void guardar()
	{
		  SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	      
		  SharedPreferences.Editor editor = settings.edit();
	      
		  EditText editKM = (EditText) findViewById(R.id.editTextSettingsKm);
	      editor.putString("km", editKM.getText().toString());

	      editor.commit();

	}
	

	/*
	 * Esta funci�n devuelve del fichero del preferencias el valor
	 * de la clave myPhone que contiene el n�mero de telefono
	 * 
	 */
	public int getPhoneNumber() {
		int myPhone = 0;
		try {
			SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
		    if (settings != null)
		    {
		    	if ( settings.contains("myPhone"))
		    	{
		    		
		    		myPhone = settings.getInt("myPhone", 0);
		    		
		    		Log.d("FindMe","La clave myPhone existe con el valor " + myPhone );
		    	}
		    }		    
			}catch (Exception e)
			{
				e.printStackTrace();
			}
	     return myPhone;
	}
	
	/*Con el m�todo getKMPref recuperamos del fichero de preferencias
	 * el valor de la clave km
	 * 
	 */
	
	public String getKMPref()
	{
		String value = "";
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
	    if (settings != null)
	    {
	    	if(settings.contains("km"))
	    	{
	    	value = settings.getString("km", "");
	    	}
	    }
		
		return value;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
       
       case R.id.menu_settings:
		  	guardar();
		} 	
		return true;
	}


}