package Processing;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameFilter.Exception;

import GUI.UI;
import Processing.VideoProcessor;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;

public class VideoProcessor extends SwingWorker<Void, Integer> {
	
	private FFmpegFrameFilter filter;
	private FFmpegFrameRecorder videoRecorder;
	private FFmpegFrameGrabber videoGrab;
	private File Directory;
	private File video;
	private String ext;
	private Long startTime;
	private UI ui;
	public String path;
	private int id;
	public boolean done = false;
    
	public VideoProcessor(String filename, UI ui, int id) {
		this.ui = ui;
		this.id= id;
		video = new File(filename);
		videoGrab = new FFmpegFrameGrabber(video.getAbsolutePath());
		ext = getFileExtension(filename);
		try {
			videoGrab.start();
		} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
			e.printStackTrace();
		}
		getDirectory();
	}
	
	private void getDirectory() {
		Directory = new File(System.getProperty("user.dir") + "/Edited Video/");
        if (!Directory.exists()) {
            Directory.mkdirs();
        }
	}
	
	public File getFile() {
		 return new File(path);
	}
	
	public void initializeFilter(String Filter) {
		// Set the FFmpeg effect/filter that will be applied
		filter = new FFmpegFrameFilter(Filter, videoGrab.getImageWidth(), videoGrab.getImageHeight());
		try {
			filter.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initVideoRecorder(String path) {
		try {  
            videoRecorder = new FFmpegFrameRecorder(path, videoGrab.getImageWidth(), videoGrab.getImageHeight(), videoGrab.getAudioChannels());
            videoRecorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
            videoRecorder.setAudioCodec(videoGrab.getAudioCodec());
            videoRecorder.setAudioBitrate(videoGrab.getAudioBitrate());
            videoRecorder.setVideoBitrate(videoGrab.getVideoBitrate());
            videoRecorder.setFrameRate(videoGrab.getFrameRate());
            videoRecorder.setSampleFormat(videoGrab.getSampleFormat());
            videoRecorder.setSampleRate(videoGrab.getSampleRate());
            videoRecorder.start();
        } catch (FrameRecorder.Exception e) {
            e.printStackTrace();
        }
	}
	
	private String getFileExtension(String filename) {
		String extension = "";
		int i = filename.lastIndexOf('.');
		if (i > 0) {
		    extension += filename.substring(i+1);
		}
		return extension;
	}
		
	@Override
	protected Void doInBackground() throws Exception {
		start();
		return null;
	}
	
	@Override
	protected void done() {
		while(!done);
		if (ui.cancelled) {
			File output = new File(path);
			output.delete();
		} else {
			long time = System.currentTimeMillis() - startTime;
			System.out.println("Video filtering took " + (time/1000) + " seconds.");
			JOptionPane.showMessageDialog(ui, "Finished Saving Video\nTime taken: " + (time/1000) + " seconds.");
			System.out.println(id);
			if (ui.progressBars.size() <= id) {
				ui.progressBars.remove(0);
				ui.processingInfo.remove(0);
			} else {
				ui.progressBars.remove(id);
				ui.processingInfo.remove(id);
			}
			ui.processors.remove(this);
			ui.updateProcessing();
			if (ui.progressBars.size() == 0) {
				ui.updateLabel("");
			}
		}
	}

	public void start() {
		Frame frame;
		try {
			System.out.println("Starting to process video: " + video.getName() + ".....");
            path = Directory + "/" + ui.outputVideo + "." + ext;
            initVideoRecorder(path);    
            
            startTime = System.currentTimeMillis();
            
            int count = 0;
            int progress = 0;
            int frames = videoGrab.getLengthInFrames();
            System.out.println("There are " + frames + " frames in this video");
            while (videoGrab.grab() != null) {
            	if (ui.cancelled) {
            		break;
            	}
                frame = videoGrab.grabImage();
              
                if (frame != null) {
                    filter.push(frame);
                    Frame filterFrame;
                    filterFrame = filter.pull();
                    videoRecorder.setTimestamp(videoGrab.getTimestamp());
                    videoRecorder.record(filterFrame, videoGrab.getPixelFormat());
                }
                count++;
                if (count % (frames/100) == 0) {
                	progress++;
                	ui.progressBars.get(id).setValue(progress);
                }
            }
            if (!ui.cancelled) {ui.progressBars.get(id).setValue(100);}
            filter.stop();
            filter.release();
            videoRecorder.stop();
            videoRecorder.release();
            videoGrab.stop();
            videoGrab.release();
            System.out.println("Finished processing video: " + video.getName() + ".....");
            long currentThreadID = Thread.currentThread().getId();
    	    System.out.println("-- Thread "+currentThreadID+ " finished processing video: " + video.getName());
    	    done = true;
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (FrameRecorder.Exception e) {
            e.printStackTrace();
        } catch (FrameFilter.Exception e) {
            e.printStackTrace();
        }
	}
	
	public static void performIO(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    long currentThreadID = Thread.currentThread().getId();
	    System.out.println("** Thread "+currentThreadID+ " finished IO("+i+")"); 
	}
	
}