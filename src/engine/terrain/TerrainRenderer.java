package engine.terrain;

import engine.scene.ICamera;
import engine.utils.OpenGlUtils;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import animation.animatedModel.AnimatedModel;

import java.util.List;


/**
 * This class deals with rendering terrains. Nothing particularly new
 * here
 *
 * @author Andy Solo
 */
public class TerrainRenderer {

    private TerrainShader shader;

    /**
     * Initializes the shader program used for rendering terrains
     */
    public TerrainRenderer() {
        this.shader = new TerrainShader();
    }

    /**
     * Renders a terrain list. The main thing to note here is that all the
     * joint transforms are loaded up to the shader to a uniform array. Also 5
     * attributes of the VAO are enabled before rendering, to include joint
     * indices and weights.
     *
     * @param terrains - the terrain list to be rendered
     * @param camera   - the camera used to render the entity.
     * @param lightDir - the direction of the light in the scene.
     */
    public void render(WorldTerrain terrains, ICamera camera, Vector3f lightDir) {
        prepare(camera, lightDir);
        //    entity.getTexture().bindToUnit(0);
        for (int x = 0; x < terrains.getWidth(); x++) {
            for (int z = 0; z < terrains.getLength(); z++) {
                Terrain terrain = terrains.getTerrainMap()[x][z];
                if(terrain == null){
                    continue;
                }
                terrain.getModel().bind(0, 1, 2);
                shader.transformationMatrix.loadMatrix(terrain.getTransformationMatrix());
                GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
                terrain.getModel().unbind(0, 1, 2);
            }
        }
        finish();
    }

    /**
     * Deletes the shader program when the game closes.
     */
    public void cleanUp() {
        shader.cleanUp();
    }

    /**
     * Starts the shader program and loads up the projection view matrix, as
     * well as the light direction. Enables and disables a few settings which
     * should be pretty self-explanatory.
     *
     * @param camera   - the camera being used.
     * @param lightDir - the direction of the light in the scene.
     */
    private void prepare(ICamera camera, Vector3f lightDir) {
        shader.start();
        shader.projectionViewMatrix.loadMatrix(camera.getProjectionViewMatrix());
        shader.lightDirection.loadVec3(lightDir);
        shader.reflectivity.loadFloat(0.0f);
        shader.shineDamper.loadFloat(0.002f);
        OpenGlUtils.antialias(true);
        OpenGlUtils.disableBlending();
        OpenGlUtils.enableDepthTesting(true);
    }

    /**
     * Stops the shader program after rendering the entity.
     */
    private void finish() {
        shader.stop();
    }

}
