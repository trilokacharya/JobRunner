package info.trilok.jobrunner.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by tacharya on 10/6/2014.
 */
public class Config {

    private static final String FILE_NAME="config.properties";
    private static volatile Config instance=null;

    private Properties prop = new Properties();

    private Config(String configFileName) throws IOException{
        try(InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(configFileName) ){
            prop.load(inputStream);
        }catch(IOException ex){
            System.err.println("Cannot open properties file:"+configFileName);
            throw ex;
        }
    }

    public static Config getInstance() throws IOException{
        if(instance==null){
            synchronized (Config.class){
                instance = new Config(FILE_NAME);
            }
        }
        return instance;
    }

    /**
     * Get this property from the config.properties file under resources. If the config key isn't found, it returns null
     * @param key
     * @return
     */
    public String getProperty(String key){
        return prop.getProperty(key);
    }
}
