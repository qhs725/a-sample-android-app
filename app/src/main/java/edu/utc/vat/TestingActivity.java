/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 9/9/15
 */

package edu.utc.vat;

import android.view.View;
import android.view.Menu;

import android.os.Bundle;

import android.app.Activity;


public class TestingActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }


    public void onClick(View view) {

        switch (view.getId()) {

        }
    }

}
