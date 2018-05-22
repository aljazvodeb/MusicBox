package com.feri.um.si.musicbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.feri.um.si.musicbox.adapterji.NajemListAdapter;
import com.feri.um.si.musicbox.modeli.Najem;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ZgodovinaActivity extends AppCompatActivity {

    public static final String TAG ="Firelog";

    private RecyclerView mList_zgodovina;
    private FirebaseFirestore mFirestore;
    private List<Najem> najemList;
    private NajemListAdapter najemListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zgodovina);

        najemList = new ArrayList<>();
        najemListAdapter = new NajemListAdapter(najemList);

       mList_zgodovina = (RecyclerView) findViewById(R.id.list_zgodovina);
       mList_zgodovina.setHasFixedSize(true);
       mList_zgodovina.setLayoutManager(new LinearLayoutManager(this));
       mList_zgodovina.setAdapter(najemListAdapter);

       mFirestore = FirebaseFirestore.getInstance();





       mFirestore.collection("Najem").addSnapshotListener(new EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

               if(e != null){
                   Log.d(TAG,"Napaka:"+ e.getMessage());
               }

               for (DocumentChange doc: documentSnapshots.getDocumentChanges()) {


                   if (doc.getType() == DocumentChange.Type.ADDED){
                       Najem najem = doc.getDocument().toObject(Najem.class);
                       najemList.add(najem);

                       najemListAdapter.notifyDataSetChanged();
               }
               }
           }
       });

    }
}
