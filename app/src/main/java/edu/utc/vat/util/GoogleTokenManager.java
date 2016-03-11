/**
 * Sports Injury Prevention Screening -- SIPS
 * v0.01.1b (12.3.15)
 */

package edu.utc.vat.util;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Properties;

import edu.utc.vat.BlueMixApplication;
import edu.utc.vat.LoginActivity;
import edu.utc.vat.LoadingActivity;
import edu.utc.vat.MainActivity;
import edu.utc.vat.UserAccount;
import edu.utc.vat.forms.RegistrationForm;


public class GoogleTokenManager extends LoadingActivity {
    private Activity thisActivity = this;

    private static final String CLASS_NAME = GoogleTokenManager.class.getName();
    private static final int ACCOUNT_PICKER_REQUEST_CODE = 17;
    private static final int AUTH_REQUEST_CODE = 18;

    // Bearer Tokens from Google Actions will always specify this issuer.
    static String GOOGLE_ISSUER = "accounts.google.com";

    /**
     * This is the key for the "Android Application Client ID" for a given Android Client, whose value is found
     * in the Google Developers Console.
     */
    private static final String GOOGLE_ANDROID_APP_CLIENT_ID_KEY = "androidAppClientID";

    /**
     * This is the key for the "Web Application Client ID" field for a given Web Application Client, whose value
     * is found in the Google Developers Console.
     */
    private static final String GOOGLE_WEB_APP_CLIENT_ID_KEY = "webAppClientID";

    /**
     * This is the "Android Application Client ID" field for a given Android Client, whose value is found
     * in the Google Developers Console.
     */
    private String androidAppClientIdValue = "";

    /**
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
    private String id = null;
    private boolean newUser = true;
    private DBHelper db;
    private int action;
    private Cursor activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        try {
            BlueMixApplication blApplication = (BlueMixApplication) getApplication();
            Properties appSettings = blApplication.getApplicationSettings();
            if (appSettings == null) throw new Exception();

            androidAppClientIdValue = appSettings.getProperty(GOOGLE_ANDROID_APP_CLIENT_ID_KEY);
            webAppClientIdValue = appSettings.getProperty(GOOGLE_WEB_APP_CLIENT_ID_KEY);
        } catch (Exception e) {
            Log.e(CLASS_NAME, "There was an error trying to read the application settings.", e);
        }
        Log.i(CLASS_NAME, "Android Application Client ID is: " + androidAppClientIdValue);
        Log.i(CLASS_NAME, "Web Application Client ID is: " + webAppClientIdValue);


        //TODO: if extra action is == 1 then attempt to validate user on the server using access token from ACTIVEUSRE table. If there is no Internet connection then login with saved user data.
        //TODO: if action == 0 then run the default login process.
        action = extras.getInt("action");
        if (extras.getInt("action") == 1) {
            new GetTokenTask().execute();
        } else if (extras.getInt("action") == 0) {
            login();
        }
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

    public void login() {
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
        if (requestCode == ACCOUNT_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            accountId = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

            // start the getToken task to get the ID token
            System.out.println("Get Google ID Token for user: " + accountId);
            db = new DBHelper(BlueMixApplication.getAppContext()); //init db
            new GetTokenTask().execute();
        } else {
            if (requestCode == AUTH_REQUEST_CODE && resultCode == RESULT_OK) {
                // start the getToken task to get the ID token
                System.out.println("Get Google ID Token...");
                new GetTokenTask().execute();
            } else {
                //Go back if cancel is hit
                Intent intent = new Intent(thisActivity, LoginActivity.class);
                startActivity(intent);
            }
        }
    }


    /**
     * An async task for getting the ID token. It is needed because GoogleAuthUtil.getToken cannot be
     * called from the main thread.
     */
    public class GetTokenTask extends AsyncTask<Void, Void, String> {
        private final String CLASS_NAME_2 = GetTokenTask.class.getName();

        @Override
        protected String doInBackground(Void... params) {
            // if the user has not selected an account then
            // do nothing.
            if (accountId == null && action == 0)
                return null;

            if (isNetwork() && action == 0) {
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
                    Log.e(CLASS_NAME_2, "GoogleAuthException " + e.getStackTrace()[0]);
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.e(CLASS_NAME_2, "General exception occured while trying to acquire Google Auth or ID Token.");
                    e.printStackTrace();
                }
            } else if (action == 1) {
                db = new DBHelper(BlueMixApplication.getAppContext()); //init db
                activeUser = db.getActiveUser();
                activeUser.moveToFirst();

                googleAccessToken = activeUser.getString(activeUser.getColumnIndexOrThrow("access_token"));
                idToken = activeUser.getString(activeUser.getColumnIndexOrThrow("id_token"));
            }

