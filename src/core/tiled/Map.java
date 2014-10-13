package core.tiled;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import core.utility.PropMap;

public class Map {

	private int width;
	private int height;
	private String tileSet;
	private ArrayList<TileLayer> tileLayers = new ArrayList<TileLayer>();
	private ObjectLayer objectLayer;
	
	public Map(String ref) {
		try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/scenes/" + ref))){
			String line;
			String[] temp;
			while((line = reader.readLine()) != null) {
				line = line.replaceAll("<|>|/>", "").trim();
				line = line.replaceAll("\"", "");
				System.out.println(line);
				
				if(line.startsWith("map")) {
					temp = line.split(" ");
					width = Integer.parseInt(temp[4].split("=")[1]);
					height = Integer.parseInt(temp[5].split("=")[1]);
				} else if(line.startsWith("image")) {
					temp = line.split("=");
					tileSet = temp[1].substring(temp[1].lastIndexOf("/") + 1, temp[1].indexOf(".png"));
				} else if(line.startsWith("layer")) {
					tileLayers.add(new TileLayer(reader, tileSet));
				} else if(line.startsWith("objectgroup")) {
					objectLayer = new ObjectLayer(reader);
					PropMap.populatePlatforms(objectLayer.getPlatforms());
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
	
	public ArrayList<TileLayer> getTileLayers() {
		return tileLayers;
	}
	
	public TileLayer getTileLayer(int x) {
		return tileLayers.get(x);
	}
	
	public ObjectLayer getObjectLayer() {
		return objectLayer;
	}
	
}
