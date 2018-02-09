package ir.berimbasket.app.data.network.endpoint;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by mohammad hosein on 26/01/2018.
 */

public interface FeedbackApi {
    @Headers("Content-Type: application/json")
    @POST("www/feedback.php")
    Call<Void> sendFeedback(@Query("pusheid") String pusheid, @Query("username") String username,
                            @Body Map<String, String> body, @Query("lang") String lang);
}
