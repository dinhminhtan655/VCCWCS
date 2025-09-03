package com.wcs.vcc.main.tripdelivery.fragmenttrips;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wcs.wcs.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DenyTripsFragment extends Fragment {


    public DenyTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_deny_trips, container, false);
        return view;
    }

}
