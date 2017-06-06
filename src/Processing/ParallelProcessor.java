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
        time = System.currentTimeMillis() - startTime;//####[53]####
        System.out.println("Video filtering for video " + id + " took " + (time / 1000) + " seconds.");//####[54]####
        JOptionPane.showMessageDialog(ui, "Finished Saving Video: " + id + "\nTime taken: " + (time / 1000) + " seconds.");//####[55]####
        System.out.println(id);//####[56]####
        System.out.println("done!!!!!");//####[57]####
    }//####[58]####
//####[60]####
    public void addFilter(VideoFilter processor, String filter) {//####[60]####
        processor.initializeFilter(filter);//####[61]####
        processor.start();//####[62]####
    }//####[63]####
//####[65]####
    private static volatile Method __pt__startSpliting_String_method = null;//####[65]####
    private synchronized static void __pt__startSpliting_String_ensureMethodVarSet() {//####[65]####
        if (__pt__startSpliting_String_method == null) {//####[65]####
            try {//####[65]####
                __pt__startSpliting_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startSpliting", new Class[] {//####[65]####
                    String.class//####[65]####
                });//####[65]####
            } catch (Exception e) {//####[65]####
                e.printStackTrace();//####[65]####
            }//####[65]####
        }//####[65]####
    }//####[65]####
    public TaskID<Void> startSpliting(String file) {//####[66]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[66]####
        return startSpliting(file, new TaskInfo());//####[66]####
    }//####[66]####
    public TaskID<Void> startSpliting(String file, TaskInfo taskinfo) {//####[66]####
        // ensure Method variable is set//####[66]####
        if (__pt__startSpliting_String_method == null) {//####[66]####
            __pt__startSpliting_String_ensureMethodVarSet();//####[66]####
        }//####[66]####
        taskinfo.setParameters(file);//####[66]####
        taskinfo.setMethod(__pt__startSpliting_String_method);//####[66]####
        taskinfo.setInstance(this);//####[66]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[66]####
    }//####[66]####
    public TaskID<Void> startSpliting(TaskID<String> file) {//####[66]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[66]####
        return startSpliting(file, new TaskInfo());//####[66]####
    }//####[66]####
    public TaskID<Void> startSpliting(TaskID<String> file, TaskInfo taskinfo) {//####[66]####
        // ensure Method variable is set//####[66]####
        if (__pt__startSpliting_String_method == null) {//####[66]####
            __pt__startSpliting_String_ensureMethodVarSet();//####[66]####
        }//####[66]####
        taskinfo.setTaskIdArgIndexes(0);//####[66]####
        taskinfo.addDependsOn(file);//####[66]####
        taskinfo.setParameters(file);//####[66]####
        taskinfo.setMethod(__pt__startSpliting_String_method);//####[66]####
        taskinfo.setInstance(this);//####[66]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[66]####
    }//####[66]####
    public TaskID<Void> startSpliting(BlockingQueue<String> file) {//####[66]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[66]####
        return startSpliting(file, new TaskInfo());//####[66]####
    }//####[66]####
    public TaskID<Void> startSpliting(BlockingQueue<String> file, TaskInfo taskinfo) {//####[66]####
        // ensure Method variable is set//####[66]####
        if (__pt__startSpliting_String_method == null) {//####[66]####
            __pt__startSpliting_String_ensureMethodVarSet();//####[66]####
        }//####[66]####
        taskinfo.setQueueArgIndexes(0);//####[66]####
        taskinfo.setIsPipeline(true);//####[66]####
        taskinfo.setParameters(file);//####[66]####
        taskinfo.setMethod(__pt__startSpliting_String_method);//####[66]####
        taskinfo.setInstance(this);//####[66]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[66]####
    }//####[66]####
    public void __pt__startSpliting(String file) {//####[66]####
        try {//####[67]####
            TaskID id1 = splitVideo(file);//####[68]####
            TaskIDGroup g = new TaskIDGroup(1);//####[69]####
            g.add(id1);//####[70]####
            System.out.println("** Going to wait for the tasks...");//####[71]####
            g.waitTillFinished();//####[72]####
            System.out.println("Done");//####[74]####
        } catch (Exception ee) {//####[76]####
        }//####[77]####
    }//####[78]####
