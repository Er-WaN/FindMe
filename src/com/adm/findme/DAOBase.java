package com.adm.findme;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public abstract class DAOBase {

	protected final static int VERSION = 17;
	protected final static String NAME = "database.db";
	
	protected SQLiteDatabase mDb;
	protected DatabaseHandler mHandler;
	
	public DAOBase(Context pContext) {
		this.mHandler = new DatabaseHandler(pContext, NAME, null, VERSION);
	}
	
	public void open() throws SQLException {
		mDb = mHandler.getWritableDatabase();
	}
	
	public void close() {
		mDb.close();
	}
	
	public SQLiteDatabase getDb() {
	    return mDb;
	  }
}
