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
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import edu.utc.vat.R;
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
                        Log.i("UPLOAD", "Calling PackageData");

                        //Start upload service
                        getActivity().startService(new Intent(getActivity(), dataUploadService.class));

                        Toast.makeText(context.getApplicationContext(), "Uploading...", Toast.LENGTH_LONG);
                        ((FlankerResultsActivity) context).finish();
                    }
                })
                .setNegativeButton(R.string.dialog_discard, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((FlankerResultsActivity) context).finish();
                    }
                });
        return builder.create();
    }
}
