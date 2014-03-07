package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class HealthActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void changeActivity(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.familyMembers:
                Intent familyIntent = new Intent(HealthActivity.this, FamilyMembers.class);
                startActivity(familyIntent);
                break;

            case R.id.existingClaims:
                Intent claimsHealth = new Intent(HealthActivity.this, ViewClaims.class);
                claimsHealth.putExtra("claimType", "person");
                startActivity(claimsHealth);
                break;

            case R.id.getQuote:
                Intent healthQuote = new Intent(HealthActivity.this, HealthQuote.class);
                startActivity(healthQuote);
                break;

            case R.id.contactAgent:
                Intent agentIntent = new Intent(HealthActivity.this, MyAgent.class);
                startActivity(agentIntent);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.health, menu);
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
}
