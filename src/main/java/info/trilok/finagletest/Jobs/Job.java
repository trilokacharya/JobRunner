package info.trilok.finagletest.Jobs;

/**
 * Created by trilok on 8/25/2014.
 */
public abstract class Job {
    public static enum JOB_STATUS{WAITING, RUNNING, FINISHED, ERROR};

    private Integer id;
    private JOB_STATUS status;

    public Job(Integer id){this.id=id;}

    public Integer getId() {
        return id;
    }

    public JOB_STATUS getStatus() {
        return status;
    }

    public void setStatus(JOB_STATUS status) {
        this.status = status;
    }

    public abstract String getErrors();

    public abstract String getOutput();

    public abstract void start();

}
