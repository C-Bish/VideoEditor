package Processing;//####[1]####
//####[1]####
import java.awt.BorderLayout;//####[3]####
import java.awt.Color;//####[4]####
import java.awt.event.ActionEvent;//####[5]####
import java.awt.event.ActionListener;//####[6]####
import java.io.IOException;//####[7]####
import java.io.File;//####[8]####
import javax.swing.JButton;//####[9]####
import javax.swing.JFrame;//####[10]####
import javax.swing.JOptionPane;//####[11]####
import javax.swing.SwingUtilities;//####[12]####
import java.util.ArrayList;//####[13]####
import javax.swing.SwingWorker;//####[14]####
import controllers.*;//####[16]####
import pt.runtime.TaskID;//####[17]####
import pt.runtime.TaskIDGroup;//####[18]####
import java.nio.file.Files;//####[20]####
import java.nio.file.Paths;//####[21]####
import GUI.UI;//####[22]####
import java.util.*;//####[24]####
//####[24]####
//-- ParaTask related imports//####[24]####
import pt.runtime.*;//####[24]####
import java.util.concurrent.ExecutionException;//####[24]####
import java.util.concurrent.locks.*;//####[24]####
import java.lang.reflect.*;//####[24]####
import pt.runtime.GuiThread;//####[24]####
import java.util.concurrent.BlockingQueue;//####[24]####
import java.util.ArrayList;//####[24]####
import java.util.List;//####[24]####
//####[24]####
public class ParallelProcessor extends SwingWorker<Void, Integer> {//####[26]####
    static{ParaTask.init();}//####[26]####
    /*  ParaTask helper method to access private/protected slots *///####[26]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[26]####
        if (m.getParameterTypes().length == 0)//####[26]####
            m.invoke(instance);//####[26]####
        else if ((m.getParameterTypes().length == 1))//####[26]####
            m.invoke(instance, arg);//####[26]####
        else //####[26]####
            m.invoke(instance, arg, interResult);//####[26]####
    }//####[26]####
//####[28]####
    private ArrayList<String> a = new ArrayList<String>();//####[28]####
//####[29]####
    private Iterator<String> subVideoNames;//####[29]####
//####[30]####
    public long startTime;//####[30]####
//####[31]####
    public long time;//####[31]####
//####[32]####
    private String filter;//####[32]####
//####[33]####
    private UI ui;//####[33]####
//####[34]####
    private int id;//####[34]####
//####[35]####
    private String file;//####[35]####
//####[36]####
    private String outputFile;//####[36]####
//####[38]####
    public ParallelProcessor(String file, String filter, UI ui, int id) {//####[38]####
        this.file = file;//####[39]####
        String ext = "";//####[40]####
        int i = file.lastIndexOf('.');//####[41]####
        if (i > 0) //####[42]####
        {//####[42]####
            ext += file.substring(i + 1);//####[43]####
        }//####[44]####
        outputFile = ui.outputVideo + "." + ext;//####[45]####
        this.filter = filter;//####[46]####
        this.ui = ui;//####[47]####
        this.id = id;//####[48]####
    }//####[49]####
//####[52]####
    @Override//####[52]####
    public void done() {//####[52]####
        ui.Paraprocessors.remove(this);//####[53]####
        ui.progressBars.get(id).setValue(100);//####[54]####
        time = System.currentTimeMillis() - startTime;//####[55]####
        System.out.println("Video filtering for video " + id + " took " + (time / 1000) + " seconds.");//####[56]####
        JOptionPane.showMessageDialog(ui, "Finished Saving Video: " + id + "\nTime taken: " + (time / 1000) + " seconds.");//####[57]####
        System.out.println(id);//####[58]####
        System.out.println("done!!!!!");//####[59]####
        ui.progressBars.remove(id);//####[60]####
        ui.processingInfo.remove(id);//####[61]####
    }//####[62]####
//####[64]####
    public void addFilter(VideoFilter processor, String filter) {//####[64]####
        processor.initializeFilter(filter);//####[65]####
        processor.start();//####[66]####
    }//####[67]####
