package ask.swt.layout;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.layout.GridLayout;

import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import ask.dialog.WindowNewFile;
import ask.main.AskeyEditor;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.custom.CLabel;

public class SWT_TitleBar extends Composite {
	
	private AskeyEditor editor;
	private Display display;
	private Shell shell;

	private Point editorMoveOriginalPosition;
	private Point editorMovePivot;
	private Point editorMoveTotal;
	
	private Label lblHeaderText;
	private HashSet<CLabel> menuBarButtons = new HashSet<>();


	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SWT_TitleBar(Shell shell, int style, AskeyEditor editor) {
		super (shell, style);
		this.shell = shell;
		this.display = shell.getDisplay();
		this.editor = editor;
		
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		this.setLayout(gl_composite);
		this.setLayoutData(gd_composite);
		
		addTitleBar();
		addMenuBar();
		addDocBar();
	}

	private void addDocBar() {
		Composite cmpDocBar = new Composite(this, SWT.NONE);
		cmpDocBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmpDocBar.setBackground(SWTResourceManager.getColor(60, 60, 60));
		RowLayout rl_DocBar = new RowLayout(SWT.HORIZONTAL);
		rl_DocBar.wrap = false;
		rl_DocBar.marginTop = 10;
		rl_DocBar.marginLeft = 10;
		rl_DocBar.marginBottom = 10;
		cmpDocBar.setLayout(rl_DocBar);
		
		Button btnNewFile = new Button(cmpDocBar, SWT.NONE);
		btnNewFile.setLayoutData(new RowData(55, 55));
		btnNewFile.setText("foo");
		btnNewFile.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editor.setStatusText("Note: All of the new file's settings can be changed later");
				WindowNewFile newFileDialog = new WindowNewFile(editor);
				newFileDialog.open();
			}
		});
	}

	private void addMenuBar() {
		Composite cmpMenuBar = new Composite(this, SWT.NONE);
		cmpMenuBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmpMenuBar.setBackground(SWTResourceManager.getColor(70, 70, 70));
		RowLayout rl_MenuBar = new RowLayout(SWT.HORIZONTAL);
		rl_MenuBar.spacing = 0;
		rl_MenuBar.wrap = false;
		rl_MenuBar.marginTop = 0;
		rl_MenuBar.marginRight = 0;
		rl_MenuBar.marginLeft = 0;
		rl_MenuBar.marginBottom = 5;
		cmpMenuBar.setLayout(rl_MenuBar);
		
		CLabel mnuFile = new CLabel(cmpMenuBar, SWT.NONE);
		mnuFile.setAlignment(SWT.CENTER);
		mnuFile.setLayoutData(new RowData(58, 24));
		mnuFile.setText("  &File  ");
		mnuFile.setForeground(SWTResourceManager.getColor(255, 255, 255));
		mnuFile.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		mnuFile.setBackground(SWTResourceManager.getColor(70, 70, 70));
		menuBarButtons.add(mnuFile);
		
			Menu mnuFilePopout = new Menu(mnuFile);
			mnuFile.setMenu(mnuFilePopout);
			
				MenuItem mntmNew = new MenuItem(mnuFilePopout, SWT.NONE);
				mntmNew.setText("New...");
				mntmNew.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						editor.setStatusText("Note: All of the new file's settings can be changed later");
						WindowNewFile newFileDialog = new WindowNewFile(editor);
						newFileDialog.open();
					}
				});
				
				MenuItem mntmOpen = new MenuItem(mnuFilePopout, SWT.NONE);
				mntmOpen.setText("Open...");
				
				new MenuItem(mnuFilePopout, SWT.SEPARATOR);
				
				MenuItem mntmSave = new MenuItem(mnuFilePopout, SWT.NONE);
				mntmSave.setText("Save");
				mntmSave.setEnabled(false);
				
				MenuItem mntmSaveAs = new MenuItem(mnuFilePopout, SWT.NONE);
				mntmSaveAs.setText("Save As...");
				mntmSaveAs.setEnabled(false);
				
				new MenuItem(mnuFilePopout, SWT.SEPARATOR);
				
				MenuItem mntmExit = new MenuItem(mnuFilePopout, SWT.NONE);
				mntmExit.setText("Exit");
				mntmExit.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						System.exit(0);
					}
				});
				
				mnuFile.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						mnuFilePopout.setVisible(true);
					}
				});
		
		CLabel mnuEdit = new CLabel(cmpMenuBar, SWT.NONE);
		mnuEdit.setAlignment(SWT.CENTER);
		mnuEdit.setLayoutData(new RowData(58, 24));
		mnuEdit.setText("  &Edit  ");
		mnuEdit.setForeground(SWTResourceManager.getColor(255, 255, 255));
		mnuEdit.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		mnuEdit.setBackground(SWTResourceManager.getColor(70, 70, 70));
		menuBarButtons.add(mnuEdit);
		
			Menu mnuEditPopout = new Menu(mnuEdit);
			mnuEdit.setMenu(mnuEditPopout);
			
				MenuItem mntmTemp = new MenuItem(mnuEditPopout, SWT.NONE);
				mntmTemp.setText("Temp");
				
