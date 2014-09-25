package com.gionji.switchuno.meteoparser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ClimaParser extends DefaultHandler{
		
	private String tempVal;	
	private DatiMeteo datiMeteo;	
	private Previsione tempPrevisione;	
	private Temp tempTemp;

	// Constructor
	public ClimaParser(){
		
	}
	
	// parso passando il file direttamente
	public DatiMeteo parseDocument(String str) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
			SAXParser sp = spf.newSAXParser();			
			
			byte[] bytes;			
			bytes = str.getBytes("UTF8");
			InputStream is = new ByteArrayInputStream(bytes);			
			sp.parse(is, this);			
		}
			catch(SAXException se) {
			se.printStackTrace();
		}
		catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return datiMeteo;
	}
	

	
	//Event Handlers
	public void startElement(String uri, String localName, String qName,
		Attributes attributes) throws SAXException {
		tempVal = "";
				
		if(qName.equalsIgnoreCase("dati")){
			datiMeteo = new DatiMeteo(null);
		}		
		else if(qName.equalsIgnoreCase("comune")) {
			
		}
		else if(qName.equalsIgnoreCase("almanacco")) {
			
		}	
		else if(qName.equalsIgnoreCase("previsione")) {
			tempPrevisione = new Previsione();
			tempPrevisione.setIdday(Integer.parseInt(attributes.getValue("idday")));
			tempPrevisione.setOra(attributes.getValue("ora"));
			tempPrevisione.setDatadescr(attributes.getValue("datadescr"));
		}		
		else if(qName.equalsIgnoreCase("simbolo")) {
			if(attributes.getValue("image_type").equals("C")){
				tempPrevisione.setClima(attributes.getValue("descr"),
						attributes.getValue("image_name"),
						attributes.getValue("image_type"));
			}
			else if(attributes.getValue("image_type").equals("W")){				
				tempPrevisione.setVento(attributes.getValue("descr"),
						attributes.getValue("image_name"),
						attributes.getValue("image_type"));
			}
		}		
		else if(qName.equalsIgnoreCase("uv")) {
			
		}		
		else if(qName.equalsIgnoreCase("temp")) {
			tempTemp = new Temp();
			tempTemp.temptype = attributes.getValue("temp_type");
		}		
		else if(qName.equalsIgnoreCase("um")) {
			
		}						
	}


	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}

	
	public void endElement(String uri, String localName,
		String qName) throws SAXException {

		if(qName.equalsIgnoreCase("comune")){
			datiMeteo.setCitta(tempVal);
		}		
		else if(qName.equalsIgnoreCase("uv")) {		
			try {
				tempPrevisione.setUv(Integer.parseInt(tempVal));
			} catch (Exception e) {
				tempPrevisione.setUv(-1);
			}
		}		
		else if(qName.equalsIgnoreCase("temp")) {
			tempTemp.value = Integer.parseInt(tempVal);
			tempPrevisione.addTemp(tempTemp);
		}		
		else if(qName.equalsIgnoreCase("um")) {
			try {
				tempPrevisione.setUm(Integer.parseInt(tempVal));
			} catch (Exception e) {
				tempPrevisione.setUm(-1);
			}
		}		
		else if(qName.equalsIgnoreCase("previsione")) {
			datiMeteo.addPrevisione(tempPrevisione);
		}
		else if(qName.equalsIgnoreCase("almanacco")) {
			
		}
	}

}