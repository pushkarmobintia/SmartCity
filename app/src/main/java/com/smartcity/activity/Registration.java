package com.smartcity.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gcm.GCMRegistrar;
import com.smartcity.Config;
import com.smartcity.Controller;
import com.smartcity.R;
import com.smartcity.network.InternetConnection;
import com.smartcity.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mobintia on 27/9/16.
 */
public class Registration extends AppCompatActivity {

    private EditText firstname,primarymobile,email;// secondarymobile, , pincode, address, landmark, country, state, city;
    private Button business_registration_form_one_button_save;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    SharedPreferences sharedPreferencesRemember;
    private InternetConnection internetConnection = new InternetConnection();
    Controller aController;
    AsyncTask<Void, Void, Void> mRegisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        sharedPreferencesRemember = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        aController = (Controller) getApplicationContext();
        if (internetConnection.isNetworkAvailable(getApplicationContext())) {

            String deviceIMEI = "";
            if(Config.SECOND_SIMULATOR){
                deviceIMEI = "000000000000001";
            }
            else
            {
            }

            // Make sure the device has the proper dependencies.
            GCMRegistrar.checkDevice(this);

            // Make sure the manifest permissions was properly set
            GCMRegistrar.checkManifest(this);

            // Register custom Broadcast receiver to show messages on activity
            registerReceiver(mHandleMessageReceiver, new IntentFilter(
                    Config.DISPLAY_REGISTRATION_MESSAGE_ACTION));

            // Get GCM registration id
            final String regId = GCMRegistrar.getRegistrationId(this);

            // Check if regid already presents
            if (regId.equals("")) {
                // Register with GCM
                GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);

            } else {

                // Device is already registered on GCM Server
                if (GCMRegistrar.isRegisteredOnServer(this)) {
                    final Context context = this;
                    Log.i("regId", "Device registered: regId = " + regId);
                    Constants.CONSTANT_REGID_NOTIFICATION = regId;

//                    myHandler = new Handler();
//                    myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);

                } else {

                    Constants.CONSTANT_REGID_NOTIFICATION = regId;

                    // Try to register again, but not in the UI thread.
                    // It's also necessary to cancel the thread onDestroy(),
                    // hence the use of AsyncTask instead of a raw thread.
                    final Context context = this;

                    mRegisterTask = new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {

                            // Register on our server
                            // On server creates a new user
                            //aController.register(context,name, regId);

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void result) {
                            mRegisterTask = null;
                            Log.i("regId", "Device registered: regId = " + regId);
                            Constants.CONSTANT_REGID_NOTIFICATION = regId;
//                            myHandler = new Handler();
//                            myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);

                        }

                    };
                    // execute AsyncTask
                    mRegisterTask.execute(null, null, null);
                }
            }
        } else {

            Constants.CONSTANT_REGID_NOTIFICATION = "";
//            myHandler = new Handler();
//            myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);
            //Constants.showDialog(SplashScreen.this);
        }

        firstname = (EditText) findViewById(R.id.firstname);
//        lastname = (EditText) findViewById(R.id.lastname);
        primarymobile = (EditText) findViewById(R.id.primarymobile);
//        secondarymobile = (EditText) findViewById(R.id.secondarymobile);
        email = (EditText) findViewById(R.id.email);
