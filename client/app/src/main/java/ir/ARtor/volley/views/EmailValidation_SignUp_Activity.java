package ir.ARtor.volley.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.ARtor.volley.R;
import ir.ARtor.volley.app.Spref;
import ir.ARtor.volley.app.app;

public class EmailValidation_SignUp_Activity extends AppCompatActivity implements View.OnClickListener {

    String email;
    PinView pinView;
    Button RegisterBtn;
    ImageView backImg;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_validation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViews();
        onclick();
        init();
    }

    private void findViews(){
        pinView = findViewById(R.id.pinView);
        RegisterBtn = findViewById(R.id.confirmBtn);
        backImg = findViewById(R.id.imageView_back);
    }

    private void onclick(){
        RegisterBtn.setOnClickListener(this);
        backImg.setOnClickListener(this);
    }

    private void init(){
        requestQueue = Volley.newRequestQueue(this);
        app.successToast(getString(R.string.toastSentCode));
        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pinView.setLineColor(getColor(R.color.colorPrimary));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == RegisterBtn){
            try {
                String entryCode =  pinView.getText().toString();
                if (entryCode.equals(getIntent().getExtras().getString("code"))){
                    register();
                }else{
                    app.failedToast(getString(R.string.toastWrongCode));
                    pinView.setLineColor(getColor(R.color.colorRed));
                }

            }catch (NullPointerException e){
                app.failedToast(getString(R.string.toastWrongCode));
            }
        } else if (v == backImg) {
            finish();
        }
    }

    private void register() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.LOCAL2 + "register.php",response -> {
            app.l(response);
            try {
                email = getIntent().getExtras().getString("email");
                JSONObject jsonObject = new JSONObject(response);
                String message = jsonObject.getString("message");
                if (message.equals("okey")){
                    Spref.getSharedPreferences().edit().putString(Spref.EMAIL, email).apply();
                    startActivity(new Intent(EmailValidation_SignUp_Activity.this, Main_Activity.class));
                    finish();
                }else {
                    app.failedToast(getString(R.string.toastConnectFailed));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        },error -> {
            app.failedToast(getString(R.string.toastConnectFailed));
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", getIntent().getStringExtra("password"));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}