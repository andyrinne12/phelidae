package main;

import engine.utils.MyFile;
import org.lwjgl.util.vector.Vector3f;

/**
 * Just some configs. File locations mostly.
 * 
 * @author Karl
 *
 */
public class GeneralSettings {
	
	public static final MyFile RES_FOLDER = new MyFile("res");
	public static final String MODEL_FILE = "cat_anim4.dae";
	public static final String ANIM_FILE = "cat_anim4.dae";
	public static final String DIFFUSE_FILE = "diffuse.png";
	
	public static final int MAX_WEIGHTS = 4;
	
	public static final Vector3f LIGHT_DIR = new Vector3f(2f, 500f, 2000f);
	
}
