package com.keg.postitnow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/** 
 * The Database class takes care of everything related to the database.
 */
public class Database {
	/** 
	 * Version of the database
	 */
	private static final int VERSION_BDD = 1;
	
	/** 
	 * Database name
	 */
	private static final String NOM_BDD = "postit";

	/** 
	 * SQLiteDatabase
	 */
	private SQLiteDatabase database;
	
	/** 
	 * Sql
	 */
	private Sql SqlDatabase;
	
	/** 
	 * Constructor of Databse
	 * 
	 * @param context the Context
	 */
	public Database(Context context){
		SqlDatabase = new Sql(context, NOM_BDD, null, VERSION_BDD);
	}
	
	
	/** 
	 * Open the database
	 */
	public void open(){
		database = SqlDatabase.getWritableDatabase();
	}


	/** 
	 * Close the database
	 */
	public void close(){
		database.close();
	}
	
	/** 
	 * Retrieve the user (using a database is not that appropriate now, but could be useful later if we want to keep track of the connexions.
	 */
	public User getUser(){
		/*
		 * getUser
		 * input: none
		 * output: User
		 * get user from database
		 */
		Cursor c = database.query("user", new String[] {"userId", "phoneNumber", "session"}, null, null, null, null, null);

		User user = new User();
		if (c.getCount() == 0) {
			ContentValues values = new ContentValues();
			values.put("phoneNumber", "");
			values.put("session", "");
			values.put("id", 0);
			values.put("userId", 0);
			database.insert("user", null, values);
			
			return user;
		}
		else {
			c.moveToFirst();
			user.update(new Long(c.getInt(0)), c.getString(1), c.getString(2));
		}
		
		c.close();
		return user;
	}


	/** 
	 * Update the user in the database
	 * 
	 * @param 	userId is the id of the user
	 * 			phoneNumber is the phone number of the user in the form of a String
	 * 			session is a String that represents the session saved on the server
	 */
	public void updateUser(Long userId, String phoneNumber, String session) {
		/*
		 * updateUser
		 * input: Long userId, String phoneNumber, String session
		 * output: none
		 * Update user in the database
		 */
		ContentValues values = new ContentValues();
		values.put("userId", userId.toString());
		values.put("phoneNumber", phoneNumber);
		values.put("session", session);
		database.update("user", values, "id=0", null);
		
	}
}
