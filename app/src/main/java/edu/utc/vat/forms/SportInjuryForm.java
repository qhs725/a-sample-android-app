package edu.utc.vat.forms;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.utc.vat.BlueMixApplication;
import edu.utc.vat.MainActivity;
import edu.utc.vat.R;
import edu.utc.vat.TestingActivity;
import edu.utc.vat.UserAccount;
import edu.utc.vat.dataUploadService;

//TODO: remove extra toasts that are commented out.

//Builds form with 12 static questions and a loop of 4 Injury questions
//Outputs JSON similar to webapp version of form
public class SportInjuryForm extends AppCompatActivity {

    private Intent intent;
    private RadioGroup rGroup;
    private TextView formQuestion;
    private ArrayList<String> qArr = new ArrayList<>();
    private int index = 0;
    private int currentPart = 1;
    private int radioButtonID;
    private View radioButton;
    private Button formNextBtn;
    private int newInjury;
    private int injuryCount = 0;
    private static JSONObject form_json = new JSONObject();
    private EditText custom_injury;

    private String[] qSetAnswerText1 = {"Never", "Rare", "Infrequent", "Occasional", "Frequent", "Persistent"}; // 1
    private String[] qSetAnswerText2 = {"Not at all", "Insignificant", "Marginal", "Moderate", "Substantial", "Severe"};// 2
    private String[] qSetAnswerText3 = {"No", "Yes"};// 3
    private String[] qSetATLocation = {"Foot/Toe", "Ankle", "Lower Leg", "Knee", "Thigh/Groin/Hip", "Pelvis/Abdomen/Low Back", "Ribs/Chest", "Neck/Upper Back", "Shoulder/Upper Arm", "Elbow/Forearm", "Wrist/Hand/Finger"};


