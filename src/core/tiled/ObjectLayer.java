package core.tiled;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import core.world.TileSet;

public class ObjectLayer {

	private ArrayList<Platform> platforms = new ArrayList<Platform>();
	
	public ObjectLayer(BufferedReader reader) {
		loadObjects(reader, 0, 0);
	}
	
	public ObjectLayer(BufferedReader reader, float xOffset, float yOffset) {
		loadObjects(reader, xOffset, yOffset);
	}
	
	public void loadObjects(BufferedReader reader, float xOffset, float yOffset) {
		String line;
		String[] temp;
		try {
			while((line = reader.readLine()) != null && !line.contains("/objectgroup")) {
				line = line.replaceAll("<|>|/>", "").trim();
				line = line.replace("\"", "");
				//System.out.println(line);
				
				if(line.startsWith("object")) {
					temp = line.split(" ");
					String name = null;
					String type = null;
					Rectangle2D rect = null;
					for(int i = 0; i<temp.length; i++) {
						if(temp[i].startsWith("name")) {
							name = temp[i].split("=")[1];
						} else if(temp[i].startsWith("type")) {
							type = temp[i].split("=")[1];
						} else if(temp[i].startsWith("x")) {
							rect = new Rectangle2D.Double(Integer.parseInt(temp[i].split("=")[1]) + xOffset, 0, 1, 1);
						} else if(temp[i].startsWith("y")) {
							rect = new Rectangle2D.Double(rect.getX(), Integer.parseInt(temp[i].split("=")[1]) + yOffset, 1, 1);
						} else if(temp[i].startsWith("width")) {
							rect = new Rectangle2D.Double(rect.getX(), rect.getY(), Integer.parseInt(temp[i].split("=")[1]), 1);
						} else if(temp[i].startsWith("height")) {
							rect = new Rectangle2D.Double(rect.getX(), rect.getY(), rect.getWidth(), Integer.parseInt(temp[i].split("=")[1]));
						}
					}
					
					if(type != null) {
						switch(type) {
						case "Slope":
							switch(name) {
							case "R45":
								load45DegreeSlopeRight(rect);
								break;
							case "RU30":
								load30DegreeSlopeRight(rect);
								break;
							case "RL30":
								loadLower30DegreeSlopeRight(rect);
								break;
							case "L45":
								load45DegreeSlope(rect);
								break;
							case "LU30":
								load30DegreeSlope(rect);
								break;
							case "LL30":
								loadLower30DegreeSlope(rect);
								break;
							}
							break;
						case "Map":
							platforms.add(new Platform(rect, name.replaceAll("_", " ")));
							break;
						}
					} else {
						platforms.add(new Platform(rect));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Platform> getPlatforms() {
		return platforms;
	}
	
	// Rising to left
	public void load45DegreeSlope(Rectangle2D e) {
		for(int x = 0; x < TileSet.tileWidth; x++) {
			platforms.add(new Platform(new Rectangle2D.Double(e.getX() + x, e.getY() + x, 1f, 2f)));
		}
	}

	public void load30DegreeSlope(Rectangle2D e) {
		for(int x = 0; x < TileSet.tileWidth / 2; x++) {
			platforms.add(new Platform(new Rectangle2D.Double(e.getX() + (2f * x), e.getY() + x, 2f, 2f)));
			//System.out.println("Upper " + platforms.get(platforms.size() - 1).getBox().getY());
		}
	}
	
	public void loadLower30DegreeSlope(Rectangle2D e) {
		for(int x = 0; x < TileSet.tileWidth / 2; x++) {
			platforms.add(new Platform(new Rectangle2D.Double(e.getX() + (2f * x), e.getCenterY() + x, 2f, 2f)));
			//System.out.println("Lower " + platforms.get(platforms.size() - 1).getBox().getY());
		}
	}

	// Rising to right
	// TODO This is wrong
	public void load45DegreeSlopeRight(Rectangle2D e) {
		for(int x = 0; x < e.getWidth() / 2; x++) {
			platforms.add(new Platform(new Rectangle2D.Double(e.getX() + ((e.getWidth() / (TileSet.tileWidth / 2)) * x), 
					e.getMaxY() - ((e.getHeight() / (TileSet.tileHeight / 2)) * x), (e.getWidth() / (TileSet.tileWidth / 2)),
					((e.getHeight() / (TileSet.tileHeight / 2)) * x))));
		}
	}

	public void load30DegreeSlopeRight(Rectangle2D e) {
		for(int x = 0; x < e.getHeight() / 4; x++) {
			platforms.add(new Platform(new Rectangle2D.Double(e.getX() + ((e.getWidth() / (TileSet.tileWidth / 4)) * x), 
					e.getCenterY() - (((e.getHeight() / 4) / (TileSet.tileHeight / 8)) * x), (e.getWidth() / (TileSet.tileWidth / 4)),
					(((e.getHeight() / 4) / (TileSet.tileHeight / 8)) * x))));
		}
	}

	public void loadLower30DegreeSlopeRight(Rectangle2D e) {
		for(int x = 0; x < e.getHeight() / 4; x++) {
			platforms.add(new Platform(new Rectangle2D.Double(e.getX() + ((e.getWidth() / (TileSet.tileWidth / 4)) * x), 
					e.getMaxY() - (((e.getHeight() / 4) / (TileSet.tileHeight / 8)) * x), (e.getWidth() / (TileSet.tileWidth / 4)),
					(((e.getHeight() / 4) / (TileSet.tileHeight / 8)) * x))));
		}
	}

}
