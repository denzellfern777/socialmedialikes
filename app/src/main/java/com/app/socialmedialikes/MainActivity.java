package com.app.socialmedialikes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static DrawerLayout drawer;
    private static MainActivity mInstance;
    private AppBarConfiguration mAppBarConfiguration;
    private LinearLayout signOutBtn;

    public static synchronized MainActivity getInstance() {
        return mInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mInstance = this;

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View hView = navigationView.getHeaderView(0);
        signOutBtn = findViewById(R.id.signout_tv);
        signOutBtn.setOnClickListener(this);
        TextView nav_user = hView.findViewById(R.id.nav_header_username);
        TextView nav_email = hView.findViewById(R.id.nav_header_email);
        TextView profile = hView.findViewById(R.id.imageView);
        nav_user.setText(SaveSharedPreference.getPrefFullName(this));
        nav_email.setText(SaveSharedPreference.getPrefEmail(this));

        String username = SaveSharedPreference.getUserName(this);

        if (username != null) {
            String subString = username.substring(0, 2);
            profile.setText(subString.toUpperCase());
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_watch_n_earn, R.id.nav_reward_history, R.id.nav_sm_profiles
//                , R.id.nav_share
//                , R.id.nav_send
        )
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void closeApp() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Do you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {

        Fragment navController1 = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        int count = navController1.getChildFragmentManager().getBackStackEntryCount();

        if (count != 0) {

            navController1.getChildFragmentManager().popBackStackImmediate();
        } else {
            closeApp();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == signOutBtn) {
            signOut();
        }
    }

    private void signOut() {

        new MaterialAlertDialogBuilder(this)
                .setTitle("Sign Out")
                .setMessage(getString(R.string.sign_out_msg))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "You Have been Signed Out.", Toast.LENGTH_SHORT).show();
                        SaveSharedPreference.setPrefWalletBalance(MainActivity.this, "0");
                        SaveSharedPreference.setUserName(MainActivity.this, "");
                        SaveSharedPreference.setPrefFullName(MainActivity.this, "");
                        SaveSharedPreference.setPrefEmail(MainActivity.this, "");
                        SaveSharedPreference.setPrefPassword(MainActivity.this, "");
                        SaveSharedPreference.setFirstLaunch(MainActivity.this, "firstLaunch");
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.sign_out_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                })
                .show();
    }

}
