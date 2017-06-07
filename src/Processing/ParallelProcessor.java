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
import controllers.*;//####[15]####
import pt.runtime.TaskID;//####[16]####
import pt.runtime.TaskIDGroup;//####[17]####
import java.nio.file.Files;//####[19]####
import java.nio.file.Paths;//####[20]####
import GUI.UI;//####[21]####
import java.util.*;//####[23]####
//####[23]####
//-- ParaTask related imports//####[23]####
import pt.runtime.*;//####[23]####
import java.util.concurrent.ExecutionException;//####[23]####
import java.util.concurrent.locks.*;//####[23]####
import java.lang.reflect.*;//####[23]####
import pt.runtime.GuiThread;//####[23]####
import java.util.concurrent.BlockingQueue;//####[23]####
import java.util.ArrayList;//####[23]####
import java.util.List;//####[23]####
//####[23]####
public class ParallelProcessor extends SwingWorker<Void, Integer> {//####[25]####
    static{ParaTask.init();}//####[25]####
    /*  ParaTask helper method to access private/protected slots *///####[25]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[25]####
        if (m.getParameterTypes().length == 0)//####[25]####
            m.invoke(instance);//####[25]####
        else if ((m.getParameterTypes().length == 1))//####[25]####
            m.invoke(instance, arg);//####[25]####
        else //####[25]####
            m.invoke(instance, arg, interResult);//####[25]####
    }//####[25]####
//####[27]####
    private ArrayList<String> a = new ArrayList<String>();//####[27]####
//####[28]####
    private Iterator<String> subVideoNames;//####[28]####
//####[29]####
    public long startTime;//####[29]####
//####[30]####
    public long time;//####[30]####
//####[31]####
    private String filter;//####[31]####
//####[32]####
    private UI ui;//####[32]####
//####[33]####
    public int id;//####[33]####
//####[34]####
    private File file;//####[34]####
//####[35]####
    private String ext, command;//####[35]####
//####[36]####
    private String outputFile;//####[36]####
//####[37]####
    private String[] cmd;//####[37]####
//####[39]####
    public ParallelProcessor(File file, String filter, UI ui, int id) {//####[39]####
        this.file = file;//####[40]####
        String fileName = file.getName();//####[41]####
        ext = "";//####[42]####
        int i = fileName.lastIndexOf('.');//####[43]####
        if (i > 0) //####[44]####
        {//####[44]####
            ext += fileName.substring(i + 1);//####[45]####
        }//####[46]####
        outputFile = ui.outputVideo + "." + ext;//####[47]####
        this.filter = filter;//####[48]####
        this.ui = ui;//####[49]####
        this.id = id;//####[50]####
        cmd = new String[2];//####[51]####
    }//####[52]####
//####[55]####
    @Override//####[55]####
    public void done() {//####[55]####
        for (int i = 0; i < 2; i++) //####[56]####
        {//####[56]####
            System.out.println(cmd[i]);//####[57]####
            try {//####[58]####
                Runtime.getRuntime().exec(cmd[i]);//####[59]####
            } catch (IOException e) {//####[60]####
                e.printStackTrace();//####[61]####
            }//####[62]####
        }//####[63]####
        ui.Paraprocessors.remove(this);//####[64]####
        ui.progressBars.get(id).setValue(100);//####[65]####
        time = System.currentTimeMillis() - startTime;//####[66]####
        File vid = new File("Edited Video/temp." + ext);//####[67]####
        vid.delete();//####[68]####
        System.out.println("Video filtering for video " + id + " took " + (time / 1000) + " seconds.");//####[69]####
        JOptionPane.showMessageDialog(ui, "Finished Saving Video: " + id + "\nTime taken: " + (time / 1000) + " seconds.");//####[70]####
        System.out.println(id);//####[71]####
        System.out.println("done!!!!!");//####[72]####
        ui.progressBars.remove(id);//####[73]####
        ui.processingInfo.remove(id);//####[74]####
        ui.updateProcessing();//####[76]####
    }//####[77]####