    private int[] qSetAnswerVersion = {1, 1, 2, 2, 2, 2, 1, 1, 2, 1, 2, 3};// Sets text of questions based on position. questions 1-12 only


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_form);

        try {
            form_json.put("type", "Sports Fitness & Injury Form");
            form_json.put("id", UserAccount.getGoogleUserID());
            form_json.put("arr[null]", 0); //forces keys from being in an array format.
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Part 1, questions 1-11 - static
        qArr.add("Over the past several years, how often have moderate-to-severe muscle and/or joint injuries limited your ability to participate fully in sports-related activities?");
        qArr.add("Over the past several years, how often has PAIN in any body part limited your OVERALL sport performance capabilities?");
        qArr.add("To what extent do you feel that previous muscle and/or joint injuries currently limit your speed, power output, and/or endurance?");
        qArr.add("To what extent is your OVERALL ability to perform weightlifting exercises and/or activities that require explosive force output (such as maximum-effort jumping) currently limited by PAIN?");
        qArr.add("To what extent is your ability to perform any SPORT-SPECIFIC SKILL (such as throwing, swinging, or kicking accuracy) currently limited by PAIN?");
        qArr.add("To what extent are you bothered by muscle spasms, stiffness, and/or aching discomfort during routine activities of daily living (such as sleeping, walking, climbing/descending stairs, etc.)?");
        qArr.add("How often do you experience sensations of joint instability, giving-way, and/or sudden pain that create apprehension during rapid and forceful movements (such as pivoting and cutting)?");
        qArr.add("As a result of participating in sport-related activities, how often do you experience joint aching, limited motion, stiffness, and/or swelling?");
        qArr.add("To what extent are you bothered by chronic joint symptoms like joint locking, catching, grinding, or persistent aching?");
        qArr.add("Over the past 12 months, to what extent have personal life events created emotional responses (such as sadness, depression, and/or anxiety) that have interfered with your enjoyment of life, ability to concentrate, and/or fulfillment of routine daily responsibilities?");
        qArr.add("To what extent have you ever experienced headaches, vision problems, loss of balance, and/or difficulty concentrating as a result of concussion or repeated head impacts?");
        //Part 2, question 12 - static
        qArr.add("Did you experience a concussion over the past 12 months?/during the past season?");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rGroup = (RadioGroup) findViewById(R.id.formRadioGroup);
        formQuestion = (TextView) findViewById(R.id.textView_sport_form);
        custom_injury = (EditText) findViewById(R.id.custom_text);
        formQuestion.setText(qArr.get(0));

        formNextBtn = (Button) findViewById(R.id.formNextBtn);

        setTitle("Sports Fitness and Injury Form - Part 1");

        findViewById(R.id.formNextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formNextBtn.getText().toString().equals("Submit")) {

                    try {
                        form_json.put("arr[" + index + "]", "0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent upload = new Intent(BlueMixApplication.getAppContext(), dataUploadService.class);
                    upload.putExtra("jsonObject", form_json.toString());


                    BlueMixApplication.getAppContext().startService(upload);
                    Toast.makeText(BlueMixApplication.getAppContext(), "Submitting...", Toast.LENGTH_LONG).show();

                    intent = new Intent(BlueMixApplication.getAppContext(), MainActivity.class);
                    startActivity(intent);
                }
                if (formNextBtn.getText().toString().equals("Next")) {
                    if (rGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(BlueMixApplication.getAppContext(), "Please select an answer", Toast.LENGTH_LONG).show();
                    } else {
                        if (currentPart == 1) {

                            radioButtonID = rGroup.getCheckedRadioButtonId();
                            radioButton = rGroup.findViewById(radioButtonID);

                            int ans = rGroup.indexOfChild(radioButton);
                            //get index of answer to store in database (can change to text if necessary)

                            try {
                                form_json.put("arr[" + (index + 1) + "]", ans + "");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            index++;
                            if (index < qArr.size()) {
                                changeToQuestion(index);
                            } else {
                                currentPart = 2;
                                rGroup.clearCheck();
                                isInjury(0);
                            }
                        } else if (currentPart == 2) {//Switches to dynamic injury questions
                            setTitle("Sports Fitness and Injury Form - Part 2");
                            radioButtonID = rGroup.getCheckedRadioButtonId();
                            radioButton = rGroup.findViewById(radioButtonID);
                            int ans = rGroup.indexOfChild(radioButton);

                            try {
                                if (index != newInjury + 2) {
                                    if (index == newInjury + 1) {
                                        form_json.put("arr[" + index + "]", (ans + 1) + "");
                                    } else {
                                        form_json.put("arr[" + index + "]", ans + "");
                                    }
                                } else { //Handles TextViews added to Injury Type question
                                    if (ans <= 4) {
                                    } else if (ans > 4 && ans < 7) {
                                        ans = ans - 1;
                                    } else {
                                        ans = ans - 2;
                                    }

                                    form_json.put("arr[" + (index) + "]", ans + "");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (index == newInjury) {
                                //Toast.makeText(BlueMixApplication.getAppContext(), "New Injury: " + index, Toast.LENGTH_LONG).show();

                                injuryCount++;
                                changeToLocation();
                            } else if (index == newInjury + 1) { //On Location
                               //Toast.makeText(BlueMixApplication.getAppContext(), "Location: " + index, Toast.LENGTH_LONG).show();
                                changeToType();
                            } else if (index == newInjury + 2) {//On Injury Type
                                //Toast.makeText(BlueMixApplication.getAppContext(), "Type: " + index, Toast.LENGTH_LONG).show();
                                String custom_ans;
                                try {
                                    if (ans == 6) {
                                        custom_ans = custom_injury.getText().toString();

                                        form_json.put("arr_1[" + (injuryCount) + "]", custom_ans);
                                        custom_injury.setVisibility(View.INVISIBLE);
                                        custom_injury.setText("");

                                    } else {
                                        form_json.put("arr_1[" + (injuryCount) + "]", "");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                changeToTimeLost();
                            } else if (index == newInjury + 3) {//On Time Lost
                                //Toast.makeText(BlueMixApplication.getAppContext(), "Time: " + index, Toast.LENGTH_LONG).show();
                                newInjury = newInjury + 4;//update index for next injury
                                isInjury(1); //Ask if there is another injury
                            }
                        }
                    }
                }

            }
        });

        //Changes Next button to Submit, hides/shows custom TextEdit for Injury Type
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                radioButtonID = rGroup.getCheckedRadioButtonId();
                radioButton = rGroup.findViewById(radioButtonID);
                int ans = rGroup.indexOfChild(radioButton);
                if (index == newInjury) {
                    if (ans == 0) {
                        formNextBtn.setText(R.string.submit_text);
                    } else if (ans == 1) {
                        formNextBtn.setText(R.string.nextbtn_text);
                    }
                } else if (index == newInjury + 2) {
                    if (ans == 8) {
                        custom_injury.setVisibility(View.VISIBLE);
                    } else if (ans != 8) { //if can be false on Injury Type question
                        custom_injury.setVisibility(View.INVISIBLE);
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

    //Changes displayed part 1 question to the one at the given index.
    private void changeToQuestion(int pos) {
        rGroup.clearCheck();
        formQuestion.setText(qArr.get(pos));

        //Sets text for radios
        if (qSetAnswerVersion[pos] == 1) {
            for (int i = 0; i < rGroup.getChildCount(); i++) {
                ((RadioButton) rGroup.getChildAt(i)).setText(qSetAnswerText1[i]);
            }

        } else if (qSetAnswerVersion[pos] == 2) {
            for (int i = 0; i < rGroup.getChildCount(); i++) {
                ((RadioButton) rGroup.getChildAt(i)).setText(qSetAnswerText2[i]);
            }

        } else if (qSetAnswerVersion[pos] == 3) {
            for (int i = 0; i < rGroup.getChildCount(); i++) {
                if (i < 2) {
                    ((RadioButton) rGroup.getChildAt(i)).setText(qSetAnswerText3[i]);
                } else {
                    ((RadioButton) rGroup.getChildAt(i)).setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    //Displays question that asks is there is an/another injury to be added
    private void isInjury(int whichV) {
        index++;
        rGroup.clearCheck();
        if (whichV == 0) {
            newInjury = index;
            formQuestion.setText("Did you sustain a musculoskeletal injury over the past 12 months?/during the past season?");
        } else {
            formQuestion.setText("Did you sustain any another musculoskeletal injuries over the past 12 months?/during the past season?");
        }


        for (int i = 0; i < rGroup.getChildCount(); i++) {
            if (i < 2) {
                ((RadioButton) rGroup.getChildAt(i)).setText(qSetAnswerText3[i]);
            } else {
                ((RadioButton) rGroup.getChildAt(i)).setVisibility(View.INVISIBLE);
            }
        }
    }

    //Removes prior questions and generates Location question.
    private void changeToLocation() {
        index++;
        rGroup.clearCheck();
        rGroup.removeAllViews();
        formQuestion.setText("Where was the injury located?");

        //Add radio for question
        RadioButton button;
        for (int i = 0; i < 11; i++) {
            button = new RadioButton(this);
            button.setText(qSetATLocation[i]);
            rGroup.addView(button);
        }
    }

    //Generates Injury Type question.
    private void changeToType() {
        index++;
        rGroup.removeAllViews();
        formQuestion.setText("What type of Injury did you sustain? (pick one)");

        TextView header1 = new TextView(this);
        RadioButton rad1 = new RadioButton(this);
        RadioButton rad2 = new RadioButton(this);
        RadioButton rad3 = new RadioButton(this);
        TextView header2 = new TextView(this);
        RadioButton rad4 = new RadioButton(this);
        RadioButton rad5 = new RadioButton(this);
        TextView header3 = new TextView(this);
        RadioButton rad6 = new RadioButton(this);

        header1.setText("Sudden Event (Acute - Traumatic)\n");
        rad1.setText("Joint Strain");
        rad2.setText("Muscle Strain");
        rad3.setText("Fracture");
        header2.setText("Gradual Onset (Chronic - Overuse)\n");
        rad4.setText("Muscle/Tendon Disorder");
        rad5.setText("Stress Fracture");
        header3.setText("Other\n");
        rad6.setText("Custom");

        rGroup.addView(header1);
        rGroup.addView(rad1);
        rGroup.addView(rad2);
        rGroup.addView(rad3);
        rGroup.addView(header2);
        rGroup.addView(rad4);
        rGroup.addView(rad5);
        rGroup.addView(header3);
        rGroup.addView(rad6);
    }

    //Generates Time Lost question
    private void changeToTimeLost() {
        index++;
        custom_injury.setVisibility(View.INVISIBLE);
        rGroup.removeAllViews();
        formQuestion.setText("Was there any time lost as a result of this injury?");

        RadioButton button;
        for (int i = 0; i <= 1; i++) {
            button = new RadioButton(this);
            button.setText(qSetAnswerText3[i]);
            rGroup.addView(button);
        }
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
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, SportInjuryForm.class);
    }
}
