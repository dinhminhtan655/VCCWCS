package com.wcs.vcc.main.tripdelivery;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.MenuItem;

import com.wcs.wcs.R;
import com.wcs.vcc.main.EmdkActivity;
import com.wcs.vcc.main.tripdelivery.fragmenttrips.CurrentTripsFragment;
import com.wcs.vcc.main.tripdelivery.fragmenttrips.DenyTripsFragment;
import com.wcs.vcc.main.tripdelivery.fragmenttrips.HistoryTripsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripDeliveryActivity extends EmdkActivity{


    @BindView(R.id.botNavi)
    BottomNavigationView navi;
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_delivery);

        ButterKnife.bind(this);
        setTitle("Hiện tại");
        fragment = new CurrentTripsFragment();
        loadFragment(fragment);
        navi.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_tripdelivery);
        if (fragment != null && fragment instanceof CurrentTripsFragment){
            finish();
        }else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0){
                getSupportFragmentManager().popBackStack();
            }else {
                super.onBackPressed();
            }
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.navi_current:
                    fragment = new CurrentTripsFragment();
                    loadFragment(fragment);
                    setTitle("Hiện tại");
                    break;

                case R.id.navi_history:
                    fragment = new HistoryTripsFragment();
                    loadFragment(fragment);
                    setTitle("Lịch sử");
                    break;

                case R.id.navi_deny:
                    fragment = new DenyTripsFragment();
                    loadFragment(fragment);
                    setTitle("Từ chối");
                    break;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_tripdelivery, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
