package engine.renderEngine;

import animation.renderer.AnimatedModelRenderer;
import engine.scene.Scene;
import engine.skybox.SkyboxRenderer;
import engine.terrain.TerrainRenderer;
import engine.utils.DisplayManager;
import shapes.ShapeRenderer;

/**
 * This class represents the entire render engine.
 * 
 * @author Karl
 *
 */
public class RenderEngine {

	private MasterRenderer renderer;

	private RenderEngine(MasterRenderer renderer) {
		this.renderer = renderer;
	}

	/**
	 * Updates the display.
	 */
	public void update() {
		DisplayManager.update();
	}

	/**
	 * Renders the scene to the screen.
	 * 
	 * @param scene
	 *            - the game scene.
	 */
	public void renderScene(Scene scene) {
		renderer.renderScene(scene);
	}

	/**
	 * Cleans up the renderers and closes the display.
	 */
	public void close() {
		renderer.cleanUp();
		DisplayManager.closeDisplay();
	}

	/**
	 * Initializes a new render engine. Creates the display and inits the
	 * renderers.
	 * 
	 * @return
	 */
	public static RenderEngine init() {
		DisplayManager.createDisplay();
		SkyboxRenderer skyRenderer = new SkyboxRenderer();
		AnimatedModelRenderer entityRenderer = new AnimatedModelRenderer();
		TerrainRenderer terrainRenderer = new TerrainRenderer();
		ShapeRenderer shapeRenderer = new ShapeRenderer();
		MasterRenderer renderer = new MasterRenderer(entityRenderer, skyRenderer,shapeRenderer,terrainRenderer);
		return new RenderEngine(renderer);
	}

}
