package com.adm.findme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;


public class Contact extends android.support.v4.app.FragmentActivity implements ShareDialog.ShareDialogListener{

	ArrayList<String> names = new ArrayList<String>();
	List<DataContact> contactos = new ArrayList<DataContact>();
	List<DataGroup> grupos = new ArrayList<DataGroup>();
	TabHost host; 
	ArrayAdapter<String> adapter_contact;
	ArrayAdapter<String> adapter_group;
	private ContactDAO contactsource;
	private GroupDAO groupsource;
	private ListAdapter listContacts;
	private ListAdapter listgroups;
	
	/**Lee los nombres de los contactos de la agenda telef�nica (SOLO DE AQUELLOS QUE TIENEN NUMERO DE TEL�FONO) y devuelve un ArrayList 
	 * de objetos de clase Contacto (es decir, con nombre y numero de telefono). SOLO 1 NUMERO DE TELEFONO POR CADA CONTACTO. No recibe nada como par�metro. 
	 * @author Carexcer
	 * @return ArrayList<Contacto> con los nombres y los telefonos de la agenda del tel�fono.
	 * */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		contactsource = new ContactDAO(this);
		contactsource.open();
		contactos = contactsource.getAllContacts();
		
		List<String> contactsList = new ArrayList<String>();
		
		for (int i = 0; i < contactos.size(); i++) {
			contactsList.add(contactos.get(i).getName());
		}
		
		groupsource = new GroupDAO(this);
		groupsource.open();
		grupos = groupsource.getAllgroups();
		
		Toast.makeText(this, "groups: "+grupos.size(), Toast.LENGTH_SHORT).show();
		
		List<String> groupsList = new ArrayList<String>();
		
		for (int i = 0; i < grupos.size(); i++) {
			groupsList.add(grupos.get(i).getName());
		}
		
		
		//listContacts = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactsList);

		//contactos = leerContactos();
		
		ListView listViewContacts = (ListView) findViewById(R.id.listViewContacts);
		ListView listViewGroups = (ListView) findViewById(R.id.listViewGroups);
		EditText editTextSearchContact = (EditText) findViewById(R.id.searchEditText);
		
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
		
		adapter_contact = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contactsList);	
		adapter_group = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,groupsList);	

		listViewContacts.setAdapter(adapter_contact);
		listViewGroups.setAdapter(adapter_group);	
		
		listViewContacts.setTextFilterEnabled(true);
		editTextSearchContact.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Contact.this.adapter_contact.getFilter().filter(s);
				Contact.this.adapter_group.getFilter().filter(s);
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub	
			}
			
		}
		);
	}
	
	public ArrayList<DataContact> leerContactos(){
		
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
			DataContact c1 = new DataContact(displayName, displayNumber, false, false);
			contactos.add(c1);
			names.add(displayName);
		}
		cursor.close();
		
		return contactos;
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_contact, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		int tab = host.getCurrentTab();
				if (tab==1)
					menu.add(0, Menu.FIRST, 0, R.string.menu_newGroup);
				else
					menu.add(0, Menu.FIRST, 0, R.string.menu_update);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (host.getCurrentTab()) {
		/*case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;*/
		case 0:
			Toast.makeText(this, "pulsado item update", Toast.LENGTH_SHORT).show();
			return true;
		case 1:
			DialogFragment dialog = new CreateGroupDialog();
	        dialog.show(getSupportFragmentManager(), "CreateGroupDialog");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub
		
	}	
}
