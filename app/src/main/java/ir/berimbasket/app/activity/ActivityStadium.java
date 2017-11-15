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

import de.hdodenhof.circleimageview.CircleImageView;
import ir.berimbasket.app.R;
import ir.berimbasket.app.activity.fragment.FragmentStadiumMap;
import ir.berimbasket.app.adapter.AdapterStadiumGallery;
import ir.berimbasket.app.entity.EntityStadium;
import ir.berimbasket.app.entity.EntityStadiumGallery;
import ir.berimbasket.app.network.HttpFunctions;
import ir.berimbasket.app.util.ApplicationLoader;
import ir.berimbasket.app.util.SendTo;

public class ActivityStadium extends AppCompatActivity {

    TextView txtStadiumName, txtStadiumTel, txtStadiumAddress, txtStadiumRound, txtTelegramChannel, txtInstagramId, txtDetailSection;
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
        ApplicationLoader.getInstance().trackScreenView("Stadium Screen");
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // FIXME: 21/09/2017  setDisplayHomeAsUp show warning for nullpointer exception cause
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void getStadiumInfo(EntityStadium entityStadium) {
        txtStadiumName.setText(entityStadium.getTitle());
        txtStadiumTel.setText("تلفن : " + "-");
        txtStadiumAddress.setText("آدرس : " + entityStadium.getAddress());
        txtStadiumRound.setText("ساعت کاری : " + "-");
        txtTelegramChannel.setText("کانال تلگرام : " + entityStadium.getTelegramChannelId());
        txtInstagramId.setText("اینستاگرام" + entityStadium.getInstagramId());
    }

    private void initViewsAndListeners() {

        txtStadiumName = findViewById(R.id.txtStadiumName);
        txtStadiumTel = findViewById(R.id.txtStadiumTel);
        txtStadiumAddress = findViewById(R.id.txtStadiumAddress);
        txtStadiumRound = findViewById(R.id.txtStadiumRound);
        txtTelegramChannel = findViewById(R.id.txtTelegramChannel);
        txtInstagramId = findViewById(R.id.txtInstagramId);
        txtDetailSection = findViewById(R.id.txtDetailSection);
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
                SendTo.sendToTelegramChat(ActivityStadium.this, UPDATE_STADIUM_INFO_BOT + entityStadium.getId());
            }
        });

        btnReportStadium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendTo.sendToTelegramChat(ActivityStadium.this, REPORT_STADIUM_BOT + entityStadium.getId());
            }
        });

        btnReserveStadium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendTo.sendToTelegramChat(ActivityStadium.this, RESERVE_STADIUM_BOT + entityStadium.getId());
            }
        });


        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendTo.sendToTelegramChat(ActivityStadium.this, STADIUM_IMAGE_BOT + entityStadium.getId());
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
            entityStadiumGallery.setUrl(STADIUM_PHOTO_BASE_URL + galleryImages[i] + ".jpg");
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... stadiumId) {
            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);

            String jsonStr = sh.makeServiceCall(STADIUM_URL + stadiumId[0]);
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
                        entityStadium.setImages(images.split(".jpg"));
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
            getStadiumInfo(entityStadium);
        }
    }

}
