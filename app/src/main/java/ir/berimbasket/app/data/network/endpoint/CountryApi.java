package ir.berimbasket.app.data.network.endpoint;

import java.util.List;

import ir.berimbasket.app.data.network.model.Country;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CountryApi {
    @GET("all")
    Call<List<Country>> getCountryList();
}
