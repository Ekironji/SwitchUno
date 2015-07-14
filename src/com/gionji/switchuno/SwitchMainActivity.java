package com.gionji.switchuno;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.gionji.switchuno.fragment.CitofonoFragment;
import com.gionji.switchuno.fragment.MediaPlayerFragment;
import com.gionji.switchuno.fragment.MeteoFragment;
import com.gionji.switchuno.fragment.SwitchFragment;
import com.gionji.switchuno.serialport.IndoleSerialProtocol;
import com.gionji.switchuno.serialport.SerialPort;
import com.gionji.switchuno.utils.GionjiUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class SwitchMainActivity extends Activity {
	
	static ProgressDialog dialog;
	
	/**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 4;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter mPagerAdapter;

    SerialPort mSerialPort = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		dialog = new ProgressDialog(this);
		// Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(3);
        mPager.setCurrentItem(1);

        try {
            SerialPort mSerialPort = GionjiUtils.getSerialPort();
            IndoleSerialProtocol mIndoleSerialProtocol = new IndoleSerialProtocol(getApplicationContext(), mSerialPort);
        } catch (IOException e) {
            Log.e("Serial Exception", "open port error");
            e.printStackTrace();
        }
	}

	
	@Override
	public void onResume() {
		super.onResume(); 
	}
 
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
    protected void onDestroy() {
        GionjiUtils.closeSerialPort();
        mSerialPort = null;
        super.onDestroy();
    }
	
	public static void showProgressDialog(){
		dialog.setMessage("Attendere il caricamento dei dati...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();		
	}
	
	public static void hideProgressDialog(){
		dialog.dismiss();		
	}
	
	 /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	
        	Fragment returnFragment = null;
        
        	switch (position) {
        	case 0:
        		returnFragment = new MeteoFragment();
        		break;
        	case 1:
        		returnFragment = new SwitchFragment();
        		break;
        	case 2:
        		returnFragment = new MediaPlayerFragment();
        		break;
            case 3:
                returnFragment = new CitofonoFragment();
                break;
    		default:
    			break;
        	
        	}        	
            return returnFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
	
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    	
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                        (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }


}
