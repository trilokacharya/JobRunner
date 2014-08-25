package info.trilok.finagletest.Jobs.jobmanager;

import info.trilok.finagletest.Jobs.Handler.JobCommand;
import info.trilok.finagletest.Requests.Request;

/**
 * Created by trilok on 8/25/2014.
 */
public interface JobManager {
    void RegisterHandler(String name, JobCommand handler);

    String RunCommand(Request request);
}
