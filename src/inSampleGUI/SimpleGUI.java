package inSampleGUI;//####[1]####
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
import controllers.*;//####[15]####
import pt.runtime.TaskIDGroup;//####[16]####
import java.nio.file.Files;//####[18]####
import java.nio.file.Paths;//####[19]####
import java.util.*;//####[21]####
//####[21]####
//-- ParaTask related imports//####[21]####
import pt.runtime.*;//####[21]####
import java.util.concurrent.ExecutionException;//####[21]####
import java.util.concurrent.locks.*;//####[21]####
import java.lang.reflect.*;//####[21]####
import pt.runtime.GuiThread;//####[21]####
import java.util.concurrent.BlockingQueue;//####[21]####
import java.util.ArrayList;//####[21]####
import java.util.List;//####[21]####
//####[21]####
public class SimpleGUI extends JFrame implements ActionListener {//####[23]####
    static{ParaTask.init();}//####[23]####
    /*  ParaTask helper method to access private/protected slots *///####[23]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[23]####
        if (m.getParameterTypes().length == 0)//####[23]####
            m.invoke(instance);//####[23]####
        else if ((m.getParameterTypes().length == 1))//####[23]####
            m.invoke(instance, arg);//####[23]####
        else //####[23]####
            m.invoke(instance, arg, interResult);//####[23]####
    }//####[23]####
//####[25]####
    private JButton btnGo = new JButton("Go!");//####[25]####
//####[26]####
    private JButton btnCheckResponsiveness = new JButton("Responsive?");//####[26]####
//####[27]####
    private ArrayList<String> a = new ArrayList<String>();//####[27]####
//####[28]####
    private Iterator<String> subVideoNames;//####[28]####
//####[29]####
    public long startTime;//####[29]####
//####[31]####
    public SimpleGUI() {//####[31]####
        super("Simple GUI example");//####[32]####
        setSize(400, 300);//####[33]####
        btnGo.addActionListener(this);//####[34]####
        btnCheckResponsiveness.addActionListener(this);//####[35]####
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);//####[36]####
        add(btnGo, BorderLayout.SOUTH);//####[37]####
        add(btnCheckResponsiveness, BorderLayout.CENTER);//####[38]####
    }//####[39]####
//####[41]####
    public void done() {//####[41]####
        JOptionPane.showMessageDialog(SimpleGUI.this, "Done!");//####[42]####
    }//####[43]####
//####[45]####
    public void addFilter(FilterProcessor processor, String filter) {//####[45]####
        processor.initializeFilter(filter);//####[47]####
        processor.start();//####[48]####
    }//####[50]####
//####[52]####
    public void actionPerformed(ActionEvent e) {//####[52]####
        String filter = "colorchannelmixer=.3:.4:.3:0:.3:.4:.3:0:.3:.4:.3";//####[54]####
        if (e.getSource() == btnCheckResponsiveness) //####[55]####
        {//####[56]####
            btnCheckResponsiveness.setBackground(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));//####[57]####
        } else {//####[59]####
            startTime = System.currentTimeMillis();//####[60]####
            TaskID splited = startSpliting("mv.mp4");//####[61]####
            TaskInfo __pt__recordNames = new TaskInfo();//####[62]####
//####[62]####
            /*  -- ParaTask dependsOn clause for 'recordNames' -- *///####[62]####
            __pt__recordNames.addDependsOn(splited);//####[62]####
//####[62]####
            TaskID recordNames = recordSubVideoNames(__pt__recordNames);//####[62]####
            TaskInfo __pt__filtered = new TaskInfo();//####[63]####
//####[63]####
            /*  -- ParaTask dependsOn clause for 'filtered' -- *///####[63]####
            __pt__filtered.addDependsOn(recordNames);//####[63]####
//####[63]####
            TaskID filtered = startFiltering(filter, __pt__filtered);//####[63]####
            TaskInfo __pt__combined = new TaskInfo();//####[64]####
//####[64]####
            /*  -- ParaTask dependsOn clause for 'combined' -- *///####[64]####
            __pt__combined.addDependsOn(filtered);//####[64]####
//####[64]####
            boolean isEDT = GuiThread.isEventDispatchThread();//####[64]####
//####[64]####
//####[64]####
            /*  -- ParaTask notify clause for 'combined' -- *///####[64]####
            try {//####[64]####
                Method __pt__combined_slot_0 = null;//####[64]####
                __pt__combined_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "done", new Class[] {});//####[64]####
                if (false) done(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[64]####
                __pt__combined.addSlotToNotify(new Slot(__pt__combined_slot_0, this, false));//####[64]####
//####[64]####
            } catch(Exception __pt__e) { //####[64]####
                System.err.println("Problem registering method in clause:");//####[64]####
                __pt__e.printStackTrace();//####[64]####
            }//####[64]####
            TaskID combined = combine(__pt__combined);//####[64]####
        }//####[65]####
    }//####[66]####
