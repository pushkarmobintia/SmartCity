package com.smartcity;
 
       public interface Config {

        static final boolean SECOND_SIMULATOR = false;

        // Google project id
        static final String GOOGLE_SENDER_ID = "561501488579";
     
        /**
         * Tag used on log messages.
         */
        static final String TAG = "GCM Android Example";
     
        // Broadcast reciever name to show gcm registration messages on screen 
        static final String DISPLAY_REGISTRATION_MESSAGE_ACTION =
                "com.smartcity.DISPLAY_REGISTRATION_MESSAGE";
         
        // Broadcast reciever name to show user messages on screen
        static final String DISPLAY_MESSAGE_ACTION =
            "com.smartcity.DISPLAY_MESSAGE";
     
        // Parse server message with this name
        static final String EXTRA_MESSAGE = "message";
         
         
    }