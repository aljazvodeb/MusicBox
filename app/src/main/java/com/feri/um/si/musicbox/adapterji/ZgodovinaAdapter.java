package com.feri.um.si.musicbox.adapterji;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.feri.um.si.musicbox.ChatActivity;
import com.feri.um.si.musicbox.R;
import com.feri.um.si.musicbox.modeli.Instrument;
import com.feri.um.si.musicbox.modeli.Najem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ZgodovinaAdapter extends RecyclerView.Adapter<ZgodovinaAdapter.ViewHolder> {

    private final static String TAG = "ZGODOVINA";

    public List<Najem> zgodovinaList;
    public List<Instrument> instrumentList;
    public Context context;

    public ZgodovinaAdapter(Context context, List<Najem> zgodovinaList, List <Instrument> instrumentList){
        this.zgodovinaList = zgodovinaList;
        this.instrumentList=instrumentList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zgodovina, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.ime.setText(instrumentList.get(position).getIme());
        holder.najemodajalec.setText(zgodovinaList.get(position).getNajemodajalec());
        holder.datumOd.setText(zgodovinaList.get(position).getDatumOd());
        holder.datumDo.setText(zgodovinaList.get(position).getDatumDo());
        holder.skupajCena.setText(zgodovinaList.get(position).getCenaSkupaj());
        final String osebaID = zgodovinaList.get(position).osebaID;

        holder.gumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Najem").document(osebaID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String najemodajalec = documentSnapshot.getString("najemodajalec");
                            String ime = documentSnapshot.getString("ime");
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("najemodajalec", najemodajalec);
                            intent.putExtra("glasbilo", ime);
                            context.startActivity(intent);
                        }
                        else {
                            Log.d(TAG, "Dokument ne obstsaja!");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                    }
                });

            }});
    }

    @Override
    public int getItemCount() {

        return zgodovinaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView ime;
        public TextView najemodajalec;
        public TextView datumOd;
        public TextView datumDo;
        public TextView skupajCena;
        public ImageButton gumb;

        public ViewHolder(View itemView){

            super(itemView);
            mView = itemView;
            
            ime = (TextView) mView.findViewById(R.id.vrstica_ime);
            najemodajalec = (TextView) mView.findViewById(R.id.vrstica_najemodajalec);
            datumOd = (TextView) mView.findViewById(R.id.vrstica_datum_od);
            datumDo = (TextView) mView.findViewById(R.id.vrstica_datum_do);
            skupajCena = (TextView) mView.findViewById(R.id.vrstica_cena);
            gumb = (ImageButton) mView.findViewById(R.id.chat);
        }
    }
}
