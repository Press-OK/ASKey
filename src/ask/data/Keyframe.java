package ask.data;

import org.eclipse.swt.graphics.Image;

public class Keyframe {
	private int keyframeStart = 0;
	private int keyframeEnd = 0;
	private boolean isEmpty = true;
	private Image image = null;

	public Keyframe (int start, int end) {
		this.keyframeStart = start;
		this.keyframeEnd = end;
	}
	
	public int getKeyframeEnd() {
		return this.keyframeEnd;
	}

	public void setKeyframeEnd(int keyframeEnd) {
		this.keyframeEnd = keyframeEnd;
	}

	public int getKeyframeStart() {
		return this.keyframeStart;
	}
	
	public void setKeyframeStart(int keyframeStart) {
		this.keyframeStart = keyframeStart;
	}
	
	public boolean isEmpty() {
		return this.isEmpty;
	}

	public void setEmpty(boolean b) {
		this.isEmpty = b;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}