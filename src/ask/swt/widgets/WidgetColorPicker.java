package ask.swt.widgets;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;

import ask.main.AskeyEditor;
import ask.swt.layout.SWT_ModularVerticalBar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.GridLayout;

public class WidgetColorPicker extends _WidgetTemplate {
	
	private AskeyEditor editor;
	private Color[] colorHistory;
	
	private Canvas cvsHistory, cvsHistory2;
	private Label lblFGR, lblFGG, lblFGB, lblBGR, lblBGG, lblBGB;
	
	private int widgetHeight = 189, widgetWidth = 182;
	private boolean isCollapsed = false;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public WidgetColorPicker(Composite parent, int style) {
		super(parent, style, "Colors");
		this.editor = ((SWT_ModularVerticalBar) this.getParent()).getEditorWindow();
		this.colorHistory = editor.getColorHistory();
		
		Composite cmpGridHolder = new Composite(this, SWT.NONE);
		GridLayout gl_cmpGridHolder = new GridLayout(1, false);
		gl_cmpGridHolder.marginHeight = 0;
		gl_cmpGridHolder.marginWidth = 0;
		gl_cmpGridHolder.verticalSpacing = 0;
		gl_cmpGridHolder.horizontalSpacing = 0;
		cmpGridHolder.setLayout(gl_cmpGridHolder);
		GridData gd_cmpGridHolder = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_cmpGridHolder.widthHint = widgetWidth;
		gd_cmpGridHolder.heightHint = widgetHeight;
		cmpGridHolder.setLayoutData(gd_cmpGridHolder);
		
		Composite cmpColorPicker = new Composite(cmpGridHolder, SWT.NONE);
		cmpColorPicker.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		cmpColorPicker.setBackground(SWTResourceManager.getColor(50, 50, 50));
		cmpColorPicker.setLayout(null);
		
		Label lblFGColor = new Label(cmpColorPicker, SWT.BORDER | SWT.SHADOW_IN);
		lblFGColor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblFGColor.setBounds(56, 23, 50, 50);
		lblFGColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Shell tempShell = new Shell(editor.getShell().getDisplay(), SWT.NONE);
				tempShell.setBounds(getBounds());
				ColorDialog dlg = new ColorDialog(tempShell);
		        dlg.setRGB(lblFGColor.getBackground().getRGB());
		        dlg.setText("Foreground Color");
		        RGB rgb = dlg.open();
		        tempShell.dispose();
		        if (rgb != null) {
		        	if (rgb.red == 0 && rgb.green == 0 && rgb.blue == 1) rgb.blue = 0; 
		        	lblFGColor.setBackground(SWTResourceManager.getColor(rgb));
		        	editor.setFGColor(SWTResourceManager.getColor(rgb));
		        	UpdateColorHexStrings();
		        	if (colorHistory[0] != SWTResourceManager.getColor(rgb)) {
			        	for (int i = colorHistory.length - 1; i > 0; i--) {
			        		colorHistory[i] = colorHistory[i-1];
			        	}
			        	colorHistory[0] = SWTResourceManager.getColor(rgb);
			        	cvsHistory.redraw();
			        	cvsHistory2.redraw();
		        	}
		        }
			}
		});
		
		lblFGB = new Label(cmpColorPicker, SWT.NONE);
		lblFGB.setText("FF");
		lblFGB.setForeground(SWTResourceManager.getColor(180, 180, 255));
		lblFGB.setFont(SWTResourceManager.getFont("Courier New", 9, SWT.NORMAL));
		lblFGB.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblFGB.setBounds(84, 10, 17, 15);
		
		lblFGG = new Label(cmpColorPicker, SWT.NONE);
		lblFGG.setText("FF");
		lblFGG.setForeground(SWTResourceManager.getColor(180, 255, 180));
		lblFGG.setFont(SWTResourceManager.getFont("Courier New", 9, SWT.NORMAL));
		lblFGG.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblFGG.setBounds(70, 10, 17, 15);
		
		lblFGR = new Label(cmpColorPicker, SWT.NONE);
		lblFGR.setText("FF");
		lblFGR.setForeground(SWTResourceManager.getColor(255, 180, 180));
		lblFGR.setFont(SWTResourceManager.getFont("Courier New", 9, SWT.NORMAL));
		lblFGR.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblFGR.setBounds(56, 10, 17, 15);
		
		Label lblBGColor = new Label(cmpColorPicker, SWT.BORDER | SWT.SHADOW_IN);
		lblBGColor.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		lblBGColor.setBounds(81, 49, 50, 50);
		lblBGColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Shell tempShell = new Shell(editor.getShell().getDisplay(), SWT.NONE);
				tempShell.setBounds(getBounds());
				ColorDialog dlg = new ColorDialog(tempShell);
		        dlg.setRGB(lblBGColor.getBackground().getRGB());
		        dlg.setText("Background Color");
		        RGB rgb = dlg.open();
		        tempShell.dispose();
		        if (rgb != null) {
		        	if (rgb.red == 0 && rgb.green == 0 && rgb.blue == 1) rgb.blue = 0; 
		        	lblBGColor.setBackground(SWTResourceManager.getColor(rgb));
		        	editor.setBGColor(SWTResourceManager.getColor(rgb));
		        	UpdateColorHexStrings();
		        	if (colorHistory[0] != SWTResourceManager.getColor(rgb)) {
			        	for (int i = colorHistory.length - 1; i > 0; i--) {
			        		colorHistory[i] = colorHistory[i-1];
			        	}
			        	colorHistory[0] = SWTResourceManager.getColor(rgb);
			        	cvsHistory.redraw();
			        	cvsHistory2.redraw();
		        	}
		        }
			}
		});
		
		lblBGB = new Label(cmpColorPicker, SWT.NONE);
		lblBGB.setText("00");
		lblBGB.setForeground(SWTResourceManager.getColor(180, 180, 255));
		lblBGB.setFont(SWTResourceManager.getFont("Courier New", 9, SWT.NORMAL));
		lblBGB.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblBGB.setBounds(117, 97, 17, 15);
		
		lblBGG = new Label(cmpColorPicker, SWT.NONE);
		lblBGG.setText("00");
		lblBGG.setForeground(SWTResourceManager.getColor(180, 255, 180));
		lblBGG.setFont(SWTResourceManager.getFont("Courier New", 9, SWT.NORMAL));
		lblBGG.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblBGG.setBounds(103, 97, 17, 15);
		
		lblBGR = new Label(cmpColorPicker, SWT.NONE);
		lblBGR.setText("00");
		lblBGR.setForeground(SWTResourceManager.getColor(255, 180, 180));
		lblBGR.setFont(SWTResourceManager.getFont("Courier New", 9, SWT.NORMAL));
		lblBGR.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblBGR.setBounds(89, 97, 17, 15);
		
		Label lblSwapColors = new Label(cmpColorPicker, SWT.NONE);
		lblSwapColors.setText("\u2195");
		lblSwapColors.setForeground(SWTResourceManager.getColor(120, 120, 120));
		lblSwapColors.setFont(SWTResourceManager.getFont("Courier New", 20, SWT.NORMAL));
		lblSwapColors.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lblSwapColors.setBounds(36, 56, 17, 31);
		lblSwapColors.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Color c = lblFGColor.getBackground();
				lblFGColor.setBackground(lblBGColor.getBackground());
				lblBGColor.setBackground(c);
				editor.setFGColor(lblFGColor.getBackground());
				editor.setBGColor(lblBGColor.getBackground());
				UpdateColorHexStrings();
			}
		});
		lblSwapColors.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				lblSwapColors.setForeground(SWTResourceManager.getColor(255, 255, 255));
			}
			@Override
			public void mouseExit(MouseEvent e) {
				lblSwapColors.setForeground(SWTResourceManager.getColor(120, 120, 120));
			}
		});
		
		cvsHistory = new Canvas(cmpColorPicker, SWT.NONE);
		cvsHistory.setBackground(SWTResourceManager.getColor(0, 0, 0));
		cvsHistory.setBounds(7, 130, 167, 23);
		cvsHistory.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Image img = new Image(editor.getShell().getDisplay(), 1, 1);
				GC gc = new GC((Canvas)e.widget);
            	gc.copyArea(img, e.x, e.y);
            	ImageData imgData = img.getImageData();
            	int pixelValue = imgData.getPixel(0,0);
            	PaletteData palette = imgData.palette;
            	RGB rgb = palette.getRGB(pixelValue);
            	if (e.button == 1) {
	            	lblFGColor.setBackground(SWTResourceManager.getColor(rgb));
	            	editor.setFGColor(SWTResourceManager.getColor(rgb));
            	} else if (e.button == 3) {
	            	lblBGColor.setBackground(SWTResourceManager.getColor(rgb));
	            	editor.setBGColor(SWTResourceManager.getColor(rgb));
            	}
            	UpdateColorHexStrings();
            	gc.dispose();
            	img.dispose();
			}
		});
		cvsHistory.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				for (int i = 0; i < colorHistory.length / 2; i++) {
					e.gc.setBackground(colorHistory[i]);
					e.gc.fillRectangle(i*10+1+i, 1, 11, cvsHistory.getBounds().height-1);
				}
			}
		});
		
		cvsHistory2 = new Canvas(cmpColorPicker, SWT.NONE);
		cvsHistory2.setBackground(SWTResourceManager.getColor(0, 0, 0));
		cvsHistory2.setBounds(7, 153, 167, 24);
		cvsHistory2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				Image img = new Image(editor.getShell().getDisplay(), 1, 1);
				GC gc = new GC((Canvas)e.widget);
            	gc.copyArea(img, e.x, e.y);
            	ImageData imgData = img.getImageData();
            	int pixelValue = imgData.getPixel(0,0);
            	PaletteData palette = imgData.palette;
            	RGB rgb = palette.getRGB(pixelValue);
            	if (e.button == 1) {
	            	lblFGColor.setBackground(SWTResourceManager.getColor(rgb));
	            	editor.setFGColor(SWTResourceManager.getColor(rgb));
            	} else if (e.button == 3) {
	            	lblBGColor.setBackground(SWTResourceManager.getColor(rgb));
	            	editor.setBGColor(SWTResourceManager.getColor(rgb));
            	}
            	UpdateColorHexStrings();
            	gc.dispose();
            	img.dispose();
			}
		});
		cvsHistory2.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				for (int i = colorHistory.length / 2; i < colorHistory.length; i++) {
					e.gc.setBackground(colorHistory[i]);
					e.gc.fillRectangle((i-colorHistory.length / 2)*10+1+(i-colorHistory.length / 2), 1, 11, cvsHistory.getBounds().height-1);
				}
			}
		});

		getDragLabel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (isCollapsed) {
					gd_cmpGridHolder.heightHint = widgetHeight;
					getParent().layout(true, true);
					isCollapsed = false;
				} else {
					gd_cmpGridHolder.heightHint = 0;
					getParent().layout(true, true);
					isCollapsed = true;
				}
			}
		});
	}
	
	private void UpdateColorHexStrings() {
		Color fg = editor.getFGColor();
		Color bg = editor.getBGColor();
		if (Integer.toHexString(fg.getRGB().red).length() == 1) {
			lblFGR.setText(("0" + Integer.toHexString(fg.getRGB().red)).toUpperCase());
		} else {
			lblFGR.setText((Integer.toHexString(fg.getRGB().red)).toUpperCase());
		}
		if (Integer.toHexString(fg.getRGB().green).length() == 1) {
			lblFGG.setText(("0" + Integer.toHexString(fg.getRGB().green)).toUpperCase());
		} else {
			lblFGG.setText((Integer.toHexString(fg.getRGB().green)).toUpperCase());
		}
		if (Integer.toHexString(fg.getRGB().blue).length() == 1) {
			lblFGB.setText(("0" + Integer.toHexString(fg.getRGB().blue)).toUpperCase());
		} else {
			lblFGB.setText((Integer.toHexString(fg.getRGB().blue)).toUpperCase());
		}
		if (Integer.toHexString(bg.getRGB().red).length() == 1) {
			lblBGR.setText(("0" + Integer.toHexString(bg.getRGB().red)).toUpperCase());
		} else {
			lblBGR.setText((Integer.toHexString(bg.getRGB().red)).toUpperCase());
		}
		if (Integer.toHexString(bg.getRGB().green).length() == 1) {
			lblBGG.setText(("0" + Integer.toHexString(bg.getRGB().green)).toUpperCase());
		} else {
			lblBGG.setText((Integer.toHexString(bg.getRGB().green)).toUpperCase());
		}
		if (Integer.toHexString(bg.getRGB().blue).length() == 1) {
			lblBGB.setText(("0" + Integer.toHexString(bg.getRGB().blue)).toUpperCase());
		} else {
			lblBGB.setText((Integer.toHexString(bg.getRGB().blue)).toUpperCase());
		}
	}
}
