package com.java.profile;

import com.java.setting.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

public class sortProfile {
    setTwitteImage setTwitteImage = new setTwitteImage();
    JSONObject jsonResult = new JSONObject();
    setProfilePic setProfilePic = new setProfilePic();

    private static final Logger logger = LogManager.getLogger(sortProfile.class);
    public JSONObject sort(JSONObject settingJson) throws IOException {


        switch (settingJson.getString("type")) {
            case "profile" -> jsonResult = setProfilePic.set(
                    Integer.parseInt(settingJson.getString("AuthKey")),
                    settingJson.getString("picture"));

            case "twitte" -> jsonResult = setTwitteImage.set(
                    Integer.parseInt(settingJson.getString("AuthKey")),
                    settingJson.getString("picture"));
        }

        return jsonResult;
    }
}
