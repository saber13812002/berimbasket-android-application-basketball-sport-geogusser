package ir.berimbasket.app.data.network.endpoint;

import ir.berimbasket.app.data.network.model.TokenResponse;
import ir.berimbasket.app.data.network.model.ValidateResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Endpoint to create new token, validate, ...
 */
public interface TokenApi {

    @FormUrlEncoded
    @POST("token")
    Call<TokenResponse> getToken(@Query("pusheid") String pusheId,
                                 @Query("lang") String lang,
                                 @Field("username") String username,
                                 @Field("password") String password);


    @POST("token/validate")
    Call<ValidateResponse> validateToken(@Query("pusheid") String pusheId,
                                         @Query("username") String username,
                                         @Query("lang") String lang,
                                         @Header("Authorization") String token);
}
