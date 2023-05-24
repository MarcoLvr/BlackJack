package me.marcolvr.client.audio;

import javazoom.jl.player.Player;
import me.marcolvr.client.BlackJackClient;

import javax.print.attribute.standard.Media;
import java.io.BufferedInputStream;
import java.io.File;

public class AudioPlayer {

    public static void pling(){
        new Thread(()->{
            try{
                BufferedInputStream buffer = new BufferedInputStream(
                        BlackJackClient.class.getClassLoader().getResourceAsStream("pling.mp3"));
                Player player = new Player(buffer);
                player.play();
            }catch (Exception e){
            }
        }).start();


    }
}