//####[79]####
    public void addFilter(VideoFilter processor, String filter) {//####[79]####
        processor.initializeFilter(filter);//####[80]####
        processor.start();//####[81]####
    }//####[82]####
//####[84]####
    private static volatile Method __pt__startSpliting_String_method = null;//####[84]####
    private synchronized static void __pt__startSpliting_String_ensureMethodVarSet() {//####[84]####
        if (__pt__startSpliting_String_method == null) {//####[84]####
            try {//####[84]####
                __pt__startSpliting_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startSpliting", new Class[] {//####[84]####
                    String.class//####[84]####
                });//####[84]####
            } catch (Exception e) {//####[84]####
                e.printStackTrace();//####[84]####
            }//####[84]####
        }//####[84]####
    }//####[84]####
    public TaskID<Void> startSpliting(String file) {//####[85]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[85]####
        return startSpliting(file, new TaskInfo());//####[85]####
    }//####[85]####
    public TaskID<Void> startSpliting(String file, TaskInfo taskinfo) {//####[85]####
        // ensure Method variable is set//####[85]####
        if (__pt__startSpliting_String_method == null) {//####[85]####
            __pt__startSpliting_String_ensureMethodVarSet();//####[85]####
        }//####[85]####
        taskinfo.setParameters(file);//####[85]####
        taskinfo.setMethod(__pt__startSpliting_String_method);//####[85]####
        taskinfo.setInstance(this);//####[85]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[85]####
    }//####[85]####
    public TaskID<Void> startSpliting(TaskID<String> file) {//####[85]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[85]####
        return startSpliting(file, new TaskInfo());//####[85]####
    }//####[85]####
    public TaskID<Void> startSpliting(TaskID<String> file, TaskInfo taskinfo) {//####[85]####
        // ensure Method variable is set//####[85]####
        if (__pt__startSpliting_String_method == null) {//####[85]####
            __pt__startSpliting_String_ensureMethodVarSet();//####[85]####
        }//####[85]####
        taskinfo.setTaskIdArgIndexes(0);//####[85]####
        taskinfo.addDependsOn(file);//####[85]####
        taskinfo.setParameters(file);//####[85]####
        taskinfo.setMethod(__pt__startSpliting_String_method);//####[85]####
        taskinfo.setInstance(this);//####[85]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[85]####
    }//####[85]####
    public TaskID<Void> startSpliting(BlockingQueue<String> file) {//####[85]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[85]####
        return startSpliting(file, new TaskInfo());//####[85]####
    }//####[85]####
    public TaskID<Void> startSpliting(BlockingQueue<String> file, TaskInfo taskinfo) {//####[85]####
        // ensure Method variable is set//####[85]####
        if (__pt__startSpliting_String_method == null) {//####[85]####
            __pt__startSpliting_String_ensureMethodVarSet();//####[85]####
        }//####[85]####
        taskinfo.setQueueArgIndexes(0);//####[85]####
        taskinfo.setIsPipeline(true);//####[85]####
        taskinfo.setParameters(file);//####[85]####
        taskinfo.setMethod(__pt__startSpliting_String_method);//####[85]####
        taskinfo.setInstance(this);//####[85]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[85]####
    }//####[85]####
    public void __pt__startSpliting(String file) {//####[85]####
        try {//####[86]####
            System.out.println("** Going to wait for the tasks...");//####[87]####
            TaskID id1 = splitVideo(file);//####[88]####
            TaskIDGroup g = new TaskIDGroup(1);//####[89]####
            g.add(id1);//####[90]####
            g.waitTillFinished();//####[92]####
            System.out.println("Done");//####[94]####
        } catch (Exception ee) {//####[96]####
        }//####[97]####
    }//####[98]####
