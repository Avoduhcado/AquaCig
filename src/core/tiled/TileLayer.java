package core.tiled;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import core.entities.Entity;
import core.render.TileIndex;
import core.world.TileSet;

public class TileLayer {

	private ArrayList<Entity> tiles = new ArrayList<Entity>();
	
	public TileLayer(BufferedReader reader, String tileSet, Point2D offset) {
		String line;
		int y = 0;
		try {
			while((line = reader.readLine()) != null && !line.contains("/layer")) {
				if(!line.contains("data")) {
					String[] temp = line.split(",");
					for(int x = 0; x<temp.length; x++) {
						if(Integer.parseInt(temp[x]) != 0 && Integer.parseInt(temp[x]) != 1) {
							/*tiles.add(new Entity((x * TileSet.tileWidth) + (float) offset.getX(),
									(y * TileSet.tileHeight) + (float) offset.getY(),
									tileSet + (Integer.parseInt(temp[x]) - 1) + ".png"));*/
							TileIndex.addTile(tileSet + (Integer.parseInt(temp[x]) - 1) + ".png", 
									new Point2D.Double((x * TileSet.tileWidth) + (float) offset.getX(),
									(y * TileSet.tileHeight) + (float) offset.getY()));
						}
					}
					y++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Entity> getTiles() {
		return tiles;
	}
	
}
