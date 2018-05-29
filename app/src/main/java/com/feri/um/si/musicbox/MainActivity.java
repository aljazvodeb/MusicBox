package com.feri.um.si.musicbox;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feri.um.si.musicbox.adapterji.InstrumentAdapter;
import com.feri.um.si.musicbox.viewmodel.MainActivityViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by simon on 5. 01. 2018.
 */

public class MainActivity extends AppCompatActivity implements
        FilterDialogFragment.FilterListener,
        InstrumentAdapter.OnInstrumentSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int LIMIT = 5;

    @BindView(R.id.vrstica)
    Toolbar mToolbar;

    @BindView(R.id.iskanje)
    TextView mCurrentSearchView;

    @BindView(R.id.sortiraj_po)
    TextView mCurrentSortByView;

    @BindView(R.id.recycler_instrumenti)
    RecyclerView mInstrumentiRecycler;

    @BindView(R.id.prazno)
    ViewGroup mEmptyView;

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private FilterDialogFragment mFilterDialog;
    private InstrumentAdapter mAdapter;

    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        // omogocimo loge
        FirebaseFirestore.setLoggingEnabled(true);

        // inicializiramo Firestore in RecyclerView
        initFirestore();
        initRecyclerView();

        mFilterDialog = new FilterDialogFragment();
    }

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();

        // pridobi najboljse ocenjena glasbila
        mQuery = mFirestore.collection("Instrumenti")
                .orderBy("avgOcena", Query.Direction.DESCENDING)
                .limit(LIMIT);
    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new InstrumentAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // ce je seznam prazen
                if (getItemCount() == 0) {
                    mInstrumentiRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mInstrumentiRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Napaka: ", Snackbar.LENGTH_LONG).show();
            }
        };

        mInstrumentiRecycler.setLayoutManager(new LinearLayoutManager(this));
        mInstrumentiRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ce je potrebna prijava
        if (shouldStartSignIn()) {
            startSignIn();
            return;
        }

        // uporabi filtre
        onFilter(mViewModel.getFiltri());

        // poslusamo za spremembe
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
        // uporabi filtre
        onFilter(mViewModel.getFiltri());

        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onFilter(Filtri filtri) {
    // konstruiramo poizvedbo za filtriranje
        Query query = mFirestore.collection("Instrumenti");
        if (filtri.imaKategorijo()) {
            query = query.whereEqualTo("kategorija", filtri.getKategorija());
        }
        if (filtri.imaMesto()) {
            query = query.whereEqualTo("mesto", filtri.getMesto());
        }
        if (filtri.imaCeno()) {
            query = query.whereGreaterThanOrEqualTo("cena", filtri.getMinCena())
                    .whereLessThanOrEqualTo("cena", filtri.getMaxCena());
        }
        //sortiranje
        if (filtri.imaSortiranje()) {
            query = query.orderBy(filtri.getSortirajPo());
        }

        // omejimo seznam
        query = query.limit(LIMIT);

        // posodobi poizvedbo
        mQuery = query;
        mAdapter.setQuery(query);

        mCurrentSearchView.setText(Html.fromHtml(filtri.getIskanjeOpis(this)));
        mCurrentSortByView.setText(filtri.getOpisSortiranja(this));
        mViewModel.setFiltri(filtri);
    }
    //ustvarimo meni
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_dodaj:
                Intent dodaj = new Intent(getApplicationContext(), DodajActivity.class);
                startActivity(dodaj);
                break;
            case R.id.menu_odjava:
                AuthUI.getInstance().signOut(this);
                startSignIn();
                break;

            case R.id.menu_izposoje:
                Intent izposoje = new Intent(getApplicationContext(), IzposojeActivity.class);
                startActivity(izposoje);
                break;

            case R.id.menu_profil:
                Intent profil = new Intent(getApplicationContext(), ProfilActivity.class);
                startActivity(profil);
                break;
            case R.id.menu_pogovori:
                Intent pogovori = new Intent(getApplicationContext(), ChatListActivity.class);
                startActivity(pogovori);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            mViewModel.setPrijavljaSe(false);
            if (resultCode == RESULT_CANCELED)
                finish();
            else if (resultCode != RESULT_OK && shouldStartSignIn()) {
                startSignIn();
            }
        }
    }

    @OnClick(R.id.filter_vrstica)
    public void onFilterClicked() {
        // pokazi dialog za filtriranje
        mFilterDialog.show(getSupportFragmentManager(), FilterDialogFragment.TAG);
    }

    @OnClick(R.id.gumb_brisi_filter)
    public void onClearFilterClicked() {
        mFilterDialog.resetFiltri();
        onFilter(Filtri.getDefault());
    }

    @Override
    public void onInstrumentSelected(DocumentSnapshot instrument) {
        //podrobnosti
        Intent intent = new Intent(this, InstrumentOpisActivity.class);
        intent.putExtra(InstrumentOpisActivity.KEY_INSTRUMENT_ID, instrument.getId());

        startActivity(intent);
    }

    private boolean shouldStartSignIn() {
        return (!mViewModel.getPrijavljaSe() && FirebaseAuth.getInstance().getCurrentUser() == null);
    }

    private void startSignIn() {
        // prijava s firebase
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        mViewModel.setPrijavljaSe(true);
    }
}