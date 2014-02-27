package com.gabilheri.insuringmylife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcus on 1/27/14.
 */
public class JSONSender extends AsyncTask<String, String, String> {

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    ProgressDialog pDialog;
    String dialogMessage;
    Activity activity;



    public JSONSender(Activity activity, String dialogMessage, List<NameValuePair> params) {

        this.params = params;
        this.dialogMessage = dialogMessage;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {

        pDialog = new ProgressDialog(activity);
        pDialog.setMessage(dialogMessage);


    }


    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String file_url) {
        pDialog.dismiss();
        if (file_url != null) {
            Toast.makeText(activity, file_url, Toast.LENGTH_LONG).show();
        }
    }
}
