package com.smartcity.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Pattern;

/**
 * Created by king on 2/10/15.
 */
public class Constants
{
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static Pattern Mobile = Pattern.compile("^[789]\\d{9}$");
    public static int SocketTimeout=60000;
    public static String status ="status";
    public static String message ="message";
    public static String streetAddress ="streetAddress";
    public static String city ="city";
    public static String latitude ="latitude";
    public static String longitude ="longitude";
    public static String state ="state";
    public static String country ="country";
    public static String zipCode ="zipCode";
    public static String user ="user";
    public static String fileUrl ="fileUrl";
    public static String comment ="comment";
    public static String address ="address";
    public static String file ="file";
    public static String userComplaintId ="userComplaintId";
    public static String status_0 ="0";
    public static String status_1="1";
    public static String email ="email";
    public static String pass ="pass";
    public static String name ="name";
    public static String Contact ="Contact";
    public static String UserStatus ="UserStatus";
    public static String Email="Email";
    public static String msg="msg";
    public static String user_Id="User Id";
    public static String ResponseCode="ResponseCode";
    public static String Data="Data";
    public static String ID="ID";
    public static String Name="Name";
    public static String Bussiness_id="Bussiness_id";
    public static String Bussines_Title="Bussines_Title";
    public static String Bussines_Link="Bussines_Link";
    public static String About_Bussines="About_Bussines";
    public static String Bussines_Email="Bussines_Email";
    public static String Bussines_Address="Bussines_Address";
    public static String Country="Country";
    public static String State="State";
    public static String City="City";
    public static String Area="Area";
    public static String Zipcode="Zipcode";
    public static String Latitude="Latitude";
    public static String Longitude="Longitude";
    public static String AddedBy="AddedBy";
    public static String Addeddate="Addeddate";
    public static String Comments="Comments";
    public static String Category="Category";
    public static String Sub_Category="Sub_Category";
    public static String Bussiness_Type="Bussiness_Type";
    public static String Bussines_Sub_type="Bussines_Sub_type";
    public static String Anythigelse="Anythigelse";
    public static String Opening_Day="Opening_Day";
    public static String LandlineNo="LandlineNo";
    public static String MobileNo="MobileNo";
    public static String AlternetMobile="AlternetMobile";
    public static String Website_Address="Website_Address";
    public static String Tag="Tag";
    public static String Payment_mode="Payment_mode";
    public static String Owner_Fullname="Owner_Fullname";
    public static String Role="Role";
    public static String EmailId="EmailId";
    public static String Owner_Discription="Owner_Discription";
    public static String image="image";
    public static String images="images";
    public static String Terms_Condition="Terms_Condition";
    public static String AddedDate="AddedDate";
    public static String Status="Status";
    public static String Comment="Comment";
    public static String imagepath="imagepath";
    public static String videopath="videopath";
    public static String addedBy="addedBy";
    public static String addeddate="addeddate";
    public static String flag="flag";
    public static String data="data";
    public static String Id="Id";
    public static String area="area";
    public static String id="id";
    public static String Bussines_Type="Bussines_Type";
    public static String Bussines_TypeWeb="Bussines Type";
    public static String info="info";
    public static String CONSTANT_TAG_POST="POST";
    public static String CONSTANT_REGID_NOTIFICATION= "";
    public static int notificationCount=0;
    public static String notificationStatus="notificationStatus";
    public static String CONSTANT_notification_count="notification_count";
    public static String notificationText="notificationText";
    public static String notificationDate="notificationDate";
    public static String notificationmessage="notificationmessage";
    public static String notificationdate="notificationdate";
    public static String link="link";
    public static String userComplaint="userComplaint";
    public static String complaintFiles="complaintFiles";
    public static String primaryMobileNumber="primaryMobileNumber";
    public static String createdDate="createdDate";
    public static String contactNumber="contactNumber";
    public static String date="date";
    public static String notification_message="notification_message";
    public static String notification_date="notification_date";
    public static String notification_subject="notification_subject";
    public static String notification_sortMessage="notification_sortMessage";
    public static String notification_complaintId="notification_complaintId";
    public static String notification_address="notification_address";
    public static String notification_contactNumber="notification_contactNumber";
    public static String constantComplaintURL="https://www.google.com";
    //Firebase app url
    public static final String FIREBASE_APP = "https://rahul-shewale.firebaseio.com";

    //Constant to store shared preferences
    public static final String SHARED_PREF = "notificationapp";

    //To store boolean in shared preferences for if the device is registered to not
    public static final String REGISTERED = "registered";

    //To store the firebase id in shared preferences
    public static final String UNIQUE_ID = "uniqueid";
    public static String regId="regId";
    public static String sharedPreferenceUserId="sharedPreferenceUserId";
    public static String sharedPreferenceComplaintId="sharedPreferenceComplaintId";


    public static void saveProfileImage(Bitmap finalBitmap, String fileName) {

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SmartCity/";
        File myDir = new File(root);
        myDir.mkdirs();

        String fname = fileName +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()){
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSavedImageString (String fileName) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SmartCity/";
        String pathName = root +fileName;
        return pathName;
    }

    public static String createAudioFolder(){
        createNowITestFolder();
        final String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SmartCity/audio";
        File folder = new File(PATH);
        if (folder.exists()) {
        }else {
            folder.mkdir();
        }
        return PATH;
    }

    public static String createVideoFolder(){
        createNowITestFolder();
        final String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SmartCity/video";
        File folder = new File(PATH);
        if (folder.exists()) {
        }else {
            folder.mkdir();
        }
        return PATH;
    }


    public static String createNowITestFolder(){

        final String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SmartCity";
        File folder = new File(PATH);
        if (folder.exists()) {
        }else {
            folder.mkdir();
        }
        return PATH;
    }

}
