package com.fr.codage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void atbash(View v){
        Intent otherActivity = new Intent(getApplicationContext(), AtbashActivity.class);
        startActivity(otherActivity);
    }
}
