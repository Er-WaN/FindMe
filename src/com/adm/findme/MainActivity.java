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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends android.support.v4.app.FragmentActivity implements LocationListener, OnItemSelectedListener, OnInfoWindowClickListener, ShareDialog.ShareDialogListener, ViewDialogListener{
	
	private GoogleMap mMap;
	private Marker marker;
	public Marker paris;
	
	
	public String[][] contactlist= new String[0][0];
	
	List<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> element;
	
	private bd_access BD= new bd_access("http://www.carlosexposito.es/");
	
	private LocationManager lm;
	
	double latitude;
	double longitude;
	private double altitude;
	private float accuracy;
	
	ProgressDialog progressDialog;
		
	int i = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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
		
			new actualizarBDLocal().execute();
			new printPlaces().execute();

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
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
    
    public void onClickSettings(View v) {
    	startActivity (new Intent (this, Settings.class));
    }
    
    //View Groups
    /**
     * Listener de boton View Group
     * @author Carlos Ruiz
     * @param vista
     * se encarga de crear y mostrar el dialogo
     * **/
    public void onClickView(View v) {

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
    public void onDialogPositiveClick(DialogFragment dialog, String place_name, String place_description) {
        // User touched the dialog's positive button
    	final String PREFS_NAME = "MyPrefsFile";
		float latitude;
		float longitude;
		int myID;
		try {
			SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);		
			latitude = preferences.getFloat("last_latitude", 0);
			longitude = preferences.getFloat("last_longitude", 0);
			myID = preferences.getInt("myUserID", 0);
		} catch(Exception e) {
			latitude = 0;
			longitude = 0;
			myID = 0;
		}
    	String[] params_for_async = {place_name, String.valueOf(latitude), String.valueOf(longitude), String.valueOf(myID) , place_description};
    	new putNewPlaceInDB().execute(params_for_async);
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
		
		final String PREFS_NAME = "MyPrefsFile";		
		
		SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
		
		Editor editor = preferences.edit();
		editor.putFloat("last_latitude", (float)latitude);
		editor.putFloat("last_longitude", (float)longitude );
		editor.commit();
		
		//new actualizarBDLocal().execute();
		new printPlaces().execute();
		
		updateMap();
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
			builder.setTitle(splitTitle(marker.getTitle())[0])
				   .setItems(items, new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int which) {
			            	   switch (which) {
				           			case 0:
					           			Intent call = new Intent(Intent.ACTION_VIEW);
					           			call.setData(Uri.parse("tel:"+splitTitle(marker.getTitle())[1]));
					           			startActivity(call);
				           			break;
				           			case 1:
					           			Intent send_sms = new Intent(Intent.ACTION_VIEW);
					           			send_sms.setData(Uri.parse("sms:"+splitTitle(marker.getTitle())[1]));
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
	
	public void updateMap(){
		
		if(marker !=null){
			marker.remove(); 
		}
		
		mMap.clear();
		for (int i = 0; i < contactlist.length; i++)
		{
			float f = Float.parseFloat(contactlist[i][2]);
			double d = Double.parseDouble(contactlist[i][3]);
			String snippet = "";
			if (contactlist[i][4].length() > 0  && contactlist[i][5].length() > 0 )
			{
				snippet = contactlist[i][4] + ":\n" + contactlist[i][5];
			} 
			else if (contactlist[i][4].length() > 0  && contactlist[i][5].length() == 0 )
			{
				snippet = contactlist[i][4];
			}
			else if (contactlist[i][4].length() == 0  && contactlist[i][5].length() > 0 ) {
				snippet = contactlist[i][5];
			}
			MarkerOptions mo = new MarkerOptions().position(new LatLng(f, d)).title(contactlist[i][0] + " - " +  contactlist[i][1]).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
			this.marker = mMap.addMarker(mo);
		};
		
		MarkerOptions mo = new MarkerOptions().position(new LatLng(latitude, longitude)).title("My Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).draggable(true);
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
	
	private class printPlaces extends AsyncTask<Void, Integer, Boolean>{
		ArrayList <ArrayList> places = new ArrayList<ArrayList>();
		public LocationManager mLocationManager;
        public claseLocation mloc;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mloc = new claseLocation();
			mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			MainActivity.this.setProgressBarIndeterminateVisibility(true);
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,mloc);
		}
		@Override
		protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
				try {
					ArrayList<DataContact> contactos = new ArrayList<DataContact>();
					// DO SOMETHING
					ContactDAO contactDAO = new ContactDAO(MainActivity.this.getApplicationContext());
					contactDAO.open();
					//contactos = (ArrayList<DataContact>) contactDAO.getAllVisibleContacts();
					contactos = (ArrayList<DataContact>) contactDAO.getAllContacts();
					int i=0,cn=0;
					int lc=contactos.size();
					while(i<lc){
						int user = (Integer) BD.getContactByTelef(Integer.parseInt(contactos.get(i).getPhoneNumber())).get(0);
						ArrayList UserPlaces = BD.getLastPlaceByUser(user);
						if(!contactos.get(i).getBlock())
						{
							if(UserPlaces.size()!=0){
								places.add(UserPlaces);
								ArrayList pla = places.get(cn);
								int id=Integer.parseInt(pla.get(0).toString());
								ArrayList contact=BD.getContactByUser(id);
								pla.add(contact.get(1));
								pla.add(contact.get(2));
								cn++;
							}
						}
						i++;
					}
					//publishProgress();
					
				}
				catch(Exception ie) {
					Log.d("AsyncTask", "InterruptedException");
					return false;
				}
		return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		// Change something in the interface
			int cont = 0,cn=0;
			MarkerOptions mo;
			ArrayList contact= new ArrayList();
			contactlist=new String[places.size()][6];
			while(cont<places.size()){
				
				//Pinto la marca correspondiente a una publicacion
				if(places.get(cont).size()!=0){
					//mo = new MarkerOptions().position(new LatLng((Double) places.get(cont).get(5),(Double) places.get(cont).get(6))).title((String)places.get(cont).get(2)).snippet((String)places.get(cont).get(8)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
					//marker = mMap.addMarker(mo);
					contactlist[cn][0] = getContactName(places.get(cont).get(7).toString());
					contactlist[cn][1] = places.get(cont).get(7).toString();
					contactlist[cn][2] = places.get(cont).get(5).toString();
					contactlist[cn][3] = places.get(cont).get(6).toString();
					contactlist[cn][4] = places.get(cont).get(2).toString();
					contactlist[cn][5] = places.get(cont).get(3).toString();
					
					cn++;
				}
				cont++;
			}
			MainActivity.this.setProgressBarIndeterminateVisibility(false);
			updateMap();
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
	
	public boolean checkFirstTime() {
		final String PREFS_NAME = "MyPrefsFile";
		boolean isFirst;
		try {
			SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);		
			isFirst = preferences.getBoolean("isFirst", true);
			
		} catch (Exception e) {
			isFirst = true;
		}
		return isFirst;
	}
	
	public void firstTimeEnable(int phone) {
		final String PREFS_NAME = "MyPrefsFile";
		
		
		
		SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
		
		Editor editor = preferences.edit();
		editor.putBoolean("isFirst", false);
		editor.putInt("myPhone", phone );
		editor.commit();
		
		new getMyUserID().execute();
	}
	
	
	public void firstTimeDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle(R.string.first_time_dialog_title);
		alert.setMessage(R.string.first_time_dialog_message);

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setHint(R.string.first_time_dialog_hint);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  // Do something with value!
			EditText edittext_phone = (EditText)input;
			int phone = Integer.parseInt(edittext_phone.getText().toString());
			int[] phone_array = {phone};
			new putContactInDB().execute(phone_array);
			firstTimeEnable(phone);
		  }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		  }
		});

		alert.show();
	}
	
	private class putContactInDB extends AsyncTask<int[], Integer, Void>{
	
			
			@Override
			protected void onPreExecute() {			
				MainActivity.this.setProgressBarIndeterminateVisibility(true);
				super.onPreExecute();
			}
	
			@Override
			protected Void doInBackground(int[]... params) {
				adm_mysql_2.bd_access DB = new adm_mysql_2.bd_access("http://www.carlosexposito.es/");
				DB.putContact(params[0][0]);
				return null;
			}
	
			@Override
			protected void onPostExecute(Void result) {
				MainActivity.this.setProgressBarIndeterminateVisibility(false);
				super.onPostExecute(result);
			}
			
	}
	

	private class getMyUserID extends AsyncTask<int[], Integer, Void>{
		
		
		@Override
		protected void onPreExecute() {			
			MainActivity.this.setProgressBarIndeterminateVisibility(true);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(int[]... params) {
			ArrayList list = new ArrayList();
			final String PREFS_NAME = "MyPrefsFile";
			adm_mysql_2.bd_access DB = new adm_mysql_2.bd_access("http://www.carlosexposito.es/");
			int myPhone;
			try {
				
				SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);		
				myPhone = preferences.getInt("myPhone", 0);
				
			} catch (Exception e) {
				myPhone = 0;
			}
			
			list = DB.getContactByTelef(myPhone);
					
			SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
			
			Editor editor = preferences.edit();
			editor.putInt("myUserID", Integer.parseInt(list.get(0).toString()));
			editor.commit();
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			MainActivity.this.setProgressBarIndeterminateVisibility(false);
			super.onPostExecute(result);
		}
		
}

	
	private class putNewPlaceInDB extends AsyncTask<String[], Integer, Void> {

		protected void onPreExecute() {			
			MainActivity.this.setProgressBarIndeterminateVisibility(true);
			super.onPreExecute();
		}
		
		protected Void doInBackground(String[]... params) {
			adm_mysql_2.bd_access DB = new adm_mysql_2.bd_access("http://www.carlosexposito.es/");
			DB.putPlace(params[0][0], Double.valueOf(params[0][1]).doubleValue(), Double.valueOf(params[0][2]).doubleValue(), Integer.parseInt(params[0][3]), params[0][4]);		
			return null;
		}
		
		protected void onPostExecute(Void result) {
			MainActivity.this.setProgressBarIndeterminateVisibility(false);
			super.onPostExecute(result);
		}
	}
	
	
	/**Recibe un arrayList con los contactos de la agenda. Inserta en la BD Local los contactos que estan 
	 * en la agenda telefonica y que tienen la aplicacion FindMe instalada (lo sabe consultando al servidor)*/
	public void insertarContactosEnBDLocal(ArrayList<DataContact> contactosAgenda){
		
		bd_access bdcon = new bd_access("http://www.carlosexposito.es/");
		ContactDAO contactDAO = new ContactDAO(MainActivity.this.getApplicationContext());
		contactDAO.open();	
		int numContactosAgenda = contactosAgenda.size();
		int telef=-1;
		for(int i=0; i< numContactosAgenda; i++){
			telef = Integer.valueOf(contactosAgenda.get(i).getPhoneNumber()).intValue();
			ArrayList datos = bdcon.getContactByTelef(telef);
			
			if(datos.size()>0){
				String phone = contactosAgenda.get(i).getPhoneNumber();
				if(contactDAO.existsContactByTelf(phone) == false){ //sino existe
					int block = (contactosAgenda.get(i).getBlock()) ? 1 : 0; 
					String name = contactosAgenda.get(i).getName();
					contactDAO.create(name, phone, 0, 0);
				}
				
			}
		}
		contactDAO.close();
	}
	
	private class actualizarBDLocal extends AsyncTask<Void, Integer, Void>{

		//ProgressDialog progressDialog;
		
		protected void onPreExecute() {			
		
			//progressDialog = ProgressDialog.show(MainActivity.this,  null, getResources().getString(R.string.sync_db));
			
			if (checkFirstTime() == true){
				firstTimeDialog();
			}
			MainActivity.this.setProgressBarIndeterminateVisibility(true);
			new printPlaces().execute();
			super.onPreExecute();
		}

		protected Void doInBackground(Void... params) {
			if (checkFirstTime() == true)
			{
				Log.v("tag", "first");
				insertarContactosEnBDLocal(leerContactosAgenda());
			}
			return null;
		}
			
		protected void onPostExecute(Void result) {

			//progressDialog.cancel();
			MainActivity.this.setProgressBarIndeterminateVisibility(false);
			
			super.onPostExecute(result);
		}

		}

	/**function that return an ArrayList of DataContact of the contacts of the phonebook*/
	public ArrayList<DataContact> leerContactosAgenda(){
		
		ArrayList<DataContact> contactos = new ArrayList<DataContact>();		
		
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME};
		
		String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
				
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + "= 'ASC'";
		Cursor cursor = getContentResolver().query(uri, projection, selection, null, sortOrder);
		while (cursor.moveToNext()) {
			String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
			Uri PURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
			String CID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
			String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor pCur = getContentResolver().query(PURI,  null, CID + " = ?",  new String[]{id}, null);
			pCur.moveToNext();
			String PNUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
			String displayNumber = pCur.getString(pCur.getColumnIndex(PNUM));
			pCur.close();
			//El ultimo parametro es "block", que realmente hace referencia a la visibilidad. Por defecto, todos visibles
			DataContact c1 = new DataContact(displayName, displayNumber, false, true);
			contactos.add(c1);
	
		}
		cursor.close();
		
		contactos = Contact.formatPhoneNumbers(contactos);
		
		return contactos;
	}

	public String[] splitTitle(String title) {
		String[] titleSplit;
		titleSplit = title.split(" - ");
		return titleSplit;
	}
	
	public String getContactName(String phone) {
		String name;
		ContactDAO contactDAO = new ContactDAO(MainActivity.this);
		contactDAO.open();
		name = contactDAO.getContactNameByPhone(phone);
		contactDAO.close();
		return name;
	}
}
