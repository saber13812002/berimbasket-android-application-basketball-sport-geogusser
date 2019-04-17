package ir.berimbasket.app.data.network.endpoint;

import ir.berimbasket.app.data.network.model.RequestOTP;
import ir.berimbasket.app.data.network.model.VerifyOTP;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to login user
 */

public interface LoginApi {

//    @GET("getStatusLoginByUsernamePassword.php")
//    Call<List<Login>> login(@Query("mac") String mac, @Query("username") String username, @Query("password") String password,
//                            @Query("pusheid") String pusheId,
//                            @Query("lang") String lang);

    @GET("otp1.php")
    Call<RequestOTP> requestOTP(@Query("phone") String phone,
                                @Query("pusheid") String pusheid);

    @GET("otp2.php")
    Call<VerifyOTP> verifyOTP(@Query("phone") String phone,
                              @Query("pusheid") String pusheid,
                              @Query("code") String code);
}
