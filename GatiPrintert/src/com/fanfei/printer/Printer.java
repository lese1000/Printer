package com.fanfei.printer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.fanfei.printer.utils.ApiUtils;
import com.fanfei.printer.utils.AudioUtils;
import com.fanfei.printer.utils.AudioUtils.TipType;
import com.fanfei.printer.utils.PrintUtils;

public class Printer {

	protected Shell shell;
	private Text searchTxt;
	private Text serviceCodeTxt;
	private Text entryPortTxt;
	private Text weigthTxt;
	private Text packagesNumTxt;
	private Table table;
	private Text trackNumTxt;
	private Text customerAccountTxt;
	private Text customerCompanyTxt;
	private boolean isAutoPrint = true;
	private MessageBox tipMessageBox;
	private Text countryCodeTxt;
	private Text consigmentStatusTxt;
	private Text lengthTxt;
	private Text widthTxt;
	private Text heightTxt;
	private Text totalVolumeTxt;
	private Group group ;
	private Group totalVolumeGroup;
	private Group chiCunGroup;
	private String labelUrl;
	private PrintListener printListener;
	private Button printLabelBtn ;
	private Label tipLabel;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			Toolkit.getDefaultToolkit().beep(); 
			Printer window = new Printer();
			Map<String, Object> map= new HashMap<String, Object>();
			map.put("title", "运单打印");
			window.open(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open(Map<String,Object> initData) {
		printListener = new PrintListener();
		Display display = Display.getDefault();
		display.addFilter(SWT.KeyDown, printListener);
		createContents(initData);
		shell.open();
		shell.layout();
		//**去除login界面的事件过滤器和关闭login界面
		if(null!=initData.get("loginShell")){
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
	protected void createContents(Map<String,Object> initData) {
		shell = new Shell();
		shell.setSize(549, 600);
		shell.setText(String.valueOf(initData.get("title")));
		
		tipMessageBox = new MessageBox(shell);
		tipMessageBox.setText("提示信息");
		
		
		searchTxt = new Text(shell, SWT.BORDER);
		searchTxt.setBounds(105, 38, 298, 23);
		searchTxt.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				/*String searchVal = searchTxt.getText();
				System.out.println("searchVal:"+searchVal);*/
				
			}
		});
		
		//查询输入框回车事件监听
		/*searchTxt.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent event) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent event) {
//				System.out.println(event.keyCode);
				if( event.keyCode==16777296||event.keyCode==13){
					doSearchAction();
				}
				
			}
		});*/
		
		printLabelBtn = new Button(shell, SWT.CHECK);
		printLabelBtn.setSelection(true);
		printLabelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(isAutoPrint){
					isAutoPrint=false;
				}else{
					isAutoPrint=true;
				}
				
//				System.out.println("isAutoPrint:"+isAutoPrint);
			}
		});
		printLabelBtn.setBounds(409, 41, 105, 17);
		printLabelBtn.setText("打印Label(F4)");
		
		Label line1Label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		line1Label.setBounds(0, 64, 534, 10);
		//====================================================group statr ============================================
		group = new Group(shell, SWT.NONE);
		group.setBounds(30, 73, 484, 358);
		
		Label serviceCodeLabel = new Label(group, SWT.RIGHT);
		serviceCodeLabel.setAlignment(SWT.RIGHT);
		serviceCodeLabel.setBounds(10, 48, 61, 17);
		serviceCodeLabel.setText("服务代码：");
		
		serviceCodeTxt = new Text(group, SWT.BORDER);
		serviceCodeTxt.setEnabled(false);
		serviceCodeTxt.setEditable(false);
		serviceCodeTxt.setBounds(77, 45, 160, 23);
		
		Label entryPortLabel = new Label(group, SWT.NONE);
		entryPortLabel.setAlignment(SWT.RIGHT);
		entryPortLabel.setBounds(267, 48, 36, 17);
		entryPortLabel.setText("线路：");
		
		entryPortTxt = new Text(group, SWT.BORDER);
		entryPortTxt.setEnabled(false);
		entryPortTxt.setEditable(false);
		entryPortTxt.setBounds(309, 45, 160, 23);
		
		Label wightLabel = new Label(group, SWT.NONE);
		wightLabel.setAlignment(SWT.RIGHT);
		wightLabel.setBounds(10, 127, 61, 17);
		wightLabel.setText("客户重量：");
		
		weigthTxt = new Text(group, SWT.BORDER);
		weigthTxt.setEnabled(false);
		weigthTxt.setEditable(false);
		weigthTxt.setBounds(77, 122, 118, 23);
		
		Label lblkg = new Label(group, SWT.NONE);
		lblkg.setBounds(201, 125, 36, 17);
		lblkg.setText("（KG）");
		
		packagesNumTxt = new Text(group, SWT.BORDER);
		packagesNumTxt.setEnabled(false);
		packagesNumTxt.setEditable(false);
		packagesNumTxt.setBounds(309, 122, 109, 23);
		
		Label packagesNumLabel = new Label(group, SWT.NONE);
		packagesNumLabel.setAlignment(SWT.RIGHT);
		packagesNumLabel.setBounds(267, 127, 36, 17);
		packagesNumLabel.setText("件数：");
		
		Label lblNewLabel_3 = new Label(group, SWT.NONE);
		lblNewLabel_3.setBounds(423, 125, 36, 17);
		lblNewLabel_3.setText("（个）");
		
		Label trackNumLabel = new Label(group, SWT.RIGHT);
		trackNumLabel.setAlignment(SWT.RIGHT);
		trackNumLabel.setBounds(17, 20, 54, 19);
		trackNumLabel.setText("运单号：");
		
		trackNumTxt = new Text(group, SWT.BORDER);
		trackNumTxt.setEnabled(false);
		trackNumTxt.setEditable(false);
		trackNumTxt.setBounds(77, 15, 392, 23);
		
		Label label_1 = new Label(group, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setBounds(0, 74, 481, 8);
		
		countryCodeTxt = new Text(group, SWT.BORDER);
		countryCodeTxt.setEnabled(false);
		countryCodeTxt.setEditable(false);
		countryCodeTxt.setBounds(77, 93, 160, 23);
		
		Label label_2 = new Label(group, SWT.RIGHT);
		label_2.setText("目的地：");
		label_2.setAlignment(SWT.RIGHT);
		label_2.setBounds(17, 99, 54, 17);
		
		Label label_3 = new Label(group, SWT.NONE);
		label_3.setText("状态：");
		label_3.setAlignment(SWT.RIGHT);
		label_3.setBounds(269, 99, 36, 17);
		
		consigmentStatusTxt = new Text(group, SWT.BORDER);
		consigmentStatusTxt.setEnabled(false);
		consigmentStatusTxt.setEditable(false);
		consigmentStatusTxt.setBounds(309, 93, 109, 23);
		
		Button statusDetailBtn = new Button(group, SWT.NONE);
		statusDetailBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		statusDetailBtn.setText("详情");
		statusDetailBtn.setBounds(423, 91, 46, 27);
		statusDetailBtn.setVisible(false);
		//=============================================totalVolumeGroup start===================================
		totalVolumeGroup = new Group(group, SWT.NONE);
		totalVolumeGroup.setBounds(10, 150, 459, 51);
		totalVolumeGroup.setVisible(true);
		
		
		Label label_4 = new Label(totalVolumeGroup, SWT.RIGHT);
		label_4.setText("总体积：");
		label_4.setBounds(5, 20, 54, 17);
		
		totalVolumeTxt = new Text(totalVolumeGroup, SWT.BORDER);
		totalVolumeTxt.setEnabled(false);
		totalVolumeTxt.setBounds(68, 17, 118, 23);
		
		Label label_6 = new Label(totalVolumeGroup, SWT.NONE);
		label_6.setText("（M³）");
		label_6.setBounds(194, 19, 36, 17);
		
		Button showChiCunListBtn = new Button(totalVolumeGroup, SWT.NONE);
		showChiCunListBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		showChiCunListBtn.setText("查看尺寸列表");
		showChiCunListBtn.setBounds(300, 14, 109, 27);
		showChiCunListBtn.setVisible(false);
		//===============================================totalVolumeGroup end=====================================
		//===============================================chiCunGroup start========================================
		chiCunGroup = new Group(group, SWT.NONE);
		chiCunGroup.setBounds(10, 150, 459, 51);
		chiCunGroup.setVisible(false);
		
		Label label_5 = new Label(chiCunGroup, SWT.RIGHT);
		label_5.setBounds(8, 20, 51, 17);
		label_5.setText("长(cm)：");
		
		lengthTxt = new Text(chiCunGroup, SWT.BORDER);
		lengthTxt.setBounds(68, 17, 80, 23);
		lengthTxt.setEnabled(false);
		
		Label lblcm = new Label(chiCunGroup, SWT.NONE);
		lblcm.setBounds(173, 21, 46, 17);
		lblcm.setText("宽(cm)：");
		
		widthTxt = new Text(chiCunGroup, SWT.BORDER);
		widthTxt.setBounds(223, 17, 80, 23);
		widthTxt.setEnabled(false);
		
		Label lblNewLabel = new Label(chiCunGroup, SWT.RIGHT);
		lblNewLabel.setBounds(309, 21, 61, 17);
		lblNewLabel.setText("高(cm)：");
		
		heightTxt = new Text(chiCunGroup, SWT.BORDER);
		heightTxt.setBounds(376, 18, 73, 23);
		heightTxt.setEnabled(false);
		
		//=============================================chiCunGroup end===========================
		
		// step1: create a Table object
		table = new Table(group, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(20, 213, 449, 135);
		table.setToolTipText("包裹信息");
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		 // step2: add columns 
		// 创建表头的字符串数组  
        String[] tableHeader = {"品名", "属性"};  
        for (int i = 0; i < tableHeader.length; i++)  
        {  
            TableColumn tableColumn = new TableColumn(table, SWT.None); 
            tableColumn.setWidth(222);
//            tableColumn.setResizable(false);//设置列宽不能改变
            tableColumn.setText(tableHeader[i]);  
            // 设置表头可移动，默认为false  
//            tableColumn.setMoveable(true); 
//            table.getColumn(i).pack();//影响列宽的设定
        }  
        
      
		//===打印按钮====
		Button printBtn = new Button(shell, SWT.NONE);
		printBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*String searchVal = searchTxt.getText();
				if(null!=searchVal&&!"".equals(searchVal)){
					showDataUseTrackNum(searchVal);
				}else{
					//提示信息
					tipMessageBox.setMessage("查询信息不能为空!");
					tipMessageBox.open();
				}*/
				manualPrint();
				
			}
		});
		printBtn.setBounds(218, 521, 80, 27);
		printBtn.setText("打印(F1)");
		
		Label customerAccountLabel = new Label(shell, SWT.NONE);
		customerAccountLabel.setAlignment(SWT.RIGHT);
		customerAccountLabel.setBounds(47, 459, 61, 17);
		customerAccountLabel.setText("客户账号：");
		
		customerAccountTxt = new Text(shell, SWT.BORDER);
		customerAccountTxt.setEnabled(false);
		customerAccountTxt.setBounds(120, 456, 379, 23);
		
		Label customerCompanyLabel = new Label(shell, SWT.NONE);
		customerCompanyLabel.setBounds(47, 495, 61, 17);
		customerCompanyLabel.setText("客户名称：");
		
		customerCompanyTxt = new Text(shell, SWT.BORDER);
		customerCompanyTxt.setEnabled(false);
		customerCompanyTxt.setBounds(120, 492, 379, 23);
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(19, 41, 80, 17);
		label.setText("运单/流水号：");
		
		
		Label line2Label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		line2Label.setBounds(0, 430, 534, 23);
		
		tipLabel = new Label(shell, SWT.NONE);
		tipLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		tipLabel.setBounds(105, 10, 379, 17);

	}
	
	//执行搜索动作
	public void doSearchAction(){
		String searchVal = searchTxt.getText();
		if(searchVal.trim().length()!=0&&!searchVal.trim().equals("")){
			//13位流水号
			if(searchVal.trim().length()==13){
				showDataUseSequenceNum(searchVal);
			}else{
				showDataUseTrackNum(searchVal);
				/*//提示信息
				tipMessageBox.setMessage("运单/流水号格式不正确!");
				tipMessageBox.open();*/
			}
			
			/*else if(searchVal.trim().length()==9){
				showDataUseTrackNum(searchVal);
			}*/
		}else{
			//提示信息
//			tipMessageBox.setMessage("查询信息不能为空!");
//			tipMessageBox.open();
			AudioUtils.play(TipType.ERROR);
			tipLabel.setText("查询信息不能为空!");
		}
	}
	
	//手动执行打印
	public void manualPrint(){
		if(null!=labelUrl){
			try {
				PrintUtils.printRemoteFile(labelUrl);
//				System.out.println("开始打印...");
			} catch (Exception exception) {
				exception.printStackTrace();
//				tipMessageBox.setMessage("获取打印机信息异常，请检查打印机是否设置正确！");
//				tipMessageBox.open();
				AudioUtils.play(TipType.ERROR);
				tipLabel.setText("获取打印机信息异常,请点击打印重试");
			}
		}else{
			//弹出无label提示信息
//			tipMessageBox.setMessage("未获取到Label信息！");
//			tipMessageBox.open();
			AudioUtils.play(TipType.ERROR);
			tipLabel.setText("未获取到Label信息！");
		}
		
	}
	
	
	public void showDataUseTrackNum(String trackNum){
		Map<String, Object> rtnData = ApiUtils.getDataByTrackNum(trackNum);
		showData(rtnData);
	}
	
	public void showDataUseSequenceNum(String SequenceNum){
		Map<String, Object> rtnData = ApiUtils.getDataBySequenceNum(SequenceNum);
		showData(rtnData);
	}
	
	public void showData(Map<String,Object> data){
		//清理旧数据
		clearOldData();
		
		if(null==data||data.isEmpty()){
			//弹出错误提示，检查网络是否有问题
//			tipMessageBox.setMessage("请求异常，请检查网络是否通畅！");
//			tipMessageBox.open();
			AudioUtils.play(TipType.ERROR);
			tipLabel.setText("请求异常，请检查网络是否通畅！");
			return;
		}
		
		
		if(data.get("code").equals("0000")){
//			Toolkit.getDefaultToolkit().beep(); //获取数据成功后的提示音
			AudioUtils.play(TipType.SUCCESS);
			
			Map<String,Object> rtnMap = (Map<String, Object>) data.get("obj");
			if(null!=rtnMap.get("InnerServerCode")){
				serviceCodeTxt.setText((String) rtnMap.get("InnerServerCode"));
			}else{
				serviceCodeTxt.setText("无信息");
			}
			
			if(null!=rtnMap.get("TrackNum")){
				trackNumTxt.setText(String.valueOf( rtnMap.get("TrackNum")));
			}else{
				trackNumTxt.setText("无信息");
			}
			
			if(null!=rtnMap.get("EntryPort")){
				entryPortTxt.setText(String.valueOf( rtnMap.get("EntryPort")));
			}else{
				entryPortTxt.setText("无信息");
			}
			if(null!=rtnMap.get("Weight")){
				weigthTxt.setText(String.valueOf(rtnMap.get("Weight")));
			}else{
				weigthTxt.setText("无信息");
			}
			if(null!=rtnMap.get("PackagesNum")){
				int num = (int) rtnMap.get("PackagesNum");
				if(num==1){
					if(null!=rtnMap.get("Length")){
						lengthTxt.setText(String.valueOf(rtnMap.get("Length")));
					}else{
						lengthTxt.setText("无信息");
					}
					if(null!=rtnMap.get("Width")){
						widthTxt.setText(String.valueOf(rtnMap.get("Width")));
					}else{
						widthTxt.setText("无信息");
					}
					if(null!=rtnMap.get("Height")){
						heightTxt.setText(String.valueOf(rtnMap.get("Height")));
					}else{
						heightTxt.setText("无信息");
					}
					
					totalVolumeGroup.setVisible(false);
					chiCunGroup.setVisible(true);
				}else{
					if(null!=rtnMap.get("TotalVolume")){
						totalVolumeTxt.setText(String.valueOf(rtnMap.get("Length")));
					}else{
						totalVolumeTxt.setText("无信息");
					}
					totalVolumeGroup.setVisible(true);
					chiCunGroup.setVisible(false);
				}
				packagesNumTxt.setText(String.valueOf(rtnMap.get("PackagesNum")));
			}else{
				packagesNumTxt.setText("无信息");
			}
			if(null!=rtnMap.get("CountryCode")){
				countryCodeTxt.setText((String) rtnMap.get("CountryCode"));
			}else{
				countryCodeTxt.setText("无信息");
			}
			
			if(null!=rtnMap.get("ConsigmentStatusName")){
				consigmentStatusTxt.setText(String.valueOf( rtnMap.get("ConsigmentStatusName")));
				if(String.valueOf( rtnMap.get("ConsigmentStatusValue")).equals("12")){
					AudioUtils.play(TipType.ERROR);
//					tipMessageBox.setMessage("运单状态数据异常！");
//					tipMessageBox.open();
					tipLabel.setText("运单状态数据异常！");
					 
				}else if(String.valueOf( rtnMap.get("ConsigmentStatusValue")).equals("11")){
					AudioUtils.play(TipType.ERROR);
//					tipMessageBox.setMessage("运单状态异常！");
//					tipMessageBox.open();
					tipLabel.setText("运单状态异常！");
					
				}else{
					
				}
			}else{
				consigmentStatusTxt.setText("无信息");
			}
			if(null!=rtnMap.get("CustomerAccount")){
				customerAccountTxt.setText(String.valueOf( rtnMap.get("CustomerAccount")));
			}else{
				customerAccountTxt.setText("无信息");
			}
			if(null!=rtnMap.get("CustomerCompany")){
				customerCompanyTxt.setText((String) rtnMap.get("CustomerCompany"));
			}else{
				customerCompanyTxt.setText("无信息");
			}
			if(null!=rtnMap.get("ArticleList")){
				setTableItem((List<Map>) rtnMap.get("ArticleList"));
			}
			if(null!=rtnMap.get("ArticleList")){
				labelUrl = (String) rtnMap.get("LabelUrl");
			}else{
				labelUrl=null;
			}

			
			//判断是否自动打印label
			if(isAutoPrint){
				if(null!=labelUrl){
					try {
						PrintUtils.printRemoteFile(labelUrl);
//						System.out.println("开始打印...");
					} catch (Exception e) {
						e.printStackTrace();
//						tipMessageBox.setMessage("获取打印机信息异常，请检查打印机是否设置正确！");
//						tipMessageBox.open();
						AudioUtils.play(TipType.ERROR);
						tipLabel.setText("获取打印机信息异常，请点击打印重试");
					}
				}else{
					//弹出无label提示信息
//					tipMessageBox.setMessage("未获取到Label信息！");
//					tipMessageBox.open();
					AudioUtils.play(TipType.ERROR);
					tipLabel.setText("未获取到Label信息!");
					
				}
			}
			
		}else{
			//弹出提示错误
//			String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//			tipMessageBox.setMessage(String.valueOf((data.get("msg"))));
//			tipMessageBox.open();
			AudioUtils.play(TipType.ERROR);
			tipLabel.setText(String.valueOf((data.get("msg"))));
			
		}
	}
	
	  // step3: add rows  
	public void setTableItem(List<Map> data){
		if(null!=data){
			TableItem tableItem = null;
			for(Map<String,String> dataItem: data){
				tableItem = new TableItem(this.table, SWT.NONE); 
				tableItem.setText(0, dataItem.get("CNNAME"));
				tableItem.setText(1, dataItem.get("CLASS_NAME"));
			}
		}
	}
	
	/**
	 * 查询成功后，清除之前的数据
	 */
	public void clearOldData(){
		
		searchTxt.setText("");
		serviceCodeTxt.setText("");
		entryPortTxt.setText("");
		weigthTxt.setText("");
		packagesNumTxt.setText("");
		trackNumTxt.setText("");
		customerAccountTxt.setText("");
		customerCompanyTxt.setText("");
		countryCodeTxt.setText("");
		consigmentStatusTxt.setText("");
		lengthTxt.setText("");
		widthTxt.setText("");
		heightTxt.setText("");
		totalVolumeTxt.setText("");
		tipLabel.setText("");
		
		
		this.table.removeAll();
	}
	
	/**
	 * 事件监听器类
	 * @author LENOVO
	 *
	 */
	public  class PrintListener implements Listener{
		/**
		 *  F1:16777226
			F2:16777227
			F3:16777228
			F4:16777229
			F5:16777230
			F6:16777231
			F7:16777232
			F8:16777233
			F9:16777234
			F10:16777235
			F11:16777236
			F12:16777237
		 */
		@Override
		public void handleEvent(Event event) {
			//确定按钮
			if(event.keyCode==13||event.keyCode==16777296){
				doSearchAction();
			}
//			System.out.println("event.keyCode::"+event.keyCode);
			//F1，执行底部打印按钮事件
			if(event.keyCode==16777226){
				manualPrint();
			}
			//F4,头部复选框事件
			if(event.keyCode==16777229){
				if(isAutoPrint){
					isAutoPrint=false;
					printLabelBtn.setSelection(false);
				}else{
					isAutoPrint=true;
					printLabelBtn.setSelection(true);
				}
			}
			
		}
		
	}
}