//####[98]####
//####[100]####
    private static volatile Method __pt__splitVideo_String_method = null;//####[100]####
    private synchronized static void __pt__splitVideo_String_ensureMethodVarSet() {//####[100]####
        if (__pt__splitVideo_String_method == null) {//####[100]####
            try {//####[100]####
                __pt__splitVideo_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__splitVideo", new Class[] {//####[100]####
                    String.class//####[100]####
                });//####[100]####
            } catch (Exception e) {//####[100]####
                e.printStackTrace();//####[100]####
            }//####[100]####
        }//####[100]####
    }//####[100]####
    public TaskIDGroup<Void> splitVideo(String fileName) throws IOException {//####[101]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[101]####
        return splitVideo(fileName, new TaskInfo());//####[101]####
    }//####[101]####
    public TaskIDGroup<Void> splitVideo(String fileName, TaskInfo taskinfo) throws IOException {//####[101]####
        // ensure Method variable is set//####[101]####
        if (__pt__splitVideo_String_method == null) {//####[101]####
            __pt__splitVideo_String_ensureMethodVarSet();//####[101]####
        }//####[101]####
        taskinfo.setParameters(fileName);//####[101]####
        taskinfo.setMethod(__pt__splitVideo_String_method);//####[101]####
        taskinfo.setInstance(this);//####[101]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[101]####
    }//####[101]####
    public TaskIDGroup<Void> splitVideo(TaskID<String> fileName) throws IOException {//####[101]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[101]####
        return splitVideo(fileName, new TaskInfo());//####[101]####
    }//####[101]####
    public TaskIDGroup<Void> splitVideo(TaskID<String> fileName, TaskInfo taskinfo) throws IOException {//####[101]####
        // ensure Method variable is set//####[101]####
        if (__pt__splitVideo_String_method == null) {//####[101]####
            __pt__splitVideo_String_ensureMethodVarSet();//####[101]####
        }//####[101]####
        taskinfo.setTaskIdArgIndexes(0);//####[101]####
        taskinfo.addDependsOn(fileName);//####[101]####
        taskinfo.setParameters(fileName);//####[101]####
        taskinfo.setMethod(__pt__splitVideo_String_method);//####[101]####
        taskinfo.setInstance(this);//####[101]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[101]####
    }//####[101]####
    public TaskIDGroup<Void> splitVideo(BlockingQueue<String> fileName) throws IOException {//####[101]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[101]####
        return splitVideo(fileName, new TaskInfo());//####[101]####
    }//####[101]####
    public TaskIDGroup<Void> splitVideo(BlockingQueue<String> fileName, TaskInfo taskinfo) throws IOException {//####[101]####
        // ensure Method variable is set//####[101]####
        if (__pt__splitVideo_String_method == null) {//####[101]####
            __pt__splitVideo_String_ensureMethodVarSet();//####[101]####
        }//####[101]####
        taskinfo.setQueueArgIndexes(0);//####[101]####
        taskinfo.setIsPipeline(true);//####[101]####
        taskinfo.setParameters(fileName);//####[101]####
        taskinfo.setMethod(__pt__splitVideo_String_method);//####[101]####
        taskinfo.setInstance(this);//####[101]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[101]####
    }//####[101]####
    public void __pt__splitVideo(String fileName) throws IOException {//####[101]####
        VideoSpliter vs = new VideoSpliter(fileName, id);//####[102]####
        String duration = vs.getVideoDuration(fileName);//####[105]####
        int durationInMs = vs.transferDuration(duration);//####[106]####
        int partitionedInMs = vs.partition(durationInMs, Runtime.getRuntime().availableProcessors());//####[107]####
        String partitionedDur = vs.transferMsToDuration(partitionedInMs);//####[108]####
        Iterator<String> it = vs.generateCommandLines(Runtime.getRuntime().availableProcessors(), partitionedInMs, partitionedDur).iterator();//####[110]####
        vs.doRealSplittingWork(it);//####[111]####
    }//####[113]####