//####[68]####
    public static void main(String[] agrs) {//####[68]####
        SwingUtilities.invokeLater(new Runnable() {//####[68]####
//####[70]####
            public void run() {//####[70]####
                SimpleGUI frame = new SimpleGUI();//####[71]####
                frame.setVisible(true);//####[72]####
            }//####[73]####
        });//####[73]####
    }//####[75]####
//####[77]####
    private static volatile Method __pt__addFilterToSubVideos_String_method = null;//####[77]####
    private synchronized static void __pt__addFilterToSubVideos_String_ensureMethodVarSet() {//####[77]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[77]####
            try {//####[77]####
                __pt__addFilterToSubVideos_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__addFilterToSubVideos", new Class[] {//####[77]####
                    String.class//####[77]####
                });//####[77]####
            } catch (Exception e) {//####[77]####
                e.printStackTrace();//####[77]####
            }//####[77]####
        }//####[77]####
    }//####[77]####
    public TaskIDGroup<Void> addFilterToSubVideos(String filter) {//####[78]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[78]####
        return addFilterToSubVideos(filter, new TaskInfo());//####[78]####
    }//####[78]####
    public TaskIDGroup<Void> addFilterToSubVideos(String filter, TaskInfo taskinfo) {//####[78]####
        // ensure Method variable is set//####[78]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[78]####
            __pt__addFilterToSubVideos_String_ensureMethodVarSet();//####[78]####
        }//####[78]####
        taskinfo.setParameters(filter);//####[78]####
        taskinfo.setMethod(__pt__addFilterToSubVideos_String_method);//####[78]####
        taskinfo.setInstance(this);//####[78]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[78]####
    }//####[78]####
    public TaskIDGroup<Void> addFilterToSubVideos(TaskID<String> filter) {//####[78]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[78]####
        return addFilterToSubVideos(filter, new TaskInfo());//####[78]####
    }//####[78]####
    public TaskIDGroup<Void> addFilterToSubVideos(TaskID<String> filter, TaskInfo taskinfo) {//####[78]####
        // ensure Method variable is set//####[78]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[78]####
            __pt__addFilterToSubVideos_String_ensureMethodVarSet();//####[78]####
        }//####[78]####
        taskinfo.setTaskIdArgIndexes(0);//####[78]####
        taskinfo.addDependsOn(filter);//####[78]####
        taskinfo.setParameters(filter);//####[78]####
        taskinfo.setMethod(__pt__addFilterToSubVideos_String_method);//####[78]####
        taskinfo.setInstance(this);//####[78]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[78]####
    }//####[78]####
    public TaskIDGroup<Void> addFilterToSubVideos(BlockingQueue<String> filter) {//####[78]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[78]####
        return addFilterToSubVideos(filter, new TaskInfo());//####[78]####
    }//####[78]####
    public TaskIDGroup<Void> addFilterToSubVideos(BlockingQueue<String> filter, TaskInfo taskinfo) {//####[78]####
        // ensure Method variable is set//####[78]####
        if (__pt__addFilterToSubVideos_String_method == null) {//####[78]####
            __pt__addFilterToSubVideos_String_ensureMethodVarSet();//####[78]####
        }//####[78]####
        taskinfo.setQueueArgIndexes(0);//####[78]####
        taskinfo.setIsPipeline(true);//####[78]####
        taskinfo.setParameters(filter);//####[78]####
        taskinfo.setMethod(__pt__addFilterToSubVideos_String_method);//####[78]####
        taskinfo.setInstance(this);//####[78]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[78]####
    }//####[78]####
    public void __pt__addFilterToSubVideos(String filter) {//####[78]####
        while (subVideoNames.hasNext()) //####[80]####
        addFilter(new FilterProcessor("SubVideos/" + subVideoNames.next()), filter);//####[81]####
    }//####[82]####
