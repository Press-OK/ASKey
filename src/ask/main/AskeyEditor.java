package ask.main;

import swing2swt.layout.BorderLayout;

import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.wb.swt.SWTResourceManager;

import ask.data.AskeyDocument;
import ask.data.Keyframe;
import ask.data.RasterTool;
import ask.swt.layout.SWT_CanvasASCII;
import ask.swt.layout.SWT_CanvasRaster;
import ask.swt.layout.SWT_CenterPane;
import ask.swt.layout.SWT_ModularVerticalBar;
import ask.swt.layout.SWT_LayersTimeline;
import ask.swt.layout.SWT_TitleBar;
import ask.swt.widgets.WidgetColorPicker;
import ask.swt.widgets.WidgetToolProperties;
import ask.swt.widgets.WidgetSpacer;
import ask.swt.widgets.WidgetRasterToolbar;

public class AskeyEditor {
	
	private Display display;
	private Shell shell;

	private SWT_TitleBar cmpTitleBar;
	private AskeyDocument activeFile = null;
	private boolean isModified = false;

	private Color FGColor = SWTResourceManager.getColor(255, 255, 255);
	private Color BGColor = SWTResourceManager.getColor(0, 0, 0);
	private Color[] colorHistory = new Color[30];
	private HashSet<Canvas> canvasesRequiringRedraw = new HashSet<>();
	
	private SWT_CanvasRaster cvsRaster = null;
	private SWT_CanvasASCII cvsAscii = null;
	private SWT_LayersTimeline cmpTimeline = null;
	
	private Keyframe activeKeyframe = null;
	
	private RasterTool selectedTool = null;

	private boolean isMaximised = true;
	private SWT_ModularVerticalBar cmpRightPane;
	private SWT_CenterPane cmpCenter;
	private SWT_ModularVerticalBar cmpLeftPane;
	private Label lblStatusBar;
	private WidgetToolProperties cmpToolProperties;

