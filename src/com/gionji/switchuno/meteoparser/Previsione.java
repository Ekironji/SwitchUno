package com.gionji.switchuno.meteoparser;

import java.util.ArrayList;

public class Previsione {
	private int idday;
	private String ora;
	private String datadescr;
	private Simbolo clima;
	private Simbolo vento;
	private int uv;
	private ArrayList<Temp> temp;
	private int um;
	
		
	public Previsione() {
		idday = -1;
		ora = null;
		datadescr = null;
		clima = null;
		vento = null;
		uv = -1;
		um = -1;
		temp = new ArrayList<Temp>();
	}
	

	public int getIdday() {
		return idday;
	}

	public void setIdday(int idday) {
		this.idday = idday;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public String getDatadescr() {
		return datadescr;
	}

	public void setDatadescr(String datadescr) {
		this.datadescr = datadescr;
	}

	public Simbolo getClima() {
		return clima;
	}

	public void setClima(String descr, String image_name, String image_type) {
		this.clima = new Simbolo(descr, image_name, image_type);
	}

	public Simbolo getVento() {
		return vento;
	}

	public void setVento(String descr, String image_name, String image_type) {
		this.vento = new Simbolo(descr, image_name, image_type);
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public int getUm() {
		return um;
	}

	public void setUm(int um) {
		this.um = um;
	}

	public ArrayList<Temp> getTemp() {
		return temp;
	}
	
	public void addTemp(Temp t){
		temp.add(t);
	}
	

	@Override
	public String toString() {
		String s = "Previsione [idday=" + idday + ", ora=" + ora + ", datadescr="
				+ datadescr ;	
				if (clima != null) {
					s += ", clima=" + clima.toString();
				}
				if (vento != null) {
					s += ", vento=" + vento.toString();
				}
		if (temp!=null) {
			for (Temp t : temp) {
				s += "\ntemp=" + t.toString();
			}
		}
		return s;
	}



	public class Simbolo{
		public String descr;
		public String image_name;
		public String image_type;
		/**
		 * @param descr
		 * @param image_name
		 * @param image_type
		 */
		
		public Simbolo(String descr, String image_name, String image_type) {
			this.descr = descr;
			this.image_name = image_name;
			this.image_type = image_type;
		}
		
		@Override
		public String toString() {
			return "Simbolo [descr=" + descr + ", image_name=" + image_name
					+ ", image_type=" + image_type + "]";
		}		

	}
	
}
