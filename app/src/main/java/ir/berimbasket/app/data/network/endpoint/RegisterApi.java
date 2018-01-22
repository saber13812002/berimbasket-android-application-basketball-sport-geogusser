package ir.berimbasket.app.data.network.endpoint;

import java.util.List;

import ir.berimbasket.app.data.network.model.CheckUsername;
import ir.berimbasket.app.data.network.model.Register;
import ir.berimbasket.app.data.network.model.VerifyBot;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to register user
 */

public interface RegisterApi  {

    @GET("getExistOrNotThisNewRequestedUsername.php")
    Call<List<CheckUsername>> checkUsername(@Query("mac") String mac,
                                            @Query("username") String username,
                                            @Query("pusheid") String pusheId,
                                            @Query("lang") String lang);

    @GET("setPasswordForThisUsername.php")
    Call<List<Register>> register(@Query("mac") String mac,
                                  @Query("username") String username,
                                  @Query("password") String password,
                                  @Query("pusheid") String pusheId,
                                  @Query("lang") String lang);

    @GET("getSignupStatusByVerificationCodeAndMAC.php")
    Call<List<VerifyBot>> verifyBotCode(@Query("mac") String mac, @Query("code") String code, @Query("pusheid") String pusheId,
                                        @Query("username") String username,
                                        @Query("lang") String lang);
}
