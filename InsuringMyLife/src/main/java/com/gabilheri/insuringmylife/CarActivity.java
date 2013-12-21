package com.gabilheri.insuringmylife;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CarActivity extends Activity {

    private ArrayList<String> drawerOptions;
    private DrawerLayout optionsDrawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private String profileName;
    private TextView profileTextView;

    private static final int MANAGE_VEHICLES = 0;
    private static final int ADD_NEW_VEHICLE = 1;
    private static final int INSURANCE_PLANS = 2;
    private static final int GET_QUOTE = 3;
    private static final int MAIN_MENU = 4;
    private static final int HOUSE_INSURANCE = 5;
    private static final int LIFE_INSURANCE = 6;
    private static final int REPORT_ACCIDENT = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        SharedPreferences sp = getSharedPreferences("selectedProfile", MODE_PRIVATE);
        profileName = sp.getString("selectedProfile", "No profiles Selected");

        profileTextView = (TextView) findViewById(R.id.profileName);
        profileTextView.setText("Hello " + profileName);

        title = drawerTitle = getTitle();

        optionsDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, optionsDrawerLayout,
                   R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

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

        drawerOptions.add("Manage my vehicles");
        drawerOptions.add("Add new vehicle");
        drawerOptions.add("Insurance plans");
        drawerOptions.add("Get a Quote");
        drawerOptions.add("Main Menu");
        drawerOptions.add("House Insurance");
        drawerOptions.add("Life Insurance");
        drawerOptions.add("Report an Accident");

        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerOptions));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    @Override
    protected void onPostCreate(Bundle savedInstance) {
        super.onPostCreate(savedInstance);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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
        }
    }

    private void selectItem(int position) {

        switch (position) {
            case MANAGE_VEHICLES:
                break;

        }
    }
}
