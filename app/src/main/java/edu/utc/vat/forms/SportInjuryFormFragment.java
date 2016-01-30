package edu.utc.vat.forms;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.utc.vat.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SportInjuryFormFragment extends Fragment {

    public SportInjuryFormFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sport_injury_form, container, false);
    }
}
