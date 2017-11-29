package ir.berimbasket.app.activity;

import android.os.AsyncTask;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.ronash.pushe.Pushe;
import de.hdodenhof.circleimageview.CircleImageView;
import ir.berimbasket.app.R;
import ir.berimbasket.app.activity.fragment.FragmentStadiumMap;
import ir.berimbasket.app.adapter.AdapterStadiumGallery;
import ir.berimbasket.app.entity.EntityStadium;
import ir.berimbasket.app.entity.EntityStadiumGallery;
import ir.berimbasket.app.exception.UnknownTelegramURL;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.PrefManager;
import ir.berimbasket.app.util.Redirect;

public class ActivityStadium extends AppCompatActivity {

    TextView txtStadiumName, txtStadiumTel, txtStadiumAddress, txtStadiumRound, txtTelegramChannel, txtInstagramId,
            txtDetailSection, txtRoof, txtDistance2Parking, txtRimHeight, txtRimNumber, txtSpotlight, txtFence, txtParking,
            txtBasketNet, txtScoreLine, txtLines;
    AppCompatButton btnCompleteStadiumDetail, btnAddImage;
    CircleImageView imgStadiumLogo;
    EntityStadium entityStadium;
    String stadiumLogoUrl;
    private ImageView btnReportStadium, btnReserveStadium;
    private static final String UPDATE_STADIUM_INFO_BOT = "https://t.me/berimbasketProfilebot?start=-";
    private static final String REPORT_STADIUM_BOT = "https://t.me/berimbasketreportbot?start=-";
    private static final String RESERVE_STADIUM_BOT = "https://t.me/Berimbasketreservebot?start=";
    private static final String STADIUM_IMAGE_BOT = "https://t.me/berimbasketuploadbot?start=";
    private static final String STADIUM_PHOTO_BASE_URL = "https://berimbasket.ir/bball/bots/playgroundphoto/";
    private static final String STADIUM_URL = "https://berimbasket.ir/bball/get.php?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadium);
        entityStadium = (EntityStadium) getIntent().getSerializableExtra("stadiumDetail");
        stadiumLogoUrl = getIntent().getStringExtra("stadiumLogoUrlPath");
        initToolbar();
        new GetStadium().execute(entityStadium.getId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Tracking the screen view (Analytics)
        ApplicationLoader.getInstance().trackScreenView(getString(R.string.analytics_screen_stadium));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // FIXME: 21/09/2017  setDisplayHomeAsUp show warning for nullpointer exception cause
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    // FIXME: 11/22/2017 use another entity for this class
    private void setStadiumInfo(EntityStadium entityStadium, String roof, String distance2Parking, String rimHeight,
                                String rimNumber, String spotlight, String fence, String parking, String basketNet,
                                String scoreLine, String lines) {
        String specSeparator = getString(R.string.activity_stadium_spec_separator);
        txtStadiumName.setText(entityStadium.getTitle());
        txtStadiumTel.setText(getString(R.string.activity_stadium_spec_phone) + " " + specSeparator + " " + "-");
        txtStadiumAddress.setText(getString(R.string.activity_stadium_spec_address) + " " + specSeparator + " " + entityStadium.getAddress());
        txtStadiumRound.setText(getString(R.string.activity_stadium_spec_work_time) + " " + specSeparator + " " + "-");
        txtTelegramChannel.setText(getString(R.string.activity_stadium_spec_telegram_channel) + " " + specSeparator + " " + entityStadium.getTelegramChannelId());
        txtInstagramId.setText(getString(R.string.activity_stadium_spec_instagram) + " " + specSeparator + " " + entityStadium.getInstagramId());
        txtRoof.setText(getString(R.string.activity_stadium_spec_roof) + " " + specSeparator + " " + roof);
        txtDistance2Parking.setText(getString(R.string.activity_stadium_spec_distance_2_parking) + " " + specSeparator + " " + distance2Parking);
        txtRimHeight.setText(getString(R.string.activity_stadium_spec_rim_height) + " " + specSeparator + " " + rimHeight);
        txtRimNumber.setText(getString(R.string.activity_stadium_spec_rim_number) + " " + specSeparator + " " + rimNumber);
        txtSpotlight.setText(getString(R.string.activity_stadium_spec_spotlight) + " " + specSeparator + " " + spotlight);
        txtFence.setText(getString(R.string.activity_stadium_spec_fence) + " " + specSeparator + " " + fence);
        txtParking.setText(getString(R.string.activity_stadium_spec_parking) + " " + specSeparator + " " + parking);
        txtBasketNet.setText(getString(R.string.activity_stadium_spec_basket_net) + " " + specSeparator + " " + basketNet);
        txtScoreLine.setText(getString(R.string.activity_stadium_spec_score_line) + " " + specSeparator + " " + scoreLine);
        txtLines.setText(getString(R.string.activity_stadium_spec_lines) + " " + specSeparator + " " + lines);
    }

    private void initViewsAndListeners() {

        txtStadiumName = findViewById(R.id.txtStadiumName);
        txtStadiumTel = findViewById(R.id.txtStadiumTel);
        txtStadiumAddress = findViewById(R.id.txtStadiumAddress);
        txtStadiumRound = findViewById(R.id.txtStadiumRound);
        txtTelegramChannel = findViewById(R.id.txtTelegramChannel);
        txtInstagramId = findViewById(R.id.txtInstagramId);
        txtDetailSection = findViewById(R.id.txtDetailSection);
        txtRoof = findViewById(R.id.txtRoof);
        txtDistance2Parking = findViewById(R.id.txtDistance2Parking);
        txtRimHeight = findViewById(R.id.txtRimHeight);
        txtRimNumber = findViewById(R.id.txtRimNumber);
        txtSpotlight = findViewById(R.id.txtSpotlight);
        txtFence = findViewById(R.id.txtFence);
        txtParking = findViewById(R.id.txtParking);
        txtBasketNet = findViewById(R.id.txtBasketNet);
        txtScoreLine = findViewById(R.id.txtScoreLine);
        txtLines = findViewById(R.id.txtLines);
        imgStadiumLogo = findViewById(R.id.imgStadiumLogo);
        btnReportStadium = findViewById(R.id.btnReportStadium);
        btnReserveStadium = findViewById(R.id.btnReserveStadium);
        btnAddImage = findViewById(R.id.btnAddImage);
        Picasso.with(ActivityStadium.this)
                .load("https://berimbasket.ir/" + stadiumLogoUrl)
                .resize(100, 100)
                .placeholder(R.drawable.stadium1)
                .error(R.drawable.stadium1)
                .centerInside()
                .into(imgStadiumLogo);

        btnCompleteStadiumDetail = findViewById(R.id.btnCompleteStadiumDetail);

        btnCompleteStadiumDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Redirect.sendToTelegram(ActivityStadium.this, UPDATE_STADIUM_INFO_BOT + entityStadium.getId());
                } catch (UnknownTelegramURL unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });

