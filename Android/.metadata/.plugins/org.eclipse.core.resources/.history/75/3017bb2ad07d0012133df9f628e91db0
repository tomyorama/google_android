package com.example.fussandr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.core.StatusData;
import com.core.TimelineAdapter;

public class CommentsActivity extends BaseActivity {
	static final String SEND_TIMELINE_NOTIFICATIONS =
			"com.marakana.yamba.SEND_TIMELINE_NOTIFICATIONS";
	Cursor cursor; //
	ListView listTimeline; //
	static final String[] FROM = { StatusData.C_USER, StatusData.C_TEXT }; //
	static final int[] TO = { R.id.textUser, R.id.textText }; //
	TimelineAdapter adapter;
	TimelineReceiver receiver;
	IntentFilter filter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);

		// Check if preferences have been set
		if (app.getPrefs().getString("Username", null) == null) { //
			startActivity(new Intent(this, PrefsActivity.class));
			Toast.makeText(this, R.string.msgSetupPrefs, Toast.LENGTH_LONG)
					.show();
		}

		// Find your views
		listTimeline = (ListView) findViewById(R.id.listTimeline);
		receiver = new TimelineReceiver();//
		filter = new IntentFilter(StatusData.NEW_STATUS_INTENT);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Close the database
		app.getStatusData().close();
		// UNregister the receiver
		unregisterReceiver(receiver);

	}

	@Override
	protected void onResume() {
		super.onResume();
		this.setupList();


	}

	// Responsible for fetching data and setting up the list and the adapter
	private void setupList() { //
		// Get the data
		cursor = app.getStatusData().getStatusUpdates();
		// Setup Adapter
		adapter = new TimelineAdapter(this, cursor);
		listTimeline.setAdapter(adapter);
		// Register the receiver
		super.registerReceiver(receiver, filter,
				SEND_TIMELINE_NOTIFICATIONS, null); //
		
	}

	// EXAMPLE!!
	// View binder constant to inject business logic that converts a timestamp
	// to
	// relative time
	static final ViewBinder VIEW_BINDER = new ViewBinder() {
		//
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) { //
			if (view.getId() != R.id.textCreatedAt)
				return false; //
			// Update the created at text to relative time
			long timestamp = cursor.getLong(columnIndex); //
			CharSequence relTime = DateUtils.getRelativeTimeSpanString(
					view.getContext(), timestamp); //
			((TextView) view).setText(relTime); //
			return true; //
		}
	};

	class TimelineReceiver extends BroadcastReceiver {
		
//
		@Override
		public void onReceive(Context context, Intent intent) { //
			cursor = app.getStatusData().getStatusUpdates();
			adapter.swapCursor(cursor);
			adapter.notifyDataSetChanged(); //
			Log.d("TimelineReceiver", "onReceived");
		}
	}

}
