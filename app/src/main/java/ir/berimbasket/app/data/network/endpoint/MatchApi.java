package ir.berimbasket.app.data.network.endpoint;

import java.util.List;

import ir.berimbasket.app.data.network.model.Match;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to access match
 */

public interface MatchApi {

    @GET("getScore.php")
    Call<List<Match>> getMatches(@Query("pusheid") String pusheId,
                                 @Query("username") String username,
                                 @Query("lang") String lang);
}
