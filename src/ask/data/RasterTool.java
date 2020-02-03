package ask.data;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import ask.interfaces.RenameableControl;

public abstract class RasterTool implements RenameableControl {
	
	private String name = "";
	private int height = 0;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String s) {
		this.name = s;
	}
	
	public abstract void drawProperties(Composite parent, int style);
	public abstract int getPropertiesHeight();
	public abstract void handleMouseDown(int x, int y, Keyframe k);
	public abstract void handleMouseMove(int x, int y, Keyframe k);
	public abstract void handleMouseUp(int x, int y, Keyframe k);
}
