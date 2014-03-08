package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gabilheri.insuringmylife.fragments.AaaDialog;
import com.gabilheri.insuringmylife.fragments.DatePickerFragment;
import com.gabilheri.insuringmylife.fragments.NoInternetDialog;
import com.gabilheri.insuringmylife.fragments.TimePickerFragment;
import com.gabilheri.insuringmylife.helpers.House;
import com.gabilheri.insuringmylife.helpers.Vehicle;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ReportHouseClaim extends Activity {

    public ProgressDialog pDialog;
    public JSONParser jsonParser = new JSONParser();
    private Button dateButton, timeButton;
    private EditText claimDescription;
    private String user_id;
    private int claimNumber;


    public JSONArray properties = null;

    // Manage the properties with an Arraylist
    private ArrayList<HashMap<String, String>> propertiesList;

    private ArrayList<House> popUpProperties;
    private ArrayAdapter<House> propertiesAdapter;
    private Spinner propertiesSpinner, causeSpinner;

    // PHP script address

    private static final String NEW_CLAIM_URL = "http://162.243.225.173/InsuringMyLife/new_claim.php";
    private static final String VIEW_HOUSES_URL = "http://162.243.225.173/InsuringMyLife/view_houses.php";

    // JSON ID's
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_house_claim);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        causeSpinner = (Spinner) findViewById(R.id.selectCause);
        propertiesSpinner = (Spinner) findViewById(R.id.propertyClaim);

        timeButton = (Button) findViewById(R.id.timeButton);
        dateButton = (Button) findViewById(R.id.dateButton);

        claimDescription = (EditText) findViewById(R.id.claimDescription);
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
            Intent aboutAAA = new Intent(ReportHouseClaim.this, AboutAAA.class);
            startActivity(aboutAAA);
        } else if(id == 2) {
            Intent aboutIns = new Intent(ReportHouseClaim.this, AboutInsuringMyLife.class);
            startActivity(aboutIns);
        } else if(id == R.id.aaa_logo) {
            DialogFragment aaaD = new AaaDialog();
            aaaD.show(getFragmentManager(), "aaa");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getInternetState()) {
            new LoadProperties().execute();
        }
    }

    public void setPropertiesAdapter() {
        propertiesAdapter = new ArrayAdapter<House>(this, R.layout.spinner, popUpProperties);
        propertiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        propertiesSpinner.setAdapter(propertiesAdapter);
    }


    public void showDatePickerDialog(View v) {
        DialogFragment datePicker = new DatePickerFragment(v.getId());
        datePicker.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment timePicker = new TimePickerFragment(v.getId());
        timePicker.show(getFragmentManager(), "timePicker");
    }

    public void submitClaim(View view) {
        if(getInternetState()) {
            new SubmitClaim().execute();
        }
    }

    class LoadProperties extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReportHouseClaim.this);
            pDialog.setMessage("Loading information...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            updateJSONdata();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            setPropertiesAdapter();
            pDialog.dismiss();

        }
    }

    public boolean getInternetState() {

        ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            DialogFragment noInternet = new NoInternetDialog();
            noInternet.show(getFragmentManager(), "noInternet");
            return false;
        }
    }

    public void updateJSONdata() {

        // Initiate the ArrayList to hold the Json data
        propertiesList = new ArrayList<HashMap<String, String>>();
        popUpProperties = new ArrayList<House>();
        int success = 0;

        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            user_id = loginPref.getString("email", "");

            params.add(new BasicNameValuePair("user_id", user_id));

            JSONObject jObject = jsonParser.makeHttpRequest(VIEW_HOUSES_URL, "POST", params);

            success = jObject.getInt(TAG_SUCCESS);
            properties = jObject.getJSONArray("houses");

            if(success == 1) {

                for(int i = 0; i < properties.length(); i++) {
                    JSONObject obj = properties.getJSONObject(i);

                    // Create a new Vehicle
                    House myHouse = new House();
                    // Get each element based on it's tag
                    myHouse.setId(obj.getString(House.TAG_ID));
                    myHouse.setAddress(obj.getString(House.TAG_ADDRESS));
                    myHouse.setCity(obj.getString(House.TAG_CITY));
                    myHouse.setState(obj.getString(House.TAG_STATE));
                    myHouse.setZipCode(obj.getString(House.TAG_ZIPCODE));
                    myHouse.setPoliceNumber(obj.getString(House.TAG_POLICENUMBER));
                    myHouse.setPoliceNumber(obj.getString(House.TAG_PAYED));
                    myHouse.setPoliceNumber(obj.getString(Vehicle.TAG_POLICENUMBER));

                    popUpProperties.add(myHouse);
                }
            } else {
                String noProperties = "There's no persons yet! Click the Add property button to add some of your awesome house!";
                Toast.makeText(ReportHouseClaim.this, noProperties , Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class SubmitClaim extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReportHouseClaim.this);
            pDialog.setMessage("Reporting Claim...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String getClaimCause = causeSpinner.getSelectedItem().toString();
            House house = (House) propertiesSpinner.getSelectedItem();

            String[] date = dateButton.getText().toString().split("/");
            String[] time = timeButton.getText().toString().split(":");
            String[] time2 = time[1].split(" ");

            String hour = time[0];
            String minute = time2[0];
            String timePeriod = time2[1];
            String month = date[0];
            String day = date[1];
            String year = date[2];
            String propertyID = house.getId();
            String policeNumber = house.getPoliceNumber();
            String description = claimDescription.getText().toString();

            Random randomNumber = new Random();
            claimNumber = randomNumber.nextInt(999999999);

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            String email = loginPref.getString("email", "");


            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_id", email));
                params.add(new BasicNameValuePair("claim_type", "property"));
                params.add(new BasicNameValuePair("claim_object_id", propertyID));
                params.add(new BasicNameValuePair("police_number", policeNumber));
                params.add(new BasicNameValuePair("claim_number", Integer.toString(claimNumber)));
                params.add(new BasicNameValuePair("claim_year", year));
                params.add(new BasicNameValuePair("claim_month", month));
                params.add(new BasicNameValuePair("claim_day", day));
                params.add(new BasicNameValuePair("claim_hour", hour));
                params.add(new BasicNameValuePair("claim_minute", minute));
                params.add(new BasicNameValuePair("claim_time_period", timePeriod));
                params.add(new BasicNameValuePair("claim_cause", getClaimCause));
                params.add(new BasicNameValuePair("claim_description", description));

                JSONObject json = jsonParser.makeHttpRequest(NEW_CLAIM_URL, "POST", params);

                Log.d("New Claim Attempt", json.toString());

                int success = json.getInt(TAG_SUCCESS);

                if(success == 1) {
                    Intent afterClaim = new Intent(ReportHouseClaim.this, AfterClaim.class);
                    afterClaim.putExtra("claimID", claimNumber);
                    afterClaim.putExtra("claimType", "property");
                    startActivity(afterClaim);
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);
                }

            } catch(JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();

        }
    }

}
