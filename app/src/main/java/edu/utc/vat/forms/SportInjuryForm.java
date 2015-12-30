package edu.utc.vat.forms;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.utc.vat.BlueMixApplication;
import edu.utc.vat.R;
import edu.utc.vat.TestingActivity;

public class SportInjuryForm extends AppCompatActivity {

    private RadioGroup rGroup;
    private TextView formQuestion;
    private ArrayList<String> qArr = new ArrayList<>();
    private int index = 0;
    private int currentPart = 1;
    private int radioButtonID;
    private View radioButton;
    private Button formNextBtn;
    private int newInjury = 13;

    //TODO: add one to the next two arrays at selected position when uploading to get value that matches Nodejs version.
    private String[] qSetAnswerText1 = {"Never", "Rare", "Infrequent", "Occasional", "Frequent", "Persistent"}; // 1
    private String[] qSetAnswerText2 = {"Not at all", "Insignificant", "Marginal", "Moderate", "Substantial", "Severe"};// 2
    private String[] qSetAnswerText3 = {"No", "Yes"};// 3

    private int[] qSetAnswerVersion = {1, 1, 2, 2, 2, 2, 1, 1, 2, 1, 2, 3};// Sets text of questions based on position. questions 1-12 only

    private ArrayList<Integer> answers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_form);

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
        formQuestion.setText(qArr.get(0));

        formNextBtn = (Button)findViewById(R.id.formNextBtn);

        setTitle("Sports Fitness and Injury Form - Part 1");

        findViewById(R.id.formNextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: add function(s) for switching between the dynamic injury questions
                //TODO: handle custom injury type in separate array or in the same?

                if (formNextBtn.getText().toString().equals("Next")) {
                    if (rGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(BlueMixApplication.getAppContext(), "Please select an answer", Toast.LENGTH_LONG).show();
                    } else {
                        if (currentPart == 1) {

                            radioButtonID = rGroup.getCheckedRadioButtonId();
                            radioButton = rGroup.findViewById(radioButtonID);

                            int ans = rGroup.indexOfChild(radioButton);
                            //get index of answer to store in database (can change to text if necessary)
                            answers.add(ans);

                            index++;
                            if (index < qArr.size()) {
                                changeToQuestion(index);
                            } else {
                                currentPart = 2;
                                index++;
                                rGroup.clearCheck();
                                isInjury();
                            }

                            Toast.makeText(BlueMixApplication.getAppContext(), "Position: " + ans, Toast.LENGTH_LONG).show();
                        } else if (currentPart == 2) {//Switches to dynamic injury questions
                            setTitle("Sports Fitness and Injury Form - Part 2");
                            radioButtonID = rGroup.getCheckedRadioButtonId();
                            radioButton = rGroup.findViewById(radioButtonID);
                            int ans = rGroup.indexOfChild(radioButton);
                            answers.add(ans);


                            if (index == newInjury) {
                                Toast.makeText(BlueMixApplication.getAppContext(), "Position: " + ans, Toast.LENGTH_LONG).show();

                                //changeToLocation();
                                newInjury = newInjury + 4;//update index for next injury
                            } else if (index == newInjury + 1) { //On Location

                                //changeToType();
                            } else if (index == newInjury + 2) {//On Injury Type

                                //changeToTimeLost();
                            } else if (index == newInjury + 3) {//On Time Lost

                                //isInjury(); //Ask if there is another injury
                            }
                        }
                        if (formNextBtn.getText().toString().equals("Submit")) {
                            //TODO: Send answers to be submitted from dataUploadService
                            Toast.makeText(BlueMixApplication.getAppContext(), "(mock) Submitting...", Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                radioButtonID = rGroup.getCheckedRadioButtonId();
                radioButton = rGroup.findViewById(radioButtonID);
                int ans = rGroup.indexOfChild(radioButton);
                if (index == newInjury) {
                    if (ans == 0) {
                        formNextBtn.setText("Submit");
                    } else if (ans == 1) {
                        formNextBtn.setText("Next");
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

    //Changes displayed question to the one at the given index.
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
    private void isInjury() {
        Toast.makeText(BlueMixApplication.getAppContext(), "Switch to Part 2 at " + index, Toast.LENGTH_LONG).show();

        rGroup.clearCheck();
        formQuestion.setText("Did you sustain a musculoskeletal injury over the past 12 months?/during the past season?");

        for (int i = 0; i < rGroup.getChildCount(); i++) {
            if (i < 2) {
                ((RadioButton) rGroup.getChildAt(i)).setText(qSetAnswerText3[i]);
            } else {
                ((RadioButton) rGroup.getChildAt(i)).setVisibility(View.INVISIBLE);
            }
        }
    }

    //Inflates overflow menu for form
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_sport_injury_form, menu);
        return true;
    }
}
