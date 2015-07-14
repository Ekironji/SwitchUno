package com.gionji.switchuno.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gionji.switchuno.R;
import com.gionji.switchuno.SwitchMainActivity;
import com.gionji.switchuno.utils.GionjiUtils;
import com.gionji.switchuno.utils.RelaysManager;
import com.gionji.switchuno.views.CircolareConIndicatore;
import com.gionji.switchuno.views.CircolareConIndicatore.OnSwitchEventListener;
import com.gionji.switchuno.views.CircolareConIndicatoreLed;

public class SwitchFragment extends Fragment {
	
	private final String TAG = "SwitchFragment";
	
	int switchSize = 140;

	private boolean isVisible;

    private RelaysManager mRelaysManager;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_switch, container, false);
        
        LinearLayout linear = (LinearLayout) rootView.findViewById(R.id.linear);

        mRelaysManager = new RelaysManager();
		
		final CircolareConIndicatore lamp;		
		lamp = new CircolareConIndicatore(getActivity().getBaseContext(), CircolareConIndicatore.LAMP);			
		lamp.setLayoutParams(new LinearLayout.LayoutParams(switchSize, switchSize));
		lamp.setSwitchEventListener(new OnSwitchEventListener(){
		    public void onEvent(){
					if (isVisible) {

                        mRelaysManager.switchRelay(RelaysManager.RELAY1, lamp.isInterruttoreAcceso() ? RelaysManager.ON
                                                                                                        : RelaysManager.OFF);// 1 = ON; 0 = OFF);
                        //TODO: controllare relays con gpio
                        //ADKinvia
						/*SwitchMainActivity.mAdkManager.writeByteArray(GionjiUtils.createPackage(
								CircolareConIndicatore.LAMP, // 1 = LAMP; 2 = ABAJOUR; 3 = BULB
								lamp.isInterruttoreAcceso() ? CircolareConIndicatore.STATE_ON
										: CircolareConIndicatore.STATE_OFF, // 1 = ON; 0 = OFF
								255, // red
								255, // green
								255) // blue
								);*/
					}
		        }
		});
		linear.addView(lamp);
		
		final CircolareConIndicatore abajure;
		abajure = new CircolareConIndicatore(getActivity().getBaseContext(), CircolareConIndicatore.ABAJURE);		
		abajure.setLayoutParams(new LinearLayout.LayoutParams(switchSize, switchSize));
		abajure.setSwitchEventListener(new OnSwitchEventListener(){
		    public void onEvent(){
	        	Log.i(TAG, "Event fine circolo");
				if (isVisible) {

                    mRelaysManager.switchRelay(RelaysManager.RELAY2, lamp.isInterruttoreAcceso() ? RelaysManager.ON
                                                                                                    : RelaysManager.OFF);

					//				//ADKinvia
					/*SwitchMainActivity.mAdkManager.writeByteArray(GionjiUtils.createPackage(
							CircolareConIndicatore.ABAJURE, // 1 = LAMP; 2 = ABAJOUR; 3 = BULB
							abajure.isInterruttoreAcceso() ? CircolareConIndicatore.STATE_ON
									: CircolareConIndicatore.STATE_OFF, // 1 = ON; 0 = OFF
							255, // red
							255, // green
							255) // blue
							);*/
				}
	        }
		});
		linear.addView(abajure);
		
		CircolareConIndicatoreLed bulb;
		bulb = new CircolareConIndicatoreLed(getActivity().getBaseContext(), CircolareConIndicatoreLed.BULB);			
		bulb.setLayoutParams(new LinearLayout.LayoutParams(switchSize, switchSize));
		linear.addView(bulb);

        return rootView;
    }
    
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if (isVisibleToUser) {
	    	isVisible = true;
	    } else {
	    	isVisible = false;
	    }
	}  
}
