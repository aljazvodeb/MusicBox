package com.feri.um.si.musicbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.feri.um.si.musicbox.adapterji.SporociloAdapter;
import com.feri.um.si.musicbox.modeli.Sporocilo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {


    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private ListView mSporociloListView;
    private SporociloAdapter mSporociloAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mSporociloEditText;
    private Button mPosljiButton;

    private String mUporabnik;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSporociloDatabaseReference;
    private ChildEventListener mChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSporociloDatabaseReference = mFirebaseDatabase.getReference().child("sporocila");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mUporabnik = user.getDisplayName();

        // Initialize references to views
        mProgressBar = findViewById(R.id.progressBar);
        mSporociloListView = findViewById(R.id.sporociloListView);
        mPhotoPickerButton = findViewById(R.id.photoPickerButton);
        mSporociloEditText = findViewById(R.id.sporociloEditText);
        mPosljiButton = findViewById(R.id.posljiButton);

        // Initialize message ListView and its adapter
        List<Sporocilo> friendlyMessages = new ArrayList<>();
        mSporociloAdapter = new SporociloAdapter(this, R.layout.item_sporocilo, friendlyMessages);
        mSporociloListView.setAdapter(mSporociloAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
            }
        });

        // Enable Send button when there's text to send
        mSporociloEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mPosljiButton.setEnabled(true);
                } else {
                    mPosljiButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mSporociloEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mPosljiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear input box
                mSporociloEditText.setText("");
            }
        });
        // Send button sends a message and clears the EditText
        mPosljiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat dateFormat = new SimpleDateFormat("HH:mm,  dd.MM.yyyy");
                Date date = new Date();
                Sporocilo sporocilo = new Sporocilo(mSporociloEditText.getText().toString(), mUporabnik, null, dateFormat.format(date));
                mSporociloDatabaseReference.push().setValue(sporocilo);
                // Clear input box
                mSporociloEditText.setText("");
            }
        });
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Sporocilo sporocilo = dataSnapshot.getValue(Sporocilo.class);
                mSporociloAdapter.add(sporocilo);
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
        };
        mSporociloDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

