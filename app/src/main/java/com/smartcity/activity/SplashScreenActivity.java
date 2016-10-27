package com.smartcity.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.smartcity.R;
import com.smartcity.util.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    private Handler myHandler;
    private static int SPLASH_TIME_OUT = 2000;
    private final static int PERMISSION_REQUEST_CODE101 = 101;
    private final static int PERMISSION_REQUEST_CODE103 = 103;
    private final static int PERMISSION_REQUEST_CODE105 = 105;
    private final static int PERMISSION_REQUEST_CODE106 = 106;
    private final static int PERMISSION_REQUEST_CODE107 = 107;
    private final static int PERMISSION_REQUEST_CODE108 = 108;
    private boolean isPermissionGranted, lastPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (isDeviceBuildVersionMarshmallow()){
            if(lastPermission == false && isPermissionGranted == true){
                myHandler = new Handler();
                myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);
            }else {
                getSMSReadPermisson();
            }
        }else{
            myHandler = new Handler();
            myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);
        }
    }


    private boolean isDeviceBuildVersionMarshmallow(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return true;

        return false;
    }


    /*
        Runtime permission request for reading SMS
     */
    private void getSMSReadPermisson() {

        int permissionCheckExternalStorage = ContextCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckLocation1 = ContextCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckCamera = ContextCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.CAMERA);
        int permissionCheckAudio = ContextCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.RECORD_AUDIO);
        int permissionCheckCall = ContextCompat.checkSelfPermission(SplashScreenActivity.this, Manifest.permission.CALL_PHONE);

        if (permissionCheckExternalStorage != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                // explanation needed
                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE101);


            }else{
                // No explanation needed
                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE101);
            }
        }

        if (permissionCheckLocation1 != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)){

                // explanation needed
                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE105);


            }else{
                // No explanation needed
                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE105);
            }
        }

        if (permissionCheckCamera != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this, Manifest.permission.CAMERA)){

                // explanation needed
                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CODE106);


            }else{
                // No explanation needed
                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_REQUEST_CODE106);
            }
        }

        if (permissionCheckAudio != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this, Manifest.permission.RECORD_AUDIO)){

                // explanation needed
                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        PERMISSION_REQUEST_CODE107);


            }else{
                // No explanation needed
                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        PERMISSION_REQUEST_CODE107);
            }
        }

        if (permissionCheckAudio != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this, Manifest.permission.CALL_PHONE)){

                // explanation needed
                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        PERMISSION_REQUEST_CODE108);


            }else{
                // No explanation needed
                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        PERMISSION_REQUEST_CODE108);
            }
        }

        if(permissionCheckExternalStorage == 0 && permissionCheckLocation1 == 0 && permissionCheckCamera == 0 && permissionCheckAudio == 0 && permissionCheckCall == 0){
            isPermissionGranted = true;
            myHandler = new Handler();
            myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode){

            case PERMISSION_REQUEST_CODE101:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    isPermissionGranted = true;
                    lastPermission = false;
                }else {

                    //Toast.makeText(SplashScreenActivity.this,"read external storage permission denied",Toast.LENGTH_SHORT).show();
                    lastPermission = false;
                }
                break;


            case PERMISSION_REQUEST_CODE105:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    isPermissionGranted = true;
                    lastPermission = false;

                }else {

                    //Toast.makeText(SplashScreenActivity.this,"permission denied",Toast.LENGTH_SHORT).show();
                    lastPermission = false;
                }

            case PERMISSION_REQUEST_CODE106:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    isPermissionGranted = true;
                    lastPermission = false;

                }else {

                    //Toast.makeText(SplashScreenActivity.this,"permission denied",Toast.LENGTH_SHORT).show();
                    lastPermission = false;
                }

            case PERMISSION_REQUEST_CODE107:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    isPermissionGranted = true;
                    lastPermission = false;

                }else {

                    //Toast.makeText(SplashScreenActivity.this,"permission denied",Toast.LENGTH_SHORT).show();
                    lastPermission = false;
                }

            case PERMISSION_REQUEST_CODE108:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    isPermissionGranted = true;
                    lastPermission = true;
                    myHandler = new Handler();
                    myHandler.postDelayed(myRunnable, SPLASH_TIME_OUT);

                }else {

                    //Toast.makeText(SplashScreenActivity.this,"permission denied",Toast.LENGTH_SHORT).show();
                    lastPermission = false;
                }
                break;
        }
    }

    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {


//            finish();
//            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//            startActivity(intent);

            final SharedPreferences sharedPreferencesRemember = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String sharedPreferenceUserId = sharedPreferencesRemember.getString(Constants.sharedPreferenceUserId, "");

            if(sharedPreferenceUserId == null || sharedPreferenceUserId.equalsIgnoreCase("")){
                finish();
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
            }else {
                finish();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }

        }
    };

    protected void onStart() {
        super.onStart();


    }

    protected void onStop() {
        super.onStop();
        myHandler.removeCallbacks(myRunnable);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (myHandler==null)
            {

            }
            else {
                myHandler.removeCallbacks(myRunnable);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}