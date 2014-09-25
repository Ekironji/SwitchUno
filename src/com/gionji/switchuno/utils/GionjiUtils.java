package com.gionji.switchuno.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

public class GionjiUtils {

	public static Bitmap makeBitmap(Context ctx, int bitmapId) {
		return(BitmapFactory.decodeResource(ctx.getResources(), bitmapId));
	}
	
	public static byte[] createPackage(int id, int state, int r, int g, int b) {
		byte[] message = new byte[5];
		
		message[0] = (byte) id; 		// 1 = LAMP; 2 = ABAJOUR; 3 = BULB		
		message[1] = (byte) state;   	// 1 = ON; 0 = OFF
		message[2] = (byte) r;			// red
		message[3] = (byte) g;			// green
		message[4] = (byte) b;			// blue
		
		return message;		
	}
	
	/**
	 * Utils Methods for playlist
	 * */
	
	// ArrayList to store song
	private static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	
	/**
	 * Function to read all mp3 files from Music folder in SDcard
	 * and store the details in ArrayList
	 * */
	public static ArrayList<HashMap<String, String>> getPlayList(){
		
		File home = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC);
		
		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {
				HashMap<String, String> song = new HashMap<String, String>();
				song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
				song.put("songPath", file.getPath());
				
				Log.i("GionjiUtils", "songTitle: " + file.getName().substring(0, (file.getName().length() - 4)));
				Log.i("GionjiUtils", "songPath" + file.getPath());
				// Adding each song to SongList
				songsList.add(song);
			}
		}
		// return songs list array
		return songsList;
	}
	
	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	static class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}
	
	
	/**
	 * Utils Methods for the seek bar 
	 * */
	
	/**
	 * Function to convert milliseconds time to
	 * Timer Format
	 * Hours:Minutes:Seconds
	 * */
	public static String milliSecondsToTimer(long milliseconds){
		String finalTimerString = "";
		String secondsString = "";
		
		// Convert total duration into time
		   int hours = (int)( milliseconds / (1000*60*60));
		   int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
		   int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		   // Add hours if there
		   if(hours > 0){
			   finalTimerString = hours + ":";
		   }
		   
		   // Prepending 0 to seconds if it is one digit
		   if(seconds < 10){ 
			   secondsString = "0" + seconds;
		   }else{
			   secondsString = "" + seconds;}
		   
		   finalTimerString = finalTimerString + minutes + ":" + secondsString;
		
		// return timer string
		return finalTimerString;
	}
	
	/**
	 * Function to get Progress percentage
	 * @param currentDuration
	 * @param totalDuration
	 * */
	public static int getProgressPercentage(long currentDuration, long totalDuration){
		Double percentage = (double) 0;
		
		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);
		
		// calculating percentage
		percentage =(((double)currentSeconds)/totalSeconds)*100;
		
		// return percentage
		return percentage.intValue();
	}

	/**
	 * Function to change progress to timer
	 * @param progress - 
	 * @param totalDuration
	 * returns current duration in milliseconds
	 * */
	public static int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double)progress) / 100) * totalDuration);
		
		// return current duration in milliseconds
		return currentDuration * 1000;
	}
	
	
	/**
	 * Function to change progress to timer
	 * @param progress - 
	 * @param totalDuration
	 * returns current duration in milliseconds
	 * */
	public static float map(float x, float fromMin, float fromMax, float toMin, float toMax) {
		
		float res = (((toMax - toMin)*(x - fromMin)) / (fromMax - fromMin)) + toMin;
		
		return res;
	}
	
	/**
	 * Function to check if network is available
	 * */
	public static boolean isNetworkAvailable(Context context) {
	     ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	     if (connectivity != null) {
	        NetworkInfo[] info = connectivity.getAllNetworkInfo();
	        if (info != null) {
	           for (int i = 0; i < info.length; i++) {
	              if (info[i].getState() == NetworkInfo.State.CONNECTED) {
	                 return true;
	              }
	           }
	        }
	     }
	     return false;
	  }
	
}
