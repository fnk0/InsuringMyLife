package com.gabilheri.insuringmylife;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CarActivity extends ListActivity {

    private String profileName;
    private TextView profileTextView;

    // Rapid navigation menu tags
    private static final int MAIN_MENU = 300;
    private static final int HOUSE_INSURANCE = 301;
    private static final int LIFE_INSURANCE = 302;
    private static final int REPORT_ACCIDENT = 303;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        SharedPreferences sp = getSharedPreferences("selectedProfile", MODE_PRIVATE);
        profileName = sp.getString("selectedProfile", "No profiles Selected");

        profileTextView = (TextView) findViewById(R.id.profileName);
        profileTextView.setText("Hello " + profileName);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView listView = (ListView) findViewById(android.R.id.list);
        String[] values = getResources().getStringArray(R.array.car_acitivy_list);

        final ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < values.length; i++) {
            list.add(values[i]);
        }

        setListAdapter(new MyArrayAdapter(this, values));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                switch (position) {
                    case 0:
                        Intent i = new Intent(CarActivity.this, VehiclesActivity.class);
                        startActivity(i);
                        break;
                }
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstance) {
        super.onPostCreate(savedInstance);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

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
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        return;
    }
}
