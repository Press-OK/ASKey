package ask.drawtools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import ask.main.AskeyEditor;
import ask.data.Keyframe;
import ask.data.RasterTool;
import ask.swt.widgets.WidgetToolProperties;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class Paintbrush extends RasterTool {
	
	private AskeyEditor editor = null;
	private int widgetHeight = 185;
	
	private boolean isDrawing = false;
	
	private int brushSize = 20;

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void drawProperties(Composite parent, int style) {
		this.editor = ((WidgetToolProperties)(parent.getParent())).getEditorWindow();
		Composite cmpPropHolder = new Composite(parent, SWT.NONE);
		GridLayout gl_cmpPropHolder = new GridLayout(1, false);
		gl_cmpPropHolder.marginHeight = 0;
		gl_cmpPropHolder.marginBottom = 0;
		gl_cmpPropHolder.marginWidth = 0;
		gl_cmpPropHolder.verticalSpacing = 5;
		gl_cmpPropHolder.horizontalSpacing = 0;
		cmpPropHolder.setLayout(gl_cmpPropHolder);
		GridData gd_cmpPropHolder = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
		gd_cmpPropHolder.widthHint = 172;
		cmpPropHolder.setLayoutData(gd_cmpPropHolder);
		cmpPropHolder.setBackground(SWTResourceManager.getColor(50, 50, 50));
		
		Composite cmpBrushSize = new Composite(cmpPropHolder, SWT.NONE);
		cmpBrushSize.setBackground(SWTResourceManager.getColor(50, 50, 50));
		cmpBrushSize.setLayout(null);
		GridData gd_cmpBrushSize = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gd_cmpBrushSize.heightHint = 87;
		gd_cmpBrushSize.widthHint = 172;
		cmpBrushSize.setLayoutData(gd_cmpBrushSize);
		
			Label lblBrushSize = new Label(cmpBrushSize, SWT.NONE);
			lblBrushSize.setBackground(SWTResourceManager.getColor(50, 50, 50));
			lblBrushSize.setForeground(SWTResourceManager.getColor(255, 255, 255));
			lblBrushSize.setBounds(5, 5, 62, 13);
			lblBrushSize.setText("Brush Size");
			
			Label lblSizeDisplay = new Label(cmpBrushSize, SWT.NONE);
			lblSizeDisplay.setBackground(SWTResourceManager.getColor(50, 50, 50));
			lblSizeDisplay.setForeground(SWTResourceManager.getColor(255, 255, 255));
			lblSizeDisplay.setAlignment(SWT.RIGHT);
			lblSizeDisplay.setBounds(133, 5, 34, 13);
			lblSizeDisplay.setText("20");
			
			Canvas cvsSizePreview = new Canvas(cmpBrushSize, SWT.NONE);
			cvsSizePreview.setBackground(SWTResourceManager.getColor(50, 50, 50));
			cvsSizePreview.setBounds(5, 49, 162, 32);
			cvsSizePreview.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					Rectangle b = cvsSizePreview.getBounds();
					e.gc.setBackground(SWTResourceManager.getColor(0, 0, 0));
					e.gc.fillOval(b.width/2-brushSize/2, b.height/2-brushSize/2, brushSize, brushSize);
					e.gc.setForeground(SWTResourceManager.getColor(0, 0, 0));
					e.gc.drawRectangle(0, 0, b.width-1, b.height-1);
				}
			});			
			
			Slider sldBrushSize = new Slider(cmpBrushSize, SWT.NONE);
			sldBrushSize.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					brushSize = sldBrushSize.getSelection();
					cvsSizePreview.redraw();
					lblSizeDisplay.setText(String.valueOf(brushSize));
				}
			});
			sldBrushSize.setMaximum(160);
			sldBrushSize.setMinimum(1);
			sldBrushSize.setSelection(20);
			sldBrushSize.setBounds(5, 27, 162, 16);
			
