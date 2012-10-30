package com.keg.postitnow;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/** 
 * Sql Class
 */	
public class Sql extends SQLiteOpenHelper {

	/** 
	 * Public constructor, nothing fancy
	 * 
	 * @param 	context
	 * 			name
	 * 			factory
	 * 			version
	 */	
	public Sql(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
 
	
	
	/** Called when the activity is first created. Initialize the layout, check for session and load postits.
	 * 
	 * @param db
	 */
	public void onCreate(SQLiteDatabase db) {
		/*
		 * onCreate
		 * input: SQLiteDatabase db
		 * output: none
		 * create database
		 */
		db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY, userId INTEGER, phoneNumber TEXT, session TEXT)");
	}


	/** Delete exesting database
	 * 
	 * @param 	db
	 * 			oldVersion
	 * 			newVersion
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/*
		 * onUpgrade
		 * input: SQLiteDatabase db, int oldVersion, int newVersion
		 * output: none
		 * check Android log for details
		 */
		db.execSQL("DROP TABLE user");
		onCreate(db);
		
	}
 

}