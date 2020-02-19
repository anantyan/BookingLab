package com.nursinglab.booking.activity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nursinglab.booking.R;
import com.nursinglab.booking.api.Auth;
import com.nursinglab.booking.component.RecordsComponent;
import com.nursinglab.booking.component.ResponseComponent;
import com.nursinglab.booking.component.SharedPreferenceComponent;
import com.nursinglab.booking.fragment.AllBookingFragment;
import com.nursinglab.booking.fragment.MyBookingFragment;
import com.nursinglab.booking.fragment.ProfileFragment;
import com.nursinglab.booking.util.RetrofitUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id._bottom_navigation) BottomNavigationView bottomNav;
    @BindView(R.id.toolbar) Toolbar toolbar;

    int getMenuChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);

        setSupportActionBar(toolbar);

        bottomNav.setOnNavigationItemSelectedListener(navBar);
        bottomNav.getMenu().findItem(R.id.nav_my_booking).setChecked(true); // make checked Home Fragment
        //bottomNav.getMenu().findItem(R.id.nav_home).setEnabled(false); // make unchecked Home Fragment

        loadFragment(new MyBookingFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navBar = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectFrag;

            getMenuChecked = menuItem.getItemId();
            for (int i=0; i<bottomNav.getMenu().size(); i++){
                MenuItem menuT = bottomNav.getMenu().getItem(i);
                //boolean isEnable = menuT.getItemId() != getMenuChecked;
                boolean isChecked = menuT.getItemId() == getMenuChecked;
                //menuT.setEnabled(isEnable);
                menuT.setChecked(isChecked);
            }

            switch (menuItem.getItemId()){
                case R.id.nav_my_booking:
                    selectFrag = new MyBookingFragment();
                    loadFragment(selectFrag);
                    toolbar.setTitle(R.string.app_name);
                    return true;
                case R.id.nav_all_booking:
                    selectFrag = new AllBookingFragment();
                    loadFragment(selectFrag);
                    toolbar.setTitle("All Booking");
                    return true;
                case R.id.nav_olahraga:
                    selectFrag = new ProfileFragment();
                    loadFragment(selectFrag);
                    toolbar.setTitle("Profile");
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.commit();
    }
}