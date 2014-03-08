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
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.gabilheri.insuringmylife.adapters.MyExpandableListAdapter;
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

public class VehiclesActivity extends Activity {

    public ProgressDialog pDialog;
    public JSONParser jsonParser = new JSONParser();


    public String user_id;

    // Create an Array to hold the vehicles
    public JSONArray vehicles = null;

    // Manage the vehicles with an Arraylist
    private ArrayList<HashMap<String, String>> vehiclesList;

    // GroupList Stuff
    SparseArray<ListRowGroup> vehiclesGroup = new SparseArray<ListRowGroup>();

    // PHP script address

    private static final String VEHICLES_URL = "http://162.243.225.173/InsuringMyLife/view_vehicles.php";

    // JSON ID's
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

        SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
        user_id = loginPref.getString("email", "");

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

    public void updateList() {
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(this, vehiclesGroup);
        listView.setAdapter(adapter);
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
            Intent aboutAAA = new Intent(VehiclesActivity.this, AboutAAA.class);
            startActivity(aboutAAA);
        } else if(id == 2) {
            Intent aboutIns = new Intent(VehiclesActivity.this, AboutInsuringMyLife.class);
            startActivity(aboutIns);
        } else if(id == R.id.aaa_logo) {
            DialogFragment aaaD = new AaaDialog();
            aaaD.show(getFragmentManager(), "aaa");
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateJSONdata() {

        // Initiate the ArrayList to hold the Json data

        vehiclesList = new ArrayList<HashMap<String, String>>();
        int success = 0;
        // Building parameters for the request

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

                    // Get each element based on it's tag
                    String year = obj.getString(Vehicle.TAG_YEAR);
                    String model = obj.getString(Vehicle.TAG_MODEL);
                    String brand = obj.getString(Vehicle.TAG_BRAND);
                    String color = obj.getString(Vehicle.TAG_COLOR);
                    String license_plate = obj.getString(Vehicle.TAG_LICENSE);
                    String main_driver = obj.getString(Vehicle.TAG_DRIVER);
                    String policeNumber = obj.getString(Vehicle.TAG_POLICENUMBER);
                    String driversLicense = obj.getString(Vehicle.TAG_DRIVER_LICENSE);
                    String licenseState = obj.getString(Vehicle.TAG_LICENSE_STATE);
                    String driverBirthday = obj.getString(Vehicle.TAG_BIRTHDAY_MONTH) + "/" +
                            obj.getString(Vehicle.TAG_BIRTHDAY_DAY) + "/" +
                            obj.get(Vehicle.TAG_BIRTHDAY_YEAR);
                    String driverGender = obj.getString(Vehicle.TAG_DRIVER_GENDER);

                    ListRowGroup group = new ListRowGroup(brand + " " + year + " " + model, brand);
                    group.children.add(brand + " " + year + " " + model);
                    group.children.add(policeNumber);
                    group.children.add(model);
                    group.children.add(color);
                    group.children.add(license_plate);
                    group.children.add(main_driver);
                    group.children.add(driversLicense + "   -   " + licenseState);
                    group.children.add(driverBirthday + "   -   " + driverGender);

                    vehiclesGroup.append(i, group);
                }

            } else {
                String noVehicles = "There's no vehicles yet! Click the Add vehicle button to add some of your awesome cars!";
                Toast.makeText(VehiclesActivity.this, noVehicles, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addVehicle(View v) {

        Intent i = new Intent(VehiclesActivity.this, NewVehicle.class);
        startActivity(i);
    }

    class LoadVehicles extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VehiclesActivity.this);
            pDialog.setMessage("Loading vehicles...");
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
            pDialog.dismiss();
            updateList();
        }
    }
}