//        pincode = (EditText) findViewById(R.id.pincode);
//        address = (EditText) findViewById(R.id.address);
//        landmark = (EditText) findViewById(R.id.landmark);
//        country = (EditText) findViewById(R.id.country);
//        state = (EditText) findViewById(R.id.state);
//        city = (EditText) findViewById(R.id.city);
        business_registration_form_one_button_save = (Button) findViewById(R.id.business_registration_form_one_button_save);

        business_registration_form_one_button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{

//                    SharedPreferences.Editor editor = sharedPreferencesRemember.edit();
//                    editor.putString(Constants.sharedPreferenceUserId, "testing");
//                    editor.commit();
//
//                    progressDialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                    finish();
//                    Intent intent = new Intent(getApplicationContext(), Custom_CameraActivity.class);
//                    startActivity(intent);

                    if(validateForm()){

                        if(internetConnection.isNetworkAvailable(getApplicationContext())){
                            String firstnameStr,usernameStr,passwordStr, primarymobileStr, secondarymobileStr, emailStr, pincodeStr, addressStr,
                                    landmarkStr, countryStr, stateStr, cityStr;
                            firstnameStr = firstname.getText().toString();
                            //lastnameStr = lastname.getText().toString();
                            usernameStr =  email.getText().toString();
                            passwordStr = "password";
                            primarymobileStr = primarymobile.getText().toString();
//                            secondarymobileStr = secondarymobile.getText().toString();
                            emailStr = email.getText().toString();


                            jsonObject= new JSONObject();

                            jsonObject.put("firstName", firstnameStr);
                            jsonObject.put("lastName", "");
                            jsonObject.put("userName", usernameStr);
                            jsonObject.put("password", passwordStr);
                            jsonObject.put("primaryMobileNumber", primarymobileStr);
                            jsonObject.put("email", emailStr);
                            jsonObject.put("deviceRegistrationId", Constants.CONSTANT_REGID_NOTIFICATION);
                            jsonObject.put("enabled", 1);
                            System.out.println("saveData JSON: " + jsonObject);

                            saveData();
                        }else{
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                        }

                    }

                }catch (Exception e){
                    System.out.println("saveData: "+e);
                }
            }
        });

    }

    private boolean validateForm(){

        if (firstname.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), R.string.error_name_null, Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (lastname.getText().toString().trim().equalsIgnoreCase("")) {
//            Toast.makeText(getApplicationContext(), R.string.error_last_name_null, Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (primarymobile.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), R.string.error_primary_mobile_null, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), R.string.error_email_null, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public boolean saveData()
    {

        String logIn = "saveData";
        final String url = "http://45.34.14.119:7075/user/save";

        progressDialog = new ProgressDialog(Registration.this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Map<String, Object> postParam= new HashMap<String, Object>();
        postParam.put("data", jsonObject);
//        postParam.put("data", "{firstName:pushkar,lastName:tamhane,userName:pt,password:asdasd,primaryMobileNumber:9898989898,secondaryMobileNumber:9898989898,email:pushkar.tamhane@mobintia.in,pincode:411038,address:pune,landmark:kothrud,country:india,state:maharashtra,city:pune,deviceRegistrationId:123456789,enabled:1}");//username


        Log.d("saveData", postParam.toString());
        Log.d("saveData", url.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject)
            {
                Log.d("saveData", jsonObject.toString());

                try
                {
                    if(jsonObject.getString("status").equalsIgnoreCase("true"))
                    {
                        JSONObject jsonObjectUser = jsonObject.getJSONObject("user");
                        String id = jsonObjectUser.getString("id");

                        SharedPreferences.Editor editor = sharedPreferencesRemember.edit();
                        editor.putString(Constants.sharedPreferenceUserId, id);
                        editor.commit();

                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);


                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                Log.d("saveData", volleyError.toString());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };

        int socketTimeout = Constants.SocketTimeout;//60 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        Controller.getInstance().addToRequestQueue(request, logIn);

        return  false;
    }

    /*  Developer Name: Pushkar Tamhane.
        Description: Create broadcast receiver to get message and show on screen

     */

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);

            // Waking up mobile if it is sleeping
            aController.acquireWakeLock(getApplicationContext());

            Toast.makeText(getApplicationContext(), newMessage,
                    Toast.LENGTH_LONG).show();

            // Releasing wake lock
            aController.releaseWakeLock();
        }
    };

    @Override
    protected void onDestroy() {
        // Cancel AsyncTask
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            // Unregister Broadcast Receiver
            unregisterReceiver(mHandleMessageReceiver);

            //Clear internal resources.
            GCMRegistrar.onDestroy(this);

        } catch (Exception e) {
        }
        super.onDestroy();
    }
}
