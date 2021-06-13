package com.example.eatcleanapp.CustomAlert;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.CaseMap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.eatcleanapp.R;

public class CustomAlertActivity {

    private Activity activity;
    private String Title;
    private String Messgae;

    public CustomAlertActivity(Builder builder) {
        this.activity = builder.activity;
        this.Title = builder.Title;
        this.Messgae = builder.Messgae;
    }

    public void showSuccessDialog(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder
                        (activity, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.layout_success_dialog,
                (ConstraintLayout) activity.findViewById (R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle))
                .setText(Title);
        ((TextView) view.findViewById(R.id.textMessage))
                .setText(Messgae);
        ((Button) view.findViewById(R.id.buttonAction))
                .setText("OK");
        ((ImageView) view.findViewById(R.id.imageIcon))
                .setImageResource(R.drawable.done);
        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    public void showErrorDialog(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder
                        (activity, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.layout_error_dialog,
                (ConstraintLayout) activity.findViewById (R.id.layoutDialogContainer)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.textTitle))
                .setText(Title);
        ((TextView) view.findViewById(R.id.textMessage))
                .setText(Messgae);
        ((Button) view.findViewById(R.id.buttonAction))
                .setText("Thoát");
        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.buttonAction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
    public static class Builder {
        private Activity activity;
        private String Title;
        private String Messgae;

        public Builder(){

        };

        public Builder(Activity activity, String title, String messgae) {
            this.activity = activity;
            Title = title;
            Messgae = messgae;
        }

        public Builder setActivity (Activity activity){
            this.activity = activity;
            return this;
        }

        public Builder setTitle (String title){
            this.Title = title;
            return this;
        }
        public Builder setMessage (String message){
            this.Messgae = message;
            return this;
        }

        public CustomAlertActivity Build (){
            return new CustomAlertActivity(this);
        }
    }
}
