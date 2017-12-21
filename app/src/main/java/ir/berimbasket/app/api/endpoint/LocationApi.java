package ir.berimbasket.app.api.endpoint;

import ir.berimbasket.app.api.model.Location;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to work with location stuffs
 */

public interface LocationApi extends BerimBasket{

    @GET("setLoc.php")
    Call<Location> setLocation(@Query("token") String token, @Query("lat") String lat, @Query("long") String longitude,
                               @Query("title") String title, @Query("username") String username, @Query("pusheid") String pusheId);
}
