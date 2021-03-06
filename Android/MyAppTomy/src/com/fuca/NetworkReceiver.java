package com.fuca;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.core.FucaApp;
import com.example.fussandr.R;

public class NetworkReceiver extends BroadcastReceiver { //
	public static final String TAG = "NetworkReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// boolean isNetworkDown = intent.getBooleanExtra(
		// ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		// //
		// if (isNetworkDown) {
		// Log.d(TAG, "onReceive: NOT connected, stopping Service");
		// context.stopService(new Intent(context, UpdaterService.class)); //
		// } else {
		// Log.d(TAG, "onReceive: connected, starting Service");
		// context.startService(new Intent(context, UpdaterService.class)); //
		// }
		FucaApp ctx = (FucaApp) context.getApplicationContext();
		ctx.updateConnectedFlags();

	}
}
