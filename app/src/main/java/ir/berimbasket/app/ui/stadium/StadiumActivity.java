package ir.berimbasket.app.ui.stadium;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.List;

import co.ronash.pushe.Pushe;
import de.hdodenhof.circleimageview.CircleImageView;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Stadium;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.base.BaseActivity;
import ir.berimbasket.app.ui.common.entity.StadiumBaseEntity;
import ir.berimbasket.app.util.LocaleManager;
import ir.berimbasket.app.util.Redirect;
import ir.berimbasket.app.util.Telegram;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StadiumActivity extends BaseActivity implements StadiumGalleryAdapter.StadiumGalleryListener {

    private static final String UPDATE_STADIUM_INFO_BOT = "https://t.me/berimbasketProfilebot?start=-";
    private static final String REPORT_STADIUM_BOT = "https://t.me/berimbasketreportbot?start=-";
    private static final String RESERVE_STADIUM_BOT = "https://t.me/Berimbasketreservebot?start=";
    private static final String STADIUM_IMAGE_BOT = "https://t.me/berimbasketuploadbot?start=";

    private static final String EXTERNAL_WEB_STADIUM_URL = "http://berimbasket.ir/bball/www/instagram.php?id=";
    private int stadiumId;

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
            StadiumBaseEntity stadiumBase = (StadiumBaseEntity) getIntent().getSerializableExtra("stadiumDetail");
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

        TextView txtStadiumTel = findViewById(R.id.txtStadiumTel);
        TextView txtStadiumAddress = findViewById(R.id.txtStadiumAddress);
        TextView txtStadiumRound = findViewById(R.id.txtStadiumRound);
        TextView txtTelegramChannel = findViewById(R.id.txtTelegramChannel);
        TextView txtInstagramId = findViewById(R.id.txtInstagramId);
        TextView txtDetailSection = findViewById(R.id.txtDetailSection);
        TextView txtRoof = findViewById(R.id.txtRoof);
        TextView txtDistance2Parking = findViewById(R.id.txtDistance2Parking);
        TextView txtRimHeight = findViewById(R.id.txtRimHeight);
        TextView txtRimNumber = findViewById(R.id.txtRimNumber);
        TextView txtSpotlight = findViewById(R.id.txtSpotlight);
        TextView txtFence = findViewById(R.id.txtFence);
        TextView txtParking = findViewById(R.id.txtParking);
        TextView txtBasketNet = findViewById(R.id.txtBasketNet);
        TextView txtScoreLine = findViewById(R.id.txtScoreLine);
        TextView txtLines = findViewById(R.id.txtLines);
        ImageView btnReportStadium = findViewById(R.id.btnReportStadium);
        ImageView btnReserveStadium = findViewById(R.id.btnReserveStadium);
        AppCompatButton btnAddImage = findViewById(R.id.btnAddImage);
        AppCompatButton btnCompleteStadiumDetail = findViewById(R.id.btnCompleteStadiumDetail);
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

        imgStadiumLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Redirect.sendToCustomTab(StadiumActivity.this, EXTERNAL_WEB_STADIUM_URL + stadium.getId());
            }
        });

        String specSeparator = getString(R.string.activity_stadium_spec_separator);
        txtStadiumTel.setText(getString(R.string.activity_stadium_spec_phone) + " " + specSeparator + " " + "-");
        txtStadiumAddress.setText(getString(R.string.activity_stadium_spec_address) + " " + specSeparator + " " + stadium.getAddress());
        txtStadiumRound.setText(getString(R.string.activity_stadium_spec_work_time) + " " + specSeparator + " " + "-");
        txtTelegramChannel.setText(getString(R.string.activity_stadium_spec_telegram_channel) + " " + specSeparator + " " + stadium.getTelegramChannelId());
        txtInstagramId.setText(getString(R.string.activity_stadium_spec_instagram) + " " + specSeparator + " " + stadium.getInstagramId());
        txtRoof.setText(getString(R.string.activity_stadium_spec_roof) + " " + specSeparator + " " + stadium.getRoof());
        txtDistance2Parking.setText(getString(R.string.activity_stadium_spec_distance_2_parking) + " " + specSeparator + " " + stadium.getDistance2parking());
        txtRimHeight.setText(getString(R.string.activity_stadium_spec_rim_height) + " " + specSeparator + " " + stadium.getRimHeight());
        txtRimNumber.setText(getString(R.string.activity_stadium_spec_rim_number) + " " + specSeparator + " " + stadium.getRimNumber());
        txtSpotlight.setText(getString(R.string.activity_stadium_spec_spotlight) + " " + specSeparator + " " + stadium.getSpotlight());
        txtFence.setText(getString(R.string.activity_stadium_spec_fence) + " " + specSeparator + " " + stadium.getFence());
        txtParking.setText(getString(R.string.activity_stadium_spec_parking) + " " + specSeparator + " " + stadium.getParking());
        txtBasketNet.setText(getString(R.string.activity_stadium_spec_basket_net) + " " + specSeparator + " " + stadium.getBasketNet());
        txtScoreLine.setText(getString(R.string.activity_stadium_spec_score_line) + " " + specSeparator + " " + stadium.getScoreline());
        txtLines.setText(getString(R.string.activity_stadium_spec_lines) + " " + specSeparator + " " + stadium.getLines());

        btnCompleteStadiumDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Redirect.sendToTelegram(StadiumActivity.this, UPDATE_STADIUM_INFO_BOT + stadium.getId(),
                            Telegram.DEFAULT_BOT);
                } catch (IllegalArgumentException unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });

        btnReportStadium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Redirect.sendToTelegram(StadiumActivity.this, REPORT_STADIUM_BOT + stadium.getId(),
                            Telegram.DEFAULT_BOT);
                } catch (IllegalArgumentException unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });

        btnReserveStadium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Redirect.sendToTelegram(StadiumActivity.this, RESERVE_STADIUM_BOT + stadium.getId()
                    , Telegram.DEFAULT_BOT);
                } catch (IllegalArgumentException unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });


        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Redirect.sendToTelegram(StadiumActivity.this, STADIUM_IMAGE_BOT + stadium.getId()
                            , Telegram.DEFAULT_BOT);
                } catch (IllegalArgumentException unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });

        // stadium gallery
        List<String> galleryImages = stadium.getImages();
        if (galleryImages.size() == 0) {
            galleryImages.add("http://berimbasket.ir/bball/bots/playgroundphoto/123423743522345.jpg");
            galleryImages.add("http://berimbasket.ir/bball/bots/playgroundphoto/123423743522345.jpg");
            galleryImages.add("http://berimbasket.ir/bball/bots/playgroundphoto/123423743522345.jpg");
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


    private void initStadiumBaseInfo(StadiumBaseEntity stadiumBase) {
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
        WebApiClient.getStadiumApi().getStadium(stadiumId, pusheId, userName, lang).enqueue(new Callback<List<Stadium>>() {
            @Override
            public void onResponse(Call<List<Stadium>> call, Response<List<Stadium>> response) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    List<Stadium> stadiums = response.body();
                    if (stadiums != null) {
                        Stadium stadium = stadiums.get(0);
                        if (fromScratch) {
                            StadiumBaseEntity entity = new StadiumBaseEntity(stadium.getId(), stadium.getTitle(),
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
        Redirect.sendToCustomTab(StadiumActivity.this, EXTERNAL_WEB_STADIUM_URL + stadiumId);
    }
}
