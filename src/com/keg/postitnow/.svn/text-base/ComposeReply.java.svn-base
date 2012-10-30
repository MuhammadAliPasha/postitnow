package com.keg.postitnow;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;



/**
 * Compose ComposeReply is used to write a reply to a postit
 */
public class ComposeReply extends Activity {
	//private int activityCode = 4;

	/**
	 * The user with his phone number and last session
	 */
	private User user = new User();
	
    /**
     * Boolean that precise if we are already sending data to the server
     */
	private boolean sending = false;

    /**
     * The id of the postit using a String. An Integer would be better... a primitive even better...
     */
	private String idPostit = "";

	
    /**
     * The message sent to the server
     */
	String message = "";
	

	
	/** Called when the activity is first created. Initialize the layout.
	 * 
	 * @param savedInstanceState Bundle
	 * 
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setResult(1); // do not quit
    	
		/// get session
		Bundle objetbunble  = this.getIntent().getExtras();
        if (objetbunble != null && objetbunble.containsKey("session") && objetbunble.containsKey("idPostit")) {
        	user.updateSession(this.getIntent().getStringExtra("session"));
        	idPostit = this.getIntent().getStringExtra("idPostit");
        }
        else {
			Display.toast(ComposeReply.this, "An error occured, please log in again");
			finish();
        }

        
        
    	setContentView(R.layout.compose_reply);	
		

     	
     	ImageView postit = (ImageView) findViewById(R.id.postit);
     	postit.setOnClickListener(new View.OnClickListener() {
 			public void onClick(View drawable) {
 				AutoCompleteTextView message = (AutoCompleteTextView) findViewById(R.id.message);
 				message.setText("");
 				RadioButton happy = (RadioButton) findViewById(R.id.happy);
 				happy.setChecked(false);
 				RadioButton sad = (RadioButton) findViewById(R.id.sad);
 				sad.setChecked(false);
 				
 			}
 		});


     	


        
        
        // add listener on post button
        Button home = (Button) findViewById(R.id.post);
 		home.setOnClickListener(new View.OnClickListener() {
 			public void onClick(View drawable) {
        		if (sending == false) {
        			sending = true;
	
	        		EditText messageInput = (EditText) findViewById(R.id.message);
	            	message = messageInput.getText().toString();
	            	
        	        Thread t = new Thread() // thread to load the data (discharge the UI thread
        	        {
        	        	public void run() {
                			sending = true;
        		            String answer = Server.reply(ComposeReply.this, user.returnSession(), message, idPostit);
        		            analyze(answer);
        	        	}
        	        };
        	        t.start();
        		}
        		else {
            		Display.toast(ComposeReply.this, ComposeReply.this.getString(R.string.sending));
        		}
 			}
 			
 	 		private void analyze(String answer) {
            	JSONObject jObject;
            	String response = "";
            	try {
					jObject = new JSONObject(answer);
					response = jObject.getString("type");
				}
            	catch (JSONException e) {
					//Display.toast(ComposeReply.this, e.toString());
					e.printStackTrace();
				}
            	
            	if (response.compareTo("ok") == 0) {
    				messageHandler.sendMessage(Message.obtain(messageHandler, 0));
            	}
            	else {
					messageHandler.sendMessage(Message.obtain(messageHandler, 1));
            	}

 	 		}

 	 		private Handler messageHandler = new Handler() {
 	 			/* (non-Javadoc)
 	 			 * @see android.os.Handler#handleMessage(android.os.Message)
 	 			 */
 	 			public void handleMessage(Message msg) {  
 	 				if (msg.what == 0) {
 	            		Display.toast(ComposeReply.this, ComposeReply.this.getString(R.string.message_sent));
 	        			sending = false;
 	 					finish();
 	 				}
 	 				else {
            			Display.toast(ComposeReply.this, ComposeReply.this.getString(R.string.message_not_sent));
            			sending = false;
 	 				}
 	 			}
 	 		};
 	 		
 		});
 		
 		

    }
    

}
