package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gabilheri.insuringmylife.helpers.House;
import com.gabilheri.insuringmylife.helpers.Person;
import com.gabilheri.insuringmylife.helpers.Vehicle;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuoteValueActivity extends Activity {

    private TextView quoteResult;
    private ArrayList<Vehicle> vehicles;
    private ArrayList<House> houses;
    private ArrayList<Person> persons;
    private String customerName, residenceType, isCustomer, student, vehicleFinanced, marriageType, houseFinanced, quoteType;
    private int numVehicles, numDrivers, numProperties;
    private double quoteValue;
    private String agentEmail;
    public JSONParser jsonParser = new JSONParser();
    public JSONArray agent_info = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_value);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp = getSharedPreferences("loginPref", MODE_PRIVATE);
        customerName = sp.getString("firstName", "") + " " +  sp.getString("lastName", "");

        quoteResult = (TextView) findViewById(R.id.quoteValue);
        quoteValue = getIntent().getDoubleExtra("quoteValue", 0.00);
        quoteResult.setText("$ " + quoteValue);

        quoteType = getIntent().getStringExtra("quoteType");

        if(quoteType != null && quoteType.equals(Vehicle.TAG_VEHICLES)) {
            getVehicleQuote();
        } else if(quoteType != null && quoteType.equals(House.TAG_HOUSES)) {
            getHouseQuote();
        }

    }

    public void getVehicleQuote() {
        numVehicles = getIntent().getIntExtra("numVehicles", 1);
        numDrivers = getIntent().getIntExtra("numDrivers", 1);
        residenceType = getIntent().getStringExtra("residenceType");
        isCustomer = getIntent().getStringExtra("isCustomer");
        student = getIntent().getStringExtra("student");
        vehicleFinanced = getIntent().getStringExtra("vehicleFinanced");
        marriageType = getIntent().getStringExtra("marriageType");

        vehicles = getIntent().getParcelableArrayListExtra("vehicles");
        int counter = 0;

        for(Vehicle v : vehicles) {
            Log.d("Vehicle " + counter + ":", v.toString());
            counter++;
        }
    }

    public void getHouseQuote() {
        numProperties = getIntent().getIntExtra("numProperties", 1);
        residenceType = getIntent().getStringExtra("residenceType");
        isCustomer = getIntent().getStringExtra("isCustomer");
        student = getIntent().getStringExtra("student");
        houseFinanced = getIntent().getStringExtra("houseFinanced");
        marriageType = getIntent().getStringExtra("marriageType");

        houses = getIntent().getParcelableArrayListExtra("houses");
        int counter = 0;

        for(House h : houses) {
            Log.d("House " + counter + ":", h.toString());
            counter++;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetAgentEmail().execute();
    }

    public void sendToAgent(View view) {

        if(quoteType != null && quoteType.equals(Vehicle.TAG_VEHICLES)) {
            sendVehicleInfo();
        } else if(quoteType != null && quoteType.equals(House.TAG_HOUSES)) {
            sendPropertyInfo();
        }

    }

    public void sendVehicleInfo() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{agentEmail})
                .putExtra(Intent.EXTRA_SUBJECT, "Vehicle Quote for Insuring My Life Customer: " + customerName);
        int counter = 1;
        int counter2 = 0;
        String extra = "The estimate quoted value for " + customerName + " is: $" + quoteValue + "\n";

        extra += "General Information about my request:\n";
        extra += "Number of Drivers: " + numDrivers + "\n";
        extra += "Number of Vehicles: " + numVehicles + "\n";
        extra += "Residence Type: " + residenceType + "\n";
        extra += "Is current a customer? " + isCustomer + "\n";
        extra += "Marriage Status: " + marriageType + "\n";
        extra += "Is a student? " + student + "\n";
        extra += "Vehicle Financed? " + vehicleFinanced + "\n\n";

        if(vehicles.size() != 0) {
            extra += "Vehicles Information: \n";
        }

        // Debugging stuff
        for(Vehicle v : vehicles) {
            Log.d("Vehicle " + counter2 + ":", v.toString());
            counter2++;
        }

        for(Vehicle v : vehicles) {

            extra +=
                    "\n" +
                            "Driver number: " + counter + "\n" +
                            "Vehicle ID: " + v.getId() + "\n" +
                            "Vehicle Police Number: " + v.getPoliceNumber() + "\n" +
                            "Vehicle Brand: " + v.getBrand() + "\n" +
                            "Vehicle Model: " + v.getModel() + "\n" +
                            "Vehicle Year: " + v.getYear() + "\n" +
                            "Vehicle Color" + v.getColor() + "\n" +
                            "Vehicle License Plate: " + v.getLicensePlate() + "\n" +
                            "Vehicle Main Driver: " + v.getMainDriver() + "\n" +
                            "Main Driver License #: " + v.getDriverLicense() + "\n" +
                            "Main Driver License State: " + v.getLicenseState() + "\n" +
                            "Main Driver Birthday: " + v.getDriverBirthday() + "\n" +
                            "Main Driver Gender: " + v.getDriverGender() + "\n";
            counter++;
        }

        emailIntent.putExtra(Intent.EXTRA_TEXT, extra);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(QuoteValueActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendPropertyInfo() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{agentEmail})
                .putExtra(Intent.EXTRA_SUBJECT, "Property Quote for Insuring My Life Customer: " + customerName);
        int counter = 1;
        int counter2 = 0;
        String extra = "The estimate quoted value for " + customerName + " is: $" + quoteValue + "\n";

        extra += "General Information about my request:\n";
        extra += "Number of Properties: " + numProperties + "\n";
        extra += "Residence Type: " + residenceType + "\n";
        extra += "Is current a customer? " + isCustomer + "\n";
        extra += "Marriage Status: " + marriageType + "\n";
        extra += "Is a student? " + student + "\n";
        extra += "Home Financed? " + houseFinanced + "\n\n";

        if(houses.size() != 0) {
            extra += "Properties Information: \n";
        }

        // Debugging stuff
        for(House h : houses) {
            Log.d("House " + counter2 + ":", h.toString());
            counter2++;
        }

        for(House h : houses) {

            extra +=
                    "\n" +
                    "Property ID: " + h.getId() + "\n" +
                    "Property Police Number: " + h.getPoliceNumber() + "\n" +
                    "Property Address: " + h.getAddress() + "\n" +
                    "Property City: " + h.getCity() + "\n" +
                    "Property State: " + h.getState() + "\n" +
                    "Property Zip Code" + h.getZipCode() + "\n" +
                    "Property Ownership Status: " + h.getPayed() + "\n" +
            counter++;
        }

        emailIntent.putExtra(Intent.EXTRA_TEXT, extra);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(QuoteValueActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }


    class GetAgentEmail extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String ... args) {
            int success;
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String zipCode;

            try {

                SharedPreferences loginPref = getSharedPreferences("loginPref", MODE_PRIVATE);
                customerName = loginPref.getString("name", "") + " " + loginPref.getString("last_name", "");
                zipCode = loginPref.getString("zip_code", "");
                params.add(new BasicNameValuePair("zip_code", zipCode));

                JSONObject jObject = jsonParser.makeHttpRequest(MyAgent.GET_AGENT_URL, "POST", params);

                success = jObject.getInt("success");
                agent_info = jObject.getJSONArray("agent_data");

                if(success == 1) {
                    for(int i = 0; i < agent_info.length(); i++) {
                        JSONObject obj = agent_info.getJSONObject(i);

                        agentEmail = obj.getString("email");

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            super.onPostExecute(file_url);
        }
    }

    public void getAnotherQuote(View view) {
        Intent intent = new Intent(QuoteValueActivity.this, QuoteActivity.class);
        startActivity(intent);
    }

    public void backToMain(View view) {
        Intent intent = new Intent(QuoteValueActivity.this, InitialActivity.class);
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quote_value, menu);
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
