package com.keg.postitnow;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/** 
 * The login activity display an interface for the user to write its phone number and its password
 *
 */
public class Login extends Activity {
	/** 
	 * Activity code of the activity
	 */
	private int activityCode = 1;

	/** Called when the activity is first created. Initialize the layout.
	 * 
	 * @param savedInstanceState Bundle
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/* onCreate
    	 * Input: Bundle savedInstanceState
    	 * Output: none
    	 * Setup the login/password layout or send to Home activity
    	 */
    	super.onCreate(savedInstanceState);
    	setResult(1);
    	setup();
    }
    
	/** Called when an activity you launched exits.
	 * It might exit the application if the request code is equal to 0. 
	 * 
	 * @param 	requestCode is an integer (0 if the program should exit, 1 if not)
	 * 			resultCode is an integer 
	 * 			data sent from the closed activity
	 */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	/*
    	 * onActivityResult
    	 * input: int requestCode, int resultCode, Intent data
    	 * output: none
    	 * Triggered when the user come back on the Home activity. We just check if we need to close it or no.
    	 */
    	if (resultCode == 0) {
    		setResult(0);
        	finish(); // on press back button on Home.
        }
    	else if ((requestCode == 1) && (resultCode == 3)) {
    		setResult(0);
    		//finish(); // on log out
        }
    	else {
    		setup();
    	}
    }

	/** 
	 * Set up the layout and the listeners
	 */
    private void setup() {
		setContentView(R.layout.login);
		
		
		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new Button.OnClickListener() { 
			public void onClick (View v) {
        		EditText phoneNumberInput = (EditText) findViewById(R.id.phoneNumber);
            	String phoneNumber = phoneNumberInput.getText().toString();
            	//String phoneNumber = "";  //for developper

        		EditText passwordInput = (EditText) findViewById(R.id.password);
            	String password = Md5.encode(passwordInput.getText().toString());
            	//String password = Md5.encode(""); // for developper
            		            	
            	String answer = Server.login(Login.this, phoneNumber, password);
            	if (answer != "") {
	            	JSONObject jObject;
	            	String response = "";
	            	String session = "";
	            	Long userId = new Long(0);
	            	try {
						jObject = new JSONObject(answer);
						response = jObject.getString("type");
						session = jObject.getJSONObject("message").getString("session_id");
						userId = new Long(jObject.getJSONObject("message").getInt("user_id"));
						

	
					} catch (JSONException e) {
						//Display.toast(Login.this, e.toString());
						e.printStackTrace();
					}
	
					if (response.compareTo("ok") == 0) {
						setResult(0);
				    	Database database = new Database(Login.this);
						database.open();
				        database.updateUser(userId, phoneNumber, session);
						database.close();
	
						Bundle objetbunble = new Bundle();
						objetbunble.putString("phoneNumber", phoneNumber);
						objetbunble.putString("session", session);
						objetbunble.putString("userId", userId.toString());
			 
						Intent intent = new Intent(Login.this, Home.class);
						intent.putExtras(objetbunble);
						startActivityForResult(intent, activityCode);
					}
					else {
						Display.toast(Login.this, Login.this.getString(R.string.authentification_fail));
					}
            	}
            	else {
					Display.toast(Login.this, Login.this.getString(R.string.network_problem));
            	}
            	
			}
		});

    }
    
}