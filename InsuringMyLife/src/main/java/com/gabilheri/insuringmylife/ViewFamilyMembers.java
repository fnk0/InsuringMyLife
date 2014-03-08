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
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.gabilheri.insuringmylife.adapters.PersonExpandableAdapter;
import com.gabilheri.insuringmylife.fragments.AaaDialog;
import com.gabilheri.insuringmylife.fragments.NoInternetDialog;
import com.gabilheri.insuringmylife.helpers.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewFamilyMembers extends Activity {

    public ProgressDialog pDialog;
    public JSONParser jsonParser = new JSONParser();

    public String user_id;

    // Create an Array to hold the houses
    public JSONArray persons = null;

    // Manage the houses with an Arraylist
    private ArrayList<HashMap<String, String>> personList;

    // GroupList Stuff
    SparseArray<ListRowGroup> personGroup = new SparseArray<ListRowGroup>();

    // JSON ID's
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstancebirthdayYear) {
        super.onCreate(savedInstancebirthdayYear);
        setContentView(R.layout.activity_family_members);

        SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
        user_id = loginPref.getString("email", "");
    }

    public void addFamilyMember(View v) {
        Intent newMember = new Intent(ViewFamilyMembers.this, NewFamilyMember.class);
        startActivity(newMember);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(getInternetState()) {
            new LoadFamilyMembers().execute();
        }
    }

    public void updateList() {
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
        PersonExpandableAdapter adapter = new PersonExpandableAdapter(this, personGroup);
        listView.setAdapter(adapter);
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
            Intent aboutAAA = new Intent(ViewFamilyMembers.this, AboutAAA.class);
            startActivity(aboutAAA);
        } else if(id == 2) {
            Intent aboutIns = new Intent(ViewFamilyMembers.this, AboutInsuringMyLife.class);
            startActivity(aboutIns);
        } else if(id == R.id.aaa_logo) {
            DialogFragment aaaD = new AaaDialog();
            aaaD.show(getFragmentManager(), "aaa");
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateJSONdata() {

        // Initiate the ArrayList to hold the Json data

        personList = new ArrayList<HashMap<String, String>>();
        int success = 0;
        // Building parameters for the request

        try {

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            user_id = loginPref.getString("email", "");

            params.add(new BasicNameValuePair("user_id", user_id));

            JSONObject jObject = jsonParser.makeHttpRequest(Person.VIEW_PERSON_URL, "POST", params);

            success = jObject.getInt(TAG_SUCCESS);
            persons = jObject.getJSONArray(Person.TAG_PERSONS);

            if(success == 1) {
                // loop through all the houses
                for(int i = 0; i < persons.length(); i++) {
                    JSONObject obj = persons.getJSONObject(i);

                    // Get each element based on it's tag
                    String userID = obj.getString(Person.TAG_USERID);
                    String policeNumber = obj.getString(Person.TAG_POLICENUMBER);
                    String name = obj.getString(Person.TAG_NAME);
                    String middleI = obj.getString(Person.TAG_MIDDLE_I);
                    String lastName = obj.getString(Person.TAG_LAST_NAME);
                    String birthdayYear = obj.getString(Person.TAG_BIRTHDAY_YEAR);
                    String birthdayMonth = obj.getString(Person.TAG_BIRTHDAY_MONTH);
                    String birthdayDay = obj.getString(Person.TAG_BIRTHDAY_DAY);
                    String age = obj.getString(Person.TAG_AGE);
                    String relationship = obj.getString(Person.TAG_RELATIONSHIP);

                    String birthday = birthdayMonth + "/" + birthdayDay + "/" + birthdayYear;

                    ListRowGroup group = new ListRowGroup(name + " " + lastName, "name");
                    group.children.add("Full Name: " + name + " " + middleI + ". " + lastName);
                    group.children.add(policeNumber);
                    group.children.add(relationship);
                    group.children.add(birthday);
                    group.children.add(age + " years old.");

                    personGroup.append(i, group);
                }

            } else {
                String noHouses = "There's no Family Members yet!";
                Toast.makeText(ViewFamilyMembers.this, noHouses, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class LoadFamilyMembers extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewFamilyMembers.this);
            pDialog.setMessage("Loading Family Members...");
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
