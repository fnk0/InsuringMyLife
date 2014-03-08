package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gabilheri.insuringmylife.fragments.AaaDialog;

public class ReportClaims extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_claims);
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
            Intent aboutAAA = new Intent(ReportClaims.this, AboutAAA.class);
            startActivity(aboutAAA);
        } else if(id == 2) {
            Intent aboutIns = new Intent(ReportClaims.this, AboutInsuringMyLife.class);
            startActivity(aboutIns);
        } else if(id == R.id.aaa_logo) {
            DialogFragment aaaD = new AaaDialog();
            aaaD.show(getFragmentManager(), "aaa");
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
