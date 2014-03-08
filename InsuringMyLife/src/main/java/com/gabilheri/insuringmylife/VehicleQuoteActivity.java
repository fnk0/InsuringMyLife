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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.gabilheri.insuringmylife.fragments.AaaDialog;
import com.gabilheri.insuringmylife.fragments.NoInternetDialog;
import com.gabilheri.insuringmylife.helpers.Vehicle;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VehicleQuoteActivity extends Activity {

    public ProgressDialog pDialog;
    public JSONParser jsonParser = new JSONParser();

    private String user_id;

    // Create an Array to hold the vehicles
    public JSONArray vehicles = null;

    // Manage the vehicles with an Arraylist
    private ArrayList<HashMap<String, String>> vehiclesList;

    private ArrayList<Vehicle> popUpVehicles;
    private ArrayAdapter<Vehicle> vehiclesAdapter;
    private Spinner spinnerNumVehicles, spinnerNumDrivers, spinnerResidenceType;
    private RadioGroup currentCustomer, isStudent, isMarried, isFinanced;
    private LinearLayout spinnersLayout;
    private ArrayList<Spinner> spinnersArray;

    // PHP script address

    private static final String VEHICLES_URL = "http://162.243.225.173/InsuringMyLife/view_vehicles.php";

    // JSON ID's
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        spinnersArray = new ArrayList<Spinner>();
        spinnersLayout = (LinearLayout) findViewById(R.id.vehicleHolder);

        spinnerNumDrivers = (Spinner) findViewById(R.id.numDrivers);
        spinnerNumVehicles = (Spinner) findViewById(R.id.numVehicles);
        spinnerResidenceType = (Spinner) findViewById(R.id.residenceType);
        currentCustomer = (RadioGroup) findViewById(R.id.currentCustomer);
        isStudent = (RadioGroup) findViewById(R.id.isStudent);
        isMarried = (RadioGroup) findViewById(R.id.isMarried);
        isFinanced = (RadioGroup) findViewById(R.id.vehicleFinanced);

    }

    public void setVehiclesAdapter() {
        vehiclesAdapter = new ArrayAdapter<Vehicle>(this, R.layout.spinner, popUpVehicles);
        vehiclesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void addNewVehicle(View view) {

        Spinner mySpinner = new Spinner(view.getContext());
        vehiclesAdapter = new ArrayAdapter<Vehicle>(this, R.layout.spinner, popUpVehicles);
        vehiclesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(vehiclesAdapter);
        spinnersArray.add(mySpinner);

        spinnersLayout.addView(mySpinner);

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

    @Override
    protected void onResume() {
        super.onResume();
        if(getInternetState()) {
            new LoadVehicles().execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(pDialog != null) {
            pDialog.dismiss();
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
            Intent aboutAAA = new Intent(VehicleQuoteActivity.this, AboutAAA.class);
            startActivity(aboutAAA);
        } else if(id == 2) {
            Intent aboutIns = new Intent(VehicleQuoteActivity.this, AboutInsuringMyLife.class);
            startActivity(aboutIns);
        } else if(id == R.id.aaa_logo) {
            DialogFragment aaaD = new AaaDialog();
            aaaD.show(getFragmentManager(), "aaa");
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadVehicles extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VehicleQuoteActivity.this);
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

            JSONObject jObject = jsonParser.makeHttpRequest(VEHICLES_URL, "POST", params);

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
                Toast.makeText(VehicleQuoteActivity.this, noVehicles, Toast.LENGTH_LONG).show();
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

        String isCustomer = "No";
        String student = "No";
        String vehicleFinanced = "No";

        for(int i = 0; i < numVehicles; i++) {
            if(numVehicles > 1) {
                startingPrice -= 50;
            }
        }

        for(int i = 0; i< numDrivers; i++) {
            if(numDrivers > 3) {
                startingPrice += 50;
            }
        }

        if(currentCustomer.getCheckedRadioButtonId() == R.id.yes) {
            isCustomer = "Yes";
            startingPrice -= 200;
        } else {
            startingPrice += 40;
        }

        if(isStudent.getCheckedRadioButtonId() == R.id.studentYes) {
            student = "Yes";
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
            vehicleFinanced = "Yes";
            startingPrice += 80;
        } else {
            startingPrice -= 80;
        }

        ArrayList<Vehicle> selectedVehicles = new ArrayList<Vehicle>();

        for(Spinner s : spinnersArray) {
            selectedVehicles.add(popUpVehicles.get(s.getSelectedItemPosition()));
        }

        Intent intent = new Intent(VehicleQuoteActivity.this, QuoteValueActivity.class);

        intent.putExtra("quoteType", Vehicle.TAG_VEHICLES);
        intent.putExtra("quoteValue", startingPrice);
        intent.putExtra("numVehicles", numVehicles);
        intent.putExtra("numDrivers", numDrivers);
        intent.putExtra("residenceType", residenceInformation);
        intent.putExtra("isCustomer", isCustomer);
        intent.putExtra("student", student);
        intent.putExtra("vehicleFinanced", vehicleFinanced);
        intent.putExtra("marriageType", marriageType);
        intent.putParcelableArrayListExtra("vehicles", selectedVehicles);
        startActivity(intent);
    }

}
