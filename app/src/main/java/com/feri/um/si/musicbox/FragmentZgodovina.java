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

import com.feri.um.si.musicbox.adapterji.ZgodovinaAdapter;
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

public class FragmentZgodovina extends Fragment {

    public static final String TAG ="ZGODOVINA";

    private RecyclerView mList_zgodovina;
    private FirebaseFirestore mFirestore;
    private List<Najem> najemList;
    private List<Instrument> instrumentList;
    ProgressDialog napredek;
    private ZgodovinaAdapter najemListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_zgodovina, container, false);

        napredek = new ProgressDialog(getActivity());
        napredek.setTitle("Nalaganje");
        napredek.setMessage("Pridobivamo podatke... ");
        napredek.setCancelable(false);
        napredek.show();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        najemList = new ArrayList<>();
        instrumentList = new ArrayList<>();
        najemListAdapter = new ZgodovinaAdapter(getContext(), najemList, instrumentList);

        mList_zgodovina = (RecyclerView) getView().findViewById(R.id.list_zgodovina);
        mList_zgodovina.setHasFixedSize(true);
        mList_zgodovina.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList_zgodovina.setAdapter(najemListAdapter);

        mFirestore = FirebaseFirestore.getInstance();


        mFirestore.collection("Najem").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if (e != null) {
                    Log.d(TAG, "Napaka:" + e.getMessage());
                }

                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String trenutni = user.getDisplayName();
                    String vBazi = doc.getDocument().getString("najemnik");
                    if (trenutni.equals(vBazi)) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            if (doc.getDocument().getString("status").equals("Potrjeno")) {
                                Najem n = doc.getDocument().toObject(Najem.class).dodajID(doc.getDocument().getId());
                                Instrument i = doc.getDocument().toObject(Instrument.class).dodajID(doc.getDocument().getId());
                                instrumentList.add(i);
                                najemList.add(n);
                                najemListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                napredek.dismiss();
            }
        });
    }
}