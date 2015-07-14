package com.gionji.switchuno.views;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.gionji.switchuno.R;
import com.gionji.switchuno.SwitchMainActivity;
import com.gionji.switchuno.utils.GionjiUtils;

public class CircolareConIndicatoreLed extends View{
	
	public static final int LAMP = 1;
	public static final int ABAJURE = 2;
	public static final int BULB = 3;
	
	public static final int STATE_ON = 1;
	public static final int STATE_OFF = 0;
	
	private int iconId = 0;
	
	private int MARGINE_ALTO = 10;
	private int SPESSORE_CERCHIO_ESTERNO = 30;
	private int MARGINE_CERCHI = 10;
	private int SPESSORE_CERCHIO_INTERNO = 7;
	private float MARGINE_IMMAGINE = 1.15f;
		
	private Bitmap iconaAccesa = BitmapFactory.decodeResource(getResources(), R.drawable.lamp_on);
	private Bitmap iconaSpenta = BitmapFactory.decodeResource(getResources(), R.drawable.lamp_off);
	
	private Bitmap lamp_off    = BitmapFactory.decodeResource(getResources(), R.drawable.lamp_off);
	private Bitmap lamp_on     = BitmapFactory.decodeResource(getResources(), R.drawable.lamp_on);
	private Bitmap abajure_off = BitmapFactory.decodeResource(getResources(), R.drawable.abajure_off);
	private Bitmap abajure_on  = BitmapFactory.decodeResource(getResources(), R.drawable.abajure_on);
	private Bitmap bulb_off    = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_off);
	private Bitmap bulb_on     = BitmapFactory.decodeResource(getResources(), R.drawable.bulb_on);
	
