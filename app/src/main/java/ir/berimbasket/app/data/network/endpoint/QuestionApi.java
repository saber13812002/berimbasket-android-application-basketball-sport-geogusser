package ir.berimbasket.app.data.network.endpoint;

import ir.berimbasket.app.data.network.model.Question;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mohammad hosein on 26/01/2018.
 */

public interface QuestionApi {
    @GET("www/getQuestion.php")
    Call<Question> getQuestion(@Query("pusheid") String pusheid, @Query("username") String username);
}
