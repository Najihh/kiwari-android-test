package com.senjacreative.kiwarichat.Utils;

public class Private {
    static String privateWith = "";
    static String baseUri = "https://kiwari-android.firebaseio.com/";

    public static String getPrivateWith() {
        return privateWith;
    }

    public static void setPrivateWith(String privateWith) {
        Private.privateWith = privateWith;
    }

    public static String getBaseUri() {
        return baseUri;
    }
}