//####[69]####
    private static volatile Method __pt__startSpliting_String_method = null;//####[69]####
    private synchronized static void __pt__startSpliting_String_ensureMethodVarSet() {//####[69]####
        if (__pt__startSpliting_String_method == null) {//####[69]####
            try {//####[69]####
                __pt__startSpliting_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startSpliting", new Class[] {//####[69]####
                    String.class//####[69]####
                });//####[69]####
            } catch (Exception e) {//####[69]####
                e.printStackTrace();//####[69]####
            }//####[69]####
        }//####[69]####
    }//####[69]####
    public TaskID<Void> startSpliting(String file) {//####[70]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[70]####
        return startSpliting(file, new TaskInfo());//####[70]####
    }//####[70]####
    public TaskID<Void> startSpliting(String file, TaskInfo taskinfo) {//####[70]####
        // ensure Method variable is set//####[70]####
        if (__pt__startSpliting_String_method == null) {//####[70]####
            __pt__startSpliting_String_ensureMethodVarSet();//####[70]####
        }//####[70]####
        taskinfo.setParameters(file);//####[70]####
        taskinfo.setMethod(__pt__startSpliting_String_method);//####[70]####
        taskinfo.setInstance(this);//####[70]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[70]####
    }//####[70]####
    public TaskID<Void> startSpliting(TaskID<String> file) {//####[70]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[70]####
        return startSpliting(file, new TaskInfo());//####[70]####
    }//####[70]####
    public TaskID<Void> startSpliting(TaskID<String> file, TaskInfo taskinfo) {//####[70]####
        // ensure Method variable is set//####[70]####
        if (__pt__startSpliting_String_method == null) {//####[70]####
            __pt__startSpliting_String_ensureMethodVarSet();//####[70]####
        }//####[70]####
        taskinfo.setTaskIdArgIndexes(0);//####[70]####
        taskinfo.addDependsOn(file);//####[70]####
        taskinfo.setParameters(file);//####[70]####
        taskinfo.setMethod(__pt__startSpliting_String_method);//####[70]####
        taskinfo.setInstance(this);//####[70]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[70]####
    }//####[70]####
    public TaskID<Void> startSpliting(BlockingQueue<String> file) {//####[70]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[70]####
        return startSpliting(file, new TaskInfo());//####[70]####
    }//####[70]####
    public TaskID<Void> startSpliting(BlockingQueue<String> file, TaskInfo taskinfo) {//####[70]####
        // ensure Method variable is set//####[70]####
        if (__pt__startSpliting_String_method == null) {//####[70]####
            __pt__startSpliting_String_ensureMethodVarSet();//####[70]####
        }//####[70]####
        taskinfo.setQueueArgIndexes(0);//####[70]####
        taskinfo.setIsPipeline(true);//####[70]####
        taskinfo.setParameters(file);//####[70]####
        taskinfo.setMethod(__pt__startSpliting_String_method);//####[70]####
        taskinfo.setInstance(this);//####[70]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[70]####
    }//####[70]####
    public void __pt__startSpliting(String file) {//####[70]####
        try {//####[71]####
            TaskID id1 = splitVideo(file);//####[72]####
            TaskIDGroup g = new TaskIDGroup(1);//####[73]####
            g.add(id1);//####[74]####
            System.out.println("** Going to wait for the tasks...");//####[75]####
            g.waitTillFinished();//####[76]####
            System.out.println("Done");//####[78]####
        } catch (Exception ee) {//####[80]####
        }//####[81]####
    }//####[82]####
