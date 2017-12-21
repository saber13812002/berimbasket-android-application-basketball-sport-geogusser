package ir.berimbasket.app.api.endpoint;

import java.util.List;

import ir.berimbasket.app.api.model.Login;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to login user
 */

public interface LoginApi extends BerimBasket{

    @GET("getStatusLoginByUsernamePassword.php")
    Call<List<Login>> login(@Query("mac") String mac, @Query("username") String username, @Query("password") String password,
                            @Query("pusheid") String pusheId);
}
