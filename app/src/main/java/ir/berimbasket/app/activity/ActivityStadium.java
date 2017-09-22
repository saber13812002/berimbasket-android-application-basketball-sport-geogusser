package ir.berimbasket.app.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import ir.berimbasket.app.R;
import ir.berimbasket.app.activity.fragment.FragmentStadiumMap;
import ir.berimbasket.app.adapter.AdapterStadiumGallery;
import ir.berimbasket.app.entity.EntityStadiumGallery;

public class ActivityStadium extends AppCompatActivity {

    TextView txtStadiumName, txtStadiumTel, txtRateNo, txtStadiumAddress, txtStadiumRound, txtTelegramChannel, txtInstagramId, txtDetailSection;
    AppCompatButton btnCompleteStadiumDetail;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium);
        initToolbar();
        initViewsAndListeners();
        initStadiumMap();
        initGalleryRecycler();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // FIXME: 21/09/2017  setDisplayHomeAsUp show warning for nullpointer exception cause
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initViewsAndListeners() {

        typeface = Typeface.createFromAsset(getAssets(), "fonts/yekan.ttf");

        txtStadiumName = (TextView) findViewById(R.id.txtStadiumName);
        txtStadiumTel = (TextView) findViewById(R.id.txtStadiumTel);
        txtRateNo = (TextView) findViewById(R.id.txtRateNo);
        txtStadiumAddress = (TextView) findViewById(R.id.txtStadiumAddress);
        txtStadiumRound = (TextView) findViewById(R.id.txtStadiumRound);
        txtTelegramChannel = (TextView) findViewById(R.id.txtTelegramChannel);
        txtInstagramId = (TextView) findViewById(R.id.txtInstagramId);
        txtDetailSection = (TextView) findViewById(R.id.txtDetailSection);

        btnCompleteStadiumDetail = (AppCompatButton) findViewById(R.id.btnCompleteStadiumDetail);

        txtStadiumName.setTypeface(typeface);
        txtStadiumTel.setTypeface(typeface);
        txtRateNo.setTypeface(typeface);
        txtStadiumAddress.setTypeface(typeface);
        txtStadiumRound.setTypeface(typeface);
        txtTelegramChannel.setTypeface(typeface);
        txtInstagramId.setTypeface(typeface);
        btnCompleteStadiumDetail.setTypeface(typeface);
        txtDetailSection.setTypeface(typeface);
    }


    private void initStadiumMap() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mapContainer, new FragmentStadiumMap());
        fragmentTransaction.commit();
    }


    private void initGalleryRecycler() {
        int[] galleryIdes = {R.drawable.slider1, R.drawable.slider2, R.drawable.slider3};

        ArrayList<EntityStadiumGallery> stadiumGalleryList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            EntityStadiumGallery entityStadiumGallery = new EntityStadiumGallery();
            entityStadiumGallery.setId(i);
            entityStadiumGallery.setUrl(galleryIdes[i]);
            stadiumGalleryList.add(entityStadiumGallery);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerStadiumGallery);
        AdapterStadiumGallery adapterStadiumGallery = new AdapterStadiumGallery(stadiumGalleryList, this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapterStadiumGallery);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
