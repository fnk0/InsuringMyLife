package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyAgent extends Activity {

    private ProgressDialog pDialogLoad;
    private HashMap<String, String> agentInfoList;
    public JSONParser jsonParser = new JSONParser();
    public JSONArray agent_info = null;
    private TextView agentName, agentLanguages, agentPhoneNum, agentHours, agentEmail;
    private String sAgentName, sAgentLanguages, sAgentPhoneNum, sAgentHours, sAgentEmail, customerName;
    private ImageView agentPicture;
    Bitmap image;

    public static final String GET_AGENT_URL = "http://162.243.225.173/InsuringMyLife/get_agent.php";
    private static final String GET_AGENT_PICTURE = "http://162.243.225.173/InsuringMyLife/assets/agents/";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_agent);
        loadViews();
    }

    private void loadViews() {
        agentName = (TextView) findViewById(R.id.agentName);
        agentLanguages = (TextView) findViewById(R.id.agentLanguages);
        agentPhoneNum = (TextView) findViewById(R.id.agentPhoneNumber);
        agentHours = (TextView) findViewById(R.id.agentHours);
        agentEmail = (TextView) findViewById(R.id.agentEmail);
        agentPicture = (ImageView) findViewById(R.id.agentPicture);
    }

    @Override
    protected void onResume() {
        new LoadAgent().execute();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_agent, menu);
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

    public void callAgent(View v) {
        String uri = "tel:" + sAgentPhoneNum.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    public void addContact(View v) {

        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, sAgentEmail)
              .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
              .putExtra(ContactsContract.Intents.Insert.COMPANY, "AAA")
              .putExtra(ContactsContract.Intents.Insert.JOB_TITLE, "Insurance Agent")
              .putExtra(ContactsContract.Intents.Insert.NAME, sAgentName)
              .putExtra(ContactsContract.Intents.Insert.PHONE, sAgentPhoneNum)
              .putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
        startActivity(intent);
    }

    public void sendEmail(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{sAgentEmail})
              .putExtra(Intent.EXTRA_SUBJECT, "Insuring My Life Customer: " + customerName);

        try {
            startActivity(Intent.createChooser(intent, "Send Email"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(MyAgent.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateJsonData() {
        int success;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        agentInfoList = new HashMap<String, String>();
        String zipCode;

        try {

            SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
            customerName = loginPref.getString("name", "") + " " + loginPref.getString("last_name", "");
            zipCode = loginPref.getString("zip_code", "");
            params.add(new BasicNameValuePair("zip_code", zipCode));

            JSONObject jObject = jsonParser.makeHttpRequest(GET_AGENT_URL, "POST", params);

            success = jObject.getInt(TAG_SUCCESS);
            agent_info = jObject.getJSONArray("agent_data");

            if(success == 1) {
                for(int i = 0; i < agent_info.length(); i++) {
                    JSONObject obj = agent_info.getJSONObject(i);

                    agentInfoList.put("zip_code", obj.getString("zip_code"));
                    agentInfoList.put("name", obj.getString("name"));
                    agentInfoList.put("last_name", obj.getString("last_name"));
                    agentInfoList.put("phone_num", obj.getString("phone_num"));
                    agentInfoList.put("email", obj.getString("email"));
                    agentInfoList.put("languages", obj.getString("languages"));
                    agentInfoList.put("hours", obj.getString("hours"));
                    Log.d("LANGUAGES: " ,obj.getString("languages"));

                    String url = GET_AGENT_PICTURE + agentInfoList.get("zip_code") + ".jpg";
                    image = downloadBitmap(url);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateFields() {
        sAgentName = agentInfoList.get("name") + " " + agentInfoList.get("last_name");
        sAgentPhoneNum = agentInfoList.get("phone_num");
        sAgentHours = agentInfoList.get("hours");
        sAgentEmail = agentInfoList.get("email");
        agentName.setText(sAgentName);
        if(agentInfoList.get("languages").contains(",")) {
            String[] languages = agentInfoList.get("languages").split(",");
            String languagesLine = "";

            for(int i = 0; i < languages.length; i++) {
                languagesLine += languages[i] + "\n";
            }
            sAgentLanguages = languagesLine;

        } else {
            sAgentLanguages = agentInfoList.get("languages");
        }
        agentLanguages.setText(sAgentLanguages);
        agentPhoneNum.setText(sAgentPhoneNum);
        agentHours.setText(sAgentHours);
        agentEmail.setText(sAgentEmail);

        if(image != null) {
            agentPicture.setBackground(null);
            agentPicture.setImageBitmap(image);
        }
    }


    class LoadAgent extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialogLoad = new ProgressDialog(MyAgent.this);
            pDialogLoad.setMessage("Loading Agent...");
            pDialogLoad.setIndeterminate(false);
            pDialogLoad.setCancelable(true);
            pDialogLoad.show();
        }

        @Override
        protected String doInBackground(String ... args) {
            updateJsonData();
            return null;
        }

        protected void onPostExecute(String file_url) {
            super.onPostExecute(file_url);
            updateFields();
            pDialogLoad.dismiss();
        }
    }

    private Bitmap downloadBitmap(String url) {
        // initilize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttoGet request
        final HttpGet getRequest = new HttpGet(url);
        try {

            HttpResponse response = client.execute(getRequest);

            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode +
                        " while retrieving bitmap from " + url);
                return null;

            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();

                    // decoding stream data back into image Bitmap that android understands
                    image = BitmapFactory.decodeStream(inputStream);


                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // You Could provide a more explicit error message for IOException
            getRequest.abort();
            Log.e("ImageDownloader", "Something went wrong while" +
                    " retrieving bitmap from " + url + e.toString());
        }

        return image;
    }

}
