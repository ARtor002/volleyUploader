package ir.ARtor.volley.views;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.ARtor.volley.R;
import ir.ARtor.volley.adapters.Images_adapter;
import ir.ARtor.volley.app.Spref;
import ir.ARtor.volley.app.app;
import ir.ARtor.volley.interfaces.MultiAction_interface;
import ir.ARtor.volley.models.Image_Model;

public class Main_Activity extends AppCompatActivity implements View.OnClickListener, MultiAction_interface {

    //    ActivityResultLauncher<String> resultLauncher;
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static int STORAGE_REQUEST_CODE = 110;
    public static int STORAGE_START_CODE = 111;
    public static int CAMERA_REQUEST_CODE = 120;
    public static int CAMERA_START_CODE = 121;
    String ids;
    ImageView searchImg, moreImg;
    BottomSheetDialog bottomSheetDialog;
    View viewBottomSheet, viewAlertDialog;
    LinearLayout fromGallery, fromCamera, deleteAccount, logOut;
    RequestQueue requestQueue;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    List<Image_Model> list;
    Images_adapter adapter;
    TextView cancelTv, deleteAccTv, cancelDeleteImageTv, deleteImageTv;
    AlertDialog alertDialog;
    RelativeLayout layout;



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
        init();
        getImages();
        onclick();

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (layout.getVisibility() == View.VISIBLE) {
                    cancelMultiAction();
                }
                else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    @Override
    protected void onResume() {
            super.onResume();
            getImages();
    }

