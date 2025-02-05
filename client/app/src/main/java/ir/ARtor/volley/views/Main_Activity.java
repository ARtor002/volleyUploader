package ir.ARtor.volley.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import ir.ARtor.volley.R;

public class Main_Activity extends AppCompatActivity implements View.OnClickListener {

    ImageView searchImg, moreImg;
    BottomSheetDialog bottomSheetDialog;
    View view;
    LinearLayout fromGallery, fromCamera, deleteAccount, logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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
        moreImg       = findViewById(R.id.moreImg);
        fromGallery   = view.findViewById(R.id.fromGallery);
        fromCamera    = view.findViewById(R.id.fromCamera);
        logOut        = view.findViewById(R.id.logOut);
        deleteAccount = view.findViewById(R.id.deleteAccount);
    }

    private void onclick() {
        moreImg.setOnClickListener(this);
        fromGallery.setOnClickListener(this);
        fromCamera.setOnClickListener(this);
        logOut.setOnClickListener(this);
        deleteAccount.setOnClickListener(this);
    }

    private void init(){
        bottomSheetDialog = new BottomSheetDialog(this);
        view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, findViewById(R.id.bottomSheet_layout));
        bottomSheetDialog.setContentView(view);
    }

    @Override
    public void onClick(View v) {
        if (v == moreImg) {
            bottomSheetDialog.show();

        } else if (v == fromGallery) {

        } else if (v == fromCamera) {

        } else if (v == logOut) {

        } else if (v == deleteAccount) {

        }
    }
}