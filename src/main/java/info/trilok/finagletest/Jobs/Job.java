package info.trilok.finagletest.Jobs;

import info.trilok.finagletest.Jobs.Command.JobCommand;

import java.util.Date;

/**
 * Created by trilok on 8/25/2014.
 */
public abstract class Job {
    public static enum JOB_STATUS{WAITING, RUNNING, FINISHED, ERROR};

    private Integer id;
    private JOB_STATUS status;
    private Date startTime;
    private Date endTime;
    private JobCommand jobCommand;

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

    public Date getStartTime(){return startTime;}
    public Date getEndTime(){return endTime;}
    public JobCommand getJobCommand(){return jobCommand;}

}