            getUserInfo();
            // get some useful information about the currently selected user
            // we will populate the Blue List form with these details later
            // getAccountDetails(googleAccessToken);


            //if (!validateToken(idToken, true)) {return null; }
            return idToken;
        }


        private boolean validateToken(String token, boolean production) {
            String details = null;

            if (token == null)
                return false;

            if (production)
                details = validateTokenForProduction(token);
            else
                details = validateTokenForTesting(token);

            if (details == null)
                return false;

            return true;
        }

        // It does not make much sense to perform this check client side.
        // This same logic will need to be applied server side.
        // location for valid google public certs - https://www.googleapis.com/oauth2/v1/certs
        private String validateTokenForProduction(String token) {

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
                    Log.d(CLASS_NAME_2, "email_verified is TRUE");
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


            if (isNetwork()) {
                getUserInfo();
                Log.e(CLASS_NAME, "NEW USER CHECK: " + newUser);
            }
            return details;
        }

        // Google recommends this form of validation for development purposes only
        // but might be good enough for client side verification of ACCESS_TOKEN or ID_TOKEN
        private String validateTokenForTesting(String token) {
            String details = token;

            if (action == 1 && !isNetwork())
                details = idToken;

            try {
                if (isNetwork()) {
                    // use this url to get details from  id_token
                    URL url = new URL("https://www.googleapis.com/oauth2/v1/tokeninfo?alt=json&id_token=" + token);

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
                }

            } catch (IOException e) {
                System.out.println(e.toString());
            } catch (URISyntaxException e) {
                System.out.println(e.toString());
            }

            return details;
        }


