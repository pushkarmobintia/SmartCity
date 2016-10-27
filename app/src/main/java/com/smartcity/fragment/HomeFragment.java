package com.smartcity.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.smartcity.activity.CameraPreview;
import com.smartcity.databasehelper.DatabaseHelper;
import com.smartcity.model.ComplaintItem;
import com.smartcity.network.GPSTracker;
import com.smartcity.network.InternetConnection;
import com.smartcity.network.JSONHandler;
import com.smartcity.ui.ViewProxy;
import com.smartcity.util.Constants;
import com.smartcity.util.FileUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by mobintia on 20/10/16.
 */
public class HomeFragment extends Fragment implements LocationListener, SurfaceHolder.Callback{

    private Camera mCamera;
    private CameraPreview mCameraPreview;
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
    FrameLayout preview;
    int PICKFILE_RESULT_CODE = 1;
    private static final String TAG = "FileChooserExampleActivity";
    private DatabaseHelper databaseHelper;
    private Button button_video;
    private static final int REQUEST_CODE = 6384; // onActivityResult request

    private TextView recordTimeText;
    private ImageButton audioSendButton;
    private View recordPanel;
    private float startedDraggingX = -1;
    private float distCanMove = dp(80);
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Timer timer;
    private MediaRecorder myAudioRecorder;
    private Button activity_add_issue_button_play, activity_add_issue_button_send_audio;
    private RelativeLayout audioview;
    private MediaRecorder recorder;
    private SurfaceHolder surfaceHolder;
    private CamcorderProfile camcorderProfile;
    private Camera camera;
    boolean recording = false;
    boolean usecamera = true;
    boolean previewRunning = false;
    SurfaceView surfaceView;
    Button btnStart, btnStop;
    File root;
    File file;
    Boolean isSDPresent;
    SimpleDateFormat simpleDateFormat;
    String timeStamp;
    View rootViewMain;
    private ImageView fragment_home_imageview_camera, fragment_home_imageview_gallery, fragment_home_imageview_audio, fragment_home_imageview_video;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        rootViewMain = rootView;
        findViewByIds(rootView);

        databaseHelper = new DatabaseHelper(getActivity());

        gps = new GPSTracker(getActivity());


        sharedPreferencesRemember = PreferenceManager.getDefaultSharedPreferences(getActivity());

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


