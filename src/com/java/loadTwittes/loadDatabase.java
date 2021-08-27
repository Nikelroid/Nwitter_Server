package com.java.loadTwittes;

import com.java.database.getDocument;
import com.java.database.getInfoDatabase;
import com.mongodb.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class loadDatabase {
    private static final Logger logger = LogManager.getLogger(loadDatabase.class);

    ArrayList<String> serialList = new ArrayList<>();
    List<String> followings = new ArrayList<>();
    List<String> mutes = new ArrayList<>();
    getInfoDatabase getInfoDatabase = new getInfoDatabase();
    getDocument getDocument = new getDocument();
    public ArrayList<String> get(int AuthKey) {
        serialList = new ArrayList<>();
        try {
            new loadCfg();
        } catch (IOException e) {
            logger.error("couldn't open config of login");
        }


        try {
            MongoClient mongoClient = MongoClients.create();
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase(loadCfg.database);
            MongoCollection<Document> userCollection = sampleTrainingDB.getCollection(
                    loadCfg.usersCollection);
            MongoCollection<Document> twitterCollection = sampleTrainingDB.getCollection(
                    loadCfg.twittesCollection);
            MongoCollection<Document> reportsCollection =  sampleTrainingDB.getCollection(
                    loadCfg.reportsCollection);

            Document user = getDocument.get(userCollection,"AuthKey", AuthKey);
            if(user==null){
                logger.error("error in connection with database");
            }else{
                followings = getInfoDatabase.getInfoStringList(user,"followings");
                mutes= getInfoDatabase.getInfoStringList(user,"mutes");
            }
            for (Document twitte :  twitterCollection.find()) {
                int reports;

                    Bson filter = eq("serial", twitte.getInteger("_id"));
                    reports = (int) reportsCollection.count(filter);
                List<String> retwitters = twitte.getList("retwittes",String.class);
                boolean isReted = false;



                for (int i = 0; i < retwitters.size(); i++) {

                    Document anotherUser = com.java.database.getDocument.get(userCollection,"username", retwitters.get(i));

                    if (anotherUser!=null) {

                        List<String> userBlocks= getInfoDatabase.getInfoStringList(anotherUser,"blocks");
                        if (followings.contains(retwitters.get(i)) && !mutes.contains(retwitters.get(i))
                                && (Boolean) anotherUser.get("enable") &&
                                !userBlocks.contains(user.getString("username"))) {
                            isReted = true;
                            break;
                        }
                    }else{
                        logger.warn("User not found");
                    }
                }
                Document anotherUser = getDocument.get(userCollection,"username",
                        twitte.get("sender").toString());

                List<String>  userBlocks= new ArrayList<>();
                Boolean isEnable = true;
                if (anotherUser!=null){
                    userBlocks = anotherUser.getList("blocks",String.class);
                    isEnable = (Boolean) anotherUser.get("enable");
                }


                    if((followings.contains(twitte.get("sender").toString())||isReted)
                && !mutes.contains(twitte.get("sender").toString()) &&
                            isEnable &&
                            !userBlocks.contains(user.getString("username"))
                && reports<loadCfg.reportsNumLimit){
                    serialList.add(twitte.get("_id").toString());
                    }
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error("error in opening database");
        }

            return serialList;
        }

    }

