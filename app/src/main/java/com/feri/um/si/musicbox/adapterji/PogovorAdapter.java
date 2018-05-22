package com.feri.um.si.musicbox.adapterji;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.feri.um.si.musicbox.R;
import com.feri.um.si.musicbox.modeli.Pogovor;

public class PogovorAdapter extends ArrayAdapter<Pogovor> {


    public PogovorAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_pogovor, parent, false);
        }

        //ImageView photoImageView = convertView.findViewById(R.id.instrumentImage);
        TextView glasbiloTextView = convertView.findViewById(R.id.instrumentText);
        TextView sogovorecTextView = convertView.findViewById(R.id.uporabnikText);


        Pogovor pogovor = getItem(position);


/*
        boolean isPhoto = message.getSlika() != null;
        if (isPhoto) {
            sporociloTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getSlika())
                    .into(photoImageView);
        } else {
            sporociloTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            sporociloTextView.setText(message.getBesedilo());
        }
        */
        glasbiloTextView.setText(pogovor.getGlasbilo());
        sogovorecTextView.setText((pogovor.getSogovorec()));

        return convertView;
    }

}
