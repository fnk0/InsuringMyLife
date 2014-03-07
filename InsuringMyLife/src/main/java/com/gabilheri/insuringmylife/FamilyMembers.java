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

import com.gabilheri.insuringmylife.adapters.PersonExpandableAdapter;
import com.gabilheri.insuringmylife.helpers.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FamilyMembers extends Activity {

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
        Intent newMeber = new Intent(FamilyMembers.this, NewFamilyMember.class);
        startActivity(newMeber);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadFamilyMembers().execute();
    }

    public void updateList() {
        ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
        PersonExpandableAdapter adapter = new PersonExpandableAdapter(this, personGroup);
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.family_members, menu);
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
                    String lastName = obj.getString(Person.TAG_LAST_NAME);
                    String birthdayYear = obj.getString(Person.TAG_BIRTHDAY_YEAR);
                    String birthdayMonth = obj.getString(Person.TAG_BIRTHDAY_MONTH);
                    String birthdayDay = obj.getString(Person.TAG_BIRTHDAY_DAY);
                    String age = obj.getString(Person.TAG_AGE);
                    String relationship = obj.getString(Person.TAG_RELATIONSHIP);
                    String hasInsurace = obj.getString(Person.TAG_INSURANCE);

                    String birthday = birthdayMonth + "/" + birthdayDay + "/" + birthdayYear;

                    ListRowGroup group = new ListRowGroup(name + " " + lastName, "name");
                    group.children.add("Full Name: " + name + lastName);
                    group.children.add(policeNumber);
                    group.children.add(relationship);
                    group.children.add(birthday + "  -  " + age + " years old.");
                    group.children.add(hasInsurace);

                    personGroup.append(i, group);
                }

            } else {
                String noHouses = "There's no Family Members yet!";
                Toast.makeText(FamilyMembers.this, noHouses, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class LoadFamilyMembers extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FamilyMembers.this);
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
