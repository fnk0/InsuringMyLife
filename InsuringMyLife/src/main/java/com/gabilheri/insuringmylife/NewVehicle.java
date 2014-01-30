package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.gabilheri.insuringmylife.fragments.DatePickerFragment;

public class NewVehicle extends Activity implements  View.OnClickListener{

    private EditText policeNumber, carInfo, carModel, carTag, carYear, carDriver, driversLicense;
    private Spinner carBrand, driversLicenseState;
    private RadioGroup carDriverGender;
    private Button mNewVehicle;

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
        carTag = (EditText) findViewById(R.id.carTag);
        carYear = (EditText) findViewById(R.id.carYear);
        carDriver = (EditText) findViewById(R.id.carDriver);
        driversLicense = (EditText) findViewById(R.id.driversLicenceNumber);
        carBrand = (Spinner) findViewById(R.id.carBrand);
        driversLicenseState = (Spinner) findViewById(R.id.stateDriversLicense);
        carDriverGender = (RadioGroup) findViewById(R.id.genderRadioGroup);

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
            pDialog.setMessage("Adding Vehicle");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {


            int getPoliceNumber = Integer.parseInt(policeNumber.getText().toString());
            String getCarBrand = carBrand.getSelectedItem().toString();
            String getCarModel = carModel.getText().toString();
            String getCarTag = carTag.getText().toString();
            int getCarYear = Integer.parseInt(carYear.getText().toString());
            String getCarDriver = carDriver.getText().toString();
            String getDriversLicense = driversLicense.getText().toString();
            String getDriversLicenseState = driversLicenseState.getSelectedItem().toString();

            /*
            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();

                //params.add("police_number", getPoliceNumber);
            } catch(JSONException e) {
                e.printStackTrace();
            }
            */
            return null;
        }
    }
}
