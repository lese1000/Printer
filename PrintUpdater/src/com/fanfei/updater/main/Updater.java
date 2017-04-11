package com.fanfei.updater.main;

import java.util.Map;

import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.fanfei.printer.Logins;
import com.fanfei.updater.thread.DownloaderThread;
import com.fanfei.updater.utils.ApiUtils;
import com.fanfei.updater.utils.AppConfiguratorText;
import com.fanfei.updater.utils.AppConfiguratorText.TextName;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class Updater extends ApplicationWindow {
	
	private static Updater window = new Updater();
	private Label tipLabel;
	private ProgressBar progressBar ;
	private Button btnStart;

	/**
	 * Create the application window.
	 */
	public Updater() {
		super(null);
		createActions();
//		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		progressBar = new ProgressBar(container, SWT.NONE);
		progressBar.setBounds(82, 119, 266, 17);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		
		tipLabel = new Label(container, SWT.NONE);
		tipLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		tipLabel.setAlignment(SWT.CENTER);
		tipLabel.setText("Checking for updates ...");
		tipLabel.setBounds(80, 71, 268, 17);
		
		btnStart = new Button(container, SWT.NONE);
		btnStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadPrinter();
			}
		});
		btnStart.setBounds(165, 165, 80, 27);
		btnStart.setText("Start-up");
		btnStart.setVisible(false);
		checkVersion();
		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		
	}



	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("GATI-Updater");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	public ProgressBar createProgressBar(Composite container){
		ProgressBar progressBar = new ProgressBar(container, SWT.NONE);
		progressBar.setBounds(151, 133, 170, 17);
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		return progressBar;
	}
	
	public void loadPrinter(){
		try {
			Runtime.getRuntime().exec("java -jar printer.jar");
		} catch (Exception e) {
			e.printStackTrace();
		}
		window.close();
	}
	public void loadPrinter2(){
		Logins login = new Logins();
		login.open();
		window.close();
	}
	
	private void checkVersion(){
		try {
			Map<String, Object> dataMap = ApiUtils.getVersion();
			if(null!=dataMap&&null!=dataMap.get("obj")){
				Map<String, Object> versionMap = (Map<String, Object>) dataMap.get("obj");
				String newVersionNum = versionMap.get("VERSION_NUM")+"";
				String updateUrl = (String) versionMap.get("UPDATE_URL");
				String currentVersionStr = AppConfiguratorText.readFile(TextName.VERSION_CONFIG);
				String currentVersionNum = currentVersionStr.split(":")[1];
				System.out.println("currentVersion:"+currentVersionNum);
				if(!newVersionNum.equals(currentVersionNum)){
					AppConfiguratorText.writeFile(TextName.VERSION_CONFIG, "version:"+newVersionNum);
					downloadNewVersion(updateUrl);
				}else{
					tipLabel.setText("This is the latest version, no need to update");
					btnStart.setVisible(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void downloadNewVersion(String fileUrl){
//		new DownloaderThread(Display.getCurrent(), progressBar, fileUrl, tipLabel).start();
		Display.getCurrent().asyncExec(new DownloaderThread(Display.getCurrent(), progressBar, fileUrl, tipLabel,btnStart));
	}
	
	
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			window.setBlockOnOpen(true);
			window.open();
//			Display.getCurrent().dispose();
//			window.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
