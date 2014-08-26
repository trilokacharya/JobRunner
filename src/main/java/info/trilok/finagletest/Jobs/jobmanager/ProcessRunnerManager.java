package info.trilok.finagletest.Jobs.jobmanager;

import info.trilok.finagletest.Jobs.Handler.JobCommand;
import info.trilok.finagletest.Jobs.Job;
import info.trilok.finagletest.Requests.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by trilok on 8/25/2014.
 */
public class ProcessRunnerManager implements JobManager {

    private static volatile JobManager instance;

    private Map<String,JobCommand> registeredHandler= new HashMap<String,JobCommand>();

    private Map<Integer, Job> currentJobs = new HashMap<Integer, Job>();

    private ProcessRunnerManager(){}

    public static synchronized JobManager getInstance() {
        if(instance==null){
            instance=new ProcessRunnerManager();
        }
        return instance;
    }

    /**
     * Register a new Job Command Handler.
     * @param name
     * @param handler
     */
    @Override
    public void RegisterHandler(String name, JobCommand handler){
        this.registeredHandler.put(name,handler);
    }


    @Override
    public String RunCommand(Request request){
        return "";
    }

}
