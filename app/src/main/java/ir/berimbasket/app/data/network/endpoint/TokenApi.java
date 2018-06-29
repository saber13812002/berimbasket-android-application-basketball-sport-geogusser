package ir.berimbasket.app.data.network.endpoint;

import ir.berimbasket.app.data.network.model.Credentials;
import ir.berimbasket.app.data.network.model.TokenResponse;
import ir.berimbasket.app.data.network.model.ValidateResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Endpoint to create new token, validate, ...
 */
public interface TokenApi {

    @POST("token")
    Call<TokenResponse> getToken(@Body Credentials credentials);

    @POST("token/validate")
    Call<ValidateResponse> validateToken(@Header("Authorization") String token);
}
