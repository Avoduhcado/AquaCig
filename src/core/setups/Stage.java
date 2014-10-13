package core.setups;

import core.Camera;
import core.entities.Actor;
import core.entities.Entity;
import core.tiled.TileLayer;
import core.world.Map;

public class Stage {

	public static float gravity = 1.75f;
	
	public Actor player;
	public Map map;
	public core.tiled.Map map2;
	
	//public static ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public Stage() {
		map2 = new core.tiled.Map("JASON.tmx");
		
		player = new Actor(100, -100, new String[]{"Spess_4_2.png", "SpessWalk_4_2.png",
				"SpessWalk_4_2.png", "SpessJump_1_2.png", "SpessFall_1_2.png"});
				
		//map = new Map("Test map.txt");
		/*for(int x = 0; x<map.getCollision().getTiles().size(); x++) {
			String[] plat = map.getCollision().getTiles().get(x).split(",");
			entities.add(new Platform(Float.parseFloat(plat[0]), Float.parseFloat(plat[1]), plat[2], 1f, 1f));
			if(plat[2].contains("40") || plat[2].contains("41") || plat[2].contains("57")) {
				((Platform) entities.get(entities.size() - 1)).setSlope(true);
				System.out.println("sdfsdf");
			}
		}
		System.out.println("Loaded collisions");
		PropMap.populateMap(entities);
		System.out.println("Initialized hitboxes");
		for(int x = 0; x<map.getBackgroundLayer(0).getTiles().size(); x++) {
			String[] plat = map.getBackgroundLayer(0).getTiles().get(x).split(",");
			entities.add(0, new Platform(Float.parseFloat(plat[0]), Float.parseFloat(plat[1]), plat[2], 1f, 1f));
		}
		System.out.println("Loaded background");
		entities.add(player);
		for(int x = 0; x<map.getForegroundLayer(0).getTiles().size(); x++) {
			String[] plat = map.getForegroundLayer(0).getTiles().get(x).split(",");
			entities.add(new Platform(Float.parseFloat(plat[0]), Float.parseFloat(plat[1]), plat[2], 1f, 1f));
		}
		System.out.println("Loaded foreground");*/
		
		Camera.get().focus = player;
	}
	
	public void update() {
		for(TileLayer layer : map2.getTileLayers()) {
			for(Entity e : layer.getTiles()) {
				e.update();
			}
		}
		player.update();
		/*for(Entity e : map.getBackgroundLayer(0).getTiles())
			e.update();
		for(Entity e : map.getCollision().getTiles())
			e.update();
		player.update();
		for(Entity e : map.getForegroundLayer(0).getTiles())
			e.update();*/
	}
	
	public void draw() {
		for(TileLayer layer : map2.getTileLayers()) {
			for(Entity e : layer.getTiles()) {
				e.draw();
			}
		}
		player.draw();
		/*for(Entity e : map.getBackgroundLayer(0).getTiles())
			e.draw();
		for(Entity e : map.getCollision().getTiles())
			e.draw();
		player.draw();
		for(Entity e : map.getForegroundLayer(0).getTiles())
			e.draw();*/
	}
	
}
