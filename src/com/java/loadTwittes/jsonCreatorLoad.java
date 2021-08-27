package com.java.loadTwittes;

import com.java.database.getDocument;
import com.java.database.getInfoDatabase;
import com.java.profile.getTwittePic;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.action.IfNot;
import org.bson.Document;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class jsonCreatorLoad {

    ArrayList<String> usernames ;
    ArrayList<String> text ;
    ArrayList<String> date ;
    ArrayList<Integer> serials ;
    ArrayList<Integer> likes ;
    ArrayList<Integer> comments ;
    ArrayList<Integer> retwittes ;
    ArrayList<Boolean> isSaved ;
    ArrayList<Boolean> isLiked ;
    ArrayList<Boolean> isRetwitted ;
    ArrayList<String> pic ;

    getTwittePic getTwittePic = new getTwittePic();
    JSONObject jsonOutput = new JSONObject();
    MongoCollection<Document> twitterCollection;
    MongoCollection<Document> usersCollection;
    List<Integer> saved = new ArrayList<>();
    getDocument getDocument = new getDocument();
    getInfoDatabase getInfoDatabase = new getInfoDatabase();
    private static final Logger logger = LogManager.getLogger(jsonCreatorLoad.class);

    private void  listRemaker (){
        usernames = new ArrayList<>();
        text = new ArrayList<>();
        date = new ArrayList<>();
        serials = new ArrayList<>();
        likes = new ArrayList<>();
        comments = new ArrayList<>();
        retwittes = new ArrayList<>();
        isSaved = new ArrayList<>();
        isLiked = new ArrayList<>();
        isRetwitted = new ArrayList<>();
        pic = new ArrayList<>();
    }
    private void adder(Document user,List<String> serialOutput) throws IOException {
        saved = getInfoDatabase.getInfoIntegerList(user,"twittesaved");
        if (twitterCollection == null)
            logger.error("error in loading database");
        else
            for (int i = serialOutput.size() - 1; i >= 0; i--) {

                Document twitte = getDocument.get(twitterCollection,"_id",
                        Integer.parseInt(serialOutput.get(i)));
                if (twitte == null) {
                    logger.warn("No saved twitte found");
                } else {

                    usernames.add(twitte.getString("sender"));
                    text.add(twitte.getString("text"));
                    date.add(twitte.getString("time"));
                    serials.add(twitte.getInteger("_id"));
                    likes.add(twitte.getList("likes", String.class).size());
                    comments.add(twitte.getList("comments", Integer.class).size());
                    retwittes.add(twitte.getList("retwittes", String.class).size());
                    if (saved.contains(twitte.getInteger("_id")))
                        isSaved.add(true);
                    else
                        isSaved.add(false);
                    if (twitte.getList("likes", String.class).
                            contains(user.getString("username")))
                        isLiked.add(true);
                    else
                        isLiked.add(false);
                    if (twitte.getList("retwittes", String.class).
                            contains(user.getString("username")))
                        isRetwitted.add(true);
                    else
                        isRetwitted.add(false);
                    pic.add(getTwittePic.get(twitte.get("_id").toString()));
                    if (usernames.size() > 50) break;
                }
            }
    }
    private void importer(){
        logger.info("load json crated");
        jsonOutput = new JSONObject();
        jsonOutput.put("key", "load").
                put("usernames", usernames).
                put("text", text).
                put("date", date).
                put("serials", serials).
                put("likes", likes).
                put("comments", comments).
                put("retwittes", retwittes).
                put("isSaved", isSaved).
                put("isLiked", isLiked).
                put("isRetwitted", isRetwitted).
                put("pic", pic).
                put("result",1);
    }

    public JSONObject creator(ArrayList<String> serialOutput, int AuthKey) throws IOException {
        if (!serialOutput.isEmpty())
        if (serialOutput.get(0).equals("~")) {
            jsonOutput.put("result", 0);
            return jsonOutput;
        }

        try {
            new loadCfg();
        } catch (IOException e) {
            logger.error("couldn't open config of load");
        }
        try {
            MongoClient mongoClient = MongoClients.create();
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase(loadCfg.database);
            usersCollection = sampleTrainingDB.getCollection(loadCfg.usersCollection);
            twitterCollection = sampleTrainingDB.getCollection(loadCfg.twittesCollection);
        } catch (Exception e) {
            logger.error("couldn't open database of load");
        }

        listRemaker();



        Document user = getDocument.get(usersCollection,"AuthKey", AuthKey);

        if (user == null) {
            logger.error("error in connection with database");
        } else {

            adder(user,serialOutput);
            importer();

        }
        return jsonOutput;
    }
}
