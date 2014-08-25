package info.trilok.finagletest.Jobs;


import com.twitter.util.*;
import info.trilok.finagletest.Requests.Request;
import org.jboss.netty.handler.codec.http.HttpResponse;
import scala.Function1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by trilok on 8/24/2014.
 */
public class JobCommandHandler implements CommandHandler {

    private ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    //Takes a function and wraps it in a Future so that it's called asynchronously
    ExecutorServiceFuturePool futurePool = new ExecutorServiceFuturePool(executorService);

    private static volatile CommandHandler instance;

    private JobCommandHandler(){}

    /**
     * get singleton instance
     * @return
     */
    public static synchronized CommandHandler getInstance(){
        if(instance==null){
            instance=new JobCommandHandler();
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
       return request.command+" -> Command Received. Will perform.";
    }

}
