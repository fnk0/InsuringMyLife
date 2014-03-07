package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ForgotPassword extends Activity {

    private LinearLayout questionsLayout, getPasswordStuff;
    private Button getAnswers, answerQuestions, resetPassword;
    private EditText securityAnswer1, securityAnswer2, emailField, passwordField, repeatPasswordField;
    private TextView securityQuestion1, securityQuestion2;
    private String question1, question2, answer1, answer2;

    private ProgressDialog pDialog;
    private ArrayList<String> questionsArrayList, answersArrayList;
    public JSONParser jsonParser = new JSONParser();
    public JSONArray questions = null;

    private static final String QUESTIONS_URL = "http://162.243.225.173/InsuringMyLife/view_questions.php";
    private static final String UPDATE_PASSWORD_URL = "http://162.243.225.173/InsuringMyLife/update_password.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        questionsLayout = (LinearLayout) findViewById(R.id.getElements);
        getPasswordStuff = (LinearLayout) findViewById(R.id.getPasswordStuff);

        getAnswers = (Button) findViewById(R.id.getQuestions);
        resetPassword = (Button) findViewById(R.id.resetPassword);
        answerQuestions = (Button) findViewById(R.id.answerQuestions);

        securityQuestion1 = (TextView) findViewById(R.id.securityQuestion1);
        securityQuestion2 = (TextView) findViewById(R.id.securityQuestion2);

        securityAnswer1 = (EditText) findViewById(R.id.securityAnswer1);
        securityAnswer2 = (EditText) findViewById(R.id.securityAnswer2);

        emailField = (EditText) findViewById(R.id.userEmail);

        passwordField = (EditText) findViewById(R.id.newPassword);
        repeatPasswordField = (EditText) findViewById(R.id.confirmPassword);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.forgot_password, menu);
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

    public void getAnswers(View view) {
        new LoadQuestions().execute();
    }

    public void answerQuestions(View v) {

        String answeredQuestion1 = securityAnswer1.getText().toString();
        String answeredQuestion2 = securityAnswer2.getText().toString();

        Log.d("Answer 1", answersArrayList.get(0));
        Log.d("Answer 2", answersArrayList.get(1));
        Log.d("User Answer 1", answeredQuestion1);
        Log.d("User Answer 1", answeredQuestion2);

        if(answeredQuestion1.equals(answersArrayList.get(0)) && answeredQuestion2.equals(answersArrayList.get(1))) {
            questionsLayout.removeAllViews();
            getPasswordStuff.setVisibility(View.VISIBLE);
        }
    }

    public void resetPassword(View v) {

        String password = passwordField.getText().toString();
        String repeatPassword = repeatPasswordField.getText().toString();

        if(!password.equals(repeatPassword)) {
            Toast.makeText(ForgotPassword.this, "Passwords don't match!", Toast.LENGTH_LONG).show();
        } else if(password.length() < 6) {
            Toast.makeText(ForgotPassword.this, "Password should be at least 6 characters long!", Toast.LENGTH_LONG).show();
        }  else {
            new UpdatePassword().execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    class LoadQuestions extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgotPassword.this);
            pDialog.setMessage("Loading Questions...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String email = emailField.getText().toString();
            questionsArrayList = new ArrayList<String>();
            answersArrayList = new ArrayList<String>();
            // Initiate the ArrayList to hold the Json data

            int success = 0;
            // Building parameters for the request

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", email));

                JSONObject jObject = jsonParser.makeHttpRequest(QUESTIONS_URL, "POST", params);

                success = jObject.getInt(TAG_SUCCESS);
                questions = jObject.getJSONArray("questions");

                if(success == 1) {
                    // loop through all the vehicles
                    for(int i = 0; i < questions.length(); i++) {
                        JSONObject obj = questions.getJSONObject(i);

                        // Get each element based on it's tag

                        int num1 = obj.getInt("number1");
                        int num2 = obj.getInt("number2");

                        question1 = obj.getString("security_question" + num1);
                        question2 = obj.getString("security_question" + num2);

                        answer1 = obj.getString("answer" + num1);
                        answer2 = obj.getString("answer" + num2);

                        questionsArrayList.add(question1);
                        questionsArrayList.add(question2);

                        answersArrayList.add(answer1);
                        answersArrayList.add(answer2);

                    }

                } else {
                    String noVehicles = "There's no vehicles yet! Click the Add vehicle button to add some of your awesome cars!";
                    Toast.makeText(ForgotPassword.this, noVehicles, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            securityQuestion1.setText(questionsArrayList.get(0));
            securityQuestion2.setText(questionsArrayList.get(1));
            questionsLayout.setVisibility(View.VISIBLE);
            pDialog.dismiss();

        }
    }

    class UpdatePassword extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgotPassword.this);
            pDialog.setMessage("Updating Password...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();
            questionsArrayList = new ArrayList<String>();
            answersArrayList = new ArrayList<String>();
            // Initiate the ArrayList to hold the Json data

            int success;
            // Building parameters for the request

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("password", password));

                JSONObject jObject = jsonParser.makeHttpRequest(UPDATE_PASSWORD_URL, "POST", params);

                Log.d("Querying: ", UPDATE_PASSWORD_URL);
                Log.d("With email: ", email);
                Log.d("And password: ", password);

                success = jObject.getInt(TAG_SUCCESS);

                Log.d("My Result is...", jObject.getString(TAG_MESSAGE));

                if(success == 1) {

                    Intent loginActivity = new Intent(ForgotPassword.this, LoginActivity.class);
                    startActivity(loginActivity);

                }
            } catch (JSONException e) {
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
