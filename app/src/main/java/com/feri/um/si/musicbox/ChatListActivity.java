package com.feri.um.si.musicbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.feri.um.si.musicbox.modeli.Sporocilo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatListActivity extends AppCompatActivity {

    private String mUporabnik;
    private ListView mPogovorListView;
    private FirebaseDatabase mFirebaseDatabase;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mUporabnik = user.getDisplayName();

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        // Initialize references to views
        mPogovorListView = findViewById(R.id.pogovorListView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);
        mPogovorListView.setAdapter(adapter);

        //Get datasnapshot at your "sporocila" root node
        DatabaseReference ref = mFirebaseDatabase.getReference().child("sporocila").child(mUporabnik);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getKey();

                list.add(value);
                adapter.notifyDataSetChanged();

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

                i.putExtra("najemodajalec", list.get(position));

                startActivity(i);
            }
        });


    }

}