        // Google recommends this form of validation for development purposes only
        // but might be good enough for client side verification of ACCESS_TOKEN or ID_TOKEN
        private String getAccountDetails(String accessToken) {
            String details = accessToken;

            if (!isNetwork() && action == 1)
                details = idToken;

            try {

                if (isNetwork()) {
                    // use this url to get details from access_token
                    URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken);

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
                }
                if (details != null) {
                    JSONObject jO = new JSONObject(details);
                    firstName = (String) jO.get("given_name");
                    lastName = (String) jO.get("family_name");
                    email = (String) jO.get("email");
                    picture = (String) jO.get("picture");
                    id = (String) jO.get("id");

                    Log.i(CLASS_NAME, "Account received is: " + email);
                    Log.i(CLASS_NAME, "First Name: " + firstName);
                    Log.i(CLASS_NAME, "Last Name: " + lastName);
                    Log.i(CLASS_NAME, "ID:  " + id);
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
            startNextActivity(idToken, googleAccessToken);

        }

        //Retrieves user info from server including a JSONObject with the user's access permissions
        private void getUserInfo() {

            HttpResponse httpresponse;
            //boolean userExists = false;
            JSONObject body;

            try {
                //Get id from sub key in initial token and only send that to server
                JSONObject token_json = new JSONObject();

                token_json = new JSONObject();
                //token_json.put("id", id);
                token_json.put("access_token", googleAccessToken);
                token_json.put("id_token", idToken);

                // Log.e("CHECK_TOKEN: ", token);

                if (isNetwork()) {
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    HttpPost post = new HttpPost("http://utc-vat.mybluemix.net/users/check");
                    post.addHeader("access_token", googleAccessToken);

                    StringEntity se = new StringEntity(token_json.toString());
                    se.setContentType("application/json;charset=UTF-8");
                    se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));

                    post.setEntity(se);

                    //Retrieve server response
                    httpresponse = httpclient.execute(post);

                    InputStreamReader isr = new InputStreamReader(
                            httpresponse.getEntity().getContent());
                    //read in the data from input stream, this can be done a variety of ways
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    //get the string version of the response data
                    body = new JSONObject(sb.toString());

                    newUser = body.getBoolean("check");
                    Log.d("ACCESS: ", body.getString("user")); //for dev use only

                    //prepare access object
                    if (body.has("user")) {
                        JSONObject user = new JSONObject(body.getString("user"));
                        saveAccessObject(user);

                        if (user.length() != 0) {
                            firstName = (String) user.get("given_name");
                            lastName = (String) user.get("family_name");
                            email = (String) user.get("email");
                            picture = (String) user.get("picture");
                            id = (String) user.get("id");

                            Log.i(CLASS_NAME, "Account received is: " + email);
                            Log.i(CLASS_NAME, "First Name: " + firstName);
                            Log.i(CLASS_NAME, "Last Name: " + lastName);
                            Log.i(CLASS_NAME, "ID:  " + id);
                        }


                    }
                } else if (action == 1) {
                    newUser = false;
                    id = activeUser.getString(activeUser.getColumnIndexOrThrow("id"));
                    firstName = activeUser.getString(activeUser.getColumnIndexOrThrow("given_name"));
                    lastName = activeUser.getString(activeUser.getColumnIndexOrThrow("family_name"));
                    email = activeUser.getString(activeUser.getColumnIndexOrThrow("email"));

                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }// GetToken class

    /**
     * Syncs Organizations and Groups the user is connected to into the database using the user object from the server
     *
     * @param user
     * @throws JSONException
     */
    private void saveAccessObject(JSONObject user) throws JSONException {

        JSONArray access_group = user.has("Groups") ? user.getJSONArray("Groups") : null;
        for (int i = 0; i < access_group.length(); i++) {
            JSONObject group = access_group.getJSONObject(i);
            db.insertGroups(group.getString("GROUPID"), group.getString("ORGANIZATIONID"), group.getString("GROUP_NAME"),
                    group.getString("GROUP_DESCRIPTION"), group.getString("ROLE_NAME"), group.getInt("GROUP_EDITING"), group.getInt("GROUP_SESSIONS"), group.getInt("GROUP_MEMBERS"),
                    group.getInt("GROUP_RESULTS"), group.getInt("GROUP_TEST"));
        }

        //Get Admin Access permissions and groups under organization
        JSONArray access_admin = user.has("Admin") ? user.getJSONArray("Admin") : null;
        for (int i = 0; i < access_admin.length(); i++) {
            JSONObject org = access_admin.getJSONObject(i);
            db.insertOrg(org.getString("ORGANIZATIONID"), org.getString("ORG_NAME"),
                    org.getString("PREMIUM_PLAN"), org.getString("ROLE_NAME"), org.getInt("ORG_INITIAL"), org.getInt("ORG_GROUPCREATE"), org.getInt("ORG_GROUPDELETE"),
                    org.getInt("ORG_EDITADMIN"));

            JSONObject org_groups = org.has("GROUPS") ? org.getJSONObject("GROUPS") : null;
            //Get groups within Organization
            if (org_groups.length() > 0)
                for (int t = 0; t < org_groups.length(); t++) {
                    JSONObject group = org_groups.getJSONObject(t + "");
                    db.insertGroups(group.getString("GROUPID"), org.getString("ORGANIZATIONID"), group.getString("GROUP_NAME"),
                            "", org.getString("ROLE_NAME"), org.getInt("GROUP_EDITING"), org.getInt("GROUP_SESSIONS"), org.getInt("GROUP_MEMBERS"),
                            org.getInt("GROUP_RESULTS"), org.getInt("GROUP_TEST"));
                }

        }


    }

    private void startNextActivity(String googleIdToken, String googleAccessToken) {

        if (googleIdToken != null && !googleIdToken.isEmpty()) {
            // if user has not been signed in then don't go onto MainActivity.
            if (email == null || email.isEmpty()) {
                Log.e(CLASS_NAME, "Error: User Not Signed in");
                return;
            }

            // we are no longer going to use selectedAccount so set to null
            // it will be reset in the event that the user clicks on sign in
            // button again.
            accountId = null;

            if (googleIdToken != null) {
                UserAccount.setIdToken(googleIdToken);
                UserAccount.setAccessToken(googleAccessToken);
                UserAccount.setName(firstName + " " + lastName);
                UserAccount.setFamilyName(lastName);
                UserAccount.setGivenName(firstName);
                UserAccount.setEmail(email);
                UserAccount.setPicture(picture);
                UserAccount.setGoogleUserID(id);
            }
            final Context context = thisActivity;
            Intent intent;


            // Save/Update Active user to SQLite db
            db.insertActiveUser(id, firstName, lastName, email, googleAccessToken, null, idToken);

            if (newUser) {
                intent = new Intent(context, RegistrationForm.class);
            } else {
                intent = new Intent(context, MainActivity.class);
            }

            Log.i(CLASS_NAME, "Opening Main Activity and passing Google ID Token:\n" + googleIdToken);
            startActivity(intent);
            finish();
        } else if (googleTokenFailedVerification) {
            Log.e(CLASS_NAME, "Error: ID Token failed verification.");
            Log.e(CLASS_NAME, "Google ID Token:\n" + googleIdToken);
        } else
            Log.e(CLASS_NAME, "Error: ID Token was not found.");

    }

    //Returns true if there is a network connection
    private boolean isNetwork() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        Boolean isNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();


        return isNetwork;
    }
}


