package edu.utc.vat;

import android.app.DialogFragment;
import android.app.Dialog;
import android.app.AlertDialog;

import android.content.DialogInterface;

import android.os.Bundle;
import android.os.AsyncTask;

import android.util.Log;


/**
 * UTC Virtual Athletic Trainer v0.000
 * rg 9/11/15
 */
public class UploadDataDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_upload_exercise_data)
                .setPositiveButton(R.string.dialog_upload, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("UPLOAD","Calling PackageData");
                        //CallNative.PackageData();
                        new UpdateTask().execute();
                    }
                })
                .setNegativeButton(R.string.dialog_discard, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO: reset timer
                    }
                });
        return builder.create();
    }

    private class UpdateTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... v) {
            CallNative.PackageData();
            return null;
        }
    }
}