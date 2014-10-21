package info.trilok.jobrunner;

import com.twitter.finagle.*;
import com.twitter.util.*;

import com.twitter.util.TimeoutException;

import java.io.IOException;

/**
 * Created by trilok on 8/23/2014.
 */
public class Server {

    public static void main(String[] args) throws TimeoutException {
        System.out.println("Reading Config");
        String port="8081";
        try {
            port = info.trilok.jobrunner.config.Config.getInstance().getProperty("port");
            if(port==null){
                System.err.println("Port is not defined. Using default: 8081");
            }
        }catch(IOException ex){
            System.err.println("Error: Unable to read config file");
            return;
        }
        System.out.println("Starting Server");

        Service restService = new RestService();

        JsonService jsonService = new JsonService();
        try {
            ListeningServer server=Http.serve(":"+port,jsonService);

            //ListeningServer server=Http.serve(":"+port,restService);
            Await.ready(server);

        }catch(InterruptedException ex) {}

    }
}
