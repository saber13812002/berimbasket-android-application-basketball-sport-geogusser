package ir.berimbasket.app.data.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import ir.berimbasket.app.data.network.endpoint.AnswerApi;
import ir.berimbasket.app.data.network.endpoint.BerimBasket;
import ir.berimbasket.app.data.network.endpoint.FeedbackApi;
import ir.berimbasket.app.data.network.endpoint.GeneralIntentApi;
import ir.berimbasket.app.data.network.endpoint.LocationApi;
import ir.berimbasket.app.data.network.endpoint.LoginApi;
import ir.berimbasket.app.data.network.endpoint.MapMarkerApi;
import ir.berimbasket.app.data.network.endpoint.MatchApi;
import ir.berimbasket.app.data.network.endpoint.MissionApi;
import ir.berimbasket.app.data.network.endpoint.PlayerApi;
import ir.berimbasket.app.data.network.endpoint.QuestionApi;
import ir.berimbasket.app.data.network.endpoint.RegisterApi;
import ir.berimbasket.app.data.network.endpoint.StadiumApi;
import ir.berimbasket.app.data.network.endpoint.UpdateApi;
import ir.berimbasket.app.data.network.gson.BooleanDefaultAdapter;
import ir.berimbasket.app.data.network.gson.IntegerDefaultAdapter;
import ir.berimbasket.app.data.network.gson.StringDefaultAdapter;
import okhttp3.OkHttpClient;
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

    public static QuestionApi getQuestionApi() {
        return buildSimpleClient(BerimBasket.BBAL_BASE_URL)
                .create(QuestionApi.class);
    }

    public static AnswerApi sendAnswerApi() {
        return buildSimpleClient(BerimBasket.BBAL_BASE_URL)
                .create(AnswerApi.class);
    }

    public static FeedbackApi postFeedbackApi() {
        return buildSimpleClient(BerimBasket.BBAL_BASE_URL)
                .create(FeedbackApi.class);
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
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Gson gsonAdapter = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                .registerTypeAdapter(String.class, new StringDefaultAdapter())
                .registerTypeAdapter(boolean.class, new BooleanDefaultAdapter())
                .registerTypeAdapter(Boolean.class, new BooleanDefaultAdapter())
                .create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gsonAdapter))
                .client(okHttpClient)
                .build();
    }

}
