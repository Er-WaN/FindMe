package com.adm.findme;


import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends android.support.v4.app.FragmentActivity  implements LocationListener, OnItemSelectedListener, OnInfoWindowClickListener, OnClickListener {

	
	private GoogleMap mMap;
	private Marker marker;
	public Marker paris;
	
	
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
    
    public void onClickGroup(View v) {
    	startActivity (new Intent (this, Group.class));
    }
    
    public void onClickSettings(View v) {
    	startActivity (new Intent (this, Settings.class));
    }
    
    public void onClickShare(View v) {
    	Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
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
		MarkerOptions mo = new MarkerOptions().position(new LatLng(latitude, longitude)).title("ErWaN").snippet("+34622617918").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
		this.marker = mMap.addMarker(mo);
        if (i == 0) {
	        CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(latitude, longitude))
				.zoom(14)
				.build();
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		};
		i = 1;
		Log.v("TAG", "loc");
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
		MarkerOptions mo_paris = new MarkerOptions().position(new LatLng(48.856578, 2.351828)).title("Paris").snippet("Population: 4,137,400").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        this.paris = mMap.addMarker(mo_paris);
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
	public void onInfoWindowClick(Marker marker) {
		//Toast.makeText(this, marker.getTitle(),  Toast.LENGTH_SHORT).show();
		
		//set up dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_on_click_info_window);
        dialog.setCancelable(true);
        dialog.setTitle(marker.getTitle());
        //set up text
        TextView call = (TextView) dialog.findViewById(R.id.callContact);
        TextView send = (TextView) dialog.findViewById(R.id.sendMessageContact);
        call.setText("Call");
        send.setText("Send Message");
        call.setOnClickListener(this);       
        send.setOnClickListener(this);
        dialog.show();
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(this,  "marker: "+marker.getSnippet(), Toast.LENGTH_SHORT).show();
		switch (v.getId()) {
		case R.id.callContact:
			Intent call = new Intent(Intent.ACTION_VIEW);
			call.setData(Uri.parse("tel:0621529465"));
			startActivity(call);
			Toast.makeText(this, "Call", Toast.LENGTH_SHORT).show();
			break;
		case R.id.sendMessageContact:
			String message = "Salut, comment tu vas?";
			Intent send_sms = new Intent(Intent.ACTION_VIEW);
			send_sms.setData(Uri.parse("sms:0621529465"));
			send_sms.putExtra("sms_boby", message);
			startActivity(send_sms);
			//Toast.makeText(this, "SMS", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	
	
}
