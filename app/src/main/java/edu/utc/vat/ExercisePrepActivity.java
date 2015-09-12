package edu.utc.vat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ExercisePrepActivity extends AppCompatActivity {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_prep, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
