package me.marcolvr.server.logger;

import me.marcolvr.logger.Logger;

public class LogMessages {

    public static void listening(int port){
        Logger.info("Listening on " + port);
    }

    public static void sockError(String message){
        Logger.err("Error trying to open server socket: " + message);
    }

    public static void serverStarted(){
        Logger.info("BlackJack Server started!");
    }

    public static void loggedIn(String address, String username){
        Logger.info(address + " logged in as " + username);
    }

    public static void disconnected(String who, String reason){
        Logger.info(who + " disconnected. Reason: " + reason);
    }

    public static void newConnection(String ip){
        Logger.info(ip + " connected");
    }
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
