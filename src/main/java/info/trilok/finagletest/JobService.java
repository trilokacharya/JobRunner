package info.trilok.finagletest;

import com.twitter.util.Future;
import info.trilok.finagletest.Jobs.Factory.JobFactory;
import info.trilok.finagletest.Jobs.Job;
import info.trilok.finagletest.Requests.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tacharya on 9/29/2014.
 */
public class JobService {
    private static final int NUM_THREADS= 2; // won't be running many concurrent jobs
    private HashMap<Long, Future<Job>> currentJobs = new HashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

    public Future<String> runCommand(Request request){
        if(request.request == Request.REQUEST_TYPE.CREATE){
            try {
                Job j=JobFactory.Create(request.details);
                executorService.submit(j);
                return Future.value(j.getId().toString());
            }catch(IOException ioex){
                System.err.println("Error creating job"+ioex.getMessage());
                return Future.value("");
            }
        }
        return Future.value("");
    }
}
