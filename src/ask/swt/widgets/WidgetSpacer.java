package ask.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class WidgetSpacer extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public WidgetSpacer(Composite parent, int style) {
		super (parent, style);
		GridLayout gl_spacer = new GridLayout(1, false);
		this.setLayout(gl_spacer);
		GridData gd_spacer = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_spacer.widthHint = 180;
		gd_spacer.heightHint = 1;
		this.setLayoutData(gd_spacer);
		this.setBackground(SWTResourceManager.getColor(65, 65, 65));
	}
}