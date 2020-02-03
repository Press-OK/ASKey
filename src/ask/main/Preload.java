package ask.main;

public class Preload {
	public static void main(String[] args) {
		try {
			AskeyEditor window = new AskeyEditor();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}