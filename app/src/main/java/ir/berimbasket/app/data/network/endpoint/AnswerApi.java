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

public interface AnswerApi {
    @Headers("Content-Type: application/json")
    @POST("www/setAnswer.php")
    Call<Void> sendAnswer(@Query("pusheid") String pusheid, @Body Map<String, String> body);
}
