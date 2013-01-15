package com.adm.findme;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

public class ContactDAO extends DAOBase{
	
	public static final String TABLE_NAME = "Contacts";
	public static final String ID = "_id";
	public static final String NAME = "Contact_name";
	public static final String PHONENUMBER = "Contact_phonenumber";
	public static final String FAVORITE = "Contact_favorite";
	public static final String BLOCK = "Contact_block";
	
	public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

	public ContactDAO(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}
	
	/**
     * Method to create a new contact
     * @params The name of the new Contact
     * **/
	/*public void create(DataContact contact) {
		ContentValues value = new ContentValues();
		value.put(NAME, contact.getName());
		value.put(PHONENUMBER, contact.getPhoneNumber());
		value.put(FAVORITE, false);
		value.put(BLOCK, false);
		mDb.insert(TABLE_NAME, null, value);
	}*/
	
	public void create(String name, String phone, int fav, int block) {
		ContentValues value = new ContentValues();
		value.put(NAME, name);
		value.put(PHONENUMBER, phone);
		value.put(FAVORITE, fav);
		value.put(BLOCK, block);
		
		mDb.insert(TABLE_NAME, null, value);
		
	}
	
	public void favorite(DataContact contact) {
		ContentValues value = new ContentValues();
		value.put(BLOCK, !contact.getFavorite());
		mDb.update(TABLE_NAME, value, ID  + " = ?", new String[] {String.valueOf(contact.getId())});
	}
	
	public void block(DataContact contact) {
		ContentValues value = new ContentValues();
		value.put(BLOCK, !contact.getBlock());
		mDb.update(TABLE_NAME, value, ID  + " = ?", new String[] {String.valueOf(contact.getId())});
	}
	
	public List<DataContact> getAllContacts() {
		List<DataContact> contactList = new ArrayList<DataContact>();
		
		Cursor cursor = mDb.rawQuery("SELECT * FROM Contacts", null);
		
		if (cursor.moveToFirst()) {
			do {
				DataContact contact = new DataContact();
				contact.setId(cursor.getInt(0));
				contact.setName(cursor.getString(1));
				contact.setPhoneNumber(cursor.getString(2));
				contact.setFavorite(cursor.getInt(3) == 0 ? true : false);
				contact.setBlock(cursor.getInt(4) == 0 ? true : false);
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return contactList;
	}
	
	public ArrayList<String> getAllContactsName() {
		ArrayList<String> contactList = new ArrayList<String>();
		
		Cursor cursor = mDb.rawQuery("SELECT * FROM Contacts", null);
		
		if (cursor.moveToFirst()) {
			do {
				contactList.add(cursor.getString(1));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return contactList;
	}
	
	
	public boolean existsContactByTelf(String telef) {
		
		Cursor cursor = mDb.rawQuery("SELECT * FROM Contacts WHERE Contact_phonenumber = '" + telef + "';", null);

		if (cursor.moveToFirst()){
			cursor.close();
			return true;
		}
		else {
			cursor.close();
			return false;
		}	
		
	}
	
	public int getNumberOfContacts() {
		Cursor cursor;
		int nb;
		cursor = mDb.rawQuery("SELECT _id AS nb FROM Contacts", null);
		nb = cursor.getInt(0);
		cursor.close();
		return nb;
	}
	
	public int getIdContact(String contact_name) {
		int contact_id;
		Cursor cursor;
		cursor = mDb.rawQuery("SELECT _id FROM Contacts WHERE Contact_name = ?", new String[] {contact_name});
		contact_id = cursor.getInt(0);
		cursor.close();
		return contact_id;
	}
	
	/**
     * Method to enable block for a contact.
     * @param String the name of the contact.
     * **/
	public void enableBlock(String contact_name) {
		ContentValues value = new ContentValues();
		value.put(BLOCK, 1);
//		mDb.update(TABLE_NAME, value, NAME + " = ? ", new String[] {contact_name});
		mDb.execSQL("UPDATE " + TABLE_NAME + " SET " + BLOCK + "=1 WHERE " + NAME + "= '" + contact_name + "' ;" );
	}
	
	/**
     * Method to disable block for a contact.
     * @param String The name of the contact.
     * **/
	public void disableBlock(String contact_name) {
		ContentValues value = new ContentValues();
		value.put(BLOCK, 0);
//		mDb.update(TABLE_NAME, value, NAME + " = ? ", new String[] {contact_name});
		mDb.execSQL("UPDATE " + TABLE_NAME + " SET " + BLOCK + "=0 WHERE " + NAME + "= '" + contact_name + "' ;" );
	}

}
