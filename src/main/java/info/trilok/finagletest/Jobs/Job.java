package info.trilok.finagletest.Jobs;

import info.trilok.finagletest.Jobs.Command.JobCommand;

import java.util.Date;

/**
 * Created by trilok on 8/25/2014.
 */
public abstract class Job implements Runnable{
    public static enum JOB_STATUS{WAITING, RUNNING, FINISHED, ERROR};

    private Long id;
    private JOB_STATUS status;
    private Date startTime;
    private Date endTime;
    private JobCommand jobCommand;

    private JobType type;

    public Job(Long id){this.id=id;}

    public Long getId() {
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

    @Override
    public abstract void run();

    public abstract boolean tryCancel(); // try to cancel this job

    public Date getStartTime(){return startTime;}
    public Date getEndTime(){return endTime;}
    public JobCommand getJobCommand(){return jobCommand;}
    public JobType getJobType(){return type;}


}