//####[82]####
//####[85]####
    private static volatile Method __pt__splitVideo_String_method = null;//####[85]####
    private synchronized static void __pt__splitVideo_String_ensureMethodVarSet() {//####[85]####
        if (__pt__splitVideo_String_method == null) {//####[85]####
            try {//####[85]####
                __pt__splitVideo_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__splitVideo", new Class[] {//####[85]####
                    String.class//####[85]####
                });//####[85]####
            } catch (Exception e) {//####[85]####
                e.printStackTrace();//####[85]####
            }//####[85]####
        }//####[85]####
    }//####[85]####
    public TaskIDGroup<Void> splitVideo(String fileName) throws IOException {//####[86]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[86]####
        return splitVideo(fileName, new TaskInfo());//####[86]####
    }//####[86]####
    public TaskIDGroup<Void> splitVideo(String fileName, TaskInfo taskinfo) throws IOException {//####[86]####
        // ensure Method variable is set//####[86]####
        if (__pt__splitVideo_String_method == null) {//####[86]####
            __pt__splitVideo_String_ensureMethodVarSet();//####[86]####
        }//####[86]####
        taskinfo.setParameters(fileName);//####[86]####
        taskinfo.setMethod(__pt__splitVideo_String_method);//####[86]####
        taskinfo.setInstance(this);//####[86]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[86]####
    }//####[86]####
    public TaskIDGroup<Void> splitVideo(TaskID<String> fileName) throws IOException {//####[86]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[86]####
        return splitVideo(fileName, new TaskInfo());//####[86]####
    }//####[86]####
    public TaskIDGroup<Void> splitVideo(TaskID<String> fileName, TaskInfo taskinfo) throws IOException {//####[86]####
        // ensure Method variable is set//####[86]####
        if (__pt__splitVideo_String_method == null) {//####[86]####
            __pt__splitVideo_String_ensureMethodVarSet();//####[86]####
        }//####[86]####
        taskinfo.setTaskIdArgIndexes(0);//####[86]####
        taskinfo.addDependsOn(fileName);//####[86]####
        taskinfo.setParameters(fileName);//####[86]####
        taskinfo.setMethod(__pt__splitVideo_String_method);//####[86]####
        taskinfo.setInstance(this);//####[86]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[86]####
    }//####[86]####
    public TaskIDGroup<Void> splitVideo(BlockingQueue<String> fileName) throws IOException {//####[86]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[86]####
        return splitVideo(fileName, new TaskInfo());//####[86]####
    }//####[86]####
    public TaskIDGroup<Void> splitVideo(BlockingQueue<String> fileName, TaskInfo taskinfo) throws IOException {//####[86]####
        // ensure Method variable is set//####[86]####
        if (__pt__splitVideo_String_method == null) {//####[86]####
            __pt__splitVideo_String_ensureMethodVarSet();//####[86]####
        }//####[86]####
        taskinfo.setQueueArgIndexes(0);//####[86]####
        taskinfo.setIsPipeline(true);//####[86]####
        taskinfo.setParameters(fileName);//####[86]####
        taskinfo.setMethod(__pt__splitVideo_String_method);//####[86]####
        taskinfo.setInstance(this);//####[86]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, 4);//####[86]####
    }//####[86]####
    public void __pt__splitVideo(String fileName) throws IOException {//####[86]####
        VideoSpliter vs = new VideoSpliter(fileName);//####[87]####
        String duration = vs.getVideoDuration(fileName);//####[90]####
        int durationInMs = vs.transferDuration(duration);//####[91]####
        int partitionedInMs = vs.partition(durationInMs, 4);//####[92]####
        String partitionedDur = vs.transferMsToDuration(partitionedInMs);//####[93]####
        Iterator<String> it = vs.generateCommandLines(4, partitionedInMs, partitionedDur).iterator();//####[95]####
        vs.doRealSplittingWork(it);//####[96]####
    }//####[98]####
