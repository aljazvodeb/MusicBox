package com.feri.um.si.musicbox.modeli;

import android.support.annotation.NonNull;

public class OsebaID {

    public String osebaID;

    public <T extends OsebaID> T dodajID (@NonNull final String id) {
        this.osebaID=id;
        return (T) this;
    }
}
