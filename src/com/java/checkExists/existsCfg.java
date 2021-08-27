package com.java.checkExists;

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

public class existsCfg {
    protected static String database = "";
    protected static String collection = "";
    protected static String messages = "";
    private static final Logger logger = LogManager.getLogger(existsCfg.class);
    public existsCfg() throws IOException {
        Path simple = Paths.get("simple.png");
        Path path = Paths.get(simple.toAbsolutePath().getParent() + configsAddress.exists);
        File configFile = new File(String.valueOf(path));

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            database = props.getProperty("database");
            collection = props.getProperty("collection");
            messages = props.getProperty("messages");
            logger.info("existsCfg red successfully");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file existsCfg doesnt exists");
        } catch (IOException ex) {
            logger.error("error in reading config existsCfg");
        }
    }

}
