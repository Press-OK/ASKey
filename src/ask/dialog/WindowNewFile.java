package ask.dialog;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import ask.data.AskeyDocument;
import ask.main.AskeyEditor;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;

public class WindowNewFile {
	private AskeyEditor editor;
	private Shell shlNewFile;
	
	private Text txtFilename;
	private Text txtRasterHeight;
	private Text txtRasterWidth;
	private Text txtAsciiHeight;
	private Text txtAsciiWidth;
	private Text txtFontDisplay;
	
	private String filename = "Unitled";
	private Font font = SWTResourceManager.getFont("Tahoma", 9, SWT.NORMAL);
	private int charH = 9, charW = 8;
	private Button chkAutoSize;
	private int rasterW = 500, rasterH = 500, asciiW = 62, asciiH = 41;
	private Color colorRaster = SWTResourceManager.getColor(255, 255, 255), colorAscii = colorRaster;
	
	public WindowNewFile(AskeyEditor editor) {
		this.editor = editor;
	}
	
	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		
		if (editor.getActiveFile() != null) {
			AskeyDocument d = editor.getActiveFile();
			rasterW = d.getRasterWidth();
			rasterH = d.getRasterHeight();
			asciiW = d.getAsciiWidth();
			asciiH = d.getAsciiHeight();
			charW = d.getCharW();
			charH = d.getCharH();
			colorRaster = SWTResourceManager.getColor(d.getRasterColorBGR(), d.getRasterColorBGG(), d.getRasterColorBGB());
			colorAscii = SWTResourceManager.getColor(d.getAsciiColorBGR(), d.getAsciiColorBGG(), d.getAsciiColorBGB());
			font = SWTResourceManager.getFont(d.getFontname(), d.getCharH(), SWT.NORMAL);
		}
		
		shlNewFile = new Shell(SWT.CLOSE | SWT.TITLE | SWT.APPLICATION_MODAL);
		shlNewFile.setBackground(SWTResourceManager.getColor(80, 80, 80));
		shlNewFile.setSize(308, 525);
		shlNewFile.setText("New File");
		shlNewFile.setLayout(null);
		shlNewFile.setLocation(display.getClientArea().width/2 - shlNewFile.getBounds().width/2, display.getClientArea().height/2 - shlNewFile.getBounds().height/2);
		
		txtFilename = new Text(shlNewFile, SWT.BORDER);
		txtFilename.setBounds(87, 24, 175, 19);
		txtFilename.setText(filename);
		txtFilename.setForeground(SWTResourceManager.getColor(255, 255, 255));
		txtFilename.setBackground(SWTResourceManager.getColor(65, 65, 65));
		txtFilename.setTextLimit(30);
		
		Button btnOk = new Button(shlNewFile, SWT.FLAT);
		btnOk.setBounds(140, 458, 65, 23);
		btnOk.setText("OK");
		
		Label lblName = new Label(shlNewFile, SWT.NONE);
		lblName.setAlignment(SWT.RIGHT);
		lblName.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		lblName.setBounds(38, 25, 43, 18);
		lblName.setForeground(SWTResourceManager.getColor(255, 255, 255));
		lblName.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblName.setText("Name:");
		
		Group grpCanvasSettings = new Group(shlNewFile, SWT.NONE);
		grpCanvasSettings.setFont(SWTResourceManager.getFont("Tahoma", 9, SWT.NORMAL));
		grpCanvasSettings.setForeground(SWTResourceManager.getColor(255, 255, 255));
		grpCanvasSettings.setBackground(SWTResourceManager.getColor(80, 80, 80));
		grpCanvasSettings.setText("Raster Canvas Settings");
		grpCanvasSettings.setBounds(25, 62, 251, 133);
		
