package com.keg.postitnow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;


/** 
 * Display is a class that give shortcuts to display messages (toast and alert)
 */
public class Display {
	/** 
	 * Toast a message
	 * 
	 * @param 	activity the activity from which the function is called
	 * 			message is a String and represents the message we want to display
	 */
    public static void toast(Activity activity, String message) {
		Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }


	/** 
	 * Write an alert
	 * 
	 * @param 	activity the activity from which the function is called
	 * 			titre is a String and represents the title we want to display
	 * 			message is a String and represents the message we want to display
	 */
    public static void alert(Activity activity, String titre, String errorStr) {
 	    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
 	    alertDialog.setTitle(titre);
 	    alertDialog.setMessage(errorStr);
 	    alertDialog.setButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
 	        public void onClick(DialogInterface dialog, int which) {
 	          return;
 	      } 
 	    }); 
 	    alertDialog.show();
 	}
    
}
