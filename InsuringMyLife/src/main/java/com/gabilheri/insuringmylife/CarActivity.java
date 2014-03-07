package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CarActivity extends Activity {

    private TextView profileTextView;
    private Button vehiclesButton, claimsButton, quoteButton, agentButton;

    // Rapid navigation menu tags
    private static final int MAIN_MENU = 300;
    private static final int HOUSE_INSURANCE = 301;
    private static final int LIFE_INSURANCE = 302;
    private static final int REPORT_ACCIDENT = 303;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        SharedPreferences sp = getSharedPreferences("loginPref", MODE_PRIVATE);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        vehiclesButton = (Button) findViewById(R.id.vehiclesButton);
        claimsButton = (Button) findViewById(R.id.claimsButton);
        quoteButton = (Button) findViewById(R.id.quoteButton);

    }

    public void changeActivity(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.vehiclesButton:
                Intent vehiclesIntent = new Intent(CarActivity.this, VehiclesActivity.class);
                startActivity(vehiclesIntent);
                break;
            case R.id.claimsButton:
                Intent claimsIntent = new Intent(CarActivity.this, ViewClaims.class);
                startActivity(claimsIntent);
                break;
            case R.id.quoteButton:
                Intent quoteIntent = new Intent(CarActivity.this, QuoteActivity.class);
                startActivity(quoteIntent);
                break;
            case R.id.agentButton:
                Intent agentIntent = new Intent(CarActivity.this, MyAgent.class);
                startActivity(agentIntent);
                break;
        }
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
