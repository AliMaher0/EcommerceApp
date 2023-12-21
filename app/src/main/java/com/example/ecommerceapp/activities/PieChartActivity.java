package com.example.ecommerceapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.models.MyCartModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity {

    PieChart pieChart;
    List<MyCartModel> list;
    FirebaseAuth auth;
    FirebaseFirestore firestore;


    // Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

//        toolbar = findViewById(R.id.pieChart_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//
//        });
         list = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList");

        pieChart = findViewById(R.id.pie_chart);

        ArrayList<PieEntry> records = new ArrayList<>();
        String name= "";
        int quantity = 0;
        for (MyCartModel oneItem: list) {
          name =  oneItem.getProductName();
          quantity = Integer.parseInt(oneItem.getTotalQuantity());
         //   Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            records.add(new PieEntry(quantity,name));
        }
//        records.add(new PieEntry(32,"Shoes"));
//        records.add(new PieEntry(14,"Watches"));
//        records.add(new PieEntry(34,"Women pent shirt"));
//        records.add(new PieEntry(38,"Kid wear"));

        PieDataSet dataSet = new PieDataSet(records , "Pie chart report");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(22f);
        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(true);
        pieChart.setCenterText("Quarterly Revenue");
        pieChart.animate();
    }
}