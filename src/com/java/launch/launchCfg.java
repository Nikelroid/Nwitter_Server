package com.java.launch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class launchCfg {

    protected static String uri;
        private static final Logger logger = LogManager.getLogger(launchCfg.class);
        public launchCfg() throws IOException {
            Path simple = Paths.get("simple.png");
            Path path = Paths.get(simple.toAbsolutePath().getParent() + configsAddress.launch);
            File configFile = new File(String.valueOf(path));

            try {
                FileReader reader = new FileReader(configFile);
                Properties props = new Properties();
                props.load(reader);

                 uri = props.getProperty("uri");

                logger.info("launchCfg red successfully");
                reader.close();
            } catch (FileNotFoundException ex) {
                logger.error("file doesnt exists");
            } catch (IOException ex) {
                logger.error("error in reading config");
            }
        }


}
