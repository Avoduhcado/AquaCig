package core.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import core.entities.Entity;

public class Layer {

	protected String name;
	protected ArrayList<Entity> tiles = new ArrayList<Entity>();
	
	public Layer(String name, BufferedReader reader) {
		this.name = name;
		
		loadTiles(reader);
	}
	
	public void loadTiles(BufferedReader reader) {
		try {
			String line = reader.readLine();
			int y = 0;
			while((line = reader.readLine()) != null && !line.matches("")) {
				String[] temp = line.split(",");
				for(int x = 0; x<temp.length; x++) {
					if(!temp[x].matches("0") && !temp[x].matches("1")) {
						tiles.add(new Entity(x * 32, y * 32, name + (Integer.parseInt(temp[x]) - 1) + ".png"));
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
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Entity> getTiles() {
		return tiles;
	}
	
}
