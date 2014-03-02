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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class UpdateProfileActivity extends Activity implements View.OnClickListener {

    private Spinner monthSpinner, daySpinner, yearSpinner, ccCompaniesSpinner, yearExpSpinner, monthExpSpinner;
    private Integer[] dayNumbers;
    private ArrayList<Integer> yearBirthdayNumbers = new ArrayList<Integer>();
    private ArrayList<Integer> yearExpirationNumbers = new ArrayList<Integer>();
    private ProgressDialog pDialogCreate, pDialogLoad;
    private EditText firstNameField, lastNameField, ccNameField, ccNumberField, cvcField, customerNumberField;
    private Button updateProfileButton;
    private String email;
    private String[] months, ccCompanies;
    public JSONArray profile_info = null;
    private HashMap<String, String> profileInfoList;
    private ArrayAdapter<Integer> daySpinnerAdapter, yearSpinnerAdapter, yearExpSpinnerAdapter;
    private ArrayAdapter<String> monthSpinnerAdapter, ccCompaniesAdapter;

    public JSONParser jsonParser = new JSONParser();
    private static final String UPDATE_PROFILE_URL = "http://162.243.225.173/InsuringMyLife/update_profile.php";
    private static final String GET_PROFILE_URL = "http://162.243.225.173/InsuringMyLife/get_profile.php";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);
        daySpinner = (Spinner) findViewById(R.id.daySpinner);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        ccCompaniesSpinner = (Spinner) findViewById(R.id.ccCompany);
        yearExpSpinner = (Spinner) findViewById(R.id.expYearSpinner);
        monthExpSpinner = (Spinner) findViewById(R.id.expMonthSpinner);
        ccCompaniesSpinner = (Spinner) findViewById(R.id.ccCompany);
        firstNameField = (EditText) findViewById(R.id.firstName);
        lastNameField = (EditText) findViewById(R.id.lastName);
        customerNumberField = (EditText) findViewById(R.id.aaaNumber);
        ccNameField = (EditText) findViewById(R.id.ccName);
        ccNumberField = (EditText) findViewById(R.id.ccNumber);
        cvcField = (EditText) findViewById(R.id.cvc);
        updateProfileButton = (Button) findViewById(R.id.updateProfile);

        dayNumbers = new Integer[31];
        for(int i = 0; i < 31; i++) {
           dayNumbers[i] = i + 1;
        }

        daySpinnerAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner, dayNumbers);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        daySpinner.setAdapter(daySpinnerAdapter);

        months = getResources().getStringArray(R.array.months);
        monthSpinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner, months);
        monthSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthSpinnerAdapter);
        monthExpSpinner.setAdapter(monthSpinnerAdapter);

        ccCompanies = getResources().getStringArray(R.array.cc_companies);
        ccCompaniesAdapter = new ArrayAdapter<String>(this, R.layout.spinner, ccCompanies);
        ccCompaniesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ccCompaniesSpinner.setAdapter(ccCompaniesAdapter);


        int year = Calendar.getInstance().get(Calendar.YEAR);

        while(year >= 1930) {
            yearBirthdayNumbers.add(year);
            year--;
        }

        yearSpinnerAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner, yearBirthdayNumbers);
        yearSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        yearSpinner.setAdapter(yearSpinnerAdapter);

        int yearExp = Calendar.getInstance().get(Calendar.YEAR);
        int maxYear = yearExp + 20;
        while(yearExp < maxYear) {
            yearExpirationNumbers.add(yearExp);
            yearExp++;
        }

        yearExpSpinnerAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner, yearExpirationNumbers);
        yearExpSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        yearExpSpinner.setAdapter(yearExpSpinnerAdapter);

        new LoadProfile().execute();
        updateProfileButton.setOnClickListener(this);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        new UpdateProfile().execute();
    }

    class UpdateProfile extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogCreate = new ProgressDialog(UpdateProfileActivity.this);
            pDialogCreate.setMessage("Updating Profile...");
            pDialogCreate.setIndeterminate(false);
            pDialogCreate.setCancelable(true);
            pDialogCreate.show();
        }

        @Override
        protected String doInBackground(String ... args) {

            int success;
            String firstName = firstNameField.getText().toString();
            String lastName = lastNameField.getText().toString();
            String ccNumber = ccNumberField.getText().toString();
            String cvcNum = cvcField.getText().toString();
            String customerNumber = customerNumberField.getText().toString();
            int monthBirthday = monthSpinner.getSelectedItemPosition() + 1;
            String dayBirthday = daySpinner.getSelectedItem().toString();
            String yearBirthday = yearSpinner.getSelectedItem().toString();
            int expMonth = monthExpSpinner.getSelectedItemPosition() + 1;
            String expYear = yearExpSpinner.getSelectedItem().toString();
            String ccCompany = ccCompaniesSpinner.getSelectedItem().toString();
            String ccName = ccNameField.getText().toString();
            String email;


            try {
                SharedPreferences loginPref = getSharedPreferences("loginPref", 0);
                email = loginPref.getString("email", "");
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("customer_number", customerNumber));
                params.add(new BasicNameValuePair("name", firstName));
                params.add(new BasicNameValuePair("last_name", lastName));
                params.add(new BasicNameValuePair("birthday_year", yearBirthday));
                params.add(new BasicNameValuePair("birthday_month", Integer.toString(monthBirthday)));
                params.add(new BasicNameValuePair("birthday_day", dayBirthday));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("cc_number", ccNumber));
                params.add(new BasicNameValuePair("cc_name", ccName));
                params.add(new BasicNameValuePair("cc_expiration_year", expYear));
                params.add(new BasicNameValuePair("cc_expiration_month", Integer.toString(expMonth)));
                params.add(new BasicNameValuePair("cc_company",ccCompany));
                params.add(new BasicNameValuePair("cc_cvc", cvcNum));

                // Posting data to script

                JSONObject json = jsonParser.makeHttpRequest(UPDATE_PROFILE_URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);

                if(success == 1) {
                    Intent i = new Intent(UpdateProfileActivity.this, InitialActivity.class);
                    startActivity(i);
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
            super.onPostExecute(file_url);
            pDialogCreate.dismiss();

            if(file_url != null) {
                Toast.makeText(UpdateProfileActivity.this, "Update Succesfull", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void updateJsonData() {
        int success;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        profileInfoList = new HashMap<String, String>();

        try {
            SharedPreferences sp = getSharedPreferences("loginPref", MODE_PRIVATE);
            email = sp.getString(TAG_EMAIL, "");

            params.add(new BasicNameValuePair(TAG_EMAIL, email));
            JSONObject jObject = jsonParser.makeHttpRequest(GET_PROFILE_URL, "POST", params);

            success = jObject.getInt(TAG_SUCCESS);
            profile_info = jObject.getJSONArray("profile_data");

            if(success == 1) {
                for(int i = 0; i < profile_info.length(); i++) {
                    JSONObject obj = profile_info.getJSONObject(i);

                    profileInfoList.put("customer_number", obj.getString("customer_number"));
                    profileInfoList.put("name", obj.getString("name"));
                    profileInfoList.put("last_name", obj.getString("last_name"));
                    profileInfoList.put("birthday_year", Integer.toString(obj.getInt("birthday_year")));
                    profileInfoList.put("birthday_month", Integer.toString(obj.getInt("birthday_month")));
                    profileInfoList.put("birthday_day", Integer.toString(obj.getInt("birthday_day")));
                    profileInfoList.put("cc_number", obj.getString("cc_number"));
                    profileInfoList.put("cc_name", obj.getString("cc_name"));
                    profileInfoList.put("cc_expiration_year", obj.getString("cc_expiration_year"));
                    profileInfoList.put("cc_expiration_month", obj.getString("cc_expiration_month"));
                    profileInfoList.put("cc_company", obj.getString("cc_company").toUpperCase());
                    profileInfoList.put("cc_cvc",  obj.getString("cc_cvc"));
                    Log.d("Company: ", obj.getString("cc_company").toUpperCase());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateList() {

        boolean thrown = false;
        do {
            try {
                firstNameField.setText(profileInfoList.get("name"));
                lastNameField.setText(profileInfoList.get("last_name"));
                if(!profileInfoList.get("customer_number").equals("0")) {
                    customerNumberField.setText(profileInfoList.get("customer_number"));
                }
                monthSpinner.setSelection(monthSpinnerAdapter.getPosition(months[Integer.parseInt(profileInfoList.get("birthday_month")) - 1]));
                daySpinner.setSelection(daySpinnerAdapter.getPosition(Integer.parseInt(profileInfoList.get("birthday_day"))));
                yearSpinner.setSelection(yearSpinnerAdapter.getPosition(Integer.parseInt(profileInfoList.get("birthday_year"))));
                ccNameField.setText(profileInfoList.get("cc_name"));
                ccNumberField.setText("****" + profileInfoList.get("cc_number"));
                cvcField.setText(profileInfoList.get("cc_cvc"));
                yearExpSpinner.setSelection(yearExpSpinnerAdapter.getPosition(Integer.parseInt(profileInfoList.get("cc_expiration_year"))));
                monthExpSpinner.setSelection(monthSpinnerAdapter.getPosition(months[Integer.parseInt(profileInfoList.get("cc_expiration_month")) - 1]));
                ccCompaniesSpinner.setSelection(ccCompaniesAdapter.getPosition(profileInfoList.get("cc_company")));
            } catch (Exception e) {
                thrown = true;
                e.printStackTrace();
            }
        } while(thrown);
    }

    class LoadProfile extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogLoad = new ProgressDialog(UpdateProfileActivity.this);
            pDialogLoad.setMessage("Loading Profile...");
            pDialogLoad.setIndeterminate(false);
            pDialogLoad.setCancelable(true);
            pDialogLoad.show();
        }

        @Override
        protected String doInBackground(String ... args) {
            updateJsonData();
            return null;
        }

        protected void onPostExecute(String file_url) {
            super.onPostExecute(file_url);
            pDialogLoad.dismiss();
            updateList();
        }
    }
}
