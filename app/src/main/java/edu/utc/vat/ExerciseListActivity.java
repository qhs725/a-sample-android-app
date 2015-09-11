package edu.utc.vat;

/**
 * Created by Jaysp656 on 9/11/2015.
 */



        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.ArrayAdapter;
        import android.widget.FrameLayout.LayoutParams;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.Toast;

        import java.io.Serializable;
        import java.util.ArrayList;

public class ExerciseListActivity extends AppCompatActivity {
    final ExerciseListActivity self = this;
    private Intent mIntent;

    //Exercise  exercises;
    ArrayList<Exercise> exercises = new ArrayList<>();
    ArrayList<String> exercise_names = new ArrayList<String>();
    public static Intent createIntent(Context context){
        return new Intent(context, ExerciseListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_list);


        //Manual Exercise 1
        Exercise e1 = new Exercise();
        e1.addName("One Leg Squat Hold");
        e1.addInstructions("1. Stand up");
        e1.addInstructions("2. Squat");
        e1.timeLimit = 20000;
        exercises.add(e1);//append to ArrayList
        exercise_names.add(e1.name); //add new exercise to listview

        //Manual Exercise 2
        Exercise e2 = new Exercise();
        e2.name = "One Leg Jump Balance";
        e2.addInstructions("1. Stand up");
        e2.addInstructions("2. Jump");
        e2.timeLimit = 15000;
        exercises.add(e2);//append to ArrayList
        exercise_names.add(e2.name); //add new exercise to listview



        ListView listView1 = (ListView)findViewById(R.id.ListView1);
        setListViewHeightBasedOnChildren(listView1);
        listView1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                //Check for exception just in case
                try{
                    Exercise ex = exercises.get(position);

                    //New Intent
                    if( ex.isInstructions()) { //If exercise has instructions (ie. first instruction contains value)
                        mIntent = new Intent(self, ExercisePrepActivity.class);
                    }
                    else{//If exercise has no instructions (ie. first instruction is null)
                        mIntent = new Intent(self, ExerciseActivity.class);
                    }

                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("exerciseName", ex);
                    mIntent.putExtras(mBundle);

                    Toast.makeText(ExerciseListActivity.this, "Loading[" + ex.name + "]", Toast.LENGTH_LONG).show();

                    startActivity(mIntent);//Start TestingActivity

                }
                catch(Exception e){
                    Log.d("className", e.getMessage());
                    Toast.makeText(ExerciseListActivity.this, "Error", Toast.LENGTH_LONG).show();

                }
            }
        });

        //Generating listview from exercise_names ArrayList
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, exercise_names);
        listView1.setAdapter(arrayAdapter);
    }

    private void setListViewHeightBasedOnChildren(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null){
            return;
        }
        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
//        for(int i = 0; i < listAdapter.getCount(); i++){
        View listItem = listAdapter.getView(0, null, listView);
        if(listItem instanceof ViewGroup){
            listItem.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
        listItem.measure(0, 0);
        totalHeight += listItem.getMeasuredHeight() * listAdapter.getCount();
//        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}

class Exercise extends ExerciseListActivity implements Serializable{
    private static final long serialVersionUID = -7060210544600464481L;

    String name;
    String[] instruction = new String[20];
    int timeLimit;
    int position = 0;

    public void addName(String newName){
        name = newName;
    }

    public void addInstructions(String s){
        instruction[position]= s;
        position++;
    }
    public Boolean isInstructions(){
        if(instruction[1] != null) {
        return true;
        }
        else{return false;}
    }
}
