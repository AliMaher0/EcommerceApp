package com.example.ecommerceapp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapters.ShowAllAdapter;
import com.example.ecommerceapp.models.ShowAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText search_box;
    Toolbar toolbar;
    private List<ShowAllModel> allModelList;
    private RecyclerView searchRecyclerView;
    private ShowAllAdapter allAdapter;
    Button camera , mic;

    String voiceString = "kids";
    //FireStore
    FirebaseFirestore db;

    ActivityResultLauncher<ScanOptions> bar_code_scanner=registerForActivityResult(new ScanContract(), result ->
    {
        if(result.getContents() != null)
        {
            androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(SearchActivity.this);
            alert.setTitle("Result");
            alert.setMessage(result.getContents());
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    String barcode_txt = result.getContents();
                    if (!barcode_txt.isEmpty()) {
                        db.collection("ShowAll").whereEqualTo("barCode", barcode_txt).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful() && task.getResult() != null) {
                                            allModelList.clear();
                                            allAdapter.notifyDataSetChanged();
                                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                                ShowAllModel model = doc.toObject(ShowAllModel.class);
                                                allModelList.add(model);
                                                allAdapter.notifyDataSetChanged();

                                            }
                                        }
                                    }
                                });

                    }
                }
            }).show();
        }

    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        db = FirebaseFirestore.getInstance();

        ////Search view
        searchRecyclerView = findViewById(R.id.search_rec);
        search_box = findViewById(R.id.search_box);
        allModelList = new ArrayList<>();
        allAdapter = new ShowAllAdapter(this, allModelList);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setAdapter(allAdapter);
        searchRecyclerView.setHasFixedSize(true);
        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    allModelList.clear();
                    allAdapter.notifyDataSetChanged();
                } else {
                    searchProduct(editable.toString());
                }

            }
        });

        mic = findViewById(R.id.voiceSearch);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchVoiceRecord();
            }
        });

        camera = findViewById(R.id.cameraSearch);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchByBarCode();
            }
        });


    }

    private void searchByBarCode() {
        ScanOptions options =new ScanOptions();
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(BarScanner.class);
        bar_code_scanner.launch(options);

    }

    private void searchProduct(String type) {
        if (!type.isEmpty()) {
            db.collection("ShowAll").whereEqualTo("type", type).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                allModelList.clear();
                                allAdapter.notifyDataSetChanged();
                                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                    ShowAllModel model = doc.toObject(ShowAllModel.class);
                                    allModelList.add(model);
                                    allAdapter.notifyDataSetChanged();

                                }
                            }
                        }
                    });
        }
    }

    private void searchVoiceRecord() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        try {
            startActivityForResult(intent, 3000);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(SearchActivity.this, "something is wrong", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&data!=null && requestCode == 3000)
        {
            ArrayList<String> text=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Toast.makeText(SearchActivity.this,text.get(0),Toast.LENGTH_SHORT).show();
            voiceString=text.get(0);
            searchProduct(voiceString);
        }
    }
}