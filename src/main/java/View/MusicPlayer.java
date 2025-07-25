package View;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MusicPlayer {
    private static Clip clip;

    public static void playBackgroundMusic() {
        //System.out.println("playBackgroundMusic() called");

        if (clip != null && clip.isRunning()) {
            System.out.println("Clip already running");
            return;
        }

        try {
            //System.out.println("Trying to load /music/background.wav as resource...");
            InputStream audioSrc = MusicPlayer.class.getResourceAsStream("/music/background.wav");
           // System.out.println(audioSrc == null ? "Resource NOT found!" : "Resource found!");

            if (audioSrc == null) {
                return;
            }

            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            System.out.println("Music started playing.");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


//    public static void stopMusic() {
//        if (clip != null && clip.isRunning()) {
//            clip.stop();
//            clip.close();
//            System.out.println("Music stopped.");
//        }
//    }
}
