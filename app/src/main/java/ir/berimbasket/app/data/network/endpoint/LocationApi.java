package ir.berimbasket.app.data.network.endpoint;

import ir.berimbasket.app.data.network.model.Location;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to work with location stuffs
 */

public interface LocationApi {

    @GET("setLoc.php")
    Call<Location> setLocation(@Query("token") String token, @Query("lat") String lat, @Query("long") String longitude,
                               @Query("title") String title, @Query("username") String username, @Query("pusheid") String pusheId);
}
