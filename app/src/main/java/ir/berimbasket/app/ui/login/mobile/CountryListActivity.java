package ir.berimbasket.app.ui.login.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.ahmadrosid.svgloader.SvgLoader;

import java.util.List;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.env.KeyConstants;
import ir.berimbasket.app.data.network.WebApiClient;
import ir.berimbasket.app.data.network.model.Country;
import ir.berimbasket.app.ui.base.BaseActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryListActivity extends BaseActivity implements CountryListAdapter.CountryListListener {

    private RecyclerView countryRecycler;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        countryRecycler = findViewById(R.id.countryRecycler);
        progress = findViewById(R.id.progress);

        WebApiClient.getCountryApi(this).getCountryList().enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                progress.setVisibility(View.INVISIBLE);
                List<Country> countries = response.body();
                LinearLayoutManager layoutManager = new LinearLayoutManager(CountryListActivity.this);
                CountryListAdapter countryListAdapter = new CountryListAdapter(countries,
                        CountryListActivity.this,
                        CountryListActivity.this);
                countryRecycler.setLayoutManager(layoutManager);
                countryRecycler.setAdapter(countryListAdapter);
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SvgLoader.pluck().close();
    }

    @Override
    public void onCountryItemClick(Country country) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KeyConstants.COUNTRY_OBJECT_KEY, country);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
