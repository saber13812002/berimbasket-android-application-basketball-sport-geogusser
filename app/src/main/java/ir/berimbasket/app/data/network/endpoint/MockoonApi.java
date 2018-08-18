package ir.berimbasket.app.data.network.endpoint;


import ir.berimbasket.app.data.network.model.RequestOTP;
import ir.berimbasket.app.data.network.model.VerifyOTP;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Endpoint to mock api in localhost
 */
public interface MockoonApi {

    @GET("requestOTP")
    Call<RequestOTP> requestOTP();

    @GET("verifyOTP")
    Call<VerifyOTP> verifyOTP();
}
