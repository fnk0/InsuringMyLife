package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class InitialActivity extends Activity implements View.OnClickListener {

    private ArrayList<String> drawerOptions;
    private DrawerLayout optionsDrawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private Button lifeButton, carButton, houseButton, accidentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        title = drawerTitle = getTitle();

        optionsDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, optionsDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a Drawer has settled in a completely closed state. */

            public void onDrawerClosed(View view) {
                getActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu();
            }
        };

        optionsDrawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerOptions = new ArrayList<String>();


        SharedPreferences profileCounter = getSharedPreferences("numProfiles", MODE_PRIVATE);
        int counter = profileCounter.getInt("numProfiles", 0);

        for(int x = 0; x < counter; x++) {
            SharedPreferences profiles = getSharedPreferences("profilesPref" + x, MODE_PRIVATE);
            drawerOptions.add(profiles.getString("fullName", "name"));
        }

        drawerOptions.add("New Profile");
        drawerOptions.add("Logout");
        optionsDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the listView
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerOptions));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        lifeButton = (Button) findViewById(R.id.lifeButton);
        carButton = (Button) findViewById(R.id.carButton);
        houseButton = (Button) findViewById(R.id.houseButton);
        accidentButton = (Button) findViewById(R.id.accidentButton);

        carButton.setOnClickListener(this);
        lifeButton.setOnClickListener(this);
        houseButton.setOnClickListener(this);
        accidentButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.lifeButton:
                break;
            case R.id.carButton:
                Intent carActivity = new Intent(InitialActivity.this, CarActivity.class);
                startActivity(carActivity);
                break;
            case R.id.accidentButton:
                break;
            case R.id.houseButton:
                Intent houseActivity = new Intent(InitialActivity.this, HouseActivity.class);
                startActivity(houseActivity);
                break;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
            optionsDrawerLayout.closeDrawer(drawerList);
        }
    }

    private void selectItem(int position) {

        String selectedProfileName;

        if(drawerOptions.get(position).equals("Logout")) {
            SharedPreferences loginPref = getSharedPreferences("loginPref", 0);
            SharedPreferences.Editor loginEditor = loginPref.edit();
            loginEditor.clear();
            loginEditor.commit();

            SharedPreferences profileCounter = getSharedPreferences("numProfiles", MODE_PRIVATE);
            int counter = profileCounter.getInt("numProfiles", 0);

            for(int x = 0; x < counter; x++) {
                SharedPreferences profiles = getSharedPreferences("profilesPref" + x, MODE_PRIVATE);
                SharedPreferences.Editor profilesEditor = profiles.edit();
                profilesEditor.clear();
                profilesEditor.commit();
            }

            Intent i = new Intent(InitialActivity.this, LoginActivity.class);
            startActivity(i);
        } else if(drawerOptions.get(position).equals("New Profile")) {
            Intent i = new Intent(InitialActivity.this, NewProfileActivity.class);
            startActivity(i);
        } else {
            selectedProfileName = drawerOptions.get(position).toString();
            SharedPreferences selectedProfile = getSharedPreferences("selectedProfile", MODE_PRIVATE);
            SharedPreferences.Editor selectedProfileEditor = selectedProfile.edit();
            selectedProfileEditor.putString("selectedProfile", selectedProfileName);
            selectedProfileEditor.commit();
        }
    }
}
