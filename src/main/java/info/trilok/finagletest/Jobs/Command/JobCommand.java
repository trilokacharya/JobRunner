package info.trilok.finagletest.Jobs.Command;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tacharya on 9/26/2014.
 */
public final class JobCommand {
    public static enum COMMAND { EXECUTE, CASCADING, GET_STATUS,GET_STDOUT,GET_STDERR,GET_RUN_INFO,TRY_CANCEL};

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

    public String toString(){
        StringBuilder sb = new StringBuilder();
       for(Map.Entry<String,String> entry: args.entrySet()){
        sb.append(entry.getKey()+":"+entry.getValue()+"\n");
        }
       return command.toString()+"\n"+sb.toString();
    }
}
