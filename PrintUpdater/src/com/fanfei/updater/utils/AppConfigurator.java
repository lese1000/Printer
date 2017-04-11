package com.fanfei.updater.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public final class AppConfigurator {
	private static AppConfigurator property;
	private Properties pro = new Properties();

	private AppConfigurator() {
		// 加载 classes下 appConfigurator.properties 属性文件
		
		InputStream inStream = AppConfigurator.this.getClass().getClassLoader().getResourceAsStream("config/userconfig.properties");
		try {
			if (inStream == null) {
				throw new NullPointerException("classes \u76EE\u5F55\u4E0B\u627E\u4E0D\u5230 appConfigurator.properties \u5C5E\u6027\u6587\u4EF6");
			}
			pro.load(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""));	//项目路径
//		filePath = filePath.replaceAll("file:/", "");
//		filePath = filePath.replaceAll("%20", " ");
//		filePath = filePath.trim() + "system.properties";
//		try {
//			pro.load(new FileInputStream(filePath));   ;
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private static AppConfigurator getInstance() {
		if(null==property){
			property = new AppConfigurator();
		}
		return property;
	}

	public static final String getProperty(String propertyKey) {
		return getInstance().pro.getProperty(propertyKey);
	}
	
	public static void  putProperty(String key,String value){
		getInstance().pro.setProperty(key, value);
//		getInstance().pro.put(key, value);
		try {
			URL url = AppConfigurator.class.getClassLoader().getResource("config/userconfig.properties");
//			System.out.println("path::"+url.getPath());
			FileOutputStream fos = new FileOutputStream(url.getPath());
			getInstance().pro.store(fos, "just for user config");
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args){
		AppConfigurator.putProperty("printer.user.selected", "fff");
		AppConfigurator.putProperty("printer.user", "xaaaxx");
		System.out.println(AppConfigurator.getProperty("printer.user.selected"));
	}

}
