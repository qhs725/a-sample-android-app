package edu.utc.vat;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class UserAccount {

	private static String idToken = null;
	private static String GoogleUserID = null;
	private static String givenName = null;
	private static String familyName = null;
	private static String userName = null;
	private static String userEmail = null;
	private static Bitmap userPicture = null;
	private static String accessToken = null;
	//uUserID is IBM Bluemix unique user id. TODO: may be deprecated
	private static String uUserID = null;
	private static String sessionID = null;
	private static String sessionInfo = null;
	
	public static Bitmap getPicture() {
		return userPicture;
	}
	public static void setPicture(Bitmap userPicture) {
		UserAccount.userPicture = userPicture;
	}
	public static String getIdToken() {
		return idToken;
	}
	public static void setIdToken(String idToken) {
		UserAccount.idToken = idToken;
	}
	public static String getName() {
		return userName;
	}
	public static void setName(String userName) {
		UserAccount.userName = userName;
	}
	public static String getGivenName() {
		return givenName;
	}
	public static void setGivenName(String givenName) {
		UserAccount.givenName = givenName;
	}
	public static String getFamilyName() {
		return familyName;
	}
	public static void setFamilyName(String familyName) {
		UserAccount.familyName = familyName;
	}
	public static String getEmail() {
		return userEmail;
	}
	public static void setEmail(String userEmail) {
		UserAccount.userEmail = userEmail;
	}
	public static String getAccessToken() {
		return accessToken;
	}
	public static void setAccessToken(String accessToken) {
		UserAccount.accessToken = accessToken;
	}
	public static String getuUserID() {
		return uUserID;
	}
	public static void setuUserID(String uUserID) {
		UserAccount.uUserID = uUserID;
	}
	public static String getGoogleUserID() {
		return GoogleUserID;
	}
	public static void setGoogleUserID(String GoogleUserID) {
		UserAccount.GoogleUserID = GoogleUserID;
	}
	public static String getSessionID() {
		return sessionID;
	}
	public static void setSessionID(String sessionID) {
		UserAccount.sessionID = sessionID;
	}
    public static String getSessionInfo() {
        return sessionInfo;
    }
    public static void setSessionInfo(String sessionInfo) {
        try {
            UserAccount.sessionInfo = java.net.URLEncoder.encode(sessionInfo, "UTF-8");
        }
        catch(UnsupportedEncodingException err){
            Log.d("USERACCOUNT", "Unsupported Encoding Exception thrown");
        }
    }
}
