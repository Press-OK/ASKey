package ask.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import ask.interfaces.RenameableControl;
import ask.main.AskeyEditor;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

public class WindowRenameSelection {

	private AskeyEditor editor;
	protected Shell shlRename;
	private RenameableControl c;
	private Text txtNewName;
	private String oldName;

	public WindowRenameSelection(AskeyEditor editor, RenameableControl c) {
		this.editor = editor;
		this.c = c;
		this.oldName = c.getName();
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		shlRename = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		shlRename.setBackground(SWTResourceManager.getColor(80, 80, 80));
		shlRename.setSize(177, 126);
		shlRename.setText("Rename " + c.getName());
		shlRename.setLayout(null);
		shlRename.setLocation(display.getClientArea().width/2 - shlRename.getBounds().width/2, display.getClientArea().height/2 - shlRename.getBounds().height/2);
		
		Label lblRename = new Label(shlRename, SWT.NONE);
		lblRename.setBounds(22, 27, 43, 13);
		lblRename.setForeground(SWTResourceManager.getColor(255, 255, 255));
		lblRename.setBackground(SWTResourceManager.getColor(80, 80, 80));
		lblRename.setText("Rename:");
		
		txtNewName = new Text(shlRename, SWT.BORDER);
		txtNewName.setBounds(70, 24, 77, 19);
		txtNewName.setForeground(SWTResourceManager.getColor(255, 255, 255));
		txtNewName.setBackground(SWTResourceManager.getColor(65, 65, 65));
		txtNewName.setText(c.getName());
		txtNewName.setTextLimit(30);
		txtNewName.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.keyCode == SWT.CR){
					apply();
				}
			}
		});
		
		Button btnOk = new Button(shlRename, SWT.FLAT);
		btnOk.setBounds(22, 68, 68, 23);
		btnOk.setText("OK");
		
		Button btnCancel = new Button(shlRename, SWT.FLAT);
		btnCancel.setBounds(96, 68, 51, 23);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editor.setStatusText("Cancelled renaming of " + oldName);
				shlRename.close();
			}
		});

		btnOk.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				apply();
			}
		});

		txtNewName.setFocus();
		txtNewName.selectAll();
		shlRename.open();
		shlRename.layout();
		while (!shlRename.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private void apply() {
		String errorTitle = null;
		String errorMsg = null;
		if (txtNewName.getText().equals("")) {
			errorTitle = "Invalid Name";
			errorMsg = "You must provide a new name.";
			txtNewName.setFocus();
			txtNewName.selectAll();
		}
		if (txtNewName.getText().matches("^.*[^a-zA-Z0-9 ].*$")) {
			errorTitle = "Invalid Name";
			errorMsg = "New name can only contain A-Z, 0-9, and spaces.";
		}
		if (txtNewName.getText().length() > 30) {
			errorTitle = "Invalid Name";
			errorMsg = "New name must be between 1-30 characters.";
		}

		if (errorMsg != null) {
			MessageBox msgBox = new MessageBox(shlRename, SWT.ICON_ERROR);
			msgBox.setText(errorTitle);
			msgBox.setMessage(errorMsg);
			msgBox.open();
		} else {
			editor.setStatusText("Renamed " + oldName + " -> " + txtNewName.getText());
			c.setName(txtNewName.getText());
			shlRename.close();
		}
	}
}
