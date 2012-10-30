package com.keg.postitnow;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/**
 * Home activity is the main Activity, it displays the last postits by friends 
 */
public class Home extends Activity {
	/**
	 * The number of postits displayed
	 */
	Long count = new Long(12);
	
	/**
	 * Postits returned should be smaller than smartId. Strange parameters of the API. Integer.MAX_VALUES should be used here.
	 */
	Long startId = new Long(10000000);
	
	/**
	 * Activity code
	 */
	private int activityCode = 2;
	
	/**
	 * The user with his phone number and last session
	 */
	private User user = new User();

	/**
	 * Id of the postit that should be deleted
	 */
	private String idPostit;


	/**
	 * Answer retrieved from the server
	 */
	private String answer;
	
	/**
	 * List of the post it we want to display
	 */
	ArrayList<Postit> listPostit = new ArrayList<Postit>();
	
	/**
	 * List of Hashmap that is used for the adapter
	 */
	private ArrayList<HashMap<String, String> > posts = new ArrayList<HashMap<String, String> >();
	
	/**
	 * Hashmap of avatars. It is used to avoid downloading more than once the same image
	 */
	private HashMap<String, Drawable> avatar = new HashMap<String, Drawable>();
	
	/**
	 * The adapter that is used to differentiate postits and replies
	 */
    private PostitAdapter adapter;
    
    /**
     * The main list view
     */
    private ListView listView;

    /**
     * Boolean that precise if we are already loading data from the server
     */
	private boolean loading = false;
	
	/**
	 * To make sure that we start just once the service. A singleton would be better...
	 */
	private boolean serviceStarted = false;


	/**
	 * The image view that will display the loading animation
	 */
	private ImageView imgLoading;
	
	/**
	 * The animation loading drawable
	 */
	private AnimationDrawable animation;
	class Starter implements Runnable {
		public void run() {
			animation.start();
		}
    }
	
	
    
	/** Called when the activity is first created. Initialize the layout, check for session and load postits.
	 * 
	 * @param savedInstanceState Bundle
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setResult(0); // will close if the user press back button


    	
		setContentView(R.layout.home);
		registerForContextMenu(findViewById(R.id.list));
		
	    adapter = new PostitAdapter(Home.this, posts, avatar);
	    listView = (ListView) findViewById(R.id.list);
	    listView.setDivider( null ); 
	    listView.setDividerHeight(0); 
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new onScrollLoad());

		// build the animation
		animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.loading0), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.loading1), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.loading2), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.loading3), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.loading4), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.loading5), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.loading6), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.loading7), 100);
        animation.setOneShot(false);

        imgLoading = (ImageView) findViewById(R.id.loading_view);
        imgLoading.setBackgroundDrawable(animation);
        imgLoading.setVisibility(ImageView.VISIBLE);
        imgLoading.post(new Starter());
		

        // check if some information have been sent via the precedent activity
		Bundle objetbunble  = this.getIntent().getExtras();
        if (objetbunble != null && objetbunble.containsKey("phoneNumber") && objetbunble.containsKey("session") && objetbunble.containsKey("userId")) {
        	user.updatePhoneNumber(this.getIntent().getStringExtra("phoneNumber"));
        	user.updateSession(this.getIntent().getStringExtra("session"));
        	user.updateUserId(this.getIntent().getStringExtra("userId"));
        }
        else { // if not, load user from database
	    	Database database = new Database(this);
			database.open();
	        user = database.getUser();
        }

		if (user.returnPhoneNumber() == "") { // if the user is not log in, send him on the Login activity
	    	setResult(1);
			Bundle objetbunbleSend = new Bundle();
			Intent intent = new Intent(Home.this, Login.class);
			intent.putExtras(objetbunbleSend);
			startActivityForResult(intent, activityCode);
		}
		else {
			// check user session valid ?

			// Attach onClickListener for the icon (compose)
			ImageView postit = (ImageView) findViewById(R.id.postit);
	     	postit.setOnClickListener(new View.OnClickListener() {
	 			public void onClick(View drawable) {
					Bundle objetbunble = new Bundle();
					objetbunble.putString("phoneNumber", user.returnPhoneNumber());
					objetbunble.putString("session", user.returnSession());
		 
					Intent intent = new Intent(Home.this, Compose.class);
					intent.putExtras(objetbunble);
					startActivityForResult(intent, activityCode);
	
	 			}
	 		});
	     	
	     	if (serviceStarted == false) {
		     	serviceStarted = true;
		    	startService(new Intent(Home.this, Location.class));
	     	}
	     		     	
	     	// display postit
	 		setupPostit();

		}

    }

    
    // class to load old messages when the user scroll to the bottom
	/** onScrollLoad Class implements OnScrollListener.
	 * 
	 * Detect when the user reaches the bottom of the messages and load more
	 */
    public class onScrollLoad implements OnScrollListener {
		private int visibleThreshold = 2; // how many post 
		private int previousTotal = 0;
		private boolean loading = true;
		
		
		/** onScroll method, call add() when the bottom is almost reached (visibleThreshold)
		 * 
		 * @param 	view
		 * 			firstVisibleItem is an integer and represents the first visible item
		 * 			visibleItemCount is an integer and represents the number of elements visible on the screen
		 * 			totalItemCount is an integer and represents the total number of elements in the listview
		 */
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			if (loading) {
				if (totalItemCount > previousTotal) {
					loading = false;
					previousTotal = totalItemCount;
				}
			}
			if (!loading && (visibleItemCount != 0) && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				add();
			}
		}
		
