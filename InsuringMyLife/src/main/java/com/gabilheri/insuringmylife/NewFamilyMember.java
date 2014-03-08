package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gabilheri.insuringmylife.fragments.AaaDialog;
import com.gabilheri.insuringmylife.fragments.DatePickerFragment;
import com.gabilheri.insuringmylife.fragments.NoInternetDialog;
import com.gabilheri.insuringmylife.helpers.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewFamilyMember extends Activity {

    private EditText policeNumber, personName, middleI, personLastName, relationship, age;
    private Button birthdayButton;
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_famiy_member);


        policeNumber = (EditText) findViewById(R.id.policeNumber);
        personName = (EditText) findViewById(R.id.personName);
        middleI = (EditText) findViewById(R.id.middleInitial);
        personLastName = (EditText) findViewById(R.id.personLastName);
        relationship = (EditText) findViewById(R.id.relationship);
        age = (EditText) findViewById(R.id.personAge);
        birthdayButton = (Button) findViewById(R.id.birthdayButton);

    }

    public void showDatePickerDialog(View v) {
        DialogFragment datePicker = new DatePickerFragment(v.getId());
        datePicker.show(getFragmentManager(), "datePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.initial, menu);

        menu.add(1, 1, 1, "About AAA");
        menu.add(2, 2, 2, "About Insuring My Life");
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
        } else if(id == 1) {
            Intent aboutAAA = new Intent(NewFamilyMember.this, AboutAAA.class);
            startActivity(aboutAAA);
        } else if(id == 2) {
            Intent aboutIns = new Intent(NewFamilyMember.this, AboutInsuringMyLife.class);
            startActivity(aboutIns);
        } else if(id == R.id.aaa_logo) {
            DialogFragment aaaD = new AaaDialog();
            aaaD.show(getFragmentManager(), "aaa");
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean getInternetState() {

        ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            DialogFragment noInternet = new NoInternetDialog();
            noInternet.show(getFragmentManager(), "noInternet");
            return false;
        }
    }

    class AddNewFamilyMember extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewFamilyMember.this);
            pDialog.setMessage("Adding Family Member...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            String email = loginPref.getString("email", "");

            try {
                String getPoliceNumber = policeNumber.getText().toString();
                String getName = personName.getText().toString();
                String getLastName = personLastName.getText().toString();
                String getMiddleI = middleI.getText().toString();
                String getRelationship = relationship.getText().toString();
                String getAge = age.getText().toString();

                String[] birthday = birthdayButton.getText().toString().split("/");

                String month = birthday[0];
                String day = birthday[1];
                String year = birthday[2];

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(Person.TAG_USERID, email));
                params.add(new BasicNameValuePair(Person.TAG_POLICENUMBER, getPoliceNumber));
                params.add(new BasicNameValuePair(Person.TAG_NAME, getName));
                params.add(new BasicNameValuePair(Person.TAG_MIDDLE_I, getMiddleI));
                params.add(new BasicNameValuePair(Person.TAG_LAST_NAME, getLastName));
                params.add(new BasicNameValuePair(Person.TAG_BIRTHDAY_YEAR, year));
                params.add(new BasicNameValuePair(Person.TAG_BIRTHDAY_MONTH, month));
                params.add(new BasicNameValuePair(Person.TAG_BIRTHDAY_DAY, day));
                params.add(new BasicNameValuePair(Person.TAG_AGE, getAge));
                params.add(new BasicNameValuePair(Person.TAG_RELATIONSHIP, getRelationship));

                JSONObject json = jsonParser.makeHttpRequest(Person.NEW_PERSON_URL, "POST", params);

                Log.d("New Family Member Attempt", json.toString());

                int success = json.getInt(TAG_SUCCESS);

                if(success == 1) {
                    Intent backToFamily = new Intent(NewFamilyMember.this, ViewFamilyMembers.class);
                    startActivity(backToFamily);
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);
                }

            } catch(JSONException e) {
                e.printStackTrace();
            } catch (java.lang.NullPointerException ex) {
                ex.printStackTrace();
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
