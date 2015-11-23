package edu.utc.vat;

import android.util.Log;

import java.io.UnsupportedEncodingException;

public class UserAccount {

	private static String idToken = null;
	private static String userName = null;
	private static String userEmail = null;
	private static String userPicture = null;
	private static String accessToken = null;
	private static String uUserID = null;
	private static String sessionID = null;
	private static String sessionInfo = null;
	
	public static String getPicture() {
		return userPicture;
	}
	public static void setPicture(String userPicture) {
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
