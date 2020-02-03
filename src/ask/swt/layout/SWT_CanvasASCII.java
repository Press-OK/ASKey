package ask.swt.layout;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

import ask.data.AskeyDocument;
import ask.main.AskeyEditor;

public class SWT_CanvasASCII extends Canvas {
	
	private SWT_CenterPane centerPane = (SWT_CenterPane)(this.getParent().getParent().getParent().getParent());
	private AskeyEditor editor = centerPane.getEditorWindow();
	
	private boolean drawCanvasSkeleton = true;
	private double camZoom = 1.0;
	private int cvsWidth;
	private int cvsHeight;

	private int skeleX = 0, skeleY = 0, skeleW = 0, skeleH = 0;

	private int sampleDepth = 3;
	private boolean antialiasBackground = true;
	private HashMap<Double, String> saturationMap = new HashMap<>();

	private Image renderedLayers;
	private String[][] outputChars;
	private Color[][] outputColors;
	private int charH = 9, charW = 8, numCharsW = 62, numCharsH = 41;
	private String fontName = "Tahoma";
	private Font font = SWTResourceManager.getFont("Tahoma", 9, SWT.NORMAL);

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SWT_CanvasASCII(Composite parent, int style) {
		super(parent, style);
		editor.setCanvasAscii(this);
		
		outputChars = new String[numCharsW][numCharsH];
		outputColors = new Color[numCharsW][numCharsH];
		for (int i = 0; i < outputChars.length; i++) {
			for (int j = 0; j < outputChars[0].length; j++) {
				outputChars[i][j] = "";
				outputColors[i][j] = SWTResourceManager.getColor(0, 0, 0);
			}
		}

		saturationMap.put(0.0, "·");
		saturationMap.put(0.1, "=");
		saturationMap.put(0.2, "I");
		saturationMap.put(0.3, "J");
		saturationMap.put(0.4, "S");
		saturationMap.put(0.5, "X");
		saturationMap.put(0.6, "W");
		saturationMap.put(0.7, "B");
		saturationMap.put(0.8, "%");
		saturationMap.put(0.9, "#");
		
		this.setBackground(SWTResourceManager.getColor(40, 40, 40));
		this.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (editor.getActiveFile() != null) {
					AskeyDocument f = editor.getActiveFile();
					skeleX = (int)(getBounds().width / 2 - cvsWidth * camZoom / 2);
					skeleY = (int)(getBounds().height / 2 - cvsHeight * camZoom / 2);
					skeleW = (int)(cvsWidth * camZoom);
					skeleH = (int)(cvsHeight * camZoom);
					
					// Draw the canvas "skeleton"
					if (drawCanvasSkeleton) {
						e.gc.setBackground(SWTResourceManager.getColor(30, 30, 30));
						e.gc.fillRectangle(skeleX+9, skeleY+5, skeleW, skeleH+4);
						e.gc.setBackground(SWTResourceManager.getColor(0, 0, 0));
						e.gc.fillRectangle(skeleX-1, skeleY-1, skeleW+2, skeleH+2);
						Color c = SWTResourceManager.getColor(f.getAsciiColorBGR(), f.getAsciiColorBGG(), f.getAsciiColorBGB());
						e.gc.setBackground(c);
						e.gc.fillRectangle(skeleX, skeleY, skeleW, skeleH);
					}
	            	e.gc.setFont(font);
					// Generate text and color maps, draw
					for (int y = 0; y < numCharsH; y++) {
						for (int x = 0; x < numCharsW; x++) {
			            	e.gc.setForeground(outputColors[x][y]);
			            	e.gc.drawText(outputChars[x][y], skeleX+x*charW, skeleY+y*charH);
						}
					}
				}
			}
		});
		this.getParent().addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event e) {
				recalculateCanvasSize();
			}
		});
		this.getParent().getHorizontalBar().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				recalculateCanvasSize();
			}
		});
		this.getParent().getVerticalBar().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				recalculateCanvasSize();
			}
		});
	}
	
	public void recalculateCanvasSize() {
		if (editor.getActiveFile() != null) {
			this.cvsWidth = editor.getActiveFile().getRasterWidth();
			this.cvsHeight = editor.getActiveFile().getRasterHeight();
			int x = -this.getParent().getHorizontalBar().getSelection();
			int y = -this.getParent().getVerticalBar().getSelection();
			int w = 0, h = 0;
			if ((int)(cvsWidth * camZoom + 5) > this.getParent().getBounds().width) {
				w += (int)(cvsWidth * camZoom) + 40;
				h -= 16;
			} else {
				w += this.getParent().getBounds().width;
			}
			if ((int)(cvsHeight * camZoom + 5) > this.getParent().getBounds().height) {
				h += (int)(cvsHeight * camZoom) + 40;
				w -= 16;
			} else {
				h += this.getParent().getBounds().height;
			}
			this.setBounds(x, y, w, h);
		}
	}

	public Image getRenderedLayers() {
		return renderedLayers;
	}

	public void setRenderedLayers(Image renderedLayers) {
		if (this.renderedLayers != null) {
			if (!this.renderedLayers.isDisposed()) {
				this.renderedLayers.dispose();
			}
		}
		this.renderedLayers = renderedLayers;
		this.generateCharArray();
		this.redraw();
	}
	
	public void updateDisplaySettingsFromEditor() {
		charH = editor.getActiveFile().getCharH();
		charW = editor.getActiveFile().getCharW();
		numCharsW = editor.getActiveFile().getAsciiWidth();
		numCharsH = editor.getActiveFile().getAsciiHeight();
		fontName = editor.getActiveFile().getFontname();
		font = SWTResourceManager.getFont(fontName, charH, SWT.NORMAL);
		charH = charH + charH / 3;
		outputChars = new String[numCharsW][numCharsH];
		outputColors = new Color[numCharsW][numCharsH];
		for (int i = 0; i < outputChars.length; i++) {
			for (int j = 0; j < outputChars[0].length; j++) {
				outputChars[i][j] = "";
				outputColors[i][j] = SWTResourceManager.getColor(0, 0, 0);
			}
		}
	}
	
	public void generateCharArray() {
		if (renderedLayers != null && !renderedLayers.isDisposed()) {
	    	Image tempImg = new Image(getDisplay(), 1, 1);
	    	ImageData tempImgData;
	    	PaletteData palette;
	    	GC tempGC = new GC(renderedLayers);
			for (int y = 0; y < numCharsH; y++) {
				for (int x = 0; x < numCharsW; x++) {	            	
	            	String c;
	            	float sampleStepW = charW / (float)(sampleDepth);
	            	float sampleStepH = charH / (float)(sampleDepth);
	            	float sampleModW = charW / (float)(sampleDepth*2);
	            	float sampleModH = charH / (float)(sampleDepth*2);
	    	    	RGB bg = new RGB(editor.getActiveFile().getRasterColorBGR(), editor.getActiveFile().getRasterColorBGG(), editor.getActiveFile().getRasterColorBGB());
	    	    	RGB rgb = null;
	    	    	float saturation = 0.0f;
	            	for (int cy = 0; cy < sampleDepth; cy++) {
		            	for (int cx = 0; cx < sampleDepth; cx++) {
							tempGC.copyArea(tempImg, (int)(Math.round(x*charW+cx*sampleStepW+sampleModW)), (int)(Math.round(y*charH+cy*sampleStepH+sampleModH)));
			            	tempImgData = tempImg.getImageData();
			            	int pixelValue = tempImgData.getPixel(0,0);
			            	palette = tempImgData.palette;
			            	RGB pixel = palette.getRGB(pixelValue);
			            	if (rgb == null) {
			            		if (!antialiasBackground) {
				            		if (pixel.red != bg.red || pixel.green != bg.green || pixel.blue != bg.blue) {
				            			rgb = pixel;
				            		}
			            		} else {
			            			rgb = pixel;
			            		}
			            	} else {
			            		if (!antialiasBackground) {
				            		if (pixel.red != bg.red || pixel.green != bg.green || pixel.blue != bg.blue) {
				            			saturation += (Math.abs(pixel.red - rgb.red) + Math.abs(pixel.green - rgb.green) + Math.abs(pixel.blue - rgb.blue)) / 765.0f;
				            			rgb = averageColors(rgb, pixel);
				            		}
			            		} else {
			            			saturation += (Math.abs(pixel.red - rgb.red) + Math.abs(pixel.green - rgb.green) + Math.abs(pixel.blue - rgb.blue)) / 765.0f;
			            			rgb = averageColors(rgb, pixel);
			            		}
				            }
		            	}
	            	}
	            	if (rgb == null) {
	            		rgb = bg;
	            	}
	            	if (saturation >= 1.0f) saturation = 1.0f;
	            	saturation = 1.0f - saturation;
	            	String oc = "";
	            	Double highest = 0.0;
	            	for (Double d : saturationMap.keySet()) {
	            		if (saturation >= d && saturation >= highest) {
	            			highest = (double) saturation;
	            			oc = saturationMap.get(d);
	            		}
	            	}
	            	outputChars[x][y] = oc;
	            	outputColors[x][y] = SWTResourceManager.getColor(rgb);
				}
			}
			tempGC.dispose();
			tempImg.dispose();
		}
	}
	
	public RGB averageColors(RGB c1, RGB c2) {
		int r = (int)(Math.ceil((c1.red + c2.red) / 2.0));
		int g = (int)(Math.ceil((c1.green + c2.green) / 2));
		int b = (int)(Math.ceil((c1.blue + c2.blue) / 2));
		return new RGB(r, g, b);
	}
}