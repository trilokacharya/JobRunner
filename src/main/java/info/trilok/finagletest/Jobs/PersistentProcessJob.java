package info.trilok.finagletest.Jobs;

import com.google.common.base.Joiner;

import java.io.*;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.util.List;

/**
 * Created by trilok on 8/25/2014.
 */
public class PersistentProcessJob extends Job{
    private Process process;
    private final List<String> command;

    // Max amount of output and Error data that we would return
    private final int DATA_RETURN_SIZE_THRESHOLD = 500000;// 500 kb

    // When there is too much data, we show the top and the bottom threshold/2 characters. This is the separator.
    private final String SEPARATOR = ".......................";

    private final File stdOut,stdErr;

    public PersistentProcessJob(Integer id, File stdOut, File stdErr, List<String> command){
        super(id);
        this.command=command;
        this.stdOut=stdOut;
        this.stdErr=stdErr;
    }

    /**
     * Starts the specified process
     */
    public void start(){
        ProcessBuilder builder=new ProcessBuilder(command);
        builder.redirectError(stdErr);
        builder.redirectOutput(stdOut);
        try{
            this.process=builder.start();
            this.setStatus(JOB_STATUS.RUNNING);
            while(process.isAlive()) {
                try {
                    process.waitFor(); // wait for the job to finish
                    if(process.exitValue()==0){ // success
                        setStatus(JOB_STATUS.FINISHED);
                    }else{ // failed
                        setStatus(JOB_STATUS.ERROR);
                    }
                } catch (InterruptedException ex) {}
            }
        }catch (IOException ex){
            System.err.println("IO Exception when trying to run process"+ Joiner.on(" ").join(command));
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
