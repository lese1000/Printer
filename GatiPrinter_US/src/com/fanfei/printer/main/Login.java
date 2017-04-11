package com.fanfei.printer.main;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Text;

import com.fanfei.printer.utils.ApiUtils;
import com.fanfei.printer.utils.AppConfigurator;
import com.fanfei.printer.utils.AppConfiguratorText;
import com.fanfei.printer.utils.AppConfiguratorText.TextName;
import com.fanfei.printer.utils.AudioUtils;
import com.fanfei.printer.utils.AudioUtils.TipType;

import org.eclipse.wb.swt.SWTResourceManager;

public class Login {
	protected Shell shell;
	private Display display;
	private static Text loginAccountTxt;
	private static Text loginPasswordTxt;
	private MessageBox tipMessageBox;
	private LoginKeyDownListener listener ;
	private Label tipLabel ;
	private Button loginBtn  ;
	private Button updateButton ;
	private boolean printerUseabl = true;
	
	

	/**
	 * Launch the application.
	 * @param args
	 */
	
	public static void main(String[] args) {
		try {
//			Toolkit.getDefaultToolkit().beep(); 
			Login window = new Login();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		listener=new LoginKeyDownListener();
		display.addFilter(SWT.KeyDown, listener);
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("GATI - Login");
		
		tipMessageBox= new MessageBox(shell);
		
		Group group = new Group(shell, SWT.NONE);
		group.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		group.setBounds(51, 41, 330, 189);
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(33, 32, 61, 17);
		label.setText("Account：");
		
		loginAccountTxt = new Text(group, SWT.BORDER);
		loginAccountTxt.setBounds(114, 29, 169, 23);
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setBounds(33, 84, 61, 17);
		label_1.setText("Password：");
		
		loginPasswordTxt = new Text(group, SWT.BORDER | SWT.PASSWORD);
		loginPasswordTxt.setBounds(114, 81, 169, 23);
		
		loginBtn = new Button(group, SWT.NONE);
		loginBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doLogin();
			}
		});
		loginBtn.setBounds(114, 133, 80, 27);
		loginBtn.setText("Submit");
		
		updateButton = new Button(group, SWT.NONE);
		updateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//
				doUpdate();
			}
		});
		updateButton.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		updateButton.setBounds(203, 133, 80, 27);
		updateButton.setText("Update");
		updateButton.setVisible(false);
		
		tipLabel = new Label(shell, SWT.NONE);
		tipLabel.setAlignment(SWT.CENTER);
		tipLabel.setBounds(51, 18, 330, 17);
		tipLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		
		
		//checkVersion
		checkVersion();

		
	}
	
	public  void doLogin(){
		String account = loginAccountTxt.getText();
		String password = loginPasswordTxt.getText();
		if(null==account||"".equals(account)){
			AudioUtils.play(TipType.ERROR);
			tipLabel.setText("Please enter user account！");
			loginAccountTxt.setFocus();
			return ;
		}
		if(null==password||"".equals(password)){
			AudioUtils.play(TipType.ERROR);
			tipLabel.setText("Please enter password！");
			loginPasswordTxt.setFocus();
			return ;
		}
		
		Map<String, Object> rtnMap = ApiUtils.userLogin(account, password);
		if(null!=rtnMap&&!rtnMap.isEmpty()){
			if(rtnMap.get("code").equals("0000")){
				tipLabel.setText("");
				Printer printer = new Printer();
				Map<String,Object> printInitData = new HashMap<String, Object>();
//				printInitData.put("title", "运单打印（账号:"+account+"）");
				printInitData.put("loginShell", shell);
				printInitData.put("loginDisplay", display);
				printInitData.put("loginListener", listener);
				printer.open(printInitData);
				
			}else{
				AudioUtils.play(TipType.ERROR);
				tipLabel.setText(String.valueOf(rtnMap.get("msg")));
			}
			
			
		}else{
			AudioUtils.play(TipType.ERROR);
			tipLabel.setText("Connection failed. Please check network connecting！");
		}
	}
	
	public class LoginKeyDownListener implements Listener{

		@Override
		public void handleEvent(Event event) {
			if(event.keyCode==13||event.keyCode==16777296){
				if(printerUseabl){
					doLogin();
				}
				
			}
		}
		
	}
	public void setErrTip(String msg){
		tipLabel.setText(msg);
		AudioUtils.play(TipType.ERROR);
	}
	
	private void checkVersion(){
		try {
			Map<String, Object> dataMap = ApiUtils.getVersion();
			if(null!=dataMap&&null!=dataMap.get("obj")){
				Map<String, Object> versionMap = (Map<String, Object>) dataMap.get("obj");
				String versionNum = versionMap.get("VERSION_NUM")+"";
				String updateUrl = (String) versionMap.get("UPDATE_URL");
				String currentVersionNum = AppConfiguratorText.readFile(TextName.VERSION_CONFIG).split(":")[1];
				if(!versionNum.equals(currentVersionNum)){
					setErrTip("Current version is too old,please click update button");
					printerUseabl = false;
					showUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void showUpdate(){
		loginBtn.setEnabled(false);
		updateButton.setVisible(true);
	}
	
	private void doUpdate(){
		try {
			Runtime.getRuntime().exec("java -jar updater.jar");
		} catch (Exception e) {
			e.printStackTrace();
		}
		shell.close();
	}
}

