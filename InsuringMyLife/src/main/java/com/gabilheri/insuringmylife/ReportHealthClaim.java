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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gabilheri.insuringmylife.fragments.DatePickerFragment;
import com.gabilheri.insuringmylife.fragments.TimePickerFragment;
import com.gabilheri.insuringmylife.helpers.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReportHealthClaim extends Activity {

    public ProgressDialog pDialog;
    public JSONParser jsonParser = new JSONParser();
    private Button dateButton, timeButton;
    private EditText claimDescription;
    private String user_id;
    private int claimNumber;

    public JSONArray persons = null;

    private ArrayList<Person> popUpMembers;
    private ArrayAdapter<Person> familyMembersAdapter;
    private Spinner familyMembersSpinner, causeSpinner;

    // PHP script address

    private static final String NEW_CLAIM_URL = "http://162.243.225.173/InsuringMyLife/new_claim.php";

    // JSON ID's
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_health_claim);

        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        causeSpinner = (Spinner) findViewById(R.id.selectCause);
        familyMembersSpinner = (Spinner) findViewById(R.id.familyMemberClaim);

        timeButton = (Button) findViewById(R.id.timeButton);
        dateButton = (Button) findViewById(R.id.dateButton);

        claimDescription = (EditText) findViewById(R.id.claimDescription);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.report_health_claim, menu);
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

    @Override
    protected void onResume() {
        super.onResume();
        new LoadFamilyMembers().execute();
    }

    public void setFamilyMembersAdapter() {
        familyMembersAdapter = new ArrayAdapter<Person>(this, R.layout.spinner, popUpMembers);
        familyMembersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        familyMembersSpinner.setAdapter(familyMembersAdapter);
    }


    public void showDatePickerDialog(View v) {
        DialogFragment datePicker = new DatePickerFragment(v.getId());
        datePicker.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment timePicker = new TimePickerFragment(v.getId());
        timePicker.show(getFragmentManager(), "timePicker");
    }

    public void submitClaim(View view) {
        new SubmitClaim().execute();
    }

    class LoadFamilyMembers extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReportHealthClaim.this);
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
            setFamilyMembersAdapter();
            pDialog.dismiss();

        }
    }

    public void updateJSONdata() {

        popUpMembers = new ArrayList<Person>();
        int success = 0;

        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            user_id = loginPref.getString("email", "");

            params.add(new BasicNameValuePair("user_id", user_id));

            JSONObject jObject = jsonParser.makeHttpRequest(Person.VIEW_PERSON_URL, "POST", params);

            success = jObject.getInt(TAG_SUCCESS);
            persons = jObject.getJSONArray(Person.TAG_PERSONS);

            if(success == 1) {
                for(int i = 0; i < persons.length(); i++) {
                    JSONObject obj = persons.getJSONObject(i);

                    // Create a new Person
                    Person myPerson = new Person();
                    // Get each element based on it's tag

                    String birthdayYear = obj.getString(Person.TAG_BIRTHDAY_YEAR);
                    String birthdayMonth = obj.getString(Person.TAG_BIRTHDAY_MONTH);
                    String birthdayDay = obj.getString(Person.TAG_BIRTHDAY_DAY);

                    String birthday = birthdayMonth + "/" + birthdayDay + "/" + birthdayYear;

                    myPerson.setId(obj.getString(Person.TAG_ID));
                    myPerson.setPoliceNumber(obj.getString(Person.TAG_POLICENUMBER));
                    myPerson.setName(obj.getString(Person.TAG_NAME));
                    myPerson.setMiddleI(obj.getString(Person.TAG_MIDDLE_I));
                    myPerson.setLastName(obj.getString(Person.TAG_LAST_NAME));
                    myPerson.setBirthDay(birthday);
                    myPerson.setAge(obj.getString(Person.TAG_AGE));
                    myPerson.setRelationship(obj.getString(Person.TAG_RELATIONSHIP));

                    popUpMembers.add(myPerson);
                }
            } else {
                String noProperties = "There's no Family members yet! Click the Add property button to add some of your family members!";
                Toast.makeText(ReportHealthClaim.this, noProperties, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class SubmitClaim extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ReportHealthClaim.this);
            pDialog.setMessage("Reporting Claim...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String getClaimCause = causeSpinner.getSelectedItem().toString();
            Person person = (Person) familyMembersSpinner.getSelectedItem();

            String[] date = dateButton.getText().toString().split("/");
            String[] time = timeButton.getText().toString().split(":");
            String[] time2 = time[1].split(" ");

            String hour = time[0];
            String minute = time2[0];
            String timePeriod = time2[1];
            String month = date[0];
            String day = date[1];
            String year = date[2];
            String personID = person.getId();
            String policeNumber = person.getPoliceNumber();
            String description = claimDescription.getText().toString();

            Random randomNumber = new Random();
            claimNumber = randomNumber.nextInt(999999999);

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            String email = loginPref.getString("email", "");


            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user_id", email));
                params.add(new BasicNameValuePair("claim_type", Person.TAG_PERSONS));
                params.add(new BasicNameValuePair("claim_object_id", personID));
                params.add(new BasicNameValuePair("police_number", policeNumber));
                params.add(new BasicNameValuePair("claim_number", Integer.toString(claimNumber)));
                params.add(new BasicNameValuePair("claim_year", year));
                params.add(new BasicNameValuePair("claim_month", month));
                params.add(new BasicNameValuePair("claim_day", day));
                params.add(new BasicNameValuePair("claim_hour", hour));
                params.add(new BasicNameValuePair("claim_minute", minute));
                params.add(new BasicNameValuePair("claim_time_period", timePeriod));
                params.add(new BasicNameValuePair("claim_cause", getClaimCause));
                params.add(new BasicNameValuePair("claim_description", description));

                JSONObject json = jsonParser.makeHttpRequest(NEW_CLAIM_URL, "POST", params);

                Log.d("New Claim Attempt", json.toString());

                int success = json.getInt(TAG_SUCCESS);

                if(success == 1) {
                    Intent afterClaim = new Intent(ReportHealthClaim.this, AfterClaim.class);
                    afterClaim.putExtra("claimID", claimNumber);
                    afterClaim.putExtra("claimType", "person");
                    startActivity(afterClaim);
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
