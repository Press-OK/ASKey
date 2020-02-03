package ask.drawtools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import ask.data.Keyframe;
import ask.data.RasterTool;

public class Eraser extends RasterTool {

	private int widgetHeight = 50;

	@Override
	public void drawProperties(Composite parent, int style) {
		Button tempButton1 = new Button(parent, SWT.NONE);
		GridData gd_tempButton1 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_tempButton1.heightHint = 50;
		gd_tempButton1.widthHint = 50;
		tempButton1.setLayoutData(gd_tempButton1);
		tempButton1.setText("eraser");
	}
	
	public int getPropertiesHeight() {
		return this.widgetHeight;
	}

	@Override
	public void handleMouseDown(int x, int y, Keyframe k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseMove(int x, int y, Keyframe k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleMouseUp(int x, int y, Keyframe k) {
		// TODO Auto-generated method stub
		
	}
}
