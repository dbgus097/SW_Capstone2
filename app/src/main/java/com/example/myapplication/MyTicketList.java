package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;

//import static android.app.PendingIntent.getActivity;

public class MyTicketList extends AppCompatActivity {

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();

    // 4개의 메뉴에 들어갈 Fragment들
    private FragmentTicketbox fragmentTicketbox = new FragmentTicketbox();
    private FragmentNewticket fragmentNewticket = new FragmentNewticket();
    private FragmentStatistics fragmentStatistics = new FragmentStatistics();
    private FragmentSettings fragmentSettings = new FragmentSettings();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_list);

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNavigationView = (com.google.android.material.bottomnavigation.BottomNavigationView) findViewById(R.id.navigationView);

        // BottomNavigationView 메뉴를 선택할 때마다 위치가 변하지 않도록
        BottomNavigationView.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragmentTicketbox).commitAllowingStateLoss();


        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.ticketbox: {
                        transaction.replace(R.id.frame_layout, fragmentTicketbox).commitAllowingStateLoss();
                        break;
                    }

                    case R.id.newticket: {
                        transaction.replace(R.id.frame_layout, fragmentNewticket).commitAllowingStateLoss();
                        break;
                    }

                    case R.id.statistics: {
                        transaction.replace(R.id.frame_layout, fragmentStatistics).commitAllowingStateLoss();
                        break;
                    }

                    case R.id.settings: {
                        transaction.replace(R.id.frame_layout, fragmentSettings).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }
}
