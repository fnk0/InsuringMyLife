package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.DialogFragment;
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

import com.gabilheri.insuringmylife.fragments.AaaDialog;

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
                claimsIntent.putExtra("claimType", "vehicle");
                startActivity(claimsIntent);
                break;
            case R.id.quoteButton:
                Intent quoteIntent = new Intent(CarActivity.this, VehicleQuoteActivity.class);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.initial, menu);

        menu.add(1, 1, 1, "About AAA");
        menu.add(2, 2, 2, "About Insuring My Life");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if(id == 1) {
            Intent aboutAAA = new Intent(CarActivity.this, AboutAAA.class);
            startActivity(aboutAAA);
        } else if(id == 2) {
            Intent aboutIns = new Intent(CarActivity.this, AboutInsuringMyLife.class);
            startActivity(aboutIns);
        } else if(id == R.id.aaa_logo) {
            DialogFragment aaaD = new AaaDialog();
            aaaD.show(getFragmentManager(), "aaa");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
        return;
    }
}