//####[78]####
//####[80]####
    private static volatile Method __pt__splitVideo_String_method = null;//####[80]####
    private synchronized static void __pt__splitVideo_String_ensureMethodVarSet() {//####[80]####
        if (__pt__splitVideo_String_method == null) {//####[80]####
            try {//####[80]####
                __pt__splitVideo_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__splitVideo", new Class[] {//####[80]####
                    String.class//####[80]####
                });//####[80]####
            } catch (Exception e) {//####[80]####
                e.printStackTrace();//####[80]####
            }//####[80]####
        }//####[80]####
    }//####[80]####
    public TaskIDGroup<Void> splitVideo(String fileName) throws IOException {//####[81]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[81]####
        return splitVideo(fileName, new TaskInfo());//####[81]####
    }//####[81]####
    public TaskIDGroup<Void> splitVideo(String fileName, TaskInfo taskinfo) throws IOException {//####[81]####
        // ensure Method variable is set//####[81]####
        if (__pt__splitVideo_String_method == null) {//####[81]####
            __pt__splitVideo_String_ensureMethodVarSet();//####[81]####
        }//####[81]####
        taskinfo.setParameters(fileName);//####[81]####
        taskinfo.setMethod(__pt__splitVideo_String_method);//####[81]####
        taskinfo.setInstance(this);//####[81]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[81]####
    }//####[81]####
    public TaskIDGroup<Void> splitVideo(TaskID<String> fileName) throws IOException {//####[81]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[81]####
        return splitVideo(fileName, new TaskInfo());//####[81]####
    }//####[81]####
    public TaskIDGroup<Void> splitVideo(TaskID<String> fileName, TaskInfo taskinfo) throws IOException {//####[81]####
        // ensure Method variable is set//####[81]####
        if (__pt__splitVideo_String_method == null) {//####[81]####
            __pt__splitVideo_String_ensureMethodVarSet();//####[81]####
        }//####[81]####
        taskinfo.setTaskIdArgIndexes(0);//####[81]####
        taskinfo.addDependsOn(fileName);//####[81]####
        taskinfo.setParameters(fileName);//####[81]####
        taskinfo.setMethod(__pt__splitVideo_String_method);//####[81]####
        taskinfo.setInstance(this);//####[81]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[81]####
    }//####[81]####
    public TaskIDGroup<Void> splitVideo(BlockingQueue<String> fileName) throws IOException {//####[81]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[81]####
        return splitVideo(fileName, new TaskInfo());//####[81]####
    }//####[81]####
    public TaskIDGroup<Void> splitVideo(BlockingQueue<String> fileName, TaskInfo taskinfo) throws IOException {//####[81]####
        // ensure Method variable is set//####[81]####
        if (__pt__splitVideo_String_method == null) {//####[81]####
            __pt__splitVideo_String_ensureMethodVarSet();//####[81]####
        }//####[81]####
        taskinfo.setQueueArgIndexes(0);//####[81]####
        taskinfo.setIsPipeline(true);//####[81]####
        taskinfo.setParameters(fileName);//####[81]####
        taskinfo.setMethod(__pt__splitVideo_String_method);//####[81]####
        taskinfo.setInstance(this);//####[81]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[81]####
    }//####[81]####
    public void __pt__splitVideo(String fileName) throws IOException {//####[81]####
        VideoSpliter vs = new VideoSpliter(fileName, id);//####[82]####
        String duration = vs.getVideoDuration(fileName);//####[85]####
        int durationInMs = vs.transferDuration(duration);//####[86]####
        int partitionedInMs = vs.partition(durationInMs, Runtime.getRuntime().availableProcessors());//####[87]####
        String partitionedDur = vs.transferMsToDuration(partitionedInMs);//####[88]####
        Iterator<String> it = vs.generateCommandLines(Runtime.getRuntime().availableProcessors(), partitionedInMs, partitionedDur).iterator();//####[90]####
        vs.doRealSplittingWork(it);//####[91]####
    }//####[93]####
