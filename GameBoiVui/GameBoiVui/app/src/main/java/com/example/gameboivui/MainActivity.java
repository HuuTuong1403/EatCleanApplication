package com.example.gameboivui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText edtNgay, edtThang, edtNam,edtTen;
    Button btnXem;
    int tuoiNguoiChoi,thangSinh,ngaySinh;
    String tenNguoiChoi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtNgay = (EditText) findViewById(R.id.editTextNgaySinh);
        edtThang = (EditText) findViewById(R.id.editTextThangSinh);
        edtNam = (EditText) findViewById(R.id.editTextNamSinh);
        edtTen = (EditText) findViewById(R.id.editTextTen);
        btnXem = (Button) findViewById(R.id.buttonXemKetQua);
        btnXem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ngay = edtNgay.getText().toString();
                String thang = edtThang.getText().toString();
                String nam = edtNam.getText().toString();
                tenNguoiChoi = edtTen.getText().toString();
                Intent form2 = new Intent(MainActivity.this,MainActivity2.class);
                if (ngay.isEmpty()|thang.isEmpty()|nam.isEmpty()) {
                    Toast.makeText(MainActivity.this,"Bạn chưa nhập thông tin",Toast.LENGTH_SHORT).show();
                }
                else {
                    tuoiNguoiChoi = 2021 - Integer.parseInt(nam);
                    ngaySinh=Integer.parseInt(ngay);
                    thangSinh=Integer.parseInt(thang);
                    tuoiNguoiChoi=(tuoiNguoiChoi+ngaySinh+thangSinh)%10;
                    form2.putExtra("tenNguoiChoi",tenNguoiChoi);
                    form2.putExtra("soBoi",tuoiNguoiChoi);
                    startActivity(form2);
                }
            }
        });


    }
}