//####[113]####
//####[115]####
    private static volatile Method __pt__getVideoFiles__method = null;//####[115]####
    private synchronized static void __pt__getVideoFiles__ensureMethodVarSet() {//####[115]####
        if (__pt__getVideoFiles__method == null) {//####[115]####
            try {//####[115]####
                __pt__getVideoFiles__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__getVideoFiles", new Class[] {//####[115]####
                    //####[115]####
                });//####[115]####
            } catch (Exception e) {//####[115]####
                e.printStackTrace();//####[115]####
            }//####[115]####
        }//####[115]####
    }//####[115]####
    public TaskID<Void> getVideoFiles() throws IOException {//####[116]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[116]####
        return getVideoFiles(new TaskInfo());//####[116]####
    }//####[116]####
    public TaskID<Void> getVideoFiles(TaskInfo taskinfo) throws IOException {//####[116]####
        // ensure Method variable is set//####[116]####
        if (__pt__getVideoFiles__method == null) {//####[116]####
            __pt__getVideoFiles__ensureMethodVarSet();//####[116]####
        }//####[116]####
        taskinfo.setParameters();//####[116]####
        taskinfo.setMethod(__pt__getVideoFiles__method);//####[116]####
        taskinfo.setInstance(this);//####[116]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[116]####
    }//####[116]####
    public void __pt__getVideoFiles() throws IOException {//####[116]####
        System.out.println("start");//####[118]####
        File[] listOfFiles = new File("SubVideos").listFiles();//####[119]####
        ArrayList<String> videoNames = new ArrayList<String>();//####[120]####
        for (File listOfFile : listOfFiles) //####[121]####
        {//####[121]####
            if (listOfFile.isFile()) //####[122]####
            {//####[123]####
                videoNames.add(listOfFile.getName());//####[124]####
                System.out.println(listOfFile.getName());//####[125]####
            }//####[126]####
        }//####[127]####
        subVideoNames = videoNames.iterator();//####[128]####
    }//####[129]####
//####[129]####
//####[131]####
    private static volatile Method __pt__startFiltering_String_method = null;//####[131]####
    private synchronized static void __pt__startFiltering_String_ensureMethodVarSet() {//####[131]####
        if (__pt__startFiltering_String_method == null) {//####[131]####
            try {//####[131]####
                __pt__startFiltering_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startFiltering", new Class[] {//####[131]####
                    String.class//####[131]####
                });//####[131]####
            } catch (Exception e) {//####[131]####
                e.printStackTrace();//####[131]####
            }//####[131]####
        }//####[131]####
    }//####[131]####
    public TaskID<Void> startFiltering(String filter) {//####[132]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[132]####
        return startFiltering(filter, new TaskInfo());//####[132]####
    }//####[132]####
    public TaskID<Void> startFiltering(String filter, TaskInfo taskinfo) {//####[132]####
        // ensure Method variable is set//####[132]####
        if (__pt__startFiltering_String_method == null) {//####[132]####
            __pt__startFiltering_String_ensureMethodVarSet();//####[132]####
        }//####[132]####
        taskinfo.setParameters(filter);//####[132]####
        taskinfo.setMethod(__pt__startFiltering_String_method);//####[132]####
        taskinfo.setInstance(this);//####[132]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[132]####
    }//####[132]####
    public TaskID<Void> startFiltering(TaskID<String> filter) {//####[132]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[132]####
        return startFiltering(filter, new TaskInfo());//####[132]####
    }//####[132]####
    public TaskID<Void> startFiltering(TaskID<String> filter, TaskInfo taskinfo) {//####[132]####
        // ensure Method variable is set//####[132]####
        if (__pt__startFiltering_String_method == null) {//####[132]####
            __pt__startFiltering_String_ensureMethodVarSet();//####[132]####
        }//####[132]####
        taskinfo.setTaskIdArgIndexes(0);//####[132]####
        taskinfo.addDependsOn(filter);//####[132]####
        taskinfo.setParameters(filter);//####[132]####
        taskinfo.setMethod(__pt__startFiltering_String_method);//####[132]####
        taskinfo.setInstance(this);//####[132]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[132]####
    }//####[132]####
    public TaskID<Void> startFiltering(BlockingQueue<String> filter) {//####[132]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[132]####
        return startFiltering(filter, new TaskInfo());//####[132]####
    }//####[132]####
    public TaskID<Void> startFiltering(BlockingQueue<String> filter, TaskInfo taskinfo) {//####[132]####
        // ensure Method variable is set//####[132]####
        if (__pt__startFiltering_String_method == null) {//####[132]####
            __pt__startFiltering_String_ensureMethodVarSet();//####[132]####
        }//####[132]####
        taskinfo.setQueueArgIndexes(0);//####[132]####
        taskinfo.setIsPipeline(true);//####[132]####
        taskinfo.setParameters(filter);//####[132]####
        taskinfo.setMethod(__pt__startFiltering_String_method);//####[132]####
        taskinfo.setInstance(this);//####[132]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[132]####
    }//####[132]####
    public void __pt__startFiltering(String filter) {//####[132]####
        try {//####[133]####
            TaskID id2 = addFilterToSubVideos(filter);//####[134]####
            TaskIDGroup gg = new TaskIDGroup(1);//####[135]####
            gg.add(id2);//####[136]####
            gg.waitTillFinished();//####[137]####
            System.out.println("** Finished...");//####[138]####
        } catch (Exception ee) {//####[140]####
        }//####[141]####
    }//####[143]####
