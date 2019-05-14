package ir.berimbasket.app.ui.stadium;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import co.ronash.pushe.Pushe;
import de.hdodenhof.circleimageview.CircleImageView;
import ir.berimbasket.app.BuildConfig;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.env.UrlConstants;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Stadium;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.ui.common.model.StadiumBase;
import ir.berimbasket.app.util.LocaleManager;
import ir.berimbasket.app.util.Redirect;
import ir.berimbasket.app.util.Telegram;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StadiumActivity extends BaseActivity implements StadiumGalleryAdapter.StadiumGalleryListener {

    private int stadiumId;
    private boolean isFabOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium);
        initToolbar();
        String pusheStadiumId = getIntent().getStringExtra("pushe_activity_extra");
        if (pusheStadiumId != null) {
            TextView txtStadiumName = findViewById(R.id.txtStadiumName);
            txtStadiumName.setText(R.string.activity_stadium_txt_stadium_name_loading);
            initStadiumList(Integer.parseInt(pusheStadiumId), getApplicationContext(), true);
            // FIXME: 12/13/2017 send another param for logo
        } else {
            StadiumBase stadiumBase = (StadiumBase) getIntent().getSerializableExtra("stadiumDetail");
            initStadiumBaseInfo(stadiumBase);
            initStadiumList(stadiumBase.getId(), getApplicationContext(), false);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // FIXME: 21/09/2017  setDisplayHomeAsUp show warning for nullpointer exception cause
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initViewsAndListeners(final Stadium stadium) {

        this.stadiumId = stadium.getId();

        ImageView btnReportStadium = findViewById(R.id.btnReportStadium);
        AppCompatButton btnAddImage = findViewById(R.id.btnAddImage);
        AppCompatButton btnCompleteStadiumDetail = findViewById(R.id.btnCompleteStadiumDetail);
        Button btnGalleryMore = findViewById(R.id.btnGalleryMore);
        SpeedDialView fab = findViewById(R.id.fabStadium);
        CircleImageView imgStadiumLogo = findViewById(R.id.imgStadiumLogo);

        if (stadium.getImages() != null && stadium.getImages().size() != 0) {
            Picasso.with(StadiumActivity.this)
                    .load(stadium.getImages().get(0))
                    .resize(100, 100)
                    .placeholder(R.drawable.stadium1)
                    .error(R.drawable.stadium1)
                    .centerInside()
                    .into(imgStadiumLogo);
        }

        initFab(fab, stadium.getId());

        imgStadiumLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pusheId = Pushe.getPusheId(getApplicationContext());
                Redirect.sendToCustomTab(StadiumActivity.this, UrlConstants.External.WP_STADIUM_PROFILE + "?id=" + stadium.getId() + "&pusheid=" + pusheId);
            }
        });

        btnGalleryMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pusheId = Pushe.getPusheId(getApplicationContext());
                Redirect.sendToCustomTab(StadiumActivity.this, UrlConstants.External.WP_STADIUM_PROFILE + "?id=" + stadium.getId() + "&pusheid=" + pusheId);
            }
        });

        initStadiumSpecRecycler(stadium);

        btnCompleteStadiumDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!BuildConfig.FLAVOR.equals("bazaar")) {
                        Redirect.sendToTelegram(StadiumActivity.this, UrlConstants.Bot.MAP + stadium.getId(),
                                Telegram.DEFAULT_BOT);
                    }
                } catch (IllegalArgumentException unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });

        btnReportStadium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!BuildConfig.FLAVOR.equals("bazaar")) {
                        Redirect.sendToTelegram(StadiumActivity.this, UrlConstants.Bot.REPORT_STADIUM + stadium.getId(),
                                Telegram.DEFAULT_BOT);
                    }
                } catch (IllegalArgumentException unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhotoToGallery(stadium.getId());
            }
        });

        // stadium gallery
        List<String> galleryImages = stadium.getImages();
        if (galleryImages.size() == 0) {
            galleryImages.add(UrlConstants.External.STADIUM_DEFAULT_PHOTO);
            galleryImages.add(UrlConstants.External.STADIUM_DEFAULT_PHOTO);
            galleryImages.add(UrlConstants.External.STADIUM_DEFAULT_PHOTO);
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerStadiumGallery);
        StadiumGalleryAdapter stadiumGalleryAdapter = new StadiumGalleryAdapter(galleryImages, this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(stadiumGalleryAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initFab(SpeedDialView fab, int stadiumId) {
        // setup menu and animation
        fab.inflate(R.menu.menu_stadium_fab);
        addInfiniteShakeAnimationToView(fab);

        // on click listener
        fab.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {
                return false;
            }

            @Override
            public void onToggleChanged(boolean isOpen) {
                isFabOpen = isOpen;
            }
        });

        // customize fab actions
        int colorWhite = ResourcesCompat.getColor(getResources(), R.color.colorWhite, getTheme());
        fab.addActionItem(new SpeedDialActionItem.Builder(R.id.action_add_game, R.drawable.ic_fab_action_add_game)
                .setFabBackgroundColor(colorWhite)
                .setLabel(getString(R.string.activity_stadium_fab_action_add_game))
                .setLabelColor(Color.BLACK)
                .create());
        fab.addActionItem(new SpeedDialActionItem.Builder(R.id.action_add_photo, R.drawable.ic_fab_action_add_photo)
                .setFabBackgroundColor(colorWhite)
                .setLabel(getString(R.string.activity_stadium_fab_action_add_photo))
                .setLabelColor(Color.BLACK)
                .create());
        fab.addActionItem(new SpeedDialActionItem.Builder(R.id.action_reserve, R.drawable.ic_toolbar_reserve)
                .setFabBackgroundColor(colorWhite)
                .setLabel(getString(R.string.activity_stadium_fab_action_reserve))
                .setLabelColor(Color.BLACK)
                .create());

        fab.setOnActionSelectedListener(actionItem -> {
            switch (actionItem.getId()) {
                case R.id.action_add_game:
                    //todo implement this
                    return false;
                case R.id.action_add_photo:
                    addPhotoToGallery(stadiumId);
                    return false;
                case R.id.action_reserve:
                    try {
                        if (!BuildConfig.FLAVOR.equals("bazaar")) {
                            Redirect.sendToTelegram(StadiumActivity.this, UrlConstants.Bot.RESERVE + stadiumId
                                    , Telegram.DEFAULT_BOT);
                        }
                    } catch (IllegalArgumentException unknownTelegramURL) {
                        // do nothing yet
                    }
                    return false;
            }
            return false;
        });
    }

    private void addInfiniteShakeAnimationToView(View view) {
        final Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake_anim);
        Handler handler = new Handler();
        int delay = 5000;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!isFabOpen) {
                    view.startAnimation(animShake);
                }
                handler.postDelayed(this, delay);
            }
        });
    }

    private void addPhotoToGallery(int stadiumId) {
        try {
            if (!BuildConfig.FLAVOR.equals("bazaar")) {
                Redirect.sendToTelegram(StadiumActivity.this, UrlConstants.Bot.UPLOAD + stadiumId
                        , Telegram.DEFAULT_BOT);
            }
        } catch (IllegalArgumentException unknownTelegramURL) {
            // do nothing yet
        }
    }

    private void initStadiumBaseInfo(StadiumBase stadiumBase) {
        // stadium title
        TextView txtStadiumName = findViewById(R.id.txtStadiumName);
        txtStadiumName.setText(stadiumBase.getTitle());

        // stadium map
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        StadiumMapFragment stadiumMapFragment = new StadiumMapFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("stadiumDetail", stadiumBase);
        stadiumMapFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.mapContainer, stadiumMapFragment);
        fragmentTransaction.commit();
    }


    private void initStadiumSpecRecycler(final Stadium stadium) {
        RecyclerView recyclerView = findViewById(R.id.recyclerStadiumSpec);
        StadiumSpecificationAdapter stadiumSpecificationAdapter = new StadiumSpecificationAdapter(getStadiumSpecListValue(stadium), getStadiumSpecListKey(), this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(stadiumSpecificationAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initStadiumList(final int stadiumId, Context context, final boolean fromScratch) {
        String pusheId = Pushe.getPusheId(context);
        String userName = new PrefManager(context).getUserName();
        String lang = LocaleManager.getLocale(context).getLanguage();
        WebApiClient.getStadiumApi(context).getStadium(stadiumId, pusheId, userName, lang).enqueue(new Callback<List<Stadium>>() {
            @Override
            public void onResponse(Call<List<Stadium>> call, Response<List<Stadium>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Stadium> stadiums = response.body();
                    if (stadiums != null) {
                        Stadium stadium = stadiums.get(0);
                        if (fromScratch) {
                            StadiumBase entity = new StadiumBase(stadium.getId(), stadium.getTitle(),
                                    stadium.getLatitude(), stadium.getLongitude());
                            initStadiumBaseInfo(entity);
                        }
                        initViewsAndListeners(stadiums.get(0));
                    }
                } else {
                    // http call with incorrect params or other network error
                }
            }

            @Override
            public void onFailure(Call<List<Stadium>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onGalleryItemClick(String imageUrl) {
        String pusheId = Pushe.getPusheId(getApplicationContext());
        Redirect.sendToCustomTab(StadiumActivity.this, UrlConstants.External.WP_STADIUM_PROFILE + "?id=" + stadiumId + "&pusheid=" + pusheId);
    }

    private ArrayList<String> getStadiumSpecListKey() {

        ArrayList<String> stadiumSpecListKey = new ArrayList<>();

        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_phone));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_address));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_work_time));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_telegram_group));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_telegram_channel));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_instagram));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_roof));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_distance_2_parking));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_rim_height));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_rim_number));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_spotlight));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_fence));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_parking));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_basket_net));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_score_line));
        stadiumSpecListKey.add(getString(R.string.activity_stadium_spec_lines));

        return stadiumSpecListKey;
    }

    private ArrayList<String> getStadiumSpecListValue(final Stadium stadium) {

        ArrayList<String> stadiumSpecListValue = new ArrayList<>();

        stadiumSpecListValue.add("-");
        stadiumSpecListValue.add(stadium.getAddress());
        stadiumSpecListValue.add("-");
        stadiumSpecListValue.add(stadium.getTelegramGroupId());
        stadiumSpecListValue.add(stadium.getTelegramChannelId());
        stadiumSpecListValue.add(stadium.getInstagramId());
        stadiumSpecListValue.add(stadium.getRoof());
        stadiumSpecListValue.add(stadium.getDistance2parking());
        stadiumSpecListValue.add(stadium.getRimHeight());
        stadiumSpecListValue.add(stadium.getRimNumber());
        stadiumSpecListValue.add(stadium.getSpotlight());
        stadiumSpecListValue.add(stadium.getFence());
        stadiumSpecListValue.add(stadium.getParking());
        stadiumSpecListValue.add(stadium.getBasketNet());
        stadiumSpecListValue.add(stadium.getScoreline());
        stadiumSpecListValue.add(stadium.getLines());
        return stadiumSpecListValue;
    }
}
