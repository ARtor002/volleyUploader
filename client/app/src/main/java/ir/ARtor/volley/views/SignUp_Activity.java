package ir.ARtor.volley.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ir.ARtor.volley.R;
import ir.ARtor.volley.app.app;

public class SignUp_Activity extends AppCompatActivity implements View.OnClickListener {

    EditText emailEdt, passwordEdt;
    Button signUpBtn;
    TextView signInTv, errorTv;
    RequestQueue requestQueue;
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
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
        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        signInTv = findViewById(R.id.signInTv);
        signUpBtn = findViewById(R.id.signUpBtn);
        errorTv = findViewById(R.id.emailErrorTv);
    }

    private void onclick(){
        signUpBtn.setOnClickListener(this);
        signInTv.setOnClickListener(this);
    }

    private void init(){
        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onClick(View v) {
        if (v == signUpBtn){
            errorTv.setVisibility(View.GONE);
            String email = emailEdt.getText().toString().toLowerCase().trim();
            String password = passwordEdt.getText().toString().trim();

            if (!email.matches(Patterns.EMAIL_ADDRESS.pattern()) || !password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_=+-.{}]).{8,}")) {
                errorTv.setVisibility(View.VISIBLE);
            } else {
                errorTv.setVisibility(View.GONE);
                signUp(email, password);
            }

        } else if (v == signInTv) {
            finish();
//            getOnBackPressedDispatcher();
        }
    }

    private void signUp(String email, String password) {
        do{
            code = new Random().nextInt(1000000);
        }while (code < 100000);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.LOCAL2 + "check_email.php",response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String message = jsonObject.getString("message");
                if (message.equals("email-okey")){
                    startActivity(new Intent(SignUp_Activity.this, EmailValidation_SignUp_Activity.class).putExtra("code", code + "")
                            .putExtra("email", email).putExtra("password", password));
                }else {
                    app.failedToast(getString(R.string.toastEmailExist));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        },error -> {
            app.failedToast(getString(R.string.toastConnectFailed));
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email", email );
                params.put("code", code + "");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}