		/** onScrollStateChanged method, must be overridden. It does nothing here
		 * 
		 * @param 	view
		 * 			scrollState is an integer 
		 */
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// does nothing
		}
	}
    
    
	/** Called when an activity you launched exits.
	 * It might exit the application if the request code is equal to 0. 
	 * 
	 * @param 	requestCode is an integer (0 if the program should exit, 1 if not)
	 * 			resultCode is an integer 
	 * 			data sent from the closed activity
	 */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (resultCode == 0) {
			setResult(0); // logout > quit
        	finish();
        }
    	else {

    		Bundle objetbunble  = this.getIntent().getExtras();
	        if (objetbunble != null && objetbunble.containsKey("phoneNumber") && objetbunble.containsKey("session") && objetbunble.containsKey("userId")) {
	        	user.updatePhoneNumber(this.getIntent().getStringExtra("phoneNumber"));
	        	user.updateSession(this.getIntent().getStringExtra("session"));
	        	user.updateUserId(this.getIntent().getStringExtra("userId"));
	        }
	        else { // if not, load user from database
		    	Database database = new Database(this);
				database.open();
		        user = database.getUser();
	        }
	
			if (user.returnPhoneNumber() == "") { // if the user is not log in, send him on the Login activity
		    	setResult(1);
				Bundle objetbunbleSend = new Bundle();
				Intent intent = new Intent(Home.this, Login.class);
				intent.putExtras(objetbunbleSend);
				startActivityForResult(intent, activityCode);
			}
			else {
				restartPostit();
			}
    	}
    }
		


	/** 
	 * setupPostit is the main method. It send the query to the server and the display of postits.
	 */
    private void setupPostit() {
    	if (loading == false ) {
	    	loading = true;
	    	// display loading animation
	    	imgLoading.setVisibility(ImageView.VISIBLE);
	    	listPostit.clear();
	
	        Thread t = new Thread() // thread to load the data (discharge the UI thread
	        {
	        	public void run() {
	            	answer = Server.getStatus(Home.this, user.returnSession(), count, startId);
	            	
	        		parseAnswer(answer);
	        	}
	        };
	        t.start();
    	}
    }
    
	/** 
	 * Clean old displayed postits and display new ones.
	 */    
    private void restartPostit() {
    	if (loading == false ) {
	    	loading = true;

	    	// display loading animation
	    	imgLoading.setVisibility(ImageView.VISIBLE);
	    	listPostit.clear();
	    	posts.clear();
	    	startId = new Long(10000000);
	 		
	        
	 		
	        Thread t = new Thread() // thread to load the data (discharge the UI thread
	        {
	        	public void run() {
	            	answer = Server.getStatus(Home.this, user.returnSession(), count, startId);
	            	
	        		parseAnswer(answer);
	        	}
	        };
	        t.start();
    	}
    }
        
	/** Parse the answer of the server retrieved.
	 * 
	 * @param 	answer is a string using JSON format. It is the string sent back by the server
	 */    
    private void parseAnswer(String answer) {
    	JSONObject jObject;
    	String response = "";
    	try {
			jObject = new JSONObject(answer);
			response = jObject.getString("type");

			if (response.compareTo("ok") == 0) {
				JSONArray responses = jObject.getJSONArray("message"); 
				
				// Object programation. It might be useful later ... For efficiency, we can directly send informations
				for (int i = 0; i < responses.length(); i++) {					
					Long id = new Long(responses.getJSONObject(i).getInt("id"));
					
					String content = responses.getJSONObject(i).getString("content").toString();
					String mood = responses.getJSONObject(i).getString("mood").toString();
					String name = responses.getJSONObject(i).getString("name").toString();

					String image = responses.getJSONObject(i).getString("image").toString();
					if (avatar.containsKey(image) == false) {
						Drawable imageDrawable = ImageOperations(null, image);
						avatar.put(image, imageDrawable);
					}

					String date = parseDate(responses.getJSONObject(i).getString("date").toString());
					Long userId = new Long(responses.getJSONObject(i).getInt("user_id"));
					Postit newPost = new Postit(id, content, mood, name, image, date, userId);

					JSONArray replies = responses.getJSONObject(i).getJSONArray("replies"); 
					for (int j = 0; j < replies.length(); j++) {
	                    Long idReply = new Long(replies.getJSONObject(j).getInt("id"));
	                    Long userIdReply = new Long(replies.getJSONObject(j).getInt("user_id"));
	                    String contentReply = replies.getJSONObject(j).getString("content").toString();
	                    String nameReply = replies.getJSONObject(j).getString("name").toString();

	                    String imageReply = replies.getJSONObject(j).getString("image").toString();
						if (avatar.containsKey(imageReply) == false) {
							Drawable imageReplyDrawable = ImageOperations(null, imageReply);
							avatar.put(imageReply, imageReplyDrawable);
						}

						String dateReply = parseDate(replies.getJSONObject(j).getString("date").toString());

						Reply reply = new Reply(idReply, id, contentReply, nameReply, imageReply, dateReply, userIdReply);
						newPost.add(reply);
					}
					
					listPostit.add(newPost);
				}
				
			}
			else {
				messageHandler.sendMessage(Message.obtain(messageHandler, 1)); // 1 = got an answer but empty 
			}
	    }
    	catch (JSONException e) {
			if (response == "") {
				messageHandler.sendMessage(Message.obtain(messageHandler, 3)); // 3 = no answer
			}
			else {
				messageHandler.sendMessage(Message.obtain(messageHandler, 2)); // 2 = error in JSON format
			}
			e.printStackTrace();
		}
		messageHandler.sendMessage(Message.obtain(messageHandler, 0)); // 0 = ok
    }
    
    
	/** Parse the date. It returns the time of the day or the day the postit was posted according to the fact if it was posted today or not.
	 * 
	 * @param 	dateRaw is a string with the format MM/DD/YYYY HH:mm AM where MM is the month, DD the day, YYYY the year, HH the hours, mm the minutes, followed by AM or PM
	 * @return 	String date that represents the time of the day of the postit if it was posted today, else it sent back the day it was posted.
	 */    
    private String parseDate(String dateRaw) {
    	/*
    	 * parseDate
    	 * input: String dateRaw
    	 * output: String date
    	 * Convert the date + time to just time if the post is from today or just the date if the post is at least one day old.
    	 */
		String date = "";
		
		Calendar timeNow = new GregorianCalendar();
		String year = new Integer(timeNow.get(Calendar.YEAR)).toString();
		String month = new Integer(timeNow.get(Calendar.MONTH)+1).toString();
		String day = new Integer(timeNow.get(Calendar.DATE)).toString();
		
		String[] postWholeDate = dateRaw.split(" ");
		String[] postDate = postWholeDate[0].split("/");
				
		if ((postDate[0].compareTo(month) == 0) && (postDate[1].compareTo(day) == 0) && (postDate[2].compareTo(year) == 0)) {
			String[] time = postWholeDate[1].split(":");
			if (postWholeDate[2].compareTo("AM") == 0) {
				if (time[0].compareTo("12") == 0) {
					date = "00:" + time[1];
				}
				else {
					if (time[0].length() == 1) {
						date = "0" + time[0] + ":" + time[1];
					}
					else {
						date = time[0] + ":" + time[1];
					}
				}
			}
			else {
				if (time[0].compareTo("12") == 0) {
					date = time[0] + ":" + time[1];
				}
				else {
					Long hours = new Long(time[0]) + 12;
					String hoursStr = hours.toString();
					date = hoursStr + ":" + time[1];
				}
			}
		}
		else {
			date = postWholeDate[0];
		}
		return date;
    }
    
    
	/** 
	 * Use once the response of the server is parsed according to JSON format.
	 */    
	private Handler messageHandler = new Handler() {
		/** Handle the message sent by the background thread.
		 * 
		 * @param 	msg is Message type. It enables us to catch any error (0 no error, 1 session not valid, 3 network problem. else json error)
		 * @return 	none
		 */
		public void handleMessage(Message msg) {  
			/* handleMessage
			 * input: Message msg
			 * output: none
			 * Display the lists of posts or not according to msg
			 */
			if (msg.what == 0) {

				
				//posts.clear();
				int token = 0;
				for (int k = 0; k < listPostit.size(); k++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("type", "postit");
					map.put("idPostit", listPostit.get(k).returnId().toString());
					map.put("username", listPostit.get(k).returnUsername());
					map.put("postit", listPostit.get(k).returnContent());
					map.put("userId", listPostit.get(k).returnUserId().toString());
					map.put("image", listPostit.get(k).returnImage().toString());

					map.put("date", listPostit.get(k).returnDate());
					posts.add(map);

					token = token + 1;
					for (int j = 0; j < listPostit.get(k).returnReplies().size(); j++) {
						HashMap<String, String> mapReply = new HashMap<String, String>();
						mapReply.put("type", "reply");
						mapReply.put("idPostit", listPostit.get(k).returnId().toString());
						mapReply.put("idReply", listPostit.get(k).returnReplies().get(j).returnIdReply().toString());
						mapReply.put("username", listPostit.get(k).returnReplies().get(j).returnUsername());
						mapReply.put("postit", listPostit.get(k).returnReplies().get(j).returnContent());
						mapReply.put("userId", listPostit.get(k).returnReplies().get(j).returnUserId().toString());
						mapReply.put("image", listPostit.get(k).returnReplies().get(j).returnImage().toString());

						
						mapReply.put("date", listPostit.get(k).returnReplies().get(j).returnDate());
						
						posts.add(mapReply);
						token = token + 1;

					}
				}
				
				if (listPostit.size() != 0) {
					startId = new Long(listPostit.get(listPostit.size()-1).returnId());
				}
				
				adapter.refresh(Home.this, posts, avatar);
				
				adapter.notifyDataSetChanged();
			}
			else if (msg.what == 1) {
				Display.toast(Home.this, Home.this.getString(R.string.session_not_valid));
				setResult(1);
 				Bundle objetbunble = new Bundle();
 				Intent intent = new Intent(Home.this, Login.class);
 				intent.putExtras(objetbunble);
 				startActivityForResult(intent, activityCode);
			}
			else if (msg.what == 3) {
				Display.toast(Home.this, Home.this.getString(R.string.network_problem));
			}
			else {
				Display.toast(Home.this, Home.this.getString(R.string.json_error));
			}
			imgLoading.setVisibility(ImageView.INVISIBLE);
	    	loading = false;
		}

	};
	
	
	/** Download an image from urlStr and return a Drawable built with the image retrieved.
	 * 
	 * @param 	ctx is the Context in which ImageOperations is called
	 * 			urlStr is the string that is used to download the image
	 * @return 	Drawable
	 */
	private Drawable ImageOperations(Context ctx, String urlStr) {
		/*
		 * ImageOperations
		 * Input: Context ctx (context), String url (url to fect)
		 * output Drawable (the avatar)
		 * Fetch an image and put it in a Drawable
		 */
		try {
			URL url = new URL(urlStr);
			Object content = url.getContent();
			InputStream is = (InputStream) content;
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	/** 
	 * Add older postits at the end of the list. It should be called when the user reaches the end of the postits.
	 */
	private void add() {
		/*
		 * add
		 * input: none
		 * output: none
		 * add old posts
		 */
    	if (loading == false ) {
	    	loading = true;
	    	// display loading animation
	    	imgLoading.setVisibility(ImageView.VISIBLE);
	    	listPostit.clear();
	
	 		
	        Thread t = new Thread() // thread to load the data (discharge the UI thread
	        {
	        	public void run() {
	            	answer = Server.getStatus(Home.this, user.returnSession(), count, startId);
	            	
	        		parseAnswer(answer);
	        	}
	        };
	        t.start();
    	}
	}
	
	
	/** Use to built a context menu
	 * 
	 * @param 	menu
	 * @param	v
	 * @param	menuInfo

	 */
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    	 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    	 View itemID = (info.targetView);
    	 
    	 int idPostit = new Long(((TextView) itemID.findViewById(R.id.idPostit)).getText().toString()).intValue();
    	 String userId = ((TextView) itemID.findViewById(R.id.userId)).getText().toString();

    	 menu.setHeaderTitle(this.getString(R.string.menu));
	
    	 menu.add(idPostit, 0, Menu.NONE, this.getString(R.string.comment));

    	 if (userId.compareTo(user.returnUserId().toString()) == 0) {
        	 String type = ((TextView) itemID.findViewById(R.id.type)).getText().toString();
	    	 if (type == "postit") {
	    		 menu.add(idPostit, 1, Menu.NONE, this.getString(R.string.delete));
	    	 }
	    	 else {
	        	 int idReply = new Long(((TextView) itemID.findViewById(R.id.idReply)).getText().toString()).intValue();
	    		 menu.add(idReply, 2, Menu.NONE, this.getString(R.string.delete));
	    	 }
    	 }
	}

    
	/** Used when a context menu is seleted to reply or delete messages.
	 * 
	 * @param 	item
	 * @return 	boolean false
	 */
	public boolean onContextItemSelected(MenuItem item) { 
		/*
		 * onContextItemSelected
		 * input: MenuItem item
		 * output: none
		 * Delete/comment a post
		 */
 		idPostit = "" + item.getGroupId();
		switch (item.getItemId()) { 
         	case 0:
				Bundle objetbunble = new Bundle();
				objetbunble.putString("phoneNumber", user.returnPhoneNumber());
				objetbunble.putString("session", user.returnSession());
				objetbunble.putString("idPostit", idPostit);
	 
				Intent intent = new Intent(Home.this, ComposeReply.class);
				intent.putExtras(objetbunble);
				startActivityForResult(intent, activityCode);
         		break;
         	case 1:
         		Server.delete(Home.this, user.returnSession(), idPostit, 0);
         		fastReload(idPostit, "postit");
         	case 2:
         		Server.delete(Home.this, user.returnSession(), idPostit, 1);
         		fastReload(idPostit, "reply");
         		break;
		}
		return false;
	}
		
	
	/** Reload the list of postits without any query to the server
	 * 
	 * @param 	idPostit is a String that represents the id of the postit
	 * 			type is a String, its value should be "postit" or "reply"
	 */
	private void fastReload(String idPostit, String type) {
		String keyToCheck;
		if (type.compareTo("postit") == 0) {
			keyToCheck = "idPostit";			
		}
		else {
			keyToCheck = "idReply";			
		}
		for (int k = 0; k < posts.size(); k++) {
			if (posts.get(k).get("type").compareTo(type) == 0) { // for later, change the type of type to int for faster comparison
				if (posts.get(k).get(keyToCheck).compareTo(idPostit) == 0) {
					posts.remove(k);
					break;
				}
			}
		}
		
		adapter.refresh(Home.this, posts, avatar);
		
		adapter.notifyDataSetChanged();
		
 		restartPostit();
	}
	
	
	
	/** It displays the menu. It is called when the menu button is pressed. 
	 * 
	 * @param 	menu
	 * @return 	boolean
	 */
 	public boolean onCreateOptionsMenu(Menu menu) {
 	    MenuInflater inflater = getMenuInflater();
 	    inflater.inflate(R.menu.principal, menu);
 	    return true;
 	}
 	
	/** It can refresh or log out the user. onOptionsItemSelected is called when an item of the menu is selected. 
	 * 
	 * @param 	item - MenuItem
	 * @return 	boolean
	 */
 	public boolean onOptionsItemSelected(MenuItem item) {
 		switch (item.getItemId()) {
 	    case R.id.refresh:
 	    	restartPostit();
 	        return true;
 	    /*
 	    case R.id.setting:
 	        setting();
 	        return true;
 	        */
 	    case R.id.logout:
 	        logout();
 	        return true;

 	    default:
 	        return super.onOptionsItemSelected(item);
 	    }
 		
 	}
 	
	/** 
	 * Log out the user
	 */
 	private void logout() {
 		/*
 		 * logout
 		 * input: none
 		 * output: none
 		 * Log out the user, destroying the session
 		 */
 		Server.logout(Home.this, user.returnSession());
    	
		Database database = new Database(Home.this);
		database.open();
        database.updateUser(new Long(0), "", "");
		database.close();
		
		setResult(3);
		finish();
 	}


}
