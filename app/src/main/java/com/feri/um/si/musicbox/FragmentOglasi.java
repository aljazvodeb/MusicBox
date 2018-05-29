package com.feri.um.si.musicbox;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feri.um.si.musicbox.adapterji.OglasiAdapter;
import com.feri.um.si.musicbox.modeli.Instrument;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentOglasi extends Fragment {

    public static final String TAG = "OGLASI";

    private RecyclerView view;

    private List<Instrument> list;
    ProgressDialog napredek;
    private OglasiAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_oglasi, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        napredek = new ProgressDialog(getActivity());
        napredek.setTitle("Nalaganje");
        napredek.setMessage("Pridobivamo podatke... ");
        napredek.setCancelable(false);
        napredek.show();

        list = new ArrayList<>();
        adapter = new OglasiAdapter(getContext(), list);

        view = getView().findViewById(R.id.odstraniView);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        view.setAdapter(adapter);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Instrumenti").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.d(TAG, "Napaka:" + e.getMessage());
                }
                // pridobi podatke iz baze

                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String trenutni = user.getDisplayName();
                    String vBazi = doc.getDocument().getString("najemodajalec");
                    if (trenutni.equals(vBazi)) { // ce sta uporabnik v bazi in trenutno prijavljeni uporabnik enaka potem prikazi oglase
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            Instrument i = doc.getDocument().toObject(Instrument.class).dodajID(doc.getDocument().getId());
                            list.add(i);
                            adapter.notifyDataSetChanged(); // obvestimo adapter o spremenjenih podatkih
                        }
                    }

                }
                napredek.dismiss();
            }
        });
    }
}