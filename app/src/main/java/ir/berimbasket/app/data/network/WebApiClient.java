package ir.berimbasket.app.data.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
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
import ir.berimbasket.app.data.network.endpoint.NotificationApi;
import ir.berimbasket.app.data.network.endpoint.PlayerApi;
import ir.berimbasket.app.data.network.endpoint.ProfileApi;
import ir.berimbasket.app.data.network.endpoint.QuestionApi;
import ir.berimbasket.app.data.network.endpoint.RegisterApi;
import ir.berimbasket.app.data.network.endpoint.StadiumApi;
import ir.berimbasket.app.data.network.endpoint.TokenApi;
import ir.berimbasket.app.data.network.endpoint.UpdateApi;
import ir.berimbasket.app.data.network.gson.BooleanDefaultAdapter;
import ir.berimbasket.app.data.network.gson.IntegerDefaultAdapter;
import ir.berimbasket.app.data.network.gson.StringDefaultAdapter;
import ir.berimbasket.app.data.pref.PrefManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mahdi on 12/21/2017.
 * prepare endpoint objects to access an api client in order to call a service
 */

public class WebApiClient {

    public static ProfileApi getProfileApi(Context context) {
        return buildApiClient(BerimBasket.JWT_URL, context)
                .create(ProfileApi.class);
    }

    public static TokenApi getTokenApi(Context context) {
        return buildApiClient(BerimBasket.AUTH_URL, context)
                .create(TokenApi.class);
    }

    public static MissionApi getMissionApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(MissionApi.class);
    }

    public static QuestionApi getQuestionApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(QuestionApi.class);
    }

    public static AnswerApi sendAnswerApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(AnswerApi.class);
    }

    public static FeedbackApi postFeedbackApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(FeedbackApi.class);
    }

    public static GeneralIntentApi getGeneralIntentApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(GeneralIntentApi.class);
    }

    public static LoginApi getLoginApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(LoginApi.class);
    }

    public static PlayerApi getPlayerApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(PlayerApi.class);
    }

    public static RegisterApi getRegisterApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(RegisterApi.class);
    }

    public static MapMarkerApi getSetMarkerApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(MapMarkerApi.class);
    }

    public static UpdateApi getUpdateApi(Context context) {
        return buildApiClient(BerimBasket.APP_BASE_URL, context)
                .create(UpdateApi.class);
    }

    public static StadiumApi getStadiumApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(StadiumApi.class);
    }

    public static MatchApi getMatchApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(MatchApi.class);
    }

    public static LocationApi getLocationApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(LocationApi.class);
    }

    public static NotificationApi getNotificationApi(Context context) {
        return buildApiClient(BerimBasket.BBAL_BASE_URL, context)
                .create(NotificationApi.class);
    }

    private static Retrofit buildApiClient(String baseUrl, Context context) {
        final PrefManager pref = new PrefManager(context);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("UserType", pref.getIsLoggedIn()? "LoggedIn":"Visitor");

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(logging)
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
