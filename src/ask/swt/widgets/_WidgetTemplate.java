package ask.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public abstract class _WidgetTemplate extends Composite {
	private boolean isCollapsed = false;
	private _WidgetTemplate child = null;
	
	private String widgetName;
	private Label lblDrag;
	private GridData gd_cmpWidget;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public _WidgetTemplate(Composite parent, int style, String name) {
		super(parent, SWT.NONE);
		this.widgetName = name;

		gd_cmpWidget = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
		this.setLayoutData(gd_cmpWidget);
		GridLayout gl_cmpWidget = new GridLayout(1, false);
		gl_cmpWidget.marginWidth = 3;
		gl_cmpWidget.marginHeight = 3;
		gl_cmpWidget.verticalSpacing = 10;
		this.setLayout(gl_cmpWidget);
		this.setBackground(SWTResourceManager.getColor(50, 50, 50));
		
		lblDrag = new Label(this, SWT.NONE);
		lblDrag.setForeground(SWTResourceManager.getColor(200, 200, 200));
		lblDrag.setBackground(SWTResourceManager.getColor(40, 40, 40));
		lblDrag.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		lblDrag.setFont(SWTResourceManager.getFont("Tahoma", 8, SWT.BOLD));
		lblDrag.setText(" ≡ " + widgetName);
	}
	
	public Label getDragLabel() {
		return this.lblDrag;
	}
}