        fragment_home_imageview_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (preview.getVisibility() == View.GONE) {
                    audioview.setVisibility(View.GONE);
                    preview.setVisibility(View.VISIBLE);
                } else {
                    audioview.setVisibility(View.GONE);
                    mCamera.takePicture(null, null, mPicture);

//                mCamera.stopPreview();
//                mCamera.startPreview();

                    if (gps.canGetLocation()) {
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        try {
                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getActivity(), Locale.getDefault());

                            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                            streetAddress = addresses.get(0).getSubLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            city = addresses.get(0).getLocality();
                            state = addresses.get(0).getAdminArea();
                            country = addresses.get(0).getCountryName();
                            pincode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                        } catch (Exception e) {

                        }

                    } else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
//                        gps.showSettingsAlert();
                    }

                    System.out.println("Latitude: " + latitude + " Longitude: " + longitude + " streetAddress: " + streetAddress + " Country: " + country + " State: " + state
                            + " city: " + city + " Pincode: " + pincode);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage(getResources().getString(R.string.are_you_sure_you_want_upload));

                    builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            if (internetConnection.isNetworkAvailable(getActivity())) {

                                String sharedPreferenceComplaintId = sharedPreferencesRemember.getString(Constants.sharedPreferenceComplaintId, "");

                                if (sharedPreferenceComplaintId == null || sharedPreferenceComplaintId.equalsIgnoreCase("")) {
                                    saveFileData("0");
                                } else {
                                    photoId = sharedPreferenceComplaintId;
                                    new JsonReadTaskPostQuestionImage().execute();
                                }

                            } else {
                                Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                            }

                        }

                    });

                    builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();
                            mCamera.stopPreview();
                            mCamera.startPreview();
                        }
                    });

                    AlertDialog alert = builder.create();
                    if (alert.isShowing()) {

                    } else {
                        alert.show();
                    }
                }
            }
        });

        fragment_home_imageview_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioview.setVisibility(View.GONE);
                preview.setVisibility(View.VISIBLE);
                showChooser();
            }
        });

        fragment_home_imageview_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity_add_issue_button_play.setEnabled(false);
                activity_add_issue_button_send_audio.setEnabled(false);
                recordTimeText.setText("00:00");
                audioview.setVisibility(View.VISIBLE);
                preview.setVisibility(View.GONE);

            }
        });

        audioSendButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    startedDraggingX = -1;
                    // startRecording();

                    DateFormat df = new SimpleDateFormat("ddMMyyyy_HHmmss");
                    String currentDateTimeString = df.format(Calendar.getInstance().getTime());

                    outputFile = Constants.createAudioFolder() + "/" + currentDateTimeString + "recording.3gp";

                    myAudioRecorder = new MediaRecorder();
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    myAudioRecorder.setOutputFile(outputFile);

                    recordTimeText.setText("00:00");

                    startrecord();
                    audioSendButton.getParent()
                            .requestDisallowInterceptTouchEvent(true);
                    recordPanel.setVisibility(View.VISIBLE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                        || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    startedDraggingX = -1;
                    stoprecord();
                    // stopRecording(true);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    float x = motionEvent.getX();
                    if (x < -distCanMove) {
                        stoprecord();
                        // stopRecording(false);
                    }
                    x = x + ViewProxy.getX(audioSendButton);

                    if (startedDraggingX != -1) {
                        float dist = (x - startedDraggingX);
                        float alpha = 1.0f + dist / distCanMove;
                        if (alpha > 1) {
                            alpha = 1;
                        } else if (alpha < 0) {
                            alpha = 0;
                        }

                    }

                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });

        activity_add_issue_button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (outputFile == null || outputFile.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please record audio first", Toast.LENGTH_LONG).show();
                } else {
                    MediaPlayer m = new MediaPlayer();

                    try {
                        m.setDataSource(outputFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        m.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    m.start();
                }
            }
        });

        activity_add_issue_button_send_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    try {
                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());

                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                        streetAddress = addresses.get(0).getSubLocality(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        city = addresses.get(0).getLocality();
                        state = addresses.get(0).getAdminArea();
                        country = addresses.get(0).getCountryName();
                        pincode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                    } catch (Exception e) {

                    }

                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
