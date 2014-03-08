package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gabilheri.insuringmylife.fragments.AaaDialog;

public class InsuInfo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insu_info);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void goToQuizz(View view) {
        Intent intent = new Intent(InsuInfo.this, QuizActivity.class);
        startActivity(intent);
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
            Intent aboutAAA = new Intent(InsuInfo.this, AboutAAA.class);
            startActivity(aboutAAA);
        } else if(id == 2) {
            Intent aboutIns = new Intent(InsuInfo.this, AboutInsuringMyLife.class);
            startActivity(aboutIns);
        } else if(id == R.id.aaa_logo) {
            DialogFragment aaaD = new AaaDialog();
            aaaD.show(getFragmentManager(), "aaa");
        }

        return super.onOptionsItemSelected(item);
    }

}
