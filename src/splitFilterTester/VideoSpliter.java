package splitFilterTester;//####[1]####
//####[1]####
import java.io.BufferedReader;//####[5]####
import java.io.File;//####[6]####
import java.io.FileInputStream;//####[7]####
import java.io.FileNotFoundException;//####[8]####
import java.io.FileOutputStream;//####[9]####
import java.io.IOException;//####[10]####
import java.io.InputStream;//####[11]####
import java.io.InputStreamReader;//####[12]####
import java.io.OutputStream;//####[13]####
import java.util.ArrayList;//####[14]####
import java.util.Arrays;//####[15]####
import java.util.Iterator;//####[16]####
import java.util.List;//####[17]####
import java.util.regex.Matcher;//####[18]####
import java.util.regex.Pattern;//####[19]####
import org.bytedeco.javacv.FFmpegFrameGrabber;//####[21]####
import org.bytedeco.javacv.FrameFilter.Exception;//####[22]####
//####[22]####
//-- ParaTask related imports//####[22]####
import pt.runtime.*;//####[22]####
import java.util.concurrent.ExecutionException;//####[22]####
import java.util.concurrent.locks.*;//####[22]####
import java.lang.reflect.*;//####[22]####
import pt.runtime.GuiThread;//####[22]####
import java.util.concurrent.BlockingQueue;//####[22]####
import java.util.ArrayList;//####[22]####
import java.util.List;//####[22]####
//####[22]####
/**
 * 
 * @author uday.p
 *
 *///####[28]####
public class VideoSpliter {//####[29]####
    static{ParaTask.init();}//####[29]####
    /*  ParaTask helper method to access private/protected slots *///####[29]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[29]####
        if (m.getParameterTypes().length == 0)//####[29]####
            m.invoke(instance);//####[29]####
        else if ((m.getParameterTypes().length == 1))//####[29]####
            m.invoke(instance, arg);//####[29]####
        else //####[29]####
            m.invoke(instance, arg, interResult);//####[29]####
    }//####[29]####
//####[33]####
    public static void splitVideFile() throws IOException {//####[34]####
        try {//####[35]####
            File file = new File("mv.mp4");//####[36]####
            if (file.exists()) //####[37]####
            {//####[37]####
                String videoFileName = file.getName().substring(0, file.getName().lastIndexOf("."));//####[39]####
                File splitFile = new File("Videos_Split/" + videoFileName);//####[40]####
                if (!splitFile.exists()) //####[41]####
                {//####[41]####
                    splitFile.mkdirs();//####[42]####
                    System.out.println("Directory Created -> " + splitFile.getAbsolutePath());//####[43]####
                }//####[44]####
                int i = 01;//####[46]####
                String videoFile = splitFile.getAbsolutePath() + "/" + String.format("%02d", i) + "_" + file.getName();//####[47]####
                InputStream inputStream = new FileInputStream(file);//####[49]####
                OutputStream outputStream = new FileOutputStream(videoFile);//####[50]####
                System.out.println("File Created Location: " + videoFile);//####[51]####
                int totalPartsToSplit = 4;//####[52]####
                int splitSize = inputStream.available() / totalPartsToSplit;//####[53]####
                int streamSize = 0;//####[54]####
                int read = 0;//####[55]####
                doTheWork(file, outputStream, inputStream, read, splitSize, streamSize, totalPartsToSplit, i, splitFile, videoFile);//####[57]####
                inputStream.close();//####[60]####
                outputStream.close();//####[61]####
                System.out.println("Total files Split ->" + totalPartsToSplit);//####[62]####
            } else {//####[63]####
                System.err.println(file.getAbsolutePath() + " File Not Found.");//####[64]####
            }//####[65]####
        } catch (Exception e) {//####[66]####
            e.printStackTrace();//####[67]####
        }//####[68]####
    }//####[69]####
//####[74]####
    public static void doTheWork(File file, OutputStream outputStream, InputStream inputStream, int read, int splitSize, int streamSize, int totalPartsToSplit, int i, File splitFile, String videoFile) throws FileNotFoundException, IOException {//####[77]####
        while (i < 4) //####[78]####
        {//####[78]####
            i++;//####[80]####
            String fileCount = String.format("%02d", i);//####[81]####
            videoFile = splitFile.getAbsolutePath() + "/" + fileCount + "_" + file.getName();//####[82]####
            outputStream = new FileOutputStream(videoFile);//####[83]####
            System.out.println("File Created Location: " + videoFile);//####[84]####
            streamSize = 0;//####[85]####
            outputStream.write(read);//####[87]####
            streamSize++;//####[88]####
        }//####[89]####
    }//####[90]####
//####[96]####
    public String getVideoDuration(String filePath) throws IOException {//####[97]####
        ProcessBuilder pb = new ProcessBuilder("ffmpeg.exe", "-i", filePath);//####[98]####
        pb.redirectErrorStream(true);//####[99]####
        Process proc = pb.start();//####[100]####
        String line;//####[101]####
        String duration = "";//####[102]####
        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));//####[103]####
        while ((line = in.readLine()) != null) //####[105]####
        {//####[105]####
            if (line.contains("Duration")) //####[106]####
            {//####[106]####
                System.out.println(line);//####[107]####
                String[] results = line.split(": ");//####[108]####
                duration = results[1].split(",")[0];//####[109]####
            }//####[110]####
        }//####[111]####
        proc.destroy();//####[112]####
        return duration;//####[113]####
    }//####[114]####
