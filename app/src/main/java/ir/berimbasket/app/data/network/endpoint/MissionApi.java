package ir.berimbasket.app.data.network.endpoint;

import java.util.List;

import ir.berimbasket.app.data.network.model.Mission;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to access missions and manipulate it if needed
 */

public interface MissionApi {

    @GET("getMission.php")
    Call<List<Mission>> getMissions(@Query("user") String user, @Query("username") String username,
                                    @Query("pusheid") String pusheId,
                                    @Query("lang") String lang);
}
