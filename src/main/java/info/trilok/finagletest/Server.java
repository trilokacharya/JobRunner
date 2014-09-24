package info.trilok.finagletest;

import com.twitter.finagle.*;
import com.twitter.util.*;
import java.net.*;
import java.nio.charset.Charset;

import com.twitter.util.TimeoutException;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil.*;
import org.jboss.netty.handler.codec.http.*;
import sun.text.normalizer.UTF16;
import com.twitter.finagle.builder.ServerBuilder;

/**
 * Created by trilok on 8/23/2014.
 */
public class Server {

    public static void main(String[] args) throws TimeoutException {
        System.out.println("Starting Server");

        Service restService = new RestService();

        JsonService jsonService = new JsonService();
        try {
            ListeningServer server=Http.serve(":8080",jsonService);

            //ListeningServer server=Http.serve(":8080",restService);
            Await.ready(server);

        }catch(InterruptedException ex) {}

    }
}
