package core.sikuli;

import org.sikuli.script.FindFailed;
import org.sikuli.script.ImagePath;
import org.sikuli.script.Screen;

public class SikuliHandler {
	private Screen screen;

	public SikuliHandler(String imagepath) {
		screen = new Screen();
		ImagePath.add(imagepath);
	}
	
	public void performclick(String image){
		try {
			screen.wait(image);
			screen.click(image);
		} catch (FindFailed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