//####[98]####
//####[100]####
    private static volatile Method __pt__getVideoFiles__method = null;//####[100]####
    private synchronized static void __pt__getVideoFiles__ensureMethodVarSet() {//####[100]####
        if (__pt__getVideoFiles__method == null) {//####[100]####
            try {//####[100]####
                __pt__getVideoFiles__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__getVideoFiles", new Class[] {//####[100]####
                    //####[100]####
                });//####[100]####
            } catch (Exception e) {//####[100]####
                e.printStackTrace();//####[100]####
            }//####[100]####
        }//####[100]####
    }//####[100]####
    public TaskID<Void> getVideoFiles() throws IOException {//####[101]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[101]####
        return getVideoFiles(new TaskInfo());//####[101]####
    }//####[101]####
    public TaskID<Void> getVideoFiles(TaskInfo taskinfo) throws IOException {//####[101]####
        // ensure Method variable is set//####[101]####
        if (__pt__getVideoFiles__method == null) {//####[101]####
            __pt__getVideoFiles__ensureMethodVarSet();//####[101]####
        }//####[101]####
        taskinfo.setParameters();//####[101]####
        taskinfo.setMethod(__pt__getVideoFiles__method);//####[101]####
        taskinfo.setInstance(this);//####[101]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[101]####
    }//####[101]####
    public void __pt__getVideoFiles() throws IOException {//####[101]####
        System.out.println("start");//####[103]####
        File[] listOfFiles = new File("SubVideos").listFiles();//####[104]####
        ArrayList<String> videoNames = new ArrayList<String>();//####[105]####
        for (File listOfFile : listOfFiles) //####[106]####
        {//####[106]####
            if (listOfFile.isFile()) //####[107]####
            {//####[108]####
                videoNames.add(listOfFile.getName());//####[109]####
                System.out.println(listOfFile.getName());//####[110]####
            }//####[111]####
        }//####[112]####
        subVideoNames = videoNames.iterator();//####[113]####
    }//####[114]####
//####[114]####
//####[116]####
    private static volatile Method __pt__combine__method = null;//####[116]####
    private synchronized static void __pt__combine__ensureMethodVarSet() {//####[116]####
        if (__pt__combine__method == null) {//####[116]####
            try {//####[116]####
                __pt__combine__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__combine", new Class[] {//####[116]####
                    //####[116]####
                });//####[116]####
            } catch (Exception e) {//####[116]####
                e.printStackTrace();//####[116]####
            }//####[116]####
        }//####[116]####
    }//####[116]####
    public TaskID<Void> combine() {//####[117]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[117]####
        return combine(new TaskInfo());//####[117]####
    }//####[117]####
    public TaskID<Void> combine(TaskInfo taskinfo) {//####[117]####
        // ensure Method variable is set//####[117]####
        if (__pt__combine__method == null) {//####[117]####
            __pt__combine__ensureMethodVarSet();//####[117]####
        }//####[117]####
        taskinfo.setParameters();//####[117]####
        taskinfo.setMethod(__pt__combine__method);//####[117]####
        taskinfo.setInstance(this);//####[117]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[117]####
    }//####[117]####
    public void __pt__combine() {//####[117]####
        new VideoCombiner().combine();//####[118]####
        long endTime = System.currentTimeMillis();//####[119]####
        long totalTime = endTime - startTime;//####[120]####
        System.out.println("Duration: " + totalTime + " ms");//####[121]####
    }//####[122]####
