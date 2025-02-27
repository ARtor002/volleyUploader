package ir.ARtor.volley.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;
import java.util.Locale;

public class application extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        setFont();
    }

    private void setFont() {
        String language = Locale.getDefault().getLanguage();

        String font;
        if (language.equalsIgnoreCase("English")){
            font = "font/pop.ttf";
        }else {
            font = "font/ir.ttf";
        }
        Typeface typeface = Typeface.createFromAsset(getAssets(),font);
        try {
            Field field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null, typeface);

        }catch (Exception e){

        }
    }


    public static Context getContext(){
        return context;
    }
}
