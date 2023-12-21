package com.example.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.fragments.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Fragment homeFragment;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseAuth auth;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth= FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        homeFragment = new HomeFragment();
        loadFragment();
    }

    private void loadFragment() {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container,homeFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout){

            sharedPreferences=getSharedPreferences("remember file",MODE_PRIVATE);
            editor=sharedPreferences.edit();
            editor.putBoolean("login",false);
            editor.apply();

            auth.signOut();
            startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            finish();

        }else if (id == R.id.menu_my_cart){
            startActivity(new Intent(MainActivity.this,CartActivity.class));
        }else if (id == R.id.location) {
            startActivity(new Intent(MainActivity.this, CurrentLocationActivity.class));
        }else if (id == R.id.search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }
        return true;
    }



}