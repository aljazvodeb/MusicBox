package com.feri.um.si.musicbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegistracijaActivity extends AppCompatActivity implements View.OnClickListener {


    EditText.editTextUporabniškoIme, editTextGeslo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);
    }


    private  void registerUser(){


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewPrijavljanje:
            registerUser();
            break;


            case R.id.textViewPrijavljanje ;
                startActivity(new Intent(this,PrijavaActivity.class));
        }
    }
}
