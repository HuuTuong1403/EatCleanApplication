package com.example.gameboivui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    TextView txtNoiDung;
    Button  btnQuayLai;
    ImageView imgHinh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        txtNoiDung = (TextView) findViewById(R.id.textViewNoiDung);
        btnQuayLai = (Button) findViewById(R.id.buttonQuayLai);
        imgHinh = (ImageView) findViewById(R.id.imageXemBoi);
        Intent form2 = getIntent();
        String ten = form2.getStringExtra("tenNguoiChoi");
        int so = form2.getIntExtra("soBoi",0);
        switch (so){
            case 0:{
                imgHinh.setImageResource(R.drawable.boi0);
                txtNoiDung.setText(ten+" năm nay bạn sẽ được crush tỏ tình");

            }
            break;
            case 1:{
                imgHinh.setImageResource(R.drawable.boi1);
                txtNoiDung.setText(ten+" năm nay tiền bạc bạn nhiều vô kể");
            }
            break;
            case 2:{
                imgHinh.setImageResource(R.drawable.boi2);
                txtNoiDung.setText(ten+" năm nay bạn sẽ gặp được nhiều may mắn");
            }
            break;
            case 3:{
                imgHinh.setImageResource(R.drawable.boi3);
                txtNoiDung.setText(ten+" năm nay bạn sẽ gặp một tai nạn bất ngờ trong cuộc sống nhưng nó không làm bạn nản lòng");
            }
            break;
            case 4:{
                imgHinh.setImageResource(R.drawable.boi4);
                txtNoiDung.setText(ten+" năm nay bạn sẽ nhận được quà từ bố hoặc mẹ");
            }
            break;
            case 5:{
                imgHinh.setImageResource(R.drawable.boi5);
                txtNoiDung.setText(ten+" năm nay lộc lá sẽ đến bên bạn");
            }
            break;
            case 6:{
                imgHinh.setImageResource(R.drawable.boi6);
                txtNoiDung.setText(ten+" hãy cẩn thận thằng bạn thân của bạn vì nó sẽ cướp lấy tất cả của bạn");
            }
            break;
            case 7:{
                imgHinh.setImageResource(R.drawable.boi7);
                txtNoiDung.setText(ten+" năm nay bạn sẽ nhận được 1 giải thưởng cao quý");
            }
            break;
            case 8:{
                imgHinh.setImageResource(R.drawable.boi8);
                txtNoiDung.setText(ten+" năm nay rất có thể bạn sẽ gặp duyên âm hãy cẩn thận nơi mà mình đến và chú ý ngôn từ của bản thân");
            }
            break;
            case 9:{
                imgHinh.setImageResource(R.drawable.boi9);
                txtNoiDung.setText(ten+" năm nay là năm thành công trong sự nghiệp của bạn hãy chuẩn bị khởi nghiệp nào!!");
            }
            break;

        }
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity2.this.finish();
                System.exit(0);
            }
        });



    }
}