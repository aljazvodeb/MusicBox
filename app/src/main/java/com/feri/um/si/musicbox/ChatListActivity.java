package com.feri.um.si.musicbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.feri.um.si.musicbox.adapterji.PogovorAdapter;
import com.feri.um.si.musicbox.modeli.Pogovor;
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

    private static final String TAG = "Chat";
    private PogovorAdapter mPogovorAdapter;
    private ProgressBar spinner;
    ArrayList<Pogovor> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String mUporabnik = user.getDisplayName();

        // inicializacija firebase komponent
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

        // inicializacija referenc na view
        ListView mPogovorListView = findViewById(R.id.pogovorListView);
        spinner = findViewById(R.id.progressBar);

        mPogovorAdapter = new PogovorAdapter(this, R.layout.item_pogovor);
        mPogovorListView.setAdapter(mPogovorAdapter);

        //pridobi datasnapshot sporocila
        DatabaseReference ref = mFirebaseDatabase.getReference().child("sporocila").child(mUporabnik);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Nimate sporočil! ", Toast.LENGTH_SHORT).show();
                    spinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Pogovor pogovor = new Pogovor();
                        pogovor.setSogovorec(dataSnapshot.getKey());
                        pogovor.setGlasbilo(snapshot.getKey());

                        list.add(pogovor);
                        mPogovorAdapter.add(pogovor);
                        spinner.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Log.d(TAG, "Napaka:" + e.getMessage());
                }

                spinner.setVisibility(View.GONE);

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
