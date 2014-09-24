package info.trilok.finagletest.Jobs.jobmanager;

import info.trilok.finagletest.Jobs.Handler.JobCommand;
import info.trilok.finagletest.Jobs.Job;
import info.trilok.finagletest.Requests.Request;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by trilok on 8/25/2014.
 */
public class ProcessRunnerManager implements JobManager {

    private static volatile JobManager instance;

    private Map<String,JobCommand> registeredHandler= new HashMap<String,JobCommand>();

    private Map<Integer, Job> currentJobs = new HashMap<Integer, Job>();

    private final String workingDirectory ;

    private ProcessRunnerManager() throws IOException{
       workingDirectory=GetWorkingDirector();
    }

    public static synchronized JobManager getInstance() throws IOException{
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

    /**
     * Gets the working directory for this job manager. All job related directories and files will be put in this location
     * @return
     * @throws IOException
     */
    private String GetWorkingDirector() throws IOException{
        Properties prop = new Properties();
        String propFileName = "config.properties";

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if(inputStream==null){
            throw new FileNotFoundException("Couldn't find properties file:"+propFileName);
        }

        try {
            prop.load(inputStream);
        }catch (IOException ex){
            System.err.println("Cannot open properties file:"+propFileName);
            throw ex;
        }
        finally{
            inputStream.close();
        }

        String workingDir= prop.getProperty("working_dir");
        return workingDir;
    }

}
