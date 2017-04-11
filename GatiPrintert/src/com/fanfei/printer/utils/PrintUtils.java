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

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
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
	private static int width = 100;
	private static int height = 150;
    private PrintUtils()
    {
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
        String filename = "F:/test.pdf";
        PDDocument document = PDDocument.load(new File(filename));
        
        // choose your printing method:
//        print(document); 
        //printWithAttributes(document);
        //printWithDialog(document);
        //printWithDialogAndAttributes(document);
        printWithPaper(document);
        document.close();
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
    private static void printWithPaper(PDDocument document)
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
    
    /**
     * 网络文件打印
     * @param uri 网络文件路径
     * @throws Exception
     */
    public static void printRemoteFile(String uri) throws Exception{
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
//        print(document);
        getWidthAndHeight();//获取打印机打印的页面大小。
        printWithPaper(document);
        
    }
    /**
     *  打印本地文件
     * @param filename 本地文件完整路径
     * @throws Exception
     */
    public static void printLocationFile(String filename) throws Exception{
    	PDDocument document = PDDocument.load(new File(filename));
    	getWidthAndHeight();//获取打印机打印的页面大小。
//    	print(document);
    	printWithPaper(document);
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
	
	public static void getWidthAndHeight(){
		/**
         * Find Available printers
         */
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
            	 
            	 width = (int) area.getWidth(MediaPrintableArea.MM);
            	 height = (int) area.getHeight(MediaPrintableArea.MM);
			}
        }
	}
}