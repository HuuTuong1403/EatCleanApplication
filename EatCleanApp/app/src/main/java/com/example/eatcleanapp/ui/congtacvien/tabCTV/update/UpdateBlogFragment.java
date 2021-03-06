package com.example.eatcleanapp.ui.congtacvien.tabCTV.update;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.RealPathUtil;
import com.example.eatcleanapp.model.blogimages;
import com.example.eatcleanapp.model.blogs;
import com.example.eatcleanapp.model.recipeimages;
import com.example.eatcleanapp.ui.home.detail.DetailActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateBlogFragment extends Fragment {

    private View view;
    private DetailActivity detailActivity;
    private ImageView imgV_updateBlog_uploadImage;
    private EditText edt_updateBlog_blogTitle, edt_updateBlog_blogContent;
    private Button btn_updateBlog_sendApproval;
    private blogs blog;
    private Uri mUri;
    private List<blogimages> listBlogImage;
    private String urlBlogImage;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        detailActivity = (DetailActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_update_blog, container, false);
        Mapping();
        setData();

        imgV_updateBlog_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && detailActivity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    openDialogChooseImage();
                }
                else
                {
                    openRequest();
                }
            }
        });

        btn_updateBlog_sendApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBlogImages();
                sendUpdate();
            }
        });

        edt_updateBlog_blogContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                setScrollEditText(view, motionEvent);
                return false;
            }
        });

        return view;
    }

    private void setData(){
        blog = detailActivity.getBlogs();
        if(blog == null){
            return;
        }
        else{
            Glide.with(view).load(blog.getImage()).placeholder(R.drawable.gray).into(imgV_updateBlog_uploadImage);
            edt_updateBlog_blogTitle.setText(blog.getBlogTitle());
            edt_updateBlog_blogContent.setText(blog.getBlogContent());
        }
    }

    private void sendUpdate() {
        if(edt_updateBlog_blogTitle.getText().toString().isEmpty() || edt_updateBlog_blogContent.getText().toString().isEmpty()){
            CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                    .setActivity(detailActivity)
                    .setTitle("Th??ng b??o")
                    .setMessage("C??c tr?????ng th??ng tin kh??ng ???????c ????? tr???ng")
                    .setType("error")
                    .Build();
            customAlertActivity.showDialog();
        }
        else{
            String BlogTitle    = edt_updateBlog_blogTitle.getText().toString();
            String BlogAuthor   = blog.getBlogAuthor();
            String BlogContent  = edt_updateBlog_blogContent.getText().toString();
            String Status       = "waitingforapproval";

            updateBlogCtv(blog.getIDBlog(), BlogTitle, BlogAuthor, BlogContent, Status);
        }
    }

    private void updateBlogCtv(String idBlog, String blogTitle, String blogAuthor, String blogContent, String status) {
        APIService.apiService.updateBlogCtv(idBlog, blogTitle, blogAuthor, blogContent, status).enqueue(new Callback<blogs>() {
            @Override
            public void onResponse(Call<blogs> call, Response<blogs> response) {
                if (mUri != null)
                {
                    getUrlImage();
                }
                else {
                    CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                            .setActivity(detailActivity)
                            .setTitle("Th??ng b??o")
                            .setMessage("Ch???nh s???a blog th??nh c??ng, vui l??ng ?????i qu???n tr??? vi??n ph?? duy???t")
                            .setType("success")
                            .Build();
                    customAlertActivity.showDialog();
                }
            }

            @Override
            public void onFailure(Call<blogs> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(detailActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage("Ch???nh s???a blog th???t b???i")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
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
                        .setActivity(detailActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage("L???i kh??ng th??? l???y d??? li???u")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
    }
    private void getUrlImage (){
        String strRealPath = RealPathUtil.getRealPath(view.getContext(), mUri);
        File file = new File(strRealPath);
        RequestBody requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBodyAvatar = MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestBodyAvatar);
        APIService.apiService.uploadImage1(multipartBodyAvatar).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                urlBlogImage = response.body();
                updateBlogImages(blog.getIDBlog(),blog.getImage(),urlBlogImage);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(detailActivity)
                        .setTitle("Th??ng b??o")
                        .setMessage(t.toString())
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
    }
    private void updateBlogImages(String IDBlog, String BlogImage, String url){
        String IDBlogImages = "";
        for (blogimages blogimage: listBlogImage
        ) {
            if (BlogImage.equals(blogimage.getBlogImages()) && IDBlog.equals(blogimage.getIDBlog())){
                IDBlogImages = blogimage.getIDBlogImages();
                break;
            }
        }
        if (!IDBlogImages.equals("")){
            APIService.apiService.updateBlogImage(IDBlogImages, url).enqueue(new Callback<blogimages>() {
                @Override
                public void onResponse(Call<blogimages> call, Response<blogimages> response) {
                    CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                            .setActivity(detailActivity)
                            .setTitle("Th??ng b??o")
                            .setMessage("Ch???nh s???a blog th??nh c??ng, vui l??ng ?????i qu???n tr??? vi??n ph?? duy???t")
                            .setType("success")
                            .Build();
                    customAlertActivity.showDialog();
                }

                @Override
                public void onFailure(Call<blogimages> call, Throwable t) {
                    CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                            .setActivity(detailActivity)
                            .setTitle("Th??ng b??o")
                            .setMessage("Ch???nh s???a h??nh ???nh blog th???t b???i")
                            .setType("error")
                            .Build();
                    customAlertActivity.showDialog();
                }
            });
        }
        else {
            CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                    .setActivity(detailActivity)
                    .setTitle("Th??ng b??o")
                    .setMessage("Kh??ng t??m th???y h??nh ???nh")
                    .setType("error")
                    .Build();
            customAlertActivity.showDialog();
        }
    }
    private void setScrollEditText(View view, MotionEvent motionEvent){
        view.getParent().requestDisallowInterceptTouchEvent(true);
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                view.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
    }

    private void Mapping() {
        imgV_updateBlog_uploadImage = (ImageView)view.findViewById(R.id.imgV_updateBlog_uploadImage);
        edt_updateBlog_blogTitle    = (EditText)view.findViewById(R.id.edt_updateBlog_blogTitle);
        edt_updateBlog_blogContent  = (EditText)view.findViewById(R.id.edt_updateBlog_blogContent);
        btn_updateBlog_sendApproval = (Button)view.findViewById(R.id.btn_updateBlog_sendApproval);
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
                                    Uri uri = Uri.fromParts("package", detailActivity.getPackageName(), null);
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
                                Toast.makeText(detailActivity, "C?? l???i x???y ra!", Toast.LENGTH_SHORT).show();
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
                if(resultCode == detailActivity.RESULT_OK){
                    Bitmap bmp = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                            byteArray.length);
                    imgV_updateBlog_uploadImage.setImageBitmap(bitmap);
                    mUri = getImageUri(view.getContext(), bmp);
                    break;
                }
            case 1:
                if(resultCode == detailActivity.RESULT_OK){
                    Uri pickImage = data.getData();
                    mUri = pickImage;
                    String paths = pickImage.getPath();
                    File imageFile = new File(paths);
                    Log.e("AAA", "" + imageFile);
                    imgV_updateBlog_uploadImage.setImageURI(pickImage);
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