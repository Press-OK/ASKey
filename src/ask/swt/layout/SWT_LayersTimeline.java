package ask.swt.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.SWTResourceManager;

import ask.data.AskeyDocument;
import ask.data.Keyframe;
import ask.data.Layer;
import ask.main.AskeyEditor;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseMoveListener;

public class SWT_LayersTimeline extends SashForm {
	
	private SWT_CenterPane centerPane = (SWT_CenterPane)(this.getParent().getParent());
	private AskeyEditor editor = centerPane.getEditorWindow();

	private ArrayList<Layer> allLayers = new ArrayList<Layer>();
	private int maxNumLayers = 20;
	private int timelineFrameHeight = 25;
	private int timelineFrameWidth = 11;
	private int timelineMaxFrames = 2000;
	private int timelineMaxWidth = timelineMaxFrames * timelineFrameWidth;
	private int timelinePlayheadPosition = -1;

	private HashMap<Integer, HashSet<Integer>> selectedMultiFrames = new HashMap<>();
	private int selectedLayer = -1;
	private int selectedFrame = -1;
	private int hoveredLayer = -1;
	private int hoveredFrame = -1;
	
	private Cursor cursor;
	private boolean mouseDownOnTicks = false;
	private boolean mouseDownOnTimeline_Keyframe = false;
	private boolean mouseDownOnTimeline_NonKeyframe = false;
	
