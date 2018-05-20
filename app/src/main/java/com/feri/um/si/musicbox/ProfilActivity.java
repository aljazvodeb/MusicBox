package com.feri.um.si.musicbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.ButterKnife;

/**
 * Created by simon on 5. 01. 2018.
 */

public class ProfilActivity extends AppCompatActivity {

    EditText ime;
    EditText email;
    Button gumb;
    String emailA;

    public static final String Firebase_Server_URL = "https://insertdata-android-examples.firebaseio.com/";

    private FirebaseFirestore mFirestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(this);

        ime = (EditText) findViewById(R.id.ime_et);
        email = (EditText) findViewById(R.id.email_et);
        gumb = (Button) findViewById(R.id.posodobi);


        mFirestore = FirebaseFirestore.getInstance();
        ime.setText(getFirebaseUser().getDisplayName());
        email.setText(getFirebaseUser().getEmail());

        gumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailA = email.getText().toString();
                getFirebaseUser().updateEmail(emailA).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProfilActivity.this, "Posodobljeno!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String napaka = e.getMessage();
                        Toast.makeText(ProfilActivity.this, "Napaka:" + napaka, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    public static FirebaseUser getFirebaseUser()
    {
        return getFirebaseAuth().getCurrentUser();
    }

    public static FirebaseAuth getFirebaseAuth()
    {
        return FirebaseAuth.getInstance();
    }
}
