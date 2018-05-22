package com.feri.um.si.musicbox.adapterji;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feri.um.si.musicbox.R;
import com.feri.um.si.musicbox.modeli.Najem;

import java.util.List;

public class NajemListAdapter extends RecyclerView.Adapter<NajemListAdapter.ViewHolder> {

    public List<Najem> najemList;

    public NajemListAdapter(List<Najem> najemList){

        this.najemList = najemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_najem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.najemnik.setText(najemList.get(position).getNajemnik());
        holder.najemodajalec.setText(najemList.get(position).getNajemodajalec());
        holder.datumOd.setText(najemList.get(position).getDatumOd());
        holder.datumDo.setText(najemList.get(position).getDatumDo());
        holder.skupajCena.setText(najemList.get(position).getCenaSkupaj());
    }

    @Override
    public int getItemCount() {

        return najemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        View mView;
        public TextView najemnik;
        public TextView najemodajalec;
        public TextView datumOd;
        public TextView datumDo;
        public TextView skupajCena;

        public ViewHolder(View itemView){

            super(itemView);
            mView = itemView;

            najemnik = (TextView) mView.findViewById(R.id.najemnik_text);
            najemodajalec = (TextView) mView.findViewById(R.id.najemodajalec_text);
            datumOd = (TextView) mView.findViewById(R.id.datumOd_text);
            datumDo = (TextView) mView.findViewById(R.id.datumDo_text);
            skupajCena = (TextView) mView.findViewById(R.id.skupajCena_text);
        }
    }
}
