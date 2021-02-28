package com.laith.weatherforecast;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.laith.fragment.TodayFragment;
import com.laith.fragment.WeeklyFragment;

public class WeatherActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //Check network
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isConnectedOrConnecting()) {
            Toast.makeText(this, getString(R.string.error_network), Toast.LENGTH_LONG).show();
        }

        //Action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.activity_current));

        //Navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutMain);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navigationViewMain);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_weather:
                        currentFragment = new TodayFragment();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, currentFragment)
                                .addToBackStack(null)
                                .commit();
                        getSupportActionBar().setTitle(getString(R.string.activity_current));
                        break;
                    case R.id.navigation_weekly:
                        currentFragment = new WeeklyFragment();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, currentFragment)
                                .addToBackStack(null)
                                .commit();
                        getSupportActionBar().setTitle(getString(R.string.activity_weekly));
                        break;
                    case R.id.navigation_setting:
                        startActivity(new Intent(WeatherActivity.this, SettingActivity.class));
                        break;
                    case R.id.navigation_exit:
                        android.os.Process.killProcess(android.os.Process.myPid());
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //Main view
        currentFragment = new TodayFragment();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, currentFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
