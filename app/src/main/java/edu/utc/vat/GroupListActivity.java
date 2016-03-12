
package edu.utc.vat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import edu.utc.vat.util.adapters.GroupAdapter;
import edu.utc.vat.util.adapters.GroupInfo;
import java.util.ArrayList;
import java.util.List;


public class GroupListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my);

        setContentView(R.layout.activity_groupslist);
        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        GroupAdapter ca = new GroupAdapter(createList(30));
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



    private List<GroupInfo> createList(int size) {

        List<GroupInfo> result = new ArrayList<GroupInfo>();
        for (int i=1; i <= size; i++) {
            GroupInfo ci = new GroupInfo();
            ci.name = GroupInfo.NAME_PREFIX + i;
            ci.surname = GroupInfo.SURNAME_PREFIX + i;
            ci.email = GroupInfo.EMAIL_PREFIX + i + "@test.com";

            result.add(ci);

        }

        return result;
    }


    public static Intent createIntent(Context context) {
        return new Intent(context, GroupListActivity.class);
    }
}
