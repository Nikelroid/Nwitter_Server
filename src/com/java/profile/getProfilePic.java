package com.java.profile;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class getProfilePic {

    private static final Logger logger = LogManager.getLogger(getProfilePic.class);

    public String get(String username) throws IOException {

        try {
            new profileCfg();
        } catch (IOException e) {
            logger.error("couldn't open config of login");
        }

        byte[] fileContent = new byte[0];
        String encodedString = null;
        try {

                Path simple = Paths.get("simple.png");
                File file = new File(
                simple.toAbsolutePath().getParent()+
                        profileCfg.profilePicPath+
                        username+ ".png");


            try {
                fileContent = FileUtils.readFileToByteArray(file);
                encodedString = Base64.getEncoder().encodeToString(fileContent);
            } catch (IOException ioException) {
                encodedString = "";
            }


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error in opening database");
            return "";
        }
        return encodedString;
    }
}
