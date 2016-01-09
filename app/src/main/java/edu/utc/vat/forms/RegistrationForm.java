package edu.utc.vat.forms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import edu.utc.vat.dataUploadService;

public class RegistrationForm extends AppCompatActivity {
    private Intent intent;
    private RadioGroup rGroup;
    private int qIndex = 0;
    private TextView formQuestion;
    private View radioButton;
    private Button formNextBtn;
    private static JSONObject form_json = new JSONObject();
    private EditText custom_1;
    private EditText custom_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rGroup = (RadioGroup) findViewById(R.id.formRadioGroup);
        formQuestion = (TextView) findViewById(R.id.textView_form);
        formNextBtn = (Button) findViewById(R.id.formNextBtn);
        custom_1 = (EditText) findViewById(R.id.custom_1);
        custom_2 = (EditText) findViewById(R.id.custom_2);

        setTitle("Registration Form");
        formQuestion.setText("What is your name?");


        findViewById(R.id.formNextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formNextBtn.getText().toString().equals("Submit")) {

                    Intent upload = new Intent(getApplication(), dataUploadService.class);
                    upload.putExtra("jsonObject", form_json.toString());


                    getApplication().startService(upload);
                    Toast.makeText(BlueMixApplication.getAppContext(), "Submitting...", Toast.LENGTH_LONG).show();

                    intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                }
                if (formNextBtn.getText().toString().equals("Next")) {

                    switch (qIndex) {
                        case 0:
                            put_json("fname", custom_1.toString());
                            put_json("lname", custom_2.toString());
                            qIndex++;
                            changeToAge();
                            break;
                        case 1:
                            put_json("age", custom_1.toString());
                            qIndex++;
                            changeToGender();
                            break;
                        case 2:
                            put_json("gender", ((RadioButton) findViewById(rGroup.getCheckedRadioButtonId())).getText().toString());
                            qIndex++;
                            changeToWeight();
                            break;
                        case 3:
                            put_json("weight", custom_1.toString());
                            qIndex++;
                            changeToHeight();
                            break;
                        case 4:
                            changeToSport();
                            qIndex++;
                            break;
                        default:
                            break;
                    }
                }
            }
        });


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

    private void put_json(String key, String value) {
        try {
            form_json.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, RegistrationForm.class);
    }

    //Changes view to age question
    private void changeToAge() {
        formQuestion.setText("What is your age?");
        custom_2.setVisibility(View.GONE);

        custom_1.setInputType(InputType.TYPE_CLASS_NUMBER);
        custom_1.setHint("Age");
    }

    //Changes view to gender question
    private void changeToGender() {
        // rGroup.removeAllViews();
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

    }

    //Changes view to weight question
    private void changeToWeight() {
        rGroup.removeAllViews();
        custom_1.setVisibility(View.VISIBLE);
        custom_1.setHint("Weight");
        formQuestion.setText("How much do you weight?");

    }

    //Changes view to height question
    private void changeToHeight() {
        rGroup.removeAllViews();
        formQuestion.setText("What is your height?");

    }

    //Changes view to sport question
    private void changeToSport() {
        rGroup.removeAllViews();
        formQuestion.setText("What sport are you in?");
        formNextBtn.setText(R.string.submit_text);


    }

}

