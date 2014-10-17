package core.render;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureList {

	public static HashMap<String, Texture> table = new HashMap<String, Texture>();
	public static HashMap<String, Model2D> models = new HashMap<String, Model2D>();
	
	public static Texture loadTexture(String ref) {
		Texture tex = table.get(ref);

		if (tex != null) {
			return tex;
		}
		
		try {
			tex = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream(System.getProperty("resources") + "/textures/" + ref));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		table.put(ref, tex);
		
		return table.get(ref);
	}
	
	public static Model2D loadModel(String ref) {
		Model2D model = models.get(ref);

		if (model != null) {
			return model;
		}
		
		model = new Model2D(new String[]{ref});
		
		models.put(ref, model);
		
		return models.get(ref);
	}
	
}
