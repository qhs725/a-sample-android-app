package edu.utc.vat.util;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Properties;

import edu.utc.vat.BlueListApplication;
import edu.utc.vat.LoginActivity;
import edu.utc.vat.MainActivity;


public class GoogleTokenManager extends Activity
{

	private Activity thisActivity = this;

	private static final String CLASS_NAME = GoogleTokenManager.class.getName();
	private static final int ACCOUNT_PICKER_REQUEST_CODE = 17;
	private static final int AUTH_REQUEST_CODE = 18;

	// Bearer Tokens from Google Actions will always specify this issuer.
	static String GOOGLE_ISSUER = "accounts.google.com";

	/*
	 * This is the key for the "Android Application Client ID" for a given Android Client, whose value is found 
	 * in the Google Developers Console.
	 */
	private static final String GOOGLE_ANDROID_APP_CLIENT_ID_KEY = "androidAppClientID";

	/*
	 * This is the key for the "Web Application Client ID" field for a given Web Application Client, whose value 
	 * is found in the Google Developers Console.
	 */
	private static final String GOOGLE_WEB_APP_CLIENT_ID_KEY = "webAppClientID";

	/*
	 * This is the "Android Application Client ID" field for a given Android Client, whose value is found 
	 * in the Google Developers Console.
	 */
	private String androidAppClientIdValue = "";

	/*
	 * This is the "Web Application Client ID" field for a given Web Application Client, whose value is found
	 * in the Google Developers Console.
	 */
	private String webAppClientIdValue = "";

	// assume the id token is valid until proven otherwise in the 
	// token validation step
	private boolean googleTokenFailedVerification = false;

	String accountId = null;
	String idToken = "Failed";
	String googleAccessToken = null;

	private String firstName = null;
	private String lastName = null;
	private String email = null;
	private String picture = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try {
			BlueListApplication blApplication = (BlueListApplication) getApplication();
			Properties appSettings = blApplication.getApplicationSettings();
			if(appSettings == null) throw new Exception();

			androidAppClientIdValue = appSettings.getProperty(GOOGLE_ANDROID_APP_CLIENT_ID_KEY);
			webAppClientIdValue = appSettings.getProperty(GOOGLE_WEB_APP_CLIENT_ID_KEY);
		} catch (Exception e) {
			Log.e(CLASS_NAME, "There was an error trying to read the application settings.", e);
		}
		Log.i(CLASS_NAME, "Android Application Client ID is: " + androidAppClientIdValue);
		Log.i(CLASS_NAME, "Web Application Client ID is: " + webAppClientIdValue);
		login();
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onStop() {
		super.onStop();
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void login()
	{
		Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
				false, null, null, null, null);

		// start the choose account intent. The result is received in onActivityResult()
		// note that if there's exactly one Google account registered, onActivityResult is called immediately
		// without launching the choose account dialog
		startActivityForResult(intent, ACCOUNT_PICKER_REQUEST_CODE);

	}

	protected void onActivityResult(final int requestCode, final int resultCode,
									final Intent data) {

		System.out.println("Verifying Google User Credentials...");
		if (requestCode == ACCOUNT_PICKER_REQUEST_CODE && resultCode == RESULT_OK)
		{
			accountId = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

			// start the getToken task to get the ID token
			System.out.println("Get Google ID Token for user: " + accountId);
			new GetTokenTask().execute();
		} else {
			if (requestCode == AUTH_REQUEST_CODE && resultCode == RESULT_OK) {
				// start the getToken task to get the ID token
				System.out.println("Get Google ID Token...");
				new GetTokenTask().execute();
			}
			else {
                //Go back if cancel is hit
				Intent intent = new Intent(thisActivity, LoginActivity.class);
				startActivity(intent);
			}
		}

	}

	/**
	 * An async task for getting the ID token. It is needed because GoogleAuthUtil.getToken cannot be
	 * called from the main thread.
	 *
	 */
	public class GetTokenTask extends AsyncTask<Void, Void, String> {
		private final String CLASS_NAME_2 = GetTokenTask.class.getName();

