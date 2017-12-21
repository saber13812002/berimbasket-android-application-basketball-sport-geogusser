package ir.berimbasket.app.api.endpoint;

import ir.berimbasket.app.api.model.SetMarker;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to access map markers and manipulate them
 */

public interface MapMarkerApi  {

    @GET("set.php")
    Call<SetMarker> setMarker(@Query("token") String token, @Query("lat") String lat, @Query("long") String longitude,
                              @Query("title") String title, @Query("username") String username, @Query("pusheid") String pusheId);
}
