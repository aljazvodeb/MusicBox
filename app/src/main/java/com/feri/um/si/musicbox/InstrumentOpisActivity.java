 package com.feri.um.si.musicbox;

 import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feri.um.si.musicbox.modeli.Instrument;
import com.google.firebase.example.musicbox.R;
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
    
     private static final String TAG="InstrumentDetail";
     public static final String KEY_INSTRUMENT_ID="key_instrument_id";

     private FirebaseFirestore mFirestore;
     private DocumentReference mInstrumentRef;
     private ListenerRegistration mInstrumentRegistration;

     @BindView(R.id.instrument_slika)
    ImageView mImageView;

     @BindView(R.id.instrument_ime)
        TextView mNameView;

     @BindView(R.id.instrument_mesto)
        TextView mCityView;

     @BindView(R.id.instrument_kategorija)
        TextView mCategoryView;

     @BindView(R.id.instrument_cena)
        TextView mPriceView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_instrument_opis);
         ButterKnife.bind(this);

         // Pridobi id instrumenta
         String instrumentId=getIntent().getExtras().getString(KEY_INSTRUMENT_ID);
         if(instrumentId==null){
         throw new IllegalArgumentException("Napaka"+KEY_INSTRUMENT_ID);
         }

         // inicializiraj firebase
         mFirestore=FirebaseFirestore.getInstance();

         // pridobi referenco na instrument preko id
         mInstrumentRef=mFirestore.collection("Instrumenti").document(instrumentId);
         }

     @OnClick(R.id.gumb_nazaj)
     public void onBackArrowClicked(View view){
             onBackPressed();
             }

     //@OnClick(R.id.kosarica)

     @Override
     public void onStart(){
             super.onStart();

         mInstrumentRegistration=mInstrumentRef.addSnapshotListener(this);
             }

    @Override
    public void onStop(){
         super.onStop();

         if(mInstrumentRegistration!=null){
             mInstrumentRegistration.remove();
             mInstrumentRegistration=null;
         }
    }

     @Override
     public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
         if (e != null) {
             Log.w(TAG, "instrument:onEvent", e);
             return;
         }
         onInstrumentNalozen(snapshot.toObject(Instrument.class));
     }
     // nalozimo podatke o instrumentu
     private void onInstrumentNalozen(Instrument instrument) {
         mNameView.setText(instrument.getIme());
         mCityView.setText(instrument.getMesto());
         mCategoryView.setText(instrument.getKategorija());
         mPriceView.setText(instrument.getCena()+"€/dan");

         Glide.with(mImageView.getContext())
                 .load(instrument.getSlika())
                 .into(mImageView);
     }
}
