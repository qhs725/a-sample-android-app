//Displays Registration/Basic User Info form and packages up the answers into a JSONObject to be passed to dataUploadService
package edu.utc.vat.forms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.utc.vat.BlueMixApplication;
import edu.utc.vat.LoginActivity;
import edu.utc.vat.MainActivity;
import edu.utc.vat.R;
import edu.utc.vat.UserAccount;
import edu.utc.vat.util.dataUploadService;

public class RegistrationForm extends AppCompatActivity {
    private Intent intent;
    private RadioGroup rGroup;
    private RadioGroup rGroup2;
    private int qIndex = 0;
    private TextView formQuestion;
    private Button formNextBtn;
    private static JSONObject form_json = new JSONObject();
    private EditText custom_1;
    private EditText custom_2;
    //variables for sports question
    private RadioGroup rLayout;
    private CheckBox isOther;
    private CheckBox soccer;
    private CheckBox volleyball;
    private CheckBox football;
    private CheckBox baseball;
    private CheckBox basketball;
    private CheckBox golf;
    private CheckBox tennis;
    private CheckBox track_cross_country;
    private CheckBox softball;
    private CheckBox wrestling;
    private CheckBox lacrosse;
    private EditText other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rGroup = (RadioGroup) findViewById(R.id.formRadioGroup);
        rGroup2 = (RadioGroup) findViewById(R.id.formRadioGroup2);
        rLayout = (RadioGroup) findViewById(R.id.container2);
        formQuestion = (TextView) findViewById(R.id.textView_form);
        formNextBtn = (Button) findViewById(R.id.formNextBtn);
        custom_1 = (EditText) findViewById(R.id.custom_1);
        custom_2 = (EditText) findViewById(R.id.custom_2);

        soccer = (CheckBox) findViewById(R.id.soccer);
        volleyball = (CheckBox) findViewById(R.id.volleyball);
        football = (CheckBox) findViewById(R.id.football);
        baseball = (CheckBox) findViewById(R.id.baseball);
        basketball = (CheckBox) findViewById(R.id.basketball);
        golf = (CheckBox) findViewById(R.id.golf);
        tennis = (CheckBox) findViewById(R.id.tennis);
        track_cross_country = (CheckBox) findViewById(R.id.track_cross_country);
        softball = (CheckBox) findViewById(R.id.softball);
        wrestling = (CheckBox) findViewById(R.id.wrestling);
        lacrosse = (CheckBox) findViewById(R.id.lacrosse);
        other = (EditText) findViewById(R.id.other);
        isOther = (CheckBox) findViewById(R.id.isOther);

        isOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CHECKED: ", v.getId() + "");
                switch (v.getId()) {
                    case R.id.isOther:
                        Log.e("CHECKED: ", v.getId() + "");
                        CheckBox checkBox = (CheckBox) v;
                        if (checkBox.isChecked()) {
                            findViewById(R.id.other).setVisibility(View.VISIBLE);
                        } else {
                            findViewById(R.id.other).setVisibility(View.GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        put_json("form_id", "registrationform"); //set form ID for server usage
        put_json("type", "form"); //type of upload
        put_json("id", UserAccount.getGoogleUserID());
        setTitle("Registration Form");
        formQuestion.setText("What is your name?");


        //TODO: add form field validation
        //Adds answers to a json object and calls method for the next question
        findViewById(R.id.formNextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formNextBtn.getText().toString().equals("Submit")) {

                    if (rGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getApplicationContext(), "Please select a choice to continue", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(getSportsAnswers()) {
                        submitForm();
                    }
                }
                if (formNextBtn.getText().toString().equals("Next")) {

                    switch (qIndex) {
                        case 0:
                            if (isEmpty(custom_1) || isEmpty(custom_2)) {
                                Toast.makeText(getApplicationContext(), "Both fields are required", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            put_json("fname", custom_1.getText().toString());
                            put_json("lname", custom_2.getText().toString());
                            qIndex++;
                            changeToAge();
                            break;
                        case 1:
                            if (isEmpty(custom_1)) {
                                Toast.makeText(getApplicationContext(), "Please enter age", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            put_json("age", custom_1.getText().toString());
                            qIndex++;
                            changeToGender();
                            break;
                        case 2:
                            if (rGroup.getCheckedRadioButtonId() == -1) {
                                Toast.makeText(getApplicationContext(), "Please select one to continue", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            put_json("gender", ((RadioButton) findViewById(rGroup.getCheckedRadioButtonId())).getText().toString());
                            qIndex++;
                            changeToWeight();
                            break;
                        case 3:
                            if (isEmpty(custom_1)) {
                                Toast.makeText(getApplicationContext(), "Please enter weight", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            put_json("weight", custom_1.getText().toString());
                            qIndex++;
                            changeToHeight();
                            break;
                        case 4:
                            if (rGroup.getCheckedRadioButtonId() == -1 || rGroup2.getCheckedRadioButtonId() == -1) {
                                Toast.makeText(getApplicationContext(), "Please select from both columns to continue", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            put_json("ht_ft", ((RadioButton) findViewById(rGroup.getCheckedRadioButtonId())).getText().toString());
                            put_json("ht_in", ((RadioButton) findViewById(rGroup2.getCheckedRadioButtonId())).getText().toString());
                            changeToSport();
                            qIndex++;
                            break;
                        default:
                            break;
                    }
                }
            }
        });


        //TODO: is a floating action button needed?
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //Inflates overflow menu for form
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_sport_injury_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_exit_form:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Adds value to the json object to be uploaded.
    private void put_json(String key, String value) {
        try {
            form_json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    //Handles intents
    public static Intent createIntent(Context context) {
        return new Intent(context, RegistrationForm.class);
    }

    //Changes view to age question
    private void changeToAge() {
        custom_1.getText().clear();
        custom_2.getText().clear();
        formQuestion.setText("What is your age?");
        custom_2.setVisibility(View.GONE);

        custom_1.setInputType(InputType.TYPE_CLASS_NUMBER);
        custom_1.setHint("Age");
    }

    //Changes view to gender question
    private void changeToGender() {
        custom_1.setVisibility(View.GONE);
        formQuestion.setText("What is your gender?");

        RadioButton rad1 = new RadioButton(this);
        RadioButton rad2 = new RadioButton(this);
        RadioButton rad3 = new RadioButton(this);
        rad1.setText("male");
        rad2.setText("female");
        rad3.setText("other");

        rGroup.addView(rad1);
        rGroup.addView(rad2);
        rGroup.addView(rad3);

        rGroup.setVisibility(View.VISIBLE);

    }

    //Changes view to weight question
    private void changeToWeight() {
        custom_1.getText().clear();
        rGroup.setVisibility(View.GONE);
        custom_1.setVisibility(View.VISIBLE);
        custom_1.setHint("Weight");
        formQuestion.setText("How much do you weight?");

    }

    //Changes view to height question
    private void changeToHeight() {
        rGroup.removeAllViews();
        formQuestion.setText("What is your height?");
        custom_1.setVisibility(View.GONE);

        //Height - Feet
        TextView text1 = new TextView(this);
        RadioButton rad1 = new RadioButton(this);
        RadioButton rad2 = new RadioButton(this);
        RadioButton rad3 = new RadioButton(this);
        RadioButton rad4 = new RadioButton(this);
        RadioButton rad5 = new RadioButton(this);
        RadioButton rad6 = new RadioButton(this);
        RadioButton rad7 = new RadioButton(this);
        RadioButton rad8 = new RadioButton(this);

        text1.setText("Feet");
        rad1.setText("3");
        rad2.setText("4");
        rad3.setText("5");
        rad4.setText("6");
        rad5.setText("7");
        rad6.setText("8");
        rad7.setText("9");
        rad8.setText("10");

        rGroup.addView(text1);
        rGroup.addView(rad1);
        rGroup.addView(rad2);
        rGroup.addView(rad3);
        rGroup.addView(rad4);
        rGroup.addView(rad5);
        rGroup.addView(rad6);
        rGroup.addView(rad7);
        rGroup.addView(rad8);

        rGroup.setVisibility(View.VISIBLE);

        //Height - Inches
        TextView text2 = new TextView(this);
        RadioButton irad0 = new RadioButton(this);
        RadioButton irad1 = new RadioButton(this);
        RadioButton irad2 = new RadioButton(this);
        RadioButton irad3 = new RadioButton(this);
        RadioButton irad4 = new RadioButton(this);
        RadioButton irad5 = new RadioButton(this);
        RadioButton irad6 = new RadioButton(this);
        RadioButton irad7 = new RadioButton(this);
        RadioButton irad8 = new RadioButton(this);
        RadioButton irad9 = new RadioButton(this);
        RadioButton irad10 = new RadioButton(this);
        RadioButton irad11 = new RadioButton(this);
        RadioButton irad12 = new RadioButton(this);

        text2.setText("Inches");
        irad0.setText("0");
        irad1.setText("1");
        irad2.setText("2");
        irad3.setText("3");
        irad4.setText("4");
        irad5.setText("5");
        irad6.setText("6");
        irad7.setText("7");
        irad8.setText("8");
        irad9.setText("9");
        irad10.setText("10");
        irad11.setText("11");
        irad12.setText("12");
        rGroup2.addView(text2);
        rGroup2.addView(irad0);
        rGroup2.addView(irad1);
        rGroup2.addView(irad2);
        rGroup2.addView(irad3);
        rGroup2.addView(irad4);
        rGroup2.addView(irad5);
        rGroup2.addView(irad6);
        rGroup2.addView(irad7);
        rGroup2.addView(irad8);
        rGroup2.addView(irad9);
        rGroup2.addView(irad10);
        rGroup2.addView(irad11);
        rGroup2.addView(irad12);

        rGroup2.setVisibility(View.VISIBLE);


    }

    //Changes view to sport question
    private void changeToSport() {
        rGroup.removeAllViews();
        rGroup2.setVisibility(View.GONE);
        rGroup.setVisibility(View.GONE);
        formQuestion.setText("What sport are you in?");
        formNextBtn.setText(R.string.submit_text);


        rLayout.setVisibility(View.VISIBLE);
    }

    //Adds sport to json if checked, leaves out if doesn't exist to mimic how a web form handles post requests
    private boolean getSportsAnswers(){

        if(soccer.isChecked())
            put_json("sport_soccer", "Soccer");

        if(volleyball.isChecked())
            put_json("sport_volleyball", "Volleyball");

        if(football.isChecked())
            put_json("sport_football", "Football");

        if(volleyball.isChecked())
            put_json("sport_baseball", "Baseball");

        if(baseball.isChecked())
            put_json("sport_basketball", "Basketball");

        if(golf.isChecked())
            put_json("sport_golf", "Golf");

        if(tennis.isChecked())
            put_json("sport_tennis", "Tennis");

        if(track_cross_country.isChecked())
            put_json("sport_track_cross", "Track/Cross-Country");

        if(softball.isChecked())
            put_json("sport_softball", "Softball");

        if(wrestling.isChecked())
            put_json("sport_wrestling", "Wrestling");

        if(lacrosse.isChecked())
            put_json("sport_lacrosse", "Lacrosse");

        if(isOther.isChecked()) {
            if(other.getText().toString() != "") {
                put_json("sport_other", other.getText().toString());
            }
            else{
                Toast.makeText(this, "Please enter sport in text field", Toast.LENGTH_SHORT).show();
                return false;
            }

        }


        return true;
    }

    //Sends all data to be uploaded to server
    private void submitForm(){
        Intent upload = new Intent(getApplication(), dataUploadService.class);
        upload.putExtra("jsonObject", form_json.toString());

        getApplication().startService(upload);
        Toast.makeText(BlueMixApplication.getAppContext(), "Submitting...", Toast.LENGTH_LONG).show();

        intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }

}

