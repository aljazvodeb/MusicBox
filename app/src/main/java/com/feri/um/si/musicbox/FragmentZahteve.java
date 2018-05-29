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
import android.widget.ImageButton;

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

public class FragmentZahteve extends Fragment implements View.OnClickListener {

    public final static String TAG = "ZAHTEVE";

    private RecyclerView view;

    private List<Najem> najemList;
    private List<Instrument> instrumentList;
    private ZahtevaAdapter adapter;
    public ImageButton sprejmi;
    public ImageButton zavrni;
    // private FragmentManager f_manager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_zahteve, container, false);
        /*
        sprejmi = inflater.inflate(R.layout.item_zahteva, container, false).findViewById(R.id.imageButtonSprejmi);
        zavrni = inflater.inflate(R.layout.item_zahteva, container, false).findViewById(R.id.imageButtonZavrni);
        */

        rootView.setOnClickListener(this);

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

                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String trenutni = user.getDisplayName();
                    String vBazi = doc.getDocument().getString("najemodajalec");

                    if (trenutni.equals(vBazi)){
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
            }
        });
    }
    /*
    public void Update () {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
*/
    @Override
    public void onClick(View v) {
        // Toast.makeText(getActivity(), "test", Toast.LENGTH_LONG).show();
    }
}