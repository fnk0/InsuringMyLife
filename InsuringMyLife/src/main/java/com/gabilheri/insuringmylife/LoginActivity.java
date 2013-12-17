package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements View.OnClickListener {

    public EditText user, pass;
    public Button mSubmit, mRegister;

    // Progress Dialog
    public ProgressDialog pDialog;

    // JSON parser class
    public JSONParser jsonParser = new JSONParser();

    // PHP login script location

    private static final String LOGIN_URL = "http://162.243.225.173/InsuringMyLife/login.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private boolean userAlreadyLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        File usernamePref = new File("/data/data/com.gabilheri.insuringmylife/shared_prefs/username.xml");
        File passwordPref = new File("/data/data/com.gabilheri.insuringmylife/shared_prefs/password.xml");

        if(usernamePref.exists() && passwordPref.exists()) {
            userAlreadyLoggedIn = true;
        }

        // Setup input fields

        user = (EditText) findViewById(R.id.username);
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
            String username;
            String password;

            if(userAlreadyLoggedIn) {

                SharedPreferences spUser = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences spPass = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

                username = spUser.getString("username", "annonymous");
                password = spPass.getString("password", "pass");

            } else {
                username = user.getText().toString();
                password = pass.getText().toString();
            }

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");

                // Getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                // Check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);

                if(success == 1) {
                    Log.d("Login succesfull!", json.toString());

                    // Save user data
                    SharedPreferences spUsername = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editUsername = spUsername.edit();
                    editUsername.putString("username", username);
                    editUsername.commit();

                    SharedPreferences spPassword = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editPassword = spPassword.edit();
                    editPassword.putString("password", password);
                    editPassword.commit();

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
}
