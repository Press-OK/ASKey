package ask.swt.layout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

import ask.data.AskeyDocument;
import ask.data.Keyframe;
import ask.data.Layer;
import ask.data.RasterTool;
import ask.main.AskeyEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseMoveListener;

public class SWT_CanvasRaster extends Canvas {
	
	private SWT_CenterPane centerPane = (SWT_CenterPane)(this.getParent().getParent().getParent().getParent());
	private AskeyEditor editor = centerPane.getEditorWindow();
	
	private boolean drawCanvasSkeleton = true;
	private double camZoom = 1.0;
	private int cvsWidth;
	private int cvsHeight;
	private Image renderedLayers;
	
	private SWT_LayersTimeline timeline = null;
	
	private int skeleX = 0, skeleY = 0, skeleW = 0, skeleH = 0;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SWT_CanvasRaster(Composite parent, int style) {
		super(parent, style);
		editor.setCanvasRaster(this);
		
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
					
					// Draw the "skeleton" and background of the canvas
					if (drawCanvasSkeleton) {
						e.gc.setBackground(SWTResourceManager.getColor(30, 30, 30));
						e.gc.fillRectangle(skeleX+9, skeleY+5, skeleW, skeleH+4);
						e.gc.setBackground(SWTResourceManager.getColor(0, 0, 0));
						e.gc.fillRectangle(skeleX-1, skeleY-1, skeleW+2, skeleH+2);
						Color c = SWTResourceManager.getColor(f.getRasterColorBGR(), f.getRasterColorBGG(), f.getRasterColorBGB());
						e.gc.setBackground(c);
						e.gc.fillRectangle(skeleX, skeleY, skeleW, skeleH);
					}
					
					// For all VISIBLE layers, if they contain a keyframe at the playhead position, draw the image from that keyframe
					if (timeline != null) {
						if (renderedLayers != null) {
							if (!renderedLayers.isDisposed()) {
								renderedLayers.dispose();
							}
						}
						renderedLayers = new Image(getDisplay(), cvsWidth, cvsHeight);
						GC asciiGCRender = new GC(renderedLayers);
						asciiGCRender.setBackground(SWTResourceManager.getColor(editor.getActiveFile().getAsciiColorBGR(),
								editor.getActiveFile().getAsciiColorBGG(),
								editor.getActiveFile().getAsciiColorBGB()));
						asciiGCRender.fillRectangle(0, 0, cvsWidth, cvsHeight);
						int playheadPosition = timeline.getPlayheadPosition();
						for (Layer l : timeline.getAllLayers()) {
							if (!l.isHidden()) {
								for (Keyframe k : l.getKeyframes()) {
									if (playheadPosition >= k.getKeyframeStart() && playheadPosition <= k.getKeyframeEnd()) {
										e.gc.drawImage(k.getImage(), skeleX, skeleY);
										asciiGCRender.drawImage(k.getImage(), 0, 0);
										break;
									}
								}
							}
						}
						asciiGCRender.dispose();
					} else {
						timeline = editor.getTimeline();
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
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Keyframe k = editor.getActiveKeyframe();
				if (k == null) {
					System.out.println("keyframe is null");
				} else {
					RasterTool rt = editor.getSelectedTool();
					if (rt == null) {
						System.out.println("selected tool is null D");
					} else {
						rt.handleMouseDown(e.x - skeleX, e.y - skeleY, k);
					}
				}
			}
			@Override
			public void mouseUp(MouseEvent e) {
				Keyframe k = editor.getActiveKeyframe();
				if (k == null) {
					System.out.println("keyframe is null");
				} else {
					RasterTool rt = editor.getSelectedTool();
					if (rt == null) {
						System.out.println("selected tool is null U");
					} else {
						rt.handleMouseUp(e.x - skeleX, e.y - skeleY, k);
						editor.getCanvasAscii().setRenderedLayers(renderedLayers);
					}
				}
			}
		});
		this.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				Keyframe k = editor.getActiveKeyframe();
				RasterTool rt = editor.getSelectedTool();
				if (k != null && rt != null) {
					if (rt != null) {
						rt.handleMouseMove(e.x - skeleX, e.y - skeleY, k);
					}
				}
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
}