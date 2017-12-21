package ir.berimbasket.app.api.endpoint;

import java.util.List;

import ir.berimbasket.app.api.model.Stadium;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to access stadium
 */

public interface StadiumApi extends BerimBasket{

    @GET("getPlayGroundJson.php")
    Call<List<Stadium>> getStadiumCards(@Query("id") int id, @Query("pusheid") String pusheId,
                                        @Query("username") String username);

    @GET("get.php")
    Call<List<Stadium>> getStadium(@Query("id") int id, @Query("pusheid") String pusheId,
                                   @Query("username") String username);
}
