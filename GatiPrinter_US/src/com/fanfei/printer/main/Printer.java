package com.fanfei.printer.main;

import java.util.HashMap;
import java.util.Map;

import javax.print.PrintService;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fanfei.printer.utils.ApiUtils;
import com.fanfei.printer.utils.AppConfiguratorText;
import com.fanfei.printer.utils.AppConfiguratorText.TextName;
import com.fanfei.printer.utils.AudioUtils;
import com.fanfei.printer.utils.AudioUtils.TipType;
import com.fanfei.printer.utils.PrintUtils;

public class Printer {

	protected Shell shell;
	private Text searchTxt;
	private Text receiverTxt;
	private Text addrTxt;
	private Text telTxt;
	private Text mobileTxt;
	private Combo combo;
	private Label tipLabel;
	private PrintService userSelectedService;
	private Map<String,PrintService> printServiceMap;
	private Label label_6;
	private Text trackNumTxt;
	private String username;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Printer window = new Printer();
			window.open(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open(Map<String,Object> initData) {
		Display display = Display.getDefault();
		display.addFilter(SWT.KeyDown, new PrinterKeyDownListener());
		createContents();
		shell.open();
		shell.layout();
		//**去除login界面的事件过滤器和关闭login界面
		if(null!=initData&&null!=initData.get("loginShell")){
			Listener loginListener = (Listener) initData.get("loginListener");
			Display loginDisplay = (Display) initData.get("loginDisplay");
			loginDisplay.removeFilter(SWT.KeyDown, loginListener);
			Shell loginShell = (Shell) initData.get("loginShell");
			loginShell.close();
		}
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(512, 370);
		shell.setText("GATI_Printer_US");
		
		Group group = new Group(shell, SWT.NONE);
		group.setBounds(30, 126, 439, 196);
		
		Label label_2 = new Label(group, SWT.RIGHT);
		label_2.setBounds(10, 52, 72, 17);
		label_2.setText("Contact name：");
		
		receiverTxt = new Text(group, SWT.BORDER);
		receiverTxt.setEnabled(false);
		receiverTxt.setEditable(false);
		receiverTxt.setBounds(85, 49, 145, 23);
		
		Label label_3 = new Label(group, SWT.RIGHT);
		label_3.setBounds(10, 87, 72, 17);
		label_3.setText("Tel：");
		
		Label label_4 = new Label(group, SWT.RIGHT);
		label_4.setBounds(10, 123, 72, 17);
		label_4.setText("Address：");
		
		addrTxt = new Text(group, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		addrTxt.setEnabled(false);
		addrTxt.setEditable(false);
		addrTxt.setText("");
		addrTxt.setToolTipText("");
		addrTxt.setBounds(85, 117, 340, 53);
		
		telTxt = new Text(group, SWT.BORDER);
		telTxt.setEnabled(false);
		telTxt.setEditable(false);
		telTxt.setBounds(85, 83, 145, 23);
		
		Label label_5 = new Label(group, SWT.NONE);
		label_5.setBounds(236, 88, 52, 17);
		label_5.setText("Mobile：");
		
		mobileTxt = new Text(group, SWT.BORDER);
		mobileTxt.setEnabled(false);
		mobileTxt.setEditable(false);
		mobileTxt.setBounds(292, 83, 133, 23);
		
		label_6 = new Label(group, SWT.RIGHT);
		label_6.setBounds(10, 21, 72, 17);
		label_6.setText("Tracking Num：");
		
		trackNumTxt = new Text(group, SWT.BORDER);
		trackNumTxt.setEnabled(false);
		trackNumTxt.setEditable(false);
		trackNumTxt.setBounds(85, 16, 340, 23);
		
		Group group_1 = new Group(shell, SWT.NONE);
		group_1.setBounds(30, 36, 439, 84);
		
		Label label_1 = new Label(group_1, SWT.RIGHT);
		label_1.setBounds(10, 22, 72, 17);
		label_1.setText("Printer：");
		
		combo = new Combo(group_1, SWT.READ_ONLY);
		combo.setBounds(85, 16, 340, 25);
		combo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String tmpSelectedService = combo.getItem(combo.getSelectionIndex());
				System.out.println(tmpSelectedService);
				userSelectedService = printServiceMap.get(tmpSelectedService);
				saveUserConifg(tmpSelectedService);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Label label = new Label(group_1, SWT.RIGHT);
		label.setBounds(10, 54, 72, 17);
		label.setText("Tracking Num：");
		
		searchTxt = new Text(group_1, SWT.BORDER);
//		trackNumTxt.setEnabled(false);
//		trackNumTxt.setEditable(false);
		searchTxt.setBounds(85, 51, 340, 23);
		searchTxt.setFocus();
		
		tipLabel = new Label(shell, SWT.NONE);
		tipLabel.setAlignment(SWT.CENTER);
		tipLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		tipLabel.setBounds(30, 13, 439, 17);
		
		//初始化
		init();

	}
	
	public class PrinterKeyDownListener implements Listener{

		@Override
		public void handleEvent(Event event) {
			if(event.keyCode==13||event.keyCode==16777296){
				doBizz();
			}
		}
	}
	
	public void init(){
		loadUseablePrinter();
	}
	
	public String getUserConifg(){
		String userSelectedPrinter = AppConfiguratorText.readFile(TextName.USER_CONFIG).split(":")[1];
		return userSelectedPrinter;
	}
	public void saveUserConifg(String printerName){
		AppConfiguratorText.writeFile(TextName.USER_CONFIG, "user.selected:"+printerName);
	}
	
	public void loadUseablePrinter(){
		PrintService[] printServiceArr = PrintUtils.getUsablePrinterList();
		
		if(null==printServiceArr||printServiceArr.length==0){
			setErrTip("Printer no found, print service not available");
			return;
		}
		String[] useablePrinterNameArr =new String[printServiceArr.length];
		boolean isExisted = false;
		printServiceMap = new HashMap<String, PrintService>();
		for(int i=0;i<printServiceArr.length;i++){
			printServiceMap.put(printServiceArr[i].getName(), printServiceArr[i]);
			useablePrinterNameArr[i]=printServiceArr[i].getName();
			if(useablePrinterNameArr[i].equals(getUserConifg())){
				String tmp = useablePrinterNameArr[0];
				useablePrinterNameArr[0]=useablePrinterNameArr[i];
				useablePrinterNameArr[i]=tmp;
				userSelectedService = printServiceArr[i];
				isExisted = true;
			}
		}
		/*if(!isExisted&&!"".equals(getUserConifg())){
			setErrTip("未找到用户指定的打印机，将采用默认打印机");
		}*/
		combo.setItems(useablePrinterNameArr);
		combo.select(0);
		
	}
	
	public void setErrTip(String msg){
		tipLabel.setText(msg);
		AudioUtils.play(TipType.ERROR);
	}
	
	public void setSuccessTip(){
		AudioUtils.play(TipType.SUCCESS);
	}
	
	public void clearSearchTxt(){
		searchTxt.setText("");
	}
	
	public void clearAll(){
		trackNumTxt.setText("");
		receiverTxt.setText("");
		addrTxt.setText("");
		telTxt.setText("");
		mobileTxt.setText("");
		tipLabel.setText("");
	}
	
	//====业务相关=====
	public void doBizz(){
		clearAll();
		doSearch();
		clearSearchTxt();
	}
	
	public <S> void doSearch(){
		String searchTrackNum = searchTxt.getText();
		if(null==searchTrackNum||"".equals(searchTrackNum.trim())){
			setErrTip("Query number cannot be empty!");
			return;
		}
		
		Map<String, Object> rtnResult = ApiUtils.getDataByTrackNum(searchTrackNum, username);
		if(null!=rtnResult&&!rtnResult.isEmpty()){
			String code = (String) rtnResult.get("code");
			if("0000".equals(code)){
				Map<String,Object> dataMap = (Map<String, Object>) rtnResult.get("obj");
				trackNumTxt.setText((String) rtnResult.get("TrackNum"));
				Object receiver = dataMap.get("Receiver");
				Object address = dataMap.get("Address");
				Object mobile = dataMap.get("Mobile");
				Object areaCode = dataMap.get("AreaCode");
				Object phone = dataMap.get("Phone");
				
				if(null!=receiver){
					receiverTxt.setText(receiver+"");
				}else{
					receiverTxt.setText("no data");
				}
				
				if(null!=address){
					addrTxt.setText(address+"");
				}else{
					addrTxt.setText("no data");
				}
				
				if(null!=mobile){
					mobileTxt.setText(mobile+"");
				}else{
					mobileTxt.setText("no data");
				}
				
				String telFormat = "";
				if(null!=areaCode&&!"".equals(areaCode)){
					telFormat = "("+dataMap.get("AreaCode")+")";
				}
				if(null!=phone){
					telFormat=telFormat+dataMap.get("Phone");
				}else{
					telFormat = "no data";
				}
				telTxt.setText(telFormat);
				
				//执行打印
				if(null!=rtnResult.get("labelUrl")){
					final String  lableUrl = (String) rtnResult.get("labelUrl");
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							doPrint(lableUrl);
						}
					}).start();
				}else{
					setErrTip("Label data no found");
				}
				
			}else{
				setErrTip((String) rtnResult.get("msg"));
				trackNumTxt.setText( rtnResult.get("TrackNum")+"");
			}
		}else if(rtnResult.isEmpty()){
			setErrTip("Shipment data no found");
			
		}else{
			setErrTip("Connection failed. Please check network connecting");
		}
	}
	
	public void doPrint(String labelUrl){
		System.out.println("begin print...");
		try {
			PrintUtils.printRemoteFileByAppointPrinter(labelUrl, userSelectedService);
		} catch (Exception e) {
			e.printStackTrace();
			setErrTip("Printer error. Please check printer");
		}
	}
}
