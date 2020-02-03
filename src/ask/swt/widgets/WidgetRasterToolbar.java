package ask.swt.widgets;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import ask.data.RasterTool;
import ask.drawtools.Eraser;
import ask.drawtools.Paintbrush;
import ask.main.AskeyEditor;
import ask.swt.layout.SWT_ModularVerticalBar;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.MouseTrackAdapter;

public class WidgetRasterToolbar extends _WidgetTemplate {
	
	private AskeyEditor editor;
	private int widgetHeight = 200, widgetWidth = 182;
	private boolean isCollapsed = false;
	
	private String oldEditorStatusTip = "";
	
	private HashMap<Button, RasterTool> allTools = new HashMap<>();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public WidgetRasterToolbar(Composite parent, int style) {
		super (parent, style, "Drawing Tools");
		this.editor = ((SWT_ModularVerticalBar)this.getParent()).getEditorWindow();
		
		Composite cmpToolBar = new Composite(this, SWT.NONE);
		GridData gd_cmpToolBar = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		gd_cmpToolBar.widthHint = widgetWidth;
		gd_cmpToolBar.heightHint = widgetHeight;
		cmpToolBar.setLayoutData(gd_cmpToolBar);
		cmpToolBar.setBackground(SWTResourceManager.getColor(50, 50, 50));
		GridLayout gl_cmpToolBar = new GridLayout(3, false);
		gl_cmpToolBar.marginHeight = 10;
		gl_cmpToolBar.marginWidth = 10;
		gl_cmpToolBar.verticalSpacing = 0;
		gl_cmpToolBar.horizontalSpacing = 0;
		cmpToolBar.setLayout(gl_cmpToolBar);

		Button tempButton1 = new Button(cmpToolBar, SWT.TOGGLE);
		GridData gd_tempButton1 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_tempButton1.heightHint = 40;
		gd_tempButton1.widthHint = 40;
		tempButton1.setLayoutData(gd_tempButton1);
		tempButton1.setText("pbrush");
		RasterTool pb = new Paintbrush();
		pb.setName("Paintbrush");
		allTools.put(tempButton1, pb);		

		Button tempButton2 = new Button(cmpToolBar, SWT.TOGGLE);
		GridData gd_tempButton2 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_tempButton2.heightHint = 40;
		gd_tempButton2.widthHint = 40;
		tempButton2.setLayoutData(gd_tempButton2);
		tempButton2.setText("eraser");
		RasterTool pb1 = new Eraser();
		pb1.setName("Eraser");
		allTools.put(tempButton2, pb1);	
		
		Button tempButton3 = new Button(cmpToolBar, SWT.TOGGLE);
		GridData gd_tempButton3 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_tempButton3.heightHint = 40;
		gd_tempButton3.widthHint = 40;
		tempButton3.setLayoutData(gd_tempButton3);
		tempButton3.setText("C");
		RasterTool pb2 = new Paintbrush();
		pb2.setName("asdasdasd");
		allTools.put(tempButton3, pb2);
		
		Button tempButton4 = new Button(cmpToolBar, SWT.TOGGLE);
		GridData gd_tempButton4 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_tempButton4.heightHint = 40;
		gd_tempButton4.widthHint = 40;
		tempButton4.setLayoutData(gd_tempButton4);
		tempButton4.setText("D");
		RasterTool pb3 = new Paintbrush();
		pb3.setName("asdasdasd");
		allTools.put(tempButton4, pb3);
		
		for (Button b : allTools.keySet()) {
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					selectTool(b);
				}
			});
			b.addMouseTrackListener(new MouseTrackAdapter() {
				@Override
				public void mouseEnter(MouseEvent e) {
					oldEditorStatusTip = editor.getStatusText();
					editor.setStatusText("Tool: " + allTools.get(b).getName());
				}
				@Override
				public void mouseExit(MouseEvent e) {
					editor.setStatusText(oldEditorStatusTip);
				}
			});
		}

		getDragLabel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (isCollapsed) {
					gd_cmpToolBar.heightHint = widgetHeight;
					getParent().layout(true, true);
					isCollapsed = false;
				} else {
					gd_cmpToolBar.heightHint = 0;
					getParent().layout(true, true);
					isCollapsed = true;
				}
			}
		});
	}
	
	private void selectTool(Button b) {
		for (Button t : allTools.keySet()) {
			if (b != t) t.setSelection(false);
		}
		editor.setSelectedTool(allTools.get(b));
		oldEditorStatusTip = editor.getStatusText();
	}
}
