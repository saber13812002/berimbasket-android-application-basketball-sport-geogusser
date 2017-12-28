package ir.berimbasket.app.data.network.endpoint;

import ir.berimbasket.app.data.network.model.Update;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to check for app update
 */

public interface UpdateApi {

    @GET("update.php")
    Call<Update> checkForUpdate(@Query("package") String packageName, @Query("version") String version,
                                @Query("username") String username, @Query("pusheid") String pusheId);
}
