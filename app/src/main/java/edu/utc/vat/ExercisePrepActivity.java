package edu.utc.vat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ExercisePrepActivity extends BaseActivity {

    private Exercise exercise;
    private int num = 0;
    private Intent mIntent;
    private Context context = this;
    private TextView instructionTextView;
    public static Intent createIntent(Context context){
        return new Intent(context, ExerciseListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_prep);
        Bundle extras = getIntent().getExtras();
        TextView titleView = (TextView)findViewById(R.id.titletextView);
        instructionTextView = (TextView)findViewById(R.id.instructionTextView);
        exercise = (Exercise) getIntent().getSerializableExtra("exerciseName");

        setTitle("Exercise Preparation");

        titleView.setText(exercise.name);
        instructionTextView.setText(exercise.instruction[num]);

    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nextbtn1: {
                num++;
                if(exercise.instruction[num] != null)
                    updateInstructions();
                else{
                    mIntent = new Intent(context, ExerciseActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("exerciseName", exercise);
                    mIntent.putExtras(mBundle);
                    startActivity(mIntent);//Start ExerciseActivity
                }
            }
            case R.id.skipbtn:{
                mIntent = new Intent(context, ExerciseActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("exerciseName", exercise);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);//Start ExerciseActivity
            }
        }
    }
    public void updateInstructions(){

        instructionTextView.setText(exercise.instruction[num]);
    }

}
