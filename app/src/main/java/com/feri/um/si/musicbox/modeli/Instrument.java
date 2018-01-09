package com.feri.um.si.musicbox.modeli;

public class Instrument {

    public static final String OZNAKA_MESTO = "mesto";
    public static final String OZNAKA_KATEGORIJA = "kategorija";
    public static final String OZNAKA_CENA = "cena";

    private String ime;
    private String mesto;
    private String kategorija;
    private String fotografija;
    private String opis;
    private String stanje;
    private String cena;

    public Instrument() {}

    public Instrument(String ime, String mesto, String kategorija, String fotografija, String opis, String stanje,
                      String cena) {
        this.ime = ime;
        this.mesto = mesto;
        this.kategorija = kategorija;
        this.fotografija=fotografija;
        this.opis=opis;
        this.stanje=stanje;
        this.cena = cena;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public String getSlika() {
        return fotografija;
    }

    public void setSlika(String fotografija) {
        this.fotografija = fotografija;
    }

    public String getStanje() {
        return stanje;
    }

    public void setStanje(String stanje) {
        this.stanje = stanje;
    }

    public String getCena() {
        return cena;
    }

    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}
