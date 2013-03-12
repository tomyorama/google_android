package com.core;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.clents.FucaWebClient;
import com.core.CommentsXmlParser.Comment;
import com.example.fussandr.R;
import com.fuca.UpdaterService;

public class FucaApp extends Application implements
		OnSharedPreferenceChangeListener { //
	private static final String TAG = FucaApp.class.getSimpleName();
	public static String WIFI;
	public static String ANY;
	// Whether there is a Wi-Fi connection.
	private boolean wifiConnected = false;
	// Whether there is a mobile connection.
	private boolean mobileConnected = false;
	private boolean updatesEnabled = false;
	private boolean canSend = true;


	private SharedPreferences prefs;
	private boolean serviceRunning;
	private StatusData statusData; //

	@Override
	public void onCreate() { //
		super.onCreate();
		Log.i(TAG, "onCreated");
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		this.statusData = new StatusData(this);
		this.WIFI = getResources().getString(R.string.wifi_network);
		this.ANY = getResources().getString(R.string.any_network);
	}
	public boolean isCanSend() {
		return canSend;
	}
	// Checks the network connection and sets the wifiConnected and
	// mobileConnected
	// variables accordingly.
	public void updateConnectedFlags() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected()) {
			wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
			mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
		} else {
			wifiConnected = false;
			mobileConnected = false;
		}
		// check permisions
		String prefsValue = prefs.getString("network_to_use", "wifi");
		updatesEnabled = prefs.getBoolean("perform_updates", true);
		//handle send messages!
		canSend = (prefsValue.equals(this.WIFI) && wifiConnected)
				|| (prefsValue.equals(this.ANY) && (mobileConnected || wifiConnected));
		boolean startServiceConditionWifi = prefsValue.equals(this.WIFI)
				&& wifiConnected && updatesEnabled;
		boolean startServiceConditionAny = prefsValue.equals(this.ANY)
				&& (mobileConnected || wifiConnected) && updatesEnabled;

		//handle service!
		if (startServiceConditionWifi || startServiceConditionAny) {
			if (!this.isServiceRunning()) {
				Log.d(TAG, "onReceive: connected, starting Service");
				this.startService(new Intent(this, UpdaterService.class));
			}//
		} else {
			if (this.isServiceRunning()) {
				Log.d(TAG, "onReceive: NOT connected, stopping Service");
				this.stopService(new Intent(this, UpdaterService.class));
			}
		}
	}

	public SharedPreferences getPrefs() {
		return prefs;
	}

	public void setPrefs(SharedPreferences prefs) {
		this.prefs = prefs;
	}

	@Override
	public void onTerminate() { //
		super.onTerminate();
		Log.i(TAG, "onTerminated");
	}

	public StatusData getStatusData() { //
		return statusData;
	}

	// Connects to the online service and puts the latest statuses into DB.
	// Returns the count of new statuses
	public synchronized int fetchStatusUpdates() { //
		Log.d(TAG, "Fetching status updates");
		try {
			// Logic:
			Log.d(TAG, "Start query ");
			FucaWebClient client = new FucaWebClient(this,
					"tomislav.slade@gmail.com");
			HttpResponse response;

			response = client.makeRequest("/testWeb", null);

			Log.d(TAG, "Response Code: "
					+ response.getStatusLine().getStatusCode());
			List<Comment> comments;

			comments = new CommentsXmlParser().parse(response.getEntity()
					.getContent());

			Log.d(TAG, "XML Parser:" + comments.get(0).text);
			long latestStatusCreatedAtTime = this.getStatusData()
					.getLatestStatusCreatedAtTime();

			ContentValues values = new ContentValues();
			int count = 0;

			// Loop over the timeline and print it out
			for (Comment comment : comments) { //
				// Insert into database
				values.clear(); //
				values.put(StatusData.C_ID, comment.id);
				values.put(StatusData.C_CREATED_AT, comment.dateCreated);
				// values.put(DbHelper.C_SOURCE, "source1");
				values.put(StatusData.C_TEXT, comment.text);
				values.put(StatusData.C_USER, comment.user);
				this.getStatusData().insertOrIgnore(values);
				Log.d(TAG, String.format("Inserted %s: %s", comment.user,
						comment.text));//
				long createdAt = comment.dateCreated;
				if (latestStatusCreatedAtTime < createdAt) {
					count++;
				}
			}
			Log.d(TAG, count > 0 ? "Got " + count + " status updates"
					: "No new status updates");
			return count;
		} catch (XmlPullParserException e) {
			Log.e(TAG, "Failed to fetch status updates", e);
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			Log.e(TAG, "Failed to fetch status updates", e);
			e.printStackTrace();
			return 0;
		} catch (RuntimeException e) {
			Log.e(TAG, "Failed to fetch status updates", e);
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			Log.e(TAG, "Failed to fetch status updates", e);
			e.printStackTrace();
			return 0;
		}

	}

	public boolean isServiceRunning() { //
		return serviceRunning;
	}

	public void setServiceRunning(boolean serviceRunning) { //
		this.serviceRunning = serviceRunning;
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

		if (key.equals("network_to_use") || key.equals("perform_updates")) {
			updateConnectedFlags();
		}
	}

}
