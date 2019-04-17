package ir.berimbasket.app.data.network.endpoint;

import ir.berimbasket.app.data.network.model.Profile;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ProfileApi {

    @GET("users/me")
    Call<Profile> getMe(@Query("pusheid") String pusheId,
                        @Query("username") String username,
                        @Query("lang") String lang,
                        @Header("Authorization") String token);
}
