package info.trilok.finagletest.Requests.Handler;

import com.twitter.util.Future;
import info.trilok.finagletest.Requests.Request;

/**
 * Created by trilok on 8/24/2014.
 */
public interface CommandHandler {
    Future<String> handleCommand(Request request);
}