//####[143]####
//####[145]####
    private static volatile Method __pt__addFilterToSubVideos_String_method = null;//####[145]####
    private synchronized static void __pt__addFilterToSubVideos_String_ensureMethodVarSet() {//####[145]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[145]####
            try {//####[145]####
                __pt__addFilterToSubVideos_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__addFilterToSubVideos", new Class[] {//####[145]####
                    String.class//####[145]####
                });//####[145]####
            } catch (Exception e) {//####[145]####
                e.printStackTrace();//####[145]####
            }//####[145]####
        }//####[145]####
    }//####[145]####
    public TaskIDGroup<Void> addFilterToSubVideos(String filter) {//####[146]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[146]####
        return addFilterToSubVideos(filter, new TaskInfo());//####[146]####
    }//####[146]####
    public TaskIDGroup<Void> addFilterToSubVideos(String filter, TaskInfo taskinfo) {//####[146]####
        // ensure Method variable is set//####[146]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[146]####
            __pt__addFilterToSubVideos_String_ensureMethodVarSet();//####[146]####
        }//####[146]####
        taskinfo.setParameters(filter);//####[146]####
        taskinfo.setMethod(__pt__addFilterToSubVideos_String_method);//####[146]####
        taskinfo.setInstance(this);//####[146]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[146]####
    }//####[146]####
    public TaskIDGroup<Void> addFilterToSubVideos(TaskID<String> filter) {//####[146]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[146]####
        return addFilterToSubVideos(filter, new TaskInfo());//####[146]####
    }//####[146]####
    public TaskIDGroup<Void> addFilterToSubVideos(TaskID<String> filter, TaskInfo taskinfo) {//####[146]####
        // ensure Method variable is set//####[146]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[146]####
            __pt__addFilterToSubVideos_String_ensureMethodVarSet();//####[146]####
        }//####[146]####
        taskinfo.setTaskIdArgIndexes(0);//####[146]####
        taskinfo.addDependsOn(filter);//####[146]####
        taskinfo.setParameters(filter);//####[146]####
        taskinfo.setMethod(__pt__addFilterToSubVideos_String_method);//####[146]####
        taskinfo.setInstance(this);//####[146]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[146]####
    }//####[146]####
    public TaskIDGroup<Void> addFilterToSubVideos(BlockingQueue<String> filter) {//####[146]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[146]####
        return addFilterToSubVideos(filter, new TaskInfo());//####[146]####
    }//####[146]####
    public TaskIDGroup<Void> addFilterToSubVideos(BlockingQueue<String> filter, TaskInfo taskinfo) {//####[146]####
        // ensure Method variable is set//####[146]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[146]####
            __pt__addFilterToSubVideos_String_ensureMethodVarSet();//####[146]####
        }//####[146]####
        taskinfo.setQueueArgIndexes(0);//####[146]####
        taskinfo.setIsPipeline(true);//####[146]####
        taskinfo.setParameters(filter);//####[146]####
        taskinfo.setMethod(__pt__addFilterToSubVideos_String_method);//####[146]####
        taskinfo.setInstance(this);//####[146]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, Runtime.getRuntime().availableProcessors());//####[146]####
    }//####[146]####
    public void __pt__addFilterToSubVideos(String filter) {//####[146]####
        int i = 0;//####[147]####
        while (subVideoNames.hasNext()) //####[148]####
        addFilter(new VideoFilter("SubVideos/" + subVideoNames.next(), ui, id, i), filter);//####[149]####
        i++;//####[150]####
    }//####[151]####
