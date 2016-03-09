package edu.utc.vat.util;

/**
 * Created by Jaysp656 on 3/8/2016.
 * 
 * Helper class for connections to SQLite Database
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sipsdb";
    public static final String ACTIVE_USER_TABLE_NAME = "activeUser";
    public static final String ACTIVE_USER_COLUMN_ID = "id";
    public static final String ACTIVE_USER_COLUMN_FIRST_NAME = "given_name";
    public static final String ACTIVE_USER_COLUMN_LAST_NAME = "family_name";
    public static final String ACTIVE_USER_COLUMN_ACCESS = "access_token";
    public static final String ACTIVE_USER_COLUMN_REFRESH = "refresh_token";
    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table IF NOT EXISTS activeUser " +
                        "(Lock char(1) not null DEFAULT 'X', id VARCHAR, given_name text, family_name text, email text, access_token text, refresh_token text, id_token text," +
                        " constraint PK_ACTIVEUSER PRIMARY KEY (Lock),constraint CK_ACTIVEUSER_Locked CHECK (Lock='X'))"
        );

         db.execSQL("CREATE TABLE IF NOT EXISTS ORG(orgID VARCHAR PRIMARY KEY, org_name VARCHAR, premium_plan text, role_name VARCHAR, org_initial INT, org_groupCreate INT, org_groupDelete INT, org_editAdmin INT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS GROUPS(groupid VARCHAR primary key, orgID VARCHAR, group_name VARCHAR, group_description TEXT, role_name VARCHAR, group_editing_perm INT, group_sessions_perm INT, group_members_perm INT, group_results_perm INT, group_test_perm INT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS activeuser");
        db.execSQL("DROP TABLE IF EXISTS GROUPS");
        db.execSQL("DROP TABLE IF EXISTS ORG");
        onCreate(db);
    }

    /**
     * ActiveUser table
     *
     */

    public Cursor getActiveUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from activeuser", null );
        return res;
    }

    public boolean insertActiveUser (String id, String given_name, String family_name, String email, String access_token, String refresh_token, String id_token)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("given_name", given_name);
        contentValues.put("family_name", family_name);
        contentValues.put("email", email);
        contentValues.put("access_token", access_token);
        contentValues.put("refresh_token", refresh_token);
        contentValues.put("id_token", id_token);

        int check = (int) db.insertWithOnConflict("activeuser", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (check == -1) {
            db.update("activeuser", contentValues, "lock = ? ", new String[] { "X" } );
        }
        return true;
    }

    //gets row count of ACTIVEUSER table to see if there is an entry
    public int isSavedUser(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ACTIVE_USER_TABLE_NAME);
        return numRows;
    }

    public Integer deleteActiveUser()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("activeuser",
                "lock = ? ",
                new String[] { "X" });
    }

    /**
     * Groups Table
     *
     *  */


    public boolean insertGroups  (String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.insert("contacts", null, contentValues);
        return true;
    }


    //TODO: change to get all Users within a group
    public ArrayList<String> getAllUSERS()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from USER", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(ACTIVE_USER_COLUMN_ID)));
            res.moveToNext();
        }
        return array_list;
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