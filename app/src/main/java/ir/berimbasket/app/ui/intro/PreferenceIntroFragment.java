package ir.berimbasket.app.ui.intro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import ir.berimbasket.app.R;
import ir.berimbasket.app.data.pref.PrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreferenceIntroFragment extends Fragment {


    public PreferenceIntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_intro_preference, container, false);

        final PrefManager pref = new PrefManager(getContext());
        String deviceLanguage = pref.getSettingsPrefLangList();
        String defaultLocation = pref.getSettingsPrefStateList();
        final String[] langListValues = getResources().getStringArray(R.array.pref_lang_list_values);
        final String[] stateListValues = getResources().getStringArray(R.array.pref_state_list_values);

        Spinner spinnerLanguage = root.findViewById(R.id.fragmentIntroPreference_spinnerLang);
        Spinner spinnerState = root.findViewById(R.id.fragmentIntroPreference_spinnerState);
        int langPosition = getItemPositionByString(langListValues, deviceLanguage);
        int statePosition = getItemPositionByString(stateListValues, defaultLocation);
        spinnerLanguage.setSelection(langPosition);
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pref.putSettingsPrefLangList(langListValues[position]);
                Log.d("Varzeshboard", "Spinner language item selected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerState.setSelection(statePosition);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pref.putSettingsPrefStateList(stateListValues[position]);
                Log.d("Varzeshboard", "Spinner state item selected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return root;
    }


    private int getItemPositionByString(String[] items, String selected) {
        for (int i = 0; i < items.length; i++)
            if (items[i].equals(selected))
                return i;
        return 0;
    }

}
