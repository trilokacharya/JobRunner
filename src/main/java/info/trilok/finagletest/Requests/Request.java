package info.trilok.finagletest.Requests;

import info.trilok.finagletest.Jobs.Command.JobCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by trilok on 8/23/2014.
 */
public class Request {
    public static enum REQUEST_TYPE {CREATE, INFO, MANAGE};

    public REQUEST_TYPE request;
    public JobCommand details;
    public Long jobId;

    public boolean isValid(){
        return details!=null && details.isValid();
    }

    public String toString(){
        return request.toString()+"\n"+details.toString();
    }
}
