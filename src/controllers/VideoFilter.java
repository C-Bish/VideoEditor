package controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameFilter.Exception;
import GUI.UI;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.*;
import Processing.VideoProcessor;

public class VideoFilter {
	
	private FFmpegFrameFilter filter;
	private FFmpegFrameRecorder videoRecorder;
	private FFmpegFrameGrabber videoGrab;
	private String filtertype;
	private File Directory;
	private File video;
	private String ext;
	private Long startTime;
	private int id;
	private UI ui;
    
	public VideoFilter(String filename, UI ui, int id) {
		this.id = id;
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
	
	public void getVideoFiles()
	{
		File[] listOfFiles = new File("SubVideos").listFiles();
		ArrayList<String> videoNames = new ArrayList<String>();
		for(File listOfFile : listOfFiles){
			if (listOfFile.isFile()) 
			{
				System.out.println(listOfFile.getName());
		        videoNames.add(listOfFile.getName());
			} 
		}
	}
	
	private void getDirectory() {
		Directory = new File(System.getProperty("user.dir") + "/EditedSubVideo/");
        if (!Directory.exists()) {
            Directory.mkdirs();
        }
        for(File file: Directory.listFiles()) 
            if (!file.isDirectory())
                file.delete();
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

	public void start() {
		Frame frame;
		try {
			System.out.println("Starting to process video: " + video.getName() + ".....");
            String path = Directory + "/edited_sub_video" + Thread.currentThread().getId() + "." + ext;
            initVideoRecorder(path);
            
            while (videoGrab.grab() != null) {
                frame = videoGrab.grabImage();
              
                if (frame != null) {
                    filter.push(frame);
                    Frame filterFrame;
                    filterFrame = filter.pull();
                    videoRecorder.setTimestamp(videoGrab.getTimestamp());
                    videoRecorder.record(filterFrame, videoGrab.getPixelFormat());
                }
            }
            filter.stop();
            videoRecorder.stop();
            videoRecorder.release();
            videoGrab.stop();
            videoGrab.release();
            System.out.println("Finished processing video: " + video.getName() + ".....");
            

        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (FrameRecorder.Exception e) {
            e.printStackTrace();
        } catch (FrameFilter.Exception e) {
            e.printStackTrace();
        }
	}
	
	public void excute()
	{
		long startTime = System.currentTimeMillis();
		start();
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Duration: " + totalTime + " ms");
	}
	
}