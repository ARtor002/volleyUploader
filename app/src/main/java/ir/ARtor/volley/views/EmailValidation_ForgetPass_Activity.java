package ir.ARtor.volley.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
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

public class EmailValidation_ForgetPass_Activity extends AppCompatActivity implements View.OnClickListener {

    String email, password;
    EditText passwordEdt;
    PinView pinView;
    Button confirmBtn, changePassBtn;
    ImageView backImg, imgv;
    RequestQueue requestQueue;
    LinearLayout linearPinView, linearPassword;

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

    private void findViews() {
        pinView = findViewById(R.id.pinView);
        confirmBtn = findViewById(R.id.confirmBtn);
        changePassBtn = findViewById(R.id.changePassBtn);
        backImg = findViewById(R.id.imageView_back);
        passwordEdt = findViewById(R.id.passwordEdt);
        linearPassword = findViewById(R.id.linearPassword);
        linearPinView = findViewById(R.id.linearPinView);
        imgv = findViewById(R.id.imgv);
    }

    private void onclick() {
        confirmBtn.setOnClickListener(this);
        backImg.setOnClickListener(this);
        changePassBtn.setOnClickListener(this);
    }

    private void init() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {

            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
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
        if (v == confirmBtn) {
            try {
                String entryCode = pinView.getText().toString();
                if (entryCode.equals(getIntent().getExtras().getString("code"))) {
                    app.successToast(getString(R.string.changePasswordBtn));
                    linearPinView.setVisibility(View.GONE);
                    confirmBtn.setVisibility(View.GONE);
                    linearPassword.setVisibility(View.VISIBLE);
                    changePassBtn.setVisibility(View.VISIBLE);
                    imgv.setImageResource(R.drawable.f);
                } else {
                    app.failedToast(getString(R.string.toastWrongCode));
                    pinView.setLineColor(getColor(R.color.colorRed));
                }

            } catch (NullPointerException e) {
                app.failedToast(getString(R.string.toastWrongCode));
            }
        } else if (v == changePassBtn) {
            password = passwordEdt.getText().toString();
            if (password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_=+-.{}]).{8,}")) {
                changePassword();
            } else {
                passwordEdt.setError(getString(R.string.PassErrorTv));
            }
        } else if (v == backImg) {
            finish();
        }
    }

    private void changePassword() {
        email = getIntent().getExtras().getString("email");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.LOCAL2 + "new_pass.php", response -> {
            app.l(response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String message = jsonObject.getString("message");
                if (message.equals("okey")) {
                    Spref.getSharedPreferences().edit().putString(Spref.EMAIL, email).apply();
                    startActivity(new Intent(EmailValidation_ForgetPass_Activity.this, Login_Activity.class));
                    finish();
                } else {
                    app.failedToast(getString(R.string.toastConnectFailed));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }, error -> {
            app.failedToast(getString(R.string.toastConnectFailed));
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}