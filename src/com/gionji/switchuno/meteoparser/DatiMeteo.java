package com.gionji.switchuno.meteoparser;

import java.util.ArrayList;
import java.util.Calendar;

public class DatiMeteo {
	
	public static final int OGGI = 1;
	public static final int DOMANI = 2;
	public static final int DOPODOMANI = 3;
	
	public static final String MATTINA = "mattina";
	public static final String POMERIGGIO = "pomeriggio";
	public static final String SERA = "sera";
		
	private String citta;
	private ArrayList<Previsione> previsioni;
	
	public DatiMeteo(String citta){
		this.citta = citta;
		previsioni = new ArrayList<Previsione>();
	}
	
	public void addPrevisione(Previsione p){
		previsioni.add(p);
	}
	
	
	public String getCitta() {
		return citta;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	@Override
	public String toString() {
		String s = "DatiMeteo [citta=" + citta + ",";
		
		for(Previsione p : previsioni ){
			s += "previsione= " + p.toString();
		}		
		s+= "]";		
		return s;
	}
	
	
	//////// GETTERS ////////
	
	public boolean isMattinoAvailable(int day){
		for(Previsione p : previsioni){
			if(p.getIdday() == day && p.getOra().equals("mattina"))
				return true;				
		}
		return false;
	}
	
	public boolean isPomeriggioAvailable(int day){
		for(Previsione p : previsioni){
			if(p.getIdday() == day && p.getOra().equals("pomeriggio"))
				return true;				
		}
		return false;
	}
	
	public boolean isSeraAvailable(int day){
		for(Previsione p : previsioni){
			if(p.getIdday() == day && p.getOra().equals("sera"))
				return true;				
		}
		return false;
	}
	
	
	
	public Calendar getData(){		
		Calendar c = Calendar.getInstance();

		return c;
	}
	
	public PrevPair getOggiTemp(){
		String temp;
		for(Previsione p : previsioni){
			if(p.getIdday() == OGGI){
				if(p.getOra().equals("giorno")){
					if(p.getTemp().size() == 2){
						temp = p.getTemp().get(0).value + "° - " + p.getTemp().get(1).value + "°";
					}
					else{
						temp =  p.getTemp().get(0).value + "°";
					}
					return new PrevPair(temp, p.getClima().descr);
				}
				else if (p.getOra().equals("notte")){
					temp =  p.getTemp().get(0).value + "°";
					return new PrevPair(temp, p.getClima().descr);
				}
			}								
		}
		return null;
	}
	
	public PrevPair getDomaniTemp(){
		String temp;
		for(Previsione p : previsioni){
			if(p.getIdday() == DOMANI){
				if(p.getOra().equals("giorno")){
					if(p.getTemp().size() == 2){
						temp = p.getTemp().get(0).value + "° - " + p.getTemp().get(1).value + "°";
					}
					else{
						temp =  p.getTemp().get(0).value + "°";
					}
					return new PrevPair(temp, p.getClima().descr);
				}
				else if (p.getOra().equals("notte")){
					temp =  p.getTemp().get(0).value + "°";
					return new PrevPair(temp, p.getClima().descr);
				}
			}								
		}
		return null;
	}
	
	public PrevPair getDopodomaniTemp(){
		String temp;
		for(Previsione p : previsioni){
			if(p.getIdday() == DOPODOMANI){
				if(p.getOra().equals("giorno")){
					if(p.getTemp().size() == 2){
						temp = p.getTemp().get(0).value + "° - " + p.getTemp().get(1).value + "°";
					}
					else{
						temp =  p.getTemp().get(0).value + "°";
					}
					return new PrevPair(temp, p.getClima().descr);
				}
				else if (p.getOra().equals("notte")){
					temp =  p.getTemp().get(0).value + "°";
					return new PrevPair(temp, p.getClima().descr);
				}
			}								
		}
		return null;
	}
	
	public Previsione getPrevisione(int day, String dayTime){
		for(Previsione p : previsioni){
			if(p.getIdday() == day){
				if(p.getOra().equals(dayTime)){
					return p;
				}				
			}			
		}
		return null;		
	}
	

	
	
	public class PrevPair{
		private String temp;
		private String icon;
		
		public PrevPair(String temp, String icon) {
			this.temp = temp;
			this.icon = icon;
		}

		public String getTemp() {
			return temp;
		}

		public String getIcon() {
			return icon;
		}
		
		
	}
}
