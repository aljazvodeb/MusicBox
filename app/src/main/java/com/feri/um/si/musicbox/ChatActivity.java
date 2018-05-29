package com.feri.um.si.musicbox;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;

import com.feri.um.si.musicbox.adapterji.SporociloAdapter;
import com.feri.um.si.musicbox.modeli.Sporocilo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private static final int RC_PHOTO_PICKER =  2;

    private SporociloAdapter mSporociloAdapter;
    private EditText mSporociloEditText;
    private Button mPosljiButton;
    private ProgressBar spinner;

    private String mNajemnik;
    private DateFormat dateFormat;
    private Date date;

    private DatabaseReference mSporociloDatabaseReference;
    private DatabaseReference mSporociloDatabaseReference2;
    private StorageReference mChatSlikeStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dateFormat = new SimpleDateFormat("HH:mm,  dd.MM.yyyy");
        date = new Date();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mNajemnik = user.getDisplayName();
        String mNajemodajalec = getIntent().getStringExtra("najemodajalec");
        String mGlasbilo = getIntent().getStringExtra("glasbilo");


        // Initialize Firebase components
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();

        mSporociloDatabaseReference = mFirebaseDatabase.getReference().child("sporocila").child(mNajemnik).child(mNajemodajalec).child(mGlasbilo);
        mSporociloDatabaseReference2 = mFirebaseDatabase.getReference().child("sporocila").child(mNajemodajalec).child(mNajemnik).child(mGlasbilo);
        mChatSlikeStorageReference = mFirebaseStorage.getReference().child("chat_slike");

        // Initialize references to views
        ListView mSporociloListView = findViewById(R.id.sporociloListView);
        ImageButton mPhotoPickerButton = findViewById(R.id.photoPickerButton);
        mSporociloEditText = findViewById(R.id.sporociloEditText);
        mPosljiButton = findViewById(R.id.posljiButton);
        Toolbar mToolbar = findViewById(R.id.toolbarChat);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mGlasbilo);
        getSupportActionBar().setSubtitle(mNajemodajalec);
        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        // Initialize adapter
        mSporociloAdapter = new SporociloAdapter(this, R.layout.item_sporocilo);
        mSporociloListView.setAdapter(mSporociloAdapter);

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
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
                spinner.setVisibility(View.VISIBLE);

                Sporocilo sporocilo = new Sporocilo(mSporociloEditText.getText().toString(), mNajemnik, null, dateFormat.format(date));
                mSporociloDatabaseReference.push().setValue(sporocilo);

                mSporociloDatabaseReference2.push().setValue(sporocilo);

                // Clear input box
                mSporociloEditText.setText("");

                spinner.setVisibility(View.GONE);

            }
        });

        ChildEventListener mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                spinner.setVisibility(View.VISIBLE);

                Sporocilo sporocilo = dataSnapshot.getValue(Sporocilo.class);
                mSporociloAdapter.add(sporocilo);

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
        };
        mSporociloDatabaseReference.addChildEventListener(mChildEventListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            spinner.setVisibility(View.VISIBLE);

            // Get reference to store file at chat_slike/<FILENAME>
            StorageReference photoRef = mChatSlikeStorageReference.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Sporocilo sporocilo = new Sporocilo(null, mNajemnik, downloadUrl.toString(), dateFormat.format(date));
                    mSporociloDatabaseReference.push().setValue(sporocilo);

                    mSporociloDatabaseReference2.push().setValue(sporocilo);

                    spinner.setVisibility(View.GONE);

                }
            });

        }
    }

}

