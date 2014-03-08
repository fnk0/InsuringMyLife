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

public class ReportVehicleClaim extends Activity {

    public ProgressDialog pDialog;
    public JSONParser jsonParser = new JSONParser();
    private Button dateButton, timeButton;
    private EditText claimDescription;
    private String user_id;
    private int claimNumber;

    // Create an Array to hold the vehicles
    public JSONArray vehicles = null;

    // Manage the vehicles with an Arraylist
    private ArrayList<HashMap<String, String>> vehiclesList;

    private ArrayList<Vehicle> popUpVehicles;
    private ArrayAdapter<Vehicle> vehiclesAdapter;
    private Spinner vehiclesSpinner, causeSpinner;

    // PHP script address

    private static final String NEW_CLAIM_URL = "http://162.243.225.173/InsuringMyLife/new_claim.php";

    // JSON ID's
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_vehicle_claim);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        causeSpinner = (Spinner) findViewById(R.id.selectCause);
        vehiclesSpinner = (Spinner) findViewById(R.id.vehiclesInvolved);

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
            Intent aboutAAA = new Intent(ReportVehicleClaim.this, AboutAAA.class);
            startActivity(aboutAAA);
        } else if(id == 2) {
            Intent aboutIns = new Intent(ReportVehicleClaim.this, AboutInsuringMyLife.class);
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
            new LoadVehicles().execute();
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

    public void setVehiclesAdapter() {
        vehiclesAdapter = new ArrayAdapter<Vehicle>(this, R.layout.spinner, popUpVehicles);
        vehiclesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehiclesSpinner.setAdapter(vehiclesAdapter);
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

    class LoadVehicles extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReportVehicleClaim.this);
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
            setVehiclesAdapter();
            pDialog.dismiss();

        }
    }

    public void updateJSONdata() {

        // Initiate the ArrayList to hold the Json data
        vehiclesList = new ArrayList<HashMap<String, String>>();
        popUpVehicles = new ArrayList<Vehicle>();
        int success = 0;

        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            user_id = loginPref.getString("email", "");

            params.add(new BasicNameValuePair(Vehicle.TAG_USERID, user_id));

            JSONObject jObject = jsonParser.makeHttpRequest(Vehicle.VEHICLES_URL, "POST", params);

            success = jObject.getInt(TAG_SUCCESS);
            vehicles = jObject.getJSONArray(Vehicle.TAG_VEHICLES);

            if(success == 1) {
                // loop through all the vehicles
                for(int i = 0; i < vehicles.length(); i++) {
                    JSONObject obj = vehicles.getJSONObject(i);

                    // Create a new Vehicle
                    Vehicle myVehicle = new Vehicle();
                    // Get each element based on it's tag
                    myVehicle.setId(obj.getString(Vehicle.TAG_ID));
                    myVehicle.setYear(obj.getString(Vehicle.TAG_YEAR));
                    myVehicle.setModel(obj.getString(Vehicle.TAG_MODEL));
                    myVehicle.setBrand(obj.getString(Vehicle.TAG_BRAND));
                    myVehicle.setColor(obj.getString(Vehicle.TAG_COLOR));
                    myVehicle.setLicensePlate(obj.getString(Vehicle.TAG_LICENSE));
                    myVehicle.setMainDriver(obj.getString(Vehicle.TAG_DRIVER));
                    myVehicle.setPoliceNumber(obj.getString(Vehicle.TAG_POLICENUMBER));
                    myVehicle.setDriverLicense(obj.getString(Vehicle.TAG_DRIVER_LICENSE));
                    myVehicle.setLicenseState(obj.getString(Vehicle.TAG_LICENSE_STATE));
                    myVehicle.setDriverBirthday(obj.getString(Vehicle.TAG_BIRTHDAY_MONTH) + "/" +
                            obj.getString(Vehicle.TAG_BIRTHDAY_DAY) + "/" +
                            obj.get(Vehicle.TAG_BIRTHDAY_YEAR));
                    myVehicle.setDriverGender(obj.getString(Vehicle.TAG_DRIVER_GENDER));

                    popUpVehicles.add(myVehicle);
                }
            } else {
                String noVehicles = "There's no vehicles yet! Click the Add vehicle button to add some of your awesome cars!";
                Toast.makeText(ReportVehicleClaim.this, noVehicles, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class SubmitClaim extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReportVehicleClaim.this);
            pDialog.setMessage("Reporting Claim...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String getClaimCause = causeSpinner.getSelectedItem().toString();
            Vehicle vehicle = (Vehicle) vehiclesSpinner.getSelectedItem();

            String[] date = dateButton.getText().toString().split("/");
            String[] time = timeButton.getText().toString().split(":");
            String[] time2 = time[1].split(" ");

            String hour = time[0];
            String minute = time2[0];
            String timePeriod = time2[1];
            String month = date[0];
            String day = date[1];
            String year = date[2];
            String vehicleID = vehicle.getId();
            String vehiclePoliceNumber = vehicle.getPoliceNumber();
            String description = claimDescription.getText().toString();

            Random randomNumber = new Random();
            claimNumber = randomNumber.nextInt(999999999);

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            String email = loginPref.getString("email", "");


            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_id", email));
                params.add(new BasicNameValuePair("claim_type", "vehicle"));
                params.add(new BasicNameValuePair("claim_object_id", vehicleID));
                params.add(new BasicNameValuePair("police_number", vehiclePoliceNumber));
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
                    Intent afterClaim = new Intent(ReportVehicleClaim.this, AfterClaim.class);

                    afterClaim.putExtra("claimID", claimNumber);
                    afterClaim.putExtra("claimType", "vehicle");
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
