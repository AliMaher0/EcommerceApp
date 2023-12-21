package com.example.ecommerceapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.R;

public class PaymentActivity extends AppCompatActivity  {

    double amount = 0.0;
    Toolbar toolbar;
    TextView subTotal,discount,shipping,total;
    Button paymentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //Toolbar
        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        amount = getIntent().getDoubleExtra("amount",0.0);
        double totalPrice = getIntent().getDoubleExtra("all",0.0);
        subTotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.textView18);
        total = findViewById(R.id.total_amt);
        paymentBtn = findViewById(R.id.pay_btn);

        subTotal.setText(Double.toString(totalPrice)+"$");

        total.setText(Double.toString(totalPrice)+"$");

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PaymentActivity.this,FinalPlacedActivity.class);
                startActivity(intent);

            }
        });
    }
}