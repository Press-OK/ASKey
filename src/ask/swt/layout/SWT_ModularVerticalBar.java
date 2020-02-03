package ask.swt.layout;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import ask.main.AskeyEditor;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.wb.swt.SWTResourceManager;

public class SWT_ModularVerticalBar extends Composite {
	
	private AskeyEditor editor;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SWT_ModularVerticalBar(Shell shell, int style, AskeyEditor editor) {
		super (shell, style);
		this.editor = editor;
		
		GridLayout gl_LeftPane = new GridLayout(1, false);
		gl_LeftPane.verticalSpacing = 20;
		gl_LeftPane.marginWidth = 3;
		gl_LeftPane.marginHeight = 0;
		this.setLayout(gl_LeftPane);
		this.setBackground(SWTResourceManager.getColor(65, 65, 65));
	}

	public AskeyEditor getEditorWindow() {
		return this.editor;
	}
}