		@Override
		protected String doInBackground(Void... params) {
			// if the user has not selected an account then 
			// do nothing.
			if( accountId == null )
				return null;

			try {
				Log.i(CLASS_NAME_2, "Web Application Client ID read in: " + webAppClientIdValue);

				// USE THIS SCOPE TO GET THE ID_TOKEN
				String clientIdScope = "audience:server:client_id:" + webAppClientIdValue;

				Log.i(CLASS_NAME_2, "client ID Scope: " + clientIdScope);

				// USE THIS SCOPE TO GET THE ACCESS_TOKEN
				String oAuthScopes = "oauth2:";
				oAuthScopes += " https://www.googleapis.com/auth/userinfo.profile";
				oAuthScopes += " https://www.googleapis.com/auth/userinfo.email";
				oAuthScopes += " https://www.googleapis.com/auth/plus.login";
				idToken = GoogleAuthUtil.getToken(getApplicationContext(), accountId, clientIdScope);

				Log.i(CLASS_NAME_2, "OAUTH Scope: " + oAuthScopes);
				Log.i(CLASS_NAME_2, "Google ID Token: \n" + idToken);
				googleAccessToken = GoogleAuthUtil.getToken(getApplicationContext(), accountId, oAuthScopes);
				Log.i(CLASS_NAME_2, "Google Access Token: \n" + googleAccessToken);

			} catch (UserRecoverableAuthException userAuthEx) {
				// Launch the intent to open the UI dialog for resolving the error (e.g. enter correct password)
				// The result (success / failure in case the user hit the Cancel button) is returned in onActivityResult()
				startActivityForResult(
						userAuthEx.getIntent(),
						AUTH_REQUEST_CODE);

			} catch (IOException e) {
				Log.e(CLASS_NAME_2, e.getMessage());
				e.printStackTrace();
			} catch (GoogleAuthException e) {
				Log.e("GetTokenTask", "A GoogleAuthException occurred. Please check your account credentials. "
						+ "Check for any extra trailing whitespace in any properties files, remove them. "
						+ "Ensure your device's debug keystore SHA-1 fingerprint is configured for your 'Client ID for Android application' ");
				Log.e(CLASS_NAME_2, "GoogleAuthException "+e.getStackTrace()[0]);
				e.printStackTrace();
			} catch (Exception e) {
				Log.e(CLASS_NAME_2, "General exception occured while trying to acquire Google Auth or ID Token.");
				e.printStackTrace();
			}

			// get some useful information about the currently selected user
			// we will populate the Blue List form with these details later
			getAccountDetails(googleAccessToken);

			if(!validateToken(idToken,true))
			{
				return null;
			}
			return idToken;
		}

		private boolean validateToken(String token, boolean production)
		{
			String details = null;

			if(token == null)
				return false;

			if(production)
				details = validateTokenForProduction(token);
			else
				details = validateTokenForTesting(token);

			if(details == null)
				return false;

			return true;
		}

		// It does not make much sense to perform this check client side.
		// This same logic will need to be applied server side.
		// location for valid google public certs - https://www.googleapis.com/oauth2/v1/certs
		private String validateTokenForProduction(String token)
		{

			String details = null;

			JsonFactory factory = new JacksonFactory();
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier(
					// Http Transport is needed to fetch Google's latest public key
					new ApacheHttpTransport(), factory);
			GoogleIdToken idToken;
			try {
				idToken = GoogleIdToken.parse(factory, token);
				if (idToken == null) {
					Log.e(CLASS_NAME_2, "Token cannot be parsed.");
					googleTokenFailedVerification = true;
					return null;
				}

				details = idToken.getPayload().toPrettyString();

				Log.i(CLASS_NAME_2, "Android Application Client ID read in: " + androidAppClientIdValue);
				Log.d(CLASS_NAME_2, "ID Token details: \n" + idToken.getPayload().toPrettyString());

				// ***************************************************************************
				// IMPORTANT: Security Issue - Make sure the email_verified flag is TRUE
				//            Otherwise, FAIL validation (googleTokenFailedVerification=true)
				// ***************************************************************************
				if (idToken.getPayload().getEmailVerified() != null &&
						idToken.getPayload().getEmailVerified()) {
					Log.d(CLASS_NAME_2, "email_verified is TRUE");   /* SAFE */
				} else {
					Log.d(CLASS_NAME_2, "Invalid token - email_verified is FALSE");
					googleTokenFailedVerification = true;
					return null;
				}

				// Verify valid token, signed by google.com, intended for a third party.
				// This is client-side verification for demonstration purposes only.
				// Authorization token validation should be done on the server-side/cloud
				if (!verifier.verify(idToken)
						|| !idToken.verifyAudience(Collections.singletonList(webAppClientIdValue))
						|| !idToken.getPayload().getAuthorizedParty().equals(androidAppClientIdValue)) {
					Log.w(CLASS_NAME_2, "Invalid token");
					googleTokenFailedVerification = true;
					return null;
				}

				// Token originates from Google and is targeted to a specific client.
				Log.i(CLASS_NAME_2, "The token is valid");

			} catch (IOException e) {
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}
			return details;
		}

