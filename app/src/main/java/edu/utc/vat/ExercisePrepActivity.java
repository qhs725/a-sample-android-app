package edu.utc.vat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ExercisePrepActivity extends Activity {

    private Exercise exercise;
    private int num = 0;
    private Intent mIntent;
    private Context context = this;
    private TextView instructionTextView;
    private Button nextbtn;
    private Button skipbtn;
    public static Intent createIntent(Context context){
        return new Intent(context, ExerciseListActivity.class);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_prep);
        nextbtn = (Button) findViewById(R.id.nextbtn1);
        skipbtn = (Button) findViewById(R.id.skipbtn);

        Bundle extras = getIntent().getExtras();

        TextView titleView = (TextView)findViewById(R.id.titletextView);


        nextbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                num++;
                if (exercise.instruction[num] != null)
                    updateInstructions();
                else {
                    mIntent = new Intent(context, ExerciseActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("exerciseName", exercise);
                    mIntent.putExtras(mBundle);
                    startActivity(mIntent);//Start ExerciseActivity
                }
            }
        });

        skipbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(context, ExerciseActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("exerciseName", exercise);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);//Start ExerciseActivity
            }
        });

        instructionTextView = (TextView)findViewById(R.id.instructionTextView);
        exercise = (Exercise) getIntent().getSerializableExtra("exerciseName");

        setTitle("Exercise Preparation");

        titleView.setText(exercise.name);
        instructionTextView.setText(exercise.instruction[num]);
    }

    public void updateInstructions(){
        instructionTextView.setText(exercise.instruction[num]);
    }

}
