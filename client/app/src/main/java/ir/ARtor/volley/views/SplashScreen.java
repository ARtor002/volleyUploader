package ir.ARtor.volley.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ir.ARtor.volley.R;
import ir.ARtor.volley.app.Spref;
import ir.ARtor.volley.app.app;

public class SplashScreen extends AppCompatActivity {

    TextView textView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViews();
        init();
        onClick();
    }

    private void onClick() {
        textView.setOnClickListener(v -> {
            textView.setVisibility(View.GONE);
            init();
        });
    }

    private void findViews() {
        textView = findViewById(R.id.connectErrorTv);
        imageView = findViewById(R.id.imageSplash);
    }

    private void init(){
        new Handler().postDelayed(() -> {
            if (app.testConnectivity()){
                if (Spref.getSharedPreferences().getBoolean(Spref.CHECK_BOX, false)){
                    startActivity(new Intent(this, Main_Activity.class));
                    finish();
                }else {
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashScreen.this, imageView, "image");
                    startActivity(new Intent(this, Login_Activity.class), activityOptionsCompat.toBundle());
                    finish();
                }
            }else {
                textView.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }
}