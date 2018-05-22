package com.feri.um.si.musicbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.feri.um.si.musicbox.adapterji.PogovorAdapter;
import com.feri.um.si.musicbox.modeli.Pogovor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ChatListActivity extends AppCompatActivity {

    private PogovorAdapter mPogovorAdapter;

    ArrayList<Pogovor> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String mUporabnik = user.getDisplayName();

        // Initialize Firebase components
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

        // Initialize references to views
        ListView mPogovorListView = findViewById(R.id.pogovorListView);

        mPogovorAdapter = new PogovorAdapter(this, R.layout.item_pogovor);
        mPogovorListView.setAdapter(mPogovorAdapter);

        //Get datasnapshot at your "sporocila" root node
        DatabaseReference ref = mFirebaseDatabase.getReference().child("sporocila").child(mUporabnik);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Pogovor pogovor = new Pogovor();
                    pogovor.setSogovorec(dataSnapshot.getKey());
                    pogovor.setGlasbilo(snapshot.getKey());

                    list.add(pogovor);
                    mPogovorAdapter.add(pogovor);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mPogovorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), ChatActivity.class);

                i.putExtra("najemodajalec", list.get(position).getSogovorec());
                i.putExtra("glasbilo", list.get(position).getGlasbilo());


                startActivity(i);
            }
        });


    }

}
