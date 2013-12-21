package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements View.OnClickListener {

    public EditText userEmail, pass;
    public Button mSubmit, mRegister;

    // Progress Dialog
    public ProgressDialog pDialog;

    // JSON parser class
    public JSONParser jsonParser = new JSONParser();
    public JSONArray userProfiles = null;

    // PHP login script location

    private static final String LOGIN_URL = "http://162.243.225.173/InsuringMyLife/login.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public boolean userAlreadyLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp = getSharedPreferences("loginPref", MODE_PRIVATE);
        if(sp.contains("email") && sp.contains("password")) {
            userAlreadyLoggedIn = true;
            new AttemptLogin().execute();
        }
        // Setup input fields

        userEmail = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);

        // Setup buttons
        mSubmit = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);

        // Register listener

        mSubmit.setOnClickListener(this);
        mRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        // Determine which button was pressed:
        switch (v.getId()) {
            case R.id.login:
                new AttemptLogin().execute();
                break;
            case R.id.register:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    // Async task is a separated thread than the thread that runs the GUI
    // Any type of networking should be done with asynctask
    // Using async task avoid slowing down the application while the php file is requested

    class AttemptLogin extends AsyncTask<String, String, String> {

        // Three methods gets called, first preExecute, then doInBackground
        // Once doInBackground is completed, the onPost execute method will be called

        boolean failure = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            // Check for success tag
            int success;
            String email;
            String password;

            if(userAlreadyLoggedIn) {

                SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
                email = loginPref.getString("email", "");
                password = loginPref.getString("password", "");

            } else {
                email = userEmail.getText().toString();
                password = pass.getText().toString();
            }

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");

                // Getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                // Check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                userProfiles = json.getJSONArray("profiles");

                if(success == 1) {
                    Log.d("Login succesfull!", json.toString());

                    // Save user data
                    SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
                    SharedPreferences.Editor loginEditor = loginPref.edit();
                    loginEditor.putString("email", email);
                    loginEditor.putString("password", password);
                    loginEditor.commit();


                    int counter = 0;
                    for(int x = 0; x < userProfiles.length(); x++) {
                        JSONObject obj = userProfiles.getJSONObject(x);

                        String profileName = obj.getString("name");
                        String profileLastName = obj.getString("last_name");
                        String fullName = "" + profileName + " " + profileLastName;
                        SharedPreferences profilesPref = getSharedPreferences("profilesPref" + x, MODE_PRIVATE);
                        SharedPreferences.Editor profileEditor = profilesPref.edit();
                        profileEditor.putString("fullName", fullName);
                        profileEditor.commit();
                        counter++;
                    }

                    SharedPreferences profilesNumber = getSharedPreferences("numProfiles", MODE_PRIVATE);
                    SharedPreferences.Editor profilesNumberEditor = profilesNumber.edit();
                    profilesNumberEditor.putInt("numProfiles", counter);
                    profilesNumberEditor.commit();

                    Intent i = new Intent(LoginActivity.this, InitialActivity.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        // After completing background task dismiss the progress dialog
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(LoginActivity.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}
