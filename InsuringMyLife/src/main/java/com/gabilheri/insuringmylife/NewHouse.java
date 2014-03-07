package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewHouse extends Activity {

    private EditText policeNumber, houseAddress, houseCity, houseZipCode;
    private Spinner houseState;
    private RadioGroup ownershipRadioGroup;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    private static final String NEW_HOUSE_URL = "http://162.243.225.173/InsuringMyLife/new_house.php";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_house);

        policeNumber = (EditText) findViewById(R.id.policeNumber);
        houseAddress = (EditText) findViewById(R.id.houseAddress);
        houseCity = (EditText) findViewById(R.id.houseCity);
        houseZipCode = (EditText) findViewById(R.id.houseZipCode);
        houseState = (Spinner) findViewById(R.id.houseState);
        ownershipRadioGroup = (RadioGroup) findViewById(R.id.ownershipRadioGroup);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_house, menu);
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

    public void addNewHouse(View view) {
        new AddNewHouse().execute();
    }

    class AddNewHouse extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewHouse.this);
            pDialog.setMessage("Adding Vehicle...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String getPoliceNumber = policeNumber.getText().toString();
            String getAddress = houseAddress.getText().toString();
            String getCity = houseCity.getText().toString();
            String getZipCode = houseZipCode.getText().toString();
            String getHouseState = houseState.getSelectedItem().toString();
            String ownershipStatus;

            int ownerShipID = ownershipRadioGroup.getCheckedRadioButtonId();

            if(ownerShipID == R.id.radioOwn) {
                ownershipStatus = "Owner";
            } else if(ownerShipID == R.id.radioRent) {
                ownershipStatus = "Rental house";
            } else {
                ownershipStatus = "Other";
            }

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            String email = loginPref.getString("email", "");


            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_id", email));
                params.add(new BasicNameValuePair("police_number", getPoliceNumber));
                params.add(new BasicNameValuePair("address", getAddress));
                params.add(new BasicNameValuePair("city", getCity));
                params.add(new BasicNameValuePair("state", getHouseState));
                params.add(new BasicNameValuePair("zip_code", getZipCode));
                params.add(new BasicNameValuePair("payed", ownershipStatus));

                JSONObject json = jsonParser.makeHttpRequest(NEW_HOUSE_URL, "POST", params);

                Log.d("New House Attempt", json.toString());

                int success = json.getInt(TAG_SUCCESS);

                if(success == 1) {
                    Intent backToHouses = new Intent(NewHouse.this, ViewHouses.class);
                    startActivity(backToHouses);
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
