package splitFilterTester;

import java.io.IOException;
import java.util.Iterator;

public class Tester {

	
	
	public static void main(String[] args) throws IOException {
		VideoSpliter vs = new VideoSpliter();
		VideoFilter vf = new VideoFilter();
    	long startTime = System.currentTimeMillis();
    	
    	
    	//========= Splitting Video Start ============
    	//--------calculation--------
    	String duration = vs.getVideoDuration("mv.mp4");  // The video name
    	int durationInMs = vs.transferDuration(duration);
    	int partitionedInMs = vs.partition(durationInMs, 4);
    	String partitionedDur = vs.transferMsToDuration(partitionedInMs);
    	//---------working-----------
    	Iterator<String> it = vs.generateCommandLines(4, partitionedInMs, partitionedDur).iterator();
    	vs.doRealSplittingWork(it);
    	//========= Splitting Video End ==============
    	
    	//========= Adding Filter ==============
    	//-----------
    	Iterator<String> smallVideos = vf.collectVideoPartitions(4).iterator();
    	vf.doRealAddingFilterWork(smallVideos);
    	
    	

		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Duration: " + totalTime + " ms");


	}

}
