package info.trilok.finagletest.Jobs.Command;

import java.util.HashMap;

/**
 * Created by tacharya on 9/26/2014.
 */
public final class JobCommand {
    public static enum COMMAND { EXECUTE, GET_STATUS,GET_STDOUT,GET_STDERR,GET_RUN_INFO,TRY_CANCEL};

    private final COMMAND command;
    private final HashMap<String,String> args;

    public JobCommand(COMMAND cmd, HashMap<String,String> args){
        this.command= cmd;
        this.args=args;
    }

    public COMMAND getCommand(){
        return command;
    }
    public HashMap<String,String> getArgs(){
        return args;
    }
}
