package com.fanfei.updater.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;


public class DownloaderThread extends Thread{
	private static  String PRINTER_PATH_NAME="printer.jar";
	private static String parentPath =null;
	static{
		String path = DownloaderThread.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			path = java.net.URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 parentPath = new File(path).getParent();
		 PRINTER_PATH_NAME = parentPath+File.separator+File.separator+"printer.jar";
	}
	private Display display;
	private ProgressBar progressBar;
	private String fileUrl;
	private Label tipLabel;
	private Button btnStart;
	
	public DownloaderThread(Display display, ProgressBar progressBar,String fileUrl,Label tipLabel,Button btnStart){
		this.display = display;
		this.progressBar = progressBar;
		this.fileUrl = fileUrl;
		this.tipLabel=tipLabel;
		this.btnStart = btnStart;
	}

	@Override
	public void run() {
		try {
			System.out.println("url::"+fileUrl);
			tipLabel.setText("Downloading ...");
			FileOutputStream fos = new FileOutputStream(new File(PRINTER_PATH_NAME));
			URL url = new URL(fileUrl);
			HttpURLConnection  httpConnection = (HttpURLConnection) url.openConnection();
			System.out.println("length::"+httpConnection.getContentLength());
			progressBar.setMaximum(httpConnection.getContentLength());
			InputStream inputStream = httpConnection.getInputStream();
			inputStream.available();
			byte[] buffer = new byte[1024];
			int size = 0;
			while((size=inputStream.read(buffer))!=-1){
				fos.write(buffer, 0, size);
				progressBar.setSelection(progressBar.getSelection()+size);
				System.out.println("size:"+size);
//				Thread.sleep(1000);
			}
//			progressBar.dispose();
			fos.flush();
			fos.close();
			inputStream.close();
//			loadPrinter();
			tipLabel.setText("Download is complete,click 'start-up'");
			btnStart.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void loadPrinter(){
		try {
			Process process = Runtime.getRuntime().exec("java -jar printer.jar");
		} catch (Exception e) {
			e.printStackTrace();
		}
		display.close();
	}
}
