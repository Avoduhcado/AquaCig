package core.utility;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import core.tiled.Platform;
import core.world.TileSet;

public class PropMap {

	private static final int tileSize = TileSet.tileWidth;
	private static HashMap<Point, ArrayList<Rectangle2D>> rectMap = new HashMap<Point, ArrayList<Rectangle2D>>();
	private static HashMap<Point, ArrayList<Platform>> platformMap = new HashMap<Point, ArrayList<Platform>>();
	
	/*public static void populateMap(ArrayList<Entity> props) {
		for(int i = 0; i<props.size(); i++) {
			Point p = new Point((int) (props.get(i).getX() / tileSize), (int) (props.get(i).getY() / tileSize));
			if(propMap.containsKey(p)) {
				propMap.get(p).add((Platform) props.get(i));
			} else {
				propMap.put(p, new ArrayList<Platform>(Arrays.asList((Platform) props.get(i))));
			}
			
			Point p2 = new Point((int) (props.get(i).getBox().getMaxX() / tileSize), (int) (props.get(i).getBox().getMaxY() / tileSize));
			if(p2.x > p.x) {
				for(int j = p.x; j<=p2.x; j++) {
					// Top Row
					if(propMap.containsKey(new Point(j, p.y))) {
						propMap.get(new Point(j, p.y)).add((Platform) props.get(i));
					} else {
						propMap.put(new Point(j, p.y), new ArrayList<Platform>(Arrays.asList((Platform) props.get(i))));
					}
					// Bottom Row
					if(propMap.containsKey(new Point(j, p2.y))) {
						propMap.get(new Point(j, p2.y)).add((Platform) props.get(i));
					} else {
						propMap.put(new Point(j, p2.y), new ArrayList<Platform>(Arrays.asList((Platform) props.get(i))));
					}
				}
			}
			if(p2.y > p.y) {
				for(int j = p.y; j<=p2.y; j++) {
					// Left Side
					if(propMap.containsKey(new Point(p.x, j))) {
						propMap.get(new Point(p.x, j)).add((Platform) props.get(i));
					} else {
						propMap.put(new Point(p.x, j), new ArrayList<Platform>(Arrays.asList((Platform) props.get(i))));
					}
					// Right Side
					if(propMap.containsKey(new Point(p2.x, j))) {
						propMap.get(new Point(p2.x, j)).add((Platform) props.get(i));
					} else {
						propMap.put(new Point(p2.x, j), new ArrayList<Platform>(Arrays.asList((Platform) props.get(i))));
					}
				}
			}
		}
		
		Iterator<Entry<Point, ArrayList<Platform>>> i = getMap().entrySet().iterator();
		while(i.hasNext()) {
			ArrayList<Platform> p = i.next().getValue();
			for(int x = 0; x<p.size(); x++) {
				System.out.println(p.get(x).getBox());
			}
		}
	}*/
	
	public static void populatePlatforms(ArrayList<Platform> platforms) {
		for(int i = 0; i<platforms.size(); i++) {
			Point p = new Point((int) (platforms.get(i).getBox().getX() / tileSize), (int) (platforms.get(i).getBox().getY() / tileSize));
			if(platformMap.containsKey(p)) {
				platformMap.get(p).add(platforms.get(i));
			} else {
				platformMap.put(p, new ArrayList<Platform>(Arrays.asList(platforms.get(i))));
			}
			
			Point p2 = new Point((int) (platforms.get(i).getBox().getMaxX() / tileSize), (int) (platforms.get(i).getBox().getMaxY() / tileSize));
			if(p2.x > p.x) {
				for(int j = p.x; j<=p2.x; j++) {
					// Top Row
					if(platformMap.containsKey(new Point(j, p.y))) {
						platformMap.get(new Point(j, p.y)).add(platforms.get(i));
					} else {
						platformMap.put(new Point(j, p.y), new ArrayList<Platform>(Arrays.asList(platforms.get(i))));
					}
					// Bottom Row
					if(platformMap.containsKey(new Point(j, p2.y))) {
						platformMap.get(new Point(j, p2.y)).add(platforms.get(i));
					} else {
						platformMap.put(new Point(j, p2.y), new ArrayList<Platform>(Arrays.asList(platforms.get(i))));
					}
				}
			}
			if(p2.y > p.y) {
				for(int j = p.y; j<=p2.y; j++) {
					// Left Side
					if(platformMap.containsKey(new Point(p.x, j))) {
						platformMap.get(new Point(p.x, j)).add(platforms.get(i));
					} else {
						platformMap.put(new Point(p.x, j), new ArrayList<Platform>(Arrays.asList(platforms.get(i))));
					}
					// Right Side
					if(platformMap.containsKey(new Point(p2.x, j))) {
						platformMap.get(new Point(p2.x, j)).add(platforms.get(i));
					} else {
						platformMap.put(new Point(p2.x, j), new ArrayList<Platform>(Arrays.asList(platforms.get(i))));
					}
				}
			}
		}
	}
	
