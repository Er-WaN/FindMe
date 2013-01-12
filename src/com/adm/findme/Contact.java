package com.adm.findme;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class Contact extends Activity {

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<DataContact> contactos = new ArrayList<DataContact>();
	TabHost host; 
	ArrayAdapter<String> adapter;
	ListView listViewContacts;
	ListView listViewGroups; 
	
	/**Lee los nombres de los contactos de la agenda telefonica (SOLO DE AQUELLOS QUE TIENEN NUMERO DE TELEFONO) y devuelve un ArrayList 
	 * de objetos de clase Contacto (es decir, con nombre y numero de telefono). SOLO 1 NUMERO DE TELEFONO POR CADA CONTACTO. No recibe nada como parametro. 
	 * @author Carexcer
	 * @return ArrayList<Contacto> con los nombres y los telefonos de la agenda del telefono.
	 * */
	public ArrayList<DataContact> leerContactos(){
		
		ArrayList<DataContact> contactos = new ArrayList<DataContact>();		
		
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID,ContactsContract.Contacts.DISPLAY_NAME};
		
		String selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
				
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
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
			DataContact c1 = new DataContact(displayName, displayNumber);
			contactos.add(c1);
			names.add(displayName);
		}
		cursor.close(); 
		
		return contactos;
		
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		/*Se define todo lo que es la interfaz de la activity*/
		setContentView(R.layout.activity_contact);
		
		listViewContacts = (ListView) findViewById(R.id.listViewContacts);
		listViewGroups = (ListView) findViewById(R.id.listViewGroups);
		
		listViewContacts.setTextFilterEnabled(true);
		listViewGroups.setTextFilterEnabled(true);
		
		host = (TabHost) findViewById(R.id.tabHost); 
		host.setup(); 
		
		TabSpec spec = host.newTabSpec("TABCONTACTS"); 
		spec.setIndicator("Contactos"); 
		spec.setContent(R.id.listViewContacts); 
		host.addTab(spec); 
		
		spec = host.newTabSpec("TABGROUPS"); 
		spec.setIndicator("Grupos"); 
		 
		spec.setContent(R.id.listViewGroups); 
		host.addTab(spec); 
		host.setCurrentTabByTag("TABCONTACTS");
		
		/*Se leen los contactos del telefono y se muestran en pantalla a través de AsyncTask*/
		new LeerContactosAsyncTask().execute();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_contact, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		int tab = host.getCurrentTab();
		if (tab==0){
			menu.getItem(0).setVisible(true);
			menu.getItem(1).setVisible(false);
		}else{
			menu.getItem(1).setVisible(true);
			menu.getItem(0).setVisible(false);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {	

		case R.id.menu_update:
			Toast.makeText(Contact.this, "pulsado item update", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.menu_newGroup:
			Toast.makeText(Contact.this, "pulsado item new group", Toast.LENGTH_SHORT).show();
			return true;
			default:
				Toast.makeText(Contact.this, "Algo ha ido mal al clickar en el menu", Toast.LENGTH_LONG).show();
		}
		return super.onOptionsItemSelected(item);
	}

	private class LeerContactosAsyncTask extends AsyncTask<Void, Integer, Void>{

		
		@Override
		protected void onPreExecute() {			
			Contact.this.setProgressBarIndeterminateVisibility(true);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			leerContactos();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			adapter = new ArrayAdapter<String>(Contact.this,android.R.layout.simple_list_item_1,names);
			listViewContacts.setAdapter(adapter);
			listViewGroups.setAdapter(adapter);
			Contact.this.setProgressBarIndeterminateVisibility(false);
			super.onPostExecute(result);
		}
		
	}
	
}
