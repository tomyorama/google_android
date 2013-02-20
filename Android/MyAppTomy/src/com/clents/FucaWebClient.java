package com.clents;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.core.FucaApp;

@SuppressLint("NewApi")
public class FucaWebClient {
	private static final String BASE_URL = "http://fuca-termin.appspot.com";
	private static final String AUTH_URL = BASE_URL + "/_ah/login";
	private static final String AUTH_TOKEN_TYPE = "ah";

	private final FucaApp mContext;
	private final String mAccountName;

	private static final String TAG = "FucaWebClient";

	public FucaWebClient(FucaApp context, String accountName) {
		this.mContext = context;
		this.mAccountName = accountName;
	}

	public HttpResponse makeRequest(String urlPath, MultipartEntity entity)
			throws Exception {
		HttpResponse res = makeRequestNoRetry(urlPath, entity, false);
		if (res.getStatusLine().getStatusCode() == 302) {
			res = makeRequestNoRetry(urlPath, entity, true);
		}
		return res;
	}

	private HttpResponse makeRequestNoRetry(String urlPath,
			MultipartEntity entity, boolean newToken) throws Exception {
		// Get auth token for account
		Account account = new Account(mAccountName, "com.google");
		String authToken = getAuthToken(account);

		if (newToken) { // invalidate the cached token
			AccountManager accountManager = AccountManager.get(mContext);
			accountManager.invalidateAuthToken(account.type, authToken);
			authToken = getAuthToken(account);
			Log.d(TAG, "New Token ");
		}
		Log.d(TAG, "Token " + authToken);
		// Get SACSID cookie
		String continueURL = BASE_URL;
		DefaultHttpClient http_client = new DefaultHttpClient();

		// Don't follow redirects
		http_client.getParams().setBooleanParameter(
				ClientPNames.HANDLE_REDIRECTS, false);

		HttpGet http_get = new HttpGet(AUTH_URL + "?continue="
				+ URLEncoder.encode(continueURL, "UTF-8") + "&auth="
				+ authToken);
		HttpResponse response;
		response = http_client.execute(http_get);
		if (response.getStatusLine().getStatusCode() != 302)
			// Response should be a redirect
			return null;

		for (Cookie cookie : http_client.getCookieStore().getCookies()) {
			if (cookie.getName().equals("ACSID")) {
				Log.d(TAG, "ACSID!");
			}
		}
		Log.d(TAG, "Make Request!");

		response.getEntity().consumeContent();
		HttpResponse result = null;

		try {
			String path = urlPath;
			if (!urlPath.contains(BASE_URL)) {
				path = BASE_URL + urlPath;
			}
			HttpPost httpPost = new HttpPost(path);
			// http_get = new HttpGet(BASE_URL + urlPath);
			/* headers */
			// http_get.setHeader("Accept", "application/xml");
			// http_get.setHeader("Content-Type", "application/xml");

			if (entity != null) {
				// httpPost.setEntity(new UrlEncodedFormEntity(entity));

				// MultipartEntity entity = new
				// MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

				// entity.addPart("number", new StringBody("5555555555"));
				// entity.addPart("clip", new StringBody("rickroll"));
				// File fileToUpload = new File(filePath);
				// FileBody fileBody = new FileBody(fileToUpload,
				// "application/octet-stream");
				// entity.addPart("upload_file", fileBody);
				// entity.addPart("tos", new StringBody("agree"));

				httpPost.setEntity(entity);
			} else {
				httpPost.setHeader("Accept", "application/xml");
				httpPost.setHeader("Content-Type", "application/xml");

			}
			result = http_client.execute(httpPost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private String getAuthToken(Account account) throws PendingAuthException {
		String authToken = null;
		AccountManager accountManager = AccountManager.get(mContext);
		// Account[] accts = accountManager.getAccountsByType("com.google");
		// Account acct = accts[0];
		Account acct = new Account("tomislav.slade@gmail.com", "com.google");
		try {
			AccountManagerFuture<Bundle> future = accountManager.getAuthToken(
					acct, AUTH_TOKEN_TYPE, null, true, null, null);
			Bundle bundle = future.getResult();
			authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
			if (authToken == null) {
				throw new PendingAuthException(bundle);
			}
		} catch (OperationCanceledException e) {
			Log.w(TAG, e.getMessage());
		} catch (AuthenticatorException e) {
			Log.w(TAG, e.getMessage());
		} catch (IOException e) {
			Log.w(TAG, e.getMessage());
		}
		return authToken;
	}

	public class PendingAuthException extends Exception {
		private static final long serialVersionUID = 1L;
		private final Bundle mAccountManagerBundle;

		public PendingAuthException(Bundle accountManagerBundle) {
			super();
			mAccountManagerBundle = accountManagerBundle;
		}

		public Bundle getAccountManagerBundle() {
			return mAccountManagerBundle;
		}
	}
}