//####[93]####
//####[95]####
    private static volatile Method __pt__getVideoFiles__method = null;//####[95]####
    private synchronized static void __pt__getVideoFiles__ensureMethodVarSet() {//####[95]####
        if (__pt__getVideoFiles__method == null) {//####[95]####
            try {//####[95]####
                __pt__getVideoFiles__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__getVideoFiles", new Class[] {//####[95]####
                    //####[95]####
                });//####[95]####
            } catch (Exception e) {//####[95]####
                e.printStackTrace();//####[95]####
            }//####[95]####
        }//####[95]####
    }//####[95]####
    public TaskID<Void> getVideoFiles() throws IOException {//####[96]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[96]####
        return getVideoFiles(new TaskInfo());//####[96]####
    }//####[96]####
    public TaskID<Void> getVideoFiles(TaskInfo taskinfo) throws IOException {//####[96]####
        // ensure Method variable is set//####[96]####
        if (__pt__getVideoFiles__method == null) {//####[96]####
            __pt__getVideoFiles__ensureMethodVarSet();//####[96]####
        }//####[96]####
        taskinfo.setParameters();//####[96]####
        taskinfo.setMethod(__pt__getVideoFiles__method);//####[96]####
        taskinfo.setInstance(this);//####[96]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[96]####
    }//####[96]####
    public void __pt__getVideoFiles() throws IOException {//####[96]####
        System.out.println("start");//####[98]####
        File[] listOfFiles = new File("SubVideos" + id).listFiles();//####[99]####
        ArrayList<String> videoNames = new ArrayList<String>();//####[100]####
        for (File listOfFile : listOfFiles) //####[101]####
        {//####[101]####
            if (listOfFile.isFile()) //####[102]####
            {//####[103]####
                videoNames.add(listOfFile.getName());//####[104]####
                System.out.println(listOfFile.getName());//####[105]####
            }//####[106]####
        }//####[107]####
        subVideoNames = videoNames.iterator();//####[108]####
    }//####[109]####
