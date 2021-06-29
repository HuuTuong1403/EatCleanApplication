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
import android.widget.Toast;

import com.example.eatcleanapp.API.APIService;
import com.example.eatcleanapp.CustomAlert.CustomAlertActivity;
import com.example.eatcleanapp.R;
import com.example.eatcleanapp.ui.quantrivien.AdminActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticAdminBlogFragment extends Fragment {

    private View view;
    private BarChart chart;
    private EditText statistic_edt_chooseYear_Blog_barChart;
    private AdminActivity adminActivity;
    private List<BarEntry> totalBlogForeachMonth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adminActivity = (AdminActivity) getActivity();
        view = inflater.inflate(R.layout.fragment_statistic_admin_blog, container, false);

        Mapping();

        statistic_edt_chooseYear_Blog_barChart.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    totalBlogForeachMonth.clear();
                    if(statistic_edt_chooseYear_Blog_barChart.getText().toString().isEmpty()){
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
                        int inputYear = Integer.parseInt(statistic_edt_chooseYear_Blog_barChart.getText().toString());
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
                            getData(statistic_edt_chooseYear_Blog_barChart.getText().toString());
                            statistic_edt_chooseYear_Blog_barChart.setText("");
                        }
                    }
                    Mapping();
                }

                return false;
            }
        });

        return view;
    }

    private void Mapping() {
        chart = view.findViewById(R.id.statistic_blog_barChart);
        statistic_edt_chooseYear_Blog_barChart = view.findViewById(R.id.statistic_edt_chooseYear_Blog_barChart);
        totalBlogForeachMonth = new ArrayList<>();
        for(int i = 0; i < 12; i++) {
            totalBlogForeachMonth.add(i, new BarEntry(i, 0));
        }
    }

    private void getData(String Year){
        APIService.apiService.getBlogMonth(Year).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                List<BarEntry> list = new ArrayList<>();
                JsonArray jsonArray = response.body();
                for(int i = 0; i < jsonArray.size(); i++){
                    JsonObject object = jsonArray.get(i).getAsJsonObject();
                    String objectCount = object.get("count").getAsString() ;
                    float count = Float.parseFloat(objectCount);
                    String objectmonth = object.get("month").getAsString();
                    float month = Float.parseFloat(objectmonth);
                    list.add(new BarEntry(month, count));
                }

                for(int i = 0; i < list.size(); i++) {
                    totalBlogForeachMonth.remove((int)list.get(i).getX());
                    totalBlogForeachMonth.add((int)list.get(i).getX(), new BarEntry(list.get(i).getX(), list.get(i).getY()));
                }

                chart.setTouchEnabled(true);
                chart.setDragEnabled(true);
                chart.setScaleEnabled(true);
                chart.setPinchZoom(false);

                XAxis xAxis = chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(false);
                xAxis.setCenterAxisLabels(true);
                xAxis.setEnabled(true);

                List<String> xAxisValues = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6","7", "8", "9", "10", "11", "12"));
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));

                BarDataSet barDataSet = new BarDataSet(totalBlogForeachMonth, "Total Blg");
                chart.animateXY(2000,2000);
                BarData data = new BarData(barDataSet);

                data.setValueFormatter(new DefaultValueFormatter(0));
                data.setValueTextSize(10);
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                chart.setData(data);
                chart.invalidate();

                chart.getLegend().setEnabled(false);
                chart.getDescription().setEnabled(false);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                CustomAlertActivity customAlertActivity = new CustomAlertActivity.Builder()
                        .setActivity(adminActivity)
                        .setTitle("Thông báo")
                        .setMessage("Đã xảy ra lỗi khi lấy dữ liệu")
                        .setType("error")
                        .Build();
                customAlertActivity.showDialog();
            }
        });
    }

}