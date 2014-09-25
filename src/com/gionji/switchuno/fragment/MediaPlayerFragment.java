package com.gionji.switchuno.fragment;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gionji.switchuno.R;
import com.gionji.switchuno.utils.GionjiUtils;
import com.gionji.switchuno.views.CircolareConIndicatore;

public class MediaPlayerFragment extends Fragment implements OnCompletionListener, SeekBar.OnSeekBarChangeListener {
	
	private final String TAG = "MediaPayerFragment";
	
	private CircolareConIndicatore btnPrevious;
	private CircolareConIndicatore btnPlay;	
	private CircolareConIndicatore btnNext;	
	private SeekBar songProgressBar;
	private TextView songTitleLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	private TextView songAlbumLabel;
	private ImageView songAlbumArtImage;
	// Media Player
	private  MediaPlayer mp;
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();
	private int currentSongIndex = 0; 
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	
	private boolean isVisible;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_mediaplayer, container, false);
        
        Log.i(TAG, "onCreateView()");
        
    	// MediaPlayer
 		mp = new MediaPlayer();
        
		songProgressBar = (SeekBar) rootView.findViewById(R.id.songProgressBar);
		songTitleLabel = (TextView) rootView.findViewById(R.id.songTitle);
		songCurrentDurationLabel = (TextView) rootView.findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) rootView.findViewById(R.id.songTotalDurationLabel);
		songAlbumLabel = (TextView) rootView.findViewById(R.id.AlbumTitle); 
		songAlbumArtImage = (ImageView) rootView.findViewById(R.id.songThumbnail);
		
        btnPrevious = (CircolareConIndicatore) rootView.findViewById(R.id.btnPrevious);
        btnPrevious.setIconaAccesa(BitmapFactory.decodeResource(getActivity().getBaseContext().getResources(),
                R.drawable.ic_prev));
        btnPrevious.setIconaSpenta(BitmapFactory.decodeResource(getActivity().getBaseContext().getResources(),
                R.drawable.ic_prev));
        btnPrevious.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(currentSongIndex > 0){
					currentSongIndex = currentSongIndex - 1;
					if(mp.isPlaying()){
						if(mp!=null){
							playSong(currentSongIndex);
						}
					} else { 
						playSong(currentSongIndex);
				 		mp.pause();
					}
				} else {
					// play last song
					currentSongIndex = songsList.size() - 1;
					if(mp.isPlaying()){
						if(mp!=null){
							playSong(currentSongIndex);
						}
					} else { 
						playSong(currentSongIndex);
				 		mp.pause();
					}
				}				
			}
		});
		
		btnPlay = (CircolareConIndicatore) rootView.findViewById(R.id.btnPlay);
		btnPlay.setIconaAccesa(BitmapFactory.decodeResource(getActivity().getBaseContext().getResources(),
                R.drawable.ic_pause));
		btnPlay.setIconaSpenta(BitmapFactory.decodeResource(getActivity().getBaseContext().getResources(),
                R.drawable.ic_play));
		btnPlay.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// check for already playing
				Log.i(TAG, "play button click");
				if(mp.isPlaying()){
					if(mp!=null){
						mp.pause();
					}
				} else {
					// Resume song
					if(mp!=null){
						mp.start();
						playSong(currentSongIndex);
					}
				}
			}
		});
		
		btnNext = (CircolareConIndicatore) rootView.findViewById(R.id.btnNext);
		btnNext.setIconaAccesa(BitmapFactory.decodeResource(getActivity().getBaseContext().getResources(),
                R.drawable.ic_next));
		btnNext.setIconaSpenta(BitmapFactory.decodeResource(getActivity().getBaseContext().getResources(),
                R.drawable.ic_next));
		btnNext.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// check if next song is there or not
				if(currentSongIndex < (songsList.size() - 1)){
					currentSongIndex = currentSongIndex + 1;
					if(mp.isPlaying()){
						if(mp!=null){
							playSong(currentSongIndex);
						}
					} else { 
						playSong(currentSongIndex);
				 		mp.pause();
					}	
				} else {
					// play first song
					currentSongIndex = 0;
					if(mp.isPlaying()){
						if(mp!=null){
							playSong(currentSongIndex + 1);
						}
					} else { 
						playSong(currentSongIndex);
				 		mp.pause();
					}
				}				
			}
		});
		
		ImageButton volume = (ImageButton) rootView.findViewById(R.id.volumeImageButton);
		volume.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AudioManager audio = (AudioManager) getActivity().getBaseContext().
						getSystemService(Context.AUDIO_SERVICE);
				audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
			}
		});
 		
 		// Listeners
 		songProgressBar.setOnSeekBarChangeListener(this); // Important
 		mp.setOnCompletionListener(this); // Important
 				
 		// Getting all songs list
 		songsList = GionjiUtils.getPlayList();	 	
 		if (songsList.size() == 0) {
	 		HashMap<String, String> song = new HashMap<String, String>();
			song.put("songTitle", "Nessuna canzone trovata");
			song.put("songPath", "");
			
			// Adding each song to SongList
			songsList.add(song);
 		}
 		
 		playSong(currentSongIndex);
 		mp.pause();
 		
		return rootView;
	}    
	
	/**
	 * Function to play a song
	 * @param songIndex - index of song
	 * */
	public void  playSong(int songIndex){
		try {
        	mp.reset();
			mp.setDataSource(songsList.get(songIndex).get("songPath"));
			mp.prepare();
			mp.start();
			
			setLayoutSongInfo(songIndex);
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setLayoutSongInfo(int songIndex) {
		MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    	mmr.setDataSource(songsList.get(songIndex).get("songPath"));
    	songTitleLabel.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
    	songAlbumLabel.setText(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) + " - " +
    			mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
    	
    	byte [] data = mmr.getEmbeddedPicture();
	    if(data != null) {
	        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
	        songAlbumArtImage.setImageBitmap(bitmap); //associated cover art in bitmap
	        songAlbumArtImage.setAdjustViewBounds(true);
	    } else {
	    	songAlbumArtImage.setImageResource(R.drawable.music); //any default cover resourse folder
	    	songAlbumArtImage.setAdjustViewBounds(true);
	    }
	    
	    // set Progress bar values
		songProgressBar.setProgress(0);
		songProgressBar.setMax(100);
		// Updating progress bar
		updateProgressBar();
	}
	
	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);        
    }	
	
	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
	   public void run() {
		   long totalDuration = mp.getDuration();
		   long currentDuration = mp.getCurrentPosition();
		  
		   // Displaying Total Duration time
		   songTotalDurationLabel.setText("" + GionjiUtils.milliSecondsToTimer(totalDuration));
		   // Displaying time completed playing
		   songCurrentDurationLabel.setText("" + GionjiUtils.milliSecondsToTimer(currentDuration));
		   
		   // Updating progress bar
		   int progress = (int)(GionjiUtils.getProgressPercentage(currentDuration, totalDuration));
		   //Log.d("TAG", ""+progress);
		   songProgressBar.setProgress(progress);
		   
		   // Running this thread after 100 milliseconds
	       mHandler.postDelayed(this, 100);
	   }
	};
	
	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
    }
	
	/**
	 * When user stops moving the progress hanlder
	 * */
	@Override
    public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = GionjiUtils.progressToTimer(seekBar.getProgress(), totalDuration);
		
		// forward or backward to certain seconds
		mp.seekTo(currentPosition);
		
		// update timer progress again
		updateProgressBar();
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

	/**
	 * On Song Playing completed
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		if (isVisible) {
			if (currentSongIndex < (songsList.size() - 1)) {
				playSong(currentSongIndex + 1);
				currentSongIndex = currentSongIndex + 1;
			} else {
				// play first song
				playSong(0);
				currentSongIndex = 0;
			}
		}
		
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {	
	}
	
	@Override
	 public void onDestroy(){
	 super.onDestroy();
	    mp.release();
	 }

}
