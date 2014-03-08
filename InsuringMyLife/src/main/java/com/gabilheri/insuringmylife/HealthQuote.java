package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.gabilheri.insuringmylife.helpers.House;
import com.gabilheri.insuringmylife.helpers.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HealthQuote extends Activity {

    public ProgressDialog pDialog;
    public JSONParser jsonParser = new JSONParser();

    private String user_id;

    // Create an Array to hold the vehicles
    public JSONArray familyMembers = null;


    private ArrayList<Person> popUpFamilyMembers;
    private ArrayAdapter<Person> personAdapter;
    private Spinner spinnerNumMembers, spinnerResidenceType;
    private RadioGroup currentCustomer, isStudent, isMarried, isFinanced;
    private LinearLayout spinnersLayout;
    private ArrayList<Spinner> spinnersArray;

    // JSON ID's
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_quote);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        spinnersArray = new ArrayList<Spinner>();
        spinnersLayout = (LinearLayout) findViewById(R.id.familyMemberHolder);

        spinnerNumMembers = (Spinner) findViewById(R.id.numMembers);
        spinnerResidenceType = (Spinner) findViewById(R.id.residenceType);
        currentCustomer = (RadioGroup) findViewById(R.id.currentCustomer);
        isStudent = (RadioGroup) findViewById(R.id.isStudent);
        isMarried = (RadioGroup) findViewById(R.id.isMarried);
        isFinanced = (RadioGroup) findViewById(R.id.houseFinanced);
    }

    public void setPersonAdapter() {
        personAdapter = new ArrayAdapter<Person>(this, R.layout.spinner, popUpFamilyMembers);
        personAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void addNewFamilyMember(View view) {
        Spinner mySpinner = new Spinner(view.getContext());
        personAdapter = new ArrayAdapter<Person>(this, R.layout.spinner, popUpFamilyMembers);
        personAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(personAdapter);
        spinnersArray.add(mySpinner);

        spinnersLayout.addView(mySpinner);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadFamilyMembers().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.health_quote, menu);
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

    class LoadFamilyMembers extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HealthQuote.this);
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
            setPersonAdapter();
            pDialog.dismiss();

        }
    }

    public void updateJSONdata() {

        popUpFamilyMembers = new ArrayList<Person>();
        int success = 0;

        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            user_id = loginPref.getString("email", "");

            params.add(new BasicNameValuePair("user_id", user_id));

            JSONObject jObject = jsonParser.makeHttpRequest(Person.VIEW_PERSON_URL, "POST", params);

            success = jObject.getInt(TAG_SUCCESS);
            familyMembers = jObject.getJSONArray(Person.TAG_PERSONS);

            if(success == 1) {
                // loop through all the vehicles
                for(int i = 0; i < familyMembers.length(); i++) {
                    JSONObject obj = familyMembers.getJSONObject(i);

                    // Create a new Vehicle
                    Person myPerson = new Person();

                    String birthdayYear = obj.getString(Person.TAG_BIRTHDAY_YEAR);
                    String birthdayMonth = obj.getString(Person.TAG_BIRTHDAY_MONTH);
                    String birthdayDay = obj.getString(Person.TAG_BIRTHDAY_DAY);

                    String birthday = birthdayMonth + "/" + birthdayDay + "/" + birthdayYear;

                    // Get each element based on it's tag
                    myPerson.setId(obj.getString(House.TAG_ID));
                    myPerson.setPoliceNumber(obj.getString(House.TAG_POLICENUMBER));
                    myPerson.setName(obj.getString(Person.TAG_NAME));
                    myPerson.setMiddleI(obj.getString(Person.TAG_MIDDLE_I));
                    myPerson.setLastName(obj.getString(Person.TAG_LAST_NAME));
                    myPerson.setBirthDay(birthday);
                    myPerson.setAge(obj.getString(Person.TAG_AGE));
                    myPerson.setRelationship(obj.getString(Person.TAG_RELATIONSHIP));

                    popUpFamilyMembers.add(myPerson);
                }
            } else {
                String noVehicles = "There's no vehicles yet! Click the Add vehicle button to add some of your awesome cars!";
                Toast.makeText(HealthQuote.this, noVehicles, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getQuote(View view) {

        double startingPrice = 3600;

        int numMembers = spinnerNumMembers.getSelectedItemPosition() + 1;
        String residenceInformation = spinnerResidenceType.getSelectedItem().toString();

        String isCustomer = "No";
        String student = "No";
        String houseFinanced = "No";

        for(int i = 0; i < numMembers; i++) {
            if(numMembers > 1) {
                startingPrice -= 50;
            }
        }

        if(currentCustomer.getCheckedRadioButtonId() == R.id.yes) {
            isCustomer = "Yes";
            startingPrice -= 200;
        } else {
            startingPrice += 40;
        }

        if(isStudent.getCheckedRadioButtonId() == R.id.studentYes) {
            student = "Yes";
            startingPrice -= 175;
        } else {
            startingPrice += 50;
        }

        String marriageType = "";

        switch (isMarried.getCheckedRadioButtonId()) {
            case R.id.marriedYes:
                marriageType = "Married";
                startingPrice -= 150;
                break;
            case R.id.marriedNo:
                marriageType = "Single";
                startingPrice += 50;
                break;
            case R.id.divorced:
                marriageType = "Divorced";
                break;
        }

        if(isFinanced.getCheckedRadioButtonId() == R.id.financedYes) {
            houseFinanced = "Yes";
            startingPrice += 80;
        } else {
            startingPrice -= 80;
        }

        ArrayList<Person> selectedPersons = new ArrayList<Person>();

        for(Spinner s : spinnersArray) {
            selectedPersons.add(popUpFamilyMembers.get(s.getSelectedItemPosition()));
        }

        Intent intent = new Intent(HealthQuote.this, QuoteValueActivity.class);

        intent.putExtra("quoteType", Person.TAG_PERSONS);
        intent.putExtra("quoteValue", startingPrice);
        intent.putExtra("numFamilyMembers", numMembers);
        intent.putExtra("residenceType", residenceInformation);
        intent.putExtra("isCustomer", isCustomer);
        intent.putExtra("student", student);
        intent.putExtra("houseFinanced", houseFinanced);
        intent.putExtra("marriageType", marriageType);
        intent.putParcelableArrayListExtra(Person.TAG_PERSONS, selectedPersons);
        startActivity(intent);
    }

}
