package core.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map {

	private String tileSet;
	private CollisionLayer collision;
	private ArrayList<Layer> background = new ArrayList<Layer>();
	private ArrayList<Layer> foreground = new ArrayList<Layer>();

	public Map(String ref) {
		try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/scenes/" + ref))) {
			String line;
			while((line = reader.readLine()) != null) {
				if(line.contains("tilesets")) {
					tileSet = (line = reader.readLine().split("=")[1]).substring(line.lastIndexOf("/") + 1, line.indexOf(".png"));
					System.out.println(tileSet);
				} else if(line.contains("layer")) {
					line = reader.readLine();
					switch(line.split("=")[1]) {
					case "Collision":
						collision = new CollisionLayer(tileSet, reader);
						break;
					case "Background":
						background.add(new Layer(tileSet, reader));
						break;
					case "Foreground":
						foreground.add(new Layer(tileSet, reader));
						break;
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("The scene file: " + ref + " has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Scene file: " + ref + " failed to load!");
			e.printStackTrace();
		}
	}
	
	public CollisionLayer getCollision() {
		return collision;
	}
	
	public ArrayList<Layer> getBackground() {
		return background;
	}
	
	public Layer getBackgroundLayer(int layer) {
		return background.get(layer);
	}
	
	public ArrayList<Layer> getForeground() {
		return foreground;
	}
	
	public Layer getForegroundLayer(int layer) {
		return foreground.get(layer);
	}

}
