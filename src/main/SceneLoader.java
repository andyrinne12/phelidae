package main;


import actors.Player;
import animation.animatedModel.AnimatedModel;
import animation.animation.Animation;
import animation.loaders.AnimatedModelLoader;
import animation.loaders.AnimationLoader;
import engine.scene.ICamera;
import engine.scene.Light;
import engine.scene.Scene;
import engine.terrain.Terrain;
import engine.terrain.WorldTerrain;
import engine.utils.MyFile;
import org.lwjgl.util.vector.Vector3f;

public class SceneLoader {

  /**
   * Sets up the scene. Loads the entity, load the animation, tells the entity to do the animation,
   * sets the light direction, creates the camera, etc...
   *
   * @param resFolder - the folder containing all the information about the animated entity (mesh,
   *                  animation, and texture info).
   * @return The entire scene.
   */
  public static Scene loadScene(MyFile resFolder) {
    ICamera camera = new Camera();

    WorldTerrain worldTerrain = new WorldTerrain(2, 2);

    Scene scene = new Scene(worldTerrain, camera);
    scene.setLightDirection(new Vector3f(500, 500, 500));

    Vector3f whiteLight = new Vector3f(1,1,1);
    Light light = new Light(new Vector3f(500, 500, 500), whiteLight);
    Light light2 = new Light(new Vector3f(-500, 500, 500), whiteLight);
    Light light3 = new Light(new Vector3f(500, 500, -500), whiteLight);
    Light light4 = new Light(new Vector3f(-500, 500, -500), whiteLight);

    scene.addLight(light);
    scene.addLight(light2);
    scene.addLight(light3);
    scene.addLight(light4);

    Terrain terrain = new Terrain(0, 0, "heightmap");
    worldTerrain.setParcel(terrain, 0, 0);
    Terrain terrain2 = new Terrain(0, 1, "heightmap");
    worldTerrain.setParcel(terrain2, 0, 1);
    Terrain terrain3 = new Terrain(1, 0, "heightmap");
    worldTerrain.setParcel(terrain3, 1, 0);

    AnimatedModel animatedModel = AnimatedModelLoader
        .loadEntity(new MyFile(resFolder, GeneralSettings.MODEL_FILE),
            new MyFile(resFolder, GeneralSettings.DIFFUSE_FILE));
    Animation animation = AnimationLoader
        .loadAnimation(new MyFile(resFolder, GeneralSettings.ANIM_FILE));
    animatedModel.doAnimation(animation);

    Player player = new Player(animatedModel, new Vector3f(3f, 5f, 3f), new Vector3f(0, 0, 0), 0.5f,
        scene);
    scene.setPlayer(player);

    return scene;
  }



}
