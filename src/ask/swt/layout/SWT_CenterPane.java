package ask.swt.layout;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import ask.main.AskeyEditor;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseEvent;

public class SWT_CenterPane extends Composite {
	
	private AskeyEditor editor;
	private double timelineHeight = 0.16;
	private SWT_CanvasRaster cvsRasterCanvas;
	private SWT_CanvasASCII cvsAsciiCanvas;
	private SWT_LayersTimeline sashLayersTimeline;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SWT_CenterPane(Shell shell, int style, AskeyEditor editor) {
		super (shell, style);
		this.editor = editor;
		
		GridLayout gd_Center = new GridLayout(1, false);
		gd_Center.marginHeight = 2;
		gd_Center.marginWidth = 2;
		setLayout(gd_Center);
		this.setBackground(SWTResourceManager.getColor(55, 55, 55));
		
		SashForm verticalSash = new SashForm(this, SWT.VERTICAL);
		verticalSash.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		verticalSash.setSashWidth(5);
		verticalSash.setBackground(SWTResourceManager.getColor(55, 55, 55));
		
		this.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				int[] vWeight = {(int) (getBounds().height * (1-timelineHeight)), (int) (getBounds().height * timelineHeight)};
				verticalSash.setWeights(vWeight);
				removePaintListener(this);
			}
		});
	
			SashForm sashEditorLR = new SashForm(verticalSash, SWT.HORIZONTAL);
			sashEditorLR.setSashWidth(5);
			sashEditorLR.setBackground(SWTResourceManager.getColor(55, 55, 55));
			
				ScrolledComposite scrlCompLeft = new ScrolledComposite(sashEditorLR, SWT.H_SCROLL | SWT.V_SCROLL);
				scrlCompLeft.setExpandHorizontal(true);
				scrlCompLeft.setExpandVertical(true);
				scrlCompLeft.setBackground(SWTResourceManager.getColor(55, 55, 55));
			
					cvsRasterCanvas = new SWT_CanvasRaster(scrlCompLeft, SWT.DOUBLE_BUFFERED);
					scrlCompLeft.setContent(cvsRasterCanvas);
					scrlCompLeft.setMinSize(cvsRasterCanvas.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					cvsRasterCanvas.addMouseTrackListener(new MouseTrackAdapter() {
						@Override
						public void mouseEnter(MouseEvent e) {
							scrlCompLeft.setFocus();
						}
					});
					editor.addCanvasToRedrawSet(cvsRasterCanvas);
					
				ScrolledComposite scrlCompRight = new ScrolledComposite(sashEditorLR, SWT.H_SCROLL | SWT.V_SCROLL);
				scrlCompRight.setExpandHorizontal(true);
				scrlCompRight.setExpandVertical(true);
				scrlCompRight.setBackground(SWTResourceManager.getColor(55, 55, 55));
			
					cvsAsciiCanvas = new SWT_CanvasASCII(scrlCompRight, SWT.DOUBLE_BUFFERED);
					scrlCompRight.setContent(cvsAsciiCanvas);
					scrlCompRight.setMinSize(cvsAsciiCanvas.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					cvsAsciiCanvas.addMouseTrackListener(new MouseTrackAdapter() {
						@Override
						public void mouseEnter(MouseEvent e) {
							scrlCompRight.setFocus();
						}
					});
//					editor.addCanvasToRedrawSet(cvsAsciiCanvas);
					
			sashLayersTimeline = new SWT_LayersTimeline(verticalSash, SWT.HORIZONTAL);
	}

	public AskeyEditor getEditorWindow() {
		return this.editor;
	}

	public SWT_LayersTimeline getTimeline() {
		return sashLayersTimeline;
	}
}