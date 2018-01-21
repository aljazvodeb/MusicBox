package com.feri.um.si.musicbox.modeli;

/**
 * Created by simon on 20. 01. 2018.
 */

public class Najem {
    private String datumOd;
    private String datumDo;
    private int cenaDan;
    private String cenaSkupaj;
    private String najemnik;
    private String najemodajalec;

    public Najem() {}

    public Najem (String datumOd, String datumDo, int cenaDan, String cenaSkupaj, String najemnik, String najemodajalec) {
        this.datumOd = datumOd;
        this.datumDo = datumDo;
        this.cenaDan = cenaDan;
        this.cenaSkupaj=cenaSkupaj;
        this.najemnik=najemnik;
        this.najemodajalec=najemodajalec;
    }

    public String getDatumOd() {
        return datumOd;
    }

    public void setDatumOd(String datumOd) {
        this.datumOd = datumOd;
    }

    public String getDatumDo() {
        return datumDo;
    }

    public void setDatumDo(String datumDo) {
        this.datumDo = datumDo;
    }

    public int getCenaDan() {
        return cenaDan;
    }

    public void setCenaDan(int cenaDan) {
        this.cenaDan = cenaDan;
    }

    public String getCenaSkupaj() {
        return cenaSkupaj;
    }

    public void setCenaSkupaj(String cenaSkupaj) {
        this.cenaSkupaj = cenaSkupaj;
    }

    public String getNajemnik() {
        return najemnik;
    }

    public void setNajemnik(String najemnik) {
        this.najemnik = najemnik;
    }

    public String getNajemodajalec() {
        return najemodajalec;
    }

    public void setNajemodajalec(String najemodajalec) {
        this.najemodajalec = najemodajalec;
    }
}
