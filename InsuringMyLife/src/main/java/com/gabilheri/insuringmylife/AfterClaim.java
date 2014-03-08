package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class AfterClaim extends Activity {

    private TextView claimID;
    private String claimType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_claim);

        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        claimType = getIntent().getStringExtra("claimType");

        claimID = (TextView) findViewById(R.id.claimID);
        claimID.setText(Integer.toString(getIntent().getIntExtra("claimID", 1612312)));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.after_claim, menu);
        return true;
    }

    public void viewClaims(View view) {
        Intent viewMyClaims = new Intent(AfterClaim.this, ViewClaims.class);
        viewMyClaims.putExtra("claimType", claimType);
        startActivity(viewMyClaims);
    }

    public void backToMain(View view) {
        Intent intent = new Intent(AfterClaim.this, InitialActivity.class);
        startActivity(intent);
    }

    public void contactAgent(View view) {
        Intent intent = new Intent(AfterClaim.this, MyAgent.class);
        startActivity(intent);
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
