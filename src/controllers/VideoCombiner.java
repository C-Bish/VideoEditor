package controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import constants.ConfigConstants;

public class VideoCombiner {
	
	private String combinedVideoNames = "concat:";

	
	public void combine()
	{
		try {
			convertVideoToTsFormat();
			//checkTsFilesAreExisting();
			runCombineProcessBuilder();
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}
	
	
	public void convertVideoToTsFormat() throws IOException
	{
		/*while(true)
		{
			if(Files.list(Paths.get("Edited Video")).count() == 4)
				break;
			else
				System.out.println("Edited Videoooo: " + Files.list(Paths.get("Edited Video")).count());	
		}*/
		File[] filteredVideos = new File("EditedSubVideo").listFiles();		
		for(File filteredVideo: filteredVideos) 
		{	
			try {
				String command = "ffmpeg -i " + "EditedTsVideo\\" + filteredVideo.getName()
				+ " -c copy -bsf:v h264_mp4toannexb -f mpegts "+ "EditedTsVideo\\" + filteredVideo.getName() + ".ts";
				Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
			combinedVideoNames += ConfigConstants.EDITED_TS_VIDEOS_PATH + "\\" +filteredVideo.getName() + ".ts|";	
		}

	}
	
	public void checkTsFilesAreExisting() throws IOException
	{
		while(true)
		{
			if(Files.list(Paths.get("Edited Video")).count() == Runtime.getRuntime().availableProcessors())
				break;
			else
				System.out.println("Edited Video: " + Files.list(Paths.get("Edited Video")).count());	
		}
	}
	
	public void runCombineProcessBuilder()
	{
		
		File directory = new File("Edited Video");
		 if (!directory.exists()) {
			 directory.mkdirs();
	     }
		
		String command = "ffmpeg.exe" + "-i" + combinedVideoNames + "-c" + "copy" + "-bsf:a" +
				"aac_adtstoasc" + ConfigConstants.COMBINED_VIDEO_PATH + "\\filteredVideo.mp4";
		
		ProcessBuilder pb = new ProcessBuilder("ffmpeg.exe", "-i", combinedVideoNames, "-c" , "copy", "-bsf:a",
				"aac_adtstoasc", "Edited Video\\CombinedVideo.mp4");
		try {
			System.out.println(combinedVideoNames);
			pb.redirectErrorStream(true);
			pb.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
