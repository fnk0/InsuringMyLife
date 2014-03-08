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
import android.widget.Spinner;
import android.widget.Toast;

import com.gabilheri.insuringmylife.fragments.AaaDialog;
import com.gabilheri.insuringmylife.fragments.NoInternetDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends Activity implements View.OnClickListener {

    // Declaring variables and constants
    private EditText firstNameField, lastNameField, passwordField, repeatPasswordField, emailField, answer1, answer2, answer3;
    private Spinner securityQuestion1, securityQuestion2, securityQuestion3;
    private Button mRegister;

    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    private static final String REGISTER_URL = "http://162.243.225.173/InsuringMyLife/new_user.php";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        firstNameField = (EditText) findViewById(R.id.firstName);
        lastNameField = (EditText) findViewById(R.id.lastName);
        passwordField = (EditText) findViewById(R.id.password);
        repeatPasswordField = (EditText) findViewById(R.id.repeatPassword);
        emailField = (EditText) findViewById(R.id.email);
        securityQuestion1 = (Spinner) findViewById(R.id.question1);
        securityQuestion2 = (Spinner) findViewById(R.id.question2);
        securityQuestion3 = (Spinner) findViewById(R.id.question3);
        answer1 = (EditText) findViewById(R.id.securityQuestion1);
        answer2 = (EditText) findViewById(R.id.securityQuestion2);
        answer3 = (EditText) findViewById(R.id.securityQuestion3);


        mRegister = (Button) findViewById(R.id.register);
        mRegister.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
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
    public void onClick(View v) {

        if(getInternetState() == true) {
            String password = passwordField.getText().toString();
            String repeatPassword = repeatPasswordField.getText().toString();
            String email = emailField.getText().toString();

            int counter = 0;

            for(int i = 0; i < email.length(); i++) {
                if(email.charAt(i) == '@') {
                    counter++;
                }
            }

            if(!password.equals(repeatPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords don't match!", Toast.LENGTH_LONG).show();
            } else if(password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password should be at least 6 characters long!", Toast.LENGTH_LONG).show();
            } else if(counter != 1) {
                Toast.makeText(RegisterActivity.this, "The e-mail does not seen valid!", Toast.LENGTH_LONG).show();
            } else {
                new CreateUser().execute();
            }
        }

    }

    class CreateUser extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int success;
            String firstName = firstNameField.getText().toString();
            String lastName = lastNameField.getText().toString();
            String password = passwordField.getText().toString();
            String email = emailField.getText().toString();
            String question1= securityQuestion1.getSelectedItem().toString();
            String question2 = securityQuestion2.getSelectedItem().toString();
            String question3 = securityQuestion3.getSelectedItem().toString();

            String sAnswer1 = answer1.getText().toString();
            String sAnswer2 = answer2.getText().toString();
            String sAnswer3 = answer3.getText().toString();

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", firstName));
                params.add(new BasicNameValuePair("last_name", lastName));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("security_question1", question1));
                params.add(new BasicNameValuePair("security_question2", question2));
                params.add(new BasicNameValuePair("security_question3", question3));
                params.add(new BasicNameValuePair("answer1", sAnswer1));
                params.add(new BasicNameValuePair("answer2", sAnswer2));
                params.add(new BasicNameValuePair("answer3", sAnswer3));


                SharedPreferences userNamePref = getSharedPreferences("firstName", MODE_PRIVATE);
                SharedPreferences.Editor userNameEditor = userNamePref.edit();
                userNameEditor.putString("firstName", firstName);
                userNameEditor.putString("lastName", lastName);
                userNameEditor.commit();

                Log.d("request!", "starting");

                // Posting user data to script

                JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", params);

                // Full Json response
                Log.d("Register attempt", json.toString());

                // Json success element
                success = json.getInt(TAG_SUCCESS);

                if(success == 1) {
                    Log.d("User created!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Register failed!", json.getString(TAG_MESSAGE));
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
                Toast.makeText(RegisterActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
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
            Intent aboutAAA = new Intent(RegisterActivity.this, AboutAAA.class);
            startActivity(aboutAAA);
        } else if(id == 2) {
            Intent aboutIns = new Intent(RegisterActivity.this, AboutInsuringMyLife.class);
            startActivity(aboutIns);
        } else if(id == R.id.aaa_logo) {
            DialogFragment aaaD = new AaaDialog();
            aaaD.show(getFragmentManager(), "aaa");
        }

        return super.onOptionsItemSelected(item);
    }
}