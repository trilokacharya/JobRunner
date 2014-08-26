package info.trilok.finagletest.Jobs;

/**
 * Created by trilok on 8/25/2014.
 */
public abstract class Job {
    public static enum JOB_STATUS{WAITING, RUNNING, TERMINATED};

    private Integer id;
    private JOB_STATUS status;

    public Job(Integer id){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JOB_STATUS getStatus() {
        return status;
    }

    public void setStatus(JOB_STATUS status) {
        this.status = status;
    }






}
