package ir.berimbasket.app.data.network.endpoint;

import java.util.List;

import ir.berimbasket.app.data.network.model.GeneralIntent;
import ir.berimbasket.app.ui.browser.BrowserActivity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 12/21/2017.
 * Endpoint to get general intent link used in {@link BrowserActivity}
 */

public interface GeneralIntentApi {

    @GET("getWebPageLinkByGeneralIntentByPusheId.php")
    Call<List<GeneralIntent>> getGeneralIntent(@Query("username") String username, @Query("pusheid") String pusheId);
}
