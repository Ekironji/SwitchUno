package com.gionji.switchuno.views;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gionji.switchuno.R;


public class CircolareConIndicatore extends View{
	
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
			
	private Paint trattoIndicatore     = new Paint();
	private Paint trattoCerchioInterno = new Paint();
	private Paint trattoImmagine       = new Paint();
	private RectF areaIndicatore       = new RectF();	
	private RectF areaCerchioInterno   = new RectF();
	private RectF areaImmagine         = new RectF();

	private int alt, larg;
	
	private int angle = 0;
	boolean interruttoreAcceso = false;
	
	private int coloreCerchio = Color.BLACK;
	
	private boolean transitionLock = false;
	
	OnSwitchEventListener mListener;	
	
	public CircolareConIndicatore(Context context) {
		super(context);		
	}
	
	public CircolareConIndicatore(Context context, int iconId) {
		super(context);	
		
		this.iconId = iconId;
		
		if(this.iconId == CircolareConIndicatore.LAMP){
			iconaAccesa = lamp_on;
			iconaSpenta = lamp_off;
		}
		else if (this.iconId == CircolareConIndicatore.ABAJURE){
			iconaAccesa = abajure_on;
			iconaSpenta = abajure_off;
		} 
		else if (this.iconId == CircolareConIndicatore.BULB){
			iconaAccesa = bulb_on;
			iconaSpenta = bulb_off;
		}
	}
	
	public CircolareConIndicatore(Context context, AttributeSet attrs) {
		super(context, attrs);		
	}

	/**
	 * @param iconaAccesa the iconaAccesa to set
	 */
	public void setIconaAccesa(Bitmap iconaAccesa) {
		this.iconaAccesa = iconaAccesa;
	}

	/**
	 * @param iconaSpenta the iconaSpenta to set
	 */
	public void setIconaSpenta(Bitmap iconaSpenta) {
		this.iconaSpenta = iconaSpenta;
	}
	
	/**
	 * @return the interruttoreAcceso
	 */
	public boolean isInterruttoreAcceso() {
		return interruttoreAcceso;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
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
		
//		Log.i("dfgfgdfgd", "Area cerchio interno" + areaCerchioInterno.height() + "  " + areaCerchioInterno.width() );
//		Log.i("TAGfgddfgdfgGHE", "Area cerchio estero" + areaIndicatore.height() + "  " +  areaIndicatore.width() );		
		
		// impostazione tratti		
		trattoIndicatore.setAntiAlias(true);
		trattoIndicatore.setColor(coloreCerchio);
		trattoIndicatore.setStrokeWidth(SPESSORE_CERCHIO_ESTERNO);
		trattoIndicatore.setStyle(Paint.Style.STROKE);		
		
		trattoCerchioInterno.setAntiAlias(true);
		trattoCerchioInterno.setColor(coloreCerchio);
		trattoCerchioInterno.setStrokeWidth(SPESSORE_CERCHIO_INTERNO);
		trattoCerchioInterno.setStyle(Paint.Style.STROKE);
		
		//		
		if(interruttoreAcceso)
			canvas.drawBitmap(iconaAccesa, null, areaImmagine, trattoImmagine);	
		else
			canvas.drawBitmap(iconaSpenta, null, areaImmagine, trattoImmagine);	
		
		canvas.drawArc(areaIndicatore,   -90, angle, false, trattoIndicatore);
		canvas.drawArc(areaCerchioInterno, 0, 	360, false, trattoCerchioInterno);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {	
		super.onTouchEvent(event);
	    int eventaction = event.getAction();
	    
	    float x = (int) event.getX();
	    float y = (int) event.getY();

	    switch (eventaction) {
	        case MotionEvent.ACTION_DOWN:
	        	if(areaImmagine.contains(x, y));
	            	new RotationAnimation().start();
	            break;

	        case MotionEvent.ACTION_MOVE:
	            break;

	        case MotionEvent.ACTION_UP:   
	            break;
	    }

	    // tell the system that we handled the event and no further processing is required
	    return true; 
		
	}
	
	
	private class RotationAnimation extends Thread{
		
		@Override
		public void run() {			
			super.run();
			
			if(transitionLock)
				return;
			else
				transitionLock = true;
			
			coloreCerchio = Color.CYAN;
			
			for (int j = 0; j < 361; j+=2) {
				angle = j;
				postInvalidate();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//SPENGI ACCENDI
			interruttoreAcceso = !interruttoreAcceso;
			
			for (int j = 0; j < 255; j+=4) {
				coloreCerchio = Color.rgb(j, 255, 255);
				postInvalidate();
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
			
			for (int j = 255; j >= 0; j-=1) {
				coloreCerchio = Color.rgb(j, j, j);
				postInvalidate();
				try {
					Thread.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			transitionLock = false;
			
			if(mListener!=null) mListener.onEvent();
			
		}
		
	}
	
	public interface OnSwitchEventListener{
		public void onEvent();
	}

	public void setSwitchEventListener(OnSwitchEventListener eventListener) {
		mListener=eventListener;
	}

}
