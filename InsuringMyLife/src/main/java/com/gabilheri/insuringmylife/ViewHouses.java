package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.gabilheri.insuringmylife.adapters.HouseExpandableAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewHouses extends Activity {

    public ProgressDialog pDialog;
    public JSONParser jsonParser = new JSONParser();


    public String user_id;

    // Create an Array to hold the houses
    public JSONArray houses = null;

    // Manage the houses with an Arraylist
    private ArrayList<HashMap<String, String>> housesList;

    // GroupList Stuff
    SparseArray<ListRowGroup> housesGroup = new SparseArray<ListRowGroup>();

    // PHP script address

    private static final String houses_URL = "http://162.243.225.173/InsuringMyLife/view_houses.php";

    // JSON ID's
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_houses);

        SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
        user_id = loginPref.getString("email", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadHouses().execute();
    }

    public void updateList() {
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
        HouseExpandableAdapter adapter = new HouseExpandableAdapter(this, housesGroup);
        listView.setAdapter(adapter);
    }

    public void addHouses(View view) {
        Intent addHouses = new Intent(ViewHouses.this, NewHouse.class);
        startActivity(addHouses);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_houses, menu);
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

        housesList = new ArrayList<HashMap<String, String>>();
        int success = 0;
        // Building parameters for the request

        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            user_id = loginPref.getString("email", "");

            params.add(new BasicNameValuePair("user_id", user_id));

            JSONObject jObject = jsonParser.makeHttpRequest(houses_URL, "POST", params);

            success = jObject.getInt(TAG_SUCCESS);
            houses = jObject.getJSONArray("houses");

            if(success == 1) {
                // loop through all the houses
                for(int i = 0; i < houses.length(); i++) {
                    JSONObject obj = houses.getJSONObject(i);

                    // Get each element based on it's tag
                    String userID = obj.getString("user_id");
                    String policeNumber = obj.getString("police_number");
                    String address = obj.getString("address");
                    String city = obj.getString("city");
                    String state = obj.getString("state");
                    String zipCode = obj.getString("zip_code");
                    String payed = obj.getString("payed");

                    ListRowGroup group = new ListRowGroup(address, "address");
                    group.children.add("Police Number: " + policeNumber);
                    group.children.add(address);
                    group.children.add(city);
                    group.children.add(state + "   -   " + zipCode);
                    group.children.add(payed);

                    housesGroup.append(i, group);
                }

            } else {
                String noHouses = "There's no houses yet!";
                Toast.makeText(ViewHouses.this, noHouses, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addVehicle(View v) {

        Intent i = new Intent(ViewHouses.this, NewVehicle.class);
        startActivity(i);
    }

    class LoadHouses extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewHouses.this);
            pDialog.setMessage("Loading houses...");
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
