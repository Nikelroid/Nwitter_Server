package com.java.profile;

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

public class profileCfg {
    protected static String database = "";
    protected static String usersCollection = "";
    protected static String twittesCollection = "";
    protected static String reportsCollection = "";
    protected static String notifsCollection = "";
    protected static String messagesCollection = "";
    protected static String profilePicPath = "";
    protected static String twittePicPath = "";
    protected static String messagePicPath = "";
    protected static int reportsNumLimit = 0;
    private static final Logger logger = LogManager.getLogger(profileCfg.class);
    public profileCfg() throws IOException {
        Path simple = Paths.get("simple.png");
        Path path = Paths.get(simple.toAbsolutePath().getParent() + configsAddress.profile);
        File configFile = new File(String.valueOf(path));

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            database = props.getProperty("database");
            usersCollection = props.getProperty("users_collection");
            twittesCollection = props.getProperty("twittes_collection");
            reportsCollection = props.getProperty("reports_collection");
            notifsCollection = props.getProperty("notifs_collection");
            messagesCollection = props.getProperty("messages_collection");
            profilePicPath = props.getProperty("profile_pic_path");
            twittePicPath = props.getProperty("twitte_pic_path");
            messagePicPath = props.getProperty("message_pic_path");
            reportsNumLimit = Integer.parseInt(props.getProperty("reports_num"));

            logger.info("loadCfg red successfully");
            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file loadCfg doesnt exists");
        } catch (IOException ex) {
            logger.error("error in reading config profileCfg");
        }
        if (database.isEmpty())database="Nwitter";
        if (usersCollection.isEmpty())usersCollection="Users";
        if (twittesCollection.isEmpty())twittesCollection="Twittes";
        if (reportsCollection.isEmpty())reportsCollection="Reports";
        if (messagesCollection.isEmpty())messagesCollection="Messages";
        if (profilePicPath.isEmpty())profilePicPath="\\src\\resources\\profiles\\";
        if (twittePicPath.isEmpty())twittePicPath="\\src\\resources\\twittes\\";
        if (messagePicPath.isEmpty())messagePicPath="\\src\\resources\\messages\\";


        if (reportsNumLimit==0)reportsNumLimit=5;
    }

}
