package com.feri.um.si.musicbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feri.um.si.musicbox.adapterji.ZahtevaAdapter;
import com.feri.um.si.musicbox.modeli.Instrument;
import com.feri.um.si.musicbox.modeli.Najem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentZahteve extends Fragment {

    public final static String TAG = "ZAHTEVE";

    private RecyclerView view;

    private List<Najem> najemList;
    private List<Instrument> instrumentList;
    private ZahtevaAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_zahteve, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view = getView().findViewById(R.id.imageButtonSprejmi);

        najemList = new ArrayList<>();
        instrumentList = new ArrayList<>();


        adapter = new ZahtevaAdapter(getContext(), najemList, instrumentList);

        view = getView().findViewById(R.id.zahteveView);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getActivity()));
        view.setAdapter(adapter);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Najem").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (e != null) {
                        Log.d(TAG, "Napaka:" + e.getMessage());
                    }
                    // pridobimo podatke iz baze
                    try {
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String trenutni = user.getDisplayName();
                            String vBazi = doc.getDocument().getString("najemodajalec");

                            if (trenutni.equals(vBazi)) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    if (doc.getDocument().getString("status").equals("Zahteva")) {
                                        Najem n = doc.getDocument().toObject(Najem.class).dodajID(doc.getDocument().getId());
                                        Instrument i = doc.getDocument().toObject(Instrument.class).dodajID(doc.getDocument().getId());
                                        instrumentList.add(i);
                                        najemList.add(n);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Log.d(TAG, "Napaka:" + ex.getMessage());
                    }
                }
            });
        }
    }