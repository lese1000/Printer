/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fanfei.printer.utils;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.PageRanges;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;

/**
 * Examples of various different ways to print PDFs using PDFBox.
 */
public final class PrintUtils{
//	private static int width = 100;
//	private static int height = 150;
    private PrintUtils()
    {
    }   

    

    /**推荐使用方式，根据文档实际大小进行打印
     * Prints the document at its actual size. This is the recommended way to print.
     */
    private static void print(PDDocument document) throws IOException, PrinterException
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
        job.print();
    }

    /**根据用户自定义属性值打印
     * Prints using custom PrintRequestAttribute values.
     */
    private static void printWithAttributes(PDDocument document)
            throws IOException, PrinterException
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(new PageRanges(1, 1)); // pages 1 to 1

        job.print(attr);
    }

    /**打印时弹出打印预览对话框
     * Prints with a print preview dialog.
     */
    private static void printWithDialog(PDDocument document) throws IOException, PrinterException
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        if (job.printDialog())
        {
            job.print();
        }
    }

    /**打印时弹出打印预览对话框和使用用户自定义打印属性
     * Prints with a print preview dialog and custom PrintRequestAttribute values.
     */
    private static void printWithDialogAndAttributes(PDDocument document)
            throws IOException, PrinterException
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        attr.add(new PageRanges(1, 1)); // pages 1 to 1

        if (job.printDialog(attr))
        {
            job.print(attr);
        }
    }
    
    /**用户自定义打印页面大小
     * Prints using a custom page size and custom margins.
     */
    private static void printWithPaper(PDDocument document,int width,int height)
            throws IOException, PrinterException
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));

        // define custom paper
        Paper paper = new Paper();
