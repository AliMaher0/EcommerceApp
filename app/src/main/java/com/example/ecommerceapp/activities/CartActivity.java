package com.example.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapters.MyCartAdapter;
import com.example.ecommerceapp.models.MyCartModel;
import com.example.ecommerceapp.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity /*implements PaymentResultListener*/ {
    double amount=0.0;
    int totalBill;
    TextView overAllAmount;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<MyCartModel> cartModelList;
    MyCartAdapter myCartAdapter;
    Button buyNowBtn, editItem;


    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.my_cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });


        //get data from my cart adapter
        overAllAmount = findViewById(R.id.textView3);
        recyclerView = findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartModelList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(this , cartModelList);
        recyclerView.setAdapter(myCartAdapter);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot doc : task.getResult().getDocuments()){
                                String documentId = doc.getId();
                                MyCartModel myCartModel = doc.toObject(MyCartModel.class);
                                myCartModel.setDocumentId(documentId);

                                int totalQuantity = 0;
                                boolean found = false;
                                for (MyCartModel cartObj: cartModelList) {
                                    if (cartObj.getProductName().equals(myCartModel.getProductName())){
                                        totalQuantity = Integer.parseInt(cartObj.getTotalQuantity()) ;
                                        totalQuantity += Integer.parseInt(myCartModel.getTotalQuantity()) ;
                                        cartObj.setTotalQuantity(Integer.toString(totalQuantity));
                                        found = true;
                                    }
                                }
                                if (found !=true) {
                                    cartModelList.add(myCartModel);
                                    myCartAdapter.notifyDataSetChanged();
                                }else{
                                    found = false;
                                }
                            }

                            calculateTotalAmount(cartModelList);
                        }
                    }
                });
       // amount = getIntent().getDoubleExtra("totalAmount",0.0);

         buyNowBtn = findViewById(R.id.buy_now);
         buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CartActivity.this,PlacedOrdersActivity.class);
                intent.putExtra("itemList" , (Serializable) cartModelList);
                startActivity(intent);

            }
        });

    }

    private void calculateTotalAmount(List<MyCartModel> cartModelList) {
        double totalAmount = 0.0;
        for (MyCartModel myCartModel :cartModelList){
            totalAmount += myCartModel.getTotalPrice();
        }
        overAllAmount.setText("Total Amount : "+totalAmount);
    }
}