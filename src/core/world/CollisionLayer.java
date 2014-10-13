package core.world;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import core.entities.Entity;
import core.entities.Platform;
import core.utility.PropMap;

public class CollisionLayer extends Layer {

	public CollisionLayer(String name, BufferedReader reader) {
		super(name, reader);
		
		ArrayList<Rectangle2D> rects = new ArrayList<Rectangle2D>();
		for(Entity e : getTiles()) {
			if(((Platform) e).isSlope()) {
				switch(((Platform) e).getSlope()) {
				case 1:
					rects.addAll(load45DegreeSlope(e.getBox()));
					break;
				case 2:
					rects.addAll(load30DegreeSlope(e.getBox()));
					break;
				case 3:
					rects.addAll(loadLower30DegreeSlope(e.getBox()));
					break;
				case 4:
					rects.addAll(load45DegreeSlopeRight(e.getBox()));
					break;
				case 5:
					rects.addAll(load30DegreeSlopeRight(e.getBox()));
					break;
				case 6:
					rects.addAll(loadLower30DegreeSlopeRight(e.getBox()));
					break;
				}
			} else if(((Platform) e).isOneWay()) {
				rects.add(new Rectangle2D.Double(e.getX(), e.getY(), e.getBox().getWidth(), 1));
			} else {
				rects.add(e.getBox());
			}
		}
		PropMap.populateRectangles(rects);
		//PropMap.populateMap(getTiles());
	}

	@Override
	public void loadTiles(BufferedReader reader) {
		try {
			String line = reader.readLine();
			int y = 0;
			while((line = reader.readLine()) != null && !line.matches("")) {
				String[] temp = line.split(",");
				for(int x = 0; x<temp.length; x++) {
					if(!temp[x].matches("0") && !temp[x].matches("1")) {
						tiles.add(new Platform(x * 32, y * 32, name + (Integer.parseInt(temp[x]) - 1) + ".png", 1f, 1f));
						if(Integer.parseInt(temp[x]) - 1 == 40) {
							((Platform) tiles.get(tiles.size() - 1)).setSlope(32, 16);
						} else if(Integer.parseInt(temp[x]) - 1 == 41) {
							((Platform) tiles.get(tiles.size() - 1)).setSlope(16, 0);
						} else if(Integer.parseInt(temp[x]) - 1 == 57) {
							((Platform) tiles.get(tiles.size() - 1)).setSlope(32, 0);
						} else if(Integer.parseInt(temp[x]) - 1 == 38) {
							((Platform) tiles.get(tiles.size() - 1)).setSlope(16, 32);
						} else if(Integer.parseInt(temp[x]) - 1 == 37) {
							((Platform) tiles.get(tiles.size() - 1)).setSlope(0, 16);
						} else if(Integer.parseInt(temp[x]) - 1 == 50) {
							((Platform) tiles.get(tiles.size() - 1)).setSlope(0, 32);
						}
						
						// One way platform
						if(Integer.parseInt(temp[x]) - 1 == 14) {
							((Platform) tiles.get(tiles.size() - 1)).setOneWay(true);
						}
					}
				}
				y++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("The scene file with layer: " + name + " has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Scene file with layer: " + name + " failed to load!");
			e.printStackTrace();
		}
	}
	
	// Rising to left
	public ArrayList<Rectangle2D> load45DegreeSlope(Rectangle2D e) {
		ArrayList<Rectangle2D> rects = new ArrayList<Rectangle2D>();
		for(int x = 0; x < e.getWidth() / 2; x++) {
			rects.add(new Rectangle2D.Double(e.getX() + ((e.getWidth() / (TileSet.tileWidth / 2)) * x), 
					e.getY() + ((e.getHeight() / (TileSet.tileHeight / 2)) * x), (e.getWidth() / (TileSet.tileWidth / 2)),
					e.getHeight() - ((e.getHeight() / (TileSet.tileHeight / 2)) * x)));
		}
		
		return rects;
	}
	
	public ArrayList<Rectangle2D> load30DegreeSlope(Rectangle2D e) {
		ArrayList<Rectangle2D> rects = new ArrayList<Rectangle2D>();
		for(int x = 0; x < e.getWidth() / 4; x++) {
			rects.add(new Rectangle2D.Double(e.getX() + ((e.getWidth() / (TileSet.tileWidth / 4)) * x), 
					e.getY() + (((e.getHeight() / 4) / (TileSet.tileHeight / 8)) * x), (e.getWidth() / (TileSet.tileWidth / 4)),
					e.getHeight() - (((e.getHeight() / 4) / (TileSet.tileHeight / 8)) * x)));
		}
		
		return rects;
	}
	
	public ArrayList<Rectangle2D> loadLower30DegreeSlope(Rectangle2D e) {
		ArrayList<Rectangle2D> rects = new ArrayList<Rectangle2D>();
		for(int x = 0; x < e.getHeight() / 4; x++) {
			rects.add(new Rectangle2D.Double(e.getX() + ((e.getWidth() / (TileSet.tileWidth / 4)) * x), 
					e.getCenterY() + (((e.getHeight() / 4) / (TileSet.tileHeight / 8)) * x), (e.getWidth() / (TileSet.tileWidth / 4)),
					e.getHeight() - (((e.getHeight() / 4) / (TileSet.tileHeight / 8)) * x)));
		}
		
		return rects;
	}
	
	// Rising to right
	public ArrayList<Rectangle2D> load45DegreeSlopeRight(Rectangle2D e) {
		ArrayList<Rectangle2D> rects = new ArrayList<Rectangle2D>();
		for(int x = 0; x < e.getWidth() / 2; x++) {
			rects.add(new Rectangle2D.Double(e.getX() + ((e.getWidth() / (TileSet.tileWidth / 2)) * x), 
					e.getMaxY() - ((e.getHeight() / (TileSet.tileHeight / 2)) * x), (e.getWidth() / (TileSet.tileWidth / 2)),
					((e.getHeight() / (TileSet.tileHeight / 2)) * x)));
		}
		
		return rects;
	}
	
	public ArrayList<Rectangle2D> load30DegreeSlopeRight(Rectangle2D e) {
		ArrayList<Rectangle2D> rects = new ArrayList<Rectangle2D>();
		for(int x = 0; x < e.getHeight() / 4; x++) {
			rects.add(new Rectangle2D.Double(e.getX() + ((e.getWidth() / (TileSet.tileWidth / 4)) * x), 
					e.getCenterY() - (((e.getHeight() / 4) / (TileSet.tileHeight / 8)) * x), (e.getWidth() / (TileSet.tileWidth / 4)),
					(((e.getHeight() / 4) / (TileSet.tileHeight / 8)) * x)));
		}
		
		return rects;
	}
	
	public ArrayList<Rectangle2D> loadLower30DegreeSlopeRight(Rectangle2D e) {
		ArrayList<Rectangle2D> rects = new ArrayList<Rectangle2D>();
		for(int x = 0; x < e.getHeight() / 4; x++) {
			rects.add(new Rectangle2D.Double(e.getX() + ((e.getWidth() / (TileSet.tileWidth / 4)) * x), 
					e.getMaxY() - (((e.getHeight() / 4) / (TileSet.tileHeight / 8)) * x), (e.getWidth() / (TileSet.tileWidth / 4)),
					(((e.getHeight() / 4) / (TileSet.tileHeight / 8)) * x)));
		}
		
		return rects;
	}
}
