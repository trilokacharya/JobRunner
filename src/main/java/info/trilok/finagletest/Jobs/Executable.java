package info.trilok.finagletest.Jobs;

import com.google.common.base.Splitter;
import info.trilok.finagletest.Jobs.Command.JobCommand;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by trilok on 8/25/2014.
 */
public class Executable extends Job{
    private Process process;
    private final JobCommand command;

    private volatile boolean shouldBeKilled=false;

    // Max amount of output and Error data that we would return
    private final int DATA_RETURN_SIZE_THRESHOLD = 500000;// 500 kb

    // When there is too much data, we show the top and the bottom threshold/2 characters. This is the separator.
    private final String SEPARATOR = ".......................";

    private final File stdOut,stdErr;

    public Executable(Long id, File stdOut, File stdErr, JobCommand command){
        super(id);
        this.command=command;
        this.stdOut=stdOut;
        this.stdErr=stdErr;
    }

    /**
     * Terrible (and only) way to figure out if a process is alive as of Java 7.
     * Java 8 has good support for checking this as a process.isAlive() call
     * @return
     */
    private boolean processIsAlive(){
        try{
            process.exitValue(); // if this throws an exception, the process is still running
            return false;
        }catch(IllegalThreadStateException ex){
            return true;
        }

    }

    /**
     * Try to cancel a running process and returns false if it is unable to do so.
     * @return
     */
    public boolean tryCancel(){
        if(processIsAlive()){
            this.process.destroy();
        }
        try {
            Thread.sleep(1000);
            if(processIsAlive()){
                return false;
            }
        }catch(InterruptedException ex){}
        return true;
    }

    /**
     * Converts the map of key:argument values to a command string, separated by a space
     * to feed into the Process builder
     * @return
     */
    private List<String> getCommandString(){
        List<String> cmd = new ArrayList<String>();
        HashMap<String, String> args = command.getArgs();
        String execCommand = args.get("executable");
        if (execCommand == null) {
            throw new IllegalArgumentException("Missing 'cmd' field in the command");
        }
        cmd.add(execCommand);
        String arguments = args.get("args");
        if (arguments == null) {
            throw new IllegalArgumentException("Missing 'args' filed in the command");
        }
        cmd.addAll(Splitter.on(" ").splitToList(arguments));
        return cmd;
    }

    /**
     * Starts the specified process
     */
    public void run(){
        ProcessBuilder builder=new ProcessBuilder(getCommandString());
        builder.redirectError(stdErr);
        builder.redirectOutput(stdOut);
        try{
            this.process=builder.start();
            this.setStatus(JOB_STATUS.RUNNING);
            while(processIsAlive()) {
                try {
                    process.waitFor();
                    if(!processIsAlive()) {
                        if (process.exitValue() == 0) { // success
                            setStatus(JOB_STATUS.FINISHED);
                        } else { // failed
                            setStatus(JOB_STATUS.ERROR);
                        }
                    }
                } catch (InterruptedException ex) {}
            }
        }catch (IOException ex){
            System.err.println("IO Exception when trying to run process"+command);
            this.setStatus(JOB_STATUS.ERROR);
        }
    }

    public String getErrors(){
        return getFileContent(stdErr);
    }

    public String getOutput(){
        return getFileContent(stdOut);
    }


    /**
     * Get content from a file, but never return more than the data return size threshold (technically, also adds a
     * separator that adds a few bytes
     * @param file
     * @return
     */
    private String getFileContent(File file) {
        if(!(file.exists() && file.canRead())){
            return "File doesn't exist or can't be read:"+file.getPath();
        }
        StringBuffer buffer = new StringBuffer();
        try(Reader reader=new FileReader(file)) {
            int fileSize = (int)file.length();
            if(fileSize> DATA_RETURN_SIZE_THRESHOLD){
                char [] chars = new char[DATA_RETURN_SIZE_THRESHOLD];
                reader.read(chars, 0, DATA_RETURN_SIZE_THRESHOLD / 2);
                buffer.append(chars);
                buffer.append("\n"+SEPARATOR+"\n");
                reader.skip(fileSize-DATA_RETURN_SIZE_THRESHOLD/2);
                reader.read(chars, 0, DATA_RETURN_SIZE_THRESHOLD / 2);
                buffer.append(chars);
            }
            else{
                char [] chars = new char[fileSize];
                reader.read(chars);
                buffer.append(chars);
            }
        }
        catch(IOException ex){
            return "Error reading file:"+stdErr.getPath()+"\n"+ex.getMessage();
        }

        return buffer.toString();
    }

}
