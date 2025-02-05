package ir.ARtor.volley.app;

import android.content.Context;
import android.content.SharedPreferences;

public class Spref {

    private static SharedPreferences sharedPreferences;
    public static final String EMAIL = "email";
    public static final String CHECK_BOX = "checkBox";

    public static SharedPreferences getSharedPreferences(){
        if (sharedPreferences == null) {
            sharedPreferences = application.getContext().getSharedPreferences(app.TAG, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }


}
