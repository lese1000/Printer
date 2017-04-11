package com.fanfei.printer;

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
import org.eclipse.wb.swt.SWTResourceManager;

import com.fanfei.printer.utils.ApiUtils;
import com.fanfei.printer.utils.AudioUtils;
import com.fanfei.printer.utils.AudioUtils.TipType;

public class Logins {
	protected Shell shell;
	private Display display;
	private static Text loginAccountTxt;
	private static Text loginPasswordTxt;
	private MessageBox tipMessageBox;
	private LoginKeyDownListener listener ;
	private Label tipLabel ;
	

	/**
	 * Launch the application.
	 * @param args
	 */
	
	public static void main(String[] args) {
		try {
//			Toolkit.getDefaultToolkit().beep(); 
			Logins window = new Logins();
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
		shell.setText("GATI - 用户登录");
		
		tipMessageBox= new MessageBox(shell);
		
		Group group = new Group(shell, SWT.NONE);
		group.setBounds(51, 41, 330, 189);
		
		Label label = new Label(group, SWT.NONE);
		label.setBounds(33, 32, 61, 17);
		label.setText("登录账号：");
		
		loginAccountTxt = new Text(group, SWT.BORDER);
		loginAccountTxt.setBounds(114, 29, 169, 23);
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setBounds(33, 84, 61, 17);
		label_1.setText("登录密码：");
		
		loginPasswordTxt = new Text(group, SWT.BORDER | SWT.PASSWORD);
		loginPasswordTxt.setBounds(114, 81, 169, 23);
		
		Button loginBtn = new Button(group, SWT.NONE);
		loginBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doLogin();
			}
		});
		loginBtn.setBounds(114, 133, 80, 27);
		loginBtn.setText("登录");
		
		tipLabel = new Label(group, SWT.NONE);
		tipLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		tipLabel.setBounds(114, 58, 206, 17);
		

		
	}
	
	public  void doLogin(){
		String account = loginAccountTxt.getText();
		String password = loginPasswordTxt.getText();
		if(null==account||"".equals(account)){
//			tipMessageBox.setMessage("");
//			tipMessageBox.open();
			AudioUtils.play(TipType.ERROR);
			tipLabel.setText("登录账号不能为空！");
			loginAccountTxt.setFocus();
			return ;
		}
		if(null==password||"".equals(password)){
//			tipMessageBox.setMessage("登录密码不能为空！");
//			tipMessageBox.open();
			AudioUtils.play(TipType.ERROR);
			tipLabel.setText("登录密码不能为空！");
			loginPasswordTxt.setFocus();
			return ;
		}
		
		Map<String, Object> rtnMap = ApiUtils.userLogin(account, password);
		if(null!=rtnMap&&!rtnMap.isEmpty()){
			if(rtnMap.get("code").equals("0000")){
				if(null!=rtnMap.get("obj")){
					Map<String,Object> objMap = (Map<String, Object>) rtnMap.get("obj");
					String roleId = (String) objMap.get("ROLE_ID");
					if(roleId.equals("1")||roleId.equals("6613784d0eb2420cb859876414d38193")){
//						tipMessageBox.setMessage("登录成功！");
//						tipMessageBox.open();
						tipLabel.setText("");
						Printer printer = new Printer();
						Map<String,Object> printInitData = new HashMap<String, Object>();
						printInitData.put("title", "运单打印（账号:"+account+"）");
						printInitData.put("loginShell", shell);
						printInitData.put("loginDisplay", display);
						printInitData.put("loginListener", listener);
						printer.open(printInitData);
						
					}else{
//						tipMessageBox.setMessage("该账号无权限操作！");
//						tipMessageBox.open();
						AudioUtils.play(TipType.ERROR);
						tipLabel.setText("该账号无权限操作！");
					}
				}
				
				
			}else{
//				tipMessageBox.setMessage(String.valueOf(rtnMap.get("msg")));
//				tipMessageBox.open();
				AudioUtils.play(TipType.ERROR);
				tipLabel.setText(String.valueOf(rtnMap.get("msg")));
			}
			
			
		}else{
//			tipMessageBox.setMessage("数据异常，请检查网络是否通畅！");
//			tipMessageBox.open();
			AudioUtils.play(TipType.ERROR);
			tipLabel.setText("数据异常，请检查网络是否通畅！");
		}
	}
	
	public class LoginKeyDownListener implements Listener{

		@Override
		public void handleEvent(Event event) {
			/*if (SWTKeySupport.convertEventToUnmodifiedAccelerator(event) == (SWT.CTRL + 'M')) {
            	System.out.println("Ctrl-M");
        	}*/
			if(event.keyCode==13||event.keyCode==16777296){
				doLogin();
			}
		}
		
	}
}

