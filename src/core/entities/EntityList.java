package core.entities;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class EntityList {

	private HashMap<String, ArrayList<Point2D>> tiles = new HashMap<String, ArrayList<Point2D>>();
	private HashMap<String, Entity> tileEntities = new HashMap<String, Entity>();
	
	public void addTile(String tileName, Point2D loc) {
		if(tiles.containsKey(tileName)) {
			tiles.get(tileName).add(loc);
		} else {
			tiles.put(tileName, new ArrayList<Point2D>(Arrays.asList(loc)));
			loadTile(tileName);
		}
	}
	
	public HashMap<String, ArrayList<Point2D>> getTileMap() {
		return tiles;
	}
	
	public ArrayList<Point2D> getTiles(String tile) {
		return tiles.get(tile);
	}
	
	public void loadTile(String tileName) {
		tileEntities.put(tileName, new Entity(0, 0, tileName));
	}
	
	public Entity getTile(String tileName) {
		return tileEntities.get(tileName);
	}
	
}
