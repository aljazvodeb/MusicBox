package com.feri.um.si.musicbox;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.ButterKnife;

/**
 * Created by simon on 5. 01. 2018.
 */

public class ProfilActivity extends FragmentActivity {

    TextView ime;
    Button gumb1;
    Button gumb2;
    Button gumb3;
    String emailA;

    public static final String Firebase_Server_URL = "https://insertdata-android-examples.firebaseio.com/";
    private FirebaseFirestore mFirestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(this);

        ime = (TextView) findViewById(R.id.ime_tx);
        gumb1 = (Button) findViewById(R.id.posodobi);
        gumb3 = (Button) findViewById(R.id.nazaj);

        final Context context = this;
        mFirestore = FirebaseFirestore.getInstance();
        ime.setText(getFirebaseUser().getDisplayName());

        gumb1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_spremeniemail);
                dialog.setTitle("Spremeni geslo");

                final EditText email = (EditText) dialog.findViewById(R.id.email);
                email.setText(getFirebaseUser().getEmail());

                Button gumb = (Button) dialog.findViewById(R.id.posodobi);
                gumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emailA = email.getText().toString();
                        System.out.print(emailA);
                        getFirebaseUser().updateEmail(emailA);
                        Toast.makeText(ProfilActivity.this, "Uspesno posodobljeno!",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });

        gumb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                startActivity(intent);
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
