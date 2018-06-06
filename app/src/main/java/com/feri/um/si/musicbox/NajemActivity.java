
package com.feri.um.si.musicbox;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.feri.um.si.musicbox.modeli.Najem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;

/**
 * Created by simon on 20. 01. 2018.
 */

public class NajemActivity extends AppCompatActivity {
    Button potrdi, izberidatum1, izberidatum2;
    EditText datum_od, datum_do, najemodajalec, cenadan, cenaskupaj, naziv, status;
    public static final String Firebase_Server_URL = "https://insertdata-android-examples.firebaseio.com/";
    String datumodA, datumdoA, najemodajalecA, najemnikA, nazivA, cenaskupajA, statusA;
    int cenadanint;
    private String zac;

    private FirebaseFirestore mFirestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_najem);
        ButterKnife.bind(this);

        potrdi = (Button) findViewById(R.id.potrdi);
        datum_od = (EditText) findViewById(R.id.od_datum);
        datum_do = (EditText) findViewById(R.id.do_datum);
        najemodajalec = (EditText) findViewById(R.id.najemodajalec_et);
        cenadan = (EditText) findViewById(R.id.et_cenadan);
        cenaskupaj = (EditText) findViewById(R.id.skupaj);
        izberidatum1 = (Button) findViewById(R.id.btn_date1);
        izberidatum2 = (Button) findViewById(R.id.btn_date2);
        naziv = (EditText) findViewById(R.id.et_nazivin);
        mFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        najemodajalec.setText(intent.getStringExtra("najemodajalec"));
        cenadanint = intent.getIntExtra("cenadan",0);
        nazivA = intent.getStringExtra("naziv");
        naziv.setText(nazivA);
        cenadan.setText(cenadanint+" €");

        final Najem najem = new Najem();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            Calendar cal = Calendar.getInstance();

            private void updateLabel() {
                String myFormat = "dd.MM.yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                if (zac.equals("A")) {
                    datum_od.setText(sdf.format(cal.getTime()));
                    datumodA = sdf.format(cal.getTime());
                        try {
                            Date datum1 = sdf.parse(datumodA);
                            if (!datum_do.getText().toString().equals("")) {
                                Date datum2 = sdf.parse(datumdoA);
                                long razl = datum2.getTime() - datum1.getTime();
                                cenaskupaj.setText(TimeUnit.DAYS.convert(razl, TimeUnit.MILLISECONDS) * cenadanint + " €"); //izracunamo ceno
                            }
                            }catch (Exception e) {
                            e.printStackTrace();
                        }
                } else {
                    datum_do.setText(sdf.format(cal.getTime()));
                    datumdoA = sdf.format(cal.getTime());
                        try {
                            Date datum1 = sdf.parse(datumodA);
                            Date datum2 = sdf.parse(datumdoA);
                            long razl = datum2.getTime()-datum1.getTime();
                            cenaskupaj.setText(TimeUnit.DAYS.convert(razl, TimeUnit.MILLISECONDS)*cenadanint+" €");
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        izberidatum1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zac = "A";
                Calendar cal = Calendar.getInstance();
                DatePickerDialog dpDialog = new DatePickerDialog(NajemActivity.this, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dpDialog.getDatePicker().setMinDate(cal.getTimeInMillis()); // onemogocimo izbiro zadnjih datumov
                dpDialog.show();
            }
        });

        izberidatum2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zac = "B";
                Calendar cal = Calendar.getInstance();
                DatePickerDialog dpDialog = new DatePickerDialog(NajemActivity.this, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dpDialog.getDatePicker().setMinDate(cal.getTimeInMillis()); // onemogocimo izbiro zadnjih datumov
                dpDialog.show();
            }
        });
        potrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!datum_od.getText().toString().equals("") && !datum_do.getText().toString().equals(""))  {
                    String myFormat = "dd.MM.yyyy";
                    long razl = 0;
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    try {
                        Date datum1 = sdf.parse(datumodA);
                        Date datum2 = sdf.parse(datumdoA);
                        razl = datum2.getTime()-datum1.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (TimeUnit.DAYS.convert(razl, TimeUnit.MILLISECONDS)> 0) {
                        datumodA = datum_od.getText().toString();
                        najem.setDatumOd(datumodA);
                        datumdoA = datum_do.getText().toString();
                        najem.setDatumDo(datumdoA);
                        najemodajalecA = najemodajalec.getText().toString();
                        najem.setNajemodajalec(najemodajalecA);
                        cenaskupajA = cenaskupaj.getText().toString();
                        najem.setCenaSkupaj(cenaskupajA);
                        najem.setNajemnik(getFirebaseUser().getDisplayName());
                        najemnikA = najem.getNajemnik();
                        najem.setStatus("Zahteva");
                        statusA = najem.getStatus();

                        Map<String, Object> najemMap = new HashMap<>();
                        najemMap.put("datumOd", datumodA);
                        najemMap.put("datumDo", datumdoA);
                        najemMap.put("cenaDan", cenadanint);
                        najemMap.put("cenaSkupaj", cenaskupajA);
                        najemMap.put("ime", nazivA);
                        najemMap.put("najemnik", najemnikA);
                        najemMap.put("najemodajalec", najemodajalecA);
                        najemMap.put("status", statusA);

                        mFirestore.collection("Najem").add(najemMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(NajemActivity.this, "Zahteva je bila poslana!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                String napaka = e.getMessage();
                                Toast.makeText(NajemActivity.this, "Napaka:" + napaka, Toast.LENGTH_SHORT).show();
                            }
                        });
                } else {
                    Toast.makeText(NajemActivity.this, "Napaka v datumu! Preverite in poskusite ponovno!", Toast.LENGTH_SHORT).show();
                }
            } else {
                    Toast.makeText(NajemActivity.this, "Napaka v datumu! Preverite in poskusite ponovno!", Toast.LENGTH_SHORT).show();
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
