package com.fanfei.printer.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class AppConfiguratorText {
	
	private static String parentPath =null;
	static{
		String path = AppConfiguratorText.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			path = java.net.URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 parentPath = new File(path).getParent();
	}
	
	public enum TextName{
		VERSION_CONFIG("printer_lib/version_config.jar"),USER_CONFIG("printer_lib/user_config.jar");
		TextName(String value){
			this.value = value;
		}
		private String value;
		public String getValue() {
			return value;
		}
	}
	
	public static String  readFile(TextName textName){
		return readTxtFile(textName.getValue());
	}
	
	public static void writeFile(TextName textName,String content){
		writeFile(textName.getValue(),content);
	}
	
	/**
	 * 写txt里的单行内容
	 * @param filePath  文件路径
	 * @param content  写入的内容
	 */
	public static void writeFile(String fileName,String content){
		String filePath = parentPath+File.separator+fileName;//与bin目录同级下的config目录下的文件
		System.out.println(filePath);
		try {
	        OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath),"utf-8");      
	        BufferedWriter writer=new BufferedWriter(write);          
	        writer.write(content);      
	        writer.close(); 

	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取txt里的单行内容
	 * @param filePath  文件路径
	 */
	public static String readTxtFile(String fileName) {
		try {
			
			String filePath = parentPath+File.separator+fileName;
			String encoding = "utf-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { 		// 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
				new FileInputStream(file), encoding);	// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					return lineTxt;
				}
				read.close();
			}else{
				System.out.println("找不到指定的文件,查看此路径是否正确:"+filePath);
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
		}
		return "";
	}
	
	public static void main(String[] args){
		writeFile(TextName.VERSION_CONFIG,"zhangsan");
	}

}
