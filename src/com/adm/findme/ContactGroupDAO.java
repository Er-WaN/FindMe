package com.adm.findme;


import android.content.ContentValues;
import android.content.Context;

/**
 * This class contains all the methods to manage the table Group.
 * 
 * @author Erwan Thouvenot
 * **/
public class ContactGroupDAO extends DAOBase {

	public static final String TABLE_NAME = "Contact_Group";
	public static final String CONTACT_ID = "Contact_id";
	public static final String GROUP_ID = "Group_id";
	
	public ContactGroupDAO(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}
	
	/**
     * Method to insert in the table Contact_Group a new line with a Contact_id and a Group_id.
     * @param group_id The id of the group.
     * @param contact_id The id of the contact.
     * **/
	public void insertContactIntoGroup(int group_id, int contact_id) {
		ContentValues value = new ContentValues();
		value.put(CONTACT_ID, contact_id);
		value.put(GROUP_ID, group_id);
		mDb.insert(TABLE_NAME, null, value);
	}
	

}
