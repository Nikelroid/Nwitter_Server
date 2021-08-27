package com.java.launch;

import com.java.botImport.botInputThread;
import com.java.connection.getter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;


public class Main {
    private static final Logger logger = LogManager.getLogger(launchCfg.class);
    public static void main(String[] args) throws URISyntaxException {

        new configsAddress();


            Thread mainThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new getter();
                    } catch (IOException | InterruptedException e) {
                        logger.error("error in load getter class");
                    }
                }
            });
            mainThread.start();

        botInputThread botThread = new botInputThread();
        botThread.start();



        }
}
