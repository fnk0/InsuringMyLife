package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewProfileActivity extends Activity implements View.OnClickListener {


    private Spinner monthSpinner;
    private Spinner daySpinner;
    private Spinner yearSpinner;
    private Spinner ccCompaniesSpinner;
    private Spinner yearExpSpinner;
    private Spinner monthExpSpinner;
    private Integer[] dayNumbers;
    private ArrayList<Integer> yearBirthdayNumbers = new ArrayList<Integer>();
    private ArrayList<Integer> yearExpirationNumbers = new ArrayList<Integer>();
    private ProgressDialog pDialog;
    private EditText firstNameField, lastNameField, ccNameField, ccNumberField, cvcField;
    private Button newProfileButton;

    JSONParser jsonParser = new JSONParser();
    private static final String PROFILE_URL = "http://162.243.225.173/InsuringMyLife/new_profile.php";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        daySpinner = (Spinner) findViewById(R.id.daySpinner);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        ccCompaniesSpinner = (Spinner) findViewById(R.id.ccCompany);
        yearExpSpinner = (Spinner) findViewById(R.id.expYearSpinner);
        monthExpSpinner = (Spinner) findViewById(R.id.expMonthSpinner);
        ccCompaniesSpinner = (Spinner) findViewById(R.id.ccCompany);
        firstNameField = (EditText) findViewById(R.id.firstName);
        lastNameField = (EditText) findViewById(R.id.lastName);
        ccNameField = (EditText) findViewById(R.id.ccName);
        ccNumberField = (EditText) findViewById(R.id.ccNumber);
        cvcField = (EditText) findViewById(R.id.cvc);
        newProfileButton = (Button) findViewById(R.id.addNewProfile);

        dayNumbers = new Integer[31];
        for(int i = 0; i < 31; i++) {
           dayNumbers[i] = i + 1;
        }

        ArrayAdapter<Integer> daySpinnerAdapter = new ArrayAdapter<Integer>(this,   android.R.layout.simple_spinner_item, dayNumbers);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        daySpinner.setAdapter(daySpinnerAdapter);

        int year = Calendar.getInstance().get(Calendar.YEAR);

        while(year >= 1930) {
            yearBirthdayNumbers.add(year);
            year--;
        }

        ArrayAdapter<Integer> yearSpinnerAdapter = new ArrayAdapter<Integer>(this,   android.R.layout.simple_spinner_item, yearBirthdayNumbers);
        yearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        yearSpinner.setAdapter(yearSpinnerAdapter);

        int yearExp = Calendar.getInstance().get(Calendar.YEAR);
        int maxYear = yearExp + 20;
        while(yearExp < maxYear) {
            yearExpirationNumbers.add(yearExp);
            yearExp++;
        }

        ArrayAdapter<Integer> yearExpSpinnerAdapter = new ArrayAdapter<Integer>(this,   android.R.layout.simple_spinner_item, yearExpirationNumbers);
        yearExpSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        yearExpSpinner.setAdapter(yearExpSpinnerAdapter);

        newProfileButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        new createProfile().execute();
    }

    class createProfile extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewProfileActivity.this);
            pDialog.setMessage("Creating Profile...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String ... args) {

            int success;
            String firstName = firstNameField.getText().toString();
            String lastName = lastNameField.getText().toString();
            String ccNumber = ccNumberField.getText().toString();
            String cvcNum = cvcField.getText().toString();
            int monthBirthday = monthSpinner.getSelectedItemPosition() + 1;
            String dayBirthday = daySpinner.getSelectedItem().toString();
            String yearBirthday = yearSpinner.getSelectedItem().toString();
            int expMonth = monthExpSpinner.getSelectedItemPosition() + 1;
            String expYear = yearExpSpinner.getSelectedItem().toString();
            String ccCompany = ccCompaniesSpinner.getSelectedItem().toString();
            String ccName = ccNameField.getText().toString();
            String email;
            String expDate;
            String birthday = "" + yearBirthday + "-" + monthBirthday + "-" + dayBirthday;
            if(expMonth < 10) {
                expDate = "0" + expMonth + "-" + expYear;
            } else {
                expDate = "" + expMonth + "-" + expYear;
            }
            try {
                SharedPreferences loginPref = getSharedPreferences("loginPref", 0);
                email = loginPref.getString("email", "");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", firstName));
                params.add(new BasicNameValuePair("last_name", lastName));
                params.add(new BasicNameValuePair("birthday", birthday));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("cc_number", ccNumber));
                params.add(new BasicNameValuePair("cc_name", ccName));
                params.add(new BasicNameValuePair("cc_expiration", expDate));
                params.add(new BasicNameValuePair("cc_company",ccCompany));
                params.add(new BasicNameValuePair("cc_cvc", cvcNum));

                // Posting data to script

                JSONObject json = jsonParser.makeHttpRequest(PROFILE_URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);

                if(success == 1) {
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if(file_url != null) {
                Toast.makeText(NewProfileActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
