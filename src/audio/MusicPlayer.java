package audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MusicPlayer implements Runnable {

  private final static File MUSIC_FOLDER = new File("res/music");
  private final static List<File> songs = Arrays.asList(MUSIC_FOLDER.listFiles());
  private final int songNum = songs.size();

  private Player player = null;
  private int songIndex = 0;
  private Thread playerThread;

  public MusicPlayer() {
  }

  private void startPlayerThread() {
    stopPlayer();
    try {
      player = new Player(new FileInputStream(songs.get(songIndex)));
    } catch (JavaLayerException | FileNotFoundException e) {
      e.printStackTrace();
    }
    playerThread = new Thread(this);
    playerThread.start();
  }

  public void stopPlayer() {
    if (this.player != null) {
      this.player.close();
      this.player = null;
    }
    this.playerThread = null;
  }

  public void nextSong() {
    songIndex++;
    songIndex %= songNum;
    startPlayerThread();
  }

  @Override

  public void run() {
    if (player != null) {
      try {
        this.player.play();
      } catch (JavaLayerException e) {
        e.printStackTrace();
      }
    }
  }

}
