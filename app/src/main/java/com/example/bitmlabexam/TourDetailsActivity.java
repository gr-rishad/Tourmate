package com.example.bitmlabexam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TourDetailsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    public static  String tName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details);

        replaceFragment(new WalletFragment());

        init();
    }

    private void init() {

        //

       tName=  getIntent().getStringExtra("tripName");

        System.out.println("============="+tName);




        bottomNavigationView = findViewById(R.id.bottomNavigationMenuId);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                   case  R.id.walletMenuId :
                       replaceFragment(new WalletFragment());
                       return true;

                    case R.id.memoriesMenuId:
                        replaceFragment(new MemoriesFragment());
                        return true;
                }
                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment){

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutId,fragment).addToBackStack(null).commit();
//        transaction.replace(R.id.frameLayoutId,fragment).;
//        transaction.commit();
    }
}