//####[122]####
//####[125]####
    private static volatile Method __pt__startSpliting_String_method = null;//####[125]####
    private synchronized static void __pt__startSpliting_String_ensureMethodVarSet() {//####[125]####
        if (__pt__startSpliting_String_method == null) {//####[125]####
            try {//####[125]####
                __pt__startSpliting_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startSpliting", new Class[] {//####[125]####
                    String.class//####[125]####
                });//####[125]####
            } catch (Exception e) {//####[125]####
                e.printStackTrace();//####[125]####
            }//####[125]####
        }//####[125]####
    }//####[125]####
    public TaskID<Void> startSpliting(String file) {//####[126]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[126]####
        return startSpliting(file, new TaskInfo());//####[126]####
    }//####[126]####
    public TaskID<Void> startSpliting(String file, TaskInfo taskinfo) {//####[126]####
        // ensure Method variable is set//####[126]####
        if (__pt__startSpliting_String_method == null) {//####[126]####
            __pt__startSpliting_String_ensureMethodVarSet();//####[126]####
        }//####[126]####
        taskinfo.setParameters(file);//####[126]####
        taskinfo.setMethod(__pt__startSpliting_String_method);//####[126]####
        taskinfo.setInstance(this);//####[126]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[126]####
    }//####[126]####
    public TaskID<Void> startSpliting(TaskID<String> file) {//####[126]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[126]####
        return startSpliting(file, new TaskInfo());//####[126]####
    }//####[126]####
    public TaskID<Void> startSpliting(TaskID<String> file, TaskInfo taskinfo) {//####[126]####
        // ensure Method variable is set//####[126]####
        if (__pt__startSpliting_String_method == null) {//####[126]####
            __pt__startSpliting_String_ensureMethodVarSet();//####[126]####
        }//####[126]####
        taskinfo.setTaskIdArgIndexes(0);//####[126]####
        taskinfo.addDependsOn(file);//####[126]####
        taskinfo.setParameters(file);//####[126]####
        taskinfo.setMethod(__pt__startSpliting_String_method);//####[126]####
        taskinfo.setInstance(this);//####[126]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[126]####
    }//####[126]####
    public TaskID<Void> startSpliting(BlockingQueue<String> file) {//####[126]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[126]####
        return startSpliting(file, new TaskInfo());//####[126]####
    }//####[126]####
    public TaskID<Void> startSpliting(BlockingQueue<String> file, TaskInfo taskinfo) {//####[126]####
        // ensure Method variable is set//####[126]####
        if (__pt__startSpliting_String_method == null) {//####[126]####
            __pt__startSpliting_String_ensureMethodVarSet();//####[126]####
        }//####[126]####
        taskinfo.setQueueArgIndexes(0);//####[126]####
        taskinfo.setIsPipeline(true);//####[126]####
        taskinfo.setParameters(file);//####[126]####
        taskinfo.setMethod(__pt__startSpliting_String_method);//####[126]####
        taskinfo.setInstance(this);//####[126]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[126]####
    }//####[126]####
    public void __pt__startSpliting(String file) {//####[126]####
        try {//####[127]####
            TaskID id1 = splitVideo(file);//####[128]####
            TaskIDGroup g = new TaskIDGroup(1);//####[129]####
            g.add(id1);//####[130]####
            System.out.println("** Going to wait for the tasks...");//####[131]####
            g.waitTillFinished();//####[132]####
            System.out.println("Done");//####[134]####
        } catch (Exception ee) {//####[136]####
        }//####[137]####
    }//####[139]####
