package com.feri.um.si.musicbox.adapterji;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.feri.um.si.musicbox.IzposojeActivity;
import com.feri.um.si.musicbox.R;
import com.feri.um.si.musicbox.modeli.Instrument;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OglasiAdapter extends RecyclerView.Adapter<OglasiAdapter.ViewHolder> {

    public List<Instrument> oglasiList;
    public Context context;

    public OglasiAdapter(Context context, List<Instrument> oglasiList){
        this.oglasiList = oglasiList;
        this.context = context;
    }

    public OglasiAdapter(){
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_odstrani_oglas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.slika.setVisibility(View.VISIBLE);
        Glide.with(holder.slika.getContext())
                .load(oglasiList.get(position).getSlika())
                .into(holder.slika);
        holder.ime.setText(oglasiList.get(position).getIme());
        holder.kategorija.setText(oglasiList.get(position).getKategorija());
        holder.mesto.setText(oglasiList.get(position).getMesto());
        holder.cena.setText(Integer.toString(oglasiList.get(position).getCena())+"€");
        final String osebaID = oglasiList.get(position).osebaID;

        holder.gumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(context);
                        }
                        builder.setTitle("Brisanje oglasa")
                                .setMessage("Ali ste prepričani, da želite zbrisati oglas?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseFirestore.getInstance().collection("Instrumenti").document(osebaID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                 Toast.makeText(context, "Oglas je uspešno zbrisan!", Toast.LENGTH_LONG).show();
                                                 Intent intent = new Intent(context, IzposojeActivity.class);
                                                 context.startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Napaka!", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return oglasiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public ImageView slika;
        public TextView ime;
        public TextView kategorija;
        public TextView mesto;
        public TextView cena;
        public ImageButton gumb;

        public ViewHolder(View itemView){

            super(itemView);
            mView = itemView;

            slika = (ImageView) mView.findViewById(R.id.vrstica_slika);
            ime = (TextView) mView.findViewById(R.id.vrstica_ime);
            kategorija = (TextView) mView.findViewById(R.id.vrstica_kategorija);
            mesto = (TextView) mView.findViewById(R.id.vrstica_mesto);
            cena = (TextView) mView.findViewById(R.id.vrstica_cena);
            gumb = (ImageButton) mView.findViewById(R.id.imageButtonOdstrani);
        }
    }
}