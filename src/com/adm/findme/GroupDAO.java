package com.adm.findme;

import android.content.ContentValues;
import android.content.Context;

public class GroupDAO extends DAOBase{
	
	public static final String TABLE_NAME = "Group_Table";
	public static final String ID = "Group_id";
	public static final String NAME = "Group_name";
	public static final String BLOCK = "Group_block";
	 	 
	public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

	public GroupDAO(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}

	/**
     * Method to create a new group
     * @params The name of the new Group
     * **/
	public void create(String name) {
		ContentValues value = new ContentValues();
		   value.put(GroupDAO.NAME, name);
		   mDb.insert(TABLE_NAME, null, value);

	}
	
	public void read(DataGroup group) {
		
	}
	
	/**
     * Method to delete a Group
     * @params Object DataGroup
     * **/
	public void delete(DataGroup group) {
		mDb.delete(TABLE_NAME, ID + " = ?", new String[] {String.valueOf(group.getId())});
	}
	
	/**
     * Method to invert the field block for a group
     * @params Object DataGroup
     * **/
	public void block(DataGroup group) {
		ContentValues value = new ContentValues();
		value.put(GroupDAO.BLOCK, !group.getBlock());
		mDb.update(TABLE_NAME, value, ID  + " = ?", new String[] {String.valueOf(group.getId())});
	}
	
}
