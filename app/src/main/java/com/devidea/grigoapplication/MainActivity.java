package com.devidea.grigoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.jetbrains.annotations.NotNull;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    public static Context mContext;
    public static NotificationModel notificationModel = new NotificationModel();

    //Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        notificationModel.getNotification();
        Log.d("noti",  String.valueOf(notificationModel.getNotificationProperty()));
        if (notificationModel.getNotificationProperty()) {
            menu.findItem(R.id.menu_alert).setIcon(R.drawable.outline_notifications_active_black_24);
        } else {
            menu.findItem(R.id.menu_alert).setIcon(R.drawable.outline_notifications_black_24);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //알림 확인 버튼
            case R.id.menu_alert:
                //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                NotificationFragment notificationFragment = new NotificationFragment();
                replaceFragment(notificationFragment);
                return true;

            case R.id.menu_mypage:
                Intent mypageIntent = new Intent(MainActivity.this, MyPageActivity.class);
                startActivity(mypageIntent);
                return true;
            case R.id.menu_logout:
                Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show();
                retrofitService = null;
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //fragment 전환 함수
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        try {
            if(!fragment.getClass().toString().equals((fragmentManager.findFragmentById(R.id.main_frame)).getClass().toString())){
                fragmentTransaction.add(R.id.main_frame, fragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.show(fragment);
                fragmentTransaction.hide(fragmentManager.findFragmentById(R.id.main_frame));

                fragmentTransaction.commit();
            }

            else{
                Log.d("fragmentTransaction", "skip");
            }

        } catch (Exception e) {
            e.printStackTrace();
            fragmentTransaction.replace(R.id.main_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        invalidateOptionsMenu();
    }


    //fragment 전환 함수
    public void replaceNotifyFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //app 제목 -> 추후에 app 이름 정해지면 수정
        getSupportActionBar().setTitle("Title");

        CalendarFragment calendarFragment = new CalendarFragment();
        BoardFragment boardFragment = new BoardFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, calendarFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.calender:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, calendarFragment).commit();
                        break;

                    case R.id.board:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, boardFragment).commit();

                        break;
                }
                return true;
            }
        });

    }
}