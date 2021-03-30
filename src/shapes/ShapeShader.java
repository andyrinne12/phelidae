package shapes;


import animation.renderer.AnimatedModelRenderer;
import engine.shaders.*;
import engine.utils.MyFile;

public class ShapeShader extends ShaderProgram {

	private static final int MAX_JOINTS = 50;// max number of joints in a skeleton
	private static final int DIFFUSE_TEX_UNIT = 0;

	private static final MyFile VERTEX_SHADER = new MyFile("src/shapes", "shapeVertex.glsl");
	private static final MyFile FRAGMENT_SHADER = new MyFile("src/shapes", "shapeFragment.glsl");

	protected UniformMatrix projectionViewMatrix = new UniformMatrix("projectionViewMatrix");
	protected UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	protected UniformVec3 lightDirection = new UniformVec3("lightDirection");
	protected UniformVec4 shapeColour = new UniformVec4("shapeColour");

	/**
	 * Creates the shader program for the {@link AnimatedModelRenderer} by
	 * loading up the vertex and fragment shader code files. It also gets the
	 * location of all the specified uniform variables, and also indicates that
	 * the diffuse texture will be sampled from texture unit 0.
	 */
	public ShapeShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "in_position", "in_textureCoords", "in_normal", "in_jointIndices",
				"in_weights");
		super.storeAllUniformLocations(projectionViewMatrix, transformationMatrix, lightDirection,shapeColour);
		connectTextureUnits();
	}

	/**
	 * Indicates which texture unit the diffuse texture should be sampled from.
	 */
	private void connectTextureUnits() {
		super.start();
		super.stop();
	}

}
