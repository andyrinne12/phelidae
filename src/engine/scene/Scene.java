package engine.scene;

import actors.Player;
import engine.terrain.WorldTerrain;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import shapes.Shape;


/**
 * Represents all the stuff in the scene (just the camera, light, and model really).
 *
 * @author Karl
 */
public class Scene {

  private final ICamera camera;
  private final WorldTerrain terrains;
  private final List<Light> lights = new ArrayList<>();
  private final List<Shape> shapeList = new ArrayList<>();
  private Vector3f lightDirection;
  private Player player;

  public Scene(WorldTerrain terrains, ICamera cam) {
    this.terrains = terrains;
    this.camera = cam;
  }

  /**
   * @return The scene's camera.
   */
  public ICamera getCamera() {
    return camera;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public WorldTerrain getTerrains() {
    return terrains;
  }

  public List<Light> getLights() {
    return lights;
  }

  public void addLight(Light light) {
    lights.add(light);
  }

  public Vector3f getLightDirection() {
    return lightDirection;
  }

  public void setLightDirection(Vector3f lightDirection) {
    this.lightDirection = lightDirection;
  }

  public List<Shape> getShapeList() {
    return shapeList;
  }

  public void addShape(Shape shape) {
    shapeList.add(shape);
  }

  public void removeShape(Shape shape) {
    shapeList.remove(shape);
  }
}
