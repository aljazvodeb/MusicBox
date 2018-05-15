package com.feri.um.si.musicbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfilActivity extends AppCompatActivity {

    @BindView(R.id.vrstica)
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
    }

    private void onDodajInstrument() {
        Intent intent = new Intent(getApplicationContext(), DodajActivity.class);
        startActivity(intent);
    }

    private void profil() {
        Intent intent = new Intent(getApplicationContext(), ProfilActivity.class);
        startActivity(intent);
    }

    private void pregled() {
        Intent intent = new Intent(getApplicationContext(), PregledActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_dodaj:
                onDodajInstrument();
                break;

            case R.id.menu_pregled:
                pregled();
                break;

            case R.id.menu_profil:
                profil();
                break;





        }
        return super.onOptionsItemSelected(item);
    }


}
