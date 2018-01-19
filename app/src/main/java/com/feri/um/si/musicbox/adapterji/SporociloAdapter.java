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

/**
 * Created by vodeb on 19. 01. 18.
 */

public class SporociloAdapter extends ArrayAdapter<Sporocilo> {

    public SporociloAdapter(Context context, int resource, List<Sporocilo> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_sporocilo, parent, false);
        }

        ImageView photoImageView = convertView.findViewById(R.id.photoImageView);
        TextView sporociloTextView = convertView.findViewById(R.id.sporociloTextView);
        TextView uporabnikTextView = convertView.findViewById(R.id.uporabnikTextView);
        TextView casTextView = convertView.findViewById(R.id.casTextView);


        Sporocilo message = getItem(position);

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
        uporabnikTextView.setText(message.getUporabnik());
        casTextView.setText((message.getCas()));

        return convertView;
    }
}
