package com.keg.postitnow;

import java.util.Timer;
import java.util.TimerTask;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;


/** 
 * Location is the class responsible for sending the user's location to the service. It extends Service.
 */
public class Location extends Service {
	/** 
	 * The timer used to send location
	 */
    private static Timer timer = new Timer();
    
    /** 
     * Informations about the user that send its location
     */
	private User user = new User();

	/** 
	 * onBind has to be implemented but is not used here.
	 * 
	 * @param 	arg0
	 * @return 	IBinder null
	 */
    public IBinder onBind(Intent arg0) {
    	return null;
    }

	/** 
	 * Start the service
	 * 
	 * @param 	intent is an Intent not used
	 * 			startId is an Integer but is not used here
	 */
    @Override
    public void onStart(Intent intent, int startId) {
    	startService();
    }
    
	/** 
	 * Destroy the service
	 */    public void onDestroy() {
    	super.onDestroy();
    }

    /*
    public int onStartCommand(Intent intent, int flags, int startId) {
    	startService();
    	return 0;
    }
     */


	/** 
	 * Start the service
	 */
	 private void startService() {
    	Database database = new Database(this);
		database.open();
        user = database.getUser();
		database.close();

    	timer.scheduleAtFixedRate(new mainTask(), 0, 600000);
	}

    
    private class mainTask extends TimerTask {
    	public void run() {
    		toastHandler.sendEmptyMessage(0);
    	}
    }

    
	/** 
	 * Handler that send the location
	 */
    private final Handler toastHandler = new Handler() {
		/** 
		 * Send the location
		 * 
		 * @param 	Message msg
		 */
		public void handleMessage(Message msg) {
    		sendLocation();
    	}
    };
    
    
	/** 
	 * Send the location
	 */
	public void sendLocation() {
		try {
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); 
			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
			
			String networkCode = tm.getSimOperator();
			
			int cellphoneId = location.getCid();
			int lac = location.getLac();
			if ((cellphoneId != -1) && (lac != -1)) {
				Server.sendLocation(user.returnSession(), cellphoneId, lac, networkCode);
			}
        }
		catch (NullPointerException ex) { 
			//TODO
        }

	}


}

