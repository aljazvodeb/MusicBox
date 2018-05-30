package com.feri.um.si.musicbox;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.feri.um.si.musicbox.modeli.Filtri;
import com.feri.um.si.musicbox.modeli.Instrument;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by simon on 5. 01. 2018.
 */

public class FilterDialogFragment extends DialogFragment {

    public static final String TAG = "FilterDialog";

    interface FilterListener {
        void onFilter(Filtri filtri);
    }

    private View mRootView;

    @BindView(R.id.spinner_kategorija)
    Spinner mKategorijaSpinner;

    @BindView(R.id.spinner_mesto)
    Spinner mMestoSpinner;

    @BindView(R.id.spinner_sortiranje)
    Spinner mSortSpinner;

    @BindView(R.id.spinner_cena)
    Spinner mCenaSpinner;

    private FilterListener mFilterListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_filtri, container, false);
        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FilterListener) {
            mFilterListener = (FilterListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick(R.id.button_iskanje)
    public void onSearchClicked() {
        if (mFilterListener != null) {
            mFilterListener.onFilter(getFiltri());
        }

        dismiss();
    }

    @OnClick(R.id.button_preklici)
    public void onCancelClicked() {
        dismiss();
    }

    @Nullable
    private String getIzbranaKategorija() {
        String izbrano = (String) mKategorijaSpinner.getSelectedItem();
        if (getString(R.string.vse_kategorije).equals(izbrano)) {
            return null;
        } else {
            return izbrano;
        }
    }

    @Nullable
    private String getIzbranoMesto() {
        String izbrano = (String) mMestoSpinner.getSelectedItem();
        if (getString(R.string.vsa_mesta).equals(izbrano)) {
            return null;
        } else {
            return izbrano;
        }
    }

    private int getIzbranaCena() {
        String izbrano = (String) mCenaSpinner.getSelectedItem();
        if (izbrano.equals(getString(R.string.cena_1))) {
            return 1;
        } else if (izbrano.equals(getString(R.string.cena_2))) {
            return 2;
        } else if (izbrano.equals(getString(R.string.cena_3))) {
            return 3;
        } else {
            return -1;
        }
    }

    @Nullable
    private String getIzbranoSortiranje() {
        String izbrano = (String) mSortSpinner.getSelectedItem();

        if (getString(R.string.sortiraj_po_ceni).equals(izbrano)) {
            return Instrument.OZNAKA_CENA;
        }
        if (getString(R.string.sortiraj_po_kategoriji).equals(izbrano)) {
            return Instrument.OZNAKA_KATEGORIJA;
        }
        return null;
    }

    @Nullable
    private Query.Direction getSortDirection() {
        String izbrano = (String) mSortSpinner.getSelectedItem();
        if (getString(R.string.sortiraj_po_ceni).equals(izbrano)) {
            return Query.Direction.ASCENDING;
        }
        if (getString(R.string.sortiraj_po_kategoriji).equals(izbrano)) {
            return Query.Direction.DESCENDING;
        }

        return null;
    }

    public void resetFiltri() {
        if (mRootView != null) {
            mKategorijaSpinner.setSelection(0);
            mMestoSpinner.setSelection(0);
            mCenaSpinner.setSelection(0);
            mSortSpinner.setSelection(0);
        }
    }

    public Filtri getFiltri() {
        Filtri filtri = new Filtri();

        if (mRootView != null) {
            filtri.setKategorija(getIzbranaKategorija());
            filtri.setMesto(getIzbranoMesto());
            if (getIzbranaCena() == 1) {
                filtri.setMinCena(0);
                filtri.setMaxCena(10);
            }
            if (getIzbranaCena() ==2) {
                filtri.setMinCena(10);
                filtri.setMaxCena(20);
            }
            if (getIzbranaCena() == 3) {
                filtri.setMinCena(20);
                filtri.setMaxCena(100);
            }
            if (getIzbranaCena() == -1) {
                filtri.setMinCena(0);
                filtri.setMaxCena(100);
            }
            filtri.setSortirajPo(getIzbranoSortiranje());
        }

        return filtri;
    }
}
