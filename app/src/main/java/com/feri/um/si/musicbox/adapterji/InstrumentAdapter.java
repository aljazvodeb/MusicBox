package com.feri.um.si.musicbox.adapterji;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feri.um.si.musicbox.R;
import com.feri.um.si.musicbox.modeli.Instrument;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

// RecyclerView adapter za prikaz seznama instrumentov

public class InstrumentAdapter extends FirestoreAdapter<InstrumentAdapter.ViewHolder> {

    public interface OnInstrumentSelectedListener {

        void onInstrumentSelected(DocumentSnapshot restaurant);

    }

    private OnInstrumentSelectedListener mListener;

    public InstrumentAdapter(Query query, OnInstrumentSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.vrstica, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.vrstica_slika)
        ImageView imageView;

        @BindView(R.id.vrstica_ime)
        TextView nameView;

        @BindView(R.id.vrstica_cena)
        TextView priceView;

        @BindView(R.id.vrstica_kategorija)
        TextView categoryView;

        @BindView(R.id.vrstica_mesto)
        TextView cityView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("StringFormatMatches")
        public void bind(final DocumentSnapshot snapshot,
                         final OnInstrumentSelectedListener listener) {

            Instrument instrument = snapshot.toObject(Instrument.class);
            Resources resources = itemView.getResources();

            // nalozi sliko
            Glide.with(imageView.getContext())
                    .load(instrument.getSlika())
                    .into(imageView);

            nameView.setText(instrument.getIme());
            cityView.setText(instrument.getMesto());
            categoryView.setText(instrument.getKategorija());
            priceView.setText(instrument.getCena()+"€/dan");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onInstrumentSelected(snapshot);
                    }
                }
            });
        }

    }
}
