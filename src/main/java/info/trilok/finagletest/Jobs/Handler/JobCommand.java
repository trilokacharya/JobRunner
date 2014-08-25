package info.trilok.finagletest.Jobs.Handler;

import info.trilok.finagletest.Jobs.jobmanager.JobManager;
import info.trilok.finagletest.Requests.Request;

/**
 * Created by trilok on 8/25/2014.
 */
public interface JobCommand {
    String execute(Request request,JobManager jobManager);
}
