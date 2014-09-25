package com.gionji.switchuno.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.gionji.switchuno.R;

public class QuickPrefsFragment extends PreferenceFragment {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }
}
