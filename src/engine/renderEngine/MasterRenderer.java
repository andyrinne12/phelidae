package engine.renderEngine;

import animation.renderer.AnimatedModelRenderer;
import engine.scene.Scene;
import engine.skybox.SkyboxRenderer;
import engine.terrain.TerrainRenderer;
import org.lwjgl.opengl.GL11;
import shapes.ShapeRenderer;

/**
 * This class is in charge of rendering everything in the scene to the screen.
 *
 * @author Karl
 */
public class MasterRenderer {

  private SkyboxRenderer skyRenderer;
  private AnimatedModelRenderer entityRenderer;
  private TerrainRenderer terrainRenderer;
  private ShapeRenderer shapeRenderer;

  protected MasterRenderer(AnimatedModelRenderer renderer, SkyboxRenderer skyRenderer,
      ShapeRenderer shapeRenderer, TerrainRenderer terrainRenderer) {
    this.skyRenderer = skyRenderer;
    this.entityRenderer = renderer;
    this.terrainRenderer = terrainRenderer;
    this.shapeRenderer = shapeRenderer;
  }

  /**
   * Renders the scene to the screen.
   *
   * @param scene
   */
  protected void renderScene(Scene scene) {
    prepare();
    entityRenderer.render(scene.getPlayer(), scene.getCamera(), scene.getLights(), scene.getLightDirection());
    shapeRenderer.render(scene.getShapeList(), scene.getCamera(), scene.getLightDirection());
    //	skyRenderer.render(scene.getCamera());
    terrainRenderer.render(scene.getTerrains(), scene.getCamera(), scene.getLightDirection());
  }

  /**
   * Clean up when the game is closed.
   */
  protected void cleanUp() {
    skyRenderer.cleanUp();
    entityRenderer.cleanUp();
    terrainRenderer.cleanUp();
  }

  /**
   * Prepare to render the current frame by clearing the framebuffer.
   */
  private void prepare() {
    GL11.glClearColor(1, 1, 1, 1);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
  }


}