//####[109]####
//####[111]####
    private static volatile Method __pt__startFiltering_String_method = null;//####[111]####
    private synchronized static void __pt__startFiltering_String_ensureMethodVarSet() {//####[111]####
        if (__pt__startFiltering_String_method == null) {//####[111]####
            try {//####[111]####
                __pt__startFiltering_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startFiltering", new Class[] {//####[111]####
                    String.class//####[111]####
                });//####[111]####
            } catch (Exception e) {//####[111]####
                e.printStackTrace();//####[111]####
            }//####[111]####
        }//####[111]####
    }//####[111]####
    public TaskID<Void> startFiltering(String filter) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return startFiltering(filter, new TaskInfo());//####[112]####
    }//####[112]####
    public TaskID<Void> startFiltering(String filter, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__startFiltering_String_method == null) {//####[112]####
            __pt__startFiltering_String_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setParameters(filter);//####[112]####
        taskinfo.setMethod(__pt__startFiltering_String_method);//####[112]####
        taskinfo.setInstance(this);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    public TaskID<Void> startFiltering(TaskID<String> filter) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return startFiltering(filter, new TaskInfo());//####[112]####
    }//####[112]####
    public TaskID<Void> startFiltering(TaskID<String> filter, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__startFiltering_String_method == null) {//####[112]####
            __pt__startFiltering_String_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setTaskIdArgIndexes(0);//####[112]####
        taskinfo.addDependsOn(filter);//####[112]####
        taskinfo.setParameters(filter);//####[112]####
        taskinfo.setMethod(__pt__startFiltering_String_method);//####[112]####
        taskinfo.setInstance(this);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    public TaskID<Void> startFiltering(BlockingQueue<String> filter) {//####[112]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[112]####
        return startFiltering(filter, new TaskInfo());//####[112]####
    }//####[112]####
    public TaskID<Void> startFiltering(BlockingQueue<String> filter, TaskInfo taskinfo) {//####[112]####
        // ensure Method variable is set//####[112]####
        if (__pt__startFiltering_String_method == null) {//####[112]####
            __pt__startFiltering_String_ensureMethodVarSet();//####[112]####
        }//####[112]####
        taskinfo.setQueueArgIndexes(0);//####[112]####
        taskinfo.setIsPipeline(true);//####[112]####
        taskinfo.setParameters(filter);//####[112]####
        taskinfo.setMethod(__pt__startFiltering_String_method);//####[112]####
        taskinfo.setInstance(this);//####[112]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[112]####
    }//####[112]####
    public void __pt__startFiltering(String filter) {//####[112]####
        try {//####[113]####
            TaskID id2 = addFilterToSubVideos(filter);//####[114]####
            TaskIDGroup gg = new TaskIDGroup(1);//####[115]####
            gg.add(id2);//####[116]####
            gg.waitTillFinished();//####[117]####
            System.out.println("** Finished...");//####[118]####
        } catch (Exception ee) {//####[120]####
        }//####[121]####
    }//####[123]####
//####[123]####
//####[125]####
    private static volatile Method __pt__addFilterToSubVideos_String_method = null;//####[125]####
    private synchronized static void __pt__addFilterToSubVideos_String_ensureMethodVarSet() {//####[125]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[125]####
            try {//####[125]####
                __pt__addFilterToSubVideos_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__addFilterToSubVideos", new Class[] {//####[125]####
                    String.class//####[125]####
                });//####[125]####
            } catch (Exception e) {//####[125]####
                e.printStackTrace();//####[125]####
            }//####[125]####
        }//####[125]####
    }//####[125]####
    public TaskIDGroup<Void> addFilterToSubVideos(String filter) {//####[126]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[126]####
        return addFilterToSubVideos(filter, new TaskInfo());//####[126]####
    }//####[126]####
    public TaskIDGroup<Void> addFilterToSubVideos(String filter, TaskInfo taskinfo) {//####[126]####
        // ensure Method variable is set//####[126]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[126]####
            __pt__addFilterToSubVideos_String_ensureMethodVarSet();//####[126]####
        }//####[126]####
        taskinfo.setParameters(filter);//####[126]####
        taskinfo.setMethod(__pt__addFilterToSubVideos_String_method);//####[126]####
        taskinfo.setInstance(this);//####[126]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[126]####
    }//####[126]####
    public TaskIDGroup<Void> addFilterToSubVideos(TaskID<String> filter) {//####[126]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[126]####
        return addFilterToSubVideos(filter, new TaskInfo());//####[126]####
    }//####[126]####
    public TaskIDGroup<Void> addFilterToSubVideos(TaskID<String> filter, TaskInfo taskinfo) {//####[126]####
        // ensure Method variable is set//####[126]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[126]####
            __pt__addFilterToSubVideos_String_ensureMethodVarSet();//####[126]####
        }//####[126]####
        taskinfo.setTaskIdArgIndexes(0);//####[126]####
        taskinfo.addDependsOn(filter);//####[126]####
        taskinfo.setParameters(filter);//####[126]####
        taskinfo.setMethod(__pt__addFilterToSubVideos_String_method);//####[126]####
        taskinfo.setInstance(this);//####[126]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[126]####
    }//####[126]####
    public TaskIDGroup<Void> addFilterToSubVideos(BlockingQueue<String> filter) {//####[126]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[126]####
        return addFilterToSubVideos(filter, new TaskInfo());//####[126]####
    }//####[126]####
    public TaskIDGroup<Void> addFilterToSubVideos(BlockingQueue<String> filter, TaskInfo taskinfo) {//####[126]####
        // ensure Method variable is set//####[126]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[126]####
            __pt__addFilterToSubVideos_String_ensureMethodVarSet();//####[126]####
        }//####[126]####
        taskinfo.setQueueArgIndexes(0);//####[126]####
        taskinfo.setIsPipeline(true);//####[126]####
        taskinfo.setParameters(filter);//####[126]####
        taskinfo.setMethod(__pt__addFilterToSubVideos_String_method);//####[126]####
        taskinfo.setInstance(this);//####[126]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[126]####
    }//####[126]####
    public void __pt__addFilterToSubVideos(String filter) {//####[126]####
        while (subVideoNames.hasNext()) //####[127]####
        addFilter(new VideoFilter("SubVideos" + id + "/" + subVideoNames.next(), ui, id), filter);//####[128]####
    }//####[129]####
//####[129]####
//####[131]####
    private static volatile Method __pt__combine__method = null;//####[131]####
    private synchronized static void __pt__combine__ensureMethodVarSet() {//####[131]####
        if (__pt__combine__method == null) {//####[131]####
            try {//####[131]####
                __pt__combine__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__combine", new Class[] {//####[131]####
                    //####[131]####
                });//####[131]####
            } catch (Exception e) {//####[131]####
                e.printStackTrace();//####[131]####
            }//####[131]####
        }//####[131]####
    }//####[131]####
    public TaskID<Void> combine() {//####[132]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[132]####
        return combine(new TaskInfo());//####[132]####
    }//####[132]####
    public TaskID<Void> combine(TaskInfo taskinfo) {//####[132]####
        // ensure Method variable is set//####[132]####
        if (__pt__combine__method == null) {//####[132]####
            __pt__combine__ensureMethodVarSet();//####[132]####
        }//####[132]####
        taskinfo.setParameters();//####[132]####
        taskinfo.setMethod(__pt__combine__method);//####[132]####
        taskinfo.setInstance(this);//####[132]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[132]####
    }//####[132]####
    public void __pt__combine() {//####[132]####
        new VideoCombiner(outputFile, id).combine();//####[133]####
        long endTime = System.currentTimeMillis();//####[134]####
        long totalTime = endTime - startTime;//####[135]####
        System.out.println("Duration: " + totalTime + " ms");//####[136]####
    }//####[137]####
