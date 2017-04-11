package com.fanfei.printer.test;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PrintPdfTest {

	public static void main(String[] args) {
		test2();

	}
	
	public static void test1(){
		try {
			  // 声明抛出所有例外
		    URL tirc = new URL("http://www.baidu.com/img/baidu_sylogo1.gif");
		    // 构建一URL对象
		    InputStream is = tirc.openStream();

		    FileOutputStream fos = new FileOutputStream(
		      "/mnt/sdcard/baidu_sylogo21.gif");

		    byte[] buffer = new byte[1024];
		    int len = is.read(buffer);

		    while (len != -1) {

		     fos.write(buffer, 0, len);
		     len = is.read(buffer);
		    }

		    fos.close();
		    is.close();
		   
		} catch (Exception e) {
			
		}
	}
	
	public static void test2(){
		InputStream input = null;
		PDDocument document = null;
//		input = new FileInputStream( pdfFile );
		try {
//			 URL tirc = new URL("http://lable20170119.oss-cn-hangzhou.aliyuncs.com/1b7d742134a74ed79bf73cdc6748b3d7");
			 URL tirc = new URL("http://116.62.35.163:8070/gati_res/test.pdf");
			    // 构建一URL对象
			    InputStream is = tirc.openStream();
		        //加载 pdf 文档
		        PDFParser parser = new PDFParser(new RandomAccessBuffer(is));
		        is.close();//关闭流
		        parser.parse();
		        document = parser.getPDDocument();
		        
		        int pages = document.getNumberOfPages();

	            // 读文本内容
	            PDFTextStripper stripper=new PDFTextStripper();
	            // 设置按顺序输出
	            stripper.setSortByPosition(true);
	            stripper.setStartPage(1);
	            stripper.setEndPage(pages);
	            String content = stripper.getText(document);
	            System.out.println(content); 
		} catch (Exception e) {
			e.printStackTrace();
		}
       
	}

}
