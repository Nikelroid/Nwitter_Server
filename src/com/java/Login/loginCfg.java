package com.java.Login;

import com.java.launch.configsAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class loginCfg {
    protected static String database = "";
    protected static String collection = "";
    private static final Logger logger = LogManager.getLogger(loginCfg.class);
    public loginCfg() throws IOException {
        Path simple = Paths.get("simple.png");
        Path path = Paths.get(simple.toAbsolutePath().getParent() + configsAddress.login);
        File configFile = new File(String.valueOf(path));

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            database = props.getProperty("database");
            collection = props.getProperty("collection");
            logger.info("loginCfg red successfully");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file loginCfg doesnt exists");
        } catch (IOException ex) {
            logger.error("error in reading config loginCfg");
        }
    }

}
