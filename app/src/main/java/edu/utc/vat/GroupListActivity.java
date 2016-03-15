
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

import java.util.ArrayList;
import java.util.List;


public class GroupListActivity extends BaseActivity {

    private DBHelper db = new DBHelper(BlueMixApplication.getAppContext());
    private String type = "default";
    private String id = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupslist);
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(intent.hasExtra("type")) {
            type = intent.getStringExtra("type");
            }
        if(intent.hasExtra("id")) {
            id = intent.getStringExtra("id");
        }



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


        switch(type){
            case "groups":
                setTitle("Select Group");
                cursor = db.getListByID(id);
                size = cursor.getCount();
                cursor.moveToFirst();

                for (int i=0; i < size; i++) {
                    listItemInfo ci = new listItemInfo();
                    ci.title = cursor.getString(cursor.getColumnIndexOrThrow("group_name"));
                    ci.role = cursor.getString(cursor.getColumnIndexOrThrow("role_name"));
                    ci.id = cursor.getString(cursor.getColumnIndexOrThrow("groupid"));
                    ci.type = "group";

                    cursor.moveToNext();
                    result.add(ci);
                }
                break;
            case "members":
                setTitle("Select Group Member");
                break;
            case "tasks":
                setTitle("Select Task");
                break;
            default: //Organizations
                setTitle("Select Organization");

                cursor = db.getOrgs();
                size = cursor.getCount();
                cursor.moveToFirst();

                for (int i=0; i < size; i++) {
                    listItemInfo ci = new listItemInfo();
                    ci.title = cursor.getString(cursor.getColumnIndexOrThrow("org_name"));
                    ci.role = cursor.getString(cursor.getColumnIndexOrThrow("role_name"));
                    ci.id = cursor.getString(cursor.getColumnIndexOrThrow("orgID"));
                    ci.type = "org";
                    cursor.moveToNext();
                    result.add(ci);

                }
                break;
        }


        return result;
    }


    public static Intent createIntent(Context context) {
        return new Intent(context, GroupListActivity.class);
    }
}
