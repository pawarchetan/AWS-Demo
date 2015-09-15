package com.lendbox.processor;

public class S3Config {
    public static String myAccessId;
    public static String mySecretId;

    public static final String getMyAccessId() {
        myAccessId = ""; //Use your access key
        return myAccessId;
    }

    public static final String getMySecretId() {
        mySecretId = ""; //use your secrete key
        return mySecretId;
    }
}
