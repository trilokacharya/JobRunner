package info.trilok.finagletest;

import com.twitter.util.ExecutorServiceFuturePool;
import com.twitter.util.Future;
import info.trilok.finagletest.Jobs.Factory.JobFactory;
import info.trilok.finagletest.Jobs.Job;
import info.trilok.finagletest.Jobs.Response.Response;
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
    private HashMap<Long, Job> currentJobs = new HashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
    private ExecutorServiceFuturePool pool = new ExecutorServiceFuturePool(executorService);
    String result="";
    Response response = new Response();

    public Response runCommand(Request request){
        if(request.request == Request.REQUEST_TYPE.CREATE){
            try {
                final Job j=JobFactory.Create(request.details);
                currentJobs.put(j.getId(),j);
                j.setStatus(Job.JOB_STATUS.WAITING);
                try {
                    j.setupJob();
//                    result=j.getId().toString();
                    response.status= Response.RESPONSE_STATUS.OK;
                    response.message=j.getId().toString();
                }catch(Exception ex){
                    response.status= Response.RESPONSE_STATUS.ERROR;
                    response.message=ex.getMessage();
//                    result="Error:"+ex.getMessage();
                    System.err.println("Error:"+ex.getMessage());

                }
                executorService.submit(j); // actually run the job now
            }catch(IOException ioex){
                System.err.println("Error creating job"+ioex.getMessage());
            }
        }else if(request.request == Request.REQUEST_TYPE.INFO){
            Job j = currentJobs.get(request.jobId);
            if(j==null){
//                result= "Job not found:"+request.jobId.toString();
                response.status= Response.RESPONSE_STATUS.ERROR;
                response.message="Job not found:"+request.jobId.toString();

            }else{
                response.status= Response.RESPONSE_STATUS.OK;
                switch(request.details.getCommand()){
                    case GET_RUN_INFO:
//                        result = j.getJobInfo();
                        response.message = j.getJobInfo();
                        break;
                    case GET_STATUS:
//                        result = j.getStatus().toString();
                        response.message= j.getStatus().toString();
                        break;
                    case GET_STDERR:
//                        result = j.getErrors();
                          response.message= j.getErrors();
                        break;
                    case GET_STDOUT:
                        //result = j.getOutput();
                        response.message= j.getOutput();
                        break;
                    default:
                        response.status= Response.RESPONSE_STATUS.ERROR;
                        response.message="Unhandled Command:"+request.details.getCommand().toString();
                }
            }
        }

        return response;
    }
}
