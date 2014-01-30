package com.gabilheri.insuringmylife;

import android.app.ProgressDialog;
import android.os.AsyncTask;

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



    public JSONSender(ProgressDialog dialog, String dialogMessage, List<NameValuePair> params) {

        this.params = params;
        this.pDialog = dialog;
        this.dialogMessage = dialogMessage;
    }

    @Override
    protected void onPreExecute() {

        pDialog.setMessage(dialogMessage);


    }


    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}
