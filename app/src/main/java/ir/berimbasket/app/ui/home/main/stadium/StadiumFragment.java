package ir.berimbasket.app.ui.home.main.stadium;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import co.ronash.pushe.Pushe;
import ir.berimbasket.app.R;
import ir.berimbasket.app.data.entity.EntityStadium;
import ir.berimbasket.app.data.network.HttpFunctions;
import ir.berimbasket.app.data.pref.PrefManager;
import ir.berimbasket.app.ui.stadium.StadiumActivity;
import ir.berimbasket.app.util.AnalyticsHelper;

public class StadiumFragment extends Fragment implements StadiumAdapter.StadiumListListener {

    private final static String STADIUM_URL = "https://berimbasket.ir/bball/get.php";
    private static final String STADIUM_PHOTO_BASE_URL = "https://berimbasket.ir";

    private StadiumAdapter adapter;
    private ProgressBar progress;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StadiumFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stadium_list, container, false);
        progress = view.findViewById(R.id.progressStadium);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerStadium);

        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new StadiumAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        new GetStadium().execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Track screen view (Analytics)
        AnalyticsHelper.getInstance().trackScreenView(getContext(), this.getClass().getSimpleName());
    }

    @Override
    public void onStadiumItemClick(EntityStadium stadium) {
        Intent intent = new Intent(getContext(), StadiumActivity.class);
        intent.putExtra("stadiumDetail", stadium);
        if (stadium.getImages().length != 0) {
            intent.putExtra("stadiumLogoUrlPath", stadium.getImages()[0]);
        } else {
            intent.putExtra("stadiumLogoUrlPath", "http://test");
        }
        startActivity(intent);
    }

    private class GetStadium extends AsyncTask<Void, Void, List<EntityStadium>> {

        private String pusheId;
        private String userName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
            pusheId = Pushe.getPusheId(getContext());
            userName = new PrefManager(getContext()).getUserName();
        }

        @Override
        protected List<EntityStadium>  doInBackground(Void... voids) {
            HttpFunctions sh = new HttpFunctions(HttpFunctions.RequestType.GET);
            String urlParams = String.format("id=0&pusheid=%s&username=%s", pusheId, userName);
            String jsonStr = sh.makeServiceCall(STADIUM_URL + "?" + urlParams);
            ArrayList<EntityStadium> stadiumList = new ArrayList<>();
            if (jsonStr != null) {
                try {
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(jsonStr);
                    JsonArray locations = element.getAsJsonArray();

                    for (int i = 0; i < locations.size(); i++) {
                        JsonObject c = locations.get(i).getAsJsonObject();

                        String id = c.get("id").getAsString();
                        String title = c.get("title").getAsString();
                        String latitude = c.get("PlaygroundLatitude").getAsString();
                        String longitude = c.get("PlaygroundLongitude").getAsString();
                        String type = c.get("PlaygroundType").getAsString();
                        String zoomLevel = c.get("ZoomLevel").getAsString();
                        String address = c.get("address").getAsString();
                        JsonArray imagesArray = c.get("images").getAsJsonArray();
                        String[] images = new String[imagesArray.size()];
                        for (int j = 0; j < imagesArray.size(); j++) {
                            images[j] = imagesArray.get(j).getAsString();
                        }
                        String thumb = "http://test";
                        if (images.length != 0) {
                            thumb = STADIUM_PHOTO_BASE_URL + images[0];
                        }
//                        String instagramId = c.getString("PgInstagramId");
//                        String telegramChannelId = c.getString("PgTlgrmChannelId");
//                        String telegramGroupId = c.getString("PgTlgrmGroupJoinLink");
//                        String telegramAdminId = c.getString("PgTlgrmGroupAdminId");

                        EntityStadium entityStadium = new EntityStadium();

                        entityStadium.setId(id != "null" ? Integer.parseInt(id) : -1);
                        entityStadium.setTitle(title);
                        entityStadium.setLatitude(latitude);
                        entityStadium.setLongitude(longitude);
                        entityStadium.setAddress(address);
//                        entityStadium.setTelegramGroupId(telegramGroupId);
//                        entityStadium.setTelegramChannelId(telegramChannelId);
//                        entityStadium.setTelegramAdminId(telegramAdminId);
//                        entityStadium.setInstagramId(instagramId);
                        entityStadium.setImages(images);
                        if (images.length != 0) {
                            if (images[0].contains("png")){
                                entityStadium.setImageType(EntityStadium.IMAGE_TYPE_PNG);
                            } else if (images[0].contains("jpg")) {
                                entityStadium.setImageType(EntityStadium.IMAGE_TYPE_JPG);
                            }
                        }
                        entityStadium.setThumbnail(thumb);
                        entityStadium.setType(type);
                        entityStadium.setZoomLevel(zoomLevel != "null" ? Integer.parseInt(zoomLevel) : -1);

                        stadiumList.add(entityStadium);


                    }
                } catch (final JsonParseException e) {
                    // do nothing yet
                }
            }
            return stadiumList;
        }

        @Override
        protected void onPostExecute(List<EntityStadium>  result) {
            super.onPostExecute(result);
            progress.setVisibility(View.INVISIBLE);
            if (getView() != null) {
                adapter.swapDataSource(result);
            }
        }
    }
}
