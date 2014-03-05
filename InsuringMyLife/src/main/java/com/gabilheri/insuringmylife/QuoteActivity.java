package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.gabilheri.insuringmylife.helpers.Vehicle;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuoteActivity extends Activity {

    public ProgressDialog pDialog;
    public JSONParser jsonParser = new JSONParser();

    private String user_id;

    // Create an Array to hold the vehicles
    public JSONArray vehicles = null;

    // Manage the vehicles with an Arraylist
    private ArrayList<HashMap<String, String>> vehiclesList;

    private ArrayList<Vehicle> popUpVehicles;
    private ArrayAdapter<Vehicle> vehiclesAdapter;
    private Spinner spinnerVehicles, spinnerNumVehicles, spinnerNumDrivers, spinnerResidenceType;
    private RadioGroup currentCustomer, isStudent, isMarried, isFinanced;
    private LinearLayout spinnersLayout;
    private ArrayList<Spinner> spinnersArray;

    // PHP script address

    private static final String VEHICLES_URL = "http://162.243.225.173/InsuringMyLife/view_vehicles.php";

    // JSON ID's
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USERID = "user_id";
    private static final String TAG_VEHICLES = "vehicles";
    private static final String TAG_YEAR = "year";
    private static final String TAG_MODEL = "model";
    private static final String TAG_BRAND = "brand";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        spinnersArray = new ArrayList<Spinner>();
        spinnerVehicles = (Spinner) findViewById(R.id.selectCars);
        spinnersLayout = (LinearLayout) findViewById(R.id.vehicleHolder);

        spinnerNumDrivers = (Spinner) findViewById(R.id.numDrivers);
        spinnerVehicles = (Spinner) findViewById(R.id.numVehicles);
        spinnerResidenceType = (Spinner) findViewById(R.id.residenceType);
        currentCustomer = (RadioGroup) findViewById(R.id.currentCustomer);
        isStudent = (RadioGroup) findViewById(R.id.isStudent);
        isMarried = (RadioGroup) findViewById(R.id.isMarried);
        isFinanced = (RadioGroup) findViewById(R.id.vehicleFinanced);

    }

    public void setVehiclesAdapter() {

        vehiclesAdapter = new ArrayAdapter<Vehicle>(this, R.layout.spinner, popUpVehicles);
        vehiclesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicles.setAdapter(vehiclesAdapter);
        spinnersArray.add(spinnerVehicles);
    }

    public void addNewVehicle(View view) {

        Spinner mySpinner = new Spinner(view.getContext());
        vehiclesAdapter = new ArrayAdapter<Vehicle>(this, R.layout.spinner, popUpVehicles);
        vehiclesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(vehiclesAdapter);
        spinnersArray.add(mySpinner);

        spinnersLayout.addView(mySpinner);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadVehicles().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pDialog.dismiss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quote, menu);
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

    class LoadVehicles extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(QuoteActivity.this);
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

            params.add(new BasicNameValuePair(TAG_USERID, user_id));

            JSONObject jObject = jsonParser.makeHttpRequest(VEHICLES_URL, "POST", params);

            success = jObject.getInt(TAG_SUCCESS);
            vehicles = jObject.getJSONArray(TAG_VEHICLES);

            if(success == 1) {
                // loop through all the vehicles
                for(int i = 0; i < vehicles.length(); i++) {
                    JSONObject obj = vehicles.getJSONObject(i);

                    // Get each element based on it's tag
                    String id = obj.getString("id");
                    String year = obj.getString(TAG_YEAR);
                    String model = obj.getString(TAG_MODEL);
                    String brand = obj.getString(TAG_BRAND);

                    Vehicle myVehicle = new Vehicle(id, brand, year, model);

                    popUpVehicles.add(myVehicle);
                }
            } else {
                String noVehicles = "There's no vehicles yet! Click the Add vehicle button to add some of your awesome cars!";
                Toast.makeText(QuoteActivity.this, noVehicles, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getQuote(View view) {

        double startingPrice = 2049;

        int numVehicles = spinnerNumVehicles.getSelectedItemPosition() + 1;
        int numDrivers = spinnerNumDrivers.getSelectedItemPosition() + 1;
        String residenceInformation = spinnerResidenceType.getSelectedItem().toString();

        boolean isCustomer = false;
        boolean student = false;
        boolean vehicleFinanced = false;

        if(currentCustomer.getCheckedRadioButtonId() == R.id.yes) {
            isCustomer = true;
            startingPrice -= 200;
        } else {
            startingPrice += 40;
        }

        if(isStudent.getCheckedRadioButtonId() == R.id.studentYes) {
            student = true;
            startingPrice -= 175;
        } else {
            startingPrice += 50;
        }

        String marriageType = "";

        switch (isMarried.getCheckedRadioButtonId()) {
            case R.id.marriedYes:
                marriageType = "Married";
                startingPrice -= 150;
                break;
            case R.id.marriedNo:
                marriageType = "Single";
                startingPrice += 50;
                break;
            case R.id.divorced:
                marriageType = "Divorced";
                break;
        }

        if(isFinanced.getCheckedRadioButtonId() == R.id.financedYes) {
            vehicleFinanced = true;
            startingPrice += 80;
        } else {
            startingPrice -= 80;
        }


        Intent intent = new Intent(QuoteActivity.this, QuoteValueActivity.class);
        startActivity(intent);
    }

}
