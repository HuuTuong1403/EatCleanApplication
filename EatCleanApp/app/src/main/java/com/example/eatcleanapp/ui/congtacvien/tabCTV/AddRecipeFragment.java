package com.example.eatcleanapp.ui.congtacvien.tabCTV;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.MainActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.model.recipes;
import com.example.eatcleanapp.model.users;
import com.example.eatcleanapp.ui.nguoidung.data_local.DataLocalManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddRecipeFragment extends Fragment {

    private View view;
    private ImageView imgV_addRecipe_uploadImage;
    private EditText edt_addRecipe_recipeTitle, edt_addRecipe_recipeContent, edt_addRecipe_recipeNutritional, edt_addRecipe_recipeIngredients, edt_addRecipe_recipeSteps, edt_addRecipe_recipeTime;
    private Button btn_addRecipe_sendApproval;
    private MainActivity mMainActivity;
    private Uri mUri;
    private String IDRecipe;
    private List<recipes> listRecipes;
    private users user;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        Mapping();

        imgV_addRecipe_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMainActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && mMainActivity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    openDialogChooseImage();
                }
                else
                {
                    openRequest();
                }
            }
        });

        btn_addRecipe_sendApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRecipes();
                sendApproval();
            }
        });

        edt_addRecipe_recipeSteps.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setScrollEditText(view, motionEvent);
                return false;
            }
        });
        edt_addRecipe_recipeIngredients.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setScrollEditText(view, motionEvent);
                return false;
            }
        });

        return view;
    }

    private void setScrollEditText(View view, MotionEvent motionEvent){
        view.getParent().requestDisallowInterceptTouchEvent(true);
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                view.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
    }
    
    private void sendApproval(){
        if (edt_addRecipe_recipeTitle.getText().toString().isEmpty() ||
                edt_addRecipe_recipeContent.getText().toString().isEmpty() ||
                edt_addRecipe_recipeNutritional.getText().toString().isEmpty() ||
                edt_addRecipe_recipeIngredients.getText().toString().isEmpty() ||
                edt_addRecipe_recipeSteps.getText().toString().isEmpty() ||
                edt_addRecipe_recipeTime.getText().toString().isEmpty()) {
            CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                    .setActivity(mMainActivity)
                    .setTitle("Thêm công thức món ăn thất bại")
                    .setMessage("Các trường nhập liệu không được trống")
                    .Build();
            customAlertActivity.showErrorDialog();
        }
        else{
            if(mUri == null){
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mMainActivity)
                        .setTitle("Thêm công thức món ăn thất bại")
                        .setMessage("Vui lòng tải hình ảnh món ăn lên")
                        .Build();
                customAlertActivity.showErrorDialog();
            }
            else{
                Random rd = new Random();
                boolean checkIDRecipe = true;
                while (checkIDRecipe){
                    checkIDRecipe = false;
                    int x = rd.nextInt((50000-1000 + 1) + 1000);
                    IDRecipe = "ID-R-" + x;
                    for (recipes recipe: listRecipes
                    ) {
                        if (IDRecipe.equals(recipe.getIDRecipes())) {
                            checkIDRecipe = true;
                            break;
                        }
                    }
                }
                String recipeTitle          = edt_addRecipe_recipeTitle.getText().toString();
                String recipeAuthor         = user.getFullName();
                String recipeContent        = edt_addRecipe_recipeContent.getText().toString();
                String recipeNutritional    = edt_addRecipe_recipeNutritional.getText().toString();
                String recipeIngredient     = edt_addRecipe_recipeIngredients.getText().toString();
                String recipeStep           = edt_addRecipe_recipeSteps.getText().toString();
                String recipeTime           = edt_addRecipe_recipeTime.getText().toString();
                String recipeStatus         = "waittingforapproval";

                addRecipeCtv(IDRecipe, recipeTitle, recipeAuthor, recipeContent, recipeNutritional, recipeIngredient, recipeStep, recipeTime, recipeStatus);
            }
        }

    }

    private void addRecipeCtv(String id, String title, String author, String content, String nuTri, String ingredient, String step, String time, String status){
        APIService.apiService.addRecipeCtv(id, title, author, content, nuTri, ingredient, step, time, status).enqueue(new Callback<recipes>() {
            @Override
            public void onResponse(Call<recipes> call, Response<recipes> response) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mMainActivity)
                        .setTitle("Thêm công thức món ăn thành công")
                        .setMessage("Gửi phê duyệt công thức món ăn thành công")
                        .Build();
                customAlertActivity.showSuccessDialog();
                edt_addRecipe_recipeTitle.setText("");
                edt_addRecipe_recipeContent.setText("");
                edt_addRecipe_recipeNutritional.setText("");
                edt_addRecipe_recipeIngredients.setText("");
                edt_addRecipe_recipeSteps.setText("");
                edt_addRecipe_recipeTime.setText("");
                imgV_addRecipe_uploadImage.setImageResource(R.drawable.up);
            }

            @Override
            public void onFailure(Call<recipes> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mMainActivity)
                        .setTitle("Thêm công thức món ăn thất bại")
                        .setMessage("Gửi phê duyệt công thức món ăn thất bại")
                        .Build();
                customAlertActivity.showErrorDialog();
            }
        });
    }

    private void getRecipes(){
        APIService.apiService.getRecipes().enqueue(new Callback<List<recipes>>() {
            @Override
            public void onResponse(Call<List<recipes>> call, Response<List<recipes>> response) {
                listRecipes = response.body();
            }

            @Override
            public void onFailure(Call<List<recipes>> call, Throwable t) {
                Toast.makeText(mMainActivity, "Call API Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Mapping() {
        imgV_addRecipe_uploadImage      = (ImageView)view.findViewById(R.id.imgV_addRecipe_uploadImage);
        edt_addRecipe_recipeTitle       = (EditText)view.findViewById(R.id.edt_addRecipe_recipeTitle);
        edt_addRecipe_recipeContent     = (EditText)view.findViewById(R.id.edt_addRecipe_recipeContent);
        edt_addRecipe_recipeNutritional = (EditText)view.findViewById(R.id.edt_addRecipe_recipeNutritional);
        edt_addRecipe_recipeIngredients = (EditText)view.findViewById(R.id.edt_addRecipe_recipeIngredients);
        edt_addRecipe_recipeSteps       = (EditText)view.findViewById(R.id.edt_addRecipe_recipeSteps);
        edt_addRecipe_recipeTime        = (EditText)view.findViewById(R.id.edt_addRecipe_recipeTime);
        btn_addRecipe_sendApproval      = (Button)view.findViewById(R.id.btn_addRecipe_sendApproval);
        listRecipes                     = new ArrayList<>();
        user                            = DataLocalManager.getUser();
    }

    private Dialog createDialog(int layout){
        Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        if(window == null){
            return null;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAtributes = window.getAttributes();
        windowAtributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAtributes);
        return dialog;
    }

    private void openRequest(){
        Dialog dialog = createDialog(R.layout.layout_dialog_request);
        if(dialog == null)
            return;
        Button btnAccept = (Button)dialog.findViewById(R.id.btn_accept_request);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Dexter.withContext(view.getContext())
                        .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .withListener(new MultiplePermissionsListener(){
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if(multiplePermissionsReport.areAllPermissionsGranted()){
                                    openDialogChooseImage();
                                }
                                if(multiplePermissionsReport.isAnyPermissionPermanentlyDenied()){
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", mMainActivity.getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            }
                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        })
                        .withErrorListener(new PermissionRequestErrorListener() {
                            @Override
                            public void onError(DexterError dexterError) {
                                Toast.makeText(mMainActivity, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                            }
                        }).onSameThread().check();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private void openDialogChooseImage(){
        Dialog dialog = createDialog(R.layout.layout_choose_image);
        if(dialog == null)
            return;

        Button btn_chooseImage_Camera = (Button)dialog.findViewById(R.id.btn_chooseImage_Camera);
        Button btn_chooseImage_Media = (Button)dialog.findViewById(R.id.btn_chooseImage_Media);
        Button btn_chooseImage_Cancel = (Button)dialog.findViewById(R.id.btn_chooseImage_Cancel);

        btn_chooseImage_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePhoto, 0);
            }
        });

        btn_chooseImage_Media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });

        btn_chooseImage_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 0:
                if(resultCode == mMainActivity.RESULT_OK){
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                            byteArray.length);
                    imgV_addRecipe_uploadImage.setImageBitmap(bitmap);
                    mUri = getImageUri(view.getContext(), bmp);
                    break;
                }
            case 1:
                if(resultCode == mMainActivity.RESULT_OK){
                    Uri pickImage = data.getData();
                    mUri = pickImage;
                    String paths = pickImage.getPath();
                    File imageFile = new File(paths);
                    Log.e("AAA", "" + imageFile);
                    imgV_addRecipe_uploadImage.setImageURI(pickImage);
                    break;
                }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, String.valueOf(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }
}