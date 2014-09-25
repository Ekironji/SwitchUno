
//   NOT USED   

package com.gionji.switchuno;

import android.os.Bundle;
import android.preference.PreferenceActivity;
 
public class QuickPrefsActivity extends PreferenceActivity {
 
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.settings);
 
    }
}
