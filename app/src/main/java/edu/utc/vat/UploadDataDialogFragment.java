package edu.utc.vat;

import android.app.DialogFragment;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.Activity;

import android.content.DialogInterface;
import android.content.Context;

import android.os.Bundle;
import android.os.AsyncTask;

import android.util.Log;

import android.widget.Toast;

import android.view.View;
import android.widget.TextView;


/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 9/11/15
 */
public class UploadDataDialogFragment extends DialogFragment {

    Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_upload_exercise_data)
                .setPositiveButton(R.string.dialog_upload, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("UPLOAD","Calling PackageData");
                        new UpdateTask().execute();
                        Toast.makeText(context, "Uploading...", Toast.LENGTH_LONG);
                    }
                })
                .setNegativeButton(R.string.dialog_discard, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO: nothing
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
            String x = "1000";
             CallNative.PackageData(x);
            return true;
        }

        protected void onPostExecute(Boolean b) {
            //TODO: something that will work if TestingActivity has been destroyed?

        }
    }
}

/*
public class UploadingDataDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_upload_exercise_data)
                .setPositiveButton(R.string.dialog_upload, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.dialog_discard, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO: reset timer
                    }
                });
        return builder.create();
    }
}
*/