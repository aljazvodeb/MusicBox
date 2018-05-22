package com.feri.um.si.musicbox.adapterji;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feri.um.si.musicbox.R;
import com.feri.um.si.musicbox.modeli.Sporocilo;

import java.util.List;


public class PogovorAdapter extends ArrayAdapter<Sporocilo> {


    public PogovorAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_pogovor, parent, false);
        }

        ImageView photoImageView = convertView.findViewById(R.id.instrumentImage);
        TextView instrumentextView = convertView.findViewById(R.id.instrumentText);
        TextView uporabnikTextView = convertView.findViewById(R.id.uporabnikText);

        Sporocilo message = getItem(position);

        boolean isPhoto = message.getSlika() != null;
        if (isPhoto) {
            instrumentextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getSlika())
                    .into(photoImageView);
        } else {
            instrumentextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            instrumentextView.setText(message.getBesedilo());
        }
        uporabnikTextView.setText(message.getUporabnik());

        return convertView;
    }

}
