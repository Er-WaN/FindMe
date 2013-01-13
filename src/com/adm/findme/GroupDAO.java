package com.adm.findme;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class GroupDAO extends DAOBase {

	public static final String TABLE_NAME = "Groups";
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
	
	public List<DataGroup> getAllgroups() {
		List<DataGroup> groupList = new ArrayList<DataGroup>();
		
		Cursor cursor = mDb.rawQuery("SELECT * FROM Groups", null);
		
		if (cursor.moveToFirst()) {
			do {
				DataGroup group = new DataGroup();
				group.setId(cursor.getInt(0));
				group.setName(cursor.getString(1));
				group.setBlock(cursor.getInt(2) == 0 ? true : false);
				groupList.add(group);
			} while (cursor.moveToNext());
		}
		return groupList;
	}
	
	public List<String> getAllGroupsName() {
		List<String> groupsListName = new ArrayList<String>();
		List<DataGroup> groupList = new ArrayList<DataGroup>();
		
		groupList = this.getAllgroups();
		
			for (int i = 0; i < groupList.size(); i++) {
				groupsListName.add(groupList.get(i).getName());
			}
		return groupsListName;
	}
	
	public Cursor getAllGroupsNameCursor() {
		Cursor cursor = mDb.rawQuery("SELECT _id, Group_name, Group_block FROM Groups", null);
		return cursor;
	}
	
}
