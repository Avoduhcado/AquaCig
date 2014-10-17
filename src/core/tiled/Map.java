package core.tiled;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import core.utility.PropMap;

public class Map {

	private int width;
	private int height;
	private String tileSet;
	private ArrayList<TileLayer> backgroundLayer = new ArrayList<TileLayer>();
	private ArrayList<TileLayer> foregroundLayer = new ArrayList<TileLayer>();
	private ObjectLayer collisionLayer;
	private ObjectLayer propLayer;
	private ObjectLayer mapLayer;
	
	public Map(String ref) {
		loadMap(ref, 0);
	}
	
	public Map(String ref, float yOffset) {
		loadMap(ref, yOffset);
	}
	
	public void loadMap(String ref, float yOffset) {
		try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/scenes/" + ref))){
			String line;
			String[] temp;
			while((line = reader.readLine()) != null) {
				line = line.replaceAll("<|>|/>", "").trim();
				line = line.replaceAll("\"", "");
				//System.out.println(line);
				
				if(line.startsWith("map")) {
					temp = line.split(" ");
					width = Integer.parseInt(temp[4].split("=")[1]);
					height = Integer.parseInt(temp[5].split("=")[1]);
				} else if(line.startsWith("image")) {
					temp = line.split("=");
					tileSet = temp[1].substring(temp[1].lastIndexOf("/") + 1, temp[1].indexOf(".png"));
				} else if(line.startsWith("layer")) {
					temp = line.split("=");
					if(temp[1].startsWith("Foreground")) {
						foregroundLayer.add(new TileLayer(reader, tileSet, new Point2D.Float(0, yOffset)));
					} else {
						backgroundLayer.add(new TileLayer(reader, tileSet, new Point2D.Float(0, yOffset)));
					}
				} else if(line.startsWith("objectgroup")) {
					temp = line.split("=");
					if(temp[1].startsWith("Collision")) {
						collisionLayer = new ObjectLayer(reader, 0, yOffset);
						PropMap.populatePlatforms(collisionLayer.getPlatforms());
					} else if(temp[1].startsWith("Prop")) {
						propLayer = new ObjectLayer(reader, 0, yOffset);
					} else if(temp[1].startsWith("Map")) {
						mapLayer = new ObjectLayer(reader, 0, yOffset);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public String getTileSet() {
		return tileSet;
	}

	public ArrayList<TileLayer> getBackgroundLayers() {
		return backgroundLayer;
	}
	
	public TileLayer getBackgroundLayer(int x) {
		return backgroundLayer.get(x);
	}
	
	public ArrayList<TileLayer> getForegroundLayers() {
		return foregroundLayer;
	}
	
	public TileLayer getForegroundLayer(int x) {
		return foregroundLayer.get(x);
	}
	
	public ObjectLayer getCollisionLayer() {
		return collisionLayer;
	}
	
	public ObjectLayer getPropLayer() {
		return propLayer;
	}
	
	public ObjectLayer getMapLayer() {
		return mapLayer;
	}
	
}
