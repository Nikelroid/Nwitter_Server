package com.java.operation;

import com.java.database.databaseControl;
import com.java.database.getDocument;
import com.java.database.getInfoDatabase;
import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class submitTwitte {
    private static final Logger logger = LogManager.getLogger(submitTwitte.class);
    databaseControl dataBaseControl = new databaseControl();
    getInfoDatabase getInfoDatabase = new getInfoDatabase();
    getDocument getDocument = new getDocument();
    List<String> likes ;
    int result = 0;
    private int idFinder(MongoCollection<Document> twitteCollection){
        int ID = 0;
        for (Document twitte:twitteCollection.find()) {
            ID = twitte.getInteger("_id");
        }

        ID++;
        return ID;
    }
    public int adder(String AuthKey, String text) {
        try {
            new operateCfg();
        } catch (IOException e) {
            logger.error("couldn't open config of operates");
        }

        MongoCollection<Document> twitteCollection = dataBaseControl.getCollection(operateCfg.twittesCollection);


        MongoCollection<Document> userCollection = dataBaseControl.getCollection(operateCfg.usersCollection);
        Document user = getDocument.get(userCollection,"AuthKey",Integer.parseInt(AuthKey));


        ArrayList<String> likes = new ArrayList<>(Collections.singleton("likes:"));
        ArrayList<Integer> comments = new ArrayList<>(Collections.singleton(0));
        ArrayList<String> retwittes = new ArrayList<>(Collections.singleton("retwittes:"));

        int ID = idFinder(twitteCollection);

        if(user!=null){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            Document newTwitte = new Document("_id", ID);
            newTwitte.append("text", text).
                    append("sender", user.get("username").toString()).
                    append("likes", likes).
                    append("comments", comments).
                    append("retwittes", retwittes).
                    append("time", dtf.format(now));
                    twitteCollection.insertOne(newTwitte);

                    result = ID;

        }
        return result;
    }
}

