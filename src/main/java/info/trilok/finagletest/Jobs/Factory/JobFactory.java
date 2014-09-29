package info.trilok.finagletest.Jobs.Factory;

import info.trilok.finagletest.Jobs.Command.JobCommand;
import info.trilok.finagletest.Jobs.Executable;
import info.trilok.finagletest.Jobs.Job;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Created by trilok on 8/25/2014.
 */
public class JobFactory {
    Job Create(JobCommand command) throws IOException{
        Job job = null;
        switch(command.getCommand()){
            case EXECUTE:
                Long jobId = new Date().getTime();
                createJobWorkingDirectory(jobId.toString());
                File stdout = createFile("stdout",jobId.toString());
                File stderr = createFile("stderr",jobId.toString());
                job = new Executable(jobId,stdout,stderr,command);
            case CASCADING:
                break;
            default:
                System.err.println("Unknown job creation type: "+command.toString());
        }
        return job;
    }

    private String getJobWorkingDirectory(String jobName) throws IOException{
        String baseWorkingDir=getWorkingDirectory();
        return baseWorkingDir+"/"+jobName;
    }

    private boolean createJobWorkingDirectory(String jobName) throws IOException{
        File jobWorkingDirectory = new File(getJobWorkingDirectory(jobName));
        return jobWorkingDirectory.mkdir();
    }

    private File createFile(String fileName, String jobName) throws IOException{
        String jobDir=getJobWorkingDirectory(jobName);
        File file = new File(jobDir+"/"+fileName);
        file.createNewFile();
        return file;
    }

    /**
     * Gets the working directory for this job manager. All job related directories and files will be put in this location
     * @return
     * @throws IOException
     */
    private String getWorkingDirectory() throws IOException{
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
