package core.render;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TileIndex {

	public static HashMap<String, Model2D> tiles = new HashMap<String, Model2D>();
	public static HashMap<String, ArrayList<Point2D>> tilePos = new HashMap<String, ArrayList<Point2D>>();
	
	public static void addTile(String tileName, Point2D pos) {
		if(tiles.get(tileName) == null) {
			tiles.put(tileName, new Model2D(new String[]{tileName}));
		}
		
		if(tilePos.get(tileName) != null) {
			tilePos.get(tileName).add(pos);
		} else {
			tilePos.put(tileName, new ArrayList<Point2D>(Arrays.asList(pos)));
		}
	}
	
	public static void draw() {
		for(Model2D m : tiles.values()) {
			for(int x = 0; x<tilePos.get(m.textureNames[0]).size(); x++) {
				m.drawAt((float) tilePos.get(m.textureNames[0]).get(x).getX(), (float) tilePos.get(m.textureNames[0]).get(x).getY());
			}
		}
	}
	
}
