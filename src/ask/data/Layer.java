package ask.data;

import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import ask.dialog.WindowRenameSelection;
import ask.interfaces.RenameableControl;
import ask.main.AskeyEditor;
import ask.swt.layout.SWT_CenterPane;

public class Layer extends Composite implements RenameableControl {
	
	private SWT_CenterPane centerPane = (SWT_CenterPane)(this.getParent().getParent().getParent().getParent().getParent().getParent());
	private AskeyEditor editor = centerPane.getEditorWindow();
	private Layer self = this;
	
	private CLabel lblLayerName;

	private HashSet<Keyframe> keyframes = new HashSet<>();
	private String name = "";
	private boolean isHidden = false;
	private boolean isLocked = false;
	
	public Layer(Composite parent, int style) {
		super(parent, style);
		GridLayout gl_cmpLayerGrid = new GridLayout(3, false);
		gl_cmpLayerGrid.horizontalSpacing = 0;
		gl_cmpLayerGrid.verticalSpacing = 0;
		gl_cmpLayerGrid.marginWidth = 0;
		gl_cmpLayerGrid.marginHeight = 0;
		setLayout(gl_cmpLayerGrid);
		setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
			lblLayerName = new CLabel(this, SWT.LEFT);
			lblLayerName.setBackground(SWTResourceManager.getColor(25, 25, 25));
			lblLayerName.setForeground(SWTResourceManager.getColor(255, 255, 255));
			GridData gd_lblLayerName = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
			gd_lblLayerName.heightHint = 25;
			lblLayerName.setLayoutData(gd_lblLayerName);
			lblLayerName.setText(name);
			lblLayerName.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDoubleClick(MouseEvent e) {
					WindowRenameSelection renameDialog = new WindowRenameSelection(editor, self);
					renameDialog.open();
				}
			});
			
			CLabel lblToggleVis = new CLabel(this, SWT.CENTER);
			lblToggleVis.setBackground(SWTResourceManager.getColor(30, 30, 30));
			lblToggleVis.setForeground(SWTResourceManager.getColor(255, 255, 255));
			GridData gd_lblToggleVis = new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1);
			gd_lblToggleVis.heightHint = 25;
			gd_lblToggleVis.widthHint = 25;
			lblToggleVis.setLayoutData(gd_lblToggleVis);
			lblToggleVis.setText("V");
			lblToggleVis.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					if (!isHidden) {
						lblToggleVis.setForeground(SWTResourceManager.getColor(255, 100, 100));
						lblToggleVis.setBackground(SWTResourceManager.getColor(60, 25, 25));
					} else {
						lblToggleVis.setForeground(SWTResourceManager.getColor(100, 255, 100));
						lblToggleVis.setBackground(SWTResourceManager.getColor(25, 25, 25));
					}
				}
				@Override
				public void mouseUp(MouseEvent e) {
					if (!isHidden) {
						lblToggleVis.setForeground(SWTResourceManager.getColor(255, 100, 100));
						lblToggleVis.setBackground(SWTResourceManager.getColor(60, 35, 35));
						isHidden = !isHidden;
					} else {
						lblToggleVis.setForeground(SWTResourceManager.getColor(100, 255, 100));
						lblToggleVis.setBackground(SWTResourceManager.getColor(35, 35, 35));
						isHidden = !isHidden;
					}
				}
			});
			lblToggleVis.addMouseTrackListener(new MouseTrackAdapter() {
				@Override
				public void mouseEnter(MouseEvent e) {
					if (isHidden) {
						lblToggleVis.setForeground(SWTResourceManager.getColor(255, 100, 100));
						lblToggleVis.setBackground(SWTResourceManager.getColor(60, 35, 35));
					} else {
						lblToggleVis.setForeground(SWTResourceManager.getColor(100, 255, 100));
						lblToggleVis.setBackground(SWTResourceManager.getColor(35, 35, 35));
					}
				}
				@Override
				public void mouseExit(MouseEvent e) {
					if (isHidden) {
						lblToggleVis.setForeground(SWTResourceManager.getColor(255, 100, 100));
						lblToggleVis.setBackground(SWTResourceManager.getColor(60, 30, 30));
					} else {
						lblToggleVis.setForeground(SWTResourceManager.getColor(255, 255, 255));
						lblToggleVis.setBackground(SWTResourceManager.getColor(30, 30, 30));
					}
				}
			});
			
			
			CLabel lblToggleLock = new CLabel(this, SWT.CENTER);
			lblToggleLock.setBackground(SWTResourceManager.getColor(35, 35, 35));
			lblToggleLock.setForeground(SWTResourceManager.getColor(255, 255, 255));
			GridData gd_lblToggleLock = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
			gd_lblToggleLock.heightHint = 25;
			gd_lblToggleLock.widthHint = 25;
			lblToggleLock.setLayoutData(gd_lblToggleLock);
			lblToggleLock.setText("L");
			lblToggleLock.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					if (!isLocked) {
						lblToggleLock.setForeground(SWTResourceManager.getColor(255, 100, 100));
						lblToggleLock.setBackground(SWTResourceManager.getColor(65, 30, 30));
					} else {
						lblToggleLock.setForeground(SWTResourceManager.getColor(100, 255, 100));
						lblToggleLock.setBackground(SWTResourceManager.getColor(30, 30, 30));
					}
				}
				@Override
				public void mouseUp(MouseEvent e) {
					if (!isLocked) {
						lblToggleLock.setForeground(SWTResourceManager.getColor(255, 100, 100));
						lblToggleLock.setBackground(SWTResourceManager.getColor(65, 40, 40));
						isLocked = !isLocked;
					} else {
						lblToggleLock.setForeground(SWTResourceManager.getColor(100, 255, 100));
						lblToggleLock.setBackground(SWTResourceManager.getColor(40, 40, 40));
						isLocked = !isLocked;
					}
				}
			});
			lblToggleLock.addMouseTrackListener(new MouseTrackAdapter() {
				@Override
				public void mouseEnter(MouseEvent e) {
					if (isLocked) {
						lblToggleLock.setForeground(SWTResourceManager.getColor(255, 100, 100));
						lblToggleLock.setBackground(SWTResourceManager.getColor(65, 40, 40));
					} else {
						lblToggleLock.setForeground(SWTResourceManager.getColor(100, 255, 100));
						lblToggleLock.setBackground(SWTResourceManager.getColor(40, 40, 40));
					}
				}
				@Override
				public void mouseExit(MouseEvent e) {
					if (isLocked) {
						lblToggleLock.setForeground(SWTResourceManager.getColor(255, 100, 100));
						lblToggleLock.setBackground(SWTResourceManager.getColor(65, 35, 35));
					} else {
						lblToggleLock.setForeground(SWTResourceManager.getColor(255, 255, 255));
						lblToggleLock.setBackground(SWTResourceManager.getColor(35, 35, 35));
					}
				}
			});
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		lblLayerName.setText("  " + name);
		this.name = name;
	}

	public HashSet<Keyframe> getKeyframes() {
		return keyframes;
	}

	public void setKeyframes(HashSet<Keyframe> keyframes) {
		this.keyframes = keyframes;
	}
	
	public void addKeyframe(Keyframe keyframes) {
		this.keyframes.add(keyframes);
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}
}