//####[117]####
    public int transferDuration(String duration) {//####[118]####
        String[] timeParts = duration.split(":");//####[119]####
        String[] secondParts = timeParts[2].split("\\.");//####[120]####
        return (int) (Integer.parseInt(timeParts[0]) * 3600000L + Integer.parseInt(timeParts[1]) * 60000 + Integer.parseInt(secondParts[0]) * 1000 + Integer.parseInt(secondParts[1]));//####[121]####
    }//####[126]####
//####[129]####
    public int partition(int duration, int numberOfParts) {//####[130]####
        return duration / numberOfParts;//####[131]####
    }//####[132]####
//####[134]####
    public String transferMsToDuration(int subsectionMs) {//####[135]####
        int hours = (int) (subsectionMs / 3600000L);//####[136]####
        int mins = (int) (subsectionMs - hours * 3600000L) / 60000;//####[137]####
        int seconds = (int) (subsectionMs - hours * 3600000L - mins * 60000) / 1000;//####[138]####
        int ms = (int) (subsectionMs - hours * 3600000L - mins * 60000 - seconds * 1000);//####[139]####
        return hours + ":" + mins + ":" + seconds + "." + ms;//####[140]####
    }//####[141]####
//####[143]####
    public ArrayList<String> generateCommandLines(int numberOfPartitions, int partitionedInMs, String partitionedDur) {//####[144]####
        ArrayList<String> list = new ArrayList<String>();//####[145]####
        for (int i = 0; i < numberOfPartitions; i++) //####[146]####
        {//####[147]####
            String oriTime = transferMsToDuration(partitionedInMs * i);//####[148]####
            String commandLine = "ffmpeg.exe -i mv.mp4 -ss " + oriTime + " -c copy -t " + partitionedDur + " mvv_" + i + ".mp4";//####[149]####
            list.add(commandLine);//####[151]####
        }//####[152]####
        return list;//####[153]####
    }//####[154]####
//####[157]####
    private static volatile Method __pt__doRealSplittingWork_IteratorString_method = null;//####[157]####
    private synchronized static void __pt__doRealSplittingWork_IteratorString_ensureMethodVarSet() {//####[157]####
        if (__pt__doRealSplittingWork_IteratorString_method == null) {//####[157]####
            try {
				__pt__doRealSplittingWork_IteratorString_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__doRealSplittingWork", new Class[] {//####[157]####
				    Iterator.class//####[157]####
				});
			} catch (SecurityException | NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//####[157]####
        }//####[157]####
    }//####[157]####
    public TaskIDGroup<Void> doRealSplittingWork(Iterator<String> it) {//####[158]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[158]####
        return doRealSplittingWork(it, new TaskInfo());//####[158]####
    }//####[158]####
    public TaskIDGroup<Void> doRealSplittingWork(Iterator<String> it, TaskInfo taskinfo) {//####[158]####
        // ensure Method variable is set//####[158]####
        if (__pt__doRealSplittingWork_IteratorString_method == null) {//####[158]####
            __pt__doRealSplittingWork_IteratorString_ensureMethodVarSet();//####[158]####
        }//####[158]####
        taskinfo.setParameters(it);//####[158]####
        taskinfo.setMethod(__pt__doRealSplittingWork_IteratorString_method);//####[158]####
        taskinfo.setInstance(this);//####[158]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[158]####
    }//####[158]####
    public TaskIDGroup<Void> doRealSplittingWork(TaskID<Iterator<String>> it) {//####[158]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[158]####
        return doRealSplittingWork(it, new TaskInfo());//####[158]####
    }//####[158]####
    public TaskIDGroup<Void> doRealSplittingWork(TaskID<Iterator<String>> it, TaskInfo taskinfo) {//####[158]####
        // ensure Method variable is set//####[158]####
        if (__pt__doRealSplittingWork_IteratorString_method == null) {//####[158]####
            __pt__doRealSplittingWork_IteratorString_ensureMethodVarSet();//####[158]####
        }//####[158]####
        taskinfo.setTaskIdArgIndexes(0);//####[158]####
        taskinfo.addDependsOn(it);//####[158]####
        taskinfo.setParameters(it);//####[158]####
        taskinfo.setMethod(__pt__doRealSplittingWork_IteratorString_method);//####[158]####
        taskinfo.setInstance(this);//####[158]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[158]####
    }//####[158]####
    public TaskIDGroup<Void> doRealSplittingWork(BlockingQueue<Iterator<String>> it) {//####[158]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[158]####
        return doRealSplittingWork(it, new TaskInfo());//####[158]####
    }//####[158]####
    public TaskIDGroup<Void> doRealSplittingWork(BlockingQueue<Iterator<String>> it, TaskInfo taskinfo) {//####[158]####
        // ensure Method variable is set//####[158]####
        if (__pt__doRealSplittingWork_IteratorString_method == null) {//####[158]####
            __pt__doRealSplittingWork_IteratorString_ensureMethodVarSet();//####[158]####
        }//####[158]####
        taskinfo.setQueueArgIndexes(0);//####[158]####
        taskinfo.setIsPipeline(true);//####[158]####
        taskinfo.setParameters(it);//####[158]####
        taskinfo.setMethod(__pt__doRealSplittingWork_IteratorString_method);//####[158]####
        taskinfo.setInstance(this);//####[158]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[158]####
    }//####[158]####
    public void __pt__doRealSplittingWork(Iterator<String> it) {//####[158]####
        while (it.hasNext()) //####[159]####
        {//####[159]####
            doPerfermance(it.next());//####[160]####
        }//####[161]####
    }//####[162]####
//####[162]####
//####[164]####
    public void doPerfermance(String commandLine) {//####[165]####
        try {//####[166]####
            Runtime.getRuntime().exec(commandLine);//####[167]####
        } catch (IOException e) {//####[168]####
            e.printStackTrace();//####[169]####
        }//####[170]####
    }//####[171]####
}//####[171]####
