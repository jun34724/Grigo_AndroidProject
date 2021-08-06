package com.devidea.grigoapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.devidea.grigoapplication.LoginActivity.retrofitService;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btn1, btn_board;

    private boolean isNotification = false;

    //Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("isnoti", String.valueOf(isNotification));
        if(isNotification) {
            menu.findItem(R.id.menu_alert).setIcon(R.drawable.outline_notifications_active_black_24);
        }
        else {
            menu.findItem(R.id.menu_alert).setIcon(R.drawable.outline_notifications_black_24);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //알림 확인 버튼
            case R.id.menu_alert:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                NotificationFragment notificationFragment = new NotificationFragment();
                transaction.replace(R.id.main_frame, notificationFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;

            case R.id.menu_mypage:
                //Toast.makeText(this,"설정",Toast.LENGTH_SHORT).show();
                Intent mypageIntent = new Intent(MainActivity.this, MyPageActivity.class);
                //mypageIntent.putExtra("userDataDTO",userDataDTO);
                startActivity(mypageIntent);
                return true;
            case R.id.menu_logout:
                Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //postlist -> postbody 전환을 위한 함수
    public void replaceFragment(Fragment fragment) {
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

        //app 제목 -> 추후에 app 이름 정해지면 수정
        getSupportActionBar().setTitle("Title");


        btn1 = findViewById(R.id.btn_1);
        btn_board = findViewById(R.id.btn_board);

        btn_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                BoardFragment boardFragment = new BoardFragment();
                transaction.replace(R.id.main_frame, boardFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        getNotification();
    }

    public void getNotification() {

        retrofitService.getNotification().enqueue(new Callback<ArrayList<NotificationDTO>>() {
            @Override
            public void onResponse(Call<ArrayList<NotificationDTO>> call, Response<ArrayList<NotificationDTO>> response) {
                if (!response.body().isEmpty()) {
                    Log.d("noti", "true");
                    isNotification = true;
                    invalidateOptionsMenu();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NotificationDTO>> call, Throwable t) {

            }
        });

    }
}