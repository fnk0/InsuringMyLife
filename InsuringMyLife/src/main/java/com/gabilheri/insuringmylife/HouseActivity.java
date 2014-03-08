package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gabilheri.insuringmylife.fragments.AaaDialog;
import com.gabilheri.insuringmylife.helpers.House;

public class HouseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void changeActivity(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.contactAgent:
                Intent agentIntent = new Intent(HouseActivity.this, MyAgent.class);
                startActivity(agentIntent);
                break;
            case R.id.myHouses:
                Intent myHouses = new Intent(HouseActivity.this, ViewHouses.class);
                startActivity(myHouses);
                break;
            case R.id.getQuote:
                Intent houseQuote = new Intent(HouseActivity.this, HouseQuote.class);
                startActivity(houseQuote);
                break;
            case R.id.existingClaims:
                Intent claimsIntent = new Intent(HouseActivity.this, ViewClaims.class);
                claimsIntent.putExtra("claimType", "property");
                startActivity(claimsIntent);
                break;
        }
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
            Intent aboutAAA = new Intent(HouseActivity.this, AboutAAA.class);
            startActivity(aboutAAA);
        } else if(id == 2) {
            Intent aboutIns = new Intent(HouseActivity.this, AboutInsuringMyLife.class);
            startActivity(aboutIns);
        } else if(id == R.id.aaa_logo) {
            DialogFragment aaaD = new AaaDialog();
            aaaD.show(getFragmentManager(), "aaa");
        }

        return super.onOptionsItemSelected(item);
    }

}
