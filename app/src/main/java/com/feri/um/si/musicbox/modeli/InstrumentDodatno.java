package com.feri.um.si.musicbox.modeli;

public class InstrumentDodatno {
    public static String getPriceString(Instrument instrument) {
        return getPriceString(Integer.parseInt(instrument.getCena()));
    }

    public static String getPriceString(int priceInt) {
        switch (priceInt) {
            case 1:
                return "0-10€";
            case 2:
                return "10-20€";
            case 3:
            default:
                return "20€+";
        }
    }
}
