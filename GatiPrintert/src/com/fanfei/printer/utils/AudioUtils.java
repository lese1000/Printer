package com.fanfei.printer.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class AudioUtils {
	private static String parentPath =null;
	static{
		String path = AudioUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			path = java.net.URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 parentPath = new File(path).getParent();
	}
	public static final String SUCCESS_FILE=parentPath+File.separator+"sound/success.wav";
	public static final String ERROR_FILE=parentPath+File.separator+"sound/error.wav";
	
	public enum TipType{
		SUCCESS,ERROR
	}
	

	public static void main(String[] args) {
//		play(TipType.ERROR);//ok
//		testPlay();
//		Toolkit.getDefaultToolkit().beep(); //default-success
//		loadAudioFileAndPlay("misc/success.wav");//ok
//		String path = System.getProperty("java.class.path");
		String path = AudioUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		 parentPath = new File(path).getParent();
		System.out.println(parentPath);
	}
	
	public static void play(TipType type){
		switch (type){
			case SUCCESS:
//				loadAudioFileAndPlay(SUCCESS_FILE);
				playAudioUseThread(SUCCESS_FILE);
				break;
			case ERROR:
//				loadAudioFileAndPlay(ERROR_FILE);
				playAudioUseThread(ERROR_FILE);
				break;
			default:
				break;
		}
		
	}
	
	private  static void playAudioUseThread(String wavfile){
//		URL fileUrl = AudioUtils.class.getClassLoader().getResource(wavfile);
		
//		System.out.println(fileUrl.getPath());
//		PlaySounds paPlaySounds =new PlaySounds(fileUrl.getPath());
		PlaySounds paPlaySounds =new PlaySounds(wavfile);
		paPlaySounds.start();
	}
	
	private static void loadAudioFileAndPlay(String wavfile){
	        AudioInputStream audio;  
	        AudioFormat format;  
	        SourceDataLine auline = null;  
	        DataLine.Info info;  
	        try {  
//	           File file = new File("F://test.mp3");  
//	            audio = AudioSystem.getAudioInputStream(file);  
	            audio = AudioSystem.getAudioInputStream(AudioUtils.class.getClassLoader().getResourceAsStream(wavfile));
	            format = audio.getFormat();  
	            info = new DataLine.Info(SourceDataLine.class, format);  
	            auline = (SourceDataLine) AudioSystem.getLine(info);  
	            auline.open(format);  
	            auline.start();  
	            int nBytesRead = 0;  
	            byte[] abData = new byte[524288];  
	            while (nBytesRead != -1) {  
	                nBytesRead = audio.read(abData, 0, abData.length);  
	                if (nBytesRead >= 0) {  
	                    auline.write(abData, 0, nBytesRead);  
	                }  
	            }  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } catch (UnsupportedAudioFileException e) {  
	            e.printStackTrace();  
	        } catch (LineUnavailableException e) {  
	            e.printStackTrace();  
	        } finally {  
	            auline.drain();  
	            auline.close();  
	        }  
	}
	
	public static void testPlay(){
		   try {  
			   
	            // Open an audio input stream.  
	             URL url = AudioUtils.class.getClass().getResource("/misc/test.mp3");  
//	            File file = new File("bin/com/whongshe/utils/hello.wav");  
	  
	             AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);  
//	            AudioInputStream audioIn = AudioSystem.getAudioInputStream(AudioUtils.class.getClassLoader().getResource("misc/msg.wav"));  
	            // Get a sound clip resource.  
	            Clip clip = AudioSystem.getClip();  
	            // Open audio clip and load samples from the audio input stream.  
	            clip.open(audioIn);  
	            clip.start();  
	        } catch (UnsupportedAudioFileException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } catch (LineUnavailableException e) {  
	            e.printStackTrace();  
	        }  
	}
	
	

}
