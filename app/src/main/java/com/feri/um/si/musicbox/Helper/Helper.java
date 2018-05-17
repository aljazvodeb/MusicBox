package com.feri.um.si.musicbox.Helper;

import android.content.Context;
import android.widget.Toast;

    public class Helper {

        public static final String NAME = "Name";

        public static final String EMAIL = "Email";

        public static final int SELECT_PICTURE = 200000;

        public static boolean isValidEmail(String email){
            if(email.contains("@")){
                return true;
            }
            return false;
        }

        public static void displayMessageToast(Context context, String displayMessage){
            Toast.makeText(context, displayMessage, Toast.LENGTH_LONG).show();
        }
    }

