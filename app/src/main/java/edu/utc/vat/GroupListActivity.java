
package edu.utc.vat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import edu.utc.vat.util.DBHelper;
import edu.utc.vat.util.adapters.GroupAdapter;
import edu.utc.vat.util.adapters.listItemInfo;
import edu.utc.vat.util.adapters.listSelections;

import java.util.ArrayList;
import java.util.List;


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

    public void setRecycler(){
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


    private List<listItemInfo> createList() {

        List<listItemInfo> result = new ArrayList<listItemInfo>();
        Cursor cursor;
        int size;


        switch (type) {
            case "group":
                setTitle("Select Group");
                cursor = db.getListByID(listSelections.getSelectedOrg());
                size = cursor.getCount();
                cursor.moveToFirst();

                for (int i = 0; i < size; i++) {
                    listItemInfo ci = new listItemInfo();
                    ci.title = cursor.getString(cursor.getColumnIndexOrThrow("group_name"));
                    ci.role = cursor.getString(cursor.getColumnIndexOrThrow("role_name"));
                    ci.id = cursor.getString(cursor.getColumnIndexOrThrow("groupid"));

                    cursor.moveToNext();
                    result.add(ci);
                }
                break;
            case "member":
                setTitle("Select Group Member");
                cursor = db.getListByID(listSelections.getSelectedGroup());
                size = cursor.getCount();
                cursor.moveToFirst();

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
                cursor = db.getTasks();
                size = cursor.getCount();
                cursor.moveToFirst();

                for (int i = 0; i < size; i++) {
                    listItemInfo ci = new listItemInfo();
                    ci.title = cursor.getString(cursor.getColumnIndexOrThrow("task_name"));
                    ci.role = cursor.getString(cursor.getColumnIndexOrThrow("task_description"));
                    ci.id = cursor.getString(cursor.getColumnIndexOrThrow("taskID"));

                    cursor.moveToNext();
                    result.add(ci);
                }
                break;
            default: //Organizations
                setTitle("Select Organization");

                cursor = db.getOrgs();
                size = cursor.getCount();
                cursor.moveToFirst();

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
    public void onResume(){
        setRecycler();

        super.onResume();
    }
    @Override
    public void onBackPressed() {

        type = listSelections.getSelectionType();
        switch(type){

            case "org":
                //default action
                break;
            case "group":
                listSelections.setSelectionType("org");
                listSelections.selectOrg(null);
                break;
            case "member":
                listSelections.setSelectionType("group");
                listSelections.selectGroup(null);
                break;
            case "task":
                listSelections.setSelectionType("member");
                listSelections.selectMember(null);

                break;
        }
        // Write your code here

        super.onBackPressed();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, GroupListActivity.class);
    }
}