//####[82]####
//####[84]####
    private static volatile Method __pt__splitVideo_String_method = null;//####[84]####
    private synchronized static void __pt__splitVideo_String_ensureMethodVarSet() {//####[84]####
        if (__pt__splitVideo_String_method == null) {//####[84]####
            try {//####[84]####
                __pt__splitVideo_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__splitVideo", new Class[] {//####[84]####
                    String.class//####[84]####
                });//####[84]####
            } catch (Exception e) {//####[84]####
                e.printStackTrace();//####[84]####
            }//####[84]####
        }//####[84]####
    }//####[84]####
    public TaskIDGroup<Void> splitVideo(String fileName) throws IOException {//####[85]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[85]####
        return splitVideo(fileName, new TaskInfo());//####[85]####
    }//####[85]####
    public TaskIDGroup<Void> splitVideo(String fileName, TaskInfo taskinfo) throws IOException {//####[85]####
        // ensure Method variable is set//####[85]####
        if (__pt__splitVideo_String_method == null) {//####[85]####
            __pt__splitVideo_String_ensureMethodVarSet();//####[85]####
        }//####[85]####
        taskinfo.setParameters(fileName);//####[85]####
        taskinfo.setMethod(__pt__splitVideo_String_method);//####[85]####
        taskinfo.setInstance(this);//####[85]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[85]####
    }//####[85]####
    public TaskIDGroup<Void> splitVideo(TaskID<String> fileName) throws IOException {//####[85]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[85]####
        return splitVideo(fileName, new TaskInfo());//####[85]####
    }//####[85]####
    public TaskIDGroup<Void> splitVideo(TaskID<String> fileName, TaskInfo taskinfo) throws IOException {//####[85]####
        // ensure Method variable is set//####[85]####
        if (__pt__splitVideo_String_method == null) {//####[85]####
            __pt__splitVideo_String_ensureMethodVarSet();//####[85]####
        }//####[85]####
        taskinfo.setTaskIdArgIndexes(0);//####[85]####
        taskinfo.addDependsOn(fileName);//####[85]####
        taskinfo.setParameters(fileName);//####[85]####
        taskinfo.setMethod(__pt__splitVideo_String_method);//####[85]####
        taskinfo.setInstance(this);//####[85]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[85]####
    }//####[85]####
    public TaskIDGroup<Void> splitVideo(BlockingQueue<String> fileName) throws IOException {//####[85]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[85]####
        return splitVideo(fileName, new TaskInfo());//####[85]####
    }//####[85]####
    public TaskIDGroup<Void> splitVideo(BlockingQueue<String> fileName, TaskInfo taskinfo) throws IOException {//####[85]####
        // ensure Method variable is set//####[85]####
        if (__pt__splitVideo_String_method == null) {//####[85]####
            __pt__splitVideo_String_ensureMethodVarSet();//####[85]####
        }//####[85]####
        taskinfo.setQueueArgIndexes(0);//####[85]####
        taskinfo.setIsPipeline(true);//####[85]####
        taskinfo.setParameters(fileName);//####[85]####
        taskinfo.setMethod(__pt__splitVideo_String_method);//####[85]####
        taskinfo.setInstance(this);//####[85]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[85]####
    }//####[85]####
    public void __pt__splitVideo(String fileName) throws IOException {//####[85]####
        VideoSpliter vs = new VideoSpliter(fileName, id);//####[86]####
        String duration = vs.getVideoDuration(fileName);//####[89]####
        int durationInMs = vs.transferDuration(duration);//####[90]####
        int partitionedInMs = vs.partition(durationInMs, Runtime.getRuntime().availableProcessors());//####[91]####
        String partitionedDur = vs.transferMsToDuration(partitionedInMs);//####[92]####
        Iterator<String> it = vs.generateCommandLines(Runtime.getRuntime().availableProcessors(), partitionedInMs, partitionedDur).iterator();//####[94]####
        vs.doRealSplittingWork(it);//####[95]####
    }//####[97]####
//####[97]####
//####[99]####
    private static volatile Method __pt__getVideoFiles__method = null;//####[99]####
    private synchronized static void __pt__getVideoFiles__ensureMethodVarSet() {//####[99]####
        if (__pt__getVideoFiles__method == null) {//####[99]####
            try {//####[99]####
                __pt__getVideoFiles__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__getVideoFiles", new Class[] {//####[99]####
                    //####[99]####
                });//####[99]####
            } catch (Exception e) {//####[99]####
                e.printStackTrace();//####[99]####
            }//####[99]####
        }//####[99]####
    }//####[99]####
    public TaskID<Void> getVideoFiles() throws IOException {//####[100]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[100]####
        return getVideoFiles(new TaskInfo());//####[100]####
    }//####[100]####
    public TaskID<Void> getVideoFiles(TaskInfo taskinfo) throws IOException {//####[100]####
        // ensure Method variable is set//####[100]####
        if (__pt__getVideoFiles__method == null) {//####[100]####
            __pt__getVideoFiles__ensureMethodVarSet();//####[100]####
        }//####[100]####
        taskinfo.setParameters();//####[100]####
        taskinfo.setMethod(__pt__getVideoFiles__method);//####[100]####
        taskinfo.setInstance(this);//####[100]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[100]####
    }//####[100]####
    public void __pt__getVideoFiles() throws IOException {//####[100]####
        System.out.println("start");//####[102]####
        File[] listOfFiles = new File("SubVideos").listFiles();//####[103]####
        ArrayList<String> videoNames = new ArrayList<String>();//####[104]####
        for (File listOfFile : listOfFiles) //####[105]####
        {//####[105]####
            if (listOfFile.isFile()) //####[106]####
            {//####[107]####
                videoNames.add(listOfFile.getName());//####[108]####
                System.out.println(listOfFile.getName());//####[109]####
            }//####[110]####
        }//####[111]####
        subVideoNames = videoNames.iterator();//####[112]####
    }//####[113]####
