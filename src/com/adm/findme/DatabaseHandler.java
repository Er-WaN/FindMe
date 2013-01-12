package com.adm.findme;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	public final String DBNAME = "FindMeDB";
	
	public static final String CONTACT_TABLE = "Contacts";
	public static final String CONTACT_ID = "Contact_id";
	public static final String CONTACT_NAME = "Contact_name";
	public static final String CONTACT_PHONENUMBER = "Contact_phonenumber";
	public static final String CONTACT_FAVORITE = "Contact_favorite";
	public static final String CONTACT_BLOCK = "Contact_block";
	
	public static final String GROUP_TABLE = "Groups";
	public static final String GROUP_ID = "Group_id";
	public static final String GROUP_NAME = "Group_name";
	public static final String GROUP_BLOCK = "Group_Block";
	
	public static final String CONTACT_GROUP_TABLE = "Contact_Group";
	public static final String CG_CONTACT_ID = "Contact_id";
	public static final String CG_GROUP_ID = "Group_id";
	
	public static final String CONTACT_TABLE_CREATE = "CREATE TABLE " + CONTACT_TABLE + 
			" (" + CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			CONTACT_NAME + " TEXT , " + CONTACT_PHONENUMBER + " TEXT , " + 
			CONTACT_FAVORITE + " INTEGER , " + CONTACT_BLOCK + " INTEGER "	+ ")";
	
	public static final String GROUP_TABLE_CREATE = "CREATE TABLE " + GROUP_TABLE + " (" + 
			GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			GROUP_NAME + " TEXT , " + GROUP_BLOCK + " INTEGER "	+ ")";
	
	public static final String CONTECT_GROUP_TABLE_CREATE = "CREATE TABLE " + CONTACT_GROUP_TABLE + " (" +
			CG_CONTACT_ID + " INTEGER " +
			CG_GROUP_ID + " INTEGER " +
			")";
			

	public DatabaseHandler(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create the table Contact_Table
		db.execSQL(CONTACT_TABLE_CREATE);

		// Create the table Group_Table
		db.execSQL(GROUP_TABLE_CREATE);
		
		// Create the table Contact_Group_Table
		db.execSQL(CONTECT_GROUP_TABLE_CREATE);	
		
		db.execSQL("insert into Contacts (Contact_name, Contact_phonenumber, Contact_favorite, Contact_block) values ('carlos', '123456789', 0, 0)");
		db.execSQL("insert into Contacts (Contact_name, Contact_phonenumber, Contact_favorite, Contact_block) values ('erwan', '987654321', 0, 0)");
		db.execSQL("insert into Contacts (Contact_name, Contact_phonenumber, Contact_favorite, Contact_block) values ('massimo', '147258369', 0, 0)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + GROUP_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CONTACT_GROUP_TABLE);
		onCreate(db);
	}

}
