package com.keg.postitnow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;

/** 
 * Server class deals with all the queries sent to the server
 */
public class Server {
	
	/** 
	 * Log in
	 * 
	 * @param 	activity
	 * 			phone number of the user
	 * 			password
	 * 
	 * @return 	The answer of the server using a String
	 */
	public static String login(Activity activity, String phoneNumber, String password) {
    	String answer = "";

    	try {
        	HttpPost httppost = new HttpPost("http://60.195.250.106:8080/UI/Service/LoginFromMobile.aspx");
        	
        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
        	nameValuePairs.add(new BasicNameValuePair("phone", phoneNumber));
        	nameValuePairs.add(new BasicNameValuePair("password", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
        	HttpClient httpclient = new DefaultHttpClient();
        	HttpResponse response= httpclient.execute(httppost);
        	
        	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        	String line;
            while ((line = reader.readLine()) != null) {
            	answer += line;
            }
		} catch (UnsupportedEncodingException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IOException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IllegalStateException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		}
		
		return answer;
	}
	
	
	
	
	/** 
	 * Send a message
	 * 
	 * @param 	activity
	 * 			session - The session of the user
	 * 			message - The message sent
	 * 			mood - The mood of the user
	 * 
	 * @return 	The answer of the server using a String
	 */
	public static String send(Activity activity, String session, String message, String mood) {
		String answer = "";

    	try {
        	HttpPost httppost = new HttpPost("http://60.195.250.106:8080/UI/Service/PostStatus.aspx");

        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
        	nameValuePairs.add(new BasicNameValuePair("session_id", session));
        	nameValuePairs.add(new BasicNameValuePair("content", message));
       		nameValuePairs.add(new BasicNameValuePair("mood", mood));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			
        	HttpClient httpclient = new DefaultHttpClient();
        	HttpResponse response= httpclient.execute(httppost);
        	
        	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        	String line;
            while ((line = reader.readLine()) != null) {
            	answer += line;
            }
		} catch (UnsupportedEncodingException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IOException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IllegalStateException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		}
		
		return answer;
	}

	/** 
	 * Send a reply
	 * 
	 * @param 	activity
	 * 			session - The session of the user
	 * 			message - The message sent
	 * 			idPostit - The postit's id
	 * 
	 * @return 	The answer of the server using a String
	 */
	public static String reply(Activity activity, String session, String message, String idPostit) {
		String answer = "";

    	try {
        	HttpPost httppost = new HttpPost("http://60.195.250.106:8080/UI/Service/Reply.aspx");
        	
        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
        	nameValuePairs.add(new BasicNameValuePair("session_id", session));
        	nameValuePairs.add(new BasicNameValuePair("content", message));
       		nameValuePairs.add(new BasicNameValuePair("id", idPostit));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			
        	HttpClient httpclient = new DefaultHttpClient();
        	HttpResponse response= httpclient.execute(httppost);
        	
        	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        	String line;
            while ((line = reader.readLine()) != null) {
            	answer += line;
            }
		} catch (UnsupportedEncodingException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IOException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IllegalStateException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		}

		return answer;
	}	
	
	/** 
	 * Send the location
	 * 
	 * @param 	session - The session of the user
	 * 			cellphoneId - The id's cellphone
	 * 			lac - The area code
	 * 			networkCode - The code of the network
	 */
	public static void sendLocation(String session, int cellphoneId, int lac, String networkCode) {
		//String answer = ""; // to get an answer (for testing)
        String data = "cell_id:" + String.valueOf(cellphoneId) + ";area_code:" + String.valueOf(lac) + ";signal_dbm:0;country_code:0;signal:0;network_code:"+networkCode ;
    	try {
        	HttpPost httppost = new HttpPost("http://60.195.250.106:8080/UI/Service/UploadInfo.aspx");
        	
        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
        	nameValuePairs.add(new BasicNameValuePair("session_id", session));
        	nameValuePairs.add(new BasicNameValuePair("type", "gsm"));
       		nameValuePairs.add(new BasicNameValuePair("record0", data));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
			
        	HttpClient httpclient = new DefaultHttpClient();
        	httpclient.execute(httppost);
        	/*
        	HttpResponse response = httpclient.execute(httppost);
        	
        	
        	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        	String line;
            while ((line = reader.readLine()) != null) {
            	answer += line;
            }
            */
            
		} catch (UnsupportedEncodingException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IOException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IllegalStateException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		}
		//return answer;
	}	
	
	
	/** 
	 * Get status
	 * 
	 * @param 	activity
	 * 			session - The session of the author
	 * 			count - The number of postits we want to retrieve
	 * 			startid - The limit of id we want
	 * 
	 * @return 	The answer of the server using a String
	 */	
	public static String getStatus(Activity activity, String session, Long count, Long startId) {
		String answer = "";

    	try {
        	HttpPost httppost = new HttpPost("http://60.195.250.106:8080/UI/Service/GetStatus.aspx");
        	
        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
        	nameValuePairs.add(new BasicNameValuePair("session_id", session));
        	nameValuePairs.add(new BasicNameValuePair("count", count.toString()));
        	nameValuePairs.add(new BasicNameValuePair("start_id", startId.toString()));
        	nameValuePairs.add(new BasicNameValuePair("mode", "expand"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
        	HttpClient httpclient = new DefaultHttpClient();
        	HttpResponse response= httpclient.execute(httppost);
        	
        	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        	String line;
            while ((line = reader.readLine()) != null) {
            	answer += line;
            }
		} catch (UnsupportedEncodingException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IOException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IllegalStateException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		}
		
		return answer;
	}

	/** 
	 * Delete a message
	 * 
	 * @param 	activity
	 * 			session - The session of the author
	 * 			idPostit - The id of the postit or reply we are going to delete
	 * 			type - The type of the message (0 for postit, 1 for reply)
	 * 
	 * @return 	The answer of the server using a String
	 */	
	public static String delete(Activity activity, String session, String idPostit, int type) {
		String answer = "";

    	try {
    		HttpPost httppost;
    		if (type == 0) {
    			httppost = new HttpPost("http://60.195.250.106:8080/UI/Service/DeleteMessage.aspx");
    		}
    		else {
    			httppost = new HttpPost("http://60.195.250.106:8080/UI/Service/DeleteReply.aspx");
    		}
    		
        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
        	nameValuePairs.add(new BasicNameValuePair("session_id", session));
    		if (type == 0) {
           		nameValuePairs.add(new BasicNameValuePair("message_id", idPostit));
    		}
    		else {
           		nameValuePairs.add(new BasicNameValuePair("reply_id", idPostit));
    		}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
        	HttpClient httpclient = new DefaultHttpClient();
        	HttpResponse response= httpclient.execute(httppost);
        	
        	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        	String line;
            while ((line = reader.readLine()) != null) {
            	answer += line;
            }
		} catch (UnsupportedEncodingException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			//Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IOException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// Display.toast(activity, e.toString());
			e.printStackTrace();
		}
		return answer;
	}	
	
	/** 
	 * Log out the user by destroying the session
	 * 
	 * @param 	activity
	 * 			session - The session of the author
	 */	
	public static void logout(Activity activity, String session) {
		try {
        	HttpPost httppost = new HttpPost("http://60.195.250.106:8080/UI/Service/LoginFromMobile.aspx");
        	
        	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
        	nameValuePairs.add(new BasicNameValuePair("session_id", session));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
        	HttpClient httpclient = new DefaultHttpClient();
        	httpclient.execute(httppost);
		} catch (UnsupportedEncodingException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IOException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		} catch (IllegalStateException e) {
            //Display.toast(activity, e.toString());
			e.printStackTrace();
		}
	}
}
