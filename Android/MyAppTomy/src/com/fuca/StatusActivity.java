package com.fuca;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.clents.FucaWebClient;
import com.core.FucaApp;
import com.example.fussandr.R;

public class StatusActivity extends BaseActivity implements OnClickListener { //
	private static final String TAG = "StatusActivity";
	EditText editText;
	Button updateButton;
	String text = "";
	Uri image;
	Uri video;
	private AlertDialog dialog;
	boolean bVideoIsBeingTouched = false;
	private String dialogMessage;

	//
	// Twitter twitter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.status_main);
		// Find views
		editText = (EditText) findViewById(R.id.editText); //
		updateButton = (Button) findViewById(R.id.buttonUpdate);
		updateButton.setOnClickListener(this); //
		CheckFiles();
	}

	private void CheckFiles() {
		// Get intent, action and MIME type
		Intent intent = getIntent();
		String action = intent.getAction();
		String type = intent.getType();
		Log.d(TAG, "CheckFiles ");
		VideoView videoView = (VideoView) findViewById(R.id.videoView1);
		ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
		File dirPath = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				StoreDirectory);
		if (action != null && action.equalsIgnoreCase(Intent.ACTION_SEND)
				&& type != null) {
			if ("text/plain".equals(type)) {
				String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
				editText.setText(sharedText);
				imageView1.setVisibility(View.GONE);
				videoView.setVisibility(View.GONE);
			} else if (type.startsWith("image/")) {
				image = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
				if (!image.getPath().startsWith(dirPath.getAbsolutePath())) {
					image = CheckFile(image, MEDIA_TYPE_IMAGE);
				}
				Log.d(TAG, " path " + image.getPath());
				Log.d(TAG, "BitmapImage ");
				int size = 10;
				Bitmap bitmapOriginal = BitmapFactory.decodeFile(image
						.getPath());
				Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(
						bitmapOriginal, bitmapOriginal.getWidth() / size,
						bitmapOriginal.getHeight() / size, true);
				bitmapOriginal.recycle();
				imageView1.setImageBitmap(bitmapsimplesize);
				videoView.setVisibility(View.GONE);
			} else if (type.startsWith("video/")) {
				video = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
				if (!video.getPath().startsWith(dirPath.getAbsolutePath())) {
					video = CheckFile(video, MEDIA_TYPE_VIDEO);
				}
				Log.d(TAG, " path " + video.getPath());
				Log.d(TAG, "VideoData ");
				videoView.setVideoURI(video);
				imageView1.setVisibility(View.GONE);

				// toggle video playing
				videoView.setOnTouchListener(new View.OnTouchListener() {
					public boolean onTouch(View v, MotionEvent event) {
						VideoView videoView = (VideoView) findViewById(R.id.videoView1);
						Log.d(TAG, "ToggleVideo");
						if (!bVideoIsBeingTouched) {
							bVideoIsBeingTouched = true;
							if (videoView.isPlaying()) {
								videoView.pause();
							} else {
								videoView.start();
							}
							Handler mhandler = new Handler();
							mhandler.postDelayed(new Runnable() {
								public void run() {
									bVideoIsBeingTouched = false;
								}
							}, 100);
						} else {

						}
						return false;
					}
				});
			}
		} else {
			Log.d(TAG, "NOne");
			imageView1.setVisibility(View.GONE);
			videoView.setVisibility(View.GONE);
			findViewById(R.id.itemsLayout).setVisibility(View.GONE);
		}
	}

	private Uri CheckFile(Uri file, int type) {
		// not picture taked by this application!!
		Uri mediaStorageDir = getOutputMediaFileUri(type);
		Log.d(TAG, "Writing new file");
		InputStream reader = null;
		OutputStream stream = null;
		try {
			reader = getContentResolver().openInputStream(file);
			stream = new BufferedOutputStream(new FileOutputStream(
					mediaStorageDir.getPath()));
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int len = 0;
			while ((len = reader.read(buffer)) != -1) {
				stream.write(buffer, 0, len);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "ERROR");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, "ERROR");
		} finally {
			// clese streams
			try {
				if (stream != null)
					stream.close();
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mediaStorageDir;

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // NO MENU
	// return true;
	// }

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		if (this.dialogMessage != null) {
			this.ToggleDialog(this.dialogMessage);
		}
		// CheckFiles();
	}

	@Override
	public void onPause() {
		super.onPause(); // Always call the superclass method first
		Log.d(TAG, "onPause");
	}

	@Override
	public void onDestroy() {
		super.onDestroy(); // Always call the superclass
		Log.d(TAG, "onDestroy");
		// Stop method tracing that the activity started during onCreate()
		// android.os.Debug.stopMethodTracing();
	}

	@Override
	protected void onStop() {
		super.onStop(); // Always call the superclass method first
		Log.d(TAG, "onStop");
	}

	@Override
	protected void onRestart() {
		super.onRestart(); // Always call the superclass method first
		Log.d(TAG, "onRestart");
		CheckFiles();
	}

	// Called when button is clicked //
	public void onClick(View v) {

		// check network!
		if (!app.isCanSend()) {
			Log.d(TAG, "no network");
			Toast.makeText(this,
					"Trnutno nema mreže ili slanje niju omogućeno!",
					Toast.LENGTH_LONG).show();
			return;
		}
		VideoView videoView = (VideoView) findViewById(R.id.videoView1);
		ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
		if (videoView.isPlaying()) {
			videoView.pause();
			videoView.stopPlayback();
		}
		String message = editText.getText().toString();
		if (message != null && message.length() > 1) {
			ToggleDialog("Slanje u tijeku!");
			new SendMessage().execute(message);

			imageView1.setVisibility(View.GONE);
			videoView.setVisibility(View.GONE);

			Log.d(TAG, "onClicked");
			editText.setText("");
		} else {
			Toast.makeText(this, "Unesite poruku!", Toast.LENGTH_LONG).show();
		}
	}

	class SendMessage extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... statuses) { //
			try {

				return downloadUrl(statuses[0]);
			} catch (IOException e) {
				Log.d(TAG, "Error  send message!");
				return "Unable to retrieve web page. URL may be invalid.";
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) { //
			super.onProgressUpdate(values);

		}

		@Override
		protected void onPostExecute(String result) {
			StatusActivity.this.ToggleDialog(null);
			startActivity(new Intent(StatusActivity.this,
					CommentsActivity.class).addFlags(
					Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(
					Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			Log.d(TAG, result);
			finish();
		}
	}

	private String downloadUrl(String message) throws IOException {

		BufferedReader reader = null;
		try {
			Log.d(TAG, "Start query ");
			FucaApp app = (FucaApp) getApplication();
			FucaWebClient client = new FucaWebClient(app, app.getPrefs()
					.getString("Username", "error"));
			try {
				MultipartEntity entity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				entity.addPart("commentString", new StringBody(message));
				entity.addPart("userName", new StringBody(app.getPrefs()
						.getString("Username", "UnknownUser")));
				String url = "/sendComment";
				Log.d(TAG, "Make request for upload");
				HttpResponse responseGetUrl = client.makeRequest(
						"/sendCommentUrl", null);
				reader = new BufferedReader(new InputStreamReader(
						responseGetUrl.getEntity().getContent()));
				url = "";
				String line = null;
				while ((line = reader.readLine()) != null) {
					url += line;
				}
				Log.d(TAG, "Url requested: " + url);
				if (image != null) {
					FileBody fileBody = new FileBody(new File(image.getPath()),
							"application/octet-stream");
					entity.addPart("upload_file", fileBody);
					image = null;

				} else if (video != null) {
					FileBody fileBody = new FileBody(new File(video.getPath()),
							"application/octet-stream");
					entity.addPart("upload_file_video", fileBody);
					video = null;
				}
				// List<NameValuePair> params = new ArrayList<NameValuePair>();
				// params.add(new BasicNameValuePair("commentString", message));
				HttpResponse response = client.makeRequest(url, entity);
				Log.d(TAG, "Comment Response Code: "
						+ response.getStatusLine().getStatusCode());
				// reader = new BufferedReader(new InputStreamReader(response
				// .getEntity().getContent()));
				//
				// String first_line = reader.readLine();
				// Log.d(TAG, "Response first_line:" + first_line);
				// Log.d(TAG, "XML Parser:" + new
				// CommentsXmlParser().parse(response
				// .getEntity().getContent()).size());
				return "Sve OK!";
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "Error" + e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "Error" + e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d(TAG, "Error" + e.getMessage());
				e.printStackTrace();
			}
			return "Nesto nevaja";
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	// /
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putString("dialogMessage", this.dialogMessage);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		this.dialogMessage = savedInstanceState.getString("dialogMessage");
	}

	private synchronized void ToggleDialog(String message) {
		if (message != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(message);
			dialog = builder.create();
			dialog.show();
		} else {
			dialog.hide();//
		}
		this.dialogMessage = message;
	}
}