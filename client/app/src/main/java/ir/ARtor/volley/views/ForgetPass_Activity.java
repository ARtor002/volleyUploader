package ir.ARtor.volley.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ForgetPass_Activity extends AppCompatActivity implements View.OnClickListener {

    ImageView backImg;
    EditText emailEdt;
    Button receiveCodeBtn;
    TextView emailErrorTv;
    int code;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViews();
        init();
        onclick();

    }

    private void onclick() {
        backImg.setOnClickListener(this);
        receiveCodeBtn.setOnClickListener(this);
    }

    private void init() {
        requestQueue = Volley.newRequestQueue(this);
    }

    private void findViews() {
        backImg = findViewById(R.id.imageView_back);
        emailEdt = findViewById(R.id.emailEdt);
        receiveCodeBtn = findViewById(R.id.receiveCodeBtn);
        emailErrorTv = findViewById(R.id.emailErrorTv);
    }

    @Override
    public void onClick(View v) {
        if (v == receiveCodeBtn) {
            emailErrorTv.setVisibility(View.GONE);
            String email = emailEdt.getText().toString().toLowerCase().trim();

            if (!email.matches(Patterns.EMAIL_ADDRESS.pattern())) {
                emailErrorTv.setVisibility(View.VISIBLE);
            } else {
                emailErrorTv.setVisibility(View.GONE);
                forgetPassword(email);
            }
        }
        if (v == backImg) {
            finish();
        }
    }

    private void forgetPassword(String email) {
        do {
            code = new Random().nextInt(1000000);
        } while (code < 100000);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.LOCAL2 + "check_email_forget.php", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String message = jsonObject.getString("message");
                if (message.equals("email-okey") || message.equals("email-notfound")) {
                    startActivity(new Intent(ForgetPass_Activity.this, EmailValidation_ForgetPass_Activity.class).putExtra("code", code + "")
                            .putExtra("email", email));
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
                params.put("code", code + "");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}