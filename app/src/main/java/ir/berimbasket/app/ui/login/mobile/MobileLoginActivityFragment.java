package ir.berimbasket.app.ui.login.mobile;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.berimbasket.app.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MobileLoginActivityFragment extends Fragment {

    public MobileLoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mobile_login, container, false);
    }
}
