/**
 * Sports Injury Prevention Screening -- SIPS
 * v0.01.1b (12.3.15)
 */

package edu.utc.vat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import edu.utc.vat.util.GoogleTokenManager;


public class LoginActivity extends AppCompatActivity implements OnClickListener{

	private Activity thisActivity = this;
	
	private com.google.android.gms.common.SignInButton mGetGoogleTokenButton;
	@SuppressWarnings("unused")
	private TextView mStatus;
	@SuppressWarnings("unused")
	private TextView statusWindow;
	@SuppressWarnings("unused")
	private String statusMessage = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	    	    	    
	    mGetGoogleTokenButton = (com.google.android.gms.common.SignInButton) findViewById(R.id.get_google_token_button);
	    mGetGoogleTokenButton.setOnClickListener(this);
        //String dirPath = getFilesDir().toString();
	}

	protected void onActivityResult(final int requestCode, final int resultCode,
	         final Intent data) {

	}
	
	protected void onStart() {
		super.onStart();
	}

	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		
		final Context context = thisActivity;
		Intent intent = null;

		switch (v.getId()) {
		
			case R.id.get_google_token_button:
			    intent = new Intent(context, GoogleTokenManager.class);
	            startActivity(intent);
	            finish();
				break;
			default:
				break;
				
		}
	}

}
