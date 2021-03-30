package engine.physics;

import actors.AnimatedEntity;
import actors.Player;
import animation.animation.Quaternion;
import engine.scene.Scene;
import engine.terrain.Terrain;
import engine.utils.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shapes.Shape;

public class TerrainContact {

    private final float MARKER_SCALE = 0.03f;

    private Vector3f entityOffset;
    private Shape marker;

    private Vector3f position = new Vector3f(0,0,0);

    public TerrainContact(Vector3f entityOffset){
        this.entityOffset = entityOffset;
        this.marker = new Shape(MARKER_SCALE);
    }


    public void update(AnimatedEntity entity) {
        Vector3f playerPos = entity.getPosition();
        Vector3f playerRot = entity.getRotation();
        float playerScale = entity.getScale();

        Vector4f finalDelta = new Vector4f(entityOffset.x, entityOffset.y, entityOffset.z, 1);
        Matrix4f mat = Maths.createTransformationMatrix(new Vector3f(0,0,0),new Quaternion(playerRot),playerScale);
        Matrix4f.transform(mat,finalDelta,finalDelta);
        position = Vector3f.add(playerPos, new Vector3f(finalDelta.x,finalDelta.y,finalDelta.z), null);
        marker.setPosition(position);
        marker.setRotation(playerRot);
    }

    public float getTerrainHeight(Scene scene){
        Terrain terrain = scene.getTerrains().getParcel(position.x, position.z);
        if(terrain == null){
            return 0;
        }
        return terrain.getHeightOfTerrain(position.x,position.z);
    }

    public void showMarker(Scene scene){
        scene.addShape(marker);
    }

    public void removeMarker(Scene scene){
        scene.removeShape(marker);
    }

    public Vector3f getPosition() {
        return position;
    }
}
