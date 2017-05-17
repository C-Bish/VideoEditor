package Processing;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

public class ImageProcessing {
	FFmpegFrameGrabber grabFrame;
	FFmpegFrameRecorder recordFrame;
	
	public ImageProcessing() {
		
	}
	
	// Gets the first frame from a video and returns it as a buffered image
	public BufferedImage getFirstFrame(File file) {
		BufferedImage pic = null;
		grabFrame = new FFmpegFrameGrabber(file.getAbsolutePath());
		String filepath = System.getProperty("user.dir") + "/image.jpg";
		try {
			grabFrame.start();
			recordFrame = new FFmpegFrameRecorder(filepath ,grabFrame.getImageWidth(), grabFrame.getImageHeight());
			recordFrame.start();
			Frame frame = grabFrame.grabImage();
			recordFrame.record(frame,grabFrame.getPixelFormat());
			grabFrame.stop();
			grabFrame.release();
			recordFrame.stop();
			recordFrame.release();
		} catch (org.bytedeco.javacv.FrameRecorder.Exception | org.bytedeco.javacv.FrameGrabber.Exception e1) {
			e1.printStackTrace();
		}
		File image = new File(filepath);
		try {
			pic = ImageIO.read(image);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return pic;
	}
	
	// Function to scale a buffered image to a certain size (Useful for fitting an image into a window)
	public BufferedImage scale(BufferedImage img, int targetWidth, int targetHeight) {
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;

        int w = img.getWidth();
        int h = img.getHeight();

        int prevW = w;
        int prevH = h;

        do {
            if (w > targetWidth) {
                w /= 2;
                w = (w < targetWidth) ? targetWidth : w;
            }
            if (h > targetHeight) {
                h /= 2;
                h = (h < targetHeight) ? targetHeight : h;
            }
            if (scratchImage == null) {
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);

        if (g2 != null) {
            g2.dispose();
        }

        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }
        return ret;
    }
	
}
