package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.gabilheri.insuringmylife.fragments.DatePickerFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewVehicle extends Activity implements  View.OnClickListener{

    private EditText policeNumber, carInfo, carModel, carTag, carYear, carDriver, driversLicense, carColor;
    private Spinner carBrand, driversLicenseState;
    private RadioGroup carDriverGender;
    private Button mNewVehicle, pickBirthday;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    private static final String NEWVEHICLE_URL = "http://162.243.225.173/InsuringMyLife/new_vehicle.php";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_vehicle);

        SharedPreferences loginName = getSharedPreferences("loginPref", MODE_PRIVATE);
        String login = loginName.getString("email", "");

        policeNumber = (EditText) findViewById(R.id.policeNumber);
        carModel = (EditText) findViewById(R.id.carModel);
        carColor = (EditText) findViewById(R.id.carColor);
        carTag = (EditText) findViewById(R.id.carTag);
        carYear = (EditText) findViewById(R.id.carYear);
        carDriver = (EditText) findViewById(R.id.carDriver);
        driversLicense = (EditText) findViewById(R.id.driversLicenceNumber);
        carBrand = (Spinner) findViewById(R.id.carBrand);
        driversLicenseState = (Spinner) findViewById(R.id.stateDriversLicense);
        carDriverGender = (RadioGroup) findViewById(R.id.genderRadioGroup);
        pickBirthday = (Button) findViewById(R.id.pickBirthday);

        mNewVehicle = (Button) findViewById(R.id.addVehicle);
        mNewVehicle.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {

       new AddNewVehicle().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_vehicle, menu);
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

    public void showDatePickerDialog(View v) {

        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getFragmentManager(), "datePicker");

    }

    class AddNewVehicle extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewVehicle.this);
            pDialog.setMessage("Adding Vehicle...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String getPoliceNumber = policeNumber.getText().toString();
            String getCarBrand = carBrand.getSelectedItem().toString();
            String getCarModel = carModel.getText().toString();
            String getCarTag = carTag.getText().toString();
            String getCarYear = carYear.getText().toString();
            String getCarDriver = carDriver.getText().toString();
            String getDriversLicense = driversLicense.getText().toString();
            String getDriversLicenseState = driversLicenseState.getSelectedItem().toString();
            String getCarColor = carColor.getText().toString();
            String[] driverBirthday = pickBirthday.getText().toString().split("/");

            String driverBirthdayMonth = driverBirthday[0];
            String driverBirthdayDay = driverBirthday[1];
            String driverBirthdayYear = driverBirthday[2];

            String driverGender;

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            String email = loginPref.getString("email", "");

            if(carDriverGender.getCheckedRadioButtonId() == R.id.radioMale) {
                driverGender = "Male";
            } else {
                driverGender = "Female";
            }


            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_id", email));
                params.add(new BasicNameValuePair("police_number", getPoliceNumber));
                params.add(new BasicNameValuePair("year", getCarYear));
                params.add(new BasicNameValuePair("model", getCarModel));
                params.add(new BasicNameValuePair("brand", getCarBrand));
                params.add(new BasicNameValuePair("color", getCarColor));
                params.add(new BasicNameValuePair("license_plate", getCarTag));
                params.add(new BasicNameValuePair("drivers_license", getDriversLicense));
                params.add(new BasicNameValuePair("license_state", getDriversLicenseState));
                params.add(new BasicNameValuePair("main_driver", getCarDriver));
                params.add(new BasicNameValuePair("driver_birthday_year", driverBirthdayYear));
                params.add(new BasicNameValuePair("driver_birthday_month", driverBirthdayMonth));
                params.add(new BasicNameValuePair("driver_birthday_day", driverBirthdayDay));
                params.add(new BasicNameValuePair("driver_gender", driverGender));

                Log.d("Query Requested", "Params");
                Log.d("user_id: ", email);
                Log.d("police_number: ", getPoliceNumber);
                Log.d("year: ", getCarYear);
                Log.d("brand: ", getCarBrand);
                Log.d("color: ", getCarColor);
                Log.d("license_plate: ", getCarTag);
                Log.d("main_driver: ", getCarDriver);
                Log.d("birthday_year: ", driverBirthdayYear);
                Log.d("birthday_month: ", driverBirthdayMonth);
                Log.d("birthday_day: ", driverBirthdayDay);
                Log.d("drivers_license: ", getDriversLicense);
                Log.d("license_state: ", getDriversLicenseState);
                Log.d("driver_gender: ", driverGender);

                JSONObject json = jsonParser.makeHttpRequest(NEWVEHICLE_URL, "POST", params);

                Log.d("New Vehicle Attempt", json.toString());

                int success = json.getInt(TAG_SUCCESS);

                if(success == 1) {
                    Intent backToVehicles = new Intent(NewVehicle.this, VehiclesActivity.class);
                    startActivity(backToVehicles);
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
