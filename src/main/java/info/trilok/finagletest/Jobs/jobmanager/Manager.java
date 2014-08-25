package info.trilok.finagletest.Jobs.jobmanager;

/**
 * Created by trilok on 8/25/2014.
 */
public class Manager {
    private static volatile Manager instance;

    public static synchronized Manager getInstance() {
        if(instance==null){
            instance=new Manager();
        }
        return instance;
    }


}
