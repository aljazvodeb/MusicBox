 package com.feri.um.si.musicbox;

 import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feri.um.si.musicbox.modeli.Instrument;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

 /**
  * Created by simon on 5. 01. 2018.
  */

public class InstrumentOpisActivity extends AppCompatActivity implements EventListener<DocumentSnapshot> {

     private static final String TAG = "InstrumentDetail";
     public static final String KEY_INSTRUMENT_ID = "key_instrument_id";
     private FirebaseFirestore mFirestore;
     private DocumentReference mInstrumentRef;
     private ListenerRegistration mInstrumentRegistration;

     @BindView(R.id.instrument_slika)
     ImageView mSlikaView;

     @BindView(R.id.instrument_ime)
     TextView mNameView;

     @BindView(R.id.instrument_mesto)
     TextView mMestoView;

     @BindView(R.id.instrument_kategorija)
     TextView mKategorijaView;

     @BindView(R.id.cena_et)
     TextView mCenaView;

     @BindView(R.id.opis_et)
     TextView mOpisView;

     @BindView(R.id.stanje_et)
     TextView mStanjeView;

     @BindView(R.id.najemodajalec_et)
     TextView mNajemodajalecView;

     @BindView(R.id.chat)
     FloatingActionButton mChatButton;

     @BindView(R.id.kosarica)
     FloatingActionButton mKosaricaButton;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_instrument_opis);
         ButterKnife.bind(this);

         // Pridobi id instrumenta
         String instrumentId = getIntent().getExtras().getString(KEY_INSTRUMENT_ID);
         if (instrumentId == null) {
             throw new IllegalArgumentException("Napaka" + KEY_INSTRUMENT_ID);
         }

         // inicializiraj firebase
         mFirestore = FirebaseFirestore.getInstance();

         // pridobi referenco na instrument preko id
         mInstrumentRef = mFirestore.collection("Instrumenti").document(instrumentId);
         //button s povezavo do chata
         mChatButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                 startActivity(intent);
             }
         });
     }

     @OnClick(R.id.gumb_nazaj)
     public void onBackArrowClicked(View view) {
         onBackPressed();
     }




     @Override
     public void onStart() {
         super.onStart();
         mInstrumentRegistration = mInstrumentRef.addSnapshotListener(this);
     }

     @Override
     public void onStop() {
         super.onStop();

         if (mInstrumentRegistration != null) {
             mInstrumentRegistration.remove();
             mInstrumentRegistration = null;
         }
     }

     @Override
     public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
         if (e != null) {
             Log.w(TAG, "instrument:onEvent", e);
             return;
         }
         onInstrumentNalozen(snapshot.toObject(Instrument.class));
         onNajem(snapshot.toObject(Instrument.class));
     }

     // nalozimo podatke o instrumentu
     private void onInstrumentNalozen(Instrument instrument) {
         mNameView.setText(instrument.getIme());
         mMestoView.setText(instrument.getMesto());
         mKategorijaView.setText(instrument.getKategorija());
         mCenaView.setText(instrument.getCena() + "€/dan");
         mOpisView.setText(instrument.getOpis());
         mStanjeView.setText(instrument.getStanje());
         mNajemodajalecView.setText(instrument.getNajemodajalec());
         Glide.with(mSlikaView.getContext())
                 .load(instrument.getSlika())
                 .into(mSlikaView);
     }

         private void onNajem(final Instrument instrument) {
             mKosaricaButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Intent intent = new Intent(getApplicationContext(), NajemActivity.class);
                     Bundle extras = new Bundle();
                     extras.putString("najemodajalec",instrument.getNajemodajalec());
                     extras.putInt("cenadan",instrument.getCena());
                     extras.putString("naziv",instrument.getIme());
                     intent.putExtras(extras);
                     startActivity(intent);
                 }
             });
         }
 }