        btnReportStadium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Redirect.sendToTelegram(ActivityStadium.this, REPORT_STADIUM_BOT + entityStadium.getId());
                } catch (UnknownTelegramURL unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });

        btnReserveStadium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Redirect.sendToTelegram(ActivityStadium.this, RESERVE_STADIUM_BOT + entityStadium.getId());
                } catch (UnknownTelegramURL unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });


        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Redirect.sendToTelegram(ActivityStadium.this, STADIUM_IMAGE_BOT + entityStadium.getId());
                } catch (UnknownTelegramURL unknownTelegramURL) {
                    // do nothing yet
                }
            }
        });
    }


    private void initStadiumMap() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentStadiumMap fragmentStadiumMap = new FragmentStadiumMap();
        Bundle bundle = new Bundle();
        bundle.putSerializable("stadiumDetail", entityStadium);
        fragmentStadiumMap.setArguments(bundle);
        fragmentTransaction.replace(R.id.mapContainer, fragmentStadiumMap);
        fragmentTransaction.commit();
    }


    private void initGalleryRecycler() {
        String[] galleryImages = entityStadium.getImages();
        ArrayList<EntityStadiumGallery> stadiumGalleryList = new ArrayList<>();
        for (int i = 0; i < galleryImages.length; i++) {
            EntityStadiumGallery entityStadiumGallery = new EntityStadiumGallery();
            entityStadiumGallery.setId(i);
            if (entityStadium.getImageType() == EntityStadium.IMAGE_TYPE_JPG) {
                entityStadiumGallery.setUrl(STADIUM_PHOTO_BASE_URL + galleryImages[i] + ".jpg");
            } else {
                entityStadiumGallery.setUrl(STADIUM_PHOTO_BASE_URL + galleryImages[i] + ".png");
            }
            stadiumGalleryList.add(entityStadiumGallery);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerStadiumGallery);
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


    private class GetStadium extends AsyncTask<Integer, Void, Void> {

        String roof, distance2Parking, rimHeight, rimNumber, spotlight, fence, parking, basketNet, scoreLine, lines;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... stadiumId) {
            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);

            PrefManager pref = new PrefManager(getApplicationContext());
            String pusheId = Pushe.getPusheId(getApplicationContext());
            String userName = pref.getUserName();
            String jsonStr = sh.makeServiceCall(STADIUM_URL + stadiumId[0] + "&username=" + userName + "&pusheid=" + pusheId);
            if (jsonStr != null) {
                try {
                    JSONArray locations = new JSONArray(jsonStr);

                    for (int i = 0; i < locations.length(); i++) {
                        JSONObject c = locations.getJSONObject(i);

                        String id = c.getString("id");
                        String title = c.getString("title");
                        String latitude = c.getString("PlaygroundLatitude");
                        String longitude = c.getString("PlaygroundLongitude");
                        String type = c.getString("PlaygroundType");
                        String zoomLevel = c.getString("ZoomLevel");
                        String address = c.getString("address");
                        String images = c.getString("PgImages");
                        String instagramId = c.getString("PgInstagramId");
                        String telegramChannelId = c.getString("PgTlgrmChannelId");
                        String telegramGroupId = c.getString("PgTlgrmGroupJoinLink");
                        String telegramAdminId = c.getString("PgTlgrmGroupAdminId");
                        roof = c.getString("roof");
                        distance2Parking = c.getString("distance2parking");
                        rimHeight = c.getString("rimHeight");
                        rimNumber = c.getString("rimNumber");
                        spotlight = c.getString("spotlight");
                        fence = c.getString("fence");
                        parking = c.getString("parking");
                        basketNet = c.getString("basketnet");
                        scoreLine = c.getString("scoreline");
                        lines = c.getString("lines");

                        entityStadium = new EntityStadium();

                        entityStadium.setId(id != "null" ? Integer.parseInt(id) : -1);
                        entityStadium.setTitle(title);
                        entityStadium.setLatitude(latitude);
                        entityStadium.setLongitude(longitude);
                        entityStadium.setAddress(address);
                        entityStadium.setTelegramGroupId(telegramGroupId);
                        entityStadium.setTelegramChannelId(telegramChannelId);
                        entityStadium.setTelegramAdminId(telegramAdminId);
                        entityStadium.setInstagramId(instagramId);
                        entityStadium.setImages(images.split("\\.[a-z]{3}"));
                        if (images.contains("png")){
                            entityStadium.setImageType(EntityStadium.IMAGE_TYPE_PNG);
                        } else if (images.contains("jpg")) {
                            entityStadium.setImageType(EntityStadium.IMAGE_TYPE_JPG);
                        }
                        entityStadium.setType(type);
                        entityStadium.setZoomLevel(zoomLevel != "null" ? Integer.parseInt(zoomLevel) : -1);

                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ActivityStadium.this,
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            initViewsAndListeners();
            initStadiumMap();
            try {
                initGalleryRecycler();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setStadiumInfo(entityStadium, roof, distance2Parking, rimHeight, rimNumber, spotlight, fence, parking
                    , basketNet, scoreLine, lines);
        }
    }

}
