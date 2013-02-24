package com.fuca;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
		Log.d(TAG, "CheckFiles ");
		// String action = intent.getAction();
		// String type = intent.getType();
		VideoView videoView = (VideoView) findViewById(R.id.videoView1);
		ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
		if (intent.getParcelableExtra("BitmapImage") != null) {
			Log.d(TAG, "BitmapImage ");
			editText.setText("Slika ..");

			// imageView1.getLayoutParams().height = 80;
			// imageView1.getLayoutParams().width = 80;
			image = (Uri) intent.getParcelableExtra("BitmapImage");
			imageView1.setImageURI(image);
			videoView.setVisibility(View.GONE);

		} else if (intent.getParcelableExtra("VideoData") != null) {
			Log.d(TAG, "VideoData ");
			editText.setText("Video ..");
			// videoView.getLayoutParams().height = 80;
			// videoView.getLayoutParams().width = 80;
			video = (Uri) intent.getParcelableExtra("VideoData");
			videoView.setVideoURI(video);
			imageView1.setVisibility(View.GONE);
		} else {
			Log.d(TAG, "NOne");
			imageView1.setVisibility(View.GONE);
			videoView.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
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

		// twitter.setStatus(editText.getText().toString()); //

		// Gets the URL from the UI's text field.
		String message = editText.getText().toString();
		if (message != null && message.length() > 1) {
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				new SendMessage().execute(message);
				VideoView videoView = (VideoView) findViewById(R.id.videoView1);
				ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
				imageView1.setVisibility(View.GONE);
				videoView.setVisibility(View.GONE);
			} else {
				// textView.setText("No network connection available.");
			}
			Log.d(TAG, "onClicked");
			editText.setText("");
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
		protected void onPostExecute(String result) { //
			Log.d(TAG, result);
		}
	}

	private String downloadUrl(String message) throws IOException {

		BufferedReader reader = null;
		try {
			Log.d(TAG, "Start query ");

			FucaWebClient client = new FucaWebClient(
					((FucaApp) getApplication()), "tomislav.slade@gmail.com");
			try {
				MultipartEntity entity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				entity.addPart("commentString", new StringBody(message));
				String url = "/sendComment";
				if (image != null) {
					// Log.d(TAG, "Read file");
					// String timeStamp = new
					// SimpleDateFormat("yyyyMMdd_HHmmss")
					// .format(new Date());
					// File fileToUpload = new File(this.getCacheDir(),
					// "tmpfile"
					// + timeStamp);
					// fileToUpload.createNewFile();
					//
					// // Convert bitmap to byte array
					// Bitmap bitmap = image;
					// ByteArrayOutputStream bos = new ByteArrayOutputStream();
					// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
					// byte[] bitmapdata = bos.toByteArray();
					//
					// // write the bytes in file
					// FileOutputStream fos = new
					// FileOutputStream(fileToUpload);
					// fos.write(bitmapdata);
					// fos.flush();
					// bos.close();
					// fos.close();
					// FileBody fileBody = new FileBody(fileToUpload,
					// "application/octet-stream");
					// entity.addPart("upload_file", fileBody);
					// // entity.addPart("tos", new StringBody("agree"));
					// Log.d(TAG, "Make request for upload");
					// HttpResponse responseGetUrl = client.makeRequest(
					// "/sendCommentUrl", null);
					// reader = new BufferedReader(new InputStreamReader(
					// responseGetUrl.getEntity().getContent()));
					// url = "";
					// String line = null;
					// while ((line = reader.readLine()) != null) {
					// url += line;
					// }
					// Log.d(TAG, "Url requested: " + url);
					// image = null;

					FileBody fileBody = new FileBody(new File(image.getPath()),
							"application/octet-stream");
					entity.addPart("upload_file", fileBody);
					// entity.addPart("tos", new StringBody("agree"));
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
					image = null;

				} else if (video != null) {
					FileBody fileBody = new FileBody(new File(video.getPath()),
							"application/octet-stream");
					entity.addPart("upload_file_video", fileBody);
					// entity.addPart("tos", new StringBody("agree"));
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
					video = null;
				}
				// List<NameValuePair> params = new ArrayList<NameValuePair>();
				// params.add(new BasicNameValuePair("commentString", message));
				HttpResponse response = client.makeRequest(url, entity);
				Log.d(TAG, "Comment Response Code: "
						+ response.getStatusLine().getStatusCode());
				Toast.makeText(
						this,
						"Response Code::\n"
								+ response.getStatusLine().getStatusCode(),
						Toast.LENGTH_LONG).show();
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
}