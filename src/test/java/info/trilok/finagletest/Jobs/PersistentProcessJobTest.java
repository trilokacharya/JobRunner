package info.trilok.finagletest.Jobs;

import org.junit.Assert;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;

public class PersistentProcessJobTest {

    @org.junit.Test
    public void testStart() throws Exception {

    }

    @org.junit.Test
    public void testGetErrors() throws Exception {
        URL filePath = this.getClass().getClassLoader().getResource("dummyLargeFile");
        File out = new File(filePath.getFile());
        PersistentProcessJob job = new PersistentProcessJob(1,out,out,new LinkedList<String>());
        String content = job.getErrors();
        System.out.println(content);
        Assert.assertTrue(content.length()<500100);
    }

    @org.junit.Test
    public void testGetOutput() throws Exception {

    }
}