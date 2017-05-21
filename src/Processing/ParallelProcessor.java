package Processing;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameFilter.Exception;

import GUI.UI;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;

public class ParallelProcessor extends SwingWorker<Void, Integer> {
	
	private FFmpegFrameFilter filter;
	private FFmpegFrameRecorder videoRecorder;
	private FFmpegFrameGrabber videoGrab;
	private File Directory;
	private File video;
	private String ext;
	private Long startTime;
	private UI ui;
    
	public ParallelProcessor(String filename, UI ui) {
		this.ui = ui;
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
		long time = System.currentTimeMillis() - startTime;
		System.out.println("Video filtering took " + (time/1000) + " seconds.");
		ui.updateLabel("");
		JOptionPane.showMessageDialog(ui, "Finished Saving Video\nTime taken: " + (time/1000) + " seconds.");	
	}

	public void start() {
		try {
			System.out.println("Starting to process video: " + video.getName() + ".....");
            String path = Directory + "/video" + System.currentTimeMillis() + "." + ext;
            initVideoRecorder(path);    
            
            startTime = System.currentTimeMillis();
            System.out.println("There is " + videoGrab.getAudioChannels() + " audio channel");
            
            //ArrayList<Frame> frames = new ArrayList<>();     
 			//ConcurrentLinkedQueue<Frame> threadSafeFrames = new ConcurrentLinkedQueue<>();
 			//threadSafeFrames.addAll(frames);
 			//System.out.println("There are " + threadSafeFrames.size() + " to process");
 			
            ArrayList<Thread> threads = new ArrayList<>();
            int numThreads = Runtime.getRuntime().availableProcessors();
            System.out.println(numThreads);
            
            for (int i = 0; i < 2; i++) {
            	///SharingWorker w = new SharingWorker(threadSafeFrames);
            	Worker w = new Worker();
            	threads.add(w);
            	w.start();     
            }
            
            for (int i = 0; i < threads.size(); i++)  {
    			Thread w = threads.get(i);
    			try {
    				w.join();
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
            filter.stop();
            videoRecorder.stop();
            videoRecorder.release();
            videoGrab.stop();
            videoGrab.release();
            System.out.println("Finished processing video: " + video.getName() + ".....");
            long currentThreadID = Thread.currentThread().getId();
    	    System.out.println("-- Thread "+currentThreadID+ " finished processing video: " + video.getName());
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

	class Worker extends Thread {
		
		public Worker() {
			
		}
 		public void run() {
 			Frame frame;
 			try {
 				while (videoGrab.grab() != null) {
 	                frame = videoGrab.grabImage();			  
				    if (frame != null) {
				    	//frames.add(frame);
				        filter.push(frame);
				        Frame filterFrame;
				        filterFrame = filter.pull();
				        videoRecorder.setTimestamp(videoGrab.getTimestamp());
				        videoRecorder.record(filterFrame, videoGrab.getPixelFormat());
				    }
				}
			} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
				e.printStackTrace();
			}	
		}
	}
	

	class SharingWorker extends Thread {
		
		private ConcurrentLinkedQueue<Frame> sharedItems;
		
		public SharingWorker(ConcurrentLinkedQueue<Frame> sharedItems) {
			this.sharedItems = sharedItems;
		}
		
 		public void run() {
 			Frame frame = null;
 			while ((frame = sharedItems.poll()) != null ) {
 				try {
					filter.push(frame);
					Frame filterFrame;
			        filterFrame = filter.pull();
			        videoRecorder.setTimestamp(videoGrab.getTimestamp());
			        videoRecorder.record(filterFrame, videoGrab.getPixelFormat());
				} catch (Exception e) {
					e.printStackTrace();
				} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
					e.printStackTrace();
				}     
 			}
 			System.out.println("Thread has finished");
		}
	}
	
}