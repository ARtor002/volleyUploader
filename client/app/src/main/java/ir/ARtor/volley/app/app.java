package ir.ARtor.volley.app;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ir.ARtor.volley.R;


public class app {

    public static final String TAG = "volley";
    public static final String BASE_URL = "http://artor.is-great.net/";
    public static final String LOCAL = "http://192.168.43.114:81/volley/";
    public static final String LOCAL2 = "http://192.168.42.59:81/volley/";




    public static void l(String message){
        Log.e(TAG, message);
    }

    public static void t(String message){
        Toast.makeText(application.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void successToast(String message){
        Toast toast = new Toast(application.getContext());
        View view = LayoutInflater.from(application.getContext()).inflate(R.layout.success_custom_toast, null);

        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);

        imageView.setImageResource(R.drawable.round_check_24);
        textView.setText(message);

        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.show();
    }

    public static void failedToast(String message){
        Toast toast = new Toast(application.getContext());
        View view = LayoutInflater.from(application.getContext()).inflate(R.layout.failed_custom_toast, null);

        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);

        imageView.setImageResource(R.drawable.round_close_24);
        textView.setText(message);

        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.show();
    }

}
