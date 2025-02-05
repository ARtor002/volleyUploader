package ir.ARtor.volley.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.ARtor.volley.R;
import ir.ARtor.volley.app.Spref;
import ir.ARtor.volley.app.app;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener {

    EditText emailEdt, passwordEdt;
    TextView forgetPasswordTv, signUpTv, errorTv;
    Button signInBtn;
    CheckBox rememberCheckBox;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViews();
        init();
        onclick();

    }

    private void findViews() {
        passwordEdt      = findViewById(R.id.passwordEdt);
        emailEdt         = findViewById(R.id.emailEdt);
        signInBtn        = findViewById(R.id.signUpBtn);
        signUpTv         = findViewById(R.id.signInTv);
        errorTv = findViewById(R.id.emailErrorTv);
        forgetPasswordTv = findViewById(R.id.forgetPasswordTv);
        rememberCheckBox = findViewById(R.id.rememberCheckBox);
    }

    private void init() {
        if (Spref.getSharedPreferences().getBoolean(Spref.CHECK_BOX, false)){
            String email = Spref.getSharedPreferences().getString(Spref.EMAIL, "");
            rememberCheckBox.setChecked(true);
            emailEdt.setText(email);
        }
        requestQueue = Volley.newRequestQueue(this);
    }

    private void onclick() {
        forgetPasswordTv.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        signUpTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == signInBtn) {
            errorTv.setVisibility(View.GONE);
            String email = emailEdt.getText().toString().toLowerCase().trim();
            String password = passwordEdt.getText().toString().trim();

            if (!email.matches(Patterns.EMAIL_ADDRESS.pattern()) || !password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_=+-.{}]).{8,}")) {
                errorTv.setVisibility(View.VISIBLE);
            } else {
                errorTv.setVisibility(View.GONE);
                login(email, password);
            }

        } else if (v == forgetPasswordTv) {
            startActivity(new Intent(this, ForgetPass_Activity.class));
        } else if (v == signUpTv) {
            startActivity(new Intent(this, SignUp_Activity.class));
        }
    }

    private void login(String email, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.LOCAL2 + "login.php", response -> {
            try {
//                app.l(response);
                JSONObject jsonObject = new JSONObject(response);
                String message = jsonObject.getString("message");
                if (message.equals("Login Successful")) {
                    Spref.getSharedPreferences().edit().putString(Spref.EMAIL, email).apply();
                    Spref.getSharedPreferences().edit().putBoolean(Spref.CHECK_BOX, rememberCheckBox.isChecked()).apply();
                    startActivity(new Intent(Login_Activity.this, Main_Activity.class));
                    finish();
                } else {
                    app.failedToast(getString(R.string.toastLoginFailed));
                    errorTv.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            app.l(error.toString());
            app.failedToast(getString(R.string.toastConnectFailed));
        }) {

//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("User-Agent", "Mozilla/5.0");
//                headers.put("Accept", "application/json");
//                headers.put("Content-Type", "application/x-www-form-urlencoded");
//                return headers;
//            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<>();
                param.put("email", email);
                param.put("password", password);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
}