		txtRasterWidth = new Text(grpCanvasSettings, SWT.BORDER);
		txtRasterWidth.setText(String.valueOf(rasterW));
		txtRasterWidth.setForeground(SWTResourceManager.getColor(255, 255, 255));
		txtRasterWidth.setBackground(SWTResourceManager.getColor(65, 65, 65));
		txtRasterWidth.setBounds(106, 40, 92, 19);
		txtRasterWidth.setTextLimit(4);
		txtRasterWidth.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!txtRasterWidth.getText().matches("^.*[^0-9].*$") && !txtRasterWidth.getText().isEmpty()) {
					int val = Integer.valueOf(txtRasterWidth.getText());
					if (val >= 10 && val <= 2500) {
						if (chkAutoSize.getSelection()) {
							rasterW = val;
				    		autoSizeAscii();
				    	}
					}
				}
		    }
		});
		txtRasterWidth.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String errorTitle = null;
				String errorMsg = null;
				if (!txtRasterWidth.getText().matches("^.*[^0-9].*$") && !txtRasterWidth.getText().isEmpty()) {
					int val = Integer.valueOf(txtRasterWidth.getText());
					if (val < 10 || val > 2500) {
						errorTitle = "Invalid Canvas Width";
						errorMsg = "Raster canvas width must be between 10-2500.";
						txtRasterWidth.setText("500");
						txtRasterWidth.setFocus();
						txtRasterWidth.selectAll();
					}
				} else {
					errorTitle = "Invalid Canvas Width";
					errorMsg = "Raster canvas width must be between 10-2500.";
					txtRasterWidth.setText("500");
					txtRasterWidth.setFocus();
					txtRasterWidth.selectAll();
				}
				if (errorMsg != null) {
					MessageBox msgBox = new MessageBox(shlNewFile, SWT.ICON_ERROR);
					msgBox.setText(errorTitle);
					msgBox.setMessage(errorMsg);
					msgBox.open();
					txtRasterWidth.setText("500");
					rasterW = 500;
				} else {
					if (chkAutoSize.getSelection()) {
						rasterW = Integer.valueOf(txtRasterWidth.getText());
			    		autoSizeAscii();
			    	}
				}
			}
		});
		
		txtRasterHeight = new Text(grpCanvasSettings, SWT.BORDER);
		txtRasterHeight.setBounds(106, 66, 92, 19);
		txtRasterHeight.setText(String.valueOf(rasterH));
		txtRasterHeight.setForeground(SWTResourceManager.getColor(255, 255, 255));
		txtRasterHeight.setBackground(SWTResourceManager.getColor(65, 65, 65));
		txtRasterHeight.setTextLimit(4);
		txtRasterHeight.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (!txtRasterHeight.getText().matches("^.*[^0-9].*$") && !txtRasterHeight.getText().isEmpty()) {
					int val = Integer.valueOf(txtRasterHeight.getText());
					if (val >= 10 && val <= 2500) {
						if (chkAutoSize.getSelection()) {
							rasterH = val;
				    		autoSizeAscii();
				    	}
					}
				}
		    }
		});
		txtRasterHeight.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String errorTitle = null;
				String errorMsg = null;
				if (!txtRasterHeight.getText().matches("^.*[^0-9].*$") && !txtRasterHeight.getText().isEmpty()) {
					int val = Integer.valueOf(txtRasterHeight.getText());
					if (val < 10 || val > 2500) {
						errorTitle = "Invalid Canvas Height";
						errorMsg = "Raster canvas height must be between 10-2500.";
						txtRasterHeight.setText("500");
						txtRasterHeight.setFocus();
						txtRasterHeight.selectAll();
					}
				} else {
					errorTitle = "Invalid Canvas Height";
					errorMsg = "Raster canvas height must be between 10-2500.";
					txtRasterHeight.setText("500");
					txtRasterHeight.setFocus();
					txtRasterHeight.selectAll();
				}
				if (errorMsg != null) {
					MessageBox msgBox = new MessageBox(shlNewFile, SWT.ICON_ERROR);
					msgBox.setText(errorTitle);
					msgBox.setMessage(errorMsg);
					msgBox.open();
					txtRasterHeight.setText("500");
					rasterH = 500;
				} else {
					if (chkAutoSize.getSelection()) {
						rasterH = Integer.valueOf(txtRasterHeight.getText());
			    		autoSizeAscii();
			    	}
				}
			}
		});
		
		Label selRasterBG = new Label(grpCanvasSettings, SWT.BORDER);
		selRasterBG.setBackground(colorRaster);
		selRasterBG.setBounds(106, 93, 111, 17);
		
		Label __lblHeight = new Label(grpCanvasSettings, SWT.NONE);
		__lblHeight.setBounds(53, 67, 47, 19);
		__lblHeight.setAlignment(SWT.RIGHT);
		__lblHeight.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		__lblHeight.setForeground(SWTResourceManager.getColor(255, 255, 255));
		__lblHeight.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		__lblHeight.setText("Height:");
		
		Label __lblWidth = new Label(grpCanvasSettings, SWT.NONE);
		__lblWidth.setText("Width:");
		__lblWidth.setForeground(SWTResourceManager.getColor(255, 255, 255));
		__lblWidth.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		__lblWidth.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		__lblWidth.setAlignment(SWT.RIGHT);
		__lblWidth.setBounds(53, 41, 47, 19);
		
		Label __lblBG = new Label(grpCanvasSettings, SWT.NONE);
		__lblBG.setText("Background:");
		__lblBG.setForeground(SWTResourceManager.getColor(255, 255, 255));
		__lblBG.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		__lblBG.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		__lblBG.setAlignment(SWT.RIGHT);
		__lblBG.setBounds(22, 92, 78, 19);
		
		Label __lblPx = new Label(grpCanvasSettings, SWT.NONE);
		__lblPx.setText("px");
		__lblPx.setForeground(SWTResourceManager.getColor(255, 255, 255));
		__lblPx.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		__lblPx.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		__lblPx.setBounds(204, 41, 18, 19);
		
		Label __lblPx1 = new Label(grpCanvasSettings, SWT.NONE);
		__lblPx1.setText("px");
		__lblPx1.setForeground(SWTResourceManager.getColor(255, 255, 255));
		__lblPx1.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		__lblPx1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		__lblPx1.setBounds(204, 67, 18, 19);
		
		Group grpAsciiCanvasSettings = new Group(shlNewFile, SWT.NONE);
		grpAsciiCanvasSettings.setFont(SWTResourceManager.getFont("Tahoma", 9, SWT.NORMAL));
		grpAsciiCanvasSettings.setText("ASCII Canvas Settings");
		grpAsciiCanvasSettings.setForeground(SWTResourceManager.getColor(255, 255, 255));
		grpAsciiCanvasSettings.setBackground(SWTResourceManager.getColor(80, 80, 80));
		grpAsciiCanvasSettings.setBounds(25, 223, 251, 205);
		
		txtAsciiWidth = new Text(grpAsciiCanvasSettings, SWT.BORDER);
		txtAsciiWidth.setText(String.valueOf(asciiW));
		txtAsciiWidth.setForeground(SWTResourceManager.getColor(255, 255, 255));
		txtAsciiWidth.setBackground(SWTResourceManager.getColor(65, 65, 65));
		txtAsciiWidth.setBounds(106, 89, 74, 19);
		txtAsciiWidth.setEnabled(false);
		txtAsciiWidth.setTextLimit(3);
		
		txtAsciiHeight = new Text(grpAsciiCanvasSettings, SWT.BORDER);
		txtAsciiHeight.setText(String.valueOf(asciiH));
		txtAsciiHeight.setForeground(SWTResourceManager.getColor(255, 255, 255));
		txtAsciiHeight.setBackground(SWTResourceManager.getColor(65, 65, 65));
		txtAsciiHeight.setBounds(106, 115, 74, 19);
		txtAsciiHeight.setEnabled(false);
		txtAsciiHeight.setTextLimit(3);
		
		chkAutoSize = new Button(grpAsciiCanvasSettings, SWT.FLAT | SWT.CHECK);
		chkAutoSize.setText("Auto-Size ASCII");
		chkAutoSize.setBackground(SWTResourceManager.getColor(80, 80, 80));
		chkAutoSize.setForeground(SWTResourceManager.getColor(255, 255, 255));
		chkAutoSize.setBounds(106, 71, 135, 16);
		chkAutoSize.setSelection(true);
		chkAutoSize.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (chkAutoSize.getSelection()) {
					String errorTitle = null;
					String errorMsg = null;
					if (!txtRasterWidth.getText().matches("^.*[^0-9].*$") && !txtRasterWidth.getText().isEmpty()) {
						int val = Integer.valueOf(txtRasterWidth.getText());
						if (val < 10 || val > 2500) {
							errorTitle = "Invalid Canvas Width";
							errorMsg = "Raster canvas width must be between 10-2500.";
							txtRasterWidth.setText("500");
							txtRasterWidth.setFocus();
							txtRasterWidth.selectAll();
						}
					} else {
						errorTitle = "Invalid Canvas Width";
						errorMsg = "Raster canvas width must be between 10-2500.";
						txtRasterWidth.setText("500");
						txtRasterWidth.setFocus();
						txtRasterWidth.selectAll();
					}
					if (!txtRasterHeight.getText().matches("^.*[^0-9].*$") && !txtRasterHeight.getText().isEmpty()) {
						int val = Integer.valueOf(txtRasterHeight.getText());
						if (val < 10 || val > 2500) {
							errorTitle = "Invalid Canvas Height";
							errorMsg = "Raster canvas height must be between 10-2500.";
							txtRasterHeight.setText("500");
							txtRasterHeight.setFocus();
							txtRasterHeight.selectAll();
						}
					} else {
						errorTitle = "Invalid Canvas Height";
						errorMsg = "Raster canvas height must be between 10-2500.";
						txtRasterHeight.setText("500");
						txtRasterHeight.setFocus();
						txtRasterHeight.selectAll();
					}
					if (errorMsg != null) {
						MessageBox msgBox = new MessageBox(shlNewFile, SWT.ICON_ERROR);
						msgBox.setText(errorTitle);
						msgBox.setMessage(errorMsg);
						msgBox.open();
					} else {
						rasterH = Integer.valueOf(txtRasterHeight.getText());
						rasterW = Integer.valueOf(txtRasterWidth.getText());
			    		autoSizeAscii();
					}
					txtAsciiWidth.setEnabled(false);
					txtAsciiHeight.setEnabled(false);
				} else {
					txtAsciiWidth.setEnabled(true);
					txtAsciiHeight.setEnabled(true);
				}
			}
		});
		
		Button btnFontSelect = new Button(grpAsciiCanvasSettings, SWT.NONE);
		btnFontSelect.setText("\u2026");
		btnFontSelect.setBounds(198, 40, 19, 19);
		btnFontSelect.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FontDialog dlg = new FontDialog(shlNewFile);
			    FontData fd = dlg.open();
			    if (fd != null && !fd.getName().equals("")) {
			    	font = new Font(shlNewFile.getDisplay(), fd);
			    	updateFont();
			    }
			}
		});
		
		txtFontDisplay = new Text(grpAsciiCanvasSettings, SWT.BORDER);
		txtFontDisplay.setEditable(false);
		String[] tempFontData = font.getFontData()[0].toString().split("\\|");
		txtFontDisplay.setText(charH + ", " + tempFontData[1]);
		txtFontDisplay.setForeground(SWTResourceManager.getColor(255, 255, 255));
		txtFontDisplay.setBackground(SWTResourceManager.getColor(65, 65, 65));
		txtFontDisplay.setBounds(106, 40, 111, 19);
		
		Label selAsciiBG = new Label(grpAsciiCanvasSettings, SWT.BORDER);
		selAsciiBG.setBackground(colorAscii);
		selAsciiBG.setBounds(106, 165, 111, 17);
		
		Label __lblHeight2 = new Label(grpAsciiCanvasSettings, SWT.NONE);
		__lblHeight2.setText("Height:");
		__lblHeight2.setForeground(SWTResourceManager.getColor(255, 255, 255));
		__lblHeight2.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		__lblHeight2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		__lblHeight2.setAlignment(SWT.RIGHT);
		__lblHeight2.setBounds(53, 116, 47, 19);
		
		Label __lblWidth2 = new Label(grpAsciiCanvasSettings, SWT.NONE);
		__lblWidth2.setText("Width:");
		__lblWidth2.setForeground(SWTResourceManager.getColor(255, 255, 255));
		__lblWidth2.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		__lblWidth2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		__lblWidth2.setAlignment(SWT.RIGHT);
		__lblWidth2.setBounds(53, 90, 47, 19);
		
		Label __lblBG2 = new Label(grpAsciiCanvasSettings, SWT.NONE);
		__lblBG2.setText("Background:");
		__lblBG2.setForeground(SWTResourceManager.getColor(255, 255, 255));
		__lblBG2.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		__lblBG2.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		__lblBG2.setAlignment(SWT.RIGHT);
		__lblBG2.setBounds(22, 164, 78, 19);
		
		Label __lblChars = new Label(grpAsciiCanvasSettings, SWT.NONE);
		__lblChars.setText("chars");
		__lblChars.setForeground(SWTResourceManager.getColor(255, 255, 255));
		__lblChars.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		__lblChars.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		__lblChars.setBounds(186, 90, 35, 19);
		
		Label __lblChars1 = new Label(grpAsciiCanvasSettings, SWT.NONE);
		__lblChars1.setText("chars");
		__lblChars1.setForeground(SWTResourceManager.getColor(255, 255, 255));
		__lblChars1.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		__lblChars1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		__lblChars1.setBounds(186, 116, 35, 19);
		
		Label __lblFont = new Label(grpAsciiCanvasSettings, SWT.NONE);
		__lblFont.setText("Font:");
		__lblFont.setForeground(SWTResourceManager.getColor(255, 255, 255));
		__lblFont.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
		__lblFont.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		__lblFont.setAlignment(SWT.RIGHT);
		__lblFont.setBounds(53, 40, 47, 19);

		Button btnMatchRaster = new Button(grpAsciiCanvasSettings, SWT.FLAT | SWT.CHECK);
		btnMatchRaster.setText("Match Raster Color");
		btnMatchRaster.setSelection(true);
		btnMatchRaster.setForeground(SWTResourceManager.getColor(255, 255, 255));
		btnMatchRaster.setBackground(SWTResourceManager.getColor(80, 80, 80));
		btnMatchRaster.setBounds(106, 147, 135, 16);
		selRasterBG.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Shell tempShell = new Shell(editor.getShell().getDisplay(), SWT.NONE);
				tempShell.setBounds(selRasterBG.getBounds());
				ColorDialog dlg = new ColorDialog(tempShell);
		        dlg.setRGB(colorRaster.getRGB());
		        dlg.setText("Raster Background Color");
		        RGB rgb = dlg.open();
		        tempShell.dispose();
		        if (rgb != null) {
		        	if (rgb.red == 0 && rgb.green == 0 && rgb.blue == 1) rgb.blue = 0;
		        	selRasterBG.setBackground(SWTResourceManager.getColor(rgb));
		        	colorRaster = (SWTResourceManager.getColor(rgb));
					if (btnMatchRaster.getSelection()) {
			        	selAsciiBG.setBackground(SWTResourceManager.getColor(rgb));
			        	colorAscii = (SWTResourceManager.getColor(rgb));
					}
		        }
			}
		});
		selAsciiBG.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if (!btnMatchRaster.getSelection()) {
					Shell tempShell = new Shell(editor.getShell().getDisplay(), SWT.NONE);
					tempShell.setBounds(selAsciiBG.getBounds());
					ColorDialog dlg = new ColorDialog(tempShell);
			        dlg.setRGB(colorAscii.getRGB());
			        dlg.setText("Raster Background Color");
			        RGB rgb = dlg.open();
			        tempShell.dispose();
			        if (rgb != null) {
			        	if (rgb.red == 0 && rgb.green == 0 && rgb.blue == 1) rgb.blue = 0;
			        	selAsciiBG.setBackground(SWTResourceManager.getColor(rgb));
			        	colorAscii = (SWTResourceManager.getColor(rgb));
			        }
				}
			}
		});

		txtFilename.selectAll();
		
		Button btnCancel = new Button(shlNewFile, SWT.FLAT);
		btnCancel.setBounds(211, 458, 65, 23);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editor.setStatusText("Cancelled new file creation");
				shlNewFile.close();
			}
		});
		
		btnOk.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String errorTitle = null;
				String errorMsg = null;
				if (txtFilename.getText().equals("")) {
					errorTitle = "Invalid Filename";
					errorMsg = "Please provide a filename.";
					txtFilename.setFocus();
					txtFilename.selectAll();
				}
				if (txtFilename.getText().matches("^.*[^a-zA-Z0-9 ].*$")) {
					errorTitle = "Invalid Filename";
					errorMsg = "Filename can only contain A-Z, 0-9, and spaces.";
					txtFilename.setFocus();
					txtFilename.selectAll();
				}
				if (txtRasterWidth.getText().length() > 4) {
					errorTitle = "Invalid Canvas Width";
					errorMsg = "Raster canvas width must be between 10-2500.";
					txtRasterWidth.setFocus();
					txtRasterWidth.selectAll();
				} else if (!txtRasterWidth.getText().matches("^.*[^0-9].*$") && !txtRasterWidth.getText().isEmpty()) {
					int val = Integer.valueOf(txtRasterWidth.getText());
					if (val < 10 || val > 2500) {
						errorTitle = "Invalid Canvas Width";
						errorMsg = "Raster canvas width must be between 10-2500.";
						txtRasterWidth.setFocus();
						txtRasterWidth.selectAll();
					}
				} else {
					errorTitle = "Invalid Canvas Width";
					errorMsg = "Raster canvas width must be between 10-2500.";
					txtRasterWidth.setFocus();
					txtRasterWidth.selectAll();
				}
				if (txtRasterHeight.getText().length() > 4) {
					errorTitle = "Invalid Canvas Width";
					errorMsg = "Raster canvas width must be between 10-2500.";
					txtRasterWidth.setFocus();
					txtRasterWidth.selectAll();
				} else if (!txtRasterHeight.getText().matches("^.*[^0-9].*$") && !txtRasterHeight.getText().isEmpty()) {
					int val = Integer.valueOf(txtRasterHeight.getText());
					if (val < 10 || val > 2500) {
						errorTitle = "Invalid Raster Height";
						errorMsg = "Raster canvas height must be between 10-2500 pixels.";
						txtRasterWidth.setFocus();
						txtRasterWidth.selectAll();
					}
				} else {
					errorTitle = "Invalid Raster Height";
					errorMsg = "Raster canvas height must be between 10-2500 pixels.";
					txtRasterWidth.setFocus();
					txtRasterWidth.selectAll();
				}
				if (charH < 6 || charH > 20) {
					errorTitle = "Invalid Font Size";
					errorMsg = "Font size must be between 6-20.";
				}
				if (!txtAsciiWidth.getText().matches("^.*[^0-9].*$") && !txtAsciiWidth.getText().isEmpty()) {
					int val = Integer.valueOf(txtAsciiWidth.getText());
					if (val < 5 || val > 315) {
						errorTitle = "Invalid ASCII Width";
						errorMsg = "ASCII canvas width must be between 5-315 characters.";
						txtAsciiWidth.setFocus();
						txtAsciiWidth.selectAll();
					}
				} else {
					errorTitle = "Invalid ASCII Width";
					errorMsg = "ASCII canvas width must be between 5-315 characters.";
					txtAsciiWidth.setFocus();
					txtAsciiWidth.selectAll();
				}
				if (!txtAsciiHeight.getText().matches("^.*[^0-9].*$") && !txtAsciiHeight.getText().isEmpty()) {
					int val = Integer.valueOf(txtAsciiHeight.getText());
					if (val < 5 || val > 315) {
						errorTitle = "Invalid ASCII Height";
						errorMsg = "ASCII canvas height must be between 5-315 characters.";
						txtAsciiHeight.setFocus();
						txtAsciiHeight.selectAll();
					}
				} else {
					errorTitle = "Invalid ASCII Height";
					errorMsg = "ASCII canvas height must be between 5-315 characters.";
					txtAsciiHeight.setFocus();
					txtAsciiHeight.selectAll();
				}
				if (errorMsg != null) {
					MessageBox msgBox = new MessageBox(shlNewFile, SWT.ICON_ERROR);
					msgBox.setText(errorTitle);
					msgBox.setMessage(errorMsg);
					msgBox.open();
				} else {
					AskeyDocument newFile = new AskeyDocument();
					newFile.setFilename(txtFilename.getText());
					newFile.setRasterWidth(Integer.valueOf(txtRasterWidth.getText()));
					newFile.setRasterHeight(Integer.valueOf(txtRasterHeight.getText()));
					newFile.setAsciiWidth(Integer.valueOf(txtAsciiWidth.getText()));
					newFile.setAsciiHeight(Integer.valueOf(txtAsciiHeight.getText()));
					String[] tempFontData = font.getFontData()[0].toString().split("\\|");
					newFile.setFontname(tempFontData[1]);
					newFile.setCharW(charW);
					newFile.setCharH(charH);
					newFile.setRasterColorBGR(colorRaster.getRed());
					newFile.setRasterColorBGG(colorRaster.getGreen());
					newFile.setRasterColorBGB(colorRaster.getBlue());
					newFile.setAsciiColorBGR(colorAscii.getRed());
					newFile.setAsciiColorBGG(colorAscii.getGreen());
					newFile.setAsciiColorBGB(colorAscii.getBlue());
					
					editor.setActiveFile(newFile);
					editor.setStatusText("Now editing:  " + txtFilename.getText() + ".ask  (" + txtRasterWidth.getText() + "x" + txtRasterHeight.getText() + ")");
					editor.setEditingEnabled(true);
					editor.getCanvasRaster().redraw();
					editor.getCanvasAscii().updateDisplaySettingsFromEditor();
					editor.getCanvasAscii().redraw();
					editor.getTimeline().initialize();
					shlNewFile.close();
				}
			}
		});
		
		shlNewFile.open();
		shlNewFile.layout();
		while (!shlNewFile.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	private boolean updateFont() {
		String[] tempFontData = font.getFontData()[0].toString().split("\\|");
    	charH = (int)(Math.round(Double.valueOf(tempFontData[2])));
    	charW = (int)(Math.round(charH * 0.8));
    	if (chkAutoSize.getSelection()) {
    		autoSizeAscii();
    	}
	    txtFontDisplay.setText(charH + ", " + tempFontData[1]);
	    return true;
	}
	
	private void autoSizeAscii() {
		asciiW = (int)(rasterW / (double)charW);
		asciiH = (int)(rasterH / (double)(charH+charH/3));
		txtAsciiWidth.setText(String.valueOf(asciiW));
		txtAsciiHeight.setText(String.valueOf(asciiH));
	}
}
