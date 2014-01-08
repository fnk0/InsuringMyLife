package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

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

    public String profile_id;
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
    private static final String TAG_USERID = "user_id";
    private static final String TAG_PROFILEID = "profile_id";
    private static final String TAG_VEHICLES = "vehicles";
    private static final String TAG_YEAR = "year";
    private static final String TAG_MODEL = "model";
    private static final String TAG_BRAND = "brand";
    private static final String TAG_COLOR = "color";
    private static final String TAG_LICENSE = "license_plate";
    private static final String TAG_DRIVER = "main_driver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

        SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
        user_id = loginPref.getString("email", "");
        SharedPreferences profilePref = getSharedPreferences("selectedProfile", MODE_PRIVATE);
        profile_id = profilePref.getString("selectedProfile", "");

    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadVehicles().execute();
    }

    public void updateList() {
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(this, vehiclesGroup);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vehicles, menu);
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

    public void updateJSONdata() {

        // Initiate the ArrayList to hold the Json data

        vehiclesList = new ArrayList<HashMap<String, String>>();
        int success = 0;

        // Building parameters for the request

        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            /*
            String user_id = "test";
            String profile_id = "Marcus Gabilheri";
            */
            params.add(new BasicNameValuePair(TAG_USERID, user_id));
            params.add(new BasicNameValuePair(TAG_PROFILEID, profile_id));

            JSONObject jObject = jsonParser.makeHttpRequest(VEHICLES_URL, "POST", params);

            success = jObject.getInt(TAG_SUCCESS);
            vehicles = jObject.getJSONArray(TAG_VEHICLES);

            if(success == 1) {
                // loop through all the vehicles
                for(int i = 0; i < vehicles.length(); i++) {
                    JSONObject obj = vehicles.getJSONObject(i);

                    // Get each element based on it's tag
                    String year = obj.getString(TAG_YEAR);
                    String model = obj.getString(TAG_MODEL);
                    String brand = obj.getString(TAG_BRAND);
                    String color = obj.getString(TAG_COLOR);
                    String license_plate = obj.getString(TAG_LICENSE);
                    String main_driver = obj.getString(TAG_DRIVER);

                    // Creating a new HashMap
                    ListRowGroup group = new ListRowGroup(brand + " " + year, brand);
                    group.children.add("Model: " + model);
                    group.children.add("Color: " + color);
                    group.children.add("license Plate: " + license_plate);
                    group.children.add("Main Driver: " + main_driver);

                    vehiclesGroup.append(i, group);
                    /*
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_YEAR, year);
                    map.put(TAG_MODEL, model);
                    map.put(TAG_BRAND, brand);
                    map.put(TAG_LICENSE, license_plate);
                    map.put(TAG_DRIVER, main_driver);
                    map.put(TAG_COLOR, color);

                    vehiclesList.add(map);
                    */
                }

            } else {
                String noVehicles = "There's no vehicles yet! Click the Add vehicle button to add some of your awesome cars!";
                Toast.makeText(VehiclesActivity.this, noVehicles, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