	public static void populateRectangles(ArrayList<Rectangle2D> rects) {
		for(int i = 0; i<rects.size(); i++) {
			Point p = new Point((int) (rects.get(i).getX() / tileSize), (int) (rects.get(i).getY() / tileSize));
			if(rectMap.containsKey(p)) {
				rectMap.get(p).add(rects.get(i));
			} else {
				rectMap.put(p, new ArrayList<Rectangle2D>(Arrays.asList(rects.get(i))));
			}
			
			Point p2 = new Point((int) (rects.get(i).getMaxX() / tileSize), (int) (rects.get(i).getMaxY() / tileSize));
			if(p2.x > p.x) {
				for(int j = p.x; j<=p2.x; j++) {
					// Top Row
					if(rectMap.containsKey(new Point(j, p.y))) {
						rectMap.get(new Point(j, p.y)).add(rects.get(i));
					} else {
						rectMap.put(new Point(j, p.y), new ArrayList<Rectangle2D>(Arrays.asList(rects.get(i))));
					}
					// Bottom Row
					if(rectMap.containsKey(new Point(j, p2.y))) {
						rectMap.get(new Point(j, p2.y)).add(rects.get(i));
					} else {
						rectMap.put(new Point(j, p2.y), new ArrayList<Rectangle2D>(Arrays.asList(rects.get(i))));
					}
				}
			}
			if(p2.y > p.y) {
				for(int j = p.y; j<=p2.y; j++) {
					// Left Side
					if(rectMap.containsKey(new Point(p.x, j))) {
						rectMap.get(new Point(p.x, j)).add(rects.get(i));
					} else {
						rectMap.put(new Point(p.x, j), new ArrayList<Rectangle2D>(Arrays.asList(rects.get(i))));
					}
					// Right Side
					if(rectMap.containsKey(new Point(p2.x, j))) {
						rectMap.get(new Point(p2.x, j)).add(rects.get(i));
					} else {
						rectMap.put(new Point(p2.x, j), new ArrayList<Rectangle2D>(Arrays.asList(rects.get(i))));
					}
				}
			}
		}
	}
	
	public static HashMap<Point, ArrayList<Rectangle2D>> getMap() {
		return rectMap;
	}
	
	public static HashMap<Point, ArrayList<Platform>> getPlatformMap() {
		return platformMap;
	}
	
	public static ArrayList<Platform> getPlatformSector(Point p) {
		if(platformMap.containsKey(p)) {
			return platformMap.get(p);
		} else {
			return new ArrayList<Platform>();
		}
	}
	
	public static ArrayList<Platform> getPlatformSectors(Rectangle2D r) {
		ArrayList<Platform> platforms = new ArrayList<Platform>();
		
		Point p = new Point((int) (r.getX() / tileSize), (int) (r.getY() / tileSize));
		if(platformMap.containsKey(p)) {
			platforms.addAll(platformMap.get(p));
		}
		
		Point p2 = new Point((int) (r.getMaxX() / tileSize), (int) (r.getMaxY() / tileSize));
		if(p.x < p2.x) {
			for(int j = p.x; j<=p2.x; j++) {
				// Top Row
				if(platformMap.containsKey(new Point(j, p.y))) {
					platforms.addAll(platformMap.get(new Point(j, p.y)));
				}
				// Bottom Row
				if(platformMap.containsKey(new Point(j, p2.y))) {
					platforms.addAll(platformMap.get(new Point(j, p2.y)));
				}
			}
		}
		if(p.y < p2.y) {
			for(int j = p.y; j<=p2.y; j++) {
				// Left Side
				if(platformMap.containsKey(new Point(p.x, j))) {
					platforms.addAll(platformMap.get(new Point(p.x, j)));
				}
				// Right Side
				if(platformMap.containsKey(new Point(p2.x, j))) {
					platforms.addAll(platformMap.get(new Point(p2.x, j)));
				}
			}
		}
		
		return platforms;
	}
	
	public static ArrayList<Rectangle2D> getMapSector(Point p) {
		if(rectMap.containsKey(p)) {
			//System.out.println(p + " " + propMap.containsKey(p));
			return rectMap.get(p);
		} else {
			return new ArrayList<Rectangle2D>();
		}
	}
	
	public static ArrayList<Rectangle2D> getMapSectors(Rectangle2D r) {
		ArrayList<Rectangle2D> platforms = new ArrayList<Rectangle2D>();
		
		Point p = new Point((int) (r.getX() / tileSize), (int) (r.getY() / tileSize));
		if(rectMap.containsKey(p)) {
			platforms.addAll(rectMap.get(p));
		}
		
		Point p2 = new Point((int) (r.getMaxX() / tileSize), (int) (r.getMaxY() / tileSize));
		if(p.x < p2.x) {
			for(int j = p.x; j<=p2.x; j++) {
				// Top Row
				if(getMap().containsKey(new Point(j, p.y))) {
					platforms.addAll(rectMap.get(new Point(j, p.y)));
				}
				// Bottom Row
				if(getMap().containsKey(new Point(j, p2.y))) {
					platforms.addAll(rectMap.get(new Point(j, p2.y)));
				}
			}
		}
		if(p.y < p2.y) {
			for(int j = p.y; j<=p2.y; j++) {
				// Left Side
				if(getMap().containsKey(new Point(p.x, j))) {
					platforms.addAll(rectMap.get(new Point(p.x, j)));
				}
				// Right Side
				if(getMap().containsKey(new Point(p2.x, j))) {
					platforms.addAll(rectMap.get(new Point(p2.x, j)));
				}
			}
		}
		
		return platforms;
	}
	
}
