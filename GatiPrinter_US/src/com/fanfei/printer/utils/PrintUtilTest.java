package com.fanfei.printer.utils;

import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.standard.MediaPrintableArea;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PrintUtilTest {

	public static void main(String[] args) {
		showUseablePrintService(DocFlavor.INPUT_STREAM.JPEG);
		try {
//			pdfToImgAndPrint("F:\\ups-lable-os-1Z6Y8253YW90227423.pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showUseablePrintService(DocFlavor docFlavor){
		PrintService[] services = getUseablePrintServiceByDocFlavor(docFlavor);
		int i = 0;
		for(PrintService item:services){
			i++;
			System.out.println("Service-"+i+"-Name:"+item.getName());
			showSelectedPrintServiceSupportedAttribute(item);
		}
	}
	
	public static void showSelectedPrintServiceSupportedAttribute(PrintService selectedService){
		Class[] supportedAttributes =
                selectedService.getSupportedAttributeCategories();
        for (Class supportedAttribute : supportedAttributes) {
           
            if (supportedAttribute.getName().equals("javax.print.attribute.standard.MediaPrintableArea")) {
            	 MediaPrintableArea area =  (MediaPrintableArea) selectedService.getDefaultAttributeValue(supportedAttribute);
            	 System.err.println("service--width:"+area.getWidth(MediaPrintableArea.MM)); 
            	 System.err.println("service--height:"+area.getHeight(MediaPrintableArea.MM)); 
            	 
			}
        }
	}
	
	public static PrintService getSelectedPrintService(String serviceName){
		PrintService[] services = getUseablePrintServiceByDocFlavor(DocFlavor.INPUT_STREAM.PNG);
		PrintService selectedService = null;
		for(PrintService item:services){
			if(item.getName().equals(serviceName)){
				selectedService = item;
				System.out.println("found:"+serviceName);
			}
		}
		return selectedService;
	}
	
	public static PrintService[] getUseablePrintServiceByDocFlavor(DocFlavor docFlavor){
		PrintService[] services = PrintServiceLookup.lookupPrintServices(docFlavor, null);
		return services;
	}
	
	public static PrintService getDefaultPrintService(){
		PrintService services = PrintServiceLookup.lookupDefaultPrintService();
		return services;
	}
	
	public static void printByDefault(Doc doc) throws PrintException{
		PrintService printService = getDefaultPrintService();
		DocPrintJob docPrintJob = printService.createPrintJob();
		docPrintJob.print(doc, null);
	}
	
	public static void pdfToImgAndPrint(String filename) throws Exception{
//		SimpleDoc doc = new SimpleDoc(printData, DocFlavor.INPUT_STREAM.PNG, null);
		PDDocument document = PDDocument.load(new File(filename)); 
		PDFRenderer pdfRenderer = new PDFRenderer(document);
		int pageCounts = document.getNumberOfPages();
		PrintService printService = getSelectedPrintService("Zebra  ZP 450-200 dpi");
		DocPrintJob printJob = printService.createPrintJob();
		PrinterJob pj = (PrinterJob) printJob;
		for(int i = 0;i<pageCounts;i++){
			BufferedImage bufImage = pdfRenderer.renderImageWithDPI(i,96);
			String tmpDir = System.getProperty("java.io.tmpdir");
			System.out.println("tmpDir:"+tmpDir);
			File tmpFile = new File(tmpDir+"tmpImg.png");
			ImageIO.write(bufImage, "PNG", tmpFile);
			
			/*SimpleDoc doc = new SimpleDoc(new FileInputStream(tmpFile), DocFlavor.INPUT_STREAM.PNG, null);
			printJob.print(doc, null);*/
		}
		
	}

}
