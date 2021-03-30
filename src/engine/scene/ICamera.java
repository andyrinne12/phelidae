package engine.scene;

import actors.Player;
import org.lwjgl.util.vector.Matrix4f;

public interface ICamera {
	
	public Matrix4f getViewMatrix();
	public Matrix4f getProjectionMatrix();
	public Matrix4f getProjectionViewMatrix();
	public void move(Scene scene);
	public void focusPlayer(Scene scene);

}