//				new MenuItem(mnuFilePopout, SWT.SEPARATOR);
				
				mnuEdit.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						mnuEditPopout.setVisible(true);
					}
				});

		for (CLabel c : menuBarButtons) {
			c.addMouseTrackListener(new MouseTrackAdapter() {
				@Override
				public void mouseEnter(MouseEvent e) {
					c.setForeground(SWTResourceManager.getColor(200, 255, 200));
					c.setBackground(SWTResourceManager.getColor(80, 80, 80));
				}
				@Override
				public void mouseExit(MouseEvent e) {
					c.setForeground(SWTResourceManager.getColor(255, 255, 255));
					c.setBackground(SWTResourceManager.getColor(70, 70, 70));
				}
			});
		}
	}

	private void addTitleBar() {
		Composite cmpTitleBar = new Composite(this, SWT.NONE);
		cmpTitleBar.setLayout(new GridLayout(5, false));
		cmpTitleBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));		
		cmpTitleBar.setBackground(SWTResourceManager.getColor(80, 80, 80));
		
		lblHeaderText = new Label(cmpTitleBar, SWT.NONE);
		GridData gd_lblHeaderText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblHeaderText.heightHint = 24;
		gd_lblHeaderText.widthHint = 500;
		lblHeaderText.setLayoutData(gd_lblHeaderText);
		lblHeaderText.setForeground(SWTResourceManager.getColor(255, 255, 255));
		lblHeaderText.setFont(SWTResourceManager.getFont("Tahoma", 12, SWT.NORMAL));
		lblHeaderText.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		updateWorkingFilename();
		
		MouseMoveListener lstn_mouseMove = new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (editorMovePivot != null && editorMoveTotal != null) {
		            final Point location = shell.getLocation();
		            location.x += e.x - editorMovePivot.x;
		            location.y += e.y - editorMovePivot.y;
		            editorMoveTotal.x += e.x - editorMovePivot.x;
		            editorMoveTotal.y += e.y - editorMovePivot.y;
		            editor.setMaximized(false, true);
		            shell.setLocation(location);
		        }
			}
		};
		MouseAdapter lstn_mouseClick = new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					editorMoveOriginalPosition = shell.getLocation();
					editorMovePivot = new Point(e.x, e.y);
					editorMoveTotal = new Point(e.x + shell.getLocation().x, e.y + shell.getLocation().y);
		        }
			}
			@Override
			public void mouseUp(MouseEvent e) {
				if (editorMoveTotal.y <= 10) {
		            editor.setMaximized(true);
		        } else if (editorMoveTotal.x >= display.getClientArea().width - 10) {
	            	shell.setLocation(display.getClientArea().width/2, -3);
	            	shell.setSize(display.getClientArea().width/2+3, display.getClientArea().height+6);
	            } else if (editorMoveTotal.x <= 10) {
	            	shell.setLocation(-3, -3);
	            	shell.setSize(display.getClientArea().width/2+3, display.getClientArea().height+6);
		        } else if (editorMoveTotal.y >= display.getClientArea().height - 10) {
	            	shell.setLocation(editorMoveOriginalPosition);
		        	shell.setMinimized(true);
		        }
				editorMovePivot = null;
				editorMoveTotal = null;
			}
		};
		
		cmpTitleBar.addMouseMoveListener(lstn_mouseMove);
		lblHeaderText.addMouseMoveListener(lstn_mouseMove);
		cmpTitleBar.addMouseListener(lstn_mouseClick);
		lblHeaderText.addMouseListener(lstn_mouseClick);
		
		Label lblSpacer = new Label(cmpTitleBar, SWT.NONE);
		lblSpacer.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		
		Label lblMinimize = new Label(cmpTitleBar, SWT.NONE);
		lblMinimize.setText("\u0336 ");
		lblMinimize.setForeground(SWTResourceManager.getColor(255, 255, 255));
		lblMinimize.setFont(SWTResourceManager.getFont("Courier New", 12, SWT.NORMAL));
		lblMinimize.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblMinimize.setAlignment(SWT.CENTER);
		lblMinimize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				shell.setMinimized(true);
			}
		});
		lblMinimize.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				lblMinimize.setForeground(SWTResourceManager.getColor(150, 150, 150));
			}
			@Override
			public void mouseExit(MouseEvent e) {
				lblMinimize.setForeground(SWTResourceManager.getColor(255, 255, 255));
			}
		});
		
		Label lblMaximize = new Label(cmpTitleBar, SWT.NONE);
		lblMaximize.setText("\u25A1 ");
		lblMaximize.setForeground(SWTResourceManager.getColor(255, 255, 255));
		lblMaximize.setFont(SWTResourceManager.getFont("Courier New", 12, SWT.NORMAL));
		lblMaximize.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblMaximize.setAlignment(SWT.CENTER);
		lblMaximize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (editor.isMaximized()) {
					editor.setMaximized(false);
				} else {
					editor.setMaximized(true);
				}
			}
		});
		lblMaximize.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				if (editor.isMaximized()) {
					lblMaximize.setForeground(SWTResourceManager.getColor(150, 150, 150));
				} else {
					lblMaximize.setForeground(SWTResourceManager.getColor(150, 220, 150));
				}
			}
			@Override
			public void mouseExit(MouseEvent e) {
				lblMaximize.setForeground(SWTResourceManager.getColor(255, 255, 255));
			}
		});
		
		Label lblQuit = new Label(cmpTitleBar, SWT.NONE);
		lblQuit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblQuit.setText("X ");
		lblQuit.setForeground(SWTResourceManager.getColor(255, 255, 255));
		lblQuit.setFont(SWTResourceManager.getFont("Courier New", 12, SWT.NORMAL));
		lblQuit.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblQuit.setAlignment(SWT.CENTER);
		lblQuit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				System.exit(0);
			}
		});
		lblQuit.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				lblQuit.setForeground(SWTResourceManager.getColor(220, 150, 150));
			}
			@Override
			public void mouseExit(MouseEvent e) {
				lblQuit.setForeground(SWTResourceManager.getColor(255, 255, 255));
			}
		});
	}
		
	public void updateWorkingFilename() {
		if (editor.getActiveFile() != null) {
			lblHeaderText.setText(" ASKey  -  " + editor.getActiveFile().getFilename());
			lblHeaderText.redraw();
		} else {
			lblHeaderText.setText(" ASKey");
		}
	}
}
