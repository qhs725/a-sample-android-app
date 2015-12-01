/**
 * UTC Virt Athletic Trainer v0.01.1 (12/3/15)
 * rg 11/24/15
 */

package edu.utc.vat.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Button;

import edu.utc.vat.R;

public class BtActivity extends Activity {

    private BluetoothAdapter mbtAdapter = null;
    private Button pairButton;
    private Button balanceButton;
    private Button flankerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(edu.utc.vat.R.layout.bluetooth_activity);

        balanceButton = (Button) findViewById(R.id.BtMenuButton1);
        flankerButton = (Button) findViewById(R.id.BtMenuButton2);
        pairButton = (Button) findViewById(R.id.BtMenuButton3);

        //setHasOptionsMenu(true); TODO: create options menu
        mbtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mbtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
    }

}
