package com.java.operation;

public class saving {
    saveDatabase saveDatabase = new saveDatabase();
    public int set(String AuthKey, String serial) {
        return saveDatabase.setter(AuthKey,serial);
    }
    public saving() { }
}
