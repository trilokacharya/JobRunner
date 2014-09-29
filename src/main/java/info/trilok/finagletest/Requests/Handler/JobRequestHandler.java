package info.trilok.finagletest.Requests.Handler;


import com.twitter.util.*;
import info.trilok.finagletest.Requests.Request;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by trilok on 8/24/2014.
 */
public class JobRequestHandler implements RequestHandler {

    private ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //Takes a function and wraps it in a Future so that it's called asynchronously
    ExecutorServiceFuturePool futurePool = new ExecutorServiceFuturePool(executorService);

    private static volatile RequestHandler instance;

    private JobRequestHandler(){}

    /**
     * get singleton instance
     * @return
     */
    public static synchronized RequestHandler getInstance(){
        if(instance==null){
            instance=new JobRequestHandler();
        }
        return instance;
    }

    /**
     * Wraps the syncHandleCommand in a FuturePool to asynchronously handle requests
     * @param request
     * @return
     */
    @Override
    public Future<String> handleCommand(final Request request) {
        Function0<String> executeCommand= new Function0<String>() {
            @Override
            public String apply() {
                return syncHandleCommand(request);
            }
        };

        return futurePool.apply(executeCommand);
    }

    /**
     * Synchronously handles given job request
     * @param request
     * @return
     */
    private String syncHandleCommand(Request request){
       return request.toString()+" -> Command Received. Will perform.";
    }

}
