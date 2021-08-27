package com.java.database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class getDocument {

    public static Document get(MongoCollection<Document> collection, String field, String value) {
        return collection.find(new Document(field, value)).first();
    }
    public Document get(MongoCollection<Document> collection,String field,int value) {
        return collection.find(new Document(field, value)).first();
    }
}
