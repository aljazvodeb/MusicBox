
 package com.feri.um.si.musicbox.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.feri.um.si.musicbox.Filtri;

 public class MainActivityViewModel extends ViewModel {

    private boolean mPrijavljaSe;
    private Filtri mFiltri;

    public MainActivityViewModel() {
        mPrijavljaSe = false;
        mFiltri = Filtri.getDefault();
    }

    public boolean getPrijavljaSe() {
        return mPrijavljaSe;
    }

    public void setPrijavljaSe(boolean mPrijavljaSe) {
        this.mPrijavljaSe = mPrijavljaSe;
    }

    public Filtri getFiltri() {
        return mFiltri;
    }

    public void setFiltri(Filtri mFiltri) {
        this.mFiltri = mFiltri;
    }
}
