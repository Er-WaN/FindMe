package com.adm.findme;

import java.util.ArrayList;
import java.util.List;
import android.util.*;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.widget.Toast;

/**
 * This class contains all the methods to manage the table Group
 * 
 * @author Erwan Thouvenot
 * **/
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
     * @param The name of the new Group
     * **/
	public void create(String name) {
		ContentValues value = new ContentValues();
		value.put(GroupDAO.NAME, name);
		value.put(GroupDAO.BLOCK, 0);
		mDb.insert(TABLE_NAME, null, value);
	}
	
	public void read(DataGroup group) {
		
	}
	
	/**
     * Method to delete a Group
     * @param Object DataGroup
     * **/
	public void delete(DataGroup group) {
		mDb.delete(TABLE_NAME, ID + " = ?", new String[] {String.valueOf(group.getId())});
	}
	
	/**
     * Method to invert the field block for a group
     * @param Object DataGroup
     * **/
	public void block(DataGroup group) {
		ContentValues value = new ContentValues();
		value.put(GroupDAO.BLOCK, !group.getBlock());
		mDb.update(TABLE_NAME, value, ID  + " = ?", new String[] {String.valueOf(group.getId())});
	}
	
	/**
     * Method to know if a group is block.
     * @param group_name String, the name of the group
     * @return isBlock Boolean, if true the group is block. 
     * **/
	public boolean getIfBlock(String group_name) {
		Cursor cursor;
		boolean isBlock;
		cursor = mDb.rawQuery("SELECT Group_block FROM Groups WHERE Group_name = ?", new String[] {group_name});
		cursor.moveToFirst();
		isBlock = (cursor.getInt(0) == 0 ? false : true);
		cursor.close();
		return isBlock;
	}
	
	/**
     * Method to know if a group is block.
     * @param group_id int, the id of the group
     * @return isBlock Boolean, if true the group is block. 
     * **/
	public boolean getIfBlock(int group_id) {
		Cursor cursor;
		boolean isBlock;
		cursor = mDb.rawQuery("SELECT Group_block FROM Groups WHERE _id = " + group_id, null);
		cursor.moveToFirst();
		isBlock = (cursor.getInt(0) == 0 ? false : true);
		cursor.close();
		return isBlock;
	}
	
	/**
     * Method to enable block for a group.
     * @param String the name of the group.
     * **/
	public void enableBlock(String group_name) {
		ContentValues value = new ContentValues();
		value.put(BLOCK, 1);
		mDb.update(TABLE_NAME, value, NAME + " = ? ", new String[] {group_name});
	}
	
	/**
     * Method to disable block for a group.
     * @param String The name of the group.
     * **/
	public void disableBlock(String group_name) {
		ContentValues value = new ContentValues();
		value.put(BLOCK, 0);
		mDb.update(TABLE_NAME, value, NAME + " = ? ", new String[] {group_name});
	}
	
	/**
     * Method to get the id of the last group created in the database.
     * @return List List of DataGroup. 
     * **/
	public List<DataGroup> getAllgroups() {
		List<DataGroup> groupList = new ArrayList<DataGroup>();
		
		Cursor cursor = mDb.rawQuery("SELECT * FROM Groups", null);
		
		if (cursor.moveToFirst()) {
			do {
				DataGroup group = new DataGroup();
				group.setId(cursor.getInt(0));
				group.setName(cursor.getString(1));
				group.setBlock(cursor.getInt(2) == 0 ? false : true);
				groupList.add(group);
			} while (cursor.moveToNext());
			
		}
		cursor.close();
		return groupList;
	}
	/**
     * Method to get the id of the last group created in the database.
     * @return groupsListName The list with the name of all groups.
     * **/
	public List<String> getAllGroupsName() {
		List<String> groupsListName = new ArrayList<String>();
		List<DataGroup> groupList = new ArrayList<DataGroup>();
		
		groupList = this.getAllgroups();
		
			for (int i = 0; i < groupList.size(); i++) {
				groupsListName.add(groupList.get(i).getName());
			}
		return groupsListName;
	}
	/**
     * Method to get all the groups in the database.
     * @return cursor The cursor with all the groups.
     * **/
	public Cursor getAllGroupsNameCursor() {
		Cursor cursor = mDb.rawQuery("SELECT _id, Group_name, Group_block FROM Groups", null);
		return cursor;
	}
	
	/**
     * Method to get the id of a group according to it name.
     * @return id The id of the group.
     * @param group_name The name of the group we want to know the id.
     * **/
	public int getGroupId(String group_name){
		int id;
		Cursor cursor;
		
		cursor = mDb.rawQuery("SELECT _id FROM Groups WHERE Group_name = " + group_name, null);
		id = cursor.getInt(0);
		cursor.close();
		return id;
	}
	
	/**
     * Method to get the id of the last group created in the database.
     * @return id The id of the last group create in the database.
     * **/
	public int getLastGroupId() {
		Cursor cursor;
		int id;
		cursor = mDb.rawQuery("SELECT _id FROM Groups ORDER BY _id DESC LIMIT 1", null);
		if (cursor.moveToFirst()) {
		}
		id = cursor.getInt(0);
		cursor.close();
		return id;
	}
	
	/**
     * Method to delete the last group in the database.
     * 
     * **/
	public void deleteLastGroup() {
		mDb.execSQL("DELETE FROM Groups WHERE _id IN (SELECT _id FROM Groups ORDER BY _id DESC LIMIT 1");
	}
	
	/**
     * Method to delete a group.
     * @params group_id int, the id of the group.
     * 
     * **/
	public void deleteGroup(int group_id) {
		mDb.execSQL("DELETE FROM Groups WHERE _id = " + group_id);
	}
	
	/**
     * Method to delete a group.
     * @params group_name String, the bame of the group.
     * 
     * **/
	public void deleteGroup(String name) {
		mDb.delete(TABLE_NAME, NAME + " = ? ", new String[] {name});
	}
	
	public boolean checkIfGroupExist(String group_name) {
		Cursor cursor;
		boolean exists;
		cursor = mDb.rawQuery("SELECT 1 FROM Groups WHERE Group_name = ?" , new String[] {group_name});
		exists = (cursor.getCount() > 0);
		cursor.close();
		return exists;
	}
	
}