//####[137]####
//####[139]####
    private static volatile Method __pt__recordSubVideoNames__method = null;//####[139]####
    private synchronized static void __pt__recordSubVideoNames__ensureMethodVarSet() {//####[139]####
        if (__pt__recordSubVideoNames__method == null) {//####[139]####
            try {//####[139]####
                __pt__recordSubVideoNames__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__recordSubVideoNames", new Class[] {//####[139]####
                    //####[139]####
                });//####[139]####
            } catch (Exception e) {//####[139]####
                e.printStackTrace();//####[139]####
            }//####[139]####
        }//####[139]####
    }//####[139]####
    public TaskID<Void> recordSubVideoNames() {//####[140]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[140]####
        return recordSubVideoNames(new TaskInfo());//####[140]####
    }//####[140]####
    public TaskID<Void> recordSubVideoNames(TaskInfo taskinfo) {//####[140]####
        // ensure Method variable is set//####[140]####
        if (__pt__recordSubVideoNames__method == null) {//####[140]####
            __pt__recordSubVideoNames__ensureMethodVarSet();//####[140]####
        }//####[140]####
        taskinfo.setParameters();//####[140]####
        taskinfo.setMethod(__pt__recordSubVideoNames__method);//####[140]####
        taskinfo.setInstance(this);//####[140]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[140]####
    }//####[140]####
    public void __pt__recordSubVideoNames() {//####[140]####
        try {//####[141]####
            TaskID id4 = getVideoFiles();//####[142]####
            TaskIDGroup gg = new TaskIDGroup(1);//####[143]####
            gg.add(id4);//####[144]####
            gg.waitTillFinished();//####[145]####
            System.out.println("** Finished saving files ...");//####[146]####
        } catch (Exception ee) {//####[148]####
        }//####[149]####
    }//####[150]####
//####[150]####
//####[153]####
    @Override//####[153]####
    protected Void doInBackground() throws Exception {//####[153]####
        startTime = System.currentTimeMillis();//####[154]####
        TaskID splited = startSpliting(file);//####[155]####
        TaskInfo __pt__recordNames = new TaskInfo();//####[156]####
//####[156]####
        /*  -- ParaTask dependsOn clause for 'recordNames' -- *///####[156]####
        __pt__recordNames.addDependsOn(splited);//####[156]####
//####[156]####
        TaskID recordNames = recordSubVideoNames(__pt__recordNames);//####[156]####
        TaskInfo __pt__filtered = new TaskInfo();//####[157]####
//####[157]####
        /*  -- ParaTask dependsOn clause for 'filtered' -- *///####[157]####
        __pt__filtered.addDependsOn(recordNames);//####[157]####
//####[157]####
        TaskID filtered = startFiltering(filter, __pt__filtered);//####[157]####
        TaskInfo __pt__combined = new TaskInfo();//####[158]####
//####[158]####
        /*  -- ParaTask dependsOn clause for 'combined' -- *///####[158]####
        __pt__combined.addDependsOn(filtered);//####[158]####
//####[158]####
        boolean isEDT = GuiThread.isEventDispatchThread();//####[158]####
//####[158]####
//####[158]####
        /*  -- ParaTask notify clause for 'combined' -- *///####[158]####
        try {//####[158]####
            Method __pt__combined_slot_0 = null;//####[158]####
            __pt__combined_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "done", new Class[] {});//####[158]####
            if (false) done(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[158]####
            __pt__combined.addSlotToNotify(new Slot(__pt__combined_slot_0, this, false));//####[158]####
//####[158]####
        } catch(Exception __pt__e) { //####[158]####
            System.err.println("Problem registering method in clause:");//####[158]####
            __pt__e.printStackTrace();//####[158]####
        }//####[158]####
        TaskID combined = combine(__pt__combined);//####[158]####
        combined.waitTillFinished();//####[159]####
        return null;//####[160]####
    }//####[161]####
}//####[161]####
