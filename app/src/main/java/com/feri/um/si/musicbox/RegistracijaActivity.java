package com.feri.um.si.musicbox;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class RegistracijaActivity extends AppCompatActivity implements View.OnClickListener {


    EditText editTextEmail, editTextGeslo;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextGeslo = (EditText) findViewById(R.id.editTextGeslo);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.textViewPrijavljanje).setOnClickListener(this);
        findViewById(R.id.buttonRegistracija).setOnClickListener(this);
    }


    private  void registerUser(){
            String email = editTextEmail.getText().toString().trim();
            String geslo = editTextGeslo.getText().toString().trim();

            if(email.isEmpty()){
                editTextEmail.setError("Email je potreben!");
                editTextEmail.requestFocus();
                return;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                editTextEmail.setError("Prosim napišite veljaven email!");
                editTextEmail.requestFocus();
                return;
            }


        if(geslo.isEmpty()){
            editTextGeslo.setError("Geslo je potrebno!");
            editTextGeslo.requestFocus();
            return;
        }

        if(geslo.length() < 6){
            editTextGeslo.setError("Geslo je prekratko! Mora imeti vsaj 6 znakov.");
            editTextGeslo.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email,geslo).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(RegistracijaActivity.this, ProfilActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"Ste že registrirani.", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonRegistracija:
            registerUser();

            break;


            case R.id.textViewPrijavljanje:
                startActivity(new Intent(this,MainActivity.class));

                break;
        }
    }
}