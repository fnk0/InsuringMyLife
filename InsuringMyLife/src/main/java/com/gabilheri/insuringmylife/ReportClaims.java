package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ReportClaims extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_claims);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.report_claims, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeActivity(View view) {

        int id = view.getId();

        switch (id) {
            case R.id.vehicleClaims:
                Intent vehicles = new Intent(ReportClaims.this, ReportVehicleClaim.class);
                startActivity(vehicles);
                break;
            case R.id.houseClaim:
                Intent houses = new Intent(ReportClaims.this, ReportHouseClaim.class);
                startActivity(houses);
                break;
            case R.id.healthClaim:
                Intent health = new Intent(ReportClaims.this, ReportHealthClaim.class);
                startActivity(health);
                break;

        }
    }

}
