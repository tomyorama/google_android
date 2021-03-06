package com.fuca;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.core.FucaApp;
import com.core.StatusData;

public class UpdaterService extends Service {
	private static final String TAG = "UpdaterService";
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
		Log.d(TAG, "onStarted");
		super.onStartCommand(intent, flags, startId);
		this.runFlag = true; //
		this.app.setServiceRunning(true);
		this.updater.start();
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
		static final String RECEIVE_TIMELINE_NOTIFICATIONS = "com.marakana.yamba.RECEIVE_TIMELINE_NOTIFICATIONS";

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
						intent.putExtra(StatusData.NEW_STATUS_EXTRA_COUNT,
								newUpdates); //
						updaterService.sendBroadcast(intent,
								RECEIVE_TIMELINE_NOTIFICATIONS); //

					}

					// next--
					Log.d(TAG, "Updater ran");
					int waitTime = Integer.parseInt(UpdaterService.this.app
							.getPrefs().getString("updates_interval", "60000"));
					Log.d(TAG, "Sleep for " + waitTime);
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					updaterService.runFlag = false;
				} //

			}
		}
	}

}