//####[151]####
//####[153]####
    private static volatile Method __pt__combine__method = null;//####[153]####
    private synchronized static void __pt__combine__ensureMethodVarSet() {//####[153]####
        if (__pt__combine__method == null) {//####[153]####
            try {//####[153]####
                __pt__combine__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__combine", new Class[] {//####[153]####
                    //####[153]####
                });//####[153]####
            } catch (Exception e) {//####[153]####
                e.printStackTrace();//####[153]####
            }//####[153]####
        }//####[153]####
    }//####[153]####
    public TaskID<Void> combine() {//####[154]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[154]####
        return combine(new TaskInfo());//####[154]####
    }//####[154]####
    public TaskID<Void> combine(TaskInfo taskinfo) {//####[154]####
        // ensure Method variable is set//####[154]####
        if (__pt__combine__method == null) {//####[154]####
            __pt__combine__ensureMethodVarSet();//####[154]####
        }//####[154]####
        taskinfo.setParameters();//####[154]####
        taskinfo.setMethod(__pt__combine__method);//####[154]####
        taskinfo.setInstance(this);//####[154]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[154]####
    }//####[154]####
    public void __pt__combine() {//####[154]####
        VideoCombiner vc = new VideoCombiner(outputFile, id);//####[155]####
        command = vc.combine();//####[156]####
        cmd[0] = command;//####[157]####
    }//####[159]####
//####[159]####
//####[161]####
    private static volatile Method __pt__startCombine__method = null;//####[161]####
    private synchronized static void __pt__startCombine__ensureMethodVarSet() {//####[161]####
        if (__pt__startCombine__method == null) {//####[161]####
            try {//####[161]####
                __pt__startCombine__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startCombine", new Class[] {//####[161]####
                    //####[161]####
                });//####[161]####
            } catch (Exception e) {//####[161]####
                e.printStackTrace();//####[161]####
            }//####[161]####
        }//####[161]####
    }//####[161]####
    public TaskID<Void> startCombine() {//####[162]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[162]####
        return startCombine(new TaskInfo());//####[162]####
    }//####[162]####
    public TaskID<Void> startCombine(TaskInfo taskinfo) {//####[162]####
        // ensure Method variable is set//####[162]####
        if (__pt__startCombine__method == null) {//####[162]####
            __pt__startCombine__ensureMethodVarSet();//####[162]####
        }//####[162]####
        taskinfo.setParameters();//####[162]####
        taskinfo.setMethod(__pt__startCombine__method);//####[162]####
        taskinfo.setInstance(this);//####[162]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[162]####
    }//####[162]####
    public void __pt__startCombine() {//####[162]####
        try {//####[163]####
            System.out.println("Start Combining.......");//####[164]####
            TaskID id10 = combine();//####[165]####
            TaskIDGroup gggggggg = new TaskIDGroup(1);//####[166]####
            gggggggg.add(id10);//####[167]####
            gggggggg.waitTillFinished();//####[169]####
            System.out.println("Combine Finished.......");//####[171]####
        } catch (Exception ee) {//####[173]####
        }//####[174]####
    }//####[177]####
