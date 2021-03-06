package com.example.eatcleanapp.ui.congtacvien.tabCTV;

import android.Manifest;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.MainActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.RealPathUtil;
import com.example.eatcleanapp.model.blogimages;
import com.example.eatcleanapp.model.blogs;
import com.example.eatcleanapp.model.recipeimages;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBlogFragment extends Fragment {

    private View view;
    private ImageView imgV_addBlog_uploadImage;
    private EditText edt_addBlog_blogTitle, edt_addBlog_blogContent;
    private Button btn_addBlog_sendApproval;
    private MainActivity mMainActivity;
    private users user;
    private List<blogs> listBlogs;
    private Uri mUri;
    private String IDBlog;
    private String urlBlogImage;
    private List<blogimages> listBlogImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_add_blog, container, false);
        Mapping();

        imgV_addBlog_uploadImage.setOnClickListener(new View.OnClickListener() {
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

        btn_addBlog_sendApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBlogs();
                getBlogImages();
                sendApproval();
            }
        });

        return view;
    }

    private void sendApproval() {
        if(edt_addBlog_blogTitle.getText().toString().isEmpty() || edt_addBlog_blogContent.getText().toString().isEmpty()){
            CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                    .setActivity(mMainActivity)
                    .setTitle("Th??ng b??o")
                    .setMessage("C??c tr?????ng nh???p li???u kh??ng ???????c tr???ng")
                    .setType("error")
                    .Build();
            customAlertActivity.showDialog();
        }
        else{
            if(mUri == null) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mMainActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage("Vui l??ng t???i h??nh ???nh blog l??n")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
            else{
                Random rd = new Random();
                boolean checkIDBlog = true;
                while (checkIDBlog){
                    checkIDBlog = false;
                    int x = rd.nextInt((50000-1000 + 1) + 1000);
                    IDBlog = "ID-B-" + x;
                    for (blogs blog: listBlogs
                    ) {
                        if (IDBlog.equals(blog.getIDBlog())) {
                            checkIDBlog = true;
                            break;
                        }
                    }
                }
                String BlogTitle    = edt_addBlog_blogTitle.getText().toString();
                String BlogAuthor   = user.getFullName();
                String BlogContent  = edt_addBlog_blogContent.getText().toString();
                String Status       = "waitingforapproval";
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date();
                String Time = df.format(now);
                addBlogCtv(IDBlog, BlogTitle, BlogAuthor, BlogContent, Time, Status);

            }
        }
    }

    private void addBlogCtv(String id, String title, String author, String content, String time, String status){
        APIService.apiService.addBlogCtv(id, title, author, content, time, status).enqueue(new Callback<blogs>() {
            @Override
            public void onResponse(Call<blogs> call, Response<blogs> response) {
                getUrlImage(id);
            }

            @Override
            public void onFailure(Call<blogs> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mMainActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage("G???i ph?? duy???t blog th???t b???i")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
    }
    private void addBlogImage (String IDBlog, String Url){
        String IDBlogImages = "";
        Random rd = new Random();
        boolean checkIDBlogImages = true;
        while (checkIDBlogImages){
            checkIDBlogImages = false;
            int x = rd.nextInt((50000-1000 + 1) + 1000);
            IDBlogImages = "RM-" + x;
            for (blogimages blogimage: listBlogImage
            ) {
                if (IDBlogImages.equals(blogimage.getBlogImages())) {
                    checkIDBlogImages = true;
                    break;
                }
            }
        }
        APIService.apiService.addBlogImage(IDBlogImages, Url, IDBlog).enqueue(new Callback<blogimages>() {
            @Override
            public void onResponse(Call<blogimages> call, Response<blogimages> response) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mMainActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage("G???i ph?? duy???t blog th??nh c??ng")
                        .setType("success")
                        .Build();
                customAlertActivity.showDialog();
                edt_addBlog_blogTitle.setText("");
                edt_addBlog_blogContent.setText("");
                imgV_addBlog_uploadImage.setImageResource(R.drawable.up);
            }

            @Override
            public void onFailure(Call<blogimages> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mMainActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage("H??nh ???nh t???i l??n th???t b???i")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
    }
    private void getUrlImage (String IDBlog){
        String strRealPath = RealPathUtil.getRealPath(view.getContext(), mUri);
        File file = new File(strRealPath);
        RequestBody requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBodyAvatar = MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestBodyAvatar);
        APIService.apiService.uploadImage1(multipartBodyAvatar).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                urlBlogImage = response.body();
                addBlogImage(IDBlog, urlBlogImage);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mMainActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage(t.toString())
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
    }
    private void getBlogs(){
        APIService.apiService.getBlogsAll().enqueue(new Callback<List<blogs>>() {
            @Override
            public void onResponse(Call<List<blogs>> call, Response<List<blogs>> response) {
                listBlogs = response.body();
            }

            @Override
            public void onFailure(Call<List<blogs>> call, Throwable t) {
                Toast.makeText(mMainActivity, "Call API Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getBlogImages(){
        APIService.apiService.getImageBlog().enqueue(new Callback<List<blogimages>>() {
            @Override
            public void onResponse(Call<List<blogimages>> call, Response<List<blogimages>> response) {
                listBlogImage = response.body();
            }

            @Override
            public void onFailure(Call<List<blogimages>> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(mMainActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage("L???i kh??ng th??? l???y d??? li???u")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
    }
    private void Mapping() {
        imgV_addBlog_uploadImage    = (ImageView)view.findViewById(R.id.imgV_addBlog_uploadImage);
        edt_addBlog_blogTitle       = (EditText)view.findViewById(R.id.edt_addBlog_blogTitle);
        edt_addBlog_blogContent     = (EditText)view.findViewById(R.id.edt_addBlog_blogContent);
        btn_addBlog_sendApproval    = (Button)view.findViewById(R.id.btn_addBlog_sendApproval);
        listBlogs                   = new ArrayList<>();
        user                        = DataLocalManager.getUser();
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
                                Toast.makeText(mMainActivity, "C?? l???i x???y ra!", Toast.LENGTH_SHORT).show();
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
                /*File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "AvatarFolder");
                mediaStorageDir.mkdirs();
                mUri = Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator +
                        "avatar.jpg"));
                takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, mUri);*/
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
                    imgV_addBlog_uploadImage.setImageBitmap(bitmap);
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
                    imgV_addBlog_uploadImage.setImageURI(pickImage);
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