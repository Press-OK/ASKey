package ask.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.SWTResourceManager;

import ask.data.RasterTool;
import ask.main.AskeyEditor;
import ask.swt.layout.SWT_ModularVerticalBar;

public class WidgetToolProperties extends _WidgetTemplate {
	
	private AskeyEditor editor;
	private int widgetHeight = 200, widgetWidth = 182;
	private boolean isCollapsed = false;
	
	private RasterTool selectedTool;
	private Composite cmpPropHolder;
	private GridData gd_cmpPropHolder;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public WidgetToolProperties(Composite parent, int style) {
		super (parent, style, "Tool Properties");
		this.editor = ((SWT_ModularVerticalBar) this.getParent()).getEditorWindow();

		cmpPropHolder = new Composite(this, SWT.NONE);
		GridLayout gl_cmpPropHolder = new GridLayout(1, false);
		gl_cmpPropHolder.marginHeight = 0;
		gl_cmpPropHolder.marginBottom = 10;
		gl_cmpPropHolder.marginWidth = 5;
		gl_cmpPropHolder.verticalSpacing = 0;
		gl_cmpPropHolder.horizontalSpacing = 0;
		cmpPropHolder.setLayout(gl_cmpPropHolder);
		gd_cmpPropHolder = new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1);
		gd_cmpPropHolder.widthHint = widgetWidth;
		gd_cmpPropHolder.heightHint = widgetHeight;
		cmpPropHolder.setLayoutData(gd_cmpPropHolder);
		cmpPropHolder.setBackground(SWTResourceManager.getColor(50, 50, 50));

		getDragLabel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (isCollapsed) {
					gd_cmpPropHolder.heightHint = widgetHeight;
					getParent().layout(true, true);
					isCollapsed = false;
				} else {
					gd_cmpPropHolder.heightHint = 0;
					getParent().layout(true, true);
					isCollapsed = true;
				}
			}
		});
	}

	public void setTool(RasterTool rasterTool) {
		this.selectedTool = rasterTool;
		for (Control c : cmpPropHolder.getChildren()) {
			c.dispose();
		}
		rasterTool.drawProperties(cmpPropHolder, SWT.NONE);
		widgetHeight = selectedTool.getPropertiesHeight();
		gd_cmpPropHolder.heightHint = rasterTool.getPropertiesHeight();
		cmpPropHolder.pack();
		getParent().layout(true, true);
	}
	
	public AskeyEditor getEditorWindow() {
		return this.editor;
	}
}