//####[177]####
//####[179]####
    private static volatile Method __pt__recordSubVideoNames__method = null;//####[179]####
    private synchronized static void __pt__recordSubVideoNames__ensureMethodVarSet() {//####[179]####
        if (__pt__recordSubVideoNames__method == null) {//####[179]####
            try {//####[179]####
                __pt__recordSubVideoNames__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__recordSubVideoNames", new Class[] {//####[179]####
                    //####[179]####
                });//####[179]####
            } catch (Exception e) {//####[179]####
                e.printStackTrace();//####[179]####
            }//####[179]####
        }//####[179]####
    }//####[179]####
    public TaskID<Void> recordSubVideoNames() {//####[180]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[180]####
        return recordSubVideoNames(new TaskInfo());//####[180]####
    }//####[180]####
    public TaskID<Void> recordSubVideoNames(TaskInfo taskinfo) {//####[180]####
        // ensure Method variable is set//####[180]####
        if (__pt__recordSubVideoNames__method == null) {//####[180]####
            __pt__recordSubVideoNames__ensureMethodVarSet();//####[180]####
        }//####[180]####
        taskinfo.setParameters();//####[180]####
        taskinfo.setMethod(__pt__recordSubVideoNames__method);//####[180]####
        taskinfo.setInstance(this);//####[180]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[180]####
    }//####[180]####
    public void __pt__recordSubVideoNames() {//####[180]####
        try {//####[181]####
            TaskID id4 = getVideoFiles();//####[182]####
            TaskIDGroup gg = new TaskIDGroup(1);//####[183]####
            gg.add(id4);//####[184]####
            gg.waitTillFinished();//####[185]####
            System.out.println("** Finished saving files ...");//####[186]####
        } catch (Exception ee) {//####[188]####
        }//####[189]####
    }//####[190]####
//####[190]####
//####[193]####
    @Override//####[193]####
    protected Void doInBackground() throws Exception {//####[193]####
        startTime = System.currentTimeMillis();//####[194]####
        TaskID split = startSpliting(file.getName());//####[195]####
        split.waitTillFinished();//####[196]####
        TaskInfo __pt__recordNames = new TaskInfo();//####[198]####
//####[198]####
        /*  -- ParaTask dependsOn clause for 'recordNames' -- *///####[198]####
        __pt__recordNames.addDependsOn(split);//####[198]####
//####[198]####
        TaskID recordNames = recordSubVideoNames(__pt__recordNames);//####[198]####
        recordNames.waitTillFinished();//####[199]####
        TaskInfo __pt__filtered = new TaskInfo();//####[200]####
//####[200]####
        /*  -- ParaTask dependsOn clause for 'filtered' -- *///####[200]####
        __pt__filtered.addDependsOn(recordNames);//####[200]####
//####[200]####
        TaskID filtered = startFiltering(filter, __pt__filtered);//####[200]####
        filtered.waitTillFinished();//####[201]####
        TaskInfo __pt__combined = new TaskInfo();//####[202]####
//####[202]####
        /*  -- ParaTask dependsOn clause for 'combined' -- *///####[202]####
        __pt__combined.addDependsOn(filtered);//####[202]####
//####[202]####
        TaskID combined = startCombine(__pt__combined);//####[202]####
        combined.waitTillFinished();//####[203]####
        String audioMap = "ffmpeg -i \"Edited Video\"/temp." + ext + " -i " + file.getName() + " -c copy -map 0:0 -map 1:1 -shortest \"Edited Video\"/" + outputFile;//####[205]####
        System.out.println(audioMap);//####[206]####
        cmd[1] = audioMap;//####[207]####
        long endTime = System.currentTimeMillis();//####[209]####
        long totalTime = endTime - startTime;//####[210]####
        System.out.println("Duration: " + totalTime + " ms");//####[211]####
        return null;//####[213]####
    }//####[214]####
}//####[214]####