	public AskeyEditor() {
		for (int i = 0; i < colorHistory.length; i++) {
			double r = 255.0;
			double g = 255.0;
			double b = 255.0;
			if (i < colorHistory.length / 2) {
				r = g = b = 255.0 * (1 - i / (colorHistory.length / 2.0));
			} else {
				r *= Math.random();
				g *= Math.random();
				b *= Math.random();
			}
			colorHistory[i] = SWTResourceManager.getColor((int)r, (int)g, (int)b);
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		display = Display.getDefault();
		shell = new Shell(display, SWT.RESIZE);
		shell.setBackground(SWTResourceManager.getColor(30, 30, 30));
		shell.setLayout(new BorderLayout(0, 0));
		setMaximized(true);
		
		cmpTitleBar = new SWT_TitleBar(shell, SWT.NONE, this);
		cmpTitleBar.setLayoutData(BorderLayout.NORTH);
		updateFilenameUI();

		cmpLeftPane = new SWT_ModularVerticalBar(shell, SWT.NONE, this);
		cmpLeftPane.setLayoutData(BorderLayout.WEST);
		WidgetRasterToolbar cmpToolBar = new WidgetRasterToolbar(cmpLeftPane, SWT.NONE);
		WidgetColorPicker cmpColorPicker = new WidgetColorPicker(cmpLeftPane, SWT.NONE);
		cmpToolProperties = new WidgetToolProperties(cmpLeftPane, SWT.NONE);
		WidgetSpacer cmpSpacer = new WidgetSpacer(cmpLeftPane, SWT.NONE);
		
		cmpRightPane = new SWT_ModularVerticalBar(shell, SWT.NONE, this);
		cmpRightPane.setLayoutData(BorderLayout.EAST);
		WidgetToolProperties cmpProperties2PLACEHOLDER = new WidgetToolProperties(cmpRightPane, SWT.NONE);
		WidgetSpacer cmpSpacer2PLACEHOLDER = new WidgetSpacer(cmpRightPane, SWT.NONE);
		
		cmpCenter = new SWT_CenterPane(shell, SWT.NONE, this);
		cmpCenter.setLayoutData(BorderLayout.CENTER);
		this.activeKeyframe = cmpCenter.getTimeline().getActiveKeyframe();
		
		Composite cmpStatusBar = new Composite(shell, SWT.NONE);
		cmpStatusBar.setBackground(SWTResourceManager.getColor(30, 30, 30));
		cmpStatusBar.setLayoutData(BorderLayout.SOUTH);
		FillLayout fl_cmpStatusBar = new FillLayout(SWT.HORIZONTAL);
		fl_cmpStatusBar.marginHeight = 2;
		cmpStatusBar.setLayout(fl_cmpStatusBar);
		
			lblStatusBar = new Label(cmpStatusBar, SWT.NONE);
			lblStatusBar.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
			lblStatusBar.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			lblStatusBar.setBackground(SWTResourceManager.getColor(50, 50, 50));
			lblStatusBar.setText("  Ready");

		setEditingEnabled(false);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void setEditingEnabled(boolean b) {
		cmpLeftPane.setEnabled(b);
		cmpRightPane.setEnabled(b);
		cmpCenter.setEnabled(b);
		if (b) {
			((ScrolledComposite)(cvsRaster.getParent())).setExpandHorizontal(false);
			((ScrolledComposite)(cvsRaster.getParent())).setExpandVertical(false);
			((ScrolledComposite)(cvsAscii.getParent())).setExpandHorizontal(false);
			((ScrolledComposite)(cvsAscii.getParent())).setExpandVertical(false);
		}
	}
	
	public String getStatusText() {
		return this.lblStatusBar.getText();
	}
	
	public void setStatusText(String s) {
		this.lblStatusBar.setText("  " + s.trim());
	}

	public Shell getShell() {
		return this.shell;
	}
	
	public AskeyEditor getEditorWindow() {
		return this;
	}
	
	public boolean isMaximized() {
		return this.isMaximised;
	}
	
	// TODO implement this from a menu
	public void stretchFullscreen() {
		isMaximised = true;
		shell.setMaximized(true);
	}
	
	public void setMaximized(boolean toMax) {
		if (toMax) {
			isMaximised = true;
			shell.setSize(display.getClientArea().width+6, display.getClientArea().height+6);
			shell.setLocation(-3, -3);
		} else if (!toMax) {
			isMaximised = false;
			shell.setSize(display.getClientArea().width-350, display.getClientArea().height-200);
			shell.setLocation(display.getClientArea().width/2 - shell.getBounds().width/2, display.getClientArea().height/2 - shell.getBounds().height/2);
		}
	}

	public void setMaximized(boolean toMax, boolean dontResize) {
		if (dontResize) {
			isMaximised = toMax;
		}
	}
	public Color getFGColor() {
		return FGColor;
	}

	public void setFGColor(Color fGColor) {
		FGColor = fGColor;
		redrawCanvases();
	}

	public Color getBGColor() {
		return BGColor;
	}

	public void setBGColor(Color bGColor) {
		BGColor = bGColor;
		redrawCanvases();
	}

	public Color[] getColorHistory() {
		return colorHistory;
	}

	private void updateFilenameUI() {
		if (activeFile != null) {
			shell.setText("ASKey - " + activeFile.getFilename());
		} else {
			shell.setText("ASKey");
		}
		cmpTitleBar.updateWorkingFilename();
	}
	
	public String getWorkingFileName() {
		return activeFile.getFilename();
	}

	public void setWorkingFileName(String workingFileName) {
		activeFile.setFilename(workingFileName);
		updateFilenameUI();
	}
	
	public boolean isModified() {
		return isModified;
	}

	public void setModified(boolean isModified) {
		this.isModified = isModified;
		updateFilenameUI();
	}

	public AskeyDocument getActiveFile() {
		return activeFile;
	}

	public void setActiveFile(AskeyDocument activeFile) {
		this.activeFile = activeFile;
		this.cvsRaster.recalculateCanvasSize();
		this.cvsAscii.recalculateCanvasSize();
		setWorkingFileName(activeFile.getFilename());
	}

	public SWT_CanvasRaster getCanvasRaster() {
		return cvsRaster;
	}

	public void setCanvasRaster(SWT_CanvasRaster canvasRaster) {
		this.cvsRaster = canvasRaster;
	}

	public SWT_CanvasASCII getCanvasAscii() {
		return cvsAscii;
	}

	public void setCanvasAscii(SWT_CanvasASCII canvasAscii) {
		this.cvsAscii = canvasAscii;
	}

	public SWT_LayersTimeline getTimeline() {
		return cmpTimeline;
	}

	public void setTimeline(SWT_LayersTimeline t) {
		this.cmpTimeline = t;
	}

	public RasterTool getSelectedTool() {
		return selectedTool;
	}

	public void setSelectedTool(RasterTool selectedTool) {
		this.selectedTool = selectedTool;
		cmpToolProperties.setTool(selectedTool);
		setStatusText("Tool selected: " + selectedTool.getName());
	}
	
	public void addCanvasToRedrawSet(Canvas c) {
		this.canvasesRequiringRedraw.add(c);
	}
	
	public void redrawCanvases() {
		for (Canvas c : this.canvasesRequiringRedraw) {
			c.redraw();
		}
	}

	public Keyframe getActiveKeyframe() {
		this.activeKeyframe = cmpCenter.getTimeline().getActiveKeyframe();
		return activeKeyframe;
	}
}