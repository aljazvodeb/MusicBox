package com.feri.um.si.musicbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.feri.um.si.musicbox.modeli.Instrument;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by simon on 5. 01. 2018.
 */

public class DodajActivity extends AppCompatActivity {

    Button dodaj, preklici;
    EditText ime, opis, url, cena;
    Spinner mesto, kategorija, stanje;
    public static final String Firebase_Server_URL = "https://insertdata-android-examples.firebaseio.com/";
    String imeA, opisA, urlA, mestoA, kategorijaA, stanjeA, najemodajalecA;
    int cenaA;

    private FirebaseFirestore mFirestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj);
        ButterKnife.bind(this);

        dodaj = (Button) findViewById(R.id.dodaj);
        ime = (EditText) findViewById(R.id.ime_et);
        opis = (EditText) findViewById(R.id.opis_et);
        url = (EditText) findViewById(R.id.url_et);
        cena = (EditText) findViewById(R.id.cena_et);
        mesto = (Spinner) findViewById(R.id.mesto_sp);
        kategorija = (Spinner) findViewById(R.id.kategorija_sp);
        stanje = (Spinner) findViewById(R.id.stanje_sp);

        mFirestore = FirebaseFirestore.getInstance();

        final Instrument instrument = new Instrument();

        dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imeA = ime.getText().toString();
                instrument.setIme(imeA);
                opisA = opis.getText().toString();
                instrument.setOpis(opisA);
                urlA = url.getText().toString();
                instrument.setSlika(urlA);
                mestoA = mesto.getSelectedItem().toString();
                instrument.setMesto(mestoA);
                kategorijaA=kategorija.getSelectedItem().toString();
                instrument.setKategorija(kategorijaA);
                stanjeA=stanje.getSelectedItem().toString();
                instrument.setStanje(stanjeA);
                cenaA= Integer.parseInt(cena.getText().toString());
                instrument.setCena(cenaA);
                instrument.setNajemodajalec(getFirebaseUser().getDisplayName());
                najemodajalecA = instrument.getNajemodajalec();

                Map<String, Object> instrumentiMap = new HashMap<>();
                instrumentiMap.put("ime", imeA);
                instrumentiMap.put("opis", opisA);
                instrumentiMap.put("slika", urlA);
                instrumentiMap.put("mesto", mestoA);
                instrumentiMap.put("kategorija", kategorijaA);
                instrumentiMap.put("stanje", stanjeA);
                instrumentiMap.put("cena", cenaA);
                instrumentiMap.put("najemodajalec", najemodajalecA);


                if (cena.getText().toString().isEmpty() || ime.getText().toString().isEmpty() || opis.getText().toString().isEmpty() || url.getText().toString().isEmpty()) {
                    Toast.makeText(DodajActivity.this, "Niste napolnili vseh polj. Dodajanje instrumenta neuspesno.", Toast.LENGTH_SHORT).show();
                }
                if (Integer.parseInt(cena.getText().toString()) > 100) {
                    Toast.makeText(DodajActivity.this, "Napaka cena je prevelika!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mFirestore.collection("Instrumenti").add(instrumentiMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(DodajActivity.this, "Instrument uspesno dodan!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String napaka = e.getMessage();
                            Toast.makeText(DodajActivity.this, "Napaka:" + napaka, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
