package com.artursworld.nccn.model.pereferences;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.artursworld.nccn.R;
import com.artursworld.nccn.controller.util.Strings;

public class ElasticSearchPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new ElasticSearchPreferenceFragment()).commit();
    }

    public static class ElasticSearchPreferenceFragment extends PreferenceFragment {

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_elasticsearch);
        }

        @Override
        public void onResume() {
            super.onResume();
            ListPreference dataPref = (ListPreference) findPreference(Strings.getStringByRId(R.string.c_select_protocol));
            if(dataPref.getValue() == null){
                dataPref.setValueIndex(0);
            }
        }
    }

}