		// Google recommends this form of validation for development purposes only
		// but might be good enough for client side verification of ACCESS_TOKEN or ID_TOKEN
		private String validateTokenForTesting(String token)
		{
			String details = token;

			try {

				// use this url to get details from  id_token
				URL url = new URL("https://www.googleapis.com/oauth2/v1/tokeninfo?alt=json&id_token="+token);


				HttpClient httpClient = new DefaultHttpClient();
				HttpGet pageGet = new HttpGet(url.toURI());
				org.apache.http.HttpResponse hResponse = httpClient.execute(pageGet);

				InputStreamReader isr = new InputStreamReader(
						hResponse.getEntity().getContent());
				//read in the data from input stream, this can be done a variety of ways
				BufferedReader reader = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				//get the string version of the response data
				details = sb.toString();
				Log.d(CLASS_NAME_2, "ID Token details:\n" + details);

			} catch (IOException e) {
				System.out.println(e.toString());
			} catch (URISyntaxException e) {
				System.out.println(e.toString());
			}


			return details;
		}

		// Google recommends this form of validation for development purposes only
		// but might be good enough for client side verification of ACCESS_TOKEN or ID_TOKEN
		private String getAccountDetails(String accessToken)
		{
			String details = accessToken;

			try {

				// use this url to get details from access_token
				URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="+accessToken);

				HttpClient httpClient = new DefaultHttpClient();
				HttpGet pageGet = new HttpGet(url.toURI());
				org.apache.http.HttpResponse hResponse = httpClient.execute(pageGet);

				InputStreamReader isr = new InputStreamReader(
						hResponse.getEntity().getContent());
				//read in the data from input stream, this can be done a variety of ways
				BufferedReader reader = new BufferedReader(isr);
				StringBuilder sb = new StringBuilder();
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				//get the string version of the response data
				details = sb.toString();
				Log.d(CLASS_NAME_2, "Access Token details:" + details);
	            
	            /* convert string to json object so we can get at useful account details
	            Details: {
	            "id": "112835395680253021869",
	            "email": "",
	            "verified_email": true,
	            "name": "",
	            "given_name": "",
	            "family_name": "",
	            "link": "https://plus.google.com/112835395680253021869",
	            "picture": "https://lh4.googleusercontent.com/-sJf3tjZ8cu0/AAAAAAAAAAI/AAAAAAAAANs/zlKMXyB6d80/photo.jpg",
	            "gender": "male",
	            "locale": "en"
	            }
	            */
				if(details != null)
				{
					JSONObject jO = new JSONObject(details);
					firstName = (String)jO.get("given_name");
					lastName = (String)jO.get("family_name");
					email = (String)jO.get("email");
					picture = (String)jO.get("picture");
				}


			} catch (IOException e) {
				System.out.println(e.toString());
			} catch (URISyntaxException e) {
				System.out.println(e.toString());
			} catch (JSONException e) {
				System.out.println(e.toString());
			}
			return details;
		}


		@Override
		protected void onPostExecute(String token) {
			Log.i(CLASS_NAME, "token is: " + token);
			Log.i(CLASS_NAME, "idToken is: " + idToken);
			Log.i(CLASS_NAME, "access token is: " + googleAccessToken);
			startMainActivity(null, idToken, googleAccessToken);

		}

	}// GetToken class

	private void startMainActivity(String wlAccessToken, String googleIdToken, String googleAccessToken)
	{
		if( googleIdToken != null && !googleIdToken.isEmpty() )
		{
			// if user has not been signed in then don't go onto MainActivity.
			if(	email == null || email.isEmpty() )
			{
				Log.e(CLASS_NAME, "Error: User Not Signed in");
				return;
			}

			// we are no longer going to use selectedAccount so set to null
			// it will be reset in the event that the user clicks on sign in 
			// button again.
			accountId = null;

			final Context context = thisActivity;
			Intent intent = new Intent(context, MainActivity.class);
			intent.putExtra("GOOGLE_ID_TOKEN",googleIdToken);
			intent.putExtra("GOOGLE_OAUTH_TOKEN",googleAccessToken);
			intent.putExtra("GOOGLE_NAME", firstName+" "+lastName);
			intent.putExtra("GOOGLE_EMAIL", email);
			intent.putExtra("GOOGLE_PICTURE", picture);
			Log.i(CLASS_NAME, "Opening Main Activity and passing Google ID Token:\n"+googleIdToken);
			startActivity(intent);
			finish();
		}
		else if(googleTokenFailedVerification) {
			Log.e(CLASS_NAME, "Error: ID Token failed verification.");
			Log.e(CLASS_NAME, "Google ID Token:\n" + googleIdToken);
		}
		else
			Log.e(CLASS_NAME, "Error: ID Token was not found.");

	}


}
