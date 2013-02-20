package com.example.fussandr;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.core.FucaApp;
import com.core.StatusData;

public class UpdaterService extends Service {
	private static final String TAG = "UpdaterService";
	static final int DELAY = 10 * 1000; // a 10 seconds
	private boolean runFlag = false; //
	private Updater updater;
	private FucaApp app;
	SQLiteDatabase db;


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.app = (FucaApp) getApplication();

		this.updater = new Updater(); //

		Log.d(TAG, "onCreated");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		this.runFlag = true; //
		this.app.setServiceRunning(true);

		this.updater.start();

		Log.d(TAG, "onStarted");
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.runFlag = false; //
		this.updater.interrupt(); //
		this.updater = null;
		this.app.setServiceRunning(false);
		Log.d(TAG, "onDestroyed");
	}

	/**
	 * Thread that performs the actual update from the online service
	 */
	private class Updater extends Thread { 
		static final String RECEIVE_TIMELINE_NOTIFICATIONS =
				"com.marakana.yamba.RECEIVE_TIMELINE_NOTIFICATIONS";

		Intent intent;//
		public Updater() {
			super("UpdaterService-Updater");
		}

		@Override
		public void run() { //
			UpdaterService updaterService = UpdaterService.this;
			while (updaterService.runFlag) { //
				try {
					Log.d(TAG, "Updater running");

					// DO
					int newUpdates = app.fetchStatusUpdates(); //
					if (newUpdates > 0) { //
						Log.d(TAG, "We have a new status");
						intent = new Intent(StatusData.NEW_STATUS_INTENT); //
						intent.putExtra(StatusData.NEW_STATUS_EXTRA_COUNT, newUpdates); //
						updaterService.sendBroadcast(intent,RECEIVE_TIMELINE_NOTIFICATIONS); //

					}
					
					// next--
					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					updaterService.runFlag = false;
				} //

			}
		}
	}

}