	private Composite cmpLayersList;
	private Canvas cvsTimeline;
	private Composite cmpLayersHeader;
	private ScrolledComposite scrlLayers;
	private ScrolledComposite scrlTimeline;
	private Canvas cvsTicks;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SWT_LayersTimeline(Composite parent, int style) {
		super(parent, SWT.NONE);
		editor.setTimeline(this);
		this.setSashWidth(5);
		this.setBackground(SWTResourceManager.getColor(55, 55, 55));
		this.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				alignLayersWithTimeline();
			}
		});
		
		Composite cmpLayers = new Composite(this, SWT.NONE);
		cmpLayers.setBackground(SWTResourceManager.getColor(40, 40, 40));
		GridLayout gl_cmpLayers = new GridLayout(1, false);
		gl_cmpLayers.marginWidth = 0;
		gl_cmpLayers.marginHeight = 0;
		gl_cmpLayers.verticalSpacing = 0;
		cmpLayers.setLayout(gl_cmpLayers);		
		
			cmpLayersHeader = new Composite(cmpLayers, SWT.NONE);
			cmpLayersHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			GridLayout gl_cmpLayersHeader = new GridLayout(2, false);
			gl_cmpLayersHeader.marginHeight = 0;
			gl_cmpLayersHeader.marginWidth = 0;
			cmpLayersHeader.setLayout(gl_cmpLayersHeader);
			cmpLayersHeader.setBackground(SWTResourceManager.getColor(40, 40, 40));
			
				CLabel lblLayersHeader = new CLabel(cmpLayersHeader, SWT.NONE);
				lblLayersHeader.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
				lblLayersHeader.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
				lblLayersHeader.setForeground(SWTResourceManager.getColor(255, 255, 255));
				lblLayersHeader.setBackground(SWTResourceManager.getColor(40, 40, 40));
				lblLayersHeader.setText(" » Layers");
					
				CLabel lblNewLayer = new CLabel(cmpLayersHeader, SWT.CENTER);
				GridData gd_lblNewLayer = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				gd_lblNewLayer.heightHint = 25;
				gd_lblNewLayer.widthHint = 25;
				lblNewLayer.setLayoutData(gd_lblNewLayer);
				lblNewLayer.setLayoutData(gd_lblNewLayer);
				lblNewLayer.setText("+");
				lblNewLayer.setForeground(SWTResourceManager.getColor(255, 255, 255));
				lblNewLayer.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.NORMAL));
				lblNewLayer.setBackground(SWTResourceManager.getColor(40, 40, 40));
				lblNewLayer.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						lblNewLayer.setForeground(SWTResourceManager.getColor(100, 225, 100));
						lblNewLayer.setBackground(SWTResourceManager.getColor(35, 35, 35));
					}
					@Override
					public void mouseUp(MouseEvent e) {
						lblNewLayer.setForeground(SWTResourceManager.getColor(100, 255, 100));
						lblNewLayer.setBackground(SWTResourceManager.getColor(45, 45, 45));
						createNewLayer(false);
					}
				});
				lblNewLayer.addMouseTrackListener(new MouseTrackAdapter() {
					@Override
					public void mouseEnter(MouseEvent e) {
						lblNewLayer.setForeground(SWTResourceManager.getColor(100, 255, 100));
						lblNewLayer.setBackground(SWTResourceManager.getColor(45, 45, 45));
					}
					@Override
					public void mouseExit(MouseEvent e) {
						lblNewLayer.setForeground(SWTResourceManager.getColor(255, 255, 255));
						lblNewLayer.setBackground(SWTResourceManager.getColor(40, 40, 40));
					}
				});
			
			scrlLayers = new ScrolledComposite(cmpLayers, SWT.NONE);
			scrlLayers.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			scrlLayers.setExpandHorizontal(true);
			scrlLayers.setExpandVertical(false);
			scrlLayers.setBackground(SWTResourceManager.getColor(20, 20, 20));
			
				cmpLayersList = new Composite(scrlLayers, SWT.NONE);
				GridLayout gl_cmpLayersList = new GridLayout(1, false);
				gl_cmpLayersList.marginTop = 0;
				gl_cmpLayersList.marginRight = 0;
				gl_cmpLayersList.marginLeft = 0;
				gl_cmpLayersList.marginBottom = 0;
				gl_cmpLayersList.verticalSpacing = 0;
				gl_cmpLayersList.marginWidth = 0;
				gl_cmpLayersList.marginHeight = 0;
				cmpLayersList.setLayout(gl_cmpLayersList);
				cmpLayersList.setBackground(SWTResourceManager.getColor(20, 20, 20));
				Layer l = createNewLayer(false);
				l.setName("");
				scrlLayers.setContent(cmpLayersList);
				
		Composite cmpTimeline = new Composite(this, SWT.NONE);
		cmpTimeline.setBackground(SWTResourceManager.getColor(255, 40, 40));
		GridLayout gl_cmpTimeline = new GridLayout(1, false);
		gl_cmpTimeline.marginWidth = 0;
		gl_cmpTimeline.marginHeight = 0;
		gl_cmpTimeline.verticalSpacing = 0;
		cmpTimeline.setLayout(gl_cmpTimeline);
		
			ScrolledComposite scrlTicks = new ScrolledComposite(cmpTimeline, SWT.NONE);
			scrlTicks.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
			scrlTicks.setExpandHorizontal(false);
			scrlTicks.setExpandVertical(false);
			scrlTicks.setBackground(SWTResourceManager.getColor(40, 40, 40));
			scrlTicks.addControlListener(new ControlAdapter() {
				@Override
				public void controlResized(ControlEvent e) {
					scrlTicks.setBounds(scrlTicks.getBounds().x, scrlTicks.getBounds().y, scrlTicks.getBounds().width, 25);
				}
			});	
			
				cvsTicks = new Canvas(scrlTicks, SWT.DOUBLE_BUFFERED);
				scrlTicks.setContent(cvsTicks);
				cvsTicks.setBackground(SWTResourceManager.getColor(40, 40, 40));
				cvsTicks.setSize((int)(Math.floor(editor.getShell().getDisplay().getClientArea().width * 0.75)), 25);
				cvsTicks.addPaintListener(new PaintListener() {
					@Override
					public void paintControl(PaintEvent e) {
						redrawTicks(e);
					}
				});
				cvsTicks.addMouseTrackListener(new MouseTrackAdapter() {
					@Override
					public void mouseEnter(MouseEvent e) {
						if (cursor != null) cursor.dispose();
						cursor = new Cursor(editor.getShell().getDisplay(), SWT.CURSOR_HAND);
						cvsTicks.setCursor(cursor);
					}
					@Override
					public void mouseExit(MouseEvent e) {
						if (cursor != null) cursor.dispose();
						cursor = new Cursor(editor.getShell().getDisplay(), SWT.CURSOR_ARROW);
						cvsTicks.setCursor(cursor);
					}
				});
				cvsTicks.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						mouseDownOnTicks = true;
						movePlayhead(e.x / timelineFrameWidth);
						cvsTicks.redraw();
						cvsTimeline.redraw();
					}
					@Override
					public void mouseUp(MouseEvent e) {
						mouseDownOnTicks = false;
					}
				});
				cvsTicks.addMouseMoveListener(new MouseMoveListener() {
					public void mouseMove(MouseEvent e) {
						if (mouseDownOnTicks) {
							int ph = timelinePlayheadPosition;
							int nf = e.x / timelineFrameWidth;
							if (nf >= 0 && nf <= timelineMaxFrames) {
								movePlayhead(e.x / timelineFrameWidth);
								if (timelinePlayheadPosition != ph) {
									cvsTicks.redraw();
									cvsTimeline.redraw();
								}
							}
						}
					}
				});
				editor.addCanvasToRedrawSet(cvsTicks);
				
			scrlTimeline = new ScrolledComposite(cmpTimeline, SWT.H_SCROLL | SWT.V_SCROLL);
			scrlTimeline.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			scrlTimeline.setExpandHorizontal(true);
			scrlTimeline.setExpandVertical(true);
			scrlTimeline.setBackground(SWTResourceManager.getColor(20, 20, 20));
			
				cvsTimeline = new Canvas(scrlTimeline, SWT.DOUBLE_BUFFERED);
				scrlTimeline.setContent(cvsTimeline);
				cvsTimeline.setBackground(SWTResourceManager.getColor(25, 25, 25));
				cvsTimeline.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						scrlTimeline.setExpandHorizontal(false);
						scrlTimeline.setExpandVertical(false);
						cvsTimeline.setBounds(0, 0, (int)(Math.floor(editor.getShell().getDisplay().getClientArea().width * 0.75)), scrlLayers.getBounds().height-16);
						cvsTimeline.removePaintListener(this);
					}
				});
				scrlTimeline.getVerticalBar().addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						alignLayersWithTimeline();
					}
				});
				scrlTimeline.getHorizontalBar().addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if (scrlTimeline.getHorizontalBar().getMaximum() < timelineMaxWidth) {
							ScrollBar h = scrlTimeline.getHorizontalBar();
							if (h.getSelection() + h.getSize().x == h.getMaximum()) {
								cvsTimeline.setBounds(cvsTimeline.getBounds().x, cvsTimeline.getBounds().y, cvsTimeline.getBounds().width + 200, cvsTimeline.getBounds().height);
							}
							h.setIncrement(h.getSize().x / 15);
						}
						Rectangle b = cvsTimeline.getBounds();
						cvsTicks.setBounds(b.x, cvsTicks.getBounds().y, b.width, 25);
						cmpLayers.redraw();
					}
				});
				cvsTimeline.addPaintListener(new PaintListener() {
					public void paintControl(PaintEvent e) {
						redrawTimeline(e);
					}
				});

				cvsTimeline.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						handleTimelineMouseDown(e);
					}
					@Override
					public void mouseUp(MouseEvent e) {
						handleTimelineMouseUp(e);
					}
				});
				cvsTimeline.addMouseMoveListener(new MouseMoveListener() {
					public void mouseMove(MouseEvent e) {
						handleTimelineMouseMove(e);
					}
				});
				editor.addCanvasToRedrawSet(cvsTimeline);
				
		// The only way to get the scrollwheel to focus the timeline from anywhere on this control
		for (Control c : this.getChildren()) {
			c.addMouseTrackListener(new MouseTrackAdapter() {
				@Override
				public void mouseEnter(MouseEvent e) {
					scrlTimeline.setFocus();
				}
			});
			if (c instanceof Composite) {
				Composite c1 = (Composite)c;
				for (Control c2 : c1.getChildren()) {
					c2.addMouseTrackListener(new MouseTrackAdapter() {
						@Override
						public void mouseEnter(MouseEvent e) {
							scrlTimeline.setFocus();
						}
					});
					if (c2 instanceof Composite) {
						Composite c3 = (Composite)c2;
						for (Control c4 : c3.getChildren()) {
							c4.addMouseTrackListener(new MouseTrackAdapter() {
								@Override
								public void mouseEnter(MouseEvent e) {
									scrlTimeline.setFocus();
								}
							});
						}
					}
				}
			}
		}
		this.setWeights(new int[] {1, 5});
	}

	public void initialize() {
		for (Layer l : allLayers) {
			l.dispose();
		}
		selectedMultiFrames = new HashMap<Integer, HashSet<Integer>>();
		allLayers = new ArrayList<Layer>();
		createNewLayer(true);
		timelinePlayheadPosition = 0;
		hoveredLayer = -1;
		hoveredFrame = -1;
		selectedLayer = 0;
		selectedFrame = 0;
		cvsTicks.redraw();
		cvsTimeline.redraw();
	}
	
	private Keyframe createNewKeyframe(Layer layer, int start, int end) {
		AskeyDocument f = editor.getActiveFile();
		Keyframe k = new Keyframe(start, end);
		Image imgBase = new Image(getDisplay(), editor.getActiveFile().getRasterWidth(), editor.getActiveFile().getRasterHeight());
		GC tempGC = new GC(imgBase);
		tempGC.setBackground(SWTResourceManager.getColor(0, 0, 1));
		tempGC.fillRectangle(0, 0, editor.getActiveFile().getRasterWidth(), editor.getActiveFile().getRasterHeight());
		ImageData id = imgBase.getImageData();
		id.transparentPixel = id.palette.getPixel(new RGB(0, 0, 1));
		Image imgNew = new Image(getDisplay(), id);
		imgBase.dispose();
		tempGC.dispose();
		k.setImage(imgNew);
		layer.addKeyframe(k);
		return k;
	}

	private Layer createNewLayer(boolean withKeyframe) {
		if (allLayers.size() < maxNumLayers) {
			Layer l = new Layer(cmpLayersList, SWT.NONE);
			l.setName("Layer "+(allLayers.size()+1));
			if (withKeyframe) {
				if (editor.getActiveFile() != null) {
					createNewKeyframe(l, 0, 0);
				}
			}
			allLayers.add(0, l);
			if (allLayers.size() > 1) {
				l.moveAbove(allLayers.get(1));
			}
			if (selectedLayer >= 0) {
				selectedLayer += 1;
			}
			
			l.addMouseTrackListener(new MouseTrackAdapter() {
				@Override
				public void mouseEnter(MouseEvent e) {
					scrlTimeline.setFocus();
				}
			});
			for (Control c : l.getChildren()) {
				c.addMouseTrackListener(new MouseTrackAdapter() {
					@Override
					public void mouseEnter(MouseEvent e) {
						scrlTimeline.setFocus();
					}
				});
			}
			
			if (cvsTimeline != null) {
				cvsTimeline.redraw();
			}
			Rectangle b = cmpLayersList.getBounds();
			cmpLayersList.setBounds(b.x, b.y, b.width, b.height+25);
			selectedMultiFrames = new HashMap<Integer, HashSet<Integer>>();
			return l;
		} else {
			return null;
		}
	}
	
	private void alignLayersWithTimeline() {
		Rectangle b = cvsTimeline.getBounds();
		cmpLayersList.setBounds(b.x, b.y, scrlLayers.getBounds().width, b.height);
	}
	
	private void handleTimelineMouseDown(MouseEvent e) {
		int layer = e.y / timelineFrameHeight;
		int frame = e.x / timelineFrameWidth;

		if (e.button == 1) {
			if ((e.stateMask & SWT.CTRL) != 0) {
				if (selectedMultiFrames.containsKey(layer)) {
					selectedMultiFrames.get(layer).add(frame);
				} else {
					HashSet<Integer> hs = new HashSet<>();
					hs.add(frame);
					selectedMultiFrames.put(layer, hs);
				}
				if (selectedMultiFrames.containsKey(selectedLayer)) {
					selectedMultiFrames.get(selectedLayer).add(selectedFrame);
				} else {
					HashSet<Integer> hs = new HashSet<>();
					hs.add(selectedFrame);
					selectedMultiFrames.put(selectedLayer, hs);
				}
			} else {
				selectedMultiFrames = new HashMap<Integer, HashSet<Integer>>();
			}
			
			selectedLayer = layer;
			selectedFrame = frame;
			boolean isKeyframe = false;
			for (Keyframe f : allLayers.get(layer).getKeyframes()) {
				if (f.getKeyframeStart() == frame) {
					isKeyframe = true;
					break;
				}
			}
			if (isKeyframe) {
				mouseDownOnTimeline_Keyframe = true;
				mouseDownOnTimeline_NonKeyframe = false;
			} else {
				mouseDownOnTimeline_Keyframe = false;
				mouseDownOnTimeline_NonKeyframe = true;
			}
			movePlayhead(frame);
			cvsTimeline.redraw();
			cvsTicks.redraw();
		}
	}

	private void handleTimelineMouseUp(MouseEvent e) {
		if (e.button == 1) {
			int layer = e.y / timelineFrameHeight;
			int frame = e.x / timelineFrameWidth;
			if (layer >= allLayers.size()) layer = allLayers.size()-1;
			if (layer < 0) layer = 0;
			if (frame >= timelineMaxFrames) frame = timelineMaxFrames;
			if (frame < 0) frame = 0;
			mouseDownOnTimeline_Keyframe = false;
			mouseDownOnTimeline_NonKeyframe = false;
			cvsTimeline.redraw();
		}
	}

	private void handleTimelineMouseMove(MouseEvent e) {
		if ((e.stateMask & SWT.CTRL) == 0) {
			if (mouseDownOnTimeline_NonKeyframe || mouseDownOnTimeline_Keyframe) {
				int layer = e.y / timelineFrameHeight;
				int frame = e.x / timelineFrameWidth;
				if (layer >= allLayers.size()) layer = allLayers.size()-1;
				if (layer < 0) layer = 0;
				if (frame >= timelineMaxFrames) frame = timelineMaxFrames;
				if (frame < 0) frame = 0;
				if (frame != hoveredFrame || layer != hoveredLayer) {
					if (mouseDownOnTimeline_NonKeyframe) {
						selectedMultiFrames = new HashMap<Integer, HashSet<Integer>>();
						if (selectedLayer < layer) {
							for (int i = selectedLayer; i <= layer; i++) {
								HashSet<Integer> selectedFrames = new HashSet<>();
								if (selectedFrame < frame) {
									for (int j = selectedFrame; j <= frame; j++) {
										selectedFrames.add(j);
									}
								} else {
									for (int j = frame; j <= selectedFrame; j++) {
										selectedFrames.add(j);
									}
								}
								selectedMultiFrames.put(i, selectedFrames);
							}
						} else {
							for (int i = layer; i <= selectedLayer; i++) {
								HashSet<Integer> selectedFrames = new HashSet<>();
								if (selectedFrame < frame) {
									for (int j = selectedFrame; j <= frame; j++) {
										selectedFrames.add(j);
									}
								} else {
									for (int j = frame; j <= selectedFrame; j++) {
										selectedFrames.add(j);
									}
								}
								selectedMultiFrames.put(i, selectedFrames);
							}
						}
					}
					hoveredLayer = layer;
					hoveredFrame = frame;
					movePlayhead(frame);
					cvsTicks.redraw();
					cvsTimeline.redraw();
				}
			}
		}
	}
	
	private void movePlayhead(int frame) {
		timelinePlayheadPosition = frame;
		editor.redrawCanvases();
	}
	
	private void redrawTicks(PaintEvent e) {
		// draw the header
		e.gc.setForeground(SWTResourceManager.getColor(255, 255, 255));
		e.gc.setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		e.gc.drawText("  » Timeline", 0, 4, true);
		
		// draw the ticks and frame numbers
		e.gc.setFont(SWTResourceManager.getFont("Tahoma", 7, SWT.NORMAL));
		e.gc.setForeground(SWTResourceManager.getColor(100, 100, 100));
		for (int i = 0; i < cvsTicks.getBounds().width / timelineFrameWidth; i++) {
			int x = i * timelineFrameWidth;
			if (x >= 80) {
				int y = i % 5 == 0 ? 20 : 23;
				e.gc.drawLine(x, y, x, 25);
				if (i % 5 == 0) {
					e.gc.drawText(String.valueOf(i), x+2, y-10, true);
				}
			}
		}
		
		// draw playhead (just the head portion)
		if (timelinePlayheadPosition >= 0) {
			int ph = timelinePlayheadPosition;
			e.gc.setForeground(SWTResourceManager.getColor(175, 0, 0));
			e.gc.setBackground(SWTResourceManager.getColor(175, 0, 0));
			e.gc.fillArc(timelineFrameWidth/2+timelineFrameWidth*ph-2, 20, 5, 5, 180, 180);
			e.gc.fillRectangle(timelineFrameWidth/2+timelineFrameWidth*ph-2, 13, 5, 9);
		}
	}
	
	private void redrawTimeline(PaintEvent e) {
		// resize the canvas vertically to fit contents
		cvsTimeline.setBounds(cvsTimeline.getBounds().x, cvsTimeline.getBounds().y, cvsTimeline.getBounds().width, timelineFrameHeight*allLayers.size());
		
		// draw all frame backgrounds
		for (Layer l : allLayers) {
			int li = allLayers.indexOf(l);
			int ymod = timelineFrameHeight*li;
			
			// empty frames
			e.gc.setForeground(SWTResourceManager.getColor(30, 30, 30));
			e.gc.setBackground(SWTResourceManager.getColor(25, 25, 25));
			for (int i = 0; i < cvsTimeline.getBounds().width / timelineFrameWidth; i++) {
				e.gc.fillRectangle(i*timelineFrameWidth, ymod, timelineFrameWidth, 25);
				e.gc.drawRectangle(i*timelineFrameWidth, ymod, timelineFrameWidth, 24);
			}
			
			// keyframes
			for (Keyframe k : l.getKeyframes()) {
				int keyframeStart = k.getKeyframeStart();
				int keyframeEnd = k.getKeyframeEnd();
				for (int i = keyframeStart; i <= keyframeEnd; i++) {
					e.gc.setForeground(SWTResourceManager.getColor(30, 30, 30));
					e.gc.setBackground(SWTResourceManager.getColor(85, 85, 85));
					e.gc.fillRectangle(i*timelineFrameWidth, ymod, timelineFrameWidth, 25);
					e.gc.drawRectangle(i*timelineFrameWidth, ymod, timelineFrameWidth, 24);
					if (i == keyframeEnd && keyframeStart != keyframeEnd) {
						e.gc.setBackground(SWTResourceManager.getColor(80, 80, 80));
						e.gc.fillRectangle(i*timelineFrameWidth-1, 1+ymod, 2, 23);
					} else {
						e.gc.setBackground(SWTResourceManager.getColor(80, 80, 80));
						e.gc.fillRectangle(i*timelineFrameWidth-1, 1+ymod, 2, 23);
					}
				}
			}
		}
		
		// draw box-selected frames if there is a multi-selection
		if (!selectedMultiFrames.isEmpty()) {
			e.gc.setForeground(SWTResourceManager.getColor(90, 90, 200));
			e.gc.setBackground(SWTResourceManager.getColor(90, 90, 200));
			for (Integer layer : selectedMultiFrames.keySet()) {
				for (Integer frame : selectedMultiFrames.get(layer)) {
					for (Keyframe f : allLayers.get(layer).getKeyframes()) {
						if (frame >= f.getKeyframeStart() && frame <= f.getKeyframeEnd()) {
							e.gc.setForeground(SWTResourceManager.getColor(140, 140, 255));
							e.gc.setBackground(SWTResourceManager.getColor(140, 140, 255));
							break;
						}
					}
					e.gc.fillRectangle(timelineFrameWidth*frame, timelineFrameHeight*layer, timelineFrameWidth, timelineFrameHeight);
					e.gc.drawRectangle(timelineFrameWidth*frame, timelineFrameHeight*layer, timelineFrameWidth-1, timelineFrameHeight-1);
					e.gc.setForeground(SWTResourceManager.getColor(90, 90, 200));
					e.gc.setBackground(SWTResourceManager.getColor(90, 90, 200));
				}
			}
		}
		
		// draw the main selected frame
		if (selectedLayer >= 0 && selectedFrame >= 0 && selectedLayer < allLayers.size()) {
			e.gc.setForeground(SWTResourceManager.getColor(0, 40, 150));
			e.gc.setBackground(SWTResourceManager.getColor(180, 190, 255));
			e.gc.fillRectangle(timelineFrameWidth*selectedFrame, timelineFrameHeight*selectedLayer, timelineFrameWidth, timelineFrameHeight);
			e.gc.drawRectangle(timelineFrameWidth*selectedFrame, timelineFrameHeight*selectedLayer, timelineFrameWidth-1, timelineFrameHeight-1);
		}
			
		// draw all frame foregrounds (keyframes, arrows etc.)
		for (Layer l : allLayers) {
			int li = allLayers.indexOf(l);
			int ymod = timelineFrameHeight*li;

			for (Keyframe k : l.getKeyframes()) {
				if (!k.isEmpty()) {
					int keyframeStart = k.getKeyframeStart();
					int keyframeEnd = k.getKeyframeEnd();
					for (int i = keyframeStart; i <= keyframeEnd; i++) {
						if (i == keyframeStart) {
							e.gc.setBackground(SWTResourceManager.getColor(0, 0, 0));
							e.gc.fillOval(i*timelineFrameWidth+timelineFrameWidth/2-3, 15+ymod, 7, 7);
							if (i != keyframeEnd) {
								e.gc.setBackground(SWTResourceManager.getColor(0, 0, 0));
								e.gc.fillRectangle(i*timelineFrameWidth+timelineFrameWidth/2, 18+ymod, timelineFrameWidth/2, 1);
							}
						} else if (i == keyframeEnd && keyframeStart != keyframeEnd) {
							e.gc.setBackground(SWTResourceManager.getColor(0, 0, 0));
							e.gc.fillArc(i*timelineFrameWidth+timelineFrameWidth-6, 16+ymod, 5, 5, 270, 180);
							e.gc.fillRectangle(i*timelineFrameWidth-1, 18+ymod, timelineFrameWidth-3, 1);
						} else {
							e.gc.setBackground(SWTResourceManager.getColor(0, 0, 0));
							e.gc.fillRectangle(i*timelineFrameWidth-1, 18+ymod, timelineFrameWidth, 1);
						}
					}
				}
			}
			
			// draw playhead (just the timeline portion)
			if (timelinePlayheadPosition >= 0) {
				int ph = timelinePlayheadPosition;
				e.gc.setForeground(SWTResourceManager.getColor(175, 0, 0));
				e.gc.setLineStyle(SWT.LINE_DOT);
				e.gc.drawLine(timelineFrameWidth/2+timelineFrameWidth*ph, 0, timelineFrameWidth/2+timelineFrameWidth*ph, cvsTimeline.getBounds().height);
				e.gc.setLineStyle(SWT.LINE_SOLID);
			}
		}
	}

	public Keyframe getActiveKeyframe() {
		Keyframe rk = null;
		if (this.selectedLayer >= 0 && this.selectedLayer < this.allLayers.size()) {
			Layer l = this.allLayers.get(this.selectedLayer);
			for (Keyframe k : l.getKeyframes()) {
				if (this.selectedFrame >= k.getKeyframeStart() || this.selectedFrame <= k.getKeyframeEnd()) {
					rk = k;
					break;
				}
			}
		}
		return rk;
	}

	public ArrayList<Layer> getAllLayers() {
		return this.allLayers;
	}
	
	public int getSelectedFrame() {
		return this.selectedFrame;
	}
	
	public int getPlayheadPosition() {
		return this.timelinePlayheadPosition;
	}
}