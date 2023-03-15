package me.marcolvr.logger;

import java.time.LocalDateTime;

/*public static final String ANSI_RESET = "\u001B[0m";
public static final String ANSI_BLACK = "\u001B[30m";
public static final String ANSI_RED = "\u001B[31m";
public static final String ANSI_GREEN = "\u001B[32m";
public static final String ANSI_YELLOW = "\u001B[33m";
public static final String ANSI_BLUE = "\u001B[34m";
public static final String ANSI_PURPLE = "\u001B[35m";
public static final String ANSI_CYAN = "\u001B[36m";
public static final String ANSI_WHITE = "\u001B[37m";*/

public class Logger {

    public static void info(String msg){
        log("\u001B[36mINFO", msg);
    }

    public static void warn(String msg){
        log("\u001B[33mWARN", msg);
    }

    public static void err(String msg){
        log("\u001B[31mERR", msg);
    }

    private static void log(String level, String msg){
        StringBuilder builder = new StringBuilder();
        LocalDateTime dateTime = LocalDateTime.now();
        builder/*.append(dateTime.getDayOfMonth())
                .append("-")
                .append(dateTime.getMonthValue())
                .append("-")
                .append(dateTime.getYear())
                .append(" ")*/
                .append("[")
                .append(fixVal(dateTime.getHour()))
                .append(":")
                .append(fixVal(dateTime.getMinute()))
                .append(":")
                .append(fixVal(dateTime.getSecond()))
                .append(" ")
                .append(level)
                .append("\u001B[0m] ")
                .append(msg);
        System.out.println(builder);
    }

    private static String fixVal(int val){
        return val < 10 ? "0"+val : String.valueOf(val);
    }

}
