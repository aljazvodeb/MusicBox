package com.feri.um.si.musicbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextGeslo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextGeslo = (EditText) findViewById(R.id.editTextGeslo);
        findViewById(R.id.textViewRegistriranje).setOnClickListener(this);
        findViewById(R.id.buttonPrijava).setOnClickListener(this);
    }


    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String geslo = editTextGeslo.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email je potreben!");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            editTextEmail.setError("Prosim napišite veljaven email!");
            editTextEmail.requestFocus();
            return;
        }


        if (geslo.isEmpty()) {
            editTextGeslo.setError("Geslo je potrebno!");
            editTextGeslo.requestFocus();
            return;
        }

        if (geslo.length() < 6) {
            editTextGeslo.setError("Geslo je prekratko! Mora imeti vsaj 6 znakov.");
            editTextGeslo.requestFocus();
            return;

        }
            mAuth.signInWithEmailAndPassword(email,geslo).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textViewRegistriranje:
            startActivity(new Intent(this,RegistracijaActivity.class));

            break;

            case R.id.buttonPrijava:
                userLogin();

                break;
        }
    }
}