//####[113]####
//####[115]####
    private static volatile Method __pt__startFiltering_String_method = null;//####[115]####
    private synchronized static void __pt__startFiltering_String_ensureMethodVarSet() {//####[115]####
        if (__pt__startFiltering_String_method == null) {//####[115]####
            try {//####[115]####
                __pt__startFiltering_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startFiltering", new Class[] {//####[115]####
                    String.class//####[115]####
                });//####[115]####
            } catch (Exception e) {//####[115]####
                e.printStackTrace();//####[115]####
            }//####[115]####
        }//####[115]####
    }//####[115]####
    public TaskID<Void> startFiltering(String filter) {//####[116]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[116]####
        return startFiltering(filter, new TaskInfo());//####[116]####
    }//####[116]####
    public TaskID<Void> startFiltering(String filter, TaskInfo taskinfo) {//####[116]####
        // ensure Method variable is set//####[116]####
        if (__pt__startFiltering_String_method == null) {//####[116]####
            __pt__startFiltering_String_ensureMethodVarSet();//####[116]####
        }//####[116]####
        taskinfo.setParameters(filter);//####[116]####
        taskinfo.setMethod(__pt__startFiltering_String_method);//####[116]####
        taskinfo.setInstance(this);//####[116]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[116]####
    }//####[116]####
    public TaskID<Void> startFiltering(TaskID<String> filter) {//####[116]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[116]####
        return startFiltering(filter, new TaskInfo());//####[116]####
    }//####[116]####
    public TaskID<Void> startFiltering(TaskID<String> filter, TaskInfo taskinfo) {//####[116]####
        // ensure Method variable is set//####[116]####
        if (__pt__startFiltering_String_method == null) {//####[116]####
            __pt__startFiltering_String_ensureMethodVarSet();//####[116]####
        }//####[116]####
        taskinfo.setTaskIdArgIndexes(0);//####[116]####
        taskinfo.addDependsOn(filter);//####[116]####
        taskinfo.setParameters(filter);//####[116]####
        taskinfo.setMethod(__pt__startFiltering_String_method);//####[116]####
        taskinfo.setInstance(this);//####[116]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[116]####
    }//####[116]####
    public TaskID<Void> startFiltering(BlockingQueue<String> filter) {//####[116]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[116]####
        return startFiltering(filter, new TaskInfo());//####[116]####
    }//####[116]####
    public TaskID<Void> startFiltering(BlockingQueue<String> filter, TaskInfo taskinfo) {//####[116]####
        // ensure Method variable is set//####[116]####
        if (__pt__startFiltering_String_method == null) {//####[116]####
            __pt__startFiltering_String_ensureMethodVarSet();//####[116]####
        }//####[116]####
        taskinfo.setQueueArgIndexes(0);//####[116]####
        taskinfo.setIsPipeline(true);//####[116]####
        taskinfo.setParameters(filter);//####[116]####
        taskinfo.setMethod(__pt__startFiltering_String_method);//####[116]####
        taskinfo.setInstance(this);//####[116]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[116]####
    }//####[116]####
    public void __pt__startFiltering(String filter) {//####[116]####
        try {//####[117]####
            TaskID id2 = addFilterToSubVideos(filter);//####[118]####
            TaskIDGroup gg = new TaskIDGroup(1);//####[119]####
            gg.add(id2);//####[120]####
            gg.waitTillFinished();//####[121]####
            System.out.println("** Finished...");//####[122]####
        } catch (Exception ee) {//####[124]####
        }//####[125]####
    }//####[127]####