//	private int TEMPO_ROTAZIONE = 500; // millis
			
	private Paint trattoCerchioEsterno     = new Paint();

	private Paint tratto8     = new Paint();
	private Paint trattoCerchioInterno = new Paint();
	private Paint trattoImmagine       = new Paint();
	private RectF areaIndicatore       = new RectF();	
	private RectF areaCerchioInterno   = new RectF();
	private RectF areaImmagine         = new RectF();

	private int alt, larg;
	
	private int angle = 0;
	boolean interruttoreAcceso = false;
	
	private int coloreCerchioInterno = Color.BLACK;
	
	private boolean transitionLock = false;
	
	OnSwitchEventListener mListener;
	
	private int selectedLedColor = Color.WHITE;
	
	
	private final static int ANIMATION_PRESSED    = 0x01;
	private final static int ANIMATION_LONG_BEGIN = 0x02;
	private final static int ANIMATION_LONG_END   = 0x03;
	private final static int ANIMATION_MOVING     = 0x04;
	private int animationIndex   = 0;
	private int spessoreCerchioEsternoOffset = 0;
	
	
	public CircolareConIndicatoreLed(Context context) {
		super(context);		
	}
	
	public CircolareConIndicatoreLed(Context context, int iconId) {
		super(context);	
		
		this.iconId = iconId;
		
		if(this.iconId == CircolareConIndicatoreLed.LAMP){
			iconaAccesa = lamp_on;
			iconaSpenta = lamp_off;
		}
		else if (this.iconId == CircolareConIndicatoreLed.ABAJURE){
			iconaAccesa = abajure_on;
			iconaSpenta = abajure_off;
		} 
		else if (this.iconId == CircolareConIndicatoreLed.BULB){
			iconaAccesa = bulb_on;
			iconaSpenta = bulb_off;
		}
		
		Log.i("",GionjiUtils.map(360, 0, 360, 0, 255) + "   xxxxxx");
		
		
	}
	
	public CircolareConIndicatoreLed(Context context, AttributeSet attrs) {
		super(context, attrs);		
	}

	
	Paint textpaint = new Paint(); 
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		textpaint.setColor(Color.WHITE); 
		textpaint.setTextSize(20);

		larg = getWidth();
		alt = larg;
				
		MARGINE_ALTO             = ((alt * 10) / 600) + 1;
		SPESSORE_CERCHIO_ESTERNO = ((alt * 30) / 600) + 1;
		MARGINE_CERCHI           = ((alt * 10) / 600) + 1;
		SPESSORE_CERCHIO_INTERNO = ((alt * 7) / 600) + 1;
		MARGINE_IMMAGINE         = ((alt * 1.15f) / 600) + 1;
		
		// imposto le dimensioni delle aree
		areaIndicatore.set(MARGINE_ALTO + SPESSORE_CERCHIO_ESTERNO,
				MARGINE_ALTO + SPESSORE_CERCHIO_ESTERNO,
				larg - (MARGINE_ALTO + SPESSORE_CERCHIO_ESTERNO),
				alt - (MARGINE_ALTO + SPESSORE_CERCHIO_ESTERNO));
		
		areaCerchioInterno.set(MARGINE_ALTO + SPESSORE_CERCHIO_ESTERNO  + SPESSORE_CERCHIO_ESTERNO + MARGINE_CERCHI, 
				MARGINE_ALTO + SPESSORE_CERCHIO_ESTERNO  + SPESSORE_CERCHIO_ESTERNO + MARGINE_CERCHI,
				larg - (MARGINE_ALTO + SPESSORE_CERCHIO_ESTERNO + SPESSORE_CERCHIO_ESTERNO + MARGINE_CERCHI),
				alt - (MARGINE_ALTO + SPESSORE_CERCHIO_ESTERNO + SPESSORE_CERCHIO_ESTERNO + MARGINE_CERCHI));
		
		float latoImmagine = areaCerchioInterno.height() / MARGINE_IMMAGINE;
		float centro = larg / 2;
		
		areaImmagine.set(centro - latoImmagine / 2,
				centro - latoImmagine / 2,
				centro + latoImmagine / 2,
				centro + latoImmagine / 2);

		
		// impostazione tratti		
		trattoCerchioEsterno.setAntiAlias(true);
		trattoCerchioEsterno.setStrokeWidth(SPESSORE_CERCHIO_ESTERNO);
		trattoCerchioEsterno.setStyle(Paint.Style.STROKE);	
		
		trattoCerchioInterno.setAntiAlias(true);
		trattoCerchioInterno.setStrokeWidth(SPESSORE_CERCHIO_INTERNO);
		trattoCerchioInterno.setStyle(Paint.Style.STROKE);
		
		//		
		if(interruttoreAcceso)
			canvas.drawBitmap(iconaAccesa, null, areaImmagine, trattoImmagine);	
		else
			canvas.drawBitmap(iconaSpenta, null, areaImmagine, trattoImmagine);	
		
		if(animationIndex == CircolareConIndicatoreLed.ANIMATION_PRESSED){
			canvas.drawArc(areaIndicatore,   -90, angle, false, trattoCerchioEsterno);
			canvas.drawArc(areaCerchioInterno, 0, 	360, false, trattoCerchioInterno);
		}	
		else if (animationIndex == CircolareConIndicatoreLed.ANIMATION_LONG_BEGIN){
			for(int i=0; i<angle; i++){
				float hue = GionjiUtils.map(i, 0, 360, 0, 255);
				
				float[] color = new float[]{hue, 255f, 255f};
				
				trattoCerchioEsterno.setColor(Color.HSVToColor(color));
				canvas.drawArc(areaIndicatore,   i - 90, 1, false, trattoCerchioEsterno);
			}

		}
		else if(animationIndex == ANIMATION_MOVING){
			for(int i=0; i<angle; i++){
				float hue = GionjiUtils.map(i, 0, 360, 0, 255);
				
				float[] color = new float[]{hue, 255f, 255f};
				
				trattoCerchioEsterno.setColor(Color.HSVToColor(color));
				canvas.drawArc(areaIndicatore,   i - 90, 3, false, trattoCerchioEsterno);
			}
			coloreCerchioInterno = selectedLedColor;
			trattoCerchioInterno.setColor(coloreCerchioInterno);
    		/*SwitchMainActivity.mAdkManager.writeByteArray(
    				GionjiUtils.createPackage(
    					CircolareConIndicatoreLed.BULB,											// 1 = LAMP; 2 = ABAJOUR; 3 = BULB
    					interruttoreAcceso ? 
    							CircolareConIndicatoreLed.STATE_ON : CircolareConIndicatoreLed.STATE_OFF, 		// 1 = ON; 0 = OFF
    					Color.red(selectedLedColor), 											// red
    					Color.green(selectedLedColor), 											// green
    					Color.blue(selectedLedColor))											// blue
    		);*/
			canvas.drawArc(areaCerchioInterno, 0, 	360, false, trattoCerchioInterno);
		}
		else if(animationIndex == ANIMATION_LONG_END){

			Log.i("","....." );
			
			canvas.drawArc(areaIndicatore,   -90, angle, false, trattoCerchioEsterno);
			canvas.drawArc(areaCerchioInterno, 0, 	360, false, trattoCerchioInterno);
		}

	}
	
	
	float x , y;
    float downX, downY;
    boolean isOnClick;
    boolean isOnLongClick;
    float SCROLL_THRESHOLD = 20;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {	
	    int eventaction = event.getAction();
	    
	    x = (int) event.getX();
	    y = (int) event.getY();
	    
	    switch (eventaction) {
	        case MotionEvent.ACTION_DOWN:
	        	downX = x;
	        	downY = y;
	        	isOnClick = true;
	            break;

	        case MotionEvent.ACTION_MOVE:
	        	if(isOnClick && ( Math.abs(downY - y) > SCROLL_THRESHOLD )){
	        		isOnClick     = false;
	        		isOnLongClick = true;
		        	new LongPressAnimation().start();
	        	}
	        	
	        	if(isOnLongClick ){
	        		selectedLedColor = getColorSlice(x, y);
	        		invalidate();
	        	}
	            break;

	        case MotionEvent.ACTION_UP:   
				if(isOnClick)
					new RotationAnimation().start();
				else {
					isOnLongClick = false;
					selectedLedColor = getColorSlice(x, y);
					new ReleaseLongPressAnimation().start();					
				}		
				break;
	    }

	    // tell the system that we handled the event and no further processing is required
	    return true; 
		
	}
	
	private int getColorSlice(float x, float y){
		double center = larg / 2;
		
		double angle = Math.toDegrees(Math.atan2((x - center) , (y - center)));

		float[] colorHsv = {GionjiUtils.map((float)angle - 180, 0f, 360f, 0f, 255f), 255, 255};
		
		//Log.i("Hue", "hue " + colorHsv[0]);
		
		colorHsv[0] = Math.abs(colorHsv[0]);
		
		return Color.HSVToColor(colorHsv);
	}
	
	private class RotationAnimation extends Thread{
		
		@Override
		public void run() {			
			super.run();
			
			if(transitionLock)
				return;
			else
				transitionLock = true;
			
			animationIndex = CircolareConIndicatoreLed.ANIMATION_PRESSED;
			
			coloreCerchioInterno = selectedLedColor;
			
			for (int j = 0; j < 361; j++) {
				angle = j;
				trattoCerchioEsterno.setColor(selectedLedColor);
				postInvalidate();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
					
			//SPENGI ACCENDI
			interruttoreAcceso = !interruttoreAcceso;
			
			/*SwitchMainActivity.mAdkManager.writeByteArray(
    				GionjiUtils.createPackage(
    					CircolareConIndicatoreLed.BULB,											// 1 = LAMP; 2 = ABAJOUR; 3 = BULB
    					interruttoreAcceso ? 
    							CircolareConIndicatoreLed.STATE_ON : CircolareConIndicatoreLed.STATE_OFF, 		// 1 = ON; 0 = OFF
    					Color.red(selectedLedColor), 											// red
    					Color.green(selectedLedColor), 											// green
    					Color.blue(selectedLedColor))											// blue
    		);*/
			
			for (int j = 0; j < 255; j+=4) {
				coloreCerchioInterno = Color.rgb(j, 255, 255);
				trattoCerchioInterno.setColor(coloreCerchioInterno);
				trattoCerchioEsterno.setColor(Color.rgb(j, 255, 255));
				
				postInvalidate();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
			
			for (int j = 255; j >= 0; j-=1) {
				coloreCerchioInterno = Color.rgb(j, j, j);
				trattoCerchioInterno.setColor(coloreCerchioInterno);
				trattoCerchioEsterno.setColor(Color.rgb(j, j, j));
				postInvalidate();
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			transitionLock = false;
			
		}		
	}
	
	private class LongPressAnimation extends Thread{
		
		@Override
		public void run() {			
			super.run();
			
			if(transitionLock)
				return;
			else
				transitionLock = true;
			
			animationIndex = CircolareConIndicatoreLed.ANIMATION_LONG_BEGIN;			
			coloreCerchioInterno = selectedLedColor;
			
			// Disegno le fette colorate
			for (int j = 0; j < 360; j+=2) {
				angle = j;
				postInvalidate();
				try {
					Thread.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
									
			//SPENGI ACCENDI
			if(!interruttoreAcceso){
				interruttoreAcceso = true;
			}
	
			transitionLock = false;
			animationIndex = CircolareConIndicatoreLed.ANIMATION_MOVING;
		}		
	}
	
	float saturation = 255f;
	
	
	private class ReleaseLongPressAnimation extends Thread{
		
		@Override
		public void run() {			
			super.run();

			
			if(transitionLock)
				return;
			else
				transitionLock = true;
			
			animationIndex = CircolareConIndicatoreLed.ANIMATION_LONG_END;
				
			Log.i("","animation index" + animationIndex);
			
			coloreCerchioInterno = selectedLedColor;
			
			for (int j = 255; j > 0; j--) {
				trattoCerchioEsterno.setColor(Color.rgb(j, j, j));
				postInvalidate();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			/*SwitchMainActivity.mAdkManager.writeByteArray(
    				GionjiUtils.createPackage(
    					CircolareConIndicatoreLed.BULB,											// 1 = LAMP; 2 = ABAJOUR; 3 = BULB
    					interruttoreAcceso ? 
    							CircolareConIndicatoreLed.STATE_ON : CircolareConIndicatoreLed.STATE_OFF, 		// 1 = ON; 0 = OFF
    					Color.red(selectedLedColor), 											// red
    					Color.green(selectedLedColor), 											// green
    					Color.blue(selectedLedColor))											// blue
    		);*/
			
			for (int j = 255; j >= 0; j-=1) {
				coloreCerchioInterno = Color.rgb(j, j, j);
				trattoCerchioInterno.setColor(coloreCerchioInterno);
				postInvalidate();
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			transitionLock = false;
			
		}		
	}
	
	public interface OnSwitchEventListener{
		public void onEvent();
	}

	public void setSwitchEventListener(OnSwitchEventListener eventListener) {
		mListener=eventListener;
	}
}
