package com.java.setting;

import com.java.database.databaseControl;
import com.java.database.getDocument;
import com.java.database.getInfoDatabase;
import com.java.operation.operateCfg;
import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class delete {
    private static final Logger logger = LogManager.getLogger(delete.class);
    databaseControl dataBaseControl = new databaseControl();
    getInfoDatabase getInfoDatabase = new getInfoDatabase();
    getDocument getDocument = new getDocument();
    public int setter(String AuthKey) {
        try {

            try {
                new settingCfg();
            } catch (IOException e) {
                logger.error("couldn't open config of operates");
            }

            MongoCollection<Document> usersCollection = dataBaseControl.getCollection(settingCfg.usersCollection);
            MongoCollection<Document> twittesCollection = dataBaseControl.getCollection(settingCfg.twittesCollection);
            MongoCollection<Document> reportCollection = dataBaseControl.getCollection(settingCfg.reportsCollection);
            MongoCollection<Document> notifsCollection = dataBaseControl.getCollection(settingCfg.notifsCollection);
            MongoCollection<Document> messagesCollection = dataBaseControl.getCollection(settingCfg.messagesCollection);

            Document user = getDocument.get(usersCollection,"AuthKey", Integer.parseInt(AuthKey));

                    String username = user.get("username").toString();

            for (Document twitte1 : twittesCollection.find()) {
                if (twitte1.get("sender").toString().equals(username)) {
                    int ID = Integer.parseInt(twitte1.get("_id").toString());
                    for (Document twitte2 : twittesCollection.find()) {

                        int ID2 = Integer.parseInt(twitte2.get("_id").toString());
                        Bson filer = eq("_id",ID2);
                        List<Integer> comments = getInfoDatabase.getInfoIntegerList(twitte2,"comments");
                        comments.remove(Integer.valueOf(ID));

                        List<Integer> retwittes = getInfoDatabase.getInfoIntegerList(twitte2,"retwittes");
                        List<Integer> likes = getInfoDatabase.getInfoIntegerList(twitte2,"likes");


                        retwittes.remove(username);
                        likes.remove(username);

                        usersCollection.findOneAndUpdate(filer, set("comments", comments));
                        usersCollection.findOneAndUpdate(filer, set("retwittes", retwittes));
                        usersCollection.findOneAndUpdate(filer, set("likes", likes));

                    }
                }
            }

            List<List<String>> userGroups = getInfoDatabase.getInfoListListString(user,"groups");

            String toUsername = "";
            for (int i = 0; i < userGroups.size(); i++) {
                String id = userGroups.get(i).get(0);
                String admin = userGroups.get(i).get(1);
                if (admin.equals(username)) {
                    for (Document users : usersCollection.find()) {
                        String Username = users.getString("username");
                        List<List<String>> groups =
                                getInfoDatabase.getInfoListListString(users,"groups");
                        for (int j = 0; j < groups.size(); j++) {
                            if (groups.get(j).get(0).equals(id)) {
                                groups.remove(j);
                                break;
                            }
                        }
                        Bson filer = eq("username", Username);
                        usersCollection.findOneAndUpdate(filer, set("groups", groups));
                    }
                }

            }


            for (Document users : usersCollection.find()) {

                String Username = users.getString("username");

                List<String> followers = getInfoDatabase.getInfoStringList(users,"followers");
                List<String> followings = getInfoDatabase.getInfoStringList(users,"followings");
                List<String> mutes = getInfoDatabase.getInfoStringList(users,"mutes");
                List<String> blocks = getInfoDatabase.getInfoStringList(users,"blocks");

                List<List<String>> categories = getInfoDatabase.getInfoListListString(users,"categoiries");

                followers.remove(username);
                followings.remove(username);
                mutes.remove(username);
                blocks.remove(username);

                for (int i = categories.size() - 1; i >= 0; i--) {
                    for (int j = 0; j < categories.get(i).size(); j++) {
                       if(categories.get(i).toString().equals(username))
                           categories.get(i).remove(j);
                       break;
                    }
                }

                Bson filer = eq("username",Username);


                usersCollection.findOneAndUpdate(filer, set("followers", followers));
                usersCollection.findOneAndUpdate(filer, set("followings", followings));
                usersCollection.findOneAndUpdate(filer, set("mutes", mutes));
                usersCollection.findOneAndUpdate(filer, set("blocks", blocks));
                usersCollection.findOneAndUpdate(filer, set("categoiries", categories));

            }

            Bson filter1 = eq("sender", username);
            Bson filter2 = eq("reporter", username);
            Bson filter3 = eq("user1", username);
            Bson filter4 = eq("user2", username);
            Bson filter5 = eq("sender", username);
            Bson filter6 = eq("receiver", username);
            Bson filter7 = eq("username", username);

            twittesCollection.deleteMany(filter1);
            reportCollection.deleteMany(filter1);
            reportCollection.deleteMany(filter2);
            notifsCollection.deleteMany(filter3);
            notifsCollection.deleteMany(filter4);
            messagesCollection.deleteMany(filter5);
            messagesCollection.deleteMany(filter6);
            usersCollection.deleteMany(filter7);

            return 1;
        } catch (Exception e) {
            logger.error("error in database collection");
            e.printStackTrace();
            return 0;
        }
    }
}

