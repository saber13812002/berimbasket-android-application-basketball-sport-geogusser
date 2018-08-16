package ir.berimbasket.app.ui.login.mobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.network.model.Country;

/**
 * A placeholder fragment containing a simple view.
 */
public class MobileLoginFragment extends Fragment {

    private final static int REQUEST_CODE = 100;

    private TextInputEditText edtCountry;
    private Country country;

    private MobileLoginListener listener;

    public interface MobileLoginListener {
        void onSignInTelegramClickListener();

        void onSignInEmailClickListener();

        void onSignInClickListener();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (MobileLoginListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mobile_login, container, false);
        edtCountry = view.findViewById(R.id.edtCountry);
        edtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // As seyed asked, no need to use this for now
//                Intent intent = new Intent(getActivity(), CountryListActivity.class);
//                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        initCountry();
        initListeners(view);

        return view;
    }

    private void initListeners(final View view) {
        view.findViewById(R.id.btnSignInEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSignInEmailClickListener();
                }
            }
        });
        view.findViewById(R.id.btnSignInTelegram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSignInTelegramClickListener();
                }
            }
        });
        view.findViewById(R.id.btnMobileSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSignInClickListener();
                }
            }
        });
    }

    private void initCountry() {
        //initial country code and name on edtCountry based on ip here
        Country country = new Country();
        country.setName("ایران");
        country.setCode("98");
        country.setNativeName("ایران");
        edtCountry.setText(getString(R.string.general_country_code_format, country.getName(), country.getCode()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Country country = (Country) data.getSerializableExtra(CountryListActivity.INTENT_RESULT_KEY);
            this.country = country;
            edtCountry.setText(getString(R.string.general_country_code_format, country.getName(), country.getCode()));
        }
    }
}
