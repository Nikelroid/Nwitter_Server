package com.java.database;

import javax.swing.text.Document;
import java.util.List;
import java.util.List;

public class getInfoDatabase {
    public int getInfoInteger(org.bson.Document document, String value) {
        return Integer.parseInt(document.get(value).toString());
    }
    public String getInfoString(org.bson.Document document,String value) {
        return document.get(value).toString();
    }
    public List<String> getInfoStringList(org.bson.Document document,String value) {
        return (document.getList(value, String.class));
    }
    public List<Integer> getInfoIntegerList(org.bson.Document document,String value) {
        return (document.getList(value, Integer.class));
    }
    public List<List<String>> getInfoListListString(org.bson.Document document, String value) {
        return (List<List<String>>) document.get(value);
    }
    public List<List<Integer>> getInfoListListInteger(org.bson.Document document, String value) {
        return (List<List<Integer>>) document.get(value);
    }

}
