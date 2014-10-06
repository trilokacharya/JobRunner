package info.trilok.jobrunner;

import com.twitter.finagle.*;
import com.twitter.util.*;

import com.twitter.util.TimeoutException;

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
