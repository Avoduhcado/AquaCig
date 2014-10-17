package core.tiled;

import java.awt.geom.Rectangle2D;
import org.lwjgl.util.vector.Vector2f;
import core.world.TileSet;

public class Platform {

	private Rectangle2D box;
	private boolean oneWay;
	private String mapName;
	
	public Platform(Rectangle2D box) {
		if(box.getHeight() == 1)
			oneWay = true;
		this.box = box;
	}
	
	public Platform(Rectangle2D box, String mapName) {
		this.box = box;
		this.mapName = mapName;
	}
	
	public boolean intersects(Rectangle2D entity, Vector2f velocity) {
		if(entity.intersects(box)) {
			if(oneWay && entity.getMaxY() <= box.getMaxY() && velocity.y > 0)
				return true;
			else if(oneWay) {
				return false;
			} else {
				return true;
			}
		}
		
		return false;
	}
	
	public Rectangle2D getBox() {
		return box;
	}
	
	public boolean isStep() {
		return (getBox().getWidth() < TileSet.tileWidth && getBox().getWidth() > 1);
	}
	
	public boolean canStep(Rectangle2D entity) {
		return (entity.getMaxY() - this.box.getY() < 2);
	}

	public boolean canJumpThrough(Rectangle2D entity, Vector2f velocity) {
		if(this.isOneWay()) {
			if(velocity.y > 0 && entity.getMaxY() - 1 < this.box.getY())
				return false;
			else if(velocity.y > 0)
				return true;
		}
		
		return false;
	}
	
	public boolean isOneWay() {
		return oneWay;
	}
	
	public void setOneWay(boolean oneWay) {
		this.oneWay = oneWay;
	}
	
	public String getMapName() {
		return mapName;
	}
	
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
}
