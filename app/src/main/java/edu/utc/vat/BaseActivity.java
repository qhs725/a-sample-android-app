/**
 * Sports Injury Prevention Screening -- SIPS
 * v0.01.1b (12.?.15)
 */

package edu.utc.vat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.Gravity;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.mobile.services.core.IBMBluemix;
import com.ibm.mobile.services.core.IBMCurrentUser;
import com.ibm.mobile.services.push.IBMPush;

import org.json.JSONException;

import bolts.Continuation;
import bolts.Task;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.utc.vat.forms.SportInjuryForm;
import edu.utc.vat.util.DBHelper;
import edu.utc.vat.util.adapters.listSelections;
import edu.utc.vat.util.dataUploadService;


public class BaseActivity extends AppCompatActivity {

    private Intent intent;
    private static final String CLASS_NAME = "LoginActivity";
    private static boolean isNetwork;
    private DBHelper db = new DBHelper(BlueMixApplication.getAppContext());
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggleCompat mDrawerToggle;
    private Toast newToast;
    private Boolean drawerInit = false;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_testing, menu);
        return true;
    }


    /**
     * This is for handling the action bar menu items
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //respond to menu item selection
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_exercise:
                //intent = new Intent(this, SportInjuryForm.class);//Temporary position
                //startActivity(intent);

                Intent upload = new Intent(BlueMixApplication.getAppContext(), dataUploadService.class);
                BlueMixApplication.getAppContext().startService(upload);
                return true;

            case R.id.logout:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                db.deleteActiveUser();
                IBMBluemix.clearSecurityToken().continueWith(
                        new Continuation<IBMCurrentUser, Void>() {
                            @Override
                            public Void then(Task<IBMCurrentUser> task) throws Exception {
                                if (task.isFaulted()) {
                                    Log.e(CLASS_NAME, "Exception : " + task.getError().getMessage());
                                    return null;
                                }
                                IBMCurrentUser user = task.getResult();
                                Log.i(CLASS_NAME, "Successfully logged out of user: " + user.getUuid());
                                return null;
                            }
                        });
                Log.i(CLASS_NAME, "Finishing Main Activity. Returning to Login Screen.");
                finish();
                return true;
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (drawerInit) {
                    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                    } else {
                        mDrawerLayout.openDrawer(Gravity.LEFT);
                    }
                    return true;
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawerInit = false;
        //setContentView(R.layout.activity_testing);
        setContentView(R.layout.activity_main);
        isNetworkAvailable();
    }


    /**
     * Checks network availability and sets isNetwork
     * TODO: CHECK -- Are both of these necessary??
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        isNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return isNetwork;
    }

    //Allows retrieval of isNetwork from other classes
    public static boolean getisNetwork() {
        return BaseActivity.isNetwork;
    }


    /**
     * showToast method for simplifying Toasts to one line of code
     */
    void showToast(String message) {
        if (newToast != null) {
            newToast.cancel();
        }
        newToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        newToast.show();
    }


    /**
     * Displays a Snackbar widget(alternative to toasts). Must include the layout to appear at and message to show.
     */
    public void showSnackbar(int v, String message) {
        Snackbar.make(findViewById(v), message, Snackbar.LENGTH_LONG).show();
    }


    /**
     * Add Active user info to navigation drawer header
     */
    public void addUserToHeader() {
        View header = getLayoutInflater().inflate(R.layout.drawer_header, null);
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        CircleImageView cv = (CircleImageView) header.findViewById(R.id.profile_image);
        TextView em = (TextView) header.findViewById(R.id.header_email);
        TextView nam = (TextView) header.findViewById(R.id.header_name);

        if (UserAccount.getPicture() != null)
            cv.setImageBitmap(UserAccount.getPicture());
        if (UserAccount.getEmail() != null)
            em.setText(UserAccount.getEmail());
        if (UserAccount.getName() != null)
            nam.setText(UserAccount.getName());

        nav.addHeaderView(header);

    }


    //Initializes Navigation Drawer
    //Activity must have appropriate xml tags before calling this
    public void initNavDrawer() {
        drawerInit = true;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navDrawer);
        //Toolbar and Navigation drawer
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerToggle = new ActionBarDrawerToggleCompat(this, mDrawerLayout, mToolbar);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        this.setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        addUserToHeader();
        mDrawerToggle.syncState();


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        //also making the text changed based on the picked items from GroupListAdapter
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        Menu navMenu = nav.getMenu();
        try {
            if (listSelections.getSelectedMember().has("name") && !listSelections.getSelectedMember().getString("name").equals("")) {
                navMenu.findItem(R.id.action_change_member).setTitle("Member: " + listSelections.getSelectedMember().getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.action_change_org:
                        Toast.makeText(getApplicationContext(), "Organization change requested", Toast.LENGTH_SHORT).show();
                        listSelections.resetOrg();
                        return openGroupListActivity();
                    case R.id.action_change_group:
                        Toast.makeText(getApplicationContext(), "Group change requested", Toast.LENGTH_SHORT).show();
                        listSelections.resetGroup();
                        return openGroupListActivity();
                    case R.id.action_change_member:
                        Toast.makeText(getApplicationContext(), "Member change requested", Toast.LENGTH_SHORT).show();
                        listSelections.resetMember();
                        return openGroupListActivity();

                    default: //Use regular onClickMethod if nothing matches
                        onOptionsItemSelected(menuItem);
                        return true;

                }
            }
        });

    }


    //Drawer toggle config
    private class ActionBarDrawerToggleCompat extends ActionBarDrawerToggle {

        public ActionBarDrawerToggleCompat(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar) {
            super(
                    activity,
                    drawerLayout, toolbar,
                    R.string.drawer_open,
                    R.string.close_drawer);
        }

        @Override
        public void onDrawerClosed(View v) {
            super.onDrawerClosed(v);
            supportInvalidateOptionsMenu();
        }

        @Override
        public void onDrawerOpened(View v) {
            super.onDrawerOpened(v);
            supportInvalidateOptionsMenu();
        }

    }


    //Starts GroupListActivity
    public static Boolean openGroupListActivity() {
        Intent intent = new Intent(BlueMixApplication.getAppContext(), GroupListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BlueMixApplication.getAppContext().startActivity(intent);
        return true;
    }

}


