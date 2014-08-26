package info.trilok.finagletest.Jobs;

import com.google.common.base.Joiner;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by trilok on 8/25/2014.
 */
public class PersistentProcessJob extends Job{
    private OutputStream outputStream;
    private Process process;
    List<String> command;

    File stdOut,stdErr;

    public PersistentProcessJob(Integer id, File stdOut, File stdErr, List<String> command){
        super(id);
        this.outputStream=outputStream;
        this.command=command;
        this.stdOut=stdOut;
        this.stdErr=stdErr;
    }

    private void runProcess(){
        ProcessBuilder builder=new ProcessBuilder(command);
        builder.redirectError(stdErr);
        builder.redirectOutput(stdOut);
        try{
            this.process=builder.start();
        }catch (IOException ex){
            System.err.println("IO Exception when trying to run process"+ Joiner.on(" ").join(command));
            this.setStatus(JOB_STATUS.TERMINATED);
        }
    }

}
