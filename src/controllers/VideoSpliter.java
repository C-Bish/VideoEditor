package controllers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameFilter.Exception;


public class VideoSpliter {
	
	private File file;
	private int id;
	
	public VideoSpliter(String originalVideoPath, int id)
	{
		this.id = id;
		File directory = new File(System.getProperty("user.dir") + "/SubVideos/");
        if (!directory.exists()) {
        	directory.mkdirs();
        }
        for(File file: directory.listFiles()) 
            if (!file.isDirectory())
                file.delete();
        file = new File(originalVideoPath);
	}
	
	public void splitVideFile() throws IOException
	{
		try {
            
            if (file.exists()) {

            String videoFileName = file.getName().substring(0, file.getName().lastIndexOf(".")); // Name of the videoFile without extension
            File splitFile = new File("Videos_Split/"+ videoFileName);//Destination folder to save.
            if (!splitFile.exists()) {
                splitFile.mkdirs();
                System.out.println("Directory Created -> "+ splitFile.getAbsolutePath());
            }

            int i = 01;// Files count starts from 1
            String videoFile = splitFile.getAbsolutePath() +"/"+ String.format("%02d", i) +"_"+ file.getName();// Location to save the files which are Split from the original file.
    		
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = new FileOutputStream(videoFile);
            System.out.println("File Created Location: "+ videoFile);
            int totalPartsToSplit = Runtime.getRuntime().availableProcessors();// Total files to split.
            int splitSize = inputStream.available() / totalPartsToSplit;
            int streamSize = 0;
            int read = 0;
            
            inputStream.close();
            outputStream.close();
            System.out.println("Total files Split ->"+ totalPartsToSplit);
	        } else {
	            System.err.println(file.getAbsolutePath() +" File Not Found.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	

	public String getVideoDuration(String filePath) throws IOException
	{
		ProcessBuilder pb = new ProcessBuilder("ffmpeg.exe", "-i", filePath);
		pb.redirectErrorStream(true);
		Process proc = pb.start();
		String line;  
		String duration = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(
		        proc.getInputStream()));             
		while ((line = in.readLine()) != null) {
			if(line.contains("Duration")){
				System.out.println(line);
				String[] results = line.split(": ");
				duration = results[1].split(",")[0];
			}
		}
		proc.destroy();
		return duration;	
	}
	
	
	public int transferDuration(String duration)
	{
		String[] timeParts = duration.split(":");
		String[] secondParts = timeParts[2].split("\\.");
		return (int) (Integer.parseInt(timeParts[0]) * 3600000L + 
				Integer.parseInt(timeParts[1]) * 60000  +
				Integer.parseInt(secondParts[0]) * 1000  +
				Integer.parseInt(secondParts[1])
				);
	}
	
	
	public int partition(int duration, int numberOfParts)
	{
		return duration / numberOfParts;
	}
	
	public String transferMsToDuration(int subsectionMs)
	{
		int hours = (int) (subsectionMs / 3600000L);
		int mins = (int) (subsectionMs - hours * 3600000L) / 60000;
		int seconds = (int) (subsectionMs - hours * 3600000L - mins * 60000) / 1000;
		int ms = (int) (subsectionMs - hours * 3600000L - mins * 60000 - seconds * 1000);
		return hours + ":" + mins + ":" + seconds + "." + ms;
	}
	
	public ArrayList<String> generateCommandLines(int numberOfPartitions, int partitionedInMs, String partitionedDur)
	{
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < numberOfPartitions; i ++)
		{
			String oriTime = transferMsToDuration(partitionedInMs * i);
			String commandLine = "ffmpeg.exe -i "+file.getName()+" -ss " + oriTime + " -c copy -t "+ partitionedDur +" SubVideos\\sub_video_"+ i +".mp4";
			
			list.add(commandLine);
			System.out.println(commandLine);
		}
		return list;
	}
	

	public void doRealSplittingWork(Iterator<String> it)
	{
		while (it.hasNext()) {
			doPerfermance(it.next());
		}
	}
	
	public void doPerfermance(String commandLine)
	{
		try {
			Runtime.getRuntime().exec(commandLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
