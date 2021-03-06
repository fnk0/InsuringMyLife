package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends Activity implements View.OnClickListener {

    // Declaring variables and constants
    private EditText firstNameField, lastNameField, passwordField, repeatPasswordField, emailField;
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

        firstNameField = (EditText) findViewById(R.id.firstName);
        lastNameField = (EditText) findViewById(R.id.lastName);
        passwordField = (EditText) findViewById(R.id.password);
        repeatPasswordField = (EditText) findViewById(R.id.repeatPassword);
        emailField = (EditText) findViewById(R.id.email);

        mRegister = (Button) findViewById(R.id.register);
        mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
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
            Toast.makeText(RegisterActivity.this, "Password should be at least 6 chars long!", Toast.LENGTH_LONG).show();
        } else if(counter != 1) {
            Toast.makeText(RegisterActivity.this, "The e-mail does not seen valid!", Toast.LENGTH_LONG).show();
        } else {
            new CreateUser().execute();
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

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", firstName));
                params.add(new BasicNameValuePair("last_name", lastName));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("email", email));

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
        getMenuInflater().inflate(R.menu.register, menu);
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