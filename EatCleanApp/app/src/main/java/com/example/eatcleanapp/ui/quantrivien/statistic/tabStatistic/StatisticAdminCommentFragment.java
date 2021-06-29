package com.example.eatcleanapp.ui.quantrivien.statistic.tabStatistic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class StatisticAdminCommentFragment extends Fragment {

    private View view;
    private BarChart chart;
    private EditText statistic_edt_chooseYear_barChart;
    private AdminActivity adminActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adminActivity = (AdminActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_statistic_admin_comment, container, false);
        chart = view.findViewById(R.id.statistic_comment_barChart);
        statistic_edt_chooseYear_barChart = view.findViewById(R.id.statistic_edt_chooseYear_barChart);

        statistic_edt_chooseYear_barChart.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(statistic_edt_chooseYear_barChart.getText().toString().isEmpty()){
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
                        int inputYear = Integer.parseInt(statistic_edt_chooseYear_barChart.getText().toString());
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
                            statistic_edt_chooseYear_barChart.setText("");
                        }
                    }
                }
                return false;
            }
        });

        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(false);

        List<BarEntry> totalUserForeachMonth = getListData();

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);

        List<String> xAxisValues = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6","7", "8", "9", "10", "11", "12"));
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));

        BarDataSet barDataSet = new BarDataSet(totalUserForeachMonth, "Total user");
        chart.animateXY(2000,2000);
        BarData data = new BarData(barDataSet);
        data.setValueFormatter(new CustomValueData());
        data.setValueTextSize(10);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
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
                return val + " comment";
            else
                return val + " comments";
        }
    }

    private List<BarEntry> getListData() {
        List<BarEntry> lists = new ArrayList<>();
        lists.add(new BarEntry(1, 1));
        lists.add(new BarEntry(2, 10));
        lists.add(new BarEntry(3, 20));
        lists.add(new BarEntry(4, 5));
        lists.add(new BarEntry(6, 10));
        lists.add(new BarEntry(7, 15));
        lists.add(new BarEntry(8, 17));
        lists.add(new BarEntry(9, 20));
        lists.add(new BarEntry(10, 21));
        lists.add(new BarEntry(11, 13));
        lists.add(new BarEntry(12, 50));
        return lists;
    }
}