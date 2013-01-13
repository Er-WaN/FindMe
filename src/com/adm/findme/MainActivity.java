package com.adm.findme;


import com.adm.findme.ViewDialog.ViewDialogListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import adm_mysql_2.bd_access;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends android.support.v4.app.FragmentActivity implements LocationListener, OnItemSelectedListener, OnInfoWindowClickListener, ShareDialog.ShareDialogListener, ViewDialogListener {

	
	private GoogleMap mMap;
	private Marker marker;
	public Marker paris;
	
	public String[][] contactlist = new String[][] {{"Carlos", "0034636847726", "39.476618","-0.345389"},{"Massimo", "00393405902903", "39.47251","-0.349509"}};
	
	List<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> element;
	
	private bd_access BD= new bd_access("http://www.carlosexposito.es/");
	
	private LocationManager lm;
	
	double latitude;
	double longitude;
	private double altitude;
	private float accuracy;
		
	int i = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
		
		Spinner map_type = (Spinner) findViewById(R.id.spinner);
		map_type.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.map_type, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		map_type.setAdapter(adapter);
		
		for (int i = 0; i < contactlist.length; i++)
		{
			element = new HashMap<String, String>();
			element.put("name", contactlist[i][0]);
		    element.put("phone", contactlist[i][1]);
			element.put("lat", contactlist[i][2]);
		    element.put("long", contactlist[i][3]);
		    liste.add(element);
		};
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    public void onClickContact(View v) {
    	startActivity (new Intent (this, Contact.class));
    }
    
    //View Groups
    /**
     * Listener de boton View Group
     * @author Carlos Ruiz
     * @param vista
     * se encarga de crear y mostrar el dialogo
     * **/
    public void onClickGroup(View v) {

    	DialogFragment viewDialog = new ViewDialog();
    	viewDialog.show(getSupportFragmentManager(), "ViewDialog");
    }
    
    /**
     * Metodo llamado desde el Dialogo vista de grupos usado para comunicarse entre ellos
     * @author Carlos Ruiz
     * **/
    //Metodo del para comunicarse con el dialogo ViewDialog
    public void onViewDialogGroupSelected(DialogFragment dialog,String selectedGroup){
    	Toast.makeText(this, selectedGroup, Toast.LENGTH_SHORT).show();
    }
    
    public void onClickSettings(View v) {
    	startActivity (new Intent (this, Settings.class));
    }
    
    
    /**
     * Listener de boton share
     * @author Carlos Ruiz
     * **/
    public void onClickShare(View v) {
    	 // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ShareDialog();
        dialog.show(getSupportFragmentManager(), "ShareDialog");
    }

    /**
     * Metodo llamado desde el dialogo Share en caso de que se pulse el boton de Compartir Localizacion
     * @author Carlos Ruiz
     * **/
    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
    	Toast.makeText(this, "Position Shared", Toast.LENGTH_SHORT).show();

    }

    /**
     * Metodo llamado desde el dialogo Share en caso de que se pulse el boton de cancelar compartir localizacion
     * @author Carlos Ruiz
     * **/
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    	Toast.makeText(this, "Cancel Share", Toast.LENGTH_SHORT).show();

    }

	@Override
	// Each time the location changes, this method is called
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		altitude = location.getAltitude();
		accuracy = location.getAccuracy();
		
		if(this.marker !=null){
			this.marker.remove(); 
		}
		
		for (int i = 0; i < contactlist.length; i++)
		{
			float f = Float.parseFloat(contactlist[i][2]);
			double d = Double.parseDouble(contactlist[i][3]);
			MarkerOptions mo = new MarkerOptions().position(new LatLng(f, d)).title(contactlist[i][0]).snippet(contactlist[i][1]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
			this.marker = mMap.addMarker(mo);
		};
		
		MarkerOptions mo = new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		this.marker = mMap.addMarker(mo);
        if (i == 0) {
	        CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(latitude, longitude))
				.zoom(14)
				.build();
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		};
		i = 1;
	}

	@Override
	public void onProviderDisabled(String provider) {		
	}

	@Override
	public void onProviderEnabled(String provider) {		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.v("i", "i: "+i);
		String newStatus = "";
		switch (status){
			case LocationProvider.OUT_OF_SERVICE:
				newStatus = "OUT_OF_SERVICE";
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				newStatus = "TEMPORARILY_UNAVAILABLE";
				break;
			case LocationProvider.AVAILABLE:
				newStatus = "AVAILABLE";
				break;		
		}
		String msg = String.format(getResources().getString(R.string.provider_new_status), provider, newStatus);
		Toast.makeText(this, msg,  Toast.LENGTH_SHORT).show();	
	}

	private void setUpMap() { 	
		//Uncoment this line to enabled my current location on the map (little blue point) but it slows the start of the app.
    	//mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(this);
    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		switch((int)id)
		{
		case 0:
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		break;
		case 1:
			mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		break;
		case 2:
			mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
		break;
		case 3:
			mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		break;
		};
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInfoWindowClick(final Marker marker) {
		//Toast.makeText(this, marker.getTitle(),  Toast.LENGTH_SHORT).show();
		CharSequence[] items = {"Call", "Send a message"};
		//set up dialog
		if (!marker.getTitle().equals("My Position")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(marker.getTitle())
				   .setItems(items, new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int which) {
			            	   switch (which) {
				           			case 0:
					           			Intent call = new Intent(Intent.ACTION_VIEW);
					           			call.setData(Uri.parse("tel:"+marker.getSnippet()));
					           			startActivity(call);
				           			break;
				           			case 1:
					           			Intent send_sms = new Intent(Intent.ACTION_VIEW);
					           			send_sms.setData(Uri.parse("sms:"+marker.getSnippet()));
					           			startActivity(send_sms);
				           			break;
				           		}
				           };});
			builder.show();
		}
	}	
	
	@Override
	public void onPause() {
		super.onPause();
		lm.removeUpdates(this);
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(this);
	};
	
	private class printPlaces extends AsyncTask<Void, Integer, Boolean>{
		ArrayList <ArrayList> places = new ArrayList<ArrayList>();
		public LocationManager mLocationManager;
        public claseLocation mloc;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mloc = new claseLocation();
			mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,mloc);
		}
		@Override
		protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		while (!isCancelled()) {
				try {
					// DO SOMETHING
					places = BD.getPlaces();
					//publishProgress();
				}
				catch(Exception ie) {
					Log.d("AsyncTask", "InterruptedException");
					return false;
				}
			}
		return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		// Change something in the interface
			int cont = 0;
			MarkerOptions mo;
			ArrayList contact;
			while(places.size()< cont){
				contact=BD.getContactByUser((Integer)places.get(cont).get(0));
				//Pinto la marca correspondiente a una publicacion
				mo = new MarkerOptions().position(new LatLng((Double) places.get(cont).get(5),(Double) places.get(cont).get(6))).title((String)places.get(cont).get(2)).snippet((String)contact.get(1)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				marker = mMap.addMarker(mo);
				cont++;
			}
			
	        CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(latitude, longitude))
				.zoom(14)
				.build();
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			
		}
		
		public class claseLocation implements LocationListener {

	        @Override
	        public void onLocationChanged(Location location) {

	            int lat = (int) location.getLatitude(); // * 1E6);
	            int log = (int) location.getLongitude(); // * 1E6);
	            int acc = (int) (location.getAccuracy());

	            String info = location.getProvider();
	            try {
	                latitude = location.getLatitude();
	                longitude = location.getLongitude();

	            } catch (Exception e) {
	                // progDailog.dismiss();
	                // Toast.makeText(getApplicationContext(),"Unable to get Location"
	                // , Toast.LENGTH_LONG).show();
	            }

	        }

	        @Override
	        public void onProviderDisabled(String provider) {
	            Log.i("OnProviderDisabled", "OnProviderDisabled");
	        }

	        @Override
	        public void onProviderEnabled(String provider) {
	            Log.i("onProviderEnabled", "onProviderEnabled");
	        }

	        @Override
	        public void onStatusChanged(String provider, int status,
	                Bundle extras) {
	            Log.i("onStatusChanged", "onStatusChanged");

	        }

	    }
	}
}