//		Composite cmpBrushOpacity = new Composite(cmpPropHolder, SWT.NONE);
//		cmpBrushOpacity.setBackground(SWTResourceManager.getColor(50, 50, 50));
//		cmpBrushOpacity.setLayout(null);
//		GridData gd_cmpBrushOpacity = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
//		gd_cmpBrushOpacity.heightHint = 87;
//		gd_cmpBrushOpacity.widthHint = 172;
//		cmpBrushOpacity.setLayoutData(gd_cmpBrushOpacity);
//		
//			Label lblBrushOpacity = new Label(cmpBrushOpacity, SWT.NONE);
//			lblBrushOpacity.setBackground(SWTResourceManager.getColor(50, 50, 50));
//			lblBrushOpacity.setForeground(SWTResourceManager.getColor(255, 255, 255));
//			lblBrushOpacity.setBounds(5, 5, 77, 13);
//			lblBrushOpacity.setText("Brush Opacity");
//			
//			Label lblOpacityDisplay = new Label(cmpBrushOpacity, SWT.NONE);
//			lblOpacityDisplay.setBackground(SWTResourceManager.getColor(50, 50, 50));
//			lblOpacityDisplay.setForeground(SWTResourceManager.getColor(255, 255, 255));
//			lblOpacityDisplay.setAlignment(SWT.RIGHT);
//			lblOpacityDisplay.setBounds(133, 5, 34, 13);
//			lblOpacityDisplay.setText("1.0");
//			
//			Canvas cvsBrushOpacity = new Canvas(cmpBrushOpacity, SWT.NONE);
//			cvsBrushOpacity.setBackground(SWTResourceManager.getColor(50, 50, 50));
//			cvsBrushOpacity.setBounds(5, 49, 162, 32);
//			cvsBrushOpacity.addPaintListener(new PaintListener() {
//				@Override
//				public void paintControl(PaintEvent e) {
//					Rectangle b = cvsBrushOpacity.getBounds();
//					e.gc.setAlpha((int)(brushOpacity*255));
//					e.gc.setBackground(SWTResourceManager.getColor(0, 0, 0));
//					e.gc.fillRectangle(0, 0, b.width, b.height);
//					e.gc.setAlpha(255);
//					e.gc.setForeground(SWTResourceManager.getColor(0, 0, 0));
//					e.gc.drawRectangle(0, 0, b.width-1, b.height-1);
//				}
//			});			
//			
//			Slider sldBrushOpacity = new Slider(cmpBrushOpacity, SWT.NONE);
//			sldBrushOpacity.addSelectionListener(new SelectionAdapter() {
//				@Override
//				public void widgetSelected(SelectionEvent e) {
//					brushOpacity = sldBrushOpacity.getSelection() / 100.0f;
//					cvsBrushOpacity.redraw();
//					lblOpacityDisplay.setText(String.valueOf(brushOpacity));
//				}
//			});
//			sldBrushOpacity.setMaximum(110);
//			sldBrushOpacity.setMinimum(1);
//			sldBrushOpacity.setSelection(100);
//			sldBrushOpacity.setBounds(5, 27, 162, 16);
	}
	
	public int getPropertiesHeight() {
		return this.widgetHeight;
	}

	@Override
	public void handleMouseDown(int x, int y, Keyframe k) {
		this.isDrawing = true;
		Color c = editor.getFGColor();
		GC gc = new GC(k.getImage());
		gc.setBackground(c);
		gc.fillOval(x-brushSize/2, y-brushSize/2, brushSize, brushSize);
		gc.dispose();
		k.setEmpty(false);
		editor.getCanvasRaster().redraw();
	}

	@Override
	public void handleMouseMove(int x, int y, Keyframe k) {
		if (this.isDrawing) {
			Color c = editor.getFGColor();
			GC gc = new GC(k.getImage());
			gc.setBackground(c);
			gc.fillOval(x-brushSize/2, y-brushSize/2, brushSize, brushSize);
			gc.dispose();
			editor.getCanvasRaster().redraw();
		}
	}

	@Override
	public void handleMouseUp(int x, int y, Keyframe k) {
		this.isDrawing = false;
	}
}
