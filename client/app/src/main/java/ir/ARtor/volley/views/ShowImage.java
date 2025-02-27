package ir.ARtor.volley.views;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.ARtor.volley.R;
import ir.ARtor.volley.adapters.Images_adapter;
import ir.ARtor.volley.app.app;
import ir.ARtor.volley.models.Image_Model;

public class ShowImage extends AppCompatActivity {

    ImageView backImg, imageView, moreImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backImg = findViewById(R.id.backImg);
        moreImg = findViewById(R.id.moreImg);
        imageView = findViewById(R.id.imageView);
        Glide.with(this).load(app.LOCAL2 + getIntent().getExtras().getString("image")).into(imageView);

        backImg.setOnClickListener(v -> {
            supportFinishAfterTransition();
        });

        moreImg.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, v);
            popupMenu.getMenuInflater().inflate(R.menu.menu_showimage, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.deleteImage){
                    deleteImage();
                    return true;
                }else if (id == R.id.downloadImage){

                }
                return false;
            });
            popupMenu.show();
        });

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                supportFinishAfterTransition();
            }
        });
    }

    private void deleteImage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.LOCAL2 + "delete_Images.php",response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("message").equals("delete_okey")){
                    finish();
                }else {
                    app.failedToast(getString(R.string.toastConnectFailed));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> app.failedToast(getString(R.string.toastConnectFailed))){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ids", getIntent().getStringExtra("id"));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}