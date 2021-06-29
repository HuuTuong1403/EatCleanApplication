package com.example.eatcleanapp.ui.quantrivien.statistic.tabStatistic;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticAdminUserFragment extends Fragment {

    private View view;
    private LineChart chart;
    private EditText statistic_edt_chooseYear;
    private AdminActivity adminActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adminActivity = (AdminActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_statistic_admin_user, container, false);

        statistic_edt_chooseYear = view.findViewById(R.id.statistic_edt_chooseYear);

        statistic_edt_chooseYear.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(statistic_edt_chooseYear.getText().toString().isEmpty()){
                        CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                .setActivity(adminActivity)
                                .setTitle("Thông báo")
                                .setMessage("Vui lòng nhập năm")
                                .setType("error")
                                .Build();
                        customAlertActivity.showDialog();
                    }
                    else{
                        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                        int inputYear = Integer.parseInt(statistic_edt_chooseYear.getText().toString()); 
                        if(inputYear > currentYear) {
                            CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                                    .setActivity(adminActivity)
                                    .setTitle("Thông báo")
                                    .setMessage("Năm không được lớn hơn năm hiện tại")
                                    .setType("error")
                                    .Build();
                            customAlertActivity.showDialog();
                        }
                        else{
                            statistic_edt_chooseYear.setText("");
                        }
                    }
                }
                return false;
            }
        });


        chart = view.findViewById(R.id.statistic_user_lineChart);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(false);

        List<Entry> totalUserForeachMonth = getListData();

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);


        List<String> xAxisValues = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6","7", "8", "9", "10", "11", "12"));
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));

        LineDataSet lineDataSet = new LineDataSet(totalUserForeachMonth, "Total user");
        chart.animateXY(2000,2000);
        LineData data = new LineData(lineDataSet);
        data.setValueFormatter(new CustomValueData());
        data.setValueTextSize(10);
        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);
        chart.invalidate();

        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);


        return view;
    }

    static class CustomValueData extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            int val = (int) value;
            if(val <= 1)
                return val + " user";
            else
                return val + " users";
        }
    }

    private List<Entry> getListData() {
        List<Entry> lists = new ArrayList<>();
        lists.add(new Entry(1, 1));
        lists.add(new Entry(2, 10));
        lists.add(new Entry(3, 20));
        lists.add(new Entry(4, 5));
        lists.add(new Entry(6, 10));
        lists.add(new Entry(7, 15));
        lists.add(new Entry(8, 17));
        lists.add(new Entry(9, 20));
        lists.add(new Entry(10, 21));
        lists.add(new Entry(11, 13));
        lists.add(new Entry(12, 50));
        return lists;
    }
}