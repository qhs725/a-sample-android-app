/**
 * UTC Virtual Athletic Trainer (aka Sports Injury Prevention Screening -- SIPS)
 * v0.01.1 (12/3/15)
 * rg 12/2/15.
 */

package edu.utc.vat.post.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.util.Log;

import android.widget.Toast;

import edu.utc.vat.R;
import edu.utc.vat.TestingActivity;
import edu.utc.vat.dataUploadService;


public class ViewDialogFragment extends DialogFragment {

    Context context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_view_test_results)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (TestingActivity.getisNetwork()) {
                            Log.i("ViewDialog", "Starting ViewResultsActivity");

                            ((TestingActivity)context).launchViewer();

                            Toast.makeText(context, "Opening accelerometer results.", Toast.LENGTH_SHORT);
                        } else {
                            Toast.makeText(context, "No network connection available.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((TestingActivity)context).Upload();
                    }
                });
        return builder.create();
    }

}
