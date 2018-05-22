package com.feri.um.si.musicbox.modeli;

public class Pogovor {

    private String glasbilo;
    private String sogovorec;

    public Pogovor() {

    }

    public Pogovor(String glasbilo, String sogovorec) {
        this.glasbilo = glasbilo;
        this.sogovorec = sogovorec;
    }

    public String getGlasbilo() {
        return glasbilo;
    }

    public void setGlasbilo(String glasbilo) {
        this.glasbilo = glasbilo;
    }

    public String getSogovorec() {
        return sogovorec;
    }

    public void setSogovorec(String sogovorec) {
        this.sogovorec = sogovorec;
    }

}
