package com.java.launch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class configsAddress {

    public static String category;
    public static String authKey;
    public static String botRegister;
    public static String edit;
    public static String connection;
    public static String groups;
    public static String database;
    public static String login;
    public static String exists;
    public static String list;
    public static String load;
    public static String messenger;
    public static String notifs;
    public static String operate;
    public static String profile;
    public static String launch;
    public static String setting;

    private static final Logger logger = LogManager.getLogger(configsAddress.class);
    public configsAddress() throws URISyntaxException {
        String configs_dir = System.getenv("configs_address_path");
        Path simple = Paths.get("simple.png");
        Path path = Paths.get(simple.toAbsolutePath().getParent() +configs_dir);
        File configFile = new File(String.valueOf(path));
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);



            category = props.getProperty("category");
            authKey = props.getProperty("authKey");
            botRegister = props.getProperty("botRegister");
            connection = props.getProperty("connection");
            database = props.getProperty("database");
            edit = props.getProperty("edit");
            exists = props.getProperty("exists");
            groups = props.getProperty("groups");
            list = props.getProperty("list");
            launch = props.getProperty("launch");
            load = props.getProperty("load");
            login = props.getProperty("login");
            messenger = props.getProperty("messenger");
            notifs = props.getProperty("notifs");
            operate = props.getProperty("operate");
            profile = props.getProperty("profile");
            setting = props.getProperty("setting");


            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}
