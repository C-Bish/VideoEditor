package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import constants.ConfigConstants;

public class VideoCombiner {
	
	private String combinedVideoNames = "concat:";
	private int id;
	private String output, ext;
	
	public VideoCombiner(String output, int id) {
		this.output = output;
		this.id = id;
		ext = "";
		int i = output.lastIndexOf('.');
		if (i > 0) {
		    ext += output.substring(i+1);
		}
	}
	
	public String combine()
	{
		String cmd = null;
		try {
			cmd = convertVideoToTsFormat();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return cmd;
	}	
	
	public String convertVideoToTsFormat() throws IOException
	{
		File[] filteredVideos = new File("EditedSubVideo").listFiles();	
		
		checkTsFilesAreExisting();
		
		for(File filteredVideo: filteredVideos) 
		{	
			String name = null;
			try {
				name = filteredVideo.getName();
				int pos = name.lastIndexOf(".");
				if (pos > 0) {
				    name = name.substring(0, pos);
				}
				String command = "ffmpeg -i " + "EditedSubVideo\\" + filteredVideo.getName()
				+ " -c copy -bsf:v h264_mp4toannexb -f mpegts "+ "EditedSubVideo\\" + name + ".ts";
				Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
			combinedVideoNames += ConfigConstants.EDITED_TS_VIDEOS_PATH + "\\" + name + ".ts|";	
		}
		System.out.println("combinedVideoNames are: " + combinedVideoNames);
		String names = "\""+combinedVideoNames+"\"";
		String command = "ffmpeg.exe" + " -i " + names + " -c" + " copy" + " -bsf:a" +
				" aac_adtstoasc " + ConfigConstants.COMBINED_VIDEO_PATH + "\\temp."+ext;
		
		System.out.println(names);
		System.out.println(command);
		
		/*try {
			Runtime.getRuntime().exec(command);		
		} catch (IOException e) {
			e.printStackTrace();
		}
		ProcessBuilder pb = new ProcessBuilder("ffmpeg.exe", "-i", names, "-c" , "copy", "-bsf:a",
				"aac_adtstoasc", ConfigConstants.COMBINED_VIDEO_PATH + "\\temp."+ext);
		try {	
			pb.redirectErrorStream(true);
			pb.start();	
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		return command;
	}
	
	public void checkTsFilesAreExisting() throws IOException
	{
		while(true)
		{
			if(Files.list(Paths.get("EditedSubVideo")).count() == Runtime.getRuntime().availableProcessors())
				break;
			else
				System.out.println("EditedSubVideo: " + Files.list(Paths.get("EditedSubVideo")).count());	
		}
	}
	
	public void runCombineProcessBuilder()
	{
		
		File directory = new File("EEditedSubVideo");
		 if (!directory.exists()) {
			 directory.mkdirs();
	     }
		
		String command = "ffmpeg.exe" + "-i" + combinedVideoNames + "-c" + "copy" + "-bsf:a" +
				"aac_adtstoasc" + ConfigConstants.COMBINED_VIDEO_PATH + "\\filteredVideo.mp4";
		
		ProcessBuilder pb = new ProcessBuilder("ffmpeg.exe", "-i", combinedVideoNames, "-c" , "copy", "-bsf:a",
				"aac_adtstoasc", "Edited Video\\"+output);
		try {
			System.out.println(combinedVideoNames);
			pb.redirectErrorStream(true);
			pb.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