//                    gps.showSettingsAlert();
                }

                System.out.println("Latitude: " + latitude + " Longitude: " + longitude + " streetAddress: " + streetAddress + " Country: " + country + " State: " + state
                        + " city: " + city + " Pincode: " + pincode);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage(getResources().getString(R.string.are_you_sure_you_want_upload_audio));

                builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        if (internetConnection.isNetworkAvailable(getActivity())) {
                            saveFileData("1");
                        } else {
                            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                        }

                    }

                });

                builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                        mCamera.stopPreview();
                        mCamera.startPreview();
                    }
                });

                AlertDialog alert = builder.create();
                if (alert.isShowing()) {

                } else {
                    alert.show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(getActivity(), mCamera);

        preview.addView(mCameraPreview);
    }

    private void findViewByIds(View rootView) {

        fragment_home_imageview_camera = (ImageView) rootView.findViewById(R.id.fragment_home_imageview_camera);
        fragment_home_imageview_gallery = (ImageView) rootView.findViewById(R.id.fragment_home_imageview_gallery);
        fragment_home_imageview_audio = (ImageView) rootView.findViewById(R.id.fragment_home_imageview_audio);
        fragment_home_imageview_video = (ImageView) rootView.findViewById(R.id.fragment_home_imageview_video);
        preview = (FrameLayout) rootView.findViewById(R.id.camera_preview);
        activity_add_issue_button_play = (Button) rootView.findViewById(R.id.activity_add_issue_button_play);
        button_video = (Button) rootView.findViewById(R.id.button_video);
        activity_add_issue_button_send_audio = (Button) rootView.findViewById(R.id.activity_add_issue_button_send_audio);
        activity_add_issue_button_play.setEnabled(false);
        activity_add_issue_button_send_audio.setEnabled(false);
        recordPanel = rootView.findViewById(R.id.record_panel);
        recordTimeText = (TextView) rootView.findViewById(R.id.recording_time_text);
        audioSendButton = (ImageButton) rootView.findViewById(R.id.chat_audio_send_button);
        audioview = (RelativeLayout) rootView.findViewById(R.id.audioview);
    }


    private void prepareRecorder() {
        recorder = new MediaRecorder();
        recorder.setPreviewDisplay(surfaceHolder.getSurface());
        if (usecamera) {
            camera.unlock();
            recorder.setCamera(camera);
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        recorder.setProfile(camcorderProfile);

        if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.MPEG_4) {
            recorder.setOutputFile("/sdcard/XYZApp/" + "XYZAppVideo" + ""
                    + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())
                    + ".mp4");
        } else if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.MPEG_4) {
            recorder.setOutputFile("/sdcard/XYZApp/" + "XYZAppVideo" + ""
                    + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())
                    + ".mp4");
        } else {
            recorder.setOutputFile("/sdcard/XYZApp/" + "XYZAppVideo" + ""
                    + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())
                    + ".mp4");
        }

        try {
            recorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("onsurfacecreated");

        if (usecamera) {
            camera = Camera.open();

            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
                previewRunning = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        System.out.println("onsurface changed");

        if (!recording && usecamera) {
            if (previewRunning) {
                camera.stopPreview();
            }

            try {
                Camera.Parameters p = camera.getParameters();

                p.setPreviewSize(camcorderProfile.videoFrameWidth,
                        camcorderProfile.videoFrameHeight);
                p.setPreviewFrameRate(camcorderProfile.videoFrameRate);

                camera.setParameters(p);

                camera.setPreviewDisplay(holder);
                camera.startPreview();
                previewRunning = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            prepareRecorder();
            if (!recording) {
                recording = true;
                recorder.start();
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (recording) {
            recorder.stop();
            recording = false;
        }
        recorder.release();
        if (usecamera) {
            previewRunning = false;
            // camera.lock();
            camera.release();
        }

    }


    private void startrecord() {
        // TODO Auto-generated method stub
        startTime = SystemClock.uptimeMillis();
        timer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 1000);
        vibrate();
        try {
            myAudioRecorder.prepare();
            myAudioRecorder.start();
        }

        catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            //System.out.println("Audio exception: " + e);
        }

        catch (IOException e) {
            // TODO Auto-generated catch block
            //System.out.println("Audio exception: " + e);
        }

//        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
    }

    private void stoprecord() {
        // TODO Auto-generated method stub

//        recordTimeText.setText("00:00");
        if (timer != null) {
            timer.cancel();
        }
        if (recordTimeText.getText().toString().equals("00:00")) {
            return;
        }

        vibrate();
        try {
            myAudioRecorder.stop();
            myAudioRecorder.release();
            myAudioRecorder = null;
        }catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            //System.out.println("Audio exception: " + e);
        }

        Toast.makeText(getActivity(), "Audio recorded successfully", Toast.LENGTH_LONG).show();

        activity_add_issue_button_play.setEnabled(true);
        activity_add_issue_button_send_audio.setEnabled(true);
    }

    private void vibrate() {
        // TODO Auto-generated method stub
        try {
            Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showChooser() {
        // Use the GET_CONTENT intent from the utility class
        Intent target = FileUtils.createGetContentIntent();
        // Create the chooser Intent
        Intent intent = Intent.createChooser(
                target, getString(R.string.chooser_title));
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            // The reason for the existence of aFileChooser
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == getActivity().RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        System.out.println("FileSelectorTestActivity: " + uri.toString());

                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(getActivity(), uri);

                            System.out.println("FileSelectorTestActivity: " + path);

                            outputFile = path;

                            if(gps.canGetLocation()){
                                double latitude = gps.getLatitude();
                                double longitude = gps.getLongitude();

                                try {
                                    Geocoder geocoder;
                                    List<Address> addresses;
                                    geocoder = new Geocoder(getActivity(), Locale.getDefault());

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

                            System.out.println("Latitude: " + latitude + " Longitude: " + longitude + " streetAddress: " + streetAddress + " Country: " + country + " State: " + state
                                    + " city: " + city + " Pincode: " + pincode);

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                            builder.setMessage(getResources().getString(R.string.are_you_sure_you_want_upload));

                            builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    if (internetConnection.isNetworkAvailable(getActivity())) {
                                        saveFileData("1");
                                    } else {
                                        Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                                    }

                                }

                            });

                            builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing
                                    dialog.dismiss();
                                    mCamera.stopPreview();
                                    mCamera.startPreview();
                                }
                            });

                            AlertDialog alert = builder.create();
                            if (alert.isShowing()) {

                            } else {
                                alert.show();
                            }
                        } catch (Exception e) {
                            System.out.println("FileSelectorTestActivity: " + e);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    public static int dp(float value) {
        return (int) Math.ceil(1 * value);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            final String hms = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(updatedTime)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(updatedTime)),
                    TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(updatedTime)));
            long lastsec = TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                    .toMinutes(updatedTime));
            System.out.println(lastsec + " hms " + hms);
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (recordTimeText != null)
                            recordTimeText.setText(hms);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });
        }
    }


    /**
     * Helper method to access the camera returns null if it cannot get the
     * camera or does not exist
     *
     * @return
     */
    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    };

    private File getOutputMediaFile() {

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SmartCity/";

        File mediaStorageDir = new File(dir);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        outputFile = Constants.getSavedImageString("IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }


    public boolean saveFileData(final String flag)
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
            final String url = "http://45.34.14.119:7075/usercomplaint/save";

            progressDialog = new ProgressDialog(getActivity());
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

                            if(flag.equalsIgnoreCase("0")){
                                SharedPreferences.Editor editor = sharedPreferencesRemember.edit();
                                editor.putString(Constants.sharedPreferenceComplaintId, photoId);
                                editor.commit();
                            }
                            progressDialog.dismiss();
                            mCamera.stopPreview();
                            mCamera.startPreview();
                            new JsonReadTaskPostQuestionImage().execute();
//                            savePhoto();
                        }else{
                            mCamera.stopPreview();
                            mCamera.startPreview();
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        mCamera.stopPreview();
                        mCamera.startPreview();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError)
                {
                    mCamera.stopPreview();
                    mCamera.startPreview();
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
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
            mCamera.stopPreview();
            mCamera.startPreview();
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
            progressDialog = new ProgressDialog(getActivity());
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
            nameValuePairs.add(new BasicNameValuePair(Constants.userComplaintId, photoId));

            String url = "http://45.34.14.119:7075/usercomplaint/addNewComplaintFiles/userComplaint";

            System.out.println("jsonPostImage: " + url);
            System.out.println("jsonPostImage: " + nameValuePairs);


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
                mCamera.stopPreview();
                mCamera.startPreview();
                progressDialog.dismiss();
                Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
            }
            else{
                System.out.println("jsonPostImage: " + jsonPostImage);
                JSONObject response = new JSONObject(jsonPostImage);

                if (response.getString(Constants.status).equalsIgnoreCase("true"))
                {
                    JSONObject jsonObjectUserComplaint = response.getJSONObject(Constants.userComplaint);
                    String complaintId = jsonObjectUserComplaint.getString(Constants.id);
                    String createdDate = jsonObjectUserComplaint.getString(Constants.createdDate);

                    JSONArray jsonArrayUserFiles = jsonObjectUserComplaint.getJSONArray(Constants.complaintFiles);

                    JSONObject userObject = jsonObjectUserComplaint.getJSONObject(Constants.user);
                    String primaryMobileNumber = userObject.getString(Constants.primaryMobileNumber);

                    JSONObject addressJsonObject = jsonObjectUserComplaint.getJSONObject(Constants.address);

                    String streetAddress = addressJsonObject.getString(Constants.streetAddress);
                    String city = addressJsonObject.getString(Constants.city);

                    JSONObject jsonObjectComplaintFiles = jsonArrayUserFiles.getJSONObject(0);
                    String fileUrl = jsonObjectComplaintFiles.getString(Constants.fileUrl);

                    long millisecond = Long.parseLong(createdDate);

                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(Long.parseLong(createdDate));
                    Date d = (Date) c.getTime();
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    String time = format.format(d);


                    System.out.println("Formatted Date: " + time);

                    databaseHelper.addInToUSerComplaints(new ComplaintItem(complaintId, streetAddress+", "+city, fileUrl, primaryMobileNumber, time));

                    mCamera.stopPreview();
                    mCamera.startPreview();
                    String successMessage = getResources().getString(R.string.upload_success)+" Your Complaint Id is "+ photoId;
                    Toast.makeText(getActivity(), successMessage, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), getResources().getString(R.string.upload_failed), Toast.LENGTH_SHORT).show();
                    mCamera.stopPreview();
                    mCamera.startPreview();
                }

            }
        } catch (JSONException e) {
            mCamera.stopPreview();
            mCamera.startPreview();
            System.out.println("jsonPostImage: " + e);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