//####[127]####
//####[129]####
    private static volatile Method __pt__addFilterToSubVideos_String_method = null;//####[129]####
    private synchronized static void __pt__addFilterToSubVideos_String_ensureMethodVarSet() {//####[129]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[129]####
            try {//####[129]####
                __pt__addFilterToSubVideos_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__addFilterToSubVideos", new Class[] {//####[129]####
                    String.class//####[129]####
                });//####[129]####
            } catch (Exception e) {//####[129]####
                e.printStackTrace();//####[129]####
            }//####[129]####
        }//####[129]####
    }//####[129]####
    public TaskIDGroup<Void> addFilterToSubVideos(String filter) {//####[130]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[130]####
        return addFilterToSubVideos(filter, new TaskInfo());//####[130]####
    }//####[130]####
    public TaskIDGroup<Void> addFilterToSubVideos(String filter, TaskInfo taskinfo) {//####[130]####
        // ensure Method variable is set//####[130]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[130]####
            __pt__addFilterToSubVideos_String_ensureMethodVarSet();//####[130]####
        }//####[130]####
        taskinfo.setParameters(filter);//####[130]####
        taskinfo.setMethod(__pt__addFilterToSubVideos_String_method);//####[130]####
        taskinfo.setInstance(this);//####[130]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[130]####
    }//####[130]####
    public TaskIDGroup<Void> addFilterToSubVideos(TaskID<String> filter) {//####[130]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[130]####
        return addFilterToSubVideos(filter, new TaskInfo());//####[130]####
    }//####[130]####
    public TaskIDGroup<Void> addFilterToSubVideos(TaskID<String> filter, TaskInfo taskinfo) {//####[130]####
        // ensure Method variable is set//####[130]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[130]####
            __pt__addFilterToSubVideos_String_ensureMethodVarSet();//####[130]####
        }//####[130]####
        taskinfo.setTaskIdArgIndexes(0);//####[130]####
        taskinfo.addDependsOn(filter);//####[130]####
        taskinfo.setParameters(filter);//####[130]####
        taskinfo.setMethod(__pt__addFilterToSubVideos_String_method);//####[130]####
        taskinfo.setInstance(this);//####[130]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[130]####
    }//####[130]####
    public TaskIDGroup<Void> addFilterToSubVideos(BlockingQueue<String> filter) {//####[130]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[130]####
        return addFilterToSubVideos(filter, new TaskInfo());//####[130]####
    }//####[130]####
    public TaskIDGroup<Void> addFilterToSubVideos(BlockingQueue<String> filter, TaskInfo taskinfo) {//####[130]####
        // ensure Method variable is set//####[130]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[130]####
            __pt__addFilterToSubVideos_String_ensureMethodVarSet();//####[130]####
        }//####[130]####
        taskinfo.setQueueArgIndexes(0);//####[130]####
        taskinfo.setIsPipeline(true);//####[130]####
        taskinfo.setParameters(filter);//####[130]####
        taskinfo.setMethod(__pt__addFilterToSubVideos_String_method);//####[130]####
        taskinfo.setInstance(this);//####[130]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[130]####
    }//####[130]####
    public void __pt__addFilterToSubVideos(String filter) {//####[130]####
        while (subVideoNames.hasNext()) //####[131]####
        addFilter(new VideoFilter("SubVideos/" + subVideoNames.next(), ui, id), filter);//####[132]####
    }//####[133]####
//####[133]####
//####[135]####
    private static volatile Method __pt__combine__method = null;//####[135]####
    private synchronized static void __pt__combine__ensureMethodVarSet() {//####[135]####
        if (__pt__combine__method == null) {//####[135]####
            try {//####[135]####
                __pt__combine__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__combine", new Class[] {//####[135]####
                    //####[135]####
                });//####[135]####
            } catch (Exception e) {//####[135]####
                e.printStackTrace();//####[135]####
            }//####[135]####
        }//####[135]####
    }//####[135]####
    public TaskID<Void> combine() {//####[136]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[136]####
        return combine(new TaskInfo());//####[136]####
    }//####[136]####
    public TaskID<Void> combine(TaskInfo taskinfo) {//####[136]####
        // ensure Method variable is set//####[136]####
        if (__pt__combine__method == null) {//####[136]####
            __pt__combine__ensureMethodVarSet();//####[136]####
        }//####[136]####
        taskinfo.setParameters();//####[136]####
        taskinfo.setMethod(__pt__combine__method);//####[136]####
        taskinfo.setInstance(this);//####[136]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[136]####
    }//####[136]####
    public void __pt__combine() {//####[136]####
        new VideoCombiner(outputFile, id).combine();//####[137]####
        long endTime = System.currentTimeMillis();//####[138]####
        long totalTime = endTime - startTime;//####[139]####
        System.out.println("Duration: " + totalTime + " ms");//####[140]####
    }//####[141]####