//####[139]####
//####[141]####
    private static volatile Method __pt__startFiltering_String_method = null;//####[141]####
    private synchronized static void __pt__startFiltering_String_ensureMethodVarSet() {//####[141]####
        if (__pt__startFiltering_String_method == null) {//####[141]####
            try {//####[141]####
                __pt__startFiltering_String_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__startFiltering", new Class[] {//####[141]####
                    String.class//####[141]####
                });//####[141]####
            } catch (Exception e) {//####[141]####
                e.printStackTrace();//####[141]####
            }//####[141]####
        }//####[141]####
    }//####[141]####
    public TaskID<Void> startFiltering(String filter) {//####[142]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[142]####
        return startFiltering(filter, new TaskInfo());//####[142]####
    }//####[142]####
    public TaskID<Void> startFiltering(String filter, TaskInfo taskinfo) {//####[142]####
        // ensure Method variable is set//####[142]####
        if (__pt__startFiltering_String_method == null) {//####[142]####
            __pt__startFiltering_String_ensureMethodVarSet();//####[142]####
        }//####[142]####
        taskinfo.setParameters(filter);//####[142]####
        taskinfo.setMethod(__pt__startFiltering_String_method);//####[142]####
        taskinfo.setInstance(this);//####[142]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[142]####
    }//####[142]####
    public TaskID<Void> startFiltering(TaskID<String> filter) {//####[142]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[142]####
        return startFiltering(filter, new TaskInfo());//####[142]####
    }//####[142]####
    public TaskID<Void> startFiltering(TaskID<String> filter, TaskInfo taskinfo) {//####[142]####
        // ensure Method variable is set//####[142]####
        if (__pt__startFiltering_String_method == null) {//####[142]####
            __pt__startFiltering_String_ensureMethodVarSet();//####[142]####
        }//####[142]####
        taskinfo.setTaskIdArgIndexes(0);//####[142]####
        taskinfo.addDependsOn(filter);//####[142]####
        taskinfo.setParameters(filter);//####[142]####
        taskinfo.setMethod(__pt__startFiltering_String_method);//####[142]####
        taskinfo.setInstance(this);//####[142]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[142]####
    }//####[142]####
    public TaskID<Void> startFiltering(BlockingQueue<String> filter) {//####[142]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[142]####
        return startFiltering(filter, new TaskInfo());//####[142]####
    }//####[142]####
    public TaskID<Void> startFiltering(BlockingQueue<String> filter, TaskInfo taskinfo) {//####[142]####
        // ensure Method variable is set//####[142]####
        if (__pt__startFiltering_String_method == null) {//####[142]####
            __pt__startFiltering_String_ensureMethodVarSet();//####[142]####
        }//####[142]####
        taskinfo.setQueueArgIndexes(0);//####[142]####
        taskinfo.setIsPipeline(true);//####[142]####
        taskinfo.setParameters(filter);//####[142]####
        taskinfo.setMethod(__pt__startFiltering_String_method);//####[142]####
        taskinfo.setInstance(this);//####[142]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[142]####
    }//####[142]####
    public void __pt__startFiltering(String filter) {//####[142]####
        try {//####[143]####
            TaskID id2 = addFilterToSubVideos(filter);//####[144]####
            TaskIDGroup gg = new TaskIDGroup(1);//####[145]####
            gg.add(id2);//####[146]####
            gg.waitTillFinished();//####[147]####
            System.out.println("** Finished...");//####[148]####
        } catch (Exception ee) {//####[150]####
        }//####[151]####
    }//####[153]####
//####[153]####
//####[155]####
    private static volatile Method __pt__recordSubVideoNames__method = null;//####[155]####
    private synchronized static void __pt__recordSubVideoNames__ensureMethodVarSet() {//####[155]####
        if (__pt__recordSubVideoNames__method == null) {//####[155]####
            try {//####[155]####
                __pt__recordSubVideoNames__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__recordSubVideoNames", new Class[] {//####[155]####
                    //####[155]####
                });//####[155]####
            } catch (Exception e) {//####[155]####
                e.printStackTrace();//####[155]####
            }//####[155]####
        }//####[155]####
    }//####[155]####
    public TaskID<Void> recordSubVideoNames() {//####[156]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[156]####
        return recordSubVideoNames(new TaskInfo());//####[156]####
    }//####[156]####
    public TaskID<Void> recordSubVideoNames(TaskInfo taskinfo) {//####[156]####
        // ensure Method variable is set//####[156]####
        if (__pt__recordSubVideoNames__method == null) {//####[156]####
            __pt__recordSubVideoNames__ensureMethodVarSet();//####[156]####
        }//####[156]####
        taskinfo.setParameters();//####[156]####
        taskinfo.setMethod(__pt__recordSubVideoNames__method);//####[156]####
        taskinfo.setInstance(this);//####[156]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[156]####
    }//####[156]####
    public void __pt__recordSubVideoNames() {//####[156]####
        try {//####[157]####
            TaskID id4 = getVideoFiles();//####[158]####
            TaskIDGroup gg = new TaskIDGroup(1);//####[159]####
            gg.add(id4);//####[160]####
            gg.waitTillFinished();//####[161]####
            System.out.println("** Finished saving files ...");//####[162]####
        } catch (Exception ee) {//####[164]####
        }//####[165]####
    }//####[166]####
//####[166]####
}//####[166]####
