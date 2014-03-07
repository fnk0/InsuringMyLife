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

import com.gabilheri.insuringmylife.adapters.ClaimsExpandableAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewClaims extends Activity {

    public ProgressDialog pDialog;
    public JSONParser jsonParser = new JSONParser();


    public String user_id, claimType;

    // Create an Array to hold the Claims
    public JSONArray claims = null;

    // Manage the Claims with an Arraylist
    private ArrayList<HashMap<String, String>> ClaimsList;

    // GroupList Stuff
    SparseArray<ListRowGroup> claimsGroup = new SparseArray<ListRowGroup>();

    // PHP script address

    private static final String CLAIMS_URL = "http://162.243.225.173/InsuringMyLife/view_claims.php";

    // JSON ID's
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_claims);

        SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
        user_id = loginPref.getString("email", "");

        claimType = getIntent().getExtras().getString("claimType");
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadClaims().execute();
    }

    public void updateList() {
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
        ClaimsExpandableAdapter adapter = new ClaimsExpandableAdapter(this, claimsGroup);
        listView.setAdapter(adapter);
    }

    public void addClaims(View view) {
        Intent addClaims = new Intent(ViewClaims.this, ReportVehicleClaim.class);
        startActivity(addClaims);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_claims, menu);
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

        ClaimsList = new ArrayList<HashMap<String, String>>();

        int success = 0;
        // Building parameters for the request

        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            user_id = loginPref.getString("email", "");

            params.add(new BasicNameValuePair("user_id", user_id));
            params.add(new BasicNameValuePair("claim_type", claimType));

            JSONObject jObject = jsonParser.makeHttpRequest(CLAIMS_URL, "POST", params);

            success = jObject.getInt(TAG_SUCCESS);
            claims = jObject.getJSONArray("claims");

            if(success == 1) {
                // loop through all the Claims
                for(int i = 0; i < claims.length(); i++) {
                    JSONObject obj = claims.getJSONObject(i);

                    // Get each element based on it's tag
                    String userID = obj.getString("user_id");
                    String claimNumber = obj.getString("claim_number");
                    String objectId = obj.getString("claim_object_id");
                    String policeNumber = obj.getString("police_number");
                    String claimYear = obj.getString("claim_year");
                    String claimMonth = obj.getString("claim_month");
                    String claimDay = obj.getString("claim_day");
                    String claimHour = obj.getString("claim_hour");
                    String claimMinute = obj.getString("claim_minute");
                    String claimPeriod = obj.getString("claim_time_period");
                    String claimCause = obj.getString("claim_cause");
                    String claimDescription = obj.getString("claim_description");

                    String claimDate = claimMonth + "/" + claimDay + "/" + claimYear;
                    String claimTime = claimHour + ":" + claimMinute + " " + claimPeriod;

                    ListRowGroup group = new ListRowGroup("Claim ID: " + claimNumber, "number");
                    group.children.add("Claim Number: " + claimNumber);
                    group.children.add(claimCause);
                    group.children.add(claimDate);
                    group.children.add(claimTime);
                    group.children.add(policeNumber);
                    group.children.add(claimDescription);

                    claimsGroup.append(i, group);
                }

            } else {
                String noClaims = "There's no Claims yet!";
                Toast.makeText(ViewClaims.this, noClaims, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addVehicle(View v) {

        Intent i = new Intent(ViewClaims.this, NewVehicle.class);
        startActivity(i);
    }

    class LoadClaims extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewClaims.this);
            pDialog.setMessage("Loading Claims...");
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
