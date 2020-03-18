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

    public void cesar(View v){
        Intent otherActivity = new Intent(getApplicationContext(), CesarActivity.class);
        startActivity(otherActivity);
    }

    public void vigenere(View v){
        Intent otherActivity = new Intent(getApplicationContext(), VigenereActivity.class);
        startActivity(otherActivity);
    }

    public void homophone(View v){
        Intent otherActivity = new Intent(getApplicationContext(), HomophoneActivity.class);
        startActivity(otherActivity);
    }

    public void playfair(View v){
        Intent otherActivity = new Intent(getApplicationContext(), PlayfairActivity.class);
        startActivity(otherActivity);
    }

    public void hill(View v){
        Intent otherActivity = new Intent(getApplicationContext(), HillActivity.class);
        startActivity(otherActivity);
    }

    public void rectTranspo(View v){
        Intent otherActivity = new Intent(getApplicationContext(), RectTranspoActivity.class);
        startActivity(otherActivity);
    }

    public void des(View v){
        Intent otherActivity = new Intent(getApplicationContext(), DESActivity.class);
        startActivity(otherActivity);
    }

    public void myEncryption(View v){
        Intent otherActivity = new Intent(getApplicationContext(), MyEncryptionActivity.class);
        startActivity(otherActivity);
    }
}