    private void cancelMultiAction() {
        layout.setVisibility(View.GONE);
        Images_adapter.count = 0;
        Images_adapter.multiAction = false;
        for (Image_Model img :
                list) {
            if (img.isSelected()) {
                img.setSelected(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void findViews() {
        viewBottomSheet = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, findViewById(R.id.bottomSheet_layout));
        viewAlertDialog = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
        moreImg       = findViewById(R.id.moreImg);
        fromGallery   = viewBottomSheet.findViewById(R.id.fromGallery);
        fromCamera    = viewBottomSheet.findViewById(R.id.fromCamera);
        logOut        = viewBottomSheet.findViewById(R.id.logOut);
        deleteAccount = viewBottomSheet.findViewById(R.id.deleteAccount);
        searchImg = viewBottomSheet.findViewById(R.id.searchImg);
        swipeRefreshLayout = findViewById(R.id.swipeRefLayout);
        recyclerView = findViewById(R.id.recyclerView);
        cancelTv = viewAlertDialog.findViewById(R.id.cancelTv);
        deleteAccTv = viewAlertDialog.findViewById(R.id.deleteAccountTv);
        deleteImageTv = findViewById(R.id.deleteImageTv);
        cancelDeleteImageTv = findViewById(R.id.cancelDeleteImageTv);
        layout = findViewById(R.id.relativeLayout);
    }

    private void onclick() {
        moreImg.setOnClickListener(this);
        deleteImageTv.setOnClickListener(this);
        cancelDeleteImageTv.setOnClickListener(this);
//        fromGallery.setOnClickListener(this);
//        fromCamera.setOnClickListener(this);
//        logOut.setOnClickListener(this);
//        deleteAccount.setOnClickListener(this);
//        cancelTv.setOnClickListener(this);
//        deleteAccTv.setOnClickListener(this);
    }

    private void init(){
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewBottomSheet);
        requestQueue = Volley.newRequestQueue(this);
        list = new ArrayList<>();
        adapter = new Images_adapter(list, this, this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        swipeRefreshLayout.setOnRefreshListener(() -> getImages());
        alertDialog =  new AlertDialog.Builder(this).setView(viewAlertDialog).setCancelable(true).create();
    }

    @Override
    public void onClick(View v) {

        if (v == moreImg) {
            bottomSheet();
        } else if (v == cancelDeleteImageTv){
            cancelMultiAction();
        } else if (v == deleteImageTv) {
            alertDialog.show();
            deleteAccTv.setOnClickListener(v1 -> {
                deleteImagesOperation();
                alertDialog.dismiss();
            });
            cancelTv.setOnClickListener(v1 -> alertDialog.dismiss());
        }
//         else if (v == fromGallery) {
//            if (!hasStoragePermission()) {
//                requestStoragePermission();
//            } else {
//                openGallery();
//            }
//        } else if (v == fromCamera) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//                if(ActivityCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED){
//                    openCamera();
//                } else {
//                    requestPermissions(new String[]{CAMERA}, CAMERA_REQUEST_CODE);
//                }
//            }
//        } else if (v == logOut) {
//            Spref.getSharedPreferences().edit().putBoolean(Spref.CHECK_BOX, false).apply();
//            startActivity(new Intent(this, Login_Activity.class));
//            finish();
//        } else if (v == deleteAccount) {
//            alertDialog.show();
//        } else if (v == cancelTv) {
//            alertDialog.dismiss();
//        } else if (v == deleteAccTv) {
//            deleteAccount();}
    }

    private void deleteImagesOperation() {
        ids = "-1";
        for (Image_Model img :
                list) {
            if (img.isSelected()) {
                ids += "," + img.getId();
            }
        }
//            ids = ids.replaceFirst(",","");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.LOCAL2 + "delete_Images.php", response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.getString("message").equals("delete_okey")){
                    Images_adapter.count = 0;
                    Images_adapter.multiAction = false;
                    layout.setVisibility(View.GONE);
                    getImages();
                    app.successToast(getString(R.string.toastDeleteSuccess));
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
                params.put("ids", ids);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void bottomSheet() {
        bottomSheetDialog.show();

        fromGallery.setOnClickListener(v -> {
            if (!hasStoragePermission()) {
                requestStoragePermission();
            } else {
                openGallery();
            }
        });

        fromCamera.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                if(ActivityCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                } else {
                    requestPermissions(new String[]{CAMERA}, CAMERA_REQUEST_CODE);
                }
            }
        });

        logOut.setOnClickListener(v -> {
            Spref.getSharedPreferences().edit().putBoolean(Spref.CHECK_BOX, false).apply();
            startActivity(new Intent(this, Login_Activity.class));
            finish();
        });

        deleteAccount.setOnClickListener(v -> deleteAccountOperate());
    }

    private void deleteAccountOperate() {
        alertDialog.show();
        deleteAccTv.setOnClickListener(v -> deleteAccount());
        cancelTv.setOnClickListener(v -> alertDialog.dismiss());
    }

    private void deleteAccount() {
//        ProgressDialog progressDialog = ProgressDialog.
        String email = Spref.getSharedPreferences().getString(Spref.EMAIL,"");
        StringRequest request = new StringRequest(Request.Method.POST,app.LOCAL2 + "delete_acc.php",response -> {
            try {
                JSONObject object = new JSONObject(response);
                if (object.getString("message").equals("Delete_okey")){
                    Spref.getSharedPreferences().edit().putString(Spref.EMAIL, "").apply();
                    Spref.getSharedPreferences().edit().putBoolean(Spref.CHECK_BOX, false).apply();
                    startActivity(new Intent(this, Login_Activity.class));
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
                params.put("email", email);
                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_START_CODE ){
            if (resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                upload(bitmap);
            }else {
                app.failedToast(getString(R.string.toastNoImageSelect));
            }
        }

        if (requestCode == STORAGE_START_CODE){
            if (resultCode == RESULT_OK){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    upload(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else {
                app.failedToast(getString(R.string.toastNoImageSelect));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA)){
                    showPermissionRationale(CAMERA_REQUEST_CODE);
                }else {
                    showSettingsRedirectDialog();
                }
            }
        }
        if (requestCode == STORAGE_REQUEST_CODE) {
            boolean granted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }

            if (granted) {
                openGallery();
            } else {
                boolean showRationale = false;
                for (String permission : permissions) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        showRationale = true;
                        break;
                    }
                }

                if (showRationale) {
                    showPermissionRationale(STORAGE_REQUEST_CODE);
                } else {
                    showSettingsRedirectDialog();
                }
            }
        }
    }

    private void getImages() {
        cancelMultiAction();
        list.clear();
        swipeRefreshLayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.LOCAL2 + "get_image.php",response -> {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    Image_Model imageModel = new Image_Model();

                    imageModel.setId(jsonObject.getString("id"));
                    imageModel.setEmail(jsonObject.getString("email"));
                    imageModel.setImage(jsonObject.getString("image"));
                    imageModel.setUploaded(jsonObject.getString("uploaded"));

                    list.add(imageModel);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> app.failedToast(getString(R.string.toastConnectFailed))){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Spref.EMAIL, Spref.getSharedPreferences().getString(Spref.EMAIL, ""));
                return params ;
            }
        };
        requestQueue.add(stringRequest);
        swipeRefreshLayout.setRefreshing(false);
    }

    private boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14 (API 34)
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED;
        }
        else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) { // Android 13 (API 33)
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
        }
        else { // Android 12  (API -32)
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED}, STORAGE_REQUEST_CODE);

        }
        else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) { // Android 13
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO}, STORAGE_REQUEST_CODE);
        }
        else { // Android 12
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        }
    }

    private void showSettingsRedirectDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.permissionTitle))
                .setMessage(getString(R.string.permissionDeniedMessage))
                .setPositiveButton(getString(R.string.settingBtn), (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton(getString(R.string.cancelBtn), (dialog, which) -> dialog.dismiss())
                .show();
    }

    public void showPermissionRationale(int code) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.permissionTitle))
                .setMessage(getString(R.string.permissionDeniedMessage))
                .setPositiveButton(getString(R.string.grantBtn), (dialog, which) ->{
                    if (code == CAMERA_REQUEST_CODE){
                        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, code);
                    }else requestStoragePermission();})
                .setNegativeButton(getString(R.string.cancelBtn), (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openGallery() {
        startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), STORAGE_START_CODE);
        bottomSheetDialog.dismiss();
    }

    private void openCamera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_START_CODE);
        bottomSheetDialog.dismiss();

    }


    private void upload(Bitmap bitmap){
        int size = (int) (bitmap.getHeight() * (812.0 / bitmap.getWidth()));
        Bitmap decode = Bitmap.createScaledBitmap(bitmap, 812, size, true);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decode.compress(Bitmap.CompressFormat.JPEG, 95, byteArrayOutputStream);
        String image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        StringRequest request = new StringRequest(Request.Method.POST, app.LOCAL2 + "upload_image.php", response -> {
            try {
                app.l(response);
                JSONObject jsonObject = new JSONObject(response);
                String message = jsonObject.getString("message");
                if (message.equals("Uploaded")){
                    app.successToast(getString(R.string.toastImageUploaded));
                    getImages();
                }else {
                    app.failedToast(getString(R.string.toastConnectFailed));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            app.failedToast(getString(R.string.toastConnectFailed));
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", Spref.getSharedPreferences().getString("email", ""));
                params.put("image", image);

                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void started() {
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void selected(int count, int pos) {
        list.get(pos).setSelected(true);
    }

    @Override
    public void deSelected(int count, int pos) {
        list.get(pos).setSelected(false);

        if (count == 0) {
            layout.setVisibility(View.GONE);
        }
    }

}