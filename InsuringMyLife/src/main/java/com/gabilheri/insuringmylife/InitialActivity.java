package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.targets.ViewTarget;
import com.gabilheri.insuringmylife.fragments.AaaDialog;

import java.util.ArrayList;

public class InitialActivity extends Activity  {

    private ArrayList<String> drawerOptions;
    private DrawerLayout optionsDrawerLayout;
    private Button carButton, healthButton, houseButton, accidenButton;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private TextView userName;
    private boolean tutorialOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        userName = (TextView) findViewById(R.id.userName);

        SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
        userName.setText("Hello " + loginPref.getString("name", "a") + " " + loginPref.getString("last_name", "b"));
        title = drawerTitle = getTitle();

        carButton = (Button) findViewById(R.id.carButton);
        healthButton = (Button) findViewById(R.id.lifeButton);
        houseButton = (Button) findViewById(R.id.houseButton);
        accidenButton = (Button) findViewById(R.id.accidentButton);

        carButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setBackgroundResource(R.drawable.car_button_pressed);
                return false;
            }
        });

        healthButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setBackgroundResource(R.drawable.life_button_pressed);
                return false;
            }
        });

        accidenButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setBackgroundResource(R.drawable.accident_button_pressed);
                return false;
            }
        });

        houseButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setBackgroundResource(R.drawable.house_button_pressed);
                return false;
            }
        });

        SharedPreferences tutorialPref = getSharedPreferences("tutorialPref", MODE_PRIVATE);

        // Checks if the tutorial has been launched already.
        if(tutorialPref.contains("tutorialOn")) {
            tutorialOn = tutorialPref.getBoolean("tutorialOn", false);
        } else {
            tutorialOn = true;
        }

        optionsDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, optionsDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a Drawer has settled in a completely closed state. */

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

        drawerOptions.add("Useful Information");
        drawerOptions.add("Update Profile");
        drawerOptions.add("Take a Quiz!");
        drawerOptions.add("Logout");
        optionsDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the listView
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerOptions));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        if(tutorialOn) {
           displayTutorial();
        }
    }

    public void changeActivity(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.lifeButton:
                Intent lifeActivity = new Intent(InitialActivity.this, HealthActivity.class);
                startActivity(lifeActivity);
                break;
            case R.id.carButton:
                Intent carActivity = new Intent(InitialActivity.this, CarActivity.class);
                startActivity(carActivity);
                break;
            case R.id.accidentButton:
                Intent accidentActivity = new Intent(InitialActivity.this, ReportClaims.class);
                startActivity(accidentActivity);
                break;
            case R.id.houseButton:
                Intent houseActivity = new Intent(InitialActivity.this, HouseActivity.class);
                startActivity(houseActivity);
                break;
        }
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.initial, menu);

        menu.add(1, 1, 1, "Tutorial");
        menu.add(2, 2, 2, "About AAA");
        menu.add(3, 3, 3, "About Insuring My Life");
        return true;
    }

    public void aaaInfo() {
        DialogFragment goToAAA = new AaaDialog();
        goToAAA.show(getFragmentManager(), "aaa");
    }

    public void displayTutorial() {
        // Displays the showcase view with tutorial
        View showcasedView = findViewById(R.id.carButton);
        ViewTarget target = new ViewTarget(showcasedView);
        target.getPoint();
        ShowcaseView.insertShowcaseView(target, this, "Icon Selection", "Please select one of the icons shown below");

        // Sets the tutorial to OFF so it doesn't shows up all the time to the user
        SharedPreferences tutorialPref = getSharedPreferences("tutorialPref", MODE_PRIVATE);
        SharedPreferences.Editor tutorialPrefEditor = tutorialPref.edit();
        tutorialPrefEditor.putBoolean("tutorialOn", false);
        tutorialPrefEditor.commit();
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
            SharedPreferences tutorialPref = getSharedPreferences("tutorialPref", MODE_PRIVATE);
            SharedPreferences.Editor tutorialPrefEditor = tutorialPref.edit();
            tutorialPrefEditor.putBoolean("tutorialOn", true);
            tutorialPrefEditor.commit();
            displayTutorial();
        } else if(id == 2) {
            Intent aboutAAA = new Intent(InitialActivity.this, AboutAAA.class);
            startActivity(aboutAAA);
        } else if(id == 3) {
            Intent aboutIns = new Intent(InitialActivity.this, AboutInsuringMyLife.class);
            startActivity(aboutIns);
        }

        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
            optionsDrawerLayout.closeDrawer(drawerList);
        }
    }

    private void selectItem(int position) {

        if(drawerOptions.get(position).equals("Logout")) {
            SharedPreferences loginPref = getSharedPreferences("loginPref", 0);
            SharedPreferences.Editor loginEditor = loginPref.edit();
            loginEditor.clear();
            loginEditor.commit();

            Intent i = new Intent(InitialActivity.this, LoginActivity.class);
            startActivity(i);
        } else if(drawerOptions.get(position).equals("Update Profile")) {
            Intent i = new Intent(InitialActivity.this, UpdateProfileActivity.class);
            startActivity(i);
        } else if(drawerOptions.get(position).equals("Useful Information")) {
            Intent i = new Intent(InitialActivity.this, Information.class);
            startActivity(i);
        } else if(drawerOptions.get(position).equals("Take a Quiz!")) {
            Intent i = new Intent(InitialActivity.this, ViewQuizzes.class);
            startActivity(i);
        } else {

        }
    }
}
