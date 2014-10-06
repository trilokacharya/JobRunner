package info.trilok.jobrunner;

import com.twitter.finagle.Service;
import com.twitter.util.Future;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.*;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import scala.runtime.AbstractFunction1;

/**
 * Created by trilok on 8/23/2014.
 */

/*
    First steps towards a Rest service
 */
public class RestService extends Service<HttpRequest, HttpResponse> {

    Splitter splitter= Splitter.on("/").omitEmptyStrings();
    HashMap<String,String> someResponse = new HashMap<String, String>();

    public RestService(){
        this.someResponse.put("create","Ok, will create");
        this.someResponse.put("status","Ok, will provide status");
        this.someResponse.put("kill","Ok, will kill job");
    }

    /**
     * Simulates an async call that takes in a REST path map and returns a Future<String> response.
     * This would be the method which would get all of the GET/ POST handlers and determine which method
     * to call to handle which REST call.
     * For now, it only returns the value from a dictionary
     * @param params
     * @return
     */
    private Future<String> getResponse(ArrayList<String> params){
        String responseContent="";
        responseContent=someResponse.get(params.get(0));
        return Future.value(responseContent);
    }



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

    // Synchronous message version that gets a string instead of a Future
    private Future<HttpResponse> createResponseFromMsg(HttpRequest request, String message, HttpResponseStatus status){
        HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), status);
        response.setContent(ChannelBuffers.copiedBuffer(message,Charset.defaultCharset()));
        return Future.value(response);
    }


    @Override
    public Future<HttpResponse> apply(HttpRequest request) {
        System.out.println("URI = "+request.getUri());
        ArrayList<String> splitPath=Lists.newArrayList(splitter.split(request.getUri().toLowerCase()));
        for(String s:splitPath){
            System.out.println("Split Level="+s);
        }
        Future<String> responseContent=null;
        if(this.someResponse.containsKey(splitPath.get(0))){
            responseContent=getResponse(splitPath);
        }

        return createResponseFromMsg(request,responseContent,HttpResponseStatus.OK);
    }
}

