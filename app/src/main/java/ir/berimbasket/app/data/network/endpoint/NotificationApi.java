package ir.berimbasket.app.data.network.endpoint;

import java.util.List;

import ir.berimbasket.app.data.network.model.Notification;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mahdi on 2/9/2018.
 */

public interface NotificationApi {

    @GET("inbox.php")
    Call<List<Notification>> getNotificationHistory(@Query("from") int from,
                                                    @Query("num") int num,
                                                    @Query("format") String format,
                                                    @Query("pusheid") String pusheId,
                                                    @Query("username") String username,
                                                    @Query("lang") String lang);
}
