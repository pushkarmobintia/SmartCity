package com.smartcity.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.smartcity.Controller;
import com.smartcity.R;
import com.smartcity.network.GPSTracker;
import com.smartcity.network.InternetConnection;
import com.smartcity.network.JSONHandler;
import com.smartcity.util.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by mobintia on 27/9/16.
 */
public class CameraActivity extends AppCompatActivity implements LocationListener {

    int TAKE_PHOTO_CODE = 0;
    private ProgressDialog progressDialog;
    private String outputFile = null, photoId = "", jsonPostImage = "";
    File newfile;
    private JSONObject jsonObjectUser = new JSONObject();
    private JSONObject jsonObjectAddress = new JSONObject();
    private JSONObject jsonObjectData = new JSONObject();
    SharedPreferences sharedPreferencesRemember;
    private InternetConnection internetConnection = new InternetConnection();
    private String latitude, longitude, streetAddress, city, state, country, pincode;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gps = new GPSTracker(CameraActivity.this);


        sharedPreferencesRemember = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // check if GPS enabled
        if(gps.canGetLocation()){
            double lat = gps.getLatitude();
            double lng = gps.getLongitude();

            latitude = String.valueOf(lat);
            longitude = String.valueOf(lng);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SmartCity/";
        File newdir = new File(dir);

        if(newdir.exists()){
        }else {
            newdir.mkdirs();
        }

        DateFormat df = new SimpleDateFormat("ddMMyyyy_HHmmss");
        String currentDateTimeString = df.format(Calendar.getInstance().getTime());

        String file = dir+currentDateTimeString+".jpg";
        newfile = new File(file);
        try {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
        }


        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
        try
        {
            intent.putExtra( "return-data", true );
            startActivityForResult(intent, TAKE_PHOTO_CODE);

        } catch( ActivityNotFoundException e )
        {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");

            toSetAnImageFromCamera(data);
            Log.d("Location details", "Pic saved");


            if(gps.canGetLocation()){
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                try {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(this, Locale.getDefault());

                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    streetAddress = addresses.get(0).getSubLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    country = addresses.get(0).getCountryName();
                    pincode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                }catch (Exception e){

                }

            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }

            System.out.println("Latitude: " + latitude + " Longitude: " + longitude + " streetAddress: "+streetAddress+" Country: "+country+" State: "+state
            +" city: "+city+" Pincode: "+pincode);

            if(internetConnection.isNetworkAvailable(getApplicationContext())){
                saveFileData();
            }else{
                Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void toSetAnImageFromCamera( Intent data )
    {
        Bundle extras = data.getExtras();
        if( extras != null )
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            Constants.saveProfileImage(photo, "myprofilepicture");
            outputFile = Constants.getSavedImageString("myprofilepicture");
//            mSelectedImagePath = Constants.compressImage(getApplicationContext(), mSelectedImagePath);


        }
    }


    public boolean saveFileData()
    {

        try{

            String userId = sharedPreferencesRemember.getString(Constants.sharedPreferenceUserId,"");

            jsonObjectUser.put(Constants.id, userId);

            jsonObjectAddress.put(Constants.streetAddress, streetAddress);
            jsonObjectAddress.put(Constants.city, city);
            jsonObjectAddress.put(Constants.latitude, latitude);
            jsonObjectAddress.put(Constants.longitude, longitude);
            jsonObjectAddress.put(Constants.state, state);
            jsonObjectAddress.put(Constants.country, country);
            jsonObjectAddress.put(Constants.zipCode, pincode);

            jsonObjectData.put(Constants.user, jsonObjectUser);
            jsonObjectData.put(Constants.address, jsonObjectAddress);
            jsonObjectData.put(Constants.fileUrl, "null");
            jsonObjectData.put(Constants.status, "PENDING");
            jsonObjectData.put(Constants.comment, "My comment");

            String logIn = "saveFileData";
            final String url = "http://45.34.14.119:7075/usercomplaint/saveFileData";

            progressDialog = new ProgressDialog(CameraActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.please_wait));
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            System.out.println("saveFileData: "+jsonObjectData.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObjectData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject)
                {
                    Log.d("saveFileData", jsonObject.toString());

                    try
                    {
                        if(jsonObject.getString("status").equalsIgnoreCase("true"))
                        {
                            JSONObject jsonObjectUser = jsonObject.getJSONObject("userComplaint");
                            photoId = jsonObjectUser.getString("id");

                            progressDialog.dismiss();

                            new JsonReadTaskPostQuestionImage().execute();
//                            savePhoto();
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
                    Log.d("saveFileData", volleyError.toString());
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

        }catch(Exception e){
            System.out.println("saveFileData: "+e);
        }


        return  false;
    }

    @Override
    public void onLocationChanged(Location location) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();
        latitude = String.valueOf(lat);
        longitude = String.valueOf(lng);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class JsonReadTaskPostQuestionImage extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(CameraActivity.this);
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params)
        {
            final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair(Constants.file, outputFile));
            nameValuePairs.add(new BasicNameValuePair(Constants.id, photoId));

            System.out.println("jsonPostImage: " + nameValuePairs);

            String url = "http://45.34.14.119:7075/usercomplaint/savePhoto";
            JSONHandler jsonHandler = new JSONHandler();
            jsonPostImage = jsonHandler.makeServiceCallUserProfilePictureUpdate(url, nameValuePairs);

            System.out.println("jsonPostImage: "+jsonPostImage);
            System.out.println("jsonPostImage: "+url);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            parseJSONObjectPostQuestionImage();
        }
    }// end async task

    // build hash set for list view
    public void parseJSONObjectPostQuestionImage() {

        try {
            if(jsonPostImage == null || jsonPostImage.equalsIgnoreCase("null") || jsonPostImage.equalsIgnoreCase("")){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            }
            else{
                System.out.println("jsonPostImage: " + jsonPostImage);
                JSONObject response = new JSONObject(jsonPostImage);

                if (response.getString(Constants.status).equalsIgnoreCase("true"))
                {
                    Toast.makeText(getApplicationContext(), response.getString(Constants.message).toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), response.getString(Constants.message).toString(), Toast.LENGTH_SHORT).show();
                }

            }
        } catch (JSONException e) {

            System.out.println("jsonPostImage: " + e);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
