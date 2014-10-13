package core.entities;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import core.render.Model2D;

public class Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1295877438003755581L;
	protected Model2D model;
	protected Rectangle2D box;
		
	public Entity(float x, float y, String ref) {
		this.model = new Model2D(new String[]{ref});
		this.model.translate(x, y, 0);
		this.box = new Rectangle2D.Double(x, y, this.model.getWidth(), this.model.getHeight());
	}
	
	public void update() {		
		model.update();
	}
	
	public void draw() {
		model.draw();
	}

	public void updateBox() {
		this.box = new Rectangle2D.Double(model.modelPos.x, model.modelPos.y, model.getWidth(), model.getHeight());
	}
	
	public Rectangle2D getBox() {
		return box;
	}
	
	public void setBox(Rectangle2D box) {
		this.box = box;
	}
	
	public float getX() {
		return model.modelPos.x;
	}
	
	public void setX(float x) {
		this.model.modelPos.x = x;
	}
	
	public float getY() {
		return model.modelPos.y;
	}
	
	public void setY(float y) {
		this.model.modelPos.y = y;
	}
	
}
