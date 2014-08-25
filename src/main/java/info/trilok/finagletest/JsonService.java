package info.trilok.finagletest;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.twitter.finagle.Service;
import com.twitter.util.Future;
import com.twitter.util.Promise;
import info.trilok.finagletest.Jobs.CommandHandler;
import info.trilok.finagletest.Jobs.JobCommandHandler;
import info.trilok.finagletest.Requests.Request;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.*;
import scala.Function1;
import scala.runtime.AbstractFunction1;

import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by trilok on 8/24/2014.
 */
public class JsonService extends Service<HttpRequest, HttpResponse> {

    CommandHandler jobCommandHandler = JobCommandHandler.getInstance();

    /**
     * HttpResponse creator that takes in a Future<String> message as the content for the body
     * @param request
     * @param message
     * @param status
     * @return
     */
    private Future<HttpResponse> createResponseFromMsg(final HttpRequest request, Future<String> message, final HttpResponseStatus status){
        return message.map(new AbstractFunction1<String, Future<HttpResponse>>() {
            @Override
            public Future<HttpResponse> apply(String msg) {
                return createResponseFromMsg(request,msg,status);
            }
        }).apply();
    }

    /**
     * Synchronouse version of the function that takes a String message instead of a future
     * @param request
     * @param message
     * @param status
     * @return
     */
    private Future<HttpResponse> createResponseFromMsg(HttpRequest request, String message, HttpResponseStatus status){
        HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), status);
        response.setContent(ChannelBuffers.copiedBuffer(message,Charset.defaultCharset()));
        return Future.value(response);
    }

    @Override
    public Future<HttpResponse> apply(HttpRequest request) {
        Request jsonRequest=null;

        // expect JSON
        if (request.getMethod() != HttpMethod.POST) {
            return createResponseFromMsg(request,"This service only accepts POST requests",HttpResponseStatus.METHOD_NOT_ALLOWED);
        }

        // Get the requests's content
        String content= request.getContent().toString(Charset.defaultCharset());

        try { // try to parse the Json
            jsonRequest= new Gson().fromJson(content, Request.class);
        }catch(JsonSyntaxException ex){
            System.err.println(ex.getMessage());
            return createResponseFromMsg(request,"Error parsing JSON:"+ex.getMessage(), HttpResponseStatus.BAD_REQUEST);
        }
        // Debug message
        System.out.println("Cmd =" + jsonRequest.command);

        // Call the jobCommand's handler and get a response back
        Future<String> responseString = jobCommandHandler.handleCommand(jsonRequest);

        // Return response asynchronously
        return createResponseFromMsg(request,responseString,HttpResponseStatus.ACCEPTED);

    }
}
