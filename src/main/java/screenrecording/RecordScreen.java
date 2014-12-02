package screenrecording;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

public class RecordScreen {
	private static final double FRAME_RATE = 10;
	private String outputFilename = "./target/videos/";
	private static Dimension screenBounds;
	private Thread t1;
	private	IMediaWriter writer; 
	public RecordScreen(String filename) throws IOException {
		if (!FileUtils.getFile(outputFilename).exists())
			FileUtils.forceMkdir(new File(outputFilename));

		outputFilename += filename
				+ DateTime.now().toDateTimeISO().toString("hhmmssddMMyyyy")
				+ ".mp4";
		RecordScreen.screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
		RecordScreen.screenBounds.setSize(800, 640);
		writer= ToolFactory.makeWriter(outputFilename);
		writer.open();

	}

	public String getOutputFilename() {
		return outputFilename;
	}

	public void beginrecord() {
		t1 = new Thread(new Runnable() {
			public void run() {
			
				writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4,
						screenBounds.width, screenBounds.height);
				long startTime = System.nanoTime();
				while (t1.isAlive()) {
					// take the screen shot
					BufferedImage screen = getDesktopScreenshot();
					// convert to the right image type
					BufferedImage bgrScreen = convertToType(screen,
							BufferedImage.TYPE_3BYTE_BGR);
					// encode the image to stream #0
					writer.encodeVideo(0, bgrScreen, System.nanoTime()
							- startTime, TimeUnit.NANOSECONDS);
					writer.setForceInterleave(true);
					// sleep for frame rate milliseconds
					try {
						Thread.sleep((long) (1000 / FRAME_RATE));
					} catch (InterruptedException e) {
						// ignore
						e.printStackTrace();
					}

				}

				writer.flush();
	

			}
		});
		t1.start();

	}

	public static BufferedImage convertToType(BufferedImage sourceImage,
			int targetType) {

		BufferedImage image;

		if (sourceImage.getType() == targetType)
			image = sourceImage;

		else {
			image = new BufferedImage(sourceImage.getWidth(),
					sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}

		return image;

	}

	public static BufferedImage getDesktopScreenshot() {
		try {
			Robot robot = new Robot();
			Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			return robot.createScreenCapture(captureSize);
		} catch (AWTException e) {
			e.printStackTrace();
			return null;
		}

	}

	public String stoprecording() throws IOException, InterruptedException {
		
		t1.stop();
		writer.close();
		Thread.sleep(1000);
		return getOutputFilename().replace("./target", "..");
	}
}
