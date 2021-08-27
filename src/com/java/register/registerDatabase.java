package com.java.register;

import com.java.database.databaseControl;
import com.java.database.getDocument;
import com.java.operation.operateCfg;
import com.mongodb.client.MongoCollection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class registerDatabase {

    ArrayList<String> followers ;
    ArrayList<String> followings ;
    ArrayList<String> mutes ;
    ArrayList<String> blocks ;
    ArrayList<Integer> twittesaved ;
    ArrayList<Integer> pmsaved ;
    ArrayList<Integer> privacy ;
    ArrayList<ArrayList<String>> groups ;
    ArrayList<ArrayList<String>> categories ;

    private static final Logger logger = LogManager.getLogger(registerDatabase.class);
    databaseControl dataBaseControl = new databaseControl();
    String collection;

    private void valueSetter(){
        followers = new ArrayList<>(Collections.singleton("Followers:"));
        followings = new ArrayList<>(Collections.singleton("Followings:"));
        mutes = new ArrayList<>(Collections.singleton("Mutes:"));
        blocks = new ArrayList<>(Collections.singleton("Block:"));
        twittesaved = new ArrayList<>(Collections.singleton(1));
        pmsaved = new ArrayList<>(Collections.singleton(1));
        privacy = new ArrayList<>();
        privacy.add(2);
        for (int i = 0; i < 3; i++)
            privacy.add(1);
        groups = new ArrayList<>();
        categories = new ArrayList<>();
    }
    private int idFinder(MongoCollection<Document> usersCollection){
        int ID = 0;
        for (Document user:usersCollection.find()) {
            ID = user.getInteger("_id");
        }

        ID++;
        return ID;
    }

    public int database(String name,
                        String username,
                        String password,
                        String birthday,
                        String email,
                        String phonenumber,
                        String bio) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        try {
            new registerCfg();
        } catch (IOException e) {
            logger.error("couldn't open config of login");
        }
        if(registerCfg.collection==null){
            collection = "Users";
        }else{
            collection = registerCfg.collection;
        }

        MongoCollection<Document> usersCollection = dataBaseControl.getCollection(collection);

        valueSetter();
        int ID = idFinder(usersCollection);


        Document newUser = new Document("_id", ID);
        newUser.append("name", name).
                append("username", username).
                append("password", password).
                append("birthday", birthday).
                append("email", email).
                append("phonenumber", phonenumber).
                append("bio", bio).
                append("lastseen", dtf.format(now)).
                append("account", false).
                append("enable", true).
                append("followers",followers).
                append("followings",followings).
                append("mutes",mutes).
                append("blocks",blocks).
                append("categoiries",categories).
                append("groups",groups).
                append("privacy",privacy).
                append("twittesaved",twittesaved).
                append("pmsaved",pmsaved).
                append("AuthKey",0);

        usersCollection.insertOne(newUser);
        return 1;
        }

    }

