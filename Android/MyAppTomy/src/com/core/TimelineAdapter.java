package com.core;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.example.fussandr.R;

public class TimelineAdapter extends SimpleCursorAdapter { //
	static final String[] FROM = { StatusData.C_CREATED_AT, StatusData.C_USER,
			StatusData.C_TEXT }; //
	static final int[] TO = { R.id.textCreatedAt, R.id.textUser, R.id.textText }; //

	// Constructor
	public TimelineAdapter(Context context, Cursor c) { //
		super(context, R.layout.row, c, FROM, TO, 0);
	}

	// This is where the actual binding of a cursor to view happens
	@Override
	public void bindView(View row, Context context, Cursor cursor) { //
		super.bindView(row, context, cursor);

		// Manually bind created at timestamp to its view
		long timestamp = cursor.getLong(cursor
				.getColumnIndex(StatusData.C_CREATED_AT)); //
		TextView textCreatedAt = (TextView) row
				.findViewById(R.id.textCreatedAt); //
		textCreatedAt.setText(DateUtils.getRelativeTimeSpanString(timestamp)); //
	}
}
