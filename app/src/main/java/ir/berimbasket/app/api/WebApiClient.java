package ir.berimbasket.app.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ir.berimbasket.app.api.endpoint.BerimBasket;
import ir.berimbasket.app.api.endpoint.GeneralIntentApi;
import ir.berimbasket.app.api.endpoint.LocationApi;
import ir.berimbasket.app.api.endpoint.LoginApi;
import ir.berimbasket.app.api.endpoint.MapMarkerApi;
import ir.berimbasket.app.api.endpoint.MatchApi;
import ir.berimbasket.app.api.endpoint.MissionApi;
import ir.berimbasket.app.api.endpoint.PlayerApi;
import ir.berimbasket.app.api.endpoint.RegisterApi;
import ir.berimbasket.app.api.endpoint.StadiumApi;
import ir.berimbasket.app.api.endpoint.UpdateApi;
import ir.berimbasket.app.api.gson.BooleanDefaultAdapter;
import ir.berimbasket.app.api.gson.IntegerDefaultAdapter;
import ir.berimbasket.app.api.gson.StringDefaultAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mahdi on 12/21/2017.
 * prepare endpoint objects to access an api client in order to call a service
 */

public class WebApiClient {

    public static MissionApi getMissionApi() {
        return buildSimpleClient(BerimBasket.BBAL_BASE_URL)
                .create(MissionApi.class);
    }

    public static GeneralIntentApi getGeneralIntentApi() {
        return buildSimpleClient(BerimBasket.BBAL_BASE_URL)
                .create(GeneralIntentApi.class);
    }

    public static LoginApi getLoginApi() {
        return buildSimpleClient(BerimBasket.BBAL_BASE_URL)
                .create(LoginApi.class);
    }

    public static PlayerApi getPlayerApi() {
        return buildSimpleClient(BerimBasket.BBAL_BASE_URL)
                .create(PlayerApi.class);
    }

    public static RegisterApi getRegisterApi() {
        return buildSimpleClient(BerimBasket.BBAL_BASE_URL)
                .create(RegisterApi.class);
    }

    public static MapMarkerApi getSetMarkerApi() {
        return buildSimpleClient(BerimBasket.BBAL_BASE_URL)
                .create(MapMarkerApi.class);
    }

    public static UpdateApi getUpdateApi() {
        return buildSimpleClient(BerimBasket.APP_BASE_URL)
                .create(UpdateApi.class);
    }

    public static StadiumApi getStadiumApi() {
        return buildSimpleClient(BerimBasket.BBAL_BASE_URL)
                .create(StadiumApi.class);
    }

    public static MatchApi getMatchApi() {
        return buildSimpleClient(BerimBasket.BBAL_BASE_URL)
                .create(MatchApi.class);
    }

    public static LocationApi getLocationApi() {
        return buildSimpleClient(BerimBasket.BBAL_BASE_URL)
                .create(LocationApi.class);
    }

    private static Retrofit buildSimpleClient(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .build();
    }

    private static Gson buildGson() {
            return new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(String.class, new StringDefaultAdapter())
                    .registerTypeAdapter(boolean.class, new BooleanDefaultAdapter())
                    .registerTypeAdapter(Boolean.class, new BooleanDefaultAdapter())
                    .create();
    }
}
