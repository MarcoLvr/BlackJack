package me.marcolvr.server.logger;

import me.marcolvr.logger.Logger;

public class LogMessages {

    public static void roomCreated(String id){
        Logger.info("[" + id + "] Room created.");
    }

    public static void playerJoined(String id, String name){
        Logger.info("[" + id + "] " + name +" joined the room.");
    }

    public static void playerFailedToJoin(String id, String name){
        Logger.info("[" + id + "] " + name +" tried to join room but at the moment it isn't accessible.");
    }

    public static void playerLeft(String id, String name){
        Logger.info("[" + id + "] " + name +" left the room.");
    }

    public static void roomEmpty(String id){
        Logger.info("[" + id + "] Room empty. Deleting it");
    }
}
