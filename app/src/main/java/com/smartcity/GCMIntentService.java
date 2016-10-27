package com.smartcity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.smartcity.activity.NotificationActivity;
import com.smartcity.databasehelper.DatabaseHelper;
import com.smartcity.model.NotificationsItem;
import com.smartcity.util.Constants;

//import com.androidexample.mobilegcm.R;
    //import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";
    DatabaseHelper db;

    private Controller aController = null;
    SharedPreferences sharedPreferencesRemember;

    public GCMIntentService() {
        // Call extended class Constructor GCMBaseIntentService
        super(Config.GOOGLE_SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    protected void onRegistered(Context context, String registrationId) {
        if(aController == null)
           aController = (Controller) getApplicationContext();

        Log.i(TAG, "---------- onRegistered -------------");
        Log.i(TAG, "Device registered: regId = " + registrationId);

        Constants.CONSTANT_REGID_NOTIFICATION = registrationId;

        //sendIdToServer(registrationId);
    }

    /**
     * Method called on device unregistred
     * */
    protected void onUnregistered(Context context, String registrationId) {

        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "---------- onUnregistered -------------");
        Log.i(TAG, "Device unregistered");

        aController.displayRegistrationMessageOnScreen(context,
                getString(R.string.gcm_unregistered));
    }

    /**
     * Method called on Receiving a new message from GCM server
     * */
    protected void onMessage(Context context, Intent intent) {

        Constants.notificationCount ++;

        sharedPreferencesRemember = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferencesRemember.edit();
        editor.putString(Constants.CONSTANT_notification_count, Constants.notificationCount+"");
        editor.commit();


        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "---------- Intent -------------" + intent.getDataString());
        Log.i(TAG, "---------- onMessage -------------");
        String actual_message = intent.getExtras().getString("notification_message");
        String notification_date = intent.getExtras().getString("notification_date");
        //String regId = intent.getExtras().getString("registration_ids");

        Log.i("GCM", "message : " + actual_message);


        db = new DatabaseHelper(getApplicationContext());
//        Calendar c = Calendar.getInstance();
//        System.out.println("Current time => " + c.getTime());

//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy,hh:MM");
//        String formattedDate = df.format(c.getTime());

        NotificationsItem notificationItem = new NotificationsItem("" ,"",actual_message,notification_date);

        long tag1_id = db.addInToNotification(notificationItem);

        Log.d("hi_ap_tag",tag1_id+"");
//        Log.d("hi_ap_date",formattedDate+"");
        Log.d("hi_ap_noti_status", "call");

        generateNotification(context, actual_message);
//        db = new DatabaseHelper(getApplicationContext());

//        int pos = actual_message.indexOf(":");
//        int last = actual_message.lastIndexOf(":");
//        String id = actual_message.substring(pos + 1, last);
//        String s = actual_message.substring(0,pos);
//        s = s + actual_message.substring(last+1,actual_message.length());
//
//        Log.d("hi_apidinGSM", id);
//        Log.d("hi_apsinGSM", s);
//
//        NotificationItem notificationItem = new NotificationItem("",id ,s);
//
//        long tag1_id = db.addInTo(notificationItem);
//
//        Log.d("app", tag1_id + "");
//
//
//         // generate notification to notify user
//        genereteNotificationBreakdown(context, s,Constants.flag1,id);
    }

//        protected void onMessage(Context context, JSONObject object)
//        {
//            try {
//                String message =object.getString("message");
//                Log.d("hi",message);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }

    /**
     * Method called on receiving a deleted message
     * */
    protected void onDeletedMessages(Context context, int total) {

        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "---------- onDeletedMessages -------------");
        String message = getString(R.string.gcm_deleted, total);

        String title = "DELETED";
        // aController.displayMessageOnScreen(context, message);
        // notifies user
        //generateNotification(context,title, message,"");
    }

    /**
     * Method called on Error
     * */
    public void onError(Context context, String errorId) {

        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "---------- onError -------------");
        Log.i(TAG, "Received error: " + errorId);
        aController.displayRegistrationMessageOnScreen(context, getString(R.string.gcm_error, errorId));
    }

    protected boolean onRecoverableError(Context context, String errorId) {

        if(aController == null)
            aController = (Controller) getApplicationContext();

        Log.i(TAG, "---------- onRecoverableError -------------");

        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        aController.displayRegistrationMessageOnScreen(context,
                getString(R.string.gcm_recoverable_error,
                errorId));

        return super.onRecoverableError(context, errorId);
    }


    /**
     * Create a notification to inform the user that server has sent a message.
     */
    private void generateNotification(Context context, String message) {

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {
            int icon = R.mipmap.ic_launcher;
            long when = System.currentTimeMillis();


            Notification notification = new Notification(icon, message, when);

            Intent notificationIntent = new Intent(context, NotificationActivity.class);
            // set intent so it does not start a new activity
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent , 0);
//            notification.setLatestEventInfo(context, context.getString(R.string.app_name), message, contentIntent);
            // Play default notification sound
            notification.defaults |= Notification.DEFAULT_SOUND;


            // Vibrate if vibrate is enabled
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notificationManager.notify(0, notification);
        }else{

            Intent notificationIntent = new Intent(context, NotificationActivity.class);
            // set intent so it does not start a new activity
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

            Notification noti = new Notification.Builder(context)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .build();

            notificationManager.notify(0, noti);
        }

    }
}