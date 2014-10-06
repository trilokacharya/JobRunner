package info.trilok.jobrunner.Jobs.Response;

/**
 * Created by tacharya on 10/3/2014.
 */
public class Response {
    public enum RESPONSE_STATUS{OK,ERROR};
    public String message;
    public RESPONSE_STATUS status;
}