//        paper.setSize(595, 842); // 1/72 inch/A4 210*297
//        paper.setSize(283, 420); // 1/72 inch/100*150 mm
        paper.setSize(pointNo(width), pointNo(height)); 
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins

        // custom page format
        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        
        // override the page format
        Book book = new Book();
        // append all pages
        book.append(new PDFPrintable(document), pageFormat, document.getNumberOfPages());
        job.setPageable(book);
        
        job.print();
    }
    private static void printWithPaperUseSelectedService(PDDocument document,int width,int height,PrintService selectedService)
    		throws IOException, PrinterException
    		{
    	PrinterJob job = PrinterJob.getPrinterJob();
    	job.setPageable(new PDFPageable(document));
    	
    	// define custom paper
    	Paper paper = new Paper();
//        paper.setSize(595, 842); // 1/72 inch/A4 210*297
//        paper.setSize(283, 420); // 1/72 inch/100*150 mm
    	paper.setSize(pointNo(width), pointNo(height)); 
    	paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight()); // no margins
    	
    	// custom page format
    	PageFormat pageFormat = new PageFormat();
    	pageFormat.setPaper(paper);
    	
    	// override the page format
    	Book book = new Book();
    	// append all pages
    	book.append(new PDFPrintable(document), pageFormat, document.getNumberOfPages());
    	job.setPageable(book);
    	job.setPrintService(selectedService);
    	job.print();
    		}
    
    /**
     * 网络文件打印
     * @param uri 网络文件路径
     * @throws Exception
     */
    public static PDDocument getNetworkFile(String uri) throws Exception{
    	PDDocument document = null;
    	URL url = new URL(uri);
	    // 构建一URL对象
	    InputStream is = url.openStream();
        //加载 pdf 文档(方式二)
        PDFParser parser = new PDFParser(new RandomAccessBuffer(is));
        //关闭流
        is.close();
        parser.parse();
        document = parser.getPDDocument();
        return document;
    }
    /**
     * 网络文件打印
     * @param uri 网络文件路径
     * @throws Exception
     */
    public static Doc getNetworkFileRtnDoc(String uri) throws Exception{
    	URL url = new URL(uri);
    	// 构建一URL对象
    	InputStream is = url.openStream();
    	//加载 pdf 文档(方式二)
		Doc doc = new SimpleDoc(is, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
    	//关闭流
		is.close();
    	return doc;
    }
    public static void printRemoteFileByDefaultPrinter(String uri) throws Exception{
    	
//        print(document);
        Map<String, Integer> sizeMap = getWidthAndHeightByDefaultPrinter();//获取打印机打印的页面大小。
        if(sizeMap.isEmpty()){
    		throw new PrinterException("未获取到打印机信息");
    	}
        printWithPaper(getNetworkFile(uri),sizeMap.get("width"),sizeMap.get("height"));
        
    }
    
    //指定打印服务
    public static void printRemoteFileByAppointPrinter(String uri,PrintService selectedService) throws Exception{
    	Map<String, Integer> sizeMap = getWidthAndHeightUseAppointPrinter(selectedService);
    	System.out.println("选择的打印机为："+selectedService.getName());
    	if(sizeMap.isEmpty()){
    		throw new PrinterException("未获取到打印机信息");
    	}
//    	printWithPaper(getNetworkFile(uri),sizeMap.get("width"),sizeMap.get("height"));
    	printWithPaperUseSelectedService(getNetworkFile(uri),sizeMap.get("width"),sizeMap.get("height"),selectedService);
    }
    /**
     *  打印本地文件
     * @param filename 本地文件完整路径
     * @throws Exception
     */
    public static void printLocationFileByDefaultPrinter(String filename) throws Exception{
    	PDDocument document = PDDocument.load(new File(filename));
    	Map<String, Integer> sizeMap = getWidthAndHeightByDefaultPrinter();//获取打印机打印的页面大小。
    	if(sizeMap.isEmpty()){
    		throw new PrinterException("未获取到打印机信息");
    	}
    	printWithPaper(document,sizeMap.get("width"),sizeMap.get("height"));
    }
    
    /**
	 * 毫米转换为点数 (取整)
	 * 
	 * @param mm 毫米数
	 *            
	 * @return 点数
	 */
	public static int pointNo(int mm) {
		return (int) Math.floor((mm * 72) / 25.4);
	}
	
	public static Map<String,Integer> getWidthAndHeightByDefaultPrinter(){
		/**
         * Find Available printers
         */
		Map<String,Integer> sizeMap = new HashMap<>();
        PrintService[] services =
                PrintServiceLookup.lookupPrintServices(
                        DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
        //需要判断
        PrintService selectedService = services[0];
        
        Class[] supportedAttributes =
                selectedService.getSupportedAttributeCategories();
        for (Class supportedAttribute : supportedAttributes) {
           
            if (supportedAttribute.getName().equals("javax.print.attribute.standard.MediaPrintableArea")) {
            	 MediaPrintableArea area =  (MediaPrintableArea) selectedService.getDefaultAttributeValue(supportedAttribute);
            	 
//            	System.err.println("service--width:"+area.getWidth(MediaPrintableArea.MM)); 
//            	System.err.println("service--height:"+area.getHeight(MediaPrintableArea.MM)); 
            	 
            	 int width = (int) area.getWidth(MediaPrintableArea.MM);
            	 int height = (int) area.getHeight(MediaPrintableArea.MM);
            	 sizeMap.put("width", width);
            	 sizeMap.put("height", height);
			}
        }
        
        return sizeMap;
	}
	
	public static Map<String,Integer> getWidthAndHeightUseAppointPrinter(PrintService selectedService){
		Map<String,Integer> sizeMap = new HashMap<>();
		Class[] supportedAttributes =
                selectedService.getSupportedAttributeCategories();
        for (Class supportedAttribute : supportedAttributes) {
           
            if (supportedAttribute.getName().equals("javax.print.attribute.standard.MediaPrintableArea")) {
            	 MediaPrintableArea area =  (MediaPrintableArea) selectedService.getDefaultAttributeValue(supportedAttribute);
            	 
//            	System.err.println("service--width:"+area.getWidth(MediaPrintableArea.MM)); 
//            	System.err.println("service--height:"+area.getHeight(MediaPrintableArea.MM)); 
            	 
            	 int width = (int) area.getWidth(MediaPrintableArea.MM);
            	 int height = (int) area.getHeight(MediaPrintableArea.MM);
            	 sizeMap.put("width", width);
            	 sizeMap.put("height", height);
			}
        }
        return sizeMap;
	}
	
	public static PrintService[] getUsablePrinterList(){
		PrintService[] services =
                PrintServiceLookup.lookupPrintServices(
                        DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
		/*for(PrintService erviceItem : services){
			System.out.println(erviceItem.getName());
		}*/
		return services;
	}
	
	public static void testGetPrinterServiceInfo(){
		Map<String,Integer> sizeMap = new HashMap<>();
        PrintService[] services =
                PrintServiceLookup.lookupPrintServices(
                        DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
//        PrintService selectedService = services[0];
        for(PrintService selectedService:services){
        	
        	 Class[] supportedAttributes =
                     selectedService.getSupportedAttributeCategories();
             for (Class supportedAttribute : supportedAttributes) {
                
                 if (supportedAttribute.getName().equals("javax.print.attribute.standard.MediaPrintableArea")) {
                 	 MediaPrintableArea area =  (MediaPrintableArea) selectedService.getDefaultAttributeValue(supportedAttribute);
                 	System.out.println("servie--name:"+selectedService.getName());
                 	System.err.println("service--width:"+area.getWidth(MediaPrintableArea.MM)); 
                 	System.err.println("service--height:"+area.getHeight(MediaPrintableArea.MM)); 
                 	 
     			}
             }
        }
       
		
	}
	
	public static PrintService getSelectedPrinter(String serviceName){
		Map<String,Integer> sizeMap = new HashMap<>();
        PrintService[] services =
                PrintServiceLookup.lookupPrintServices(
                        DocFlavor.SERVICE_FORMATTED.PAGEABLE, null);
        PrintService selectedService = null;
        for(PrintService serviceItem:services){
        	if(serviceItem.getName().equals(serviceName)){
        		selectedService = serviceItem;
        		return selectedService;
        	}
        }
        
        return selectedService;
		
	}
	
	public static void testUseSelectedPrinter(){
		PrintService selectedService = getSelectedPrinter("Zebra  ZP 450-200 dpi");
		try {
			Map<String, Integer> sizeMap = getWidthAndHeightUseAppointPrinter(selectedService);
			PDDocument document = PDDocument.load(new File("F:\\ups-lable-os-1Z6Y8253YW90227423.pdf"));
			printWithPaperUseSelectedService(document, sizeMap.get("width"), sizeMap.get("height"), selectedService);;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
     * Entry point.
     */
    public static void main(String args[]) throws PrinterException, IOException
    {
       /* if (args.length != 1)
        {
            System.err.println("usage: java " + Printing.class.getName() + " <input>");
            System.exit(1);
        }*/

//        String filename = args[0];
//        String filename = "F:/test.pdf";
//        PDDocument document = PDDocument.load(new File(filename));
        
        // choose your printing method:
//        print(document); 
        //printWithAttributes(document);
        //printWithDialog(document);
        //printWithDialogAndAttributes(document);
//        printWithPaper(document);
//        document.close();
    	
//    	getUsablePrinterList();
//    	testGetPrinterServiceInfo();
    	testUseSelectedPrinter();
    }
}