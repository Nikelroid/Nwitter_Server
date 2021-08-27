package com.java.list;

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

public class listCfg {
    protected static String database = "";
    protected static String usersCollection = "";
    protected static String twittesCollection = "";
    protected static String reportsCollection = "";
    protected static int reportsNumLimit = 0;
    private static final Logger logger = LogManager.getLogger(listCfg.class);
    public listCfg() throws IOException {
        Path simple = Paths.get("simple.png");
        Path path = Paths.get(simple.toAbsolutePath().getParent() + configsAddress.list);
        File configFile = new File(String.valueOf(path));

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            database = props.getProperty("database");
            usersCollection = props.getProperty("users_collection");
            twittesCollection = props.getProperty("twittes_collection");
            reportsCollection = props.getProperty("reports_collection");
            reportsNumLimit = Integer.parseInt(props.getProperty("reports_num"));
            logger.info("loadCfg red successfully");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file loadCfg doesnt exists");
        } catch (IOException ex) {
            logger.error("error in reading config loadCfg");
        }
        if (database.isEmpty())database="Nwitter";
        if (usersCollection.isEmpty())usersCollection="Users";
        if (twittesCollection.isEmpty())twittesCollection="Twittes";
        if (reportsCollection.isEmpty())reportsCollection="Reports";
        if (reportsNumLimit==0)reportsNumLimit=5;
    }

}
