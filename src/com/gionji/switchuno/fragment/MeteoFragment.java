package com.gionji.switchuno.fragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gionji.switchuno.R;
import com.gionji.switchuno.SwitchMainActivity;
import com.gionji.switchuno.meteoparser.ClimaParser;
import com.gionji.switchuno.meteoparser.DatiMeteo;
import com.gionji.switchuno.meteoparser.Previsione;
import com.gionji.switchuno.utils.GionjiUtils;

public class MeteoFragment extends Fragment {
	
	private final String TAG = "MeteoFragment";
	
	public final static String BASE_ADDRESS = "http://www.lamma.rete.toscana.it/previ/ita/xml/comuni_web/dati/";
	
	private ImageView meteoImg;
	private ImageView weatherStateImg;
	private TextView weatherStateTxt;
	private TextView gradesTxt;
	
	private DatiMeteo datiMeteo;
	
	private boolean isVisible = false;
	private boolean progressVisible = false;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_meteo, container, false);
        
        meteoImg = (ImageView) rootView.findViewById(R.id.imageMeteo);
        TextView cityTxt = (TextView) rootView.findViewById(R.id.textCity);
        TextView timeTxt = (TextView) rootView.findViewById(R.id.textTime);
        TextView dateTxt = (TextView) rootView.findViewById(R.id.textDate);
        weatherStateImg = (ImageView) rootView.findViewById(R.id.imageWeatherState);
        weatherStateTxt = (TextView) rootView.findViewById(R.id.textWeatherState);
        gradesTxt = (TextView) rootView.findViewById(R.id.textGrades);
        
        cityTxt.setText(R.string.dummy_city);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ITALY);
        String time = timeFormat.format(new Date());
        timeTxt.setText(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        String date = dateFormat.format(new Date());
        dateTxt.setText(date);
        
        if (GionjiUtils.isNetworkAvailable(getActivity())) {
			new DownloadXmlTask().execute("arezzo");
		} else {
			Toast.makeText(getActivity().getApplicationContext(), "No internet connection ", Toast.LENGTH_SHORT).show();
		}
		return rootView;
	}
	
	private void setDatiMateoLayout() {
		
		if (datiMeteo != null) {
			Previsione p = datiMeteo.getPrevisione(DatiMeteo.OGGI, DatiMeteo.SERA);
			Log.i(TAG, "desc: " + p.getClima().descr + "gradi: "
					+ datiMeteo.getOggiTemp().getTemp());
			meteoImg.setImageResource(getWeatherImage(p.getClima().descr));
			weatherStateTxt.setText(p.getClima().descr);
			weatherStateImg
					.setImageResource(getWeatherIcon(p.getClima().descr));
			gradesTxt.setText(datiMeteo.getOggiTemp().getTemp());
		}
	}
	
	private int getWeatherImage(String n) {
		if(n.equals("sereno")) return R.drawable.def_sole ;
    	else if(n.equals("quasi sereno")) return R.drawable.def_sole ;
    	else if(n.equals("poco nuvoloso")) return R.drawable.def_parzialmente_nuvoloso ;
    	else if(n.equals("nuvoloso")) return R.drawable.def_nuovoloso ;
    	else if(n.equals("molto nuvoloso")) return R.drawable.def_nuovoloso ;
    	else if(n.equals("coperto")) return R.drawable.def_nuovoloso ;
    	else if(n.equals("velato")) return R.drawable.def_parzialmente_nuvoloso ;
    	else if(n.equals("variabile pioggia debole")) return R.drawable.def_pioggia ;
    	else if(n.equals("variabile pioggia")) return R.drawable.def_pioggia ;
    	else if(n.equals("nuvoloso pioggia debole")) return R.drawable.def_nuovoloso ;
    	else if(n.equals("nuvoloso pioggia")) return R.drawable.def_pioggia ;
    	else if(n.equals("variabile temporale")) return R.drawable.def_temporale ;
    	else if(n.equals("pioggia debole")) return R.drawable.def_pioggia ;
    	else if(n.equals("pioggia")) return R.drawable.def_pioggia ;
    	else if(n.equals("pioggia forte")) return R.drawable.def_temporale ;
    	else if(n.equals("temporale")) return R.drawable.def_temporale ;
    	else if(n.equals("variabile neve")) return R.drawable.def_nuovoloso ;
    	else if(n.equals("nuvoloso neve")) return R.drawable.def_nuovoloso ;
    	else if(n.equals("pioggia neve")) return R.drawable.def_pioggia ;
    	else if(n.equals("neve debole")) return R.drawable.def_pioggia ;
    	else if(n.equals("neve")) return R.drawable.def_pioggia ;
    	else if(n.equals("nebbia")) return R.drawable.def_nuovoloso ;
    	else if(n.equals("foschia")) return R.drawable.def_parzialmente_nuvoloso ;
    	else return 0;
	}
	
	private int getWeatherIcon(String n) {		
		if(n.equals("sereno")) return R.drawable.sereno ;
    	else if(n.equals("quasi sereno")) return R.drawable.quasi_sereno_poco_nuvoloso ;
    	else if(n.equals("poco nuvoloso")) return R.drawable.quasi_sereno_poco_nuvoloso ;
    	else if(n.equals("nuvoloso")) return R.drawable.nuvoloso_molto_nuvoloso ;
    	else if(n.equals("molto nuvoloso")) return R.drawable.nuvoloso_molto_nuvoloso ;
    	else if(n.equals("coperto")) return R.drawable.coperto ;
    	else if(n.equals("velato")) return R.drawable.velato ;
    	else if(n.equals("variabile pioggia debole")) return R.drawable.variabile_pioggia_nuvoloso_pioggia_debole_nuvoloso_pioggia ;
    	else if(n.equals("variabile pioggia")) return R.drawable.variabile_pioggia_nuvoloso_pioggia_debole_nuvoloso_pioggia ;
    	else if(n.equals("nuvoloso pioggia debole")) return R.drawable.variabile_pioggia_nuvoloso_pioggia_debole_nuvoloso_pioggia ;
    	else if(n.equals("nuvoloso pioggia")) return R.drawable.variabile_pioggia_nuvoloso_pioggia_debole_nuvoloso_pioggia ;
    	else if(n.equals("variabile temporale")) return R.drawable.variabile_temporale ;
    	else if(n.equals("pioggia debole")) return R.drawable.pioggia_pioggia_debole ;
    	else if(n.equals("pioggia")) return R.drawable.pioggia_pioggia_debole ;
    	else if(n.equals("pioggia forte")) return R.drawable.temporale_pioggia_forte ;
    	else if(n.equals("temporale")) return R.drawable.temporale_pioggia_forte ;
    	else if(n.equals("variabile neve")) return R.drawable.nuvoloso_neve_variabile_neve ;
    	else if(n.equals("nuvoloso neve")) return R.drawable.nuvoloso_neve_variabile_neve ;
    	else if(n.equals("pioggia neve")) return R.drawable.neve_neve_debole_pioggia_neve ;
    	else if(n.equals("neve debole")) return R.drawable.neve_neve_debole_pioggia_neve ;
    	else if(n.equals("neve")) return R.drawable.neve_neve_debole_pioggia_neve ;
    	else if(n.equals("nebbia")) return R.drawable.nebbia ;
    	else if(n.equals("foschia")) return R.drawable.foschia ;
    	else return 0;
	}
	
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	    super.setUserVisibleHint(isVisibleToUser);
	    if (isVisibleToUser) {
	    	isVisible = true;	
	    	
	    	if (progressVisible) {
	    		SwitchMainActivity.showProgressDialog();
	    	}
	    	
//	    	TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 
//	    															-100f, 
//	    															TranslateAnimation.ABSOLUTE, 
//	    															100f, 
//	    															TranslateAnimation.ABSOLUTE, 
//	    															0f, 
//	    															TranslateAnimation.ABSOLUTE, 
//	    															0f);
//	    	animation.setDuration(3000);
//	    	animation.setRepeatCount(-1);
//	    	animation.setRepeatMode(Animation.REVERSE);
//	    	animation.setInterpolator(new LinearInterpolator());	    	
//	        meteoImg.startAnimation(animation);
//
	    } else { 
	    	isVisible = false;
	    	if (!progressVisible) {
	    		SwitchMainActivity.hideProgressDialog();
	    	}
	    }
	}
	
	
	private class DownloadXmlTask extends AsyncTask<String, Integer, String> {
	 	@Override
	 	protected void onPreExecute() {
		 	//super.onPreExecute();
		 	showProgressDialog();
		 	Log.i(TAG, "onPreExecute");
	 	}
	 
        protected String doInBackground(String... urls) {
        	Log.i(TAG, "doInBackground");
        	String res = null;
        	res = getWeatherXmlFile(urls[0]);
        	return res;          
        }

        protected void onPostExecute(String result) {
        	Log.i(TAG,"onPostExecute");
        	ClimaParser parser = new ClimaParser();

        	try {        	
        		datiMeteo = parser.parseDocument(result);  
        		
			} catch (Exception e) {
				e.printStackTrace();
			}
        	       	
        	setDatiMateoLayout();
        	hideProgressDialog();
        }
    }
    
    // Faccio la query a lamma e salvo l'xml in un stringa
    public String getWeatherXmlFile(String city){
    	
    	String citta = city.toLowerCase().replace(" ", "").replace("\'", "");
    	
		String url = BASE_ADDRESS + citta;
		Log.v("Indirizzo xml usato " , url);
		
		HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        String xmltemp = null;
        
        HttpResponse rsp;
		
		try {
			rsp = client.execute(get);			
			xmltemp = EntityUtils.toString(rsp.getEntity());
		} catch (ClientProtocolException e) {
			Log.e(TAG , e.toString());
		} catch (ParseException e) {
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
		
	    return  xmltemp;
    }
	
	private void showProgressDialog(){
		progressVisible = true;
		if (isVisible) {
			SwitchMainActivity.showProgressDialog();
		}
	}
	
	private void hideProgressDialog(){
		progressVisible = false;
		SwitchMainActivity.hideProgressDialog();
	}
	
}
