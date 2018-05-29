package com.feri.um.si.musicbox.adapterji;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.feri.um.si.musicbox.IzposojeActivity;
import com.feri.um.si.musicbox.R;
import com.feri.um.si.musicbox.modeli.Instrument;
import com.feri.um.si.musicbox.modeli.Najem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ZahtevaAdapter  extends RecyclerView.Adapter<ZahtevaAdapter.ViewHolder> {
    public List<Najem> zahtevaList;
    public List<Instrument> instrumentList;
    public FragmentManager f_manager;

    public Context context;

    public ZahtevaAdapter(Context context, List<Najem> zahtevaList, List<Instrument> instrumentList){
        this.context = context;
        this.zahtevaList = zahtevaList;
        this.instrumentList = instrumentList;
    }

    public ZahtevaAdapter(){
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zahteva, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ime.setText(instrumentList.get(position).getIme());
        holder.datum_od.setText(zahtevaList.get(position).getDatumOd());
        holder.datum_do.setText(zahtevaList.get(position).getDatumDo());
        holder.cena.setText(zahtevaList.get(position).getCenaSkupaj());
        holder.najemnik.setText(zahtevaList.get(position).getNajemnik());

        final String osebaID = zahtevaList.get(position).osebaID;

        holder.sprejmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                try {
                    db.collection("Najem")
                            .document(osebaID)
                            .update("status", "Potrjeno").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Potrjeno!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, IzposojeActivity.class);
                            context.startActivity(intent);
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Napaka!", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Napaka!", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.zavrni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                try {
                    db.collection("Najem")
                            .document(osebaID)
                            .update("status", "Zavrnjeno").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Zavrjeno!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, IzposojeActivity.class);
                            context.startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Napaka!", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Napaka!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return zahtevaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView ime;
        public TextView datum_od;
        public TextView datum_do;
        public TextView cena;
        public TextView najemnik;
        public ImageButton sprejmi;
        public ImageButton zavrni;

        public ViewHolder(View itemView){

            super(itemView);
            mView = itemView;

            ime = (TextView) mView.findViewById(R.id.vrstica_ime);
            datum_od = (TextView) mView.findViewById(R.id.vrstica_datum_od);
            datum_do = (TextView) mView.findViewById(R.id.vrstica_datum_do);
            cena = (TextView) mView.findViewById(R.id.vrstica_cena);
            najemnik = (TextView) mView.findViewById(R.id.vrstica_najemnik);
            sprejmi = (ImageButton) mView.findViewById(R.id.imageButtonSprejmi);
            zavrni = (ImageButton) mView.findViewById(R.id.imageButtonZavrni);

        }
    }
}
