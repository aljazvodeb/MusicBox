package com.feri.um.si.musicbox;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PregledActivity extends AppCompatActivity implements
        FilterDialogFragment.FilterListener,
        InstrumentAdapter.OnInstrumentSelectedListener{
    private FragmentManager fragmentManager;

    private Fragment fragment = null;
    private static final String TAG = "PregledActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final int LIMIT = 5;

    @BindView(R.id.vrstica)
    Toolbar mToolbar;

    @BindView(R.id.iskanje)
    TextView mCurrentSearchView;

    @BindView(R.id.sortiraj_po)
    TextView mCurrentSortByView;

    @BindView(R.id.recycler_instrumenti)
    RecyclerView mRestaurantsRecycler;

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
        setContentView(R.layout.activity_pregled);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        // omogocimo loge
        FirebaseFirestore.setLoggingEnabled(true);

        // inicializiramo Firestore in RecyclerView
        initFirestore();
        initRecyclerView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFilterDialog = new FilterDialogFragment();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new ProfileFragment();
        fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
        fragmentTransaction.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        disableNavigationViewScrollbars(navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_profile) {
                    fragment = new ProfileFragment();
                } else if (id == R.id.nav_chat) {

                }   else if (id == R.id.nav_logout) {

                }

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container_wrapper, fragment);
                transaction.commit();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                assert drawer != null;
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
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
                    mRestaurantsRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mRestaurantsRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Napaka: ", Snackbar.LENGTH_LONG).show();
            }
        };

        mRestaurantsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantsRecycler.setAdapter(mAdapter);
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

    private void onDodajInstrument() {
        Intent intent = new Intent(getApplicationContext(), DodajActivity.class);
        startActivity(intent);
    }

    private void profil() {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }
    private void odjava(){
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, MainActivity.class));
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
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

