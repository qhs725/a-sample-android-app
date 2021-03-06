package edu.utc.vat.util;

/**
 * Created by Jaysp656 on 3/8/2016.
 * <p/>
 * Helper class for connections to SQLite Database
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sipsdb";
    public static final String ACTIVE_USER_TABLE_NAME = "activeUser";
    public static final String ACTIVE_USER_COLUMN_ID = "id";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        buildTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS activeuser");
        db.execSQL("DROP TABLE IF EXISTS GROUPS");
        db.execSQL("DROP TABLE IF EXISTS ORG");
        onCreate(db);
    }


    public void buildTables(SQLiteDatabase db) {
        db.execSQL(
                "create table IF NOT EXISTS activeUser " +
                        "(Lock char(1) not null DEFAULT 'X', id VARCHAR, image BLOB, given_name text, family_name text, email text, access_token text, refresh_token text, id_token text," +
                        " constraint PK_ACTIVEUSER PRIMARY KEY (Lock),constraint CK_ACTIVEUSER_Locked CHECK (Lock='X'))"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS ORG(orgID VARCHAR PRIMARY KEY, org_name VARCHAR, premium_plan text, role_name VARCHAR, org_initial INT, org_groupCreate INT, org_groupDelete INT, org_editAdmin INT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS GROUPS(groupid VARCHAR primary key, orgID VARCHAR, group_name VARCHAR, group_description TEXT, role_name VARCHAR, group_editing_perm INT, group_sessions_perm INT, group_members_perm INT, group_results_perm INT, group_test_perm INT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS groupMembers(memberID VARCHAR, groupID VARCHAR, orgID VARCHAR, name VARCHAR, role_name VARCHAR, UNIQUE (memberID, groupID) ON CONFLICT REPLACE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS taskInfo(taskID VARCHAR primary key, task_name VARCHAR, task_description text, task_type VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Sessions(sessionID VARCHAR primary key, session_desc text, start_date DATE, end_date DATE, groupID VARCHAR, createdBy VARCHAR, session_type VARCHAR, dateAdded DATE);");

    }


    /**
     * ActiveUser table
     */

    public Cursor getActiveUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from activeuser", null);
        return res;
    }

    public boolean insertActiveUser(String id, byte[] image, String given_name, String family_name, String email, String access_token, String refresh_token, String id_token) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("image", image);
        contentValues.put("given_name", given_name);
        contentValues.put("family_name", family_name);
        contentValues.put("email", email);
        contentValues.put("access_token", access_token);
        contentValues.put("refresh_token", refresh_token);
        contentValues.put("id_token", id_token);

        int check = (int) db.insertWithOnConflict("activeuser", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (check == -1) {
            db.update("activeuser", contentValues, "lock = ? ", new String[]{"X"});
        }
        return true;
    }

    //gets row count of ACTIVEUSER table to see if there is an entry
    public int isSavedUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ACTIVE_USER_TABLE_NAME);
        return numRows;
    }

    public Integer deleteActiveUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = -9;

        result = db.delete("activeuser",
                "lock = ? ",
                new String[]{"X"});

        db.execSQL("DROP TABLE IF EXISTS ORG");
        db.execSQL("DROP TABLE IF EXISTS GROUPS");
        db.execSQL("DROP TABLE IF EXISTS groupMembers");
        db.execSQL("DROP TABLE IF EXISTS taskInfo");
        db.execSQL("DROP TABLE IF EXISTS Sessions");

        buildTables(db);
        return result;
    }

    /**
     * Groups Table
     * <p/>
     * Functions for interacting with the groups table.
     */


    public boolean insertGroups(String groupId, String orgId, String name, String desc, String role, int edit_perm, int session_perm, int members_perm, int results_perm, int test_perm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("groupid", groupId);
        contentValues.put("orgID", orgId);
        contentValues.put("group_name", name);
        contentValues.put("group_description", desc);
        contentValues.put("role_name", role);
        contentValues.put("group_editing_perm", edit_perm);
        contentValues.put("group_sessions_perm", session_perm);
        contentValues.put("group_members_perm", members_perm);
        contentValues.put("group_results_perm", results_perm);
        contentValues.put("group_test_perm", test_perm);

        int check = (int) db.insertWithOnConflict("groups", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (check == -1) {
            db.update("groups", contentValues, "groupid= ? ", new String[]{groupId});
        }
        return true;
    }


    //TODO: change to get all Users within a group? not working currently
    public ArrayList<String> getAllUSERS() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from USER", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(ACTIVE_USER_COLUMN_ID)));
            res.moveToNext();
        }
        return array_list;
    }


    /**
     * Org Table
     * <p/>
     * Functions for interacting with the organizations table.
     */

    public boolean insertOrg(String orgID, String name, String plan, String role, int org_initial, int org_groupCreate, int org_groupDelete, int org_editAdmin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("orgID", orgID);
        contentValues.put("org_name", name);
        contentValues.put("premium_plan", plan);
        contentValues.put("role_name", role);
        contentValues.put("org_initial", org_initial);
        contentValues.put("org_groupCreate", org_groupCreate);
        contentValues.put("org_groupDelete", org_groupDelete);
        contentValues.put("org_editAdmin", org_editAdmin);


        // db.insert("groups", null, contentValues);

        int check = (int) db.insertWithOnConflict("org", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (check == -1) {
            db.update("org", contentValues, "orgID= ? ", new String[]{orgID});
        }

        return true;
    }


    public Cursor getOrgs() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from ORG", null);
        return res;
    }

    /**
     * Group Members Table
     * <p/>
     * Functions for interacting with the groupMembers table.
     */

    public boolean insertMember(String memberID, String groupID, String orgId, String name, String role) {//}, String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("memberID", memberID);
        contentValues.put("groupID", groupID);
        contentValues.put("orgID", orgId);
        contentValues.put("name", name);
        contentValues.put("role_name", role);
        //contentValues.put("numID", number);

        int check = (int) db.insertWithOnConflict("groupMembers", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (check == -1) {
            db.update("groupMembers", contentValues, "groupID= ? AND memberID = ? ", new String[]{groupID, memberID});
        }
        return true;
    }

    /**
     * Sessions Table
     * <p/>
     * sessionID VARCHAR primary key, session_desc text, start_date DATE, end_date DATE, groupID VARCHAR,
     * createdBy VARCHAR, session_type VARCHAR, dateAdded DATE
     */
    public boolean insertSession(String id, String desc, String type, String groupID, String createdBy, String start, String end, String added) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("sessionID", id);
        contentValues.put("session_desc", desc);
        contentValues.put("groupID", groupID);
        contentValues.put("createdBy", createdBy);
        contentValues.put("start_date", start);
        contentValues.put("end_date", end);
        contentValues.put("session_type", type);
        contentValues.put("dateAdded", added);

        int check = (int) db.insertWithOnConflict("Sessions", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (check == -1) {
            db.update("Sessions", contentValues, "sessionID= ?", new String[]{id});
        }
        return true;
    }

    public String getActiveSessionID(String groupID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sessionID = "";
        boolean first = true;

        Cursor res = db.rawQuery("select * from Sessions WHERE GROUPID = '" + groupID + "'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            if (first) {
                first = false;
                sessionID = res.getString(res.getColumnIndexOrThrow("sessionID"));
            } else {
                sessionID = sessionID + ", " + res.getString(res.getColumnIndexOrThrow("sessionID"));
            }
            res.moveToNext();
        }
        return sessionID;
    }


    /**
     * Tasks Info Table
     * taskID VARCHAR primary key, task_name VARCHAR, task_description text, task_type VARCHAR
     * Functions for interacting with the taskInfo table.
     */

    public boolean insertTask(String id, String name, String desc, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("taskID", id);
        contentValues.put("task_name", name);
        contentValues.put("task_description", desc);
        contentValues.put("task_type", type);

        int check = (int) db.insertWithOnConflict("taskInfo", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (check == -1) {
            db.update("taskInfo", contentValues, "taskID= ?", new String[]{id});
        }
        return true;
    }

    public Cursor getTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from taskInfo", null);
        return res;
    }


    /**
     * Misc
     * <p/>
     * Functions for interacting with the database
     */

    //Retrieves groups within an organization OR members within a group OR task info for group
    //Can not retrieve Organization using Organization ID
    public Cursor getListByID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from GROUPS WHERE orgID = '" + id + "'", null);

        if (!(res.moveToFirst()) || res.getCount() == 0) {
            res = db.rawQuery("select * from groupMembers WHERE groupID = '" + id + "'", null);
        }
        if (!(res.moveToFirst()) || res.getCount() == 0) {
            //TODO: Create taskInfo table
            //res = db.rawQuery("select * from taskInfo", null);
        }

        return res;
    }

    /*
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ACTIVE_USER_TABLE_NAME);
        return numRows;
    }

    public Integer deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("sipsdb",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }
    */


}