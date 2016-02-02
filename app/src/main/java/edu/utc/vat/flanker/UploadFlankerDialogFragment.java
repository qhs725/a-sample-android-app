/**
 * Sports Injury Prevention Screening -- SIPS
 * v0.01.1b (12.?.15)
 * rg 12/2/15.
 */

package edu.utc.vat.flanker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.utc.vat.BlueMixApplication;
import edu.utc.vat.CallNative;
import edu.utc.vat.R;
import edu.utc.vat.TestingActivity;
import edu.utc.vat.UserAccount;
import edu.utc.vat.util.dataUploadService;

public class UploadFlankerDialogFragment extends DialogFragment {

    Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_upload_exercise_data)
                .setPositiveButton(R.string.dialog_upload, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (TestingActivity.getisNetwork()) {
                            Log.i("UPLOAD", "Calling PackageData");

                            //Start upload service
                            JSONObject temp_json = new JSONObject();
                            try {
                                temp_json.put("accessToken", UserAccount.getAccessToken());
                                temp_json.put("id", UserAccount.getGoogleUserID());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent upload = new Intent(context.getApplicationContext(), dataUploadService.class);
                            upload.putExtra("flanker_json", temp_json.toString());
                            BlueMixApplication.getAppContext().startService(upload);

                            new UpdateTask().execute();
                            Toast.makeText(context.getApplicationContext(), "Uploading...", Toast.LENGTH_LONG);
                            ((FlankerResultsActivity) context).finish();
                        } else {
                            Toast.makeText(context, "No network connection available", Toast.LENGTH_LONG).show();
                            ((FlankerResultsActivity) context).finish();
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_discard, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((FlankerResultsActivity) context).finish();
                    }
                });
        return builder.create();
    }

    private class UpdateTask extends AsyncTask<Void, Void, Boolean> {

        protected Void onPreExecute(Void v) {
            return null;
        }

        @Override
        protected Boolean doInBackground(Void... v) {
            String x = "1000";  //TODO: Does PackageData() still require an argument?
           // CallNative.PackageData(x);
            return true;
        }

        protected void onPostExecute(Boolean b) {
            //TODO: nothing
        }
    }

}
