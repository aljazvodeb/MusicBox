package com.feri.um.si.musicbox.modeli;


/**
 * Created by vodeb on 19. 01. 18.
 */

public class Sporocilo {
    private String besedilo;
    private String uporabnik;
    private String slika;
    private String cas;

    public Sporocilo() {

    }

    public Sporocilo(String besedilo, String uporabnik, String slika, String cas) {
        this.besedilo = besedilo;
        this.uporabnik = uporabnik;
        this.slika = slika;
        this.cas = cas;
    }

    public String getBesedilo() {
        return besedilo;
    }

    public void setBesedilo(String besedilo) {
        this.besedilo = besedilo;
    }

    public String getUporabnik() {
        return uporabnik;
    }

    public void setUporabnik(String uporabnik) {
        this.uporabnik = uporabnik;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }
}
