package com.java.user;

import com.java.profile.getProfilePic;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class userProfileGetter {
    JSONObject result = new JSONObject();
    ArrayList<String> serialList = new ArrayList<>();
    private boolean self = false;
    com.java.profile.getProfilePic getProfilePic = new getProfilePic();
    private static final Logger logger = LogManager.getLogger(userProfileGetter.class);
    public JSONObject get(int myAuthKey , String username) {

        serialList = new ArrayList<>();
        try {
            new editCfg();
        } catch (IOException e) {
            logger.error("couldn't open config of login");
        }


        try {
            MongoClient mongoClient = MongoClients.create();
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase(editCfg.database);
            MongoCollection<Document> userCollection = sampleTrainingDB.getCollection(
                    editCfg.usersCollection);
            MongoCollection<Document> notifsCollection = sampleTrainingDB.getCollection(editCfg.
                    notifsCollection);

            Document me = userCollection.find(new Document("AuthKey", myAuthKey)).first();
            if(username.equals("1")){
                username=me.get("username").toString();
                self=true;
            }
            Document user = userCollection.find(new Document("username", username)).first();
            if (user==null){
                user = userCollection.find(new Document("name", username)).first();
                if (user==null)
                result.put("username","-");
            }

            if(user==null || me==null){
                logger.error("error in connection with database");
            }else{

                List<String> followers = user.getList("followers",String.class);
                List<String> followings = user.getList("followings",String.class);
                List<String> blocks = user.getList("blocks",String.class);
                List<String> mutes = user.getList("mutes",String.class);
                List<Integer> privacy = user.getList("privacy",Integer.class);

                List<String> myFollowings = me.getList("followings",String.class);
                List<String> myBlocks = me.getList("blocks",String.class);
                List<String> myMutes = me.getList("mutes",String.class);
                String myUsername = me.getString("username");

                boolean followedBy = followings.contains(myUsername);

                result.put("username",username)
                        .put("name",user.getString("name"))
                        .put("bio",user.getString("bio"))
                        .put("followers",followers.size()-1)
                        .put("followings",followings.size()-1)
                        .put("blocks",blocks.size()-1)
                        .put("mutes",mutes.size()-1)
                        .put("account",user.getBoolean("account"))
                        .put("enable",user.getBoolean("enable"))
                        .put("isFollowing",myFollowings.contains(username))
                        .put("isMuted",myMutes.contains(username))
                        .put("isBlocked",myBlocks.contains(username))
                        .put("followedBy",followings.contains(myUsername))
                        .put("mutedBy",mutes.contains(myUsername))
                        .put("blockedBy",blocks.contains(myUsername))
                        .put("AuthKey",user.get("AuthKey").toString())
                        .put("pic", getProfilePic.get(username));

                if (privacy.get(0)==1 || (privacy.get(0)==2 && followedBy) || self)
                    result.put("lastseen",user.get("lastseen").toString());
                    else
                    result.put("lastseen","recently");

                if ((privacy.get(1)==1 || (privacy.get(1)==2 && followedBy) &&
                        !user.get("birthday").toString().equals("//"))||self)
                    result.put("birthday",user.get("birthday").toString());
                else
                    result.put("birthday"," ");

                if (privacy.get(2)==1 || (privacy.get(2)==2 && followedBy)||self)
                    result.put("email",user.get("email").toString());
                else
                    result.put("email"," ");

                if (privacy.get(3)==1 || (privacy.get(3)==2 && followedBy)||self)
                    result.put("phonenumber",user.get("phonenumber").toString());
                else
                    result.put("phonenumber"," ");

                Document myReq = new Document();
                myReq.append("type", 8).append("user1", me.get("username")).append("user2", user.get("username"));
                Document request = notifsCollection.find(myReq).first();
                result.put("requested", request != null);

            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error("error in opening database");
        }
        return result;
    }
}
