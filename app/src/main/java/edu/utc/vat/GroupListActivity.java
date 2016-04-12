/**
 * Class summary:
 * Uses GroupAdapter to display the Organizations, Groups, Group Members, and tasks related to the logged-in user.
 */
package edu.utc.vat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import edu.utc.vat.util.DBHelper;
import edu.utc.vat.util.adapters.GroupAdapter;
import edu.utc.vat.util.adapters.listItemInfo;
import edu.utc.vat.util.adapters.listSelections;


public class GroupListActivity extends BaseActivity {

    private DBHelper db = new DBHelper(BlueMixApplication.getAppContext());
    private String type = "default";
    //private String id = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupslist);

        setRecycler();
    }


    //Init RecyclerView
    public void setRecycler() {
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        type = listSelections.getSelectionType();

        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        GroupAdapter ca = new GroupAdapter(createList());
        recList.setAdapter(ca);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_testing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Creates list to display using the value of type variable to decide which list to display
    private List<listItemInfo> createList() {
        List<listItemInfo> result = new ArrayList<listItemInfo>();
        listItemInfo header = new listItemInfo();
        Cursor cursor;
        int size;


        switch (type) {
            case "group":
                setTitle("Select Group");
                listSelections.selectGroup(null, -1);
                cursor = db.getListByID(listSelections.getSelectedOrg());
                size = cursor.getCount();
                cursor.moveToFirst();

                //Header
                header.title = "Select Group";
                result.add(header);

                for (int i = 0; i < size; i++) {
                    listItemInfo ci = new listItemInfo();
                    ci.title = cursor.getString(cursor.getColumnIndexOrThrow("group_name"));
                    ci.role = cursor.getString(cursor.getColumnIndexOrThrow("role_name"));
                    ci.id = cursor.getString(cursor.getColumnIndexOrThrow("groupid"));
                    ci.test_perm = cursor.getInt(cursor.getColumnIndexOrThrow("group_test_perm"));
                    cursor.moveToNext();
                    result.add(ci);
                }
                break;
            case "member":
                setTitle("Select Group Member");
                listSelections.selectMember(null, null);
                cursor = db.getListByID(listSelections.getSelectedGroup());
                size = cursor.getCount();
                cursor.moveToFirst();

                //Header
                header.title = "Select Member";
                result.add(header);

                for (int i = 0; i < size; i++) {
                    listItemInfo ci = new listItemInfo();
                    ci.title = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    ci.role = cursor.getString(cursor.getColumnIndexOrThrow("role_name"));
                    ci.id = cursor.getString(cursor.getColumnIndexOrThrow("memberID"));

                    cursor.moveToNext();
                    result.add(ci);
                }

                break;
            case "task":
                setTitle("Select Task");
                listSelections.selectTask(null, null, null, null);
                cursor = db.getTasks();
                size = cursor.getCount();
                cursor.moveToFirst();

                //Header
                header.title = "Select Task";
                result.add(header);

                for (int i = 0; i < size; i++) {
                    listItemInfo ci = new listItemInfo();
                    ci.title = cursor.getString(cursor.getColumnIndexOrThrow("task_name"));
                    ci.description = cursor.getString(cursor.getColumnIndexOrThrow("task_description"));
                    ci.id = cursor.getString(cursor.getColumnIndexOrThrow("taskID"));
                    ci.type = cursor.getString(cursor.getColumnIndexOrThrow("task_type"));

                    cursor.moveToNext();
                    result.add(ci);
                }
                break;
            default: //Organizations
                setTitle("Select Organization");
                listSelections.selectOrg(null);
                cursor = db.getOrgs();
                size = cursor.getCount();
                cursor.moveToFirst();

                //Header
                header.title = "Select Organization";
                result.add(header);

                /*//TODO: ***GO TO TEST LIST INSTEAD***
                if (size == 0) {//If user is not in an Organization then display TestingActivity instead of lists
                    Intent intent = new Intent(this, TestingActivity.class);
                    listSelections.selectTask("FREE", "FREE Mode: Sample Task", "Limited version to task", "regular");
                    this.startActivity(intent);
                }
                */
                for (int i = 0; i < size; i++) {
                    listItemInfo ci = new listItemInfo();
                    ci.title = cursor.getString(cursor.getColumnIndexOrThrow("org_name"));
                    ci.role = cursor.getString(cursor.getColumnIndexOrThrow("role_name"));
                    ci.id = cursor.getString(cursor.getColumnIndexOrThrow("orgID"));
                    cursor.moveToNext();
                    result.add(ci);

                }
                break;
        }
        return result;
    }

    @Override
    public void onResume() {
        setRecycler();
        super.onResume();
    }


    //Makes necessary changes to listSelections before returning to previous activity
    @Override
    public void onBackPressed() {
        type = listSelections.getSelectionType();
        switch (type) {

            case "org":
                //default action
                break;
            case "group":
                listSelections.setSelectionType("org");
                listSelections.selectOrg(null);
                break;
            case "task":
                if (listSelections.getGroupPerm() == 1) {
                    listSelections.setSelectionType("member");
                    listSelections.selectMember(null, null);
                    break;
                }
                //else: use next case
            case "member":
                listSelections.setSelectionType("group");
                listSelections.selectGroup(null, -1);
                break;
        }
        // Write your code here

        super.onBackPressed();
        finish();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, GroupListActivity.class);
    }
}
