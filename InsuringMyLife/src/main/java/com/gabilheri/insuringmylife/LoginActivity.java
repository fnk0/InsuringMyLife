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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gabilheri.insuringmylife.fragments.NoInternetDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements View.OnClickListener {

    public EditText userEmail, pass;
    public Button mSubmit, mRegister, mForgot;

    // Progress Dialog
    public ProgressDialog pDialog;

    // JSON parser class
    public JSONParser jsonParser = new JSONParser();

    // PHP login script location

    private static final String LOGIN_URL = "http://162.243.225.173/InsuringMyLife/login.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_NAME = "name";
    private static final String TAG_LASTNAME = "last_name";
    private static final String TAG_ZIP = "zip_code";
    public boolean userAlreadyLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp = getSharedPreferences("loginPref", MODE_PRIVATE);
        if(getInternetState()) {
            if(sp.contains(TAG_EMAIL) && sp.contains(TAG_PASSWORD
            )) {
                userAlreadyLoggedIn = true;
                new AttemptLogin().execute();
            }
        }
        // Setup input fields

        userEmail = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);

        // Setup buttons
        mSubmit = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);
        mForgot = (Button) findViewById(R.id.forgotUsernamePassword);

        // Register listener

        mSubmit.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mForgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        // Determine which button was pressed:
        switch (v.getId()) {
            case R.id.login:
                if(getInternetState()) {
                    new AttemptLogin().execute();
                }
                break;
            case R.id.register:
                Intent i = new Intent(this, RegisterActivity.class);
                startActivity(i);
                break;
            case R.id.forgotUsernamePassword:
                Intent forgot = new Intent(this, ForgotPassword.class);
                startActivity(forgot);
                break;
            default:
                break;
        }
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


    // Async task is a separated thread than the thread that runs the GUI
    // Any type of networking should be done with asynctask
    // Using async task avoid slowing down the application while the php file is requested

    class AttemptLogin extends AsyncTask<String, String, String> {

        // Three methods gets called, first preExecute, then doInBackground
        // Once doInBackground is completed, the onPost execute method will be called

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
            String firstName;
            String lastName;
            String zipCode;

            if(userAlreadyLoggedIn) {

                SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
                email = loginPref.getString(TAG_EMAIL, "");
                password = loginPref.getString(TAG_PASSWORD, "");

            } else {
                email = userEmail.getText().toString();
                password = pass.getText().toString();
            }

            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair(TAG_EMAIL, email));
                params.add(new BasicNameValuePair(TAG_PASSWORD, password));

                Log.d("request!", "starting");

                // Getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                // Check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                lastName = json.getString(TAG_LASTNAME);
                firstName = json.getString(TAG_NAME);
                zipCode = json.getString(TAG_ZIP);

                if(success == 1) {
                    Log.d("Login succesfull!", json.toString());

                    // Save user data
                    SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
                    SharedPreferences.Editor loginEditor = loginPref.edit();
                    loginEditor.putString(TAG_EMAIL, email);
                    loginEditor.putString(TAG_PASSWORD, password);
                    loginEditor.putString(TAG_NAME, firstName);
                    loginEditor.putString(TAG_LASTNAME, lastName);
                    loginEditor.putString(TAG_ZIP, zipCode);
                    loginEditor.commit();

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
