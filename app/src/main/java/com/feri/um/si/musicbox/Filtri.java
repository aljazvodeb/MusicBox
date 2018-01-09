 package com.feri.um.si.musicbox;

import android.content.Context;
import android.text.TextUtils;

import com.google.firebase.example.musicbox.R;
import com.feri.um.si.musicbox.modeli.Instrument;
import com.google.firebase.firestore.Query;

 /**
  * Created by simon on 5. 01. 2018.
  */

public class Filtri {

    private String kategorija = null;
    private String mesto = null;
    private int cena = -1;
    private String sortBy = null;
    private Query.Direction sortDirection = null;

    public Filtri() {}

    public static Filtri getDefault() {
        Filtri filtri = new Filtri();
        filtri.setSortirajPo(Instrument.OZNAKA_CENA);
        filtri.setSortSmer(Query.Direction.DESCENDING);
        return filtri;
    }

    public boolean imaKategorijo() {
        return !(TextUtils.isEmpty(kategorija));
    }

    public boolean imaMesto() {
        return !(TextUtils.isEmpty(mesto));
    }

    public boolean imaCeno() {
        return (cena > 0);
    }

    public boolean imaSortiranje() {
        return !(TextUtils.isEmpty(sortBy));
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public int getCena() {
        return cena;
    }

    public void setCena(int cena) {
        this.cena = cena;
    }

    public String getSortirajPo() {
        return sortBy;
    }

    public void setSortirajPo(String sortBy) {
        this.sortBy = sortBy;
    }

    public Query.Direction getSortSmer() {
        return sortDirection;
    }

    public void setSortSmer(Query.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getIskanjeOpis(Context context) {
        StringBuilder desc = new StringBuilder();

        if (kategorija == null && mesto == null) {
            desc.append("<b>");
            desc.append(context.getString(R.string.vsi_instrumenti));
            desc.append("</b>");
        }

        if (kategorija != null) {
            desc.append("<b>");
            desc.append(kategorija);
            desc.append("</b>");
        }

        if (kategorija != null && mesto != null) {
            desc.append(" v ");
        }

        if (mesto != null) {
            desc.append("<b>");
            desc.append(mesto);
            desc.append("</b>");
        }

        if (cena > 0) {
            desc.append(" za ");
            desc.append("<b>");
            desc.append(cena);
            desc.append(" eur ");
            desc.append("</b>");
        }
        return desc.toString();
    }

    public String getOpisSortiranja(Context context) {
        if (Instrument.OZNAKA_KATEGORIJA.equals(sortBy)) {
                return context.getString(R.string.sortiraj_po_kategoriji);
            }
        else {
            return context.getString(R.string.sortiraj_po_ceni);
        }
    }
}
