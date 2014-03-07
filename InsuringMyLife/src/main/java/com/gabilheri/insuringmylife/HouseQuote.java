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

import com.gabilheri.insuringmylife.helpers.House;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HouseQuote extends Activity {

    public ProgressDialog pDialog;
    public JSONParser jsonParser = new JSONParser();

    private String user_id;

    // Create an Array to hold the vehicles
    public JSONArray vehicles = null;

    // Manage the vehicles with an Arraylist
    private ArrayList<HashMap<String, String>> housesList;

    private ArrayList<House> popUpHouses;
    private ArrayAdapter<House> housesAdapter;
    private Spinner spinnerNumProperties, spinnerResidenceType;
    private RadioGroup currentCustomer, isStudent, isMarried, isFinanced;
    private LinearLayout spinnersLayout;
    private ArrayList<Spinner> spinnersArray;

    // PHP script address

    private static final String HOUSES_URL = "http://162.243.225.173/InsuringMyLife/view_houses.php";

    // JSON ID's
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_quote);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        spinnersArray = new ArrayList<Spinner>();
        spinnersLayout = (LinearLayout) findViewById(R.id.propertyHolder);

        spinnerNumProperties = (Spinner) findViewById(R.id.numProperties);
        spinnerResidenceType = (Spinner) findViewById(R.id.residenceType);
        currentCustomer = (RadioGroup) findViewById(R.id.currentCustomer);
        isStudent = (RadioGroup) findViewById(R.id.isStudent);
        isMarried = (RadioGroup) findViewById(R.id.isMarried);
        isFinanced = (RadioGroup) findViewById(R.id.houseFinanced);
    }

    public void setHousesAdapter() {
        housesAdapter = new ArrayAdapter<House>(this, R.layout.spinner, popUpHouses);
        housesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void addNewProperty(View view) {

        Spinner mySpinner = new Spinner(view.getContext());
        housesAdapter = new ArrayAdapter<House>(this, R.layout.spinner, popUpHouses);
        housesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(housesAdapter);
        spinnersArray.add(mySpinner);

        spinnersLayout.addView(mySpinner);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadHouses().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pDialog.dismiss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.house_quote, menu);
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

    class LoadHouses extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HouseQuote.this);
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
            setHousesAdapter();
            pDialog.dismiss();

        }
    }

    public void updateJSONdata() {

        // Initiate the ArrayList to hold the Json data
        housesList = new ArrayList<HashMap<String, String>>();
        popUpHouses = new ArrayList<House>();
        int success = 0;

        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            user_id = loginPref.getString("email", "");

            params.add(new BasicNameValuePair(House.TAG_USERID, user_id));

            JSONObject jObject = jsonParser.makeHttpRequest(HOUSES_URL, "POST", params);

            success = jObject.getInt(TAG_SUCCESS);
            vehicles = jObject.getJSONArray(House.TAG_HOUSES);

            if(success == 1) {
                // loop through all the vehicles
                for(int i = 0; i < vehicles.length(); i++) {
                    JSONObject obj = vehicles.getJSONObject(i);

                    // Create a new Vehicle
                    House myHouse = new House();
                    // Get each element based on it's tag
                    myHouse.setId(obj.getString(House.TAG_ID));
                    myHouse.setPoliceNumber(obj.getString(House.TAG_POLICENUMBER));
                    myHouse.setAddress(obj.getString(House.TAG_ADDRESS));
                    myHouse.setCity(obj.getString(House.TAG_CITY));
                    myHouse.setState(obj.getString(House.TAG_STATE));
                    myHouse.setZipCode(obj.getString(House.TAG_ZIPCODE));
                    myHouse.setPayed(obj.getString(House.TAG_PAYED));

                    popUpHouses.add(myHouse);
                }
            } else {
                String noVehicles = "There's no vehicles yet! Click the Add vehicle button to add some of your awesome cars!";
                Toast.makeText(HouseQuote.this, noVehicles, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getQuote(View view) {

        double startingPrice = 1768;

        int numHouses = spinnerNumProperties.getSelectedItemPosition() + 1;
        String residenceInformation = spinnerResidenceType.getSelectedItem().toString();

        String isCustomer = "No";
        String student = "No";
        String houseFinanced = "No";

        for(int i = 0; i < numHouses; i++) {
            if(numHouses > 1) {
                startingPrice -= 50;
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
            houseFinanced = "Yes";
            startingPrice += 80;
        } else {
            startingPrice -= 80;
        }

        ArrayList<House> selectedHouses = new ArrayList<House>();

        for(Spinner s : spinnersArray) {
            selectedHouses.add(popUpHouses.get(s.getSelectedItemPosition()));
        }

        Intent intent = new Intent(HouseQuote.this, QuoteValueActivity.class);

        intent.putExtra("quoteType", House.TAG_HOUSES);
        intent.putExtra("quoteValue", startingPrice);
        intent.putExtra("numProperties", numHouses);
        intent.putExtra("residenceType", residenceInformation);
        intent.putExtra("isCustomer", isCustomer);
        intent.putExtra("student", student);
        intent.putExtra("houseFinanced", houseFinanced);
        intent.putExtra("marriageType", marriageType);
        intent.putParcelableArrayListExtra("houses", selectedHouses);
        startActivity(intent);
    }

}
