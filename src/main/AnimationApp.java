package main;

import audio.MusicPlayer;
import engine.renderEngine.RenderEngine;
import engine.scene.Scene;
import engine.utils.DisplayManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;


public class AnimationApp {

  /**
   * Initialises the engine and loads the scene. For every frame it updates the camera, updates the
   * animated entity (which updates the animation), renders the scene to the screen, and then
   * updates the display. When the display is close the engine gets cleaned up.
   *
   * @param args
   */
  public static void main(String[] args) {

    RenderEngine engine = RenderEngine.init();

    Scene scene = SceneLoader.loadScene(GeneralSettings.RES_FOLDER);

    MusicPlayer music = new MusicPlayer();

    while (!Display.isCloseRequested()) {
      float delta = DisplayManager.getFrameTime();

      while (Keyboard.next()) {
        if (Keyboard.getEventKey() == Keyboard.KEY_1) {
          if (Keyboard.getEventKeyState()) {
            music.nextSong();
          }
        } else if (Keyboard.getEventKey() == Keyboard.KEY_2) {
          if (Keyboard.getEventKeyState()) {
            music.stopPlayer();
          }
        }
      }

      scene.getPlayer().update(delta, scene);

      scene.getCamera().move(scene);
      engine.renderScene(scene);
      engine.update();
    }

    music.stopPlayer();
    engine.close();

  }

}
