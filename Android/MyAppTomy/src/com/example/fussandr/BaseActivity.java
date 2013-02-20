package com.example.fussandr;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.core.FucaApp;

public class BaseActivity extends Activity { //
	FucaApp app; //
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private Uri fileUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (FucaApp) getApplication(); //
	}

	// Called only once first time menu is clicked on
	@Override
	public boolean onCreateOptionsMenu(Menu menu) { //
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	// Called every time user clicks on a menu item
	@Override
	public boolean onOptionsItemSelected(MenuItem item) { //

		switch (item.getItemId()) {
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.itemToggleService:
			if (app.isServiceRunning()) {
				stopService(new Intent(this, UpdaterService.class));
			} else {
				startService(new Intent(this, UpdaterService.class));
			}
			break;
		case R.id.itemPurge:
			// app.getStatusData().delete();
			Toast.makeText(this, R.string.msgAllDataPurged, Toast.LENGTH_LONG)
					.show();
			break;
		case R.id.itemTimeline:
			startActivity(new Intent(this, CommentsActivity.class).addFlags(
					Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(
					Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.itemStatus:
			startActivity(new Intent(this, StatusActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.itemTakePicture:
			if (isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(takePictureIntent,
						CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
			break;
		case R.id.itemTakeVideo:
			if (isIntentAvailable(this, MediaStore.ACTION_VIDEO_CAPTURE)) {
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO); // create a
																	// file to
																	// save the
																	// video

				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_VIDEO_CAPTURE);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set
																				// the
																				// image
																				// file
																				// name

				takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set
																				// the
																				// video
																				// image
																				// quality
																				// to
																				// high

				startActivityForResult(takePictureIntent,
						CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
			}
			break;
		}
		return true;
	}

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	// Called every time menu is opened
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) { //
		MenuItem toggleItem = menu.findItem(R.id.itemToggleService); //
		if (app.isServiceRunning()) { //
			toggleItem.setTitle(R.string.StopService);
			toggleItem.setIcon(android.R.drawable.ic_media_pause);
		} else { //
			toggleItem.setTitle(R.string.StartService);
			toggleItem.setIcon(android.R.drawable.ic_media_play);
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {

				// The user picked a contact.
				// The Intent's data Uri identifies which contact was selected.

				// Do something with the contact here (bigger example below)
				Bundle extras = data.getExtras();
				Intent statusActivity = new Intent(this, StatusActivity.class);
				statusActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				statusActivity.putExtra("BitmapImage", (Bitmap) data
						.getExtras().get("data"));
				startActivity(statusActivity);
				// mImageBitmap = (Bitmap) extras.get("data");
				// mImageView.setImageBitmap(mImageBitmap);
			}
		} else if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				// Video captured and saved to fileUri specified in the Intent
				Toast.makeText(this, "Video saved to:\n" + data.getData(),
						Toast.LENGTH_LONG).show();
				Intent statusActivity = new Intent(this, StatusActivity.class);
				statusActivity.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				statusActivity.putExtra("VideoData", data.getData());
				startActivity(statusActivity);
			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the video capture
			} else {
				// Video capture failed, advise user
			}
		}
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

}