
package com.feri.um.si.musicbox;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.feri.um.si.musicbox.modeli.Instrument;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;

/**
 * Created by simon on 20. 01. 2018.
 */

public class NajemActivity extends AppCompatActivity {
    Button potrdi, btnDatum, izberidatum1, izberidatum2;
    EditText datum_od, datum_do, najemodajalec, cenadan, cenaskupaj;
    public static final String Firebase_Server_URL = "https://insertdata-android-examples.firebaseio.com/";
    String datumodA, datumdoA;
    int cenadanint;

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
        mFirestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        najemodajalec.setText(intent.getStringExtra("najemodajalec"));
        cenadanint = intent.getIntExtra("cenadan",0);
        cenadan.setText(cenadanint+" €");
        final Instrument instrument = new Instrument();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            Calendar cal = Calendar.getInstance();

            private void updateLabel() {
                String myFormat = "dd.MM.yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                if (datum_od.getText().toString().equals("")) {
                    datum_od.setText(sdf.format(cal.getTime()));
                    datumodA = sdf.format(cal.getTime());
                    if (!datum_od.getText().toString().equals("") && !datum_do.getText().toString().equals("")) {
                        String add = datum_od.getText().toString();
                        try {
                            Date datum1 = sdf.parse(datumodA);
                            Date datum2 = sdf.parse(datumdoA);
                            long razl = datum2.getTime()-datum1.getTime();
                            cenaskupaj.setText(TimeUnit.DAYS.convert(razl, TimeUnit.MILLISECONDS)*cenadanint+" €");
                            if (TimeUnit.DAYS.convert(razl, TimeUnit.MILLISECONDS)<0) {
                                Toast.makeText(NajemActivity.this, "Napaka v datumu!", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    datum_do.setText(sdf.format(cal.getTime()));
                    datumdoA = sdf.format(cal.getTime());
                    if (!datum_od.getText().toString().equals("") && !datum_do.getText().toString().equals("")) {
                        try {
                            Date datum1 = sdf.parse(datumodA);
                            Date datum2 = sdf.parse(datumdoA);
                            long razl = datum2.getTime()-datum1.getTime();
                            cenaskupaj.setText(TimeUnit.DAYS.convert(razl, TimeUnit.MILLISECONDS)*cenadanint+" €");
                            if (TimeUnit.DAYS.convert(razl, TimeUnit.MILLISECONDS)<0) {
                                Toast.makeText(NajemActivity.this, "Napaka v datumu!", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
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
                Calendar cal = Calendar.getInstance();
                new DatePickerDialog(NajemActivity.this, date, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        izberidatum2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                new DatePickerDialog(NajemActivity.this, date, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }
}