//####[141]####
//####[143]####
    private static volatile Method __pt__recordSubVideoNames__method = null;//####[143]####
    private synchronized static void __pt__recordSubVideoNames__ensureMethodVarSet() {//####[143]####
        if (__pt__recordSubVideoNames__method == null) {//####[143]####
            try {//####[143]####
                __pt__recordSubVideoNames__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__recordSubVideoNames", new Class[] {//####[143]####
                    //####[143]####
                });//####[143]####
            } catch (Exception e) {//####[143]####
                e.printStackTrace();//####[143]####
            }//####[143]####
        }//####[143]####
    }//####[143]####
    public TaskID<Void> recordSubVideoNames() {//####[144]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[144]####
        return recordSubVideoNames(new TaskInfo());//####[144]####
    }//####[144]####
    public TaskID<Void> recordSubVideoNames(TaskInfo taskinfo) {//####[144]####
        // ensure Method variable is set//####[144]####
        if (__pt__recordSubVideoNames__method == null) {//####[144]####
            __pt__recordSubVideoNames__ensureMethodVarSet();//####[144]####
        }//####[144]####
        taskinfo.setParameters();//####[144]####
        taskinfo.setMethod(__pt__recordSubVideoNames__method);//####[144]####
        taskinfo.setInstance(this);//####[144]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[144]####
    }//####[144]####
    public void __pt__recordSubVideoNames() {//####[144]####
        try {//####[145]####
            TaskID id4 = getVideoFiles();//####[146]####
            TaskIDGroup gg = new TaskIDGroup(1);//####[147]####
            gg.add(id4);//####[148]####
            gg.waitTillFinished();//####[149]####
            System.out.println("** Finished saving files ...");//####[150]####
        } catch (Exception ee) {//####[152]####
        }//####[153]####
    }//####[154]####
//####[154]####
//####[157]####
    @Override//####[157]####
    protected Void doInBackground() throws Exception {//####[157]####
        startTime = System.currentTimeMillis();//####[158]####
        TaskID split = startSpliting(file);//####[159]####
        split.waitTillFinished();//####[160]####
        TaskInfo __pt__recordNames = new TaskInfo();//####[161]####
//####[161]####
        /*  -- ParaTask dependsOn clause for 'recordNames' -- *///####[161]####
        __pt__recordNames.addDependsOn(split);//####[161]####
//####[161]####
        TaskID recordNames = recordSubVideoNames(__pt__recordNames);//####[161]####
        TaskInfo __pt__filtered = new TaskInfo();//####[162]####
//####[162]####
        /*  -- ParaTask dependsOn clause for 'filtered' -- *///####[162]####
        __pt__filtered.addDependsOn(recordNames);//####[162]####
//####[162]####
        TaskID filtered = startFiltering(filter, __pt__filtered);//####[162]####
        TaskInfo __pt__combined = new TaskInfo();//####[163]####
//####[163]####
        /*  -- ParaTask dependsOn clause for 'combined' -- *///####[163]####
        __pt__combined.addDependsOn(filtered);//####[163]####
//####[163]####
        boolean isEDT = GuiThread.isEventDispatchThread();//####[163]####
//####[163]####
//####[163]####
        /*  -- ParaTask notify clause for 'combined' -- *///####[163]####
        try {//####[163]####
            Method __pt__combined_slot_0 = null;//####[163]####
            __pt__combined_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "done", new Class[] {});//####[163]####
            if (false) done(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[163]####
            __pt__combined.addSlotToNotify(new Slot(__pt__combined_slot_0, this, false));//####[163]####
//####[163]####
        } catch(Exception __pt__e) { //####[163]####
            System.err.println("Problem registering method in clause:");//####[163]####
            __pt__e.printStackTrace();//####[163]####
        }//####[163]####
        TaskID combined = combine(__pt__combined);//####[163]####
        combined.waitTillFinished();//####[164]####
        return null;//####[165]####
    }//####[166]####
}//####[166]####
