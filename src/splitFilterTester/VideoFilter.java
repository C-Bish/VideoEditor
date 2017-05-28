package splitFilterTester;

import java.util.ArrayList;
import java.util.Iterator;

public class VideoFilter {

	// add paratask here
	public void doRealAddingFilterWork(Iterator<String> smallVideos)
	{
			while(smallVideos.hasNext())
			{
				addFilter(smallVideos.next());
				System.out.println("lllll");
			}
	}
	
	public ArrayList<String> collectVideoPartitions(int numberOfPartitions)
	{
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < numberOfPartitions; i ++)
		{
			list.add("mvv_"+i+".mp4"); //sub video name -- video_1.mp4 or video_2.mp4
		}
		return list;
	}
	
	public static void addFilter(String smallVideo)
	{
		PTProcessor processor = new PTProcessor(smallVideo);
		String filter = "colorchannelmixer=.3:.4:.3:0:.3:.4:.3:0:.3:.4:.3";
		processor.initializeFilter(filter);
		processor.excute();
		
